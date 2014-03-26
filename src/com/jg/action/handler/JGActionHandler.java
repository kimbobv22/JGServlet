package com.jg.action.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jg.action.JGAction;
import com.jg.action.handler.JGService.JGResultDef;
import com.jg.action.handler.JGServiceMap.JGActionClassDef;
import com.jg.action.handler.JGServiceMap.JGResultPageDef;
import com.jg.log.JGLog;
import com.jg.main.JGKeyword;
import com.jg.util.JGFileUtils;
import com.jg.util.JGReflectionUtils;
import com.jg.util.JGStringUtils;

public class JGActionHandler{
	
	static private JGActionHandler _sharedHandler = null;
	static public JGActionHandler sharedHandler() throws Exception{
		if(_sharedHandler == null){
			synchronized(JGActionHandler.class){
				_sharedHandler = new JGActionHandler();
			}
		}
		return _sharedHandler;
	}
	
	static protected String _XMLDirectoryPath = null;
	static public void setXMLDirectoryPath(String path_){
		_XMLDirectoryPath = path_;
	}
	static public String getXMLDirectoryPath(){
		return _XMLDirectoryPath;
	}
	
	HashMap<String, JGServiceMap> _serviceMaps = new HashMap<String, JGServiceMap>();
	
	public int sizeOfServiceMaps(){
		return _serviceMaps.size();
	}
	
	protected void setServiceMap(String mapName_, JGServiceMap serviceMap_){
		_serviceMaps.put(mapName_, serviceMap_);
	}
	protected void removeServiceMap(String mapName_){
		_serviceMaps.remove(mapName_);
	}
	public JGServiceMap getServiceMap(String mapName_){
		return _serviceMaps.get(mapName_);
	}
	public JGServiceMap getPrimaryServiceMap(){
		Iterator<String> keyIterator_ = _serviceMaps.keySet().iterator();
		while(keyIterator_.hasNext()){
			String mapName_ = keyIterator_.next();
			JGServiceMap serviceMap_ = getServiceMap(mapName_);
			if(serviceMap_.isPrimary()){
				return serviceMap_;
			}
		}
		
		return null;
	}
	
	protected ArrayList<JGServiceFilter> _filters = new ArrayList<JGServiceFilter>();
	public int sizeOfFilters(){
		return _filters.size();
	}
	public JGServiceFilter getFilter(int index_){
		return _filters.get(index_);
	}
	protected void addFilter(JGServiceFilter filter_){
		_filters.add(filter_);
	}
	
	public JGActionHandler() throws Exception{
		reload();
	}
	
	public void reload() throws Exception{
		_serviceMaps.clear();
		_searchXMLDirectory(_XMLDirectoryPath);
	}
	
	private void _searchXMLDirectory(String filePath_) throws Exception{
		try{
			File targetDirectory_ = new File(filePath_);
			File[] fileList_ = targetDirectory_.listFiles();
			int fileCount_ = fileList_.length;
			for(int index_=0;index_<fileCount_;++index_){
				File targetFile_ = fileList_[index_];
				String childPath_ = targetFile_.getCanonicalPath();
				if(targetFile_.isFile() && targetFile_.getName().endsWith(".xml")){
					_addServiceMapFromXML(JGFileUtils.removeSuffix(JGFileUtils.convertToRelativePath(_XMLDirectoryPath, childPath_)), childPath_);
				}else if(targetFile_.isDirectory()){
					_searchXMLDirectory(childPath_);
				}
			}
		}catch(Exception ex_){
			throw new Exception("failed to read the web service XML files", ex_);
		}
	}
	
	private void _addServiceMapFromXML(String mapName_, String childPath_) throws Exception{
		try{
			JGLog.log(9,"Loaded service, map name : "+mapName_);
			
			//parse service map
			Document rootDocument_ = new SAXBuilder().build(new File(childPath_));
			Element serviceMapElement_ = rootDocument_.getRootElement();
			
			JGServiceMap serviceMap_ = new JGServiceMap(JGStringUtils.getBoolean(serviceMapElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_ISPRIMARY), false));
			
			//action classes
			Element actionClassesElement_ = serviceMapElement_.getChild(JGKeyword.STR_ELEMENT_ACTIONCLASSES);
			if(actionClassesElement_ != null){
				List<?> actionClassList_ = actionClassesElement_.getChildren(JGKeyword.STR_ELEMENT_CLASS);
				int actionClassCount_ = actionClassList_.size();
				for(int index_=0;index_<actionClassCount_;++index_){
					Element actionClassElement_ = (Element)actionClassList_.get(index_);
					
					serviceMap_.addActionClassDef(
							actionClassElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_NAME)
							,actionClassElement_.getValue()
						);
				}
			}
			
			JGLog.log(9," - Loaded action classes, total : "+serviceMap_.sizeOfActionClassDefs());
			
			//result pages
			Element resultPagesElement_ = serviceMapElement_.getChild(JGKeyword.STR_ELEMENT_RESULTPAGES);
			if(resultPagesElement_ != null){
				List<?> resultPageList_ = resultPagesElement_.getChildren(JGKeyword.STR_ELEMENT_PAGE);
				int resultPageCount_ = resultPageList_.size();
				for(int index_=0;index_<resultPageCount_;++index_){
					Element resultPageElement_ = (Element)resultPageList_.get(index_);
					
					serviceMap_.addResultPageDef(
							resultPageElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_NAME)
							,resultPageElement_.getValue()
						);
				}
			}
			
			JGLog.log(9," - Loaded result pages, total : "+serviceMap_.sizeOfResultPageDefs());
			
			//filters
			Element filtersElement_ = serviceMapElement_.getChild(JGKeyword.STR_ELEMENT_FILTERS);
			if(filtersElement_ != null){
				List<?> filters_ = filtersElement_.getChildren(JGKeyword.STR_ELEMENT_FILTER);
				if(filters_ != null){
					int filterCount_ = filters_.size();
					for(int index_=0;index_<filterCount_;++index_){
						Element filterElement_ = (Element)filters_.get(index_);
						
						String fullKey_ = filterElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_SERVICEID);
						boolean isLocalFilter_ = JGStringUtils.getBoolean(filterElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_LOCALFILTER), true);
						
						JGServiceKey serviceKey_ = null;
						if(JGServiceKey.isFullKey(fullKey_)){
							serviceKey_ = new JGServiceKey(fullKey_);
						}else{
							serviceKey_ = new JGServiceKey(mapName_, fullKey_);
						}
						
						addFilter(new JGServiceFilter(serviceKey_, isLocalFilter_));
					}	
				}
			}
			
			JGLog.log(9," - Loaded filters, total : "+sizeOfFilters());
			
			//services
			List<?> serviceList_ = serviceMapElement_.getChildren(JGKeyword.STR_ELEMENT_SERVICE);
			if(serviceMapElement_ != null){
				int serviceCount_ = serviceList_.size();
				for(int index_=0;index_<serviceCount_;++index_){
					Element serviceElement_ = (Element)serviceList_.get(index_);
					
					boolean isPrivate_ = JGStringUtils.getBoolean(serviceElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_ISPRIVATE), false);
					boolean isPrimary_ = JGStringUtils.getBoolean(serviceElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_ISPRIMARY), false);
					
					JGService service_ = new JGService(
							new JGServiceKey(mapName_, serviceElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_SERVICEID))
							,serviceElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_ACTIONCLASSNAME)
							,serviceElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_MAPPINGMETHOD)
							,serviceElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_FSERVICEID)
							,isPrivate_, isPrimary_
						);
					
					//add result to service
					List<?> resultList_ = serviceElement_.getChildren(JGKeyword.STR_ELEMENT_RESULT);
					int resultCount_ = resultList_.size();
					for(int rIndex_=0;rIndex_<resultCount_;++rIndex_){
						Element resultElement_ = (Element)resultList_.get(rIndex_);
						
						int code_ = 0;
						try{
							code_ = Integer.valueOf(resultElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_CODE)).intValue(); 
						}catch(Exception ex_){}
						String pageValue_ = resultElement_.getValue();
						
						boolean isRedirect_ = false;
						try{
							isRedirect_ = Boolean.valueOf(resultElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_ISREDIRECT)).booleanValue();
						}catch(Exception ex_){}
						
						service_.addResultDef(
								code_
								,resultElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_PAGENAME)
								,pageValue_
								,resultElement_.getAttributeValue(JGKeyword.STR_ATTRIBUTE_SERVICEID)
								,isRedirect_
							);
					}
					
					serviceMap_.addService(service_);
				}
			}
			JGLog.log(9," - Loaded services, total : "+serviceMap_.sizeOfService());
			
			_serviceMaps.put(mapName_, serviceMap_);
			
		}catch(Exception ex_){
			throw new Exception("failed to read web service XML file",ex_);
		}
	}
	
	public JGService getService(JGServiceKey serviceKey_,boolean checkPrimary_) throws Exception{
		JGServiceMap serviceMap_ = null;
		if(checkPrimary_ && serviceKey_._mapName == null){
			JGServiceMap pServiceMap_ = getPrimaryServiceMap();
			if(pServiceMap_ == null){
				throw new Exception("Service mapName is null");
			}
			serviceMap_ = pServiceMap_;
		}else{
			serviceMap_ = getServiceMap(serviceKey_.getMapName());
			if(serviceMap_ == null){
				throw new Exception("can't not find Service map : "+serviceKey_._mapName);
			}
		}
		
		JGService service_ = null;
		if(checkPrimary_ && serviceKey_._serviceID == null){
			JGService pService_ = serviceMap_.getPrimaryService();
			if(pService_ == null){
				throw new Exception("Service key is null");
			}
			service_ = pService_;
		}else{
			service_ = serviceMap_.getService(serviceKey_.getServiceID());
		}
		
		return service_;
	}
	public JGService getService(String mapName_, String serviceID_) throws Exception{
		return getService(new JGServiceKey(mapName_, serviceID_),false);
	}
	public JGService getPrimaryService() throws Exception{
		JGServiceMap serviceMap_ = getPrimaryServiceMap();
		if(serviceMap_ == null){
			return null;
		}
		
		return serviceMap_.getPrimaryService();
	}
	
	protected String convertStringByMappingRegex(String targetString_, JGServiceBox serviceBox_) throws Exception{
		//convert mapping paramters
		Pattern paramterPattern_ = Pattern.compile(JGKeyword.STR_REGEX_MAPPING_PARAMETER);
		Matcher paramterPatternMatcher_ = paramterPattern_.matcher(targetString_);
		
		while(paramterPatternMatcher_.find()){
			String matchedStr_ = paramterPatternMatcher_.group();
			String keyName_ = matchedStr_.substring(2,matchedStr_.length()-1);
			try{
				targetString_ = targetString_.replaceAll("\\$\\{("+keyName_+")\\}", serviceBox_.getParameter(keyName_));
			}catch(NullPointerException ex_){
				throw new Exception("Couldn't find mapped parameter value",ex_);
			}
		}
		
		//convert mapping attributes
		Pattern attributePattern_ = Pattern.compile(JGKeyword.STR_REGEX_MAPPING_ATTRIBUTE);
		Matcher attributePatternMatcher_ = attributePattern_.matcher(targetString_);
		
		while(attributePatternMatcher_.find()){
			String matchedStr_ = attributePatternMatcher_.group();
			String keyName_ = matchedStr_.substring(2,matchedStr_.length()-1);
			try{
				targetString_ = targetString_.replaceAll("\\#\\{("+keyName_+")\\}", (String)serviceBox_.getAttribute(keyName_));
			}catch(NullPointerException ex_){
				throw new Exception("Couldn't find mapped attribute value",ex_);
			}
		}
		
		return targetString_;
	}
	
	private Object _handleService(JGServiceBox serviceBox_, JGService service_) throws Exception{
		Object resultObject_ = null;
		
		String actionClassName_ = service_.getActionClassName();
		JGServiceKey fServiceKey_ = service_.getForwardServiceKey();
		if(actionClassName_ != null){
			resultObject_ = _processAction(serviceBox_, service_);
		}else if(fServiceKey_ != null){
			resultObject_ = _handleService(serviceBox_, getService(fServiceKey_,false));
		}
		
		return resultObject_;
	}
	
	protected void handleService(JGServiceBox serviceBox_, JGService service_, boolean isSubService_, boolean doResult_) throws Exception{
		if(!isSubService_){
			if(service_.isPrivate())
				throw new Exception("Can't not access private service : "+serviceBox_.getRequestServiceKey().toString());
			
			int sizeOfFilters_ = sizeOfFilters();
			for(int fIndex_=0;fIndex_<sizeOfFilters_;++fIndex_){
				JGServiceFilter filter_ = _filters.get(fIndex_);
				filter_.doFilter(this, serviceBox_);
			}
		}
		
		Object resultObject_ = _handleService(serviceBox_, service_);
		
		if(!doResult_) return;
		
		int resultCode_ = 0;
		if(resultObject_ != null){
			resultCode_ = (Integer)resultObject_;
		}
		
		//handle result
		int resultIndex_ = service_.indexOfResultDefByCode(resultCode_);
		if(resultIndex_ >= 0){
			JGResultDef resultDef_ = service_.getResultDef(resultIndex_);
			String fServiceID_ = resultDef_.getForwardServiceID();
			
			//forwarding to service
			if(!JGStringUtils.isBlank(fServiceID_)){
				fServiceID_ = convertStringByMappingRegex(fServiceID_, serviceBox_);
				
				if(!JGServiceKey.isFullKey(fServiceID_)){
					handleService(serviceBox_, service_.getParentMap().getService(fServiceID_),true);
				}else{
					handleService(serviceBox_, getService(new JGServiceKey(fServiceID_),false),true);
				}
			}
			//forwarding to result page
			else{
				String page_ = resultDef_.getPage();
				String pageName_ = resultDef_.getPageName();
				
				if(JGStringUtils.isBlank(page_)){
					JGResultPageDef resultPageDef_ = service_.getResultPageDefFromParentMap(pageName_);
					if(resultPageDef_ == null){
						throw new Exception("Can't find result rage : "+pageName_);
					}
					page_ = resultPageDef_.getPage();
				}
				
				page_ = convertStringByMappingRegex(page_, serviceBox_);
				
				if(resultDef_.isRedirect()){
					serviceBox_.getResponse().sendRedirect(page_);
				}else{
					HttpServletRequest request_ = serviceBox_.getRequest();
					RequestDispatcher requestDispatcher_ = request_.getRequestDispatcher(page_);
					requestDispatcher_.forward(request_, serviceBox_.getResponse());
				}
			}
		}
	}
	
	public void handleService(JGServiceBox serviceBox_, JGService service_, boolean isSubService_) throws Exception{
		handleService(serviceBox_, service_, isSubService_, true);
	}
	protected void handleService(JGServiceBox serviceBox_, boolean isSubService_, boolean doResult_) throws Exception{
		handleService(serviceBox_, getService(serviceBox_.getRequestServiceKey(), false), isSubService_, doResult_);
	}
	protected void handleService(JGServiceBox serviceBox_, boolean isSubService_) throws Exception{
		handleService(serviceBox_, isSubService_, true);
	}
	protected void handleService(JGServiceBox serviceBox_, JGServiceKey serviceKey_, boolean isSubService_, boolean doResult_) throws Exception{
		handleService(new JGServiceBox(serviceBox_.getRequest(), serviceBox_.getResponse(), serviceKey_), isSubService_, doResult_);
	}
	protected void handleService(JGServiceBox serviceBox_, JGServiceKey serviceKey_, boolean isSubService_) throws Exception{
		handleService(serviceBox_, serviceKey_, isSubService_, true);
	}
	
	protected JGAction makeAction(JGService service_) throws Exception{
		if(service_ == null){
			throw new NullPointerException("Servie can't be null");
		}
		
		JGActionClassDef actionClassDef_ = service_.getActionClassFromParentMap();
		Class<?> actionClass_ = Class.forName(actionClassDef_._mappingClass);
		
		return (JGAction)actionClass_.newInstance();
	}
	protected JGAction makeAction(JGServiceKey serviceKey_) throws Exception{
		return makeAction(getService(serviceKey_,false));
	}
	private Object _processAction(JGServiceBox serviceBox_, JGService service_) throws Exception{
		JGAction action_ = makeAction(service_);
		return JGReflectionUtils.invokeMethod(action_, action_.getClass().getSuperclass()
				, "process", new Object[]{service_, serviceBox_});
	}
}

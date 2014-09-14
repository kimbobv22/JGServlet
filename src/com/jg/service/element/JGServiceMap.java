package com.jg.service.element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jg.util.JGFileUtils;
import com.jg.util.JGStringUtils;

public class JGServiceMap extends JGDirectory{
	
	static public final String STR_ELEMENT_SERVICEMAP = "serviceMap";
	static public final String STR_ELEMENT_SERVICE = "service";
	static public final String STR_ELEMENT_ACTIONCLASSES = "actionClasses";
	static public final String STR_ELEMENT_CLASS = "class";
	static public final String STR_ELEMENT_RESULTPAGES = "resultPages";
	static public final String STR_ELEMENT_PAGE = "page";
	
	static public final String STR_ATTRIBUTE_ISPRIMARY = "isPrimary";
	
	protected boolean _isPrimary = false;
	public boolean isPrimary(){
		return _isPrimary;
	}
	protected void setPrimary(boolean isPrimary_){
		_isPrimary = isPrimary_;
	}
	
	protected JGVirtualDirectory _virtualDirectory = null;
	public JGVirtualDirectory getVirtualDirectory(){
		return _virtualDirectory;
	}
	protected void setVirtualDirectory(JGVirtualDirectory virtualDirectoy_){
		_virtualDirectory = virtualDirectoy_;
	}
	
	public JGServiceMap(String name_, boolean isPrimary_){
		super(name_, JGDirectoryType.Map);
		_isPrimary = isPrimary_;
	}
	
	public void addChild(JGDirectory directory_){
		throw new RuntimeException("can't add directory to service map");
	}
	
	@Override
	protected boolean test(String path_){
		if(_virtualDirectory != null){
			return _virtualDirectory.test(path_);
		}else{
			return super.test(path_);
		}
	}
	
	protected ArrayList<JGActionClass> _actionClasses = new ArrayList<JGActionClass>();
	public int countOfActionClasses(){
		return _actionClasses.size();
	}
	
	protected void addActionClass(JGActionClass actionClass_){
		int index_ = indexOfActionClass(actionClass_);
		if(index_ >= 0) return;
		_actionClasses.add(actionClass_);
	}
	public JGActionClass getActionClass(int index_){
		return _actionClasses.get(index_);
	}
	public JGActionClass getActionClass(String actionClassName_){
		int index_ = indexOfActionClass(actionClassName_);
		if(index_ < 0) return null;
		return getActionClass(index_);
	}
	
	public int indexOfActionClass(String actionClassName_){
		int size_ = _actionClasses.size();
		for(int index_=0;index_<size_;++index_){
			JGActionClass actionClass_ = _actionClasses.get(index_);
			if(actionClass_._name.equals(actionClassName_))
				return index_;
		}
		
		return -1;
	}
	public int indexOfActionClass(JGActionClass actionClass_){
		return indexOfActionClass(actionClass_._name);
	}
	
	protected ArrayList<JGService> _services = new ArrayList<JGService>();
	public int countOfService(){
		return _services.size();
	}
	
	protected void addService(JGService service_) throws Exception{
		int index_ = indexOfService(service_.getPattern());
		if(index_ >= 0)
			throw new Exception("service duplicated");
		
		_services.add(service_);
	}
	public JGService getService(int index_){
		return (JGService)_services.get(index_);
	}
	public JGService getPrimaryService(){
		int length_ = _services.size();
		for(int index_=0;index_<length_;++index_){
			JGService service_ = _services.get(index_);
			if(service_._isPrimary)
				return service_;
		}
		
		return null;
	}
	
	public JGService findService(String pattern_){
		int index_ = indexOfService(pattern_);
		if(index_ < 0) return null;
		return getService(index_);
	}
	public int indexOfService(String pattern_){
		int size_ = _services.size();
		for(int index_=0;index_<size_;++index_){
			JGService service_ = _services.get(index_);
			if(service_.test(pattern_))
				return index_;
		}
		
		return -1;
	}
	
	protected ArrayList<JGResultPage> _resultPages = new ArrayList<JGResultPage>();
	public int countOfResultPages(){
		return _resultPages.size();
	}
	
	protected void addResultPage(JGResultPage resultPage_){
		int index_ = indexOfResultPage(resultPage_._pageName);
		if(index_ >= 0) throw new RuntimeException("can't add result page duplicated");
		_resultPages.add(resultPage_);
	}
	
	public JGResultPage getResultPage(int index_){
		return _resultPages.get(index_);
	}
	public JGResultPage getResultPage(String pageName_){
		int index_ = indexOfResultPage(pageName_);
		if(index_ < 0) return null;
		return getResultPage(index_);
	}
	public int indexOfResultPage(String pageName_){
		int size_ = _resultPages.size();
		for(int index_=0;index_<size_;++index_){
			JGResultPage resultPage_ = _resultPages.get(index_);
			if(resultPage_._pageName.equals(pageName_))
				return index_;
		}
		return -1;
	}
	
	public static JGServiceMap make(File file_) throws Exception{
		if(file_ == null || !file_.isFile())
			throw new IllegalStateException("invalid service map file");
		
		Document document_ = new SAXBuilder().build(file_);
		Element rootElement_ = document_.getRootElement();
		
		if(!rootElement_.getName().equals(STR_ELEMENT_SERVICEMAP))
			throw new IllegalStateException("invalid service map element name");
		
		JGServiceMap serviceMap_ = new JGServiceMap(JGFileUtils.removeSuffix(file_.getName())
				,JGStringUtils.getBoolean(rootElement_.getAttributeValue(STR_ATTRIBUTE_ISPRIMARY), false));
		
		//add virtual directory
		Element virtualDirectoryElement_ = rootElement_.getChild(JGVirtualDirectory.STR_ELEMENT_VIRTUALDIRECTORY);
		if(virtualDirectoryElement_ != null){
			serviceMap_.setVirtualDirectory(JGVirtualDirectory.make(virtualDirectoryElement_));
		}
		
		//add action classes
		Element actionClassesElement_ = rootElement_.getChild(STR_ELEMENT_ACTIONCLASSES);
		if(actionClassesElement_ != null){
			List<?> actionClassList_ = actionClassesElement_.getChildren(STR_ELEMENT_CLASS);
			int actionClassCount_ = actionClassList_.size();
			for(int index_=0;index_<actionClassCount_;++index_){
				Element actionClassElement_ = (Element)actionClassList_.get(index_);
				JGActionClass actionClass_ = JGActionClass.make(actionClassElement_);
				serviceMap_.addActionClass(actionClass_);
			}
		}
		
		//add result pages
		Element resultPagesElement_ = rootElement_.getChild(STR_ELEMENT_RESULTPAGES);
		if(resultPagesElement_ != null){
			List<?> resultPageElements_ = resultPagesElement_.getChildren(STR_ELEMENT_PAGE);
			int countOfResultPageElements_ = resultPageElements_.size();
			for(int rpIndex_= 0;rpIndex_<countOfResultPageElements_;++rpIndex_){
				Element resultPageElement_ = (Element)resultPageElements_.get(rpIndex_);
				JGResultPage resultPage_ = JGResultPage.make(resultPageElement_);
				serviceMap_.addResultPage(resultPage_);
			}
		}
		
		//add services
		List<?> servicesElement_ = rootElement_.getChildren(STR_ELEMENT_SERVICE);
		int countOfServices_ = servicesElement_.size();
		for(int sIndex_=0;sIndex_<countOfServices_;++sIndex_){
			Element serviceElement_ = (Element)servicesElement_.get(sIndex_);
			JGService service_ = JGService.make(serviceElement_);
			serviceMap_.addService(service_);
		}
		
		return serviceMap_;
	}
	public static JGServiceMap make(String filePath_) throws Exception{
		return make(new File(filePath_));
	}
}

package com.jg.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jg.main.JGMainConfig;
import com.jg.service.element.JGAction;
import com.jg.service.element.JGDirectory;
import com.jg.service.element.JGDirectoryAnalysis;
import com.jg.service.element.JGResult;
import com.jg.service.element.JGResultPage;
import com.jg.service.element.JGService;
import com.jg.service.element.JGServiceFilter;
import com.jg.service.element.JGServiceMap;
import com.jg.service.element.JGVirtualDirectory;
import com.jg.service.exception.JGActionProcessException;
import com.jg.service.exception.JGDirectoryNotFoundException;
import com.jg.service.exception.JGInvalidServiceException;
import com.jg.service.exception.JGRequestDeniedException;
import com.jg.service.exception.JGResultNotDefinedException;
import com.jg.service.exception.JGResultNotFoundException;
import com.jg.service.exception.JGResultPageNotFoundException;
import com.jg.service.exception.JGServiceNotFoundException;
import com.jg.util.JGFileUtils;

public class JGServiceHandler{
	
	static public final String STR_FILENAME_JGVIRTUAL = "_jgvirtual";
	
	static private JGServiceHandler _sharedHandler = null;
	static public JGServiceHandler sharedHandler(){
		if(_sharedHandler == null){
			synchronized(JGServiceHandler.class){
				if(_sharedHandler == null){
					try{
						_sharedHandler = new JGServiceHandler();
					}catch(Exception ex_){
						throw new RuntimeException("failed to load JGServiceHandler", ex_);
					}
				}
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
	
	protected JGDirectory _rootDirectory = new JGDirectory(JGMainConfig.sharedConfig().getKeySeparator());
	public JGDirectory getRootDirectory(){
		return _rootDirectory;
	}
	
	protected ArrayList<JGServiceFilter> _serviceFilters = new ArrayList<JGServiceFilter>();
	public int countOfFilters(){
		return _serviceFilters.size();
	}
	
	public void addFilter(JGServiceFilter serviceFilter_){
		_serviceFilters.add(serviceFilter_);
	}
	public JGServiceFilter getFilter(int index_){
		return _serviceFilters.get(index_);
	}
	
	public JGServiceHandler() throws Exception{
		reload();
	}
	
	public void reload() throws Exception{
		if(_XMLDirectoryPath == null)
			throw new NullPointerException("XML Directory path is null");
		
		_recursiveParseXML(_rootDirectory, _XMLDirectoryPath);
	}
	
	private void _recursiveParseXML(JGDirectory parent_, String path_) throws Exception{
		File targetDirectory_ = new File(path_);
		final String keySeparator_ = JGMainConfig.sharedConfig().getKeySeparator();
		
		if(!targetDirectory_.isDirectory())
			throw new IllegalAccessException("search path must be a Directory, "+path_);
		
		File[] vXmlCheckList_ = targetDirectory_.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file_, String name_){
				return JGFileUtils.removeSuffix(name_).equalsIgnoreCase(STR_FILENAME_JGVIRTUAL);
			}
		});
		
		boolean isRootParse_ = false;
		if(_rootDirectory == parent_ && vXmlCheckList_.length > 0){ // do virtualize
			JGVirtualDirectory virtualDirectory_ = JGVirtualDirectory.make(vXmlCheckList_[0]);
			parent_.addChild(virtualDirectory_);
			parent_ = virtualDirectory_;
			isRootParse_ = true;
		}
		
		File[] xmlFilesList_ = targetDirectory_.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file_, String name_){
				return (!JGFileUtils.removeSuffix(name_).equalsIgnoreCase(STR_FILENAME_JGVIRTUAL) && "xml".equalsIgnoreCase(JGFileUtils.getSuffix(name_)));
			}
		});
		
		int countOfXmlFiles_ = xmlFilesList_.length;
		for(int xmlIndex_ = 0;xmlIndex_<countOfXmlFiles_;++xmlIndex_){
			JGServiceMap serviceMap_ = JGServiceMap.make(xmlFilesList_[xmlIndex_]);
			parent_.addChild(serviceMap_);
		}
		
		File[] directoriesList_ = targetDirectory_.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File file_, String name_){
				try{
					return new File(file_.getCanonicalPath()+keySeparator_+name_).isDirectory();
				}catch(IOException ex_){
					return false;
				}
			}
		});
		int countOfDirectories_ = directoriesList_.length;
		for(int dIndex_=0;dIndex_<countOfDirectories_;++dIndex_){
			File directoryFile_ = directoriesList_[dIndex_];
			JGDirectory directory_ = new JGDirectory(directoryFile_.getName());
			parent_.addChild(directory_);
			_recursiveParseXML(directory_, directoryFile_.getAbsolutePath());
		}
		
		if(!isRootParse_ && vXmlCheckList_.length > 0){ // do virtualize
			JGVirtualDirectory virtualDirectory_ = JGVirtualDirectory.make(vXmlCheckList_[0]);
			JGDirectory pParent_ = parent_.getParent();
			parent_.moveChildrenTo(virtualDirectory_);
			pParent_.addChild(virtualDirectory_);
		}
	}
	
	public Object handleService(JGServiceBox serviceBox_, String basePath_, String servicePath_, boolean onlyResult_) throws Exception{
		File servicePathFile_ = new File(basePath_, servicePath_);
		servicePath_ = servicePathFile_.getCanonicalPath();
		
		JGDirectoryAnalysis directoryAnalysis_ = _rootDirectory.analyze(servicePath_);
		JGDirectory lastDirectory_ = directoryAnalysis_.getLastDirectory();
		JGServiceMap findedServiceMap_ = directoryAnalysis_.getFindedServiceMap();
		JGService findedService_ = directoryAnalysis_.getFindedService();
		
		if(lastDirectory_ == null)
			throw new JGDirectoryNotFoundException("failed to find directory", servicePath_);
		
		if(findedService_ == null)
			throw new JGServiceNotFoundException(servicePath_);
		
		if(basePath_.equals(JGMainConfig.sharedConfig().getKeySeparator())){
			if(findedService_.isPrivate())
				throw new JGRequestDeniedException("can't not access private service",servicePath_,findedService_);
			
			serviceBox_._virtualDirectoryData = directoryAnalysis_.getVirtualDirectoryData();
			
			int countOfFilters_ = countOfFilters();
			for(int fIndex_=0;fIndex_<countOfFilters_;++fIndex_){
				JGServiceFilter serviceFilter_ = getFilter(fIndex_);
				if(serviceFilter_.acceptFilter(serviceBox_))
					serviceFilter_.doFilter(serviceBox_);
			}
		}
		
		String actionClassName_ = findedService_.getActionClassName();
		String mappingMethod_ = findedService_.getMappingMethod();
		String forwardPath_ = findedService_.getForwardPath();		
		
		boolean isAction_ = (actionClassName_ != null && mappingMethod_ != null);
		boolean isForwardPath_ = (forwardPath_ != null);
		
		Object resultObject_ = null;
		JGResult otherResult_ = findedService_.getOtherResult();
		
		if(isAction_){
			JGAction targetAction_ = null;
			try{
				targetAction_ = JGAction.make(findedServiceMap_.getActionClass(actionClassName_).getClassPath());
			}catch(Exception ex_){
				throw new JGActionProcessException("failed to make action", servicePath_, actionClassName_, mappingMethod_, ex_);
			}
			
			try{
				resultObject_ = targetAction_.process(mappingMethod_, serviceBox_);
			}catch(Exception ex_){
				throw new JGActionProcessException("action error occurred", servicePath_, actionClassName_, mappingMethod_, ex_.getCause());
			}
			
			if(onlyResult_)
				return resultObject_;
			
		}else if(isForwardPath_){
			resultObject_ = handleService(serviceBox_, directoryAnalysis_.getDirectoryPath() , _convertMappingRegex(forwardPath_, serviceBox_), true);
		}else if(otherResult_ == null){
			throw new JGInvalidServiceException("invalid service",servicePath_, actionClassName_, mappingMethod_, forwardPath_);
		}
		
		int resultCode_ = 0;
		JGResult result_ = null;
		
		if(resultObject_ != null){
			resultCode_ = (Integer)resultObject_;
			result_ = findedService_.getResultWithCode(resultCode_);
			
			if(result_ == null && otherResult_ == null)
				throw new JGResultNotFoundException("failed to find result", servicePath_, resultCode_);
		}
		
		if(result_ == null)
			result_ = otherResult_;
		
		if(result_ != null){
			String rPage_ = result_.getPage();
			String rPageName_ = result_.getPageName();
			String rForwardPath_ = result_.getForwardPath();
			
			if(rPageName_ != null){
				JGResultPage resultPage_ = findedServiceMap_.getResultPage(rPageName_);
				if(resultPage_ == null)
					throw new JGResultPageNotFoundException("failed to find result page", servicePath_, resultCode_, rPageName_);
				
				rPage_ = resultPage_.getPage();
			}
			
			if(rPage_ != null){
				rPage_ = _convertMappingRegex(rPage_, serviceBox_);
				if(result_.isRedirect()) serviceBox_.redirect(rPage_);
				else serviceBox_.forward(rPage_);
				return resultObject_;
			}else if(rForwardPath_ != null){
				return handleService(serviceBox_, directoryAnalysis_.getDirectoryPath(), _convertMappingRegex(rForwardPath_, serviceBox_));
			}else throw new JGResultNotDefinedException("result not defined", servicePath_, resultCode_);
		}
		
		return null;
	}
	public Object handleService(JGServiceBox serviceBox_, String basePath_, String servicePath_) throws Exception{
		return handleService(serviceBox_, basePath_, servicePath_, false);
	}
	
	private String _convertMappingRegex(String destination_, JGServiceBox serviceBox_) throws Exception{
		Pattern pattern_ = Pattern.compile("\\#\\{[\\w\\-\\_]{1,}\\}");
		Matcher matcher_ = pattern_.matcher(destination_);
		
		while(matcher_.find()){
			String matchedStr_ = matcher_.group();
			String parameterName_ = matchedStr_.substring(2,matchedStr_.length()-1);
			destination_ = destination_.replaceAll("\\#\\{"+parameterName_+"\\}", serviceBox_.getParameter(parameterName_));
		}
		
		pattern_ = Pattern.compile("\\#v\\{[\\w\\-\\_]{1,}\\}");
		matcher_ = pattern_.matcher(destination_);
		
		while(matcher_.find()){
			String matchedStr_ = matcher_.group();
			String vDataName_ = matchedStr_.substring(2,matchedStr_.length()-1);
			destination_ = destination_.replaceAll("\\#v\\{"+vDataName_+"\\}", serviceBox_.getVirtualDirectoryData(vDataName_));
		}
		
		return destination_;
	}
}

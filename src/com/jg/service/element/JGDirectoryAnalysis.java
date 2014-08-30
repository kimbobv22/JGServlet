package com.jg.service.element;

import java.util.HashMap;

import com.jg.service.element.JGDirectory.JGDirectoryType;



public class JGDirectoryAnalysis{
	
	protected JGDirectory _lastDirectory;
	protected void setLastDirectory(JGDirectory directory_){
		_lastDirectory = directory_;
	}
	public JGDirectory getLastDirectory(){
		return _lastDirectory;
	}
	
	public boolean didFindServiceMap(){
		return (_lastDirectory != null && _lastDirectory._directoryType == JGDirectoryType.Map);
	}
	public JGServiceMap getFindedServiceMap(){
		if(didFindServiceMap())
			return (JGServiceMap)_lastDirectory;
		else return null;
	}
	
	protected JGService _findedService = null;
	protected void setFindedService(JGService service_){
		_findedService = service_;
	}
	public JGService getFindedService(){
		return _findedService;
	}
	
	protected HashMap<String, String> _virtualDirectoryData;
	public HashMap<String, String> getVirtualDirectoryData(){
		return _virtualDirectoryData;
	}
	
	protected JGDirectoryAnalysis(JGDirectory lastDirectory_, JGService findedService_, HashMap<String, String> virtualDirectoryData_){
		_lastDirectory = lastDirectory_;
		_findedService = findedService_;
		_virtualDirectoryData = virtualDirectoryData_;
	}
}

package com.jg.service.exception;

public class JGInvalidServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	protected String _path;
	public String getPath(){
		return _path;
	}
	
	protected String _actionClassName;
	public String getActionClassName(){
		return _actionClassName;
	}
	
	protected String _mappingMethod;
	public String getMappingMethod(){
		return _mappingMethod;
	}
	
	protected String _forwardPath;
	public String getForwardPath(){
		return _forwardPath;
	}
	
	public JGInvalidServiceException(String message_, String path_, String actionClassName_, String mappingMethod_, String forwardPath_){
		super(message_);
		_path = path_;
		_actionClassName = actionClassName_;
		_mappingMethod = mappingMethod_;
		_forwardPath = forwardPath_;
	}
}

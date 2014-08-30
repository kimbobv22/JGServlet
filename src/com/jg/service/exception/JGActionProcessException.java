package com.jg.service.exception;


public class JGActionProcessException extends Exception{

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
	
	public JGActionProcessException(String message_, String path_, String actionClassName_, String mappingMethod_, Throwable throw_){
		super(message_, throw_);
		_path = path_;
		_actionClassName = actionClassName_;
		_mappingMethod = mappingMethod_;
	}
}

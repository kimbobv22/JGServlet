package com.jg.service.exception;

public class JGServiceNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	protected String _path = null;
	public String getPath(){
		return _path;
	}
	
	public JGServiceNotFoundException(String path_){
		_path = path_;
	}
}

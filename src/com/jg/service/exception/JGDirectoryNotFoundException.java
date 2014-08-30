package com.jg.service.exception;


public class JGDirectoryNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	
	protected String _path = null;
	public String getPath(){
		return _path;
	}
	
	public JGDirectoryNotFoundException(String message_, String path_) throws Exception{
		super(message_);
		_path = path_;
	}
}

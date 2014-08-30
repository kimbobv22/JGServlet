package com.jg.service.exception;

import com.jg.service.element.JGService;

public class JGRequestDeniedException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String _path = null;
	public String getPath(){
		return _path;
	}
	
	protected JGService _service = null;
	public JGService getService(){
		return _service;
	}
	
	public JGRequestDeniedException(String message_, String path_, JGService service_){
		super(message_);
		_path = path_;
		_service = service_;
	}
}

package com.jg.service.exception;


public class JGResultPageNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	protected int _receivedResultCode = -1;
	public int getReceivedResultCode(){
		return _receivedResultCode;
	}
	
	protected String _path = null;
	public String getPath(){
		return _path;
	}
	
	protected String _pageName = null;
	public String getPageName(){
		return _pageName;
	}
	
	public JGResultPageNotFoundException(String message_, String path_, int receivedResultCode_, String pageName_){
		super(message_);
		_path = path_;
		_receivedResultCode = receivedResultCode_;
		_pageName = pageName_;
	}
	public JGResultPageNotFoundException(String message_, String path_, int receivedResultCode_){
		this(message_,path_,receivedResultCode_, null);
	}
}

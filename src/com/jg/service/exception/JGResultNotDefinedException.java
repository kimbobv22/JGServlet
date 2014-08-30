package com.jg.service.exception;

public class JGResultNotDefinedException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	protected String _path = null;
	public String getPath(){
		return _path;
	}
	
	protected int _receivedResultCode = -1;
	public int getReceivedResultCode(){
		return _receivedResultCode;
	}
		
	public JGResultNotDefinedException(String message_, String path_, int receivedResultCode_){
		super(message_);
		_path = path_;
		_receivedResultCode = receivedResultCode_;
	}
}

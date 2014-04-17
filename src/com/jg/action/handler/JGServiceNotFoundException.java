package com.jg.action.handler;

public class JGServiceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	protected JGServiceKey _serviceKey = null;
	public JGServiceKey getServiceKey(){
		return _serviceKey;
	}

	public JGServiceNotFoundException(JGServiceKey serviceKey_, String message_){
		super(message_);
		_serviceKey = serviceKey_;
	}
}

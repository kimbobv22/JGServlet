package com.jg.action;

import com.jg.action.handler.JGServiceKey;

public class JGActionException extends Exception{

	private static final long serialVersionUID = 1L;
	
	protected JGServiceKey _serviceKey = null;
	public JGServiceKey getServiceKey(){
		return _serviceKey;
	}
	
	public JGActionException(String message_, Throwable throw_, JGServiceKey serviceKey_){
		super(message_, throw_);
		this._serviceKey = serviceKey_;
	}
}

package com.jg.action.handler;

import javax.servlet.http.HttpServletRequest;

import com.jg.main.JGKeyword;
import com.jg.util.JGStringUtils;


public class JGServiceKey {

	protected String _mapName = null;
	public String getMapName(){
		return _mapName;
	}
	
	protected String _serviceID = null;
	public String getServiceID(){
		return _serviceID;
	}
	
	protected JGServiceKey(){}
	public JGServiceKey(String mapName_, String serviceID_){
		_mapName = mapName_;
		_serviceID = serviceID_;
	}
	public JGServiceKey(String fullKey_){
		String[] splitedKeys_ = fullKey_.split(JGKeyword.STR_SERVICEID_SPLITTER_REGX);
		_mapName = splitedKeys_[0];
		_serviceID = splitedKeys_[1];
	}
	
	public boolean equalsWithOtherKey(JGServiceKey otherKey_){
		return _mapName.equals(otherKey_._mapName) && _serviceID.equals(otherKey_._serviceID);
	}
	
	@Override
	public String toString() {
		return _mapName+JGKeyword.STR_SERVICEID_SPLITTER+_serviceID;
	}
	
	static public JGServiceKey makeKey(HttpServletRequest request_){
		String servletPath_ = request_.getContextPath()+request_.getServletPath();
		String requestURI_ = request_.getRequestURI();
		String serviceMap_ = requestURI_.substring(servletPath_.length()+1);
		
		if(serviceMap_.length() == 0){
			serviceMap_ = null;
		}
		
		return new JGServiceKey(serviceMap_, request_.getParameter(JGKeyword.STR_SERVICEID));
	}
	static public boolean isFullKey(String key_){
		return !JGStringUtils.isBlank(key_) && key_.indexOf(JGKeyword.STR_SERVICEID_SPLITTER) >= 0;
	}
}

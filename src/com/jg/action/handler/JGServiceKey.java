package com.jg.action.handler;

import javax.servlet.http.HttpServletRequest;

import com.jg.action.JGActionKeyword;
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
		String[] splitedKeys_ = fullKey_.split(JGActionKeyword.STR_SERVICEID_SPLITTER_REGX);
		_mapName = splitedKeys_[0];
		_serviceID = splitedKeys_[1];
	}
	
	public boolean equalsWithOtherKey(JGServiceKey otherKey_){
		return _mapName.equals(otherKey_._mapName) && _serviceID.equals(otherKey_._serviceID);
	}
	
	@Override
	public String toString() {
		return _mapName+JGActionKeyword.STR_SERVICEID_SPLITTER+_serviceID;
	}
	
	public String makeServiceKeyParameters(){
		return JGServiceKey.makeServiceKeyParameters(_mapName, _serviceID);
	}
	
	static public JGServiceKey makeKey(HttpServletRequest request_){
		return new JGServiceKey(request_.getParameter(JGActionKeyword.STR_SERVICEMAP),request_.getParameter(JGActionKeyword.STR_SERVICEID));
	}
	static public boolean isFullKey(String key_){
		return !JGStringUtils.isBlank(key_) && key_.indexOf(JGActionKeyword.STR_SERVICEID_SPLITTER) >= 0;
	}
	
	static public String makeServiceKeyParameters(String srvMap_, String srvID_){
		return (JGActionKeyword.STR_SERVICEMAP+"="+srvMap_+"&"+JGActionKeyword.STR_SERVICEID+"="+srvID_);
	}
	static public String makeServiceKeyParameters(String fullKey_){
		JGServiceKey serviceKey_ = new JGServiceKey(fullKey_);
		return makeServiceKeyParameters(serviceKey_._mapName, serviceKey_._serviceID);
	}
}

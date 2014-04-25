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
	
	protected String _virtualMap = null;
	
	protected JGServiceKey(){}
	public JGServiceKey(String mapName_, String serviceID_){
		_mapName = mapName_;
		_serviceID = serviceID_;
	}
	
	public boolean equalsWithOtherKey(JGServiceKey otherKey_){
		return _mapName.equals(otherKey_._mapName) && _serviceID.equals(otherKey_._serviceID);
	}
	
	@Override
	public String toString() {
		return _mapName+JGKeyword.STR_SERVICEID_SPLITTER+_serviceID;
	}
	
	static public JGServiceKey makeKey(JGServiceBox serviceBox_) throws Exception{
		HttpServletRequest request_ = serviceBox_.getRequest();
		String servletPath_ = request_.getServletPath().substring(1);
		
		if(servletPath_.length() == 0){
			servletPath_ = null;
		}else{
			int lastIndex_ = servletPath_.lastIndexOf("/");
			if(lastIndex_+1 == servletPath_.length())
				servletPath_ = servletPath_.substring(0,lastIndex_);
		}
		
		if(JGMultipartData.isMultipart(request_)){
			JGMultipartData multipartData_ = serviceBox_.multipartData();
			return new JGServiceKey(servletPath_, multipartData_.getFormFieldValue(JGKeyword.STR_SERVICEID));
		}else return new JGServiceKey(servletPath_, request_.getParameter(JGKeyword.STR_SERVICEID));
	}
	static public JGServiceKey makeKey(String fullKey_){
		if(isFullKey(fullKey_)){
			String[] splitedKeys_ = fullKey_.split(JGKeyword.STR_SERVICEID_SPLITTER_REGX);
			return new JGServiceKey(splitedKeys_[0], splitedKeys_[1]);
		}else if(!JGStringUtils.isBlank(fullKey_) && fullKey_.indexOf("/") >= 0){
			return new JGServiceKey(fullKey_, null);
		}else return new JGServiceKey(null, fullKey_);
	}
	static public boolean isFullKey(String key_){
		return !JGStringUtils.isBlank(key_) && key_.indexOf(JGKeyword.STR_SERVICEID_SPLITTER) >= 0;
	}
}

package com.jg.action.handler;



public class JGServiceFilter{
	
	protected JGServiceKey _serviceKey = null;
	public JGServiceKey getServiceKey(){
		return _serviceKey;
	}
	protected void setServiceKey(JGServiceKey serviceKey_){
		_serviceKey = serviceKey_;
	}
	
	protected boolean _localFilter = true;
	public boolean isLocalFilter(){
		return _localFilter;
	}
	protected void setLocalFilter(boolean isLocalFilter_){
		_localFilter = isLocalFilter_;
	}
	
	protected JGServiceFilter(JGServiceKey serviceKey_, boolean isLocalFilter_){
		setServiceKey(serviceKey_);
		_localFilter = isLocalFilter_;
	}
	
	protected void doFilter(JGServiceHandler actionHandler_, JGServiceBox serviceBox_) throws Exception{
		JGServiceKey tServiceKey_ =  serviceBox_.getRequestServiceKey();
		if(!_localFilter || (_localFilter && tServiceKey_._mapName.equals(_serviceKey._mapName))){
			actionHandler_.handleService(serviceBox_, _serviceKey, false, true, false);
		}
	}
}

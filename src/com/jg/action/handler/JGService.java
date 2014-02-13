package com.jg.action.handler;

import java.util.ArrayList;

import com.jg.action.handler.JGServiceMap.JGActionClassDef;
import com.jg.action.handler.JGServiceMap.JGResultPageDef;
import com.jg.util.JGStringUtils;

public class JGService{
	
	public class JGResultDef{
		protected int _code = 0;
		public int getCode(){
			return _code;
		}
		
		protected String _pageName = null;
		public String getPageName(){
			return _pageName;
		}
		
		protected String _page = null;
		public String getPage(){
			return _page;
		}
		
		protected String _fServiceID = null;
		public String getForwardServiceID(){
			return _fServiceID;
		}
		
		protected boolean _isRedirect = false;
		public boolean isRedirect(){
			return _isRedirect;
		}
		
		public JGResultDef(int code_, String pageName_, String page_, String fServiceID_, boolean isRedirect_){
			_code = code_;
			_pageName = pageName_;
			_page = page_;
			_fServiceID = fServiceID_;
			_isRedirect = isRedirect_;
		}
	}
	
	protected String _actionClassName = null;
	public String getActionClassName(){
		return _actionClassName;
	}
	
	protected String _mappingMethod = null;
	public String getMappingMethod(){
		return _mappingMethod;
	}
	
	protected JGServiceKey _fServiceKey = null;
	public JGServiceKey getForwardServiceKey(){
		return _fServiceKey;
	}
	
	protected boolean _isPrivate = false;
	public boolean isPrivate(){
		return _isPrivate;
	}
	
	protected boolean _isPrimary = false;
	public boolean isPrimary(){
		return _isPrimary;
	}
	
	protected JGServiceKey _serviceKey = null;
	public JGServiceKey getServiceKey(){
		return _serviceKey;
	}
	public boolean equalsWithKey(JGServiceKey key_){
		return _serviceKey.equalsWithOtherKey(key_);
	}
	
	protected JGServiceMap _parentMap = null;
	protected void setParentMap(JGServiceMap serviceMap_){
		_parentMap = serviceMap_;
	}
	public JGServiceMap getParentMap(){
		return _parentMap;
	}
	
	private ArrayList<JGService.JGResultDef> _resultDefs = new ArrayList<JGService.JGResultDef>();
	
	public int sizeOfResultDefs(){
		return _resultDefs.size();
	}
	
	protected void addResultDef(JGResultDef resultDef_){
		int index_ = indexOfResultDefByCode(resultDef_.getCode());
		if(index_ >=0){
			removeResultDef(index_);
		}
		
		_resultDefs.add(resultDef_);
	}
	protected JGResultDef addResultDef(int code_, String pageName_, String page_, String fServiceID_, boolean isRedirect_){
		JGResultDef resultDef_ = new JGResultDef(code_, pageName_, page_, fServiceID_,isRedirect_);
		addResultDef(resultDef_);
		return resultDef_;
	}
	protected JGResultDef addResultDef(int code_, String pageName_, String page_, String fServiceID_){
		return addResultDef(code_, pageName_, page_, fServiceID_, false);
	}
	protected JGResultDef addResultDef(int code_, String pageName_, String page_){
		return addResultDef(code_, pageName_, page_, null);
	}
	
	protected void removeResultDef(int index_){
		_resultDefs.remove(index_);
	}
	protected void removeResultDef(JGResultDef resultDef_){
		_resultDefs.remove(resultDef_);
	}
	protected void removeResultDefByCode(int code_){
		removeResultDef(indexOfResultDefByCode(code_));
	}
	public JGResultDef getResultDef(int index_){
		return _resultDefs.get(index_);
	}
	public JGResultDef getResultDefByCode(int code_){
		return getResultDef(indexOfResultDefByCode(code_));
	}
	
	public int indexOfResultDef(JGResultDef resultDef_){
		return _resultDefs.indexOf(resultDef_);
	}
	public int indexOfResultDefByCode(int code_){
		int count_ = _resultDefs.size();
		for(int index_=0;index_<count_;++index_){
			if(_resultDefs.get(index_).getCode() == code_){
				return index_;
			}
		}
		
		return -1;
	}
	
	public JGActionClassDef getActionClassFromParentMap(){
		return _parentMap.getActionClassDef(_actionClassName);
	}
	public JGResultPageDef getResultPageDefFromParentMap(String name_){
		return _parentMap.getResultPageDef(name_);
	}
	
	public JGService(JGServiceKey serviceKey_, String actionClassName_, String mappingMethod_, String fServiceId_, boolean isPrivate_, boolean isPrimary_){
		_serviceKey = serviceKey_;
		_actionClassName = actionClassName_;
		_mappingMethod = mappingMethod_;
		if(JGServiceKey.isFullKey(fServiceId_)){
			_fServiceKey = new JGServiceKey(fServiceId_);
		}else if(!JGStringUtils.isBlank(fServiceId_)){
			_fServiceKey = new JGServiceKey(_serviceKey._mapName,fServiceId_);
		}
		_isPrivate = isPrivate_;
		_isPrimary = isPrimary_;
	}
}

package com.jg.service.element;

import org.jdom.Element;

import com.jg.util.JGStringUtils;

public class JGResult{
	
	static public String STR_ATTRIBUTE_CODE = "code";
	static public String STR_ATTRIBUTE_PAGENAME = "pageName";
	static public String STR_ATTRIBUTE_FORWARDPATH = "forwardPath";
	static public String STR_ATTRIBUTE_ISREDIRECT = "isRedirect";
	
	protected int _code = 0;
	public int getCode(){
		return _code;
	}
	protected void setCode(int code_){
		_code = code_;
	}
	
	protected String _pageName = null;
	public String getPageName(){
		return _pageName;
	}
	protected void setPageName(String pageName_){
		_pageName = pageName_;
	}
	
	protected String _page = null;
	public String getPage(){
		return _page;
	}
	protected void setPage(String page_){
		_page = page_;
	}
	
	protected String _forwardPath = null;
	public String getForwardPath(){
		return _forwardPath;
	}
	protected void setForwardPath(String forwardPath_){
		_forwardPath = forwardPath_;
	}
	
	protected boolean _isRedirect = false;
	public boolean isRedirect(){
		return _isRedirect;
	}
	public void setRedirect(boolean redirect_){
		_isRedirect = redirect_;
	}
	
	public JGResult(int code_){
		_code = code_;
	}
	
	public static JGResult make(Element element_) throws Exception{
		int code_ = JGStringUtils.getInteger(element_.getAttributeValue(STR_ATTRIBUTE_CODE), 0);
		String pageName_ = element_.getAttributeValue(STR_ATTRIBUTE_PAGENAME);
		String page_ = element_.getValue();
		String forwardPath_ = element_.getAttributeValue(STR_ATTRIBUTE_FORWARDPATH);
		boolean isRedirect_ = JGStringUtils.getBoolean(element_.getAttributeValue(STR_ATTRIBUTE_ISREDIRECT), false);
		
		JGResult result_ = new JGResult(code_);
		result_.setPageName(pageName_);
		result_.setPage(page_);
		result_.setForwardPath(forwardPath_);
		result_.setRedirect(isRedirect_);

		return result_;
	}
}

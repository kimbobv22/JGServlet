package com.jg.service.element;

import org.jdom.Element;

public class JGResultPage{
	
	static public final String STR_ATTRIBUTE_NAME = "name";
	
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
	
	public static JGResultPage make(Element element_) throws Exception{
		String pageName_ = element_.getAttributeValue(STR_ATTRIBUTE_NAME);
		String page_ = element_.getValue();
		
		JGResultPage resultPage_ = new JGResultPage();
		resultPage_.setPageName(pageName_);
		resultPage_.setPage(page_);

		return resultPage_;
	}
}

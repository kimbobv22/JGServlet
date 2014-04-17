package com.jg.action.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JGVirtualMap{
	protected String _pattern = null;
	public String getPattern(){
		return _pattern;
	}
	
	protected JGServiceKey _serviceKey = null;
	public JGServiceKey getServiceKey(){
		return _serviceKey;
	}
	
	protected JGVirtualMap(String pattern_, JGServiceKey serviceKey_){
		_pattern = pattern_;
		_serviceKey = serviceKey_;
	}
	
	public boolean test(String str_){
		Pattern paramterPattern_ = Pattern.compile(_pattern);
		Matcher paramterPatternMatcher_ = paramterPattern_.matcher(str_);
		return paramterPatternMatcher_.find();
	}
}
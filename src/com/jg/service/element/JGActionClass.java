package com.jg.service.element;

import org.jdom.Element;

public class JGActionClass{

	static public String STR_ATTRIBUTE_NAME = "name";
	
	protected String _name = null;
	public String getName(){
		return _name;
	}
	protected void setName(String name_){
		_name = name_;
	}
	
	protected String _classPath = null;
	public String getClassPath(){
		return _classPath;
	}
	protected void setClassPath(String classPath_){
		_classPath = classPath_;
	}
	
	public JGActionClass(String name_, String classPath_){
		_name = name_;
		_classPath = classPath_;
	}
	
	public JGAction makeAction() throws Exception{
		Class<?> actionClass_ = Class.forName(_classPath);
		return (JGAction)actionClass_.newInstance();
	}
	
	static public JGActionClass make(Element element_) throws Exception{
		String actionName_ = element_.getAttributeValue(STR_ATTRIBUTE_NAME);
		String classPath_ = element_.getValue();
		return new JGActionClass(actionName_, classPath_);
	}
}

package com.jg.util;


public class JGCommonUtils {
	
	static public Object NVL(Object value_, Object replaceValue_, Object nullValue_){
		return (value_ != null ? replaceValue_ : nullValue_);
	}
	static public Object NVL(Object value_, Object nullValue_){
		return NVL(value_, value_, nullValue_); 
	}
}

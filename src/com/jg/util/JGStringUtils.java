package com.jg.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JGStringUtils {

	static public String getStringWithStringArray(String[] stringArray_, String connectCharacter_){
		StringBuffer result_ = new StringBuffer();
		int count_ = stringArray_.length;
		for(int index_=0;index_<count_;++index_){
			result_.append(stringArray_[index_]);
			if(index_ < count_-1){
				result_.append(connectCharacter_);
			}
		}
		
		return result_.toString();
	}
	static public String getStringWithStringArray(List<String> stringArray_, String connectCharacter_){
		return getStringWithStringArray((String[])stringArray_.toArray(), connectCharacter_);
	}
	
	
	static public String getDateString(Date date_, String pattern_){
		SimpleDateFormat format_ = new SimpleDateFormat(pattern_);
		return format_.format(date_);
	}
	
	static public boolean getBoolean(String booleanStr_, boolean nvl_){
		try{
			if(!isBlank(booleanStr_) && (booleanStr_.equalsIgnoreCase("true") || booleanStr_.equalsIgnoreCase("false")))
				return Boolean.valueOf(booleanStr_).booleanValue();
			else return nvl_;
		}catch(Exception ex_){return nvl_;}
	}
	static public boolean getBoolean(String booleanStr_){
		return getBoolean(booleanStr_, false);
	}
	
	static public String getEncodedString(String value_) throws UnsupportedEncodingException{
		return new String(value_.getBytes("8859-1"), "euc-kr");
	}
	
	static public boolean isBlank(String value_){
		return (value_ == null || value_.length() == 0);
	}
	
	static public Integer getInteger(String integer_, Integer nvl_){
		try{
			
			if(!isBlank(integer_))
				return Integer.valueOf(integer_);
			else return nvl_;
		}catch(Exception ex_){return nvl_;}
	}
	
	static public Long getLong(String long_, Long nvl_){
		try{
			
			if(!isBlank(long_))
				return Long.valueOf(long_);
			else return nvl_;
		}catch(Exception ex_){return nvl_;}
	}
	
	static public Double getDouble(String double_, Double nvl_){
		try{
			
			if(!isBlank(double_))
				return Double.valueOf(double_);
			else return nvl_;
		}catch(Exception ex_){return nvl_;}
	}
	
	static public Float getFloat(String float_, Float nvl_){
		try{
			
			if(!isBlank(float_))
				return Float.valueOf(float_);
			else return nvl_;
		}catch(Exception ex_){return nvl_;}
	}
}

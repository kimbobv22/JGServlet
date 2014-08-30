package com.jg.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.jg.main.JGMainConfig;

public class JGResponseWriter {
	
	static public final String STR_TEXT_APPLICATION_JSON = "application/json";
	static public final String STR_TEXT_TEXTHTML = "text/html";
	static public final String STR_KEY_RESULT = "result";
	static public final String STR_KEY_MESSAGE = "message";
	
	private HttpServletResponse _response = null;
	private StringBuffer _stringBuffer = new StringBuffer();
	public StringBuffer getStringBuffer(){
		return _stringBuffer;
	}
	
	protected JGResponseWriter(){}
	protected JGResponseWriter(HttpServletResponse response_){
		_response = response_;
	}
	
	public void append(Object object_){
		_stringBuffer.append(object_);
	}
	
	public void print(String contentType_, String charset_) throws Exception{
		_response.setContentType(contentType_+"; charset="+charset_);
		PrintWriter writer_ = null;
		try{
			writer_ = _response.getWriter();
		}catch(IOException ex_){
			new Exception("failed to load HttpWriter", ex_);
		}
		String printString_ = _stringBuffer.toString();
		writer_.print(printString_);
		writer_.close();
		clear();
	}
	public void print(String contentType_) throws Exception{
		print(contentType_,JGMainConfig.sharedConfig().getCharacterEncoding());
	}
	public void print() throws Exception{
		print(STR_TEXT_TEXTHTML);
	}
	
	public void appendAndPrint(Object object_, String contentType_, String charset_) throws Exception{
		append(object_);
		print(contentType_, charset_);
	}
	public void appendAndPrint(Object object_, String contentType_) throws Exception{
		append(object_);
		print(contentType_);
	}
	public void appendAndPrint(Object object_) throws Exception{
		append(object_);
		print();
	}
	
	public void printResultJSON(int resultCode_, Object message_) throws Exception{
		clear();
		HashMap<String, Object> resultMap_ = new HashMap<String, Object>();
		resultMap_.put(STR_KEY_RESULT, resultCode_);
		resultMap_.put(STR_KEY_MESSAGE, message_);
		append(new JSONObject(resultMap_).toJSONString());
		print(STR_TEXT_APPLICATION_JSON);
	}
	public void printResultJSON(int result_) throws Exception{
		printResultJSON(result_,null);
	}
	
	public void clear(){
		_stringBuffer = new StringBuffer();
	}
}

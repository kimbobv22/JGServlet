package com.jg.action.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.jg.main.JGMainConfig;
import com.jg.util.JGEncryptionUtil;

public class JGResponseWriter {
	private HttpServletResponse _response = null;
	private StringBuffer _stringBuffer = new StringBuffer();
	public StringBuffer getStringBuffer(){
		return _stringBuffer;
	}
	
	@SuppressWarnings("unused")
	private JGResponseWriter(){}
	protected JGResponseWriter(HttpServletResponse response_){
		_response = response_;
	}
	
	public void append(Object object_){
		_stringBuffer.append(object_);
	}
	
	public void print(String contentType_, String charset_, boolean doEncrypt_) throws Exception{
		_response.setContentType(contentType_+"; charset="+charset_);
		PrintWriter writer_ = null;
		try{
			writer_ = _response.getWriter();
		}catch(IOException ex_){
			new Exception("failed to load HttpWriter", ex_);
		}
		String printString_ = _stringBuffer.toString();
		if(doEncrypt_){
			try{
				printString_ = JGEncryptionUtil.encrypt(printString_);
			}catch(Exception ex_){
				 throw new Exception("failed to encryption", ex_);
			}
		}
		writer_.print(printString_);
		writer_.close();
		clear();
	}
	
	public void print(String contentType_, String charset_) throws Exception{
		print(contentType_, charset_, JGMainConfig.sharedConfig().isServiceEncryption());
	}
	public void print(String contentType_) throws Exception{
		print(contentType_,JGMainConfig.sharedConfig().getCharacterEncoding());
	}
	public void print() throws Exception{
		print("text/html");
	}
	
	public void appendAndPrint(Object object_, String contentType_, String charset_, boolean doEncrypt_) throws Exception{
		append(object_);
		print(contentType_, charset_, doEncrypt_);
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
		resultMap_.put("result", resultCode_);
		resultMap_.put("message", message_);
		append(new JSONObject(resultMap_).toJSONString());
		print("application/json");
	}
	public void printResultJSON(int result_) throws Exception{
		printResultJSON(result_,null);
	}

	public void clear(){
		_stringBuffer = new StringBuffer();
	}
}

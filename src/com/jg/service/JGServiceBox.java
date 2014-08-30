package com.jg.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.util.JGServletUtils;
import com.jg.vo.JGDataset;

public class JGServiceBox{
	
	protected HttpServletRequest _request = null;
	public HttpServletRequest getRequest(){
		return _request;
	}
	
	protected HttpServletResponse _response = null;
	public HttpServletResponse getResponse(){
		return _response;
	}
	
	protected JGMultipartData _multipartData = null;
	public JGMultipartData getMultipartData() throws Exception{
		if(_multipartData == null){
			_multipartData = new JGMultipartData(this);
		}
		return _multipartData;
	}
	
	protected JGResponseWriter _writer = null;
	public JGResponseWriter writer(){
		return _writer;
	}
	
	protected String _requestPath = null;
	public String getRequestPath(){
		return _requestPath;
	}
	
	protected HashMap<String, String> _virtualDirectoryData = new HashMap<String, String>();
	public int countOfVirtualDirectoryData(){
		return _virtualDirectoryData.size();
	}
	public Set<String> getVirtualDirectoryDataKeySet(){
		return _virtualDirectoryData.keySet();
	}
	public Collection<String> getVirtualDirectoryDataValues(){
		return _virtualDirectoryData.values();
	}
	public String getVirtualDirectoryData(String key_){
		return _virtualDirectoryData.get(key_);
	}
	
	protected JGServiceBox(){}
	public JGServiceBox(HttpServletRequest request_, HttpServletResponse response_, String requestPath_) throws Exception{
		_request = request_;
		_response = response_;
		_writer = new JGResponseWriter(response_);
		_requestPath = requestPath_;
	}
	public JGServiceBox(HttpServletRequest request_, HttpServletResponse response_) throws Exception{
		this(request_,response_, null);
		_requestPath = JGServletUtils.parseServicePath(request_);
	}
	
	public HttpSession getSession(){
		return _request.getSession();
	}
	public String getParameter(String key_) throws Exception{
		return _request.getParameter(key_);
	}
	public JGDataset getParameterAsDataset(String key_) throws Exception{
		return new JGDataset(getParameter(key_));
	}
	
	public void setAttribute(String keyName_, Object value_){
		_request.setAttribute(keyName_, value_);
	}
	public Object getAttribute(String keyName_){
		return _request.getAttribute(keyName_);
	}
	
	public Cookie getCookie(String name_){
		Cookie[] cookies_ = _request.getCookies();
		if(cookies_ == null){
			return null;
		}
		
		int length_ = cookies_.length;
		for(int index_=0;index_<length_;++index_){
			Cookie cookie_ = cookies_[index_];
			if(cookie_.getName().equals(name_)){
				return cookie_;
			}
		}
		return null;
	}
	public Cookie putCookie(String name_, String value_, int expiry_){
		Cookie cookie_ = new Cookie(name_, value_);
		cookie_.setMaxAge(expiry_);
		_response.addCookie(cookie_);
		return cookie_;
	}
	public Cookie putCookie(String name_, String value_){
		return putCookie(name_, value_, -1);
	}
	public void removeCookie(String name_){
		putCookie(name_, null, 0);
	}
	
	public void forward(String url_) throws Exception{
		_request.getRequestDispatcher(url_).forward(_request, _response);
	}
	
	public void redirect(String url_) throws Exception{
		_response.sendRedirect(url_);
	}
	
	public void sendError(int errorCode_) throws Exception{
		_response.sendError(errorCode_);
	}
}

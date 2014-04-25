package com.jg.action.handler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.vo.JGDataset;

public class JGServiceBox{
	protected HttpServletRequest _request = null;
	protected HttpServletResponse _response = null;
	public HttpServletRequest getRequest(){
		return _request;
	}
	public HttpServletResponse getResponse(){
		return _response;
	}
	
	protected JGMultipartData _multipartData = null;
	public JGMultipartData multipartData() throws Exception{
		if(_multipartData == null){
			_multipartData = new JGMultipartData(this);
		}
		return _multipartData;
	}
	
	protected JGResponseWriter _writer = null;
	public JGResponseWriter writer(){
		return _writer;
	}
	
	protected JGServiceKey _requestServiceKey = null;
	public JGServiceKey getRequestServiceKey(){
		return _requestServiceKey;
	}

	protected JGServiceBox(){}
	public JGServiceBox(HttpServletRequest request_, HttpServletResponse response_, JGServiceKey serviceKey_) throws Exception{
		_request = request_;
		_response = response_;
		_writer = new JGResponseWriter(response_);
		_requestServiceKey = serviceKey_;
	}
	public JGServiceBox(HttpServletRequest request_, HttpServletResponse response_) throws Exception{
		this(request_,response_, null);
		_requestServiceKey = JGServiceKey.makeKey(this);
	}
	
	public HttpSession getSession(){
		return _request.getSession();
	}
	
	public String getParameter(String key_) throws Exception{
		if(JGMultipartData.isMultipart(this))
			return multipartData().getFormFieldValue(key_);
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
}

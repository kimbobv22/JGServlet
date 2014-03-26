package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.action.handler.JGServiceBox;
import com.jg.action.handler.JGServiceKey;
import com.jg.filter.JGFilterChain;
import com.jg.log.JGLog;
import com.jg.main.loader.JGMainLoader;

public abstract class JGMainServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	protected JGServletErrorHandlerDef _errorHandler = null;
	protected JGServletErrorHandlerDef getErrorHandler(){
		return _errorHandler;
	}
	
	public void setErrorHandler(JGServletErrorHandlerDef errorHandler_){
		_errorHandler = errorHandler_;
	}
	
	protected JGFilterChain<JGMainServlet,JGServiceBox> _filterChain = new JGFilterChain<JGMainServlet,JGServiceBox>(this);
	protected JGFilterChain<JGMainServlet,JGServiceBox> getFilterChain(){
		return _filterChain;
	}
	
	@Override
	protected final void doGet(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException {
		try{
			JGServiceBox serviceBox_ = _createServiceBox(request_, response_);
			
			if(!acceptRequest(serviceBox_)){
				getErrorHandler().didRejectRequest(request_, response_);
			}else{
				handleGet(serviceBox_);
			}
		}catch(Exception ex_){
			JGLog.error(ex_.getLocalizedMessage(),ex_);
			getErrorHandler().didRaiseError(ex_, request_, response_);
		}
	}
	@Override
	protected final void doPost(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException {
		try{
			JGServiceBox serviceBox_ = _createServiceBox(request_, response_);
			
			if(!acceptRequest(serviceBox_)){
				getErrorHandler().didRejectRequest(request_, response_);
			}else{
				handlePost(serviceBox_);
			}
		}catch(Exception ex_){
			JGLog.error(ex_.getLocalizedMessage(),ex_);
			getErrorHandler().didRaiseError(ex_, request_, response_);
		}
	}
	
	private final JGServiceBox _createServiceBox(HttpServletRequest request_, HttpServletResponse response_) throws Exception{
		try{
			if(!JGMainLoader.didStartup()){
				throw new Exception("Shared JGMainLoader not loaded, bind JGMainServletContextListener to ServletContextListener"); 
			}
			
			JGServiceBox serviceBox_ = new JGServiceBox(request_, response_, JGServiceKey.makeKey(request_));
			_filterChain.doFilter(serviceBox_);
			
			return serviceBox_;
		}catch(Exception ex_){
			throw new Exception("can't create service box",ex_);
		}
	}
	
	abstract protected void handleGet(JGServiceBox serviceBox_) throws Exception;
	abstract protected void handlePost(JGServiceBox serviceBox_) throws Exception;
	
	abstract protected boolean acceptRequest(JGServiceBox serviceBox_) throws ServletException, IOException;
}

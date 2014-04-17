package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.action.handler.JGServiceBox;
import com.jg.action.handler.JGServiceHandler;
import com.jg.action.handler.JGServiceKey;
import com.jg.action.handler.JGServiceNotFoundException;
import com.jg.filter.JGFilterChain;
import com.jg.log.JGLog;
import com.jg.main.loader.JGMainLoader;

public abstract class JGHttpServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	protected JGServletErrorHandlerDef _errorHandler = new JGServletDefaultErrorHandler();
	protected JGServletErrorHandlerDef getErrorHandler(){
		return _errorHandler;
	}
	
	public void setErrorHandler(JGServletErrorHandlerDef errorHandler_){
		_errorHandler = errorHandler_;
	}
	
	protected JGFilterChain<JGHttpServlet,JGServiceBox> _filterChain = new JGFilterChain<JGHttpServlet,JGServiceBox>(this);
	protected JGFilterChain<JGHttpServlet,JGServiceBox> getFilterChain(){
		return _filterChain;
	}
	
	private boolean _checkMainLoader() throws ServletException{
		boolean result_ = JGMainLoader.didStartup();
		
		if(!result_)
			throw new ServletException("Shared JGMainLoader not loaded, bind JGMainServletContextListener to ServletContextListener");
		
		return result_;
	}
	
	@Override
	protected final void doGet(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException {
		_checkMainLoader();
		
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
	protected final void doPost(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		_checkMainLoader(); 
		
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
			JGServiceBox serviceBox_ = new JGServiceBox(request_, response_);
			_filterChain.doFilter(serviceBox_);
			
			return serviceBox_;
		}catch(Exception ex_){
			throw new Exception("can't create service box",ex_);
		}
	}
	
	protected final void handleGet(JGServiceBox serviceBox_) throws Exception{
		_doProcess(serviceBox_);
	};
	
	protected final void handlePost(JGServiceBox serviceBox_)throws Exception{
		_doProcess(serviceBox_);
	}
	
	private void _doProcess(JGServiceBox serviceBox_) throws Exception{
		try{
			JGServiceKey serviceKey_ = serviceBox_.getRequestServiceKey();
			JGServiceHandler.sharedHandler().handleService(serviceBox_, serviceKey_, true, false, true);
		}catch(JGServiceNotFoundException nex_){
			_errorHandler.serviceNotFound(nex_.getServiceKey(), serviceBox_.getRequest(), serviceBox_.getResponse());
		}
	}
	
	protected boolean acceptRequest(JGServiceBox serviceBox_) throws ServletException, IOException{return true;}
}

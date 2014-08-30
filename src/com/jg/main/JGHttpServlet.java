package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.main.loader.JGMainLoader;
import com.jg.service.JGServiceBox;
import com.jg.service.JGServiceHandler;
import com.jg.service.exception.JGActionProcessException;
import com.jg.service.exception.JGDirectoryNotFoundException;
import com.jg.service.exception.JGInvalidServiceException;
import com.jg.service.exception.JGRequestDeniedException;
import com.jg.service.exception.JGResultNotDefinedException;
import com.jg.service.exception.JGResultNotFoundException;
import com.jg.service.exception.JGResultPageNotFoundException;
import com.jg.service.exception.JGServiceNotFoundException;

public abstract class JGHttpServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	protected JGServletErrorHandlerDef _errorHandler = new JGServletDefaultErrorHandler();
	protected JGServletErrorHandlerDef getErrorHandler(){
		return _errorHandler;
	}
	
	public void setErrorHandler(JGServletErrorHandlerDef errorHandler_){
		_errorHandler = errorHandler_;
	}
	
	private boolean _checkMainLoader() throws ServletException{
		boolean result_ = JGMainLoader.didStartup();
		
		if(!result_)
			throw new ServletException("Shared JGMainLoader not loaded, bind JGMainServletContextListener to ServletContextListener");
		
		return result_;
	}
	
	private void doProcess(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		_checkMainLoader();
		JGServiceBox serviceBox_ = null;
		
		try{
			serviceBox_ = new JGServiceBox(request_, response_);
		}catch(Exception ex_){
			getErrorHandler().requestFailed(request_, response_, ex_);
			return;
		}
		
		try{
			JGServiceHandler.sharedHandler().handleService(serviceBox_, serviceBox_.getRequestPath());
		}catch(JGDirectoryNotFoundException ex_){
			getErrorHandler().directoryNotFound(serviceBox_, ex_.getPath());
		}catch(JGRequestDeniedException ex_){
			getErrorHandler().requestDenied(serviceBox_, ex_.getPath(), ex_.getService());
		}catch(JGResultNotFoundException ex_){
			getErrorHandler().resultNotFound(serviceBox_, ex_.getPath(), ex_.getReceivedResultCode());
		}catch(JGResultNotDefinedException ex_){
			getErrorHandler().resultNotDefined(serviceBox_, ex_.getPath(), ex_.getReceivedResultCode());
		}catch(JGResultPageNotFoundException ex_){
			getErrorHandler().resultPageNotFound(serviceBox_, ex_.getPath(), ex_.getReceivedResultCode(), ex_.getPageName());
		}catch(JGServiceNotFoundException ex_){
			getErrorHandler().serviceNotFound(serviceBox_, ex_.getPath());
		}catch(JGActionProcessException ex_){
			getErrorHandler().actionErrorOccurred(serviceBox_, ex_.getPath(), ex_.getActionClassName(), ex_.getMappingMethod(), ex_.getCause());
		}catch(JGInvalidServiceException ex_){
			getErrorHandler().invalidService(serviceBox_, ex_.getPath(), ex_.getActionClassName(), ex_.getMappingMethod(), ex_.getForwardPath());
		}catch(Exception ex_){
			getErrorHandler().unknownErrorOccurred(serviceBox_, serviceBox_.getRequestPath(), ex_);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		doProcess(request_, response_);
	}
	@Override
	protected void doPut(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		doProcess(request_, response_);
	}
	@Override
	protected void doHead(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		doProcess(request_, response_);
	}
	@Override
	protected void doTrace(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		doProcess(request_, response_);
	}
	@Override
	protected void doOptions(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		doProcess(request_, response_);
	}
	@Override
	protected final void doGet(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException {
		doProcess(request_, response_);
	}
	@Override
	protected final void doPost(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		doProcess(request_, response_);
	}
}

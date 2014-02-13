package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.action.handler.JGServiceBox;


public class JGDefaultHttpServletErrorHandler extends JGHttpServletErrorHandlerDef{
	
	@Override
	protected void didRejectRequest(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		response_.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
	}
	
	@Override
	protected void didRaiseError(Throwable error_, HttpServletRequest request_, HttpServletResponse response_) throws ServletException,IOException{
		throw new ServletException("Failed to handle a request",error_);
	}
	
	@Override
	protected void serviceNotFound(JGServiceBox serviceBox_) throws Exception{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
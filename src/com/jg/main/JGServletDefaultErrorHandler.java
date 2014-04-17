package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.action.handler.JGServiceKey;

public class JGServletDefaultErrorHandler extends JGServletErrorHandlerDef{
	protected void didRejectRequest(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		response_.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
	}
	protected void didRaiseError(Throwable error_, HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		response_.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
	protected void serviceNotFound(JGServiceKey serviceKey_, HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException{
		response_.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}

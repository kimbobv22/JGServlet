package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.action.handler.JGServiceKey;

public abstract class JGServletErrorHandlerDef{
	abstract protected void didRejectRequest(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException;
	abstract protected void didRaiseError(Throwable error_, HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException;
	abstract protected void serviceNotFound(JGServiceKey serviceKey_, HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException;
}

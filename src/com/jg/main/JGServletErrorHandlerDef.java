package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class JGServletErrorHandlerDef{
	abstract protected void didRejectRequest(HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException;
	abstract protected void didRaiseError(Throwable error_, HttpServletRequest request_, HttpServletResponse response_) throws ServletException, IOException;
}

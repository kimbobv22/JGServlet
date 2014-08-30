package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGService;

public abstract class JGServletErrorHandlerDef{
	abstract protected void requestFailed(HttpServletRequest request_, HttpServletResponse response_, Throwable throw_) throws ServletException, IOException;
	abstract protected void directoryNotFound(JGServiceBox serviceBox_, String path_) throws ServletException, IOException;
	abstract protected void requestDenied(JGServiceBox serviceBox_, String path_, JGService service_) throws ServletException, IOException;
	abstract protected void serviceNotFound(JGServiceBox serviceBox_, String path_) throws ServletException, IOException;
	abstract protected void invalidService(JGServiceBox serviceBox_, String path_, String actionClassName_, String mappingMethod_, String forwardPath_) throws ServletException, IOException;
	abstract protected void resultNotFound(JGServiceBox serviceBox_, String path_, int receivedResultCode_) throws ServletException, IOException;
	abstract protected void resultNotDefined(JGServiceBox serviceBox_, String path_, int receivedResultCode_) throws ServletException, IOException;
	abstract protected void resultPageNotFound(JGServiceBox serviceBox_, String path_, int receivedResultCode_, String pageName_) throws ServletException, IOException;
	abstract protected void actionErrorOccurred(JGServiceBox serviceBox_, String path_, String actionClassName_, String mappingMethod_, Throwable throw_) throws ServletException, IOException;
	abstract protected void unknownErrorOccurred(JGServiceBox serviceBox_, String path_, Throwable throw_) throws ServletException, IOException;
}

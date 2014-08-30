package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGService;

public class JGServletDefaultErrorHandler extends JGServletErrorHandlerDef{
	protected void requestFailed(HttpServletRequest request_, HttpServletResponse response_, Throwable throw_) throws ServletException, IOException{
		response_.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
	protected void directoryNotFound(JGServiceBox serviceBox_, String path_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "directory not found : "+path_);
	}
	protected void requestDenied(JGServiceBox serviceBox_, String path_, JGService service_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "request denied");
	}
	protected void serviceNotFound(JGServiceBox serviceBox_,String path_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "service not found : "+path_);
	}
	protected void invalidService(JGServiceBox serviceBox_, String path_, String actionClassName_, String mappingMethod_, String forwardPath_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "invalid service : "+path_+" : "+actionClassName_+" : "+mappingMethod_+" : "+forwardPath_);
	}
	protected void resultNotFound(JGServiceBox serviceBox_, String path_, int receivedResultCode_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "result not found : "+receivedResultCode_);
	}
	protected void resultNotDefined(JGServiceBox serviceBox_,String path_, int receivedResultCode_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "result not defined : "+receivedResultCode_);
	}
	protected void resultPageNotFound(JGServiceBox serviceBox_,String path_, int receivedResultCode_, String pageName_) throws ServletException, IOException{
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND, "result page not found : "+pageName_);
	}
	protected void actionErrorOccurred(JGServiceBox serviceBox_, String path_, String actionClassName_, String mappingMethod_, Throwable throw_) throws ServletException, IOException{
		throw_.printStackTrace();
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "action error occurred : "+path_+" : "+actionClassName_+" : "+mappingMethod_);
	}
	protected void unknownErrorOccurred(JGServiceBox serviceBox_, String path_, Throwable throw_) throws ServletException, IOException {
		throw_.printStackTrace();
		serviceBox_.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "unknown error occurred : "+path_+" : "+throw_);
	}
}

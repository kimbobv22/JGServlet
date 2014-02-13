package com.jg.main;

import java.io.IOException;

import javax.servlet.ServletException;

import com.jg.action.handler.JGActionHandler;
import com.jg.action.handler.JGService;
import com.jg.action.handler.JGServiceBox;

public abstract class JGHttpServlet extends JGMainServlet{
	
	private static final long serialVersionUID = 1L;
	
	protected final JGHttpServletErrorHandlerDef getErrorHandler(){
		return (JGHttpServletErrorHandlerDef)_errorHandler;
	}
	public final void setErrorHandler(JGHttpServletErrorHandlerDef errorHandler_){
		_errorHandler = errorHandler_;
	}
	
	@Override
	protected final void handleGet(JGServiceBox serviceBox_) throws Exception{
		_doProcess(serviceBox_);
	};
	
	@Override
	protected final void handlePost(JGServiceBox serviceBox_)throws Exception{
		_doProcess(serviceBox_);
	}
	
	private void _doProcess(JGServiceBox serviceBox_) throws Exception{
		JGService service_ = null;
		try{
			service_ = JGActionHandler.sharedHandler().getService(serviceBox_.getRequestServiceKey(),true);
			if(service_ == null)
				throw new Exception("service not found");
		}catch(Exception ex_){
			getErrorHandler().serviceNotFound(serviceBox_);
		}
		
		if(service_ != null) JGActionHandler.sharedHandler().handleService(serviceBox_, service_, false);
	}
	
	protected boolean acceptRequest(JGServiceBox serviceBox_) throws ServletException, IOException{return true;}
}

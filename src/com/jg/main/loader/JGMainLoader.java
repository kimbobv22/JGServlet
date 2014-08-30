package com.jg.main.loader;

import java.util.HashMap;

import javax.servlet.ServletContext;

import com.jg.db.xml.JGDBXMLQueryManager;
import com.jg.log.JGLog;
import com.jg.main.JGMainConfig;
import com.jg.service.JGServiceHandler;

public class JGMainLoader extends HashMap<String, Object>{
	
	private static final long serialVersionUID = 1L;
	static private JGMainLoader _sharedLoader = null;
	static protected void startupLoader(ServletContext servletContext_){
		if(_sharedLoader == null){
			synchronized(JGMainLoader.class){
				try{
					if(_sharedLoader == null)
						_sharedLoader = new JGMainLoader(servletContext_);
				}catch(Exception ex_){
					JGLog.error("failed to startup JGServlet",ex_);
					ex_.printStackTrace();
					_sharedLoader = null;
				}
			}
		}
	}
	static public boolean didStartup(){
		return (_sharedLoader != null);
	}
	
	protected JGMainLoader(ServletContext servletContext_) throws Exception{
		try{
			JGLog.info("Initializing JGServlet...");
			JGLog.info("Loading JGMainConfig...");
			JGMainConfig.makeSharedConfig(servletContext_.getRealPath("/WEB-INF"));
		}catch(Exception ex_){
			throw new Exception("Failed to load JGMainConfig", ex_);
		}
		
		try{
			JGLog.info("Loading JGServiceHandler...");
			JGServiceHandler.setXMLDirectoryPath(JGMainConfig.sharedConfig().getServicePath());
			JGServiceHandler.sharedHandler();
		}catch(Exception ex_){
			throw new Exception("Failed to load JGActionHandler",ex_);
		}
		
		try{
			JGLog.info("Loading JGDBXMLQueryManager...");
			JGDBXMLQueryManager.setXMLDirectoryPath(JGMainConfig.sharedConfig().getQueryPath());
			JGDBXMLQueryManager.sharedManager();
		}catch(Exception ex_){
			throw new Exception("Failed to load JGDBXMLQueryManager...");
		}
		JGLog.info("Succeed to initialize JGServlet!");
	}
}

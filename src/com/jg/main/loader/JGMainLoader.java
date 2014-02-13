package com.jg.main.loader;

import java.util.HashMap;

import com.jg.action.handler.JGActionHandler;
import com.jg.db.xml.JGDBXMLQueryManager;
import com.jg.log.JGLog;
import com.jg.main.JGMainConfig;

public class JGMainLoader extends HashMap<String, Object>{
	
	private static final long serialVersionUID = 1L;
	static private JGMainLoader _sharedLoader = null;
	static protected void startupLoader(){
		if(_sharedLoader == null){
			synchronized(JGMainLoader.class){
				try{
					_sharedLoader = new JGMainLoader();
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

	protected JGMainLoader() throws Exception{
		try{
			JGLog.log(9,"Initializing JGServlet...");
			JGLog.log(9,"Loading JGMainConfig...");
			JGMainConfig.sharedConfig();
		}catch(Exception ex_){
			throw new Exception("Failed to load JGMainConfig", ex_);
		}
		
		try{
			JGLog.log(9,"Loading JGActionHandler...");
			JGActionHandler.sharedHandler();
		}catch(Exception ex_){
			throw new Exception("Failed to load JGActionHandler",ex_);
		}
		
		try{
			JGLog.log(9,"Loading JGDBXMLQueryManager...");
			JGDBXMLQueryManager.setXMLDirectoryPath(JGMainConfig.sharedConfig().getDBXMLQueryDirectoryPath());
			JGDBXMLQueryManager.sharedManager();
		}catch(Exception ex_){
			throw new Exception("Failed to load JGDBXMLQueryManager...");
		}
		JGLog.log(9,"Succeed to initialize JGServlet!");
	}
}

package com.jg.main.loader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JGMainServletContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent contextEvent_){
		JGMainLoader.startupLoader(contextEvent_.getServletContext());
	}
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent_){}
}

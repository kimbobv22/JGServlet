package sample;

import javax.servlet.ServletContextEvent;

import com.jg.main.loader.JGMainServletContextListener;
import com.jg.service.JGServiceHandler;
public class ServletContextListener extends JGMainServletContextListener{
	
	@Override
	public void contextInitialized(ServletContextEvent contextEvent_){
		super.contextInitialized(contextEvent_);
		JGServiceHandler.sharedHandler().addFilter(SampleFilter.class);
	}
}

package sample;

import javax.servlet.ServletContextEvent;

import com.jg.main.loader.JGMainServletContextListener;
import com.jg.service.JGServiceBox;
import com.jg.service.JGServiceHandler;
import com.jg.service.element.JGServiceFilter;
public class ServletContextListener extends JGMainServletContextListener{
	
	@Override
	public void contextInitialized(ServletContextEvent contextEvent_){
		super.contextInitialized(contextEvent_);
		
		JGServiceHandler.sharedHandler().addFilter(new JGServiceFilter() {
			
			@Override
			public void doFilter(JGServiceBox serviceBox_){
				System.out.println("yap!");
			}
			
			@Override
			public boolean acceptFilter(JGServiceBox serviceBox_){
				return true;
			}
		});
	}
}

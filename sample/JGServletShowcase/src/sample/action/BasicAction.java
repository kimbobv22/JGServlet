package sample.action;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGAction;

public class BasicAction extends JGAction{
	
	public int testMethod(JGServiceBox serviceBox_) throws Exception{
		serviceBox_.setAttribute("testAttribute", "Welcome to JGServlet");
		serviceBox_.setAttribute("VDData",serviceBox_.getVirtualDirectoryData("sampleVD"));
		serviceBox_.setAttribute("VDData2",serviceBox_.getVirtualDirectoryData("sampleVD2"));
		return 0;
	}
}

package sample.action;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGAction;

public class DBAction extends JGAction{
	
	public void testConnection(JGServiceBox serviceBox_) throws Exception{
		String DBConfigName_ = serviceBox_.getParameter("DBConfigName");
		getDBConnection(DBConfigName_);
		serviceBox_.writer().printResultJSON(0);
	} 

}

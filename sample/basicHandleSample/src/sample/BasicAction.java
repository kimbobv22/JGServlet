package sample;

import com.jg.action.JGAction;
import com.jg.action.handler.JGServiceBox;

public class BasicAction extends JGAction{

	public int basicPageHandle(JGServiceBox serviceBox_, String arg1_) throws Exception{
		serviceBox_.setAttribute("message", "hello "+arg1_+" :)");
		return 0;
	}
	public int basicPageHandle(JGServiceBox serviceBox_) throws Exception{
		return basicPageHandle(serviceBox_, "world");
	}
	
}

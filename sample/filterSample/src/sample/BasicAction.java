package sample;

import com.jg.action.JGAction;
import com.jg.action.handler.JGServiceBox;

public class BasicAction extends JGAction{

	public int testFilter(JGServiceBox serviceBox_) throws Exception{
		serviceBox_.setAttribute("message", "hello world :)");
		return 0;
	}
	
}

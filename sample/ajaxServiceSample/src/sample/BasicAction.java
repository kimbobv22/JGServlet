package sample;

import com.jg.action.JGAction;
import com.jg.action.handler.JGServiceBox;

public class BasicAction extends JGAction{

	public void testMethod(JGServiceBox serviceBox_) throws Exception{
		serviceBox_.writer().printResultJSON(0, "hello world :)");
	}
	
}

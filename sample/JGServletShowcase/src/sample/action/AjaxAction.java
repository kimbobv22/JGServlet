package sample.action;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGAction;

public class AjaxAction extends JGAction{
	
	public void testAjax(JGServiceBox serviceBox_) throws Exception{
		serviceBox_.writer().printResultJSON(0, "this is AJAX!");
	}

}

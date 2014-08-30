package sample.action;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGAction;
import com.jg.util.JGStringUtils;

public class PostAction extends JGAction{
	
	public int testPost(JGServiceBox serviceBox_) throws Exception{
		int number_ = JGStringUtils.getInteger(serviceBox_.getParameter("number"), -1);
		serviceBox_.setAttribute("postResult", number_);
		return (number_ >= 0 ? 0 : -1);
	}

}

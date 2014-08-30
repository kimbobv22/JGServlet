package sample.action;

import com.jg.service.JGMultipartData;
import com.jg.service.JGServiceBox;
import com.jg.service.element.JGAction;

public class MultipartAction extends JGAction{
	
	public void testMultipart(JGServiceBox serviceBox_) throws Exception{
		JGMultipartData multipart_ = serviceBox_.getMultipartData();
		multipart_.doWriteUploadData();
		serviceBox_.writer().printResultJSON(0, multipart_.getParameter("param1"));
	}

}

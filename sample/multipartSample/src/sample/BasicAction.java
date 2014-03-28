package sample;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;
import org.json.simple.JSONArray;

import com.jg.action.JGAction;
import com.jg.action.handler.JGMultipartData;
import com.jg.action.handler.JGMultipartUploadHandlerDef;
import com.jg.action.handler.JGMultipartUploadResult;
import com.jg.action.handler.JGServiceBox;

public class BasicAction extends JGAction{
	
	public void fileUploadReceiver(JGServiceBox serviceBox_) throws Exception{
		String testParameter_ = serviceBox_.getParameter("testParameter");
		System.out.println(testParameter_);
		JGMultipartData mData_ = serviceBox_.multipartData();
		
		ArrayList<JGMultipartUploadResult> uploadResult_ = mData_.doWriteUploadData(new JGMultipartUploadHandlerDef() {
			
			@Override
			protected String renameFile(FileItem fileItem_){
				return UUID.randomUUID().toString();
			}
			
			@Override
			protected boolean acceptUploadData(FileItem fileItem_) {
				return true;
			}
			
			@Override
			protected void fileUploaded(FileItem arg0, JGMultipartUploadResult arg1){
				
			}
		});
		
		JSONArray result_ = new JSONArray();
		int length_ = uploadResult_.size();
		for(int index_=0;index_<length_;++index_){
			result_.add(uploadResult_.get(index_).toJSON());
		}
		
		serviceBox_.writer().printResultJSON(0,result_);
	}
}

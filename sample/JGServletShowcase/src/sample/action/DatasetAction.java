package sample.action;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGAction;
import com.jg.vo.JGDataset;

public class DatasetAction extends JGAction{
	
	protected JGDataset _storeDataset(JGServiceBox serviceBox_) throws Exception{
		JGDataset dataset_ = new JGDataset();
		dataset_.addColumns("col1","col2");
		dataset_.addRow();
		dataset_.addRow();
		
		dataset_.setColumnValue("col1", 0, "testValue1");
		serviceBox_.getSession().setAttribute("testDataset", dataset_);
		return dataset_;
	}
	
	public void getDataset(JGServiceBox serviceBox_) throws Exception{
		JGDataset dataset_ = (JGDataset)serviceBox_.getSession().getAttribute("testDataset");
		
		if(dataset_ == null){
			dataset_ = _storeDataset(serviceBox_);
		}
		
		serviceBox_.writer().printResultJSON(0, dataset_.toJSON());
	}
	
	public void storeDataset(JGServiceBox serviceBox_) throws Exception{
		JGDataset dataset_ = serviceBox_.getParameterAsDataset("dataset");
		serviceBox_.getSession().setAttribute("testDataset", dataset_);
		serviceBox_.writer().printResultJSON(0, "OK");
	}

}

package sample;

import com.jg.action.JGAction;
import com.jg.action.handler.JGServiceBox;
import com.jg.vo.JGDataset;

public class BasicAction extends JGAction{

	public void testMethod(JGServiceBox serviceBox_) throws Exception{
		JGDataset dataset_ = new JGDataset();
		
		dataset_.addRow();dataset_.addRow();
		dataset_.addColumns("col1","col2","col3");
		
		dataset_.setColumnValue("col1", 0, "mike");
		dataset_.setColumnValue("col2", 0, "ate");
		dataset_.setColumnValue("col3", 0, "a apple");
		
		dataset_.setColumnValue("col1", 1, "jhone");
		dataset_.setColumnValue("col2", 1, "ate");
		dataset_.setColumnValue("col3", 1, "a pie");
		
		serviceBox_.writer().printResultJSON(0, dataset_.toJSON(false));
	}
	
}

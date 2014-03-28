package sample;

import com.jg.action.JGAction;
import com.jg.action.handler.JGServiceBox;
import com.jg.db.JGDBConnection;
import com.jg.db.vo.JGDBQuery;
import com.jg.db.xml.JGDBXMLQueryManager;
import com.jg.vo.JGDataset;

public class BasicAction extends JGAction{
	
	/**
	 * check the API for JGDBConnection
	 */
	public void dbConnectionTest(JGServiceBox serviceBox_) throws Exception{
		JGDBConnection connection_ = getDBConnection();
		JGDataset result_ = connection_.executeQuery("SELECT * FROM TABS");
		serviceBox_.writer().printResultJSON(0, result_.toJSON(false));
	}
	
	public void getQueryByParameters(JGServiceBox serviceBox_) throws Exception{
		String col1_ = serviceBox_.getParameter("col1");
		String col2_ = serviceBox_.getParameter("col2");
		String col3_ = serviceBox_.getParameter("col3");
		String col4_ = serviceBox_.getParameter("col4");
		
		JGDBQuery query_ = JGDBXMLQueryManager.sharedManager().createQuery("query.test", "testQuery"
				,new Object[]{"col1", col1_, "col2", col2_, "col3", col3_, "col4", col4_});
		serviceBox_.writer().printResultJSON(0, query_.toString());
	}
	
	public void getQueryByDataset(JGServiceBox serviceBox_) throws Exception{
		JGDataset condData_ = serviceBox_.getParameterAsDataset("condData");
		JGDBQuery query_ = JGDBXMLQueryManager.sharedManager().createQuery("query.test", "testQuery", condData_);
		
		serviceBox_.writer().printResultJSON(0, query_.toString());
	}
}

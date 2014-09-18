package com.jg.service.element;

import java.util.ArrayList;

import com.jg.db.JGDBConfig;
import com.jg.db.JGDBConnection;
import com.jg.main.JGMainConfig;

public class JGBackbone{
	
	protected ArrayList<JGDBConnection> _DBConnectionList  = new ArrayList<JGDBConnection>();
	
	protected JGDBConnection getDBConnection(JGDBConfig dbConfig_) throws Exception{
		int connectionCount_ = _DBConnectionList.size();
		for(int tIndex_=0;tIndex_<connectionCount_;++tIndex_){
			JGDBConnection connection_ = _DBConnectionList.get(tIndex_);
			if(connection_.getDBConfig() == dbConfig_){
				return connection_;
			}
		}
		
		JGDBConnection connection_ = new JGDBConnection(dbConfig_);
		_DBConnectionList.add(connection_);
		return connection_;
	}
	protected JGDBConnection getDBConnection(String dbName_) throws Exception{
		return getDBConnection(JGMainConfig.sharedConfig().getDBConfig(dbName_));
	}
	protected JGDBConnection getDBConnection() throws Exception{
		return getDBConnection(JGMainConfig.sharedConfig().getPrimaryDBName());
	}
	protected void clearDBConnection(){
		_DBConnectionList.clear();
	}
}

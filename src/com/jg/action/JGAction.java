package com.jg.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.jg.action.handler.JGService;
import com.jg.action.handler.JGServiceBox;
import com.jg.action.handler.JGServiceKey;
import com.jg.db.JGDBConfig;
import com.jg.db.JGDBConnection;
import com.jg.main.JGMainConfig;
import com.jg.util.JGReflectionUtils;

public abstract class JGAction{
	
	private ArrayList<JGDBConnection> _DBConnectionList  = new ArrayList<JGDBConnection>();
	
	protected JGDBConnection getDBConnection(int index_) throws Exception{
		JGDBConfig dbConfig_ = JGMainConfig.sharedConfig().getDBConfigMap().getDBConfig(index_);
		
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
	protected JGDBConnection getDBConnection() throws Exception{
		return getDBConnection(0);
	}
	protected void clearDBConnection(){
		_DBConnectionList.clear();
	}
	
	@SuppressWarnings("unused")
	private Object process(JGService service_, JGServiceBox serviceBox_) throws Exception{
		try{
			Method method_ = getClass().getMethod(service_.getMappingMethod(),JGServiceBox.class);
		}catch(NoSuchMethodException ex_){
			throw new Exception("Can't find Action method", ex_);
		}
		
		try{
			return (Integer)JGReflectionUtils.invokeMethod(this, service_.getMappingMethod(), serviceBox_);
		}catch(IllegalArgumentException ex_){
			throw new Exception("Illegal argument for action method",ex_);
		}catch(IllegalAccessException ex_){
			throw new Exception("Illegal access for action method",ex_);
		}catch(InvocationTargetException ex_){
			JGServiceKey serviceKey_ = service_.getServiceKey();
			didCaughtException(new JGActionException("process error of action method "+serviceKey_.toString(), ex_.getCause(), service_.getServiceKey()));
			return -1;
		}finally{
			clearDBConnection();
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		try{
			clearDBConnection();
			cleanup();
		}catch(Exception ex_){}finally{
			super.finalize();
		}
	}
	
	abstract protected void initAction(JGServiceBox firstServiceBox_);
	protected void cleanup(){/*override me*/}
	protected void didCaughtException(JGActionException actionError_) throws Exception{
		throw actionError_;
	}
}

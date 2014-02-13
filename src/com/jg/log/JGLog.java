package com.jg.log;

import com.jg.main.JGMainConfig;


public class JGLog{
	
	static protected JGLogHandlerDef _logHandler = new JGDefaultLogHandler();
	static public void setLogHandler(JGLogHandlerDef logHandler_){
		_logHandler = logHandler_;
	}
	static public JGLogHandlerDef getLogHandler(){
		return _logHandler;
	}
	
	static public int debugLevel(){
		int debugLevel_ = 9;
		if(JGMainConfig.isInitialized()){
			debugLevel_ = JGMainConfig.sharedConfig().getDebugLevel();
		}
		
		return debugLevel_;
	} 
	
	static public void log(int level_, Object log_){
		if(debugLevel() >= level_){
			if(_logHandler == null){
				throw new NullPointerException("Log handler is null");
			}
			_logHandler.didCaughtLog(log_);
		}
	}
	static public void log(Object log_){
		log(debugLevel(),log_);
	}

	static public void log(int level_, String format_, Object... values_){
		log(level_,String.format(format_, values_));
	}
	static public void log(String format_, Object... values_){
		log(String.format(format_, values_));
	}
	
	static public void warn(Object warn_){
		_logHandler.didCaughtWarn(warn_);
	}
	static public void warn(String format_, Object... values_){
		warn(String.format(format_, values_));
	}
	
	static public void error(String log_, Throwable thrown_){
		_logHandler.didCaughtError(log_, thrown_);
	}
}

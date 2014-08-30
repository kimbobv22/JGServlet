package com.jg.log;



public class JGLog{
	
	static protected JGLogHandlerDef _logHandler = new JGDefaultLogHandler();
	static public void setLogHandler(JGLogHandlerDef logHandler_){
		_logHandler = logHandler_;
	}
	static public JGLogHandlerDef getLogHandler(){
		return _logHandler;
	}
	
	static public void debug(Object log_){
		_logHandler.didCaughtDebug(log_);
	}

	static public void debug(String format_, Object... values_){
		debug(String.format(format_, values_));
	}
	
	static public void warn(Object warn_){
		_logHandler.didCaughtWarn(warn_);
	}
	static public void warn(String format_, Object... values_){
		warn(String.format(format_, values_));
	}
	
	static public void info(Object warn_){
		_logHandler.didCaughtInfo(warn_);
	}
	static public void info(String format_, Object... values_){
		info(String.format(format_, values_));
	}
	
	static public void error(String log_, Throwable thrown_){
		_logHandler.didCaughtError(log_, thrown_);
	}
}

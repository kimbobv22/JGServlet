package com.jg.log;

public class JGDefaultLogHandler extends JGLogHandlerDef {

	@Override
	protected void didCaughtDebug(Object log_){
		System.out.println(log_);
	}
	
	@Override
	protected void didCaughtWarn(Object log_) {
		System.err.println(log_);
	}
	
	@Override
	protected void didCaughtInfo(Object log_) {
		System.err.println(log_);
	}
	
	@Override
	protected void didCaughtError(String error_, Throwable thrown_){
		thrown_.printStackTrace();
	}
}

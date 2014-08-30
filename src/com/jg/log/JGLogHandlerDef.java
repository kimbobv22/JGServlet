package com.jg.log;

public abstract class JGLogHandlerDef{
	protected abstract void didCaughtDebug(Object log_);
	protected abstract void didCaughtWarn(Object log_);
	protected abstract void didCaughtInfo(Object log_);
	protected abstract void didCaughtError(String error_, Throwable thrown_);
}

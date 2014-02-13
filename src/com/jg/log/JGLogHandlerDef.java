package com.jg.log;

public abstract class JGLogHandlerDef{
	protected abstract void didCaughtLog(Object log_);
	protected abstract void didCaughtWarn(Object log_);
	protected abstract void didCaughtError(String error_, Throwable thrown_);
}

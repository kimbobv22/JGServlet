package com.jg.filter;

public abstract class JGFilterDef<T,P> {
	protected boolean acceptFilter(T target_, JGFilterChain<T,P> filterChain_, P parameter_) throws Exception{return true;}
	abstract protected void doFilter(T target_, JGFilterChain<T,P> filterChain_, P parameter_) throws Exception;
}

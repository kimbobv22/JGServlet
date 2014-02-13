package com.jg.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JGReflectionUtils{

	public static Object invokeMethod(Object target_, Class<?> targetClass_, String methodName_, Class<?>[] parameterTypes_, Object... parameters_) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method targetMethod_ = targetClass_.getDeclaredMethod(methodName_, parameterTypes_);
		targetMethod_.setAccessible(true);
		
		Object returnValue_ = null;
		boolean hasReturnValue_ = true;
		try{
			hasReturnValue_ = !(targetMethod_.getReturnType().equals(Void.TYPE));
		}catch(NullPointerException ex_){
			hasReturnValue_ = false;
		}
		
		if(hasReturnValue_){
			returnValue_ = targetMethod_.invoke(target_, parameters_);
		}else{
			targetMethod_.invoke(target_, parameters_);
		}
		
		return returnValue_;
	}
	
	public static Object invokeMethod(Object target_, Class<?> targetClass_, String methodName_, Object... parameters_) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class<?>[] parameterTypes_ = null;
		if(parameters_ != null && parameters_.length > 0){
			int parameterCount_ = parameters_.length;
			parameterTypes_ = new Class<?>[parameterCount_];
			for(int index_=0;index_<parameterCount_;++index_){
				parameterTypes_[index_] = parameters_[index_].getClass();
			}
		}
		
		return invokeMethod(target_, targetClass_, methodName_, parameterTypes_, parameters_);
	}
	
	public static Object invokeMethod(Object target_, String methodName_, Object... parameters_) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return invokeMethod(target_, target_.getClass(), methodName_, parameters_);
	}
}


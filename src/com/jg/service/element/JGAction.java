package com.jg.service.element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.jg.service.JGServiceBox;
import com.jg.util.JGReflectionUtils;

public abstract class JGAction extends JGBackbone{
	
	protected JGAction(){}
	
	@SuppressWarnings("unused")
	public Object process(String mappingMethod_, JGServiceBox serviceBox_) throws Exception{
		
		try{
			Method method_ = getClass().getMethod(mappingMethod_,JGServiceBox.class);
		}catch(NoSuchMethodException ex_){
			throw new Exception("can't find Action method", ex_);
		}
		
		try{
			return JGReflectionUtils.invokeMethod(this, mappingMethod_, serviceBox_); 
		}catch(IllegalArgumentException ex_){
			throw new Exception("illegal argument for action method", ex_);
		}catch(IllegalAccessException ex_){
			throw new Exception("illegal access for action method", ex_);
		}catch(InvocationTargetException ex_){
			Throwable cause_ = ex_.getCause();
			if(didCaughtException(cause_, mappingMethod_)) throw new Exception("process error of action method", cause_);
			return -1;
		}finally{
			clearDBConnection();
		}
	}
	
	static public JGAction make(JGAction original_, Class<?> anotherClass_) throws Exception{
		JGAction another_ = (JGAction)anotherClass_.newInstance();
		another_._DBConnectionList = original_._DBConnectionList;
		return another_;
	}
	public JGAction make(Class<?> class_) throws Exception{
		return make(this, class_);
	}
	
	static public JGAction make(String class_) throws Exception{
		Class<?> actionClass_ = Class.forName(class_);
		return (JGAction)actionClass_.newInstance();
	}
	
	protected boolean didCaughtException(Throwable cause_, String mappingMethod_){
		return true;
	}
}

package com.jg.action.example;

import com.jg.action.JGAction;
import com.jg.action.handler.JGServiceBox;


public class TestAction extends JGAction {

	public void testAction000(JGServiceBox serviceBox_) throws Exception{;
		
	}
	public void testAction001(JGServiceBox serviceBox_) throws Exception{
		
		
	}
	
	@Override
	protected void initAction(JGServiceBox serviceBox_){
		
	}
	
	protected void didCaughtException(Throwable throwable_) throws Exception{
		throwable_.printStackTrace();
	}
}

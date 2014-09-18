package com.jg.service.element;

import com.jg.service.JGServiceBox;

public abstract class JGServiceFilter extends JGBackbone{
	
	abstract public boolean acceptFilter(JGServiceBox serviceBox_) throws Exception;
	abstract public void doFilter(JGServiceBox serviceBox_) throws Exception;
}

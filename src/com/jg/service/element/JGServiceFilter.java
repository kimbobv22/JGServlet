package com.jg.service.element;

import com.jg.service.JGServiceBox;

public interface JGServiceFilter{
	abstract public boolean acceptFilter(JGServiceBox serviceBox_) throws Exception;
	abstract public void doFilter(JGServiceBox serviceBox_) throws Exception;
}

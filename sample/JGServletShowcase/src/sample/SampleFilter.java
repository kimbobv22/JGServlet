package sample;

import com.jg.service.JGServiceBox;
import com.jg.service.element.JGServiceFilter;

public class SampleFilter extends JGServiceFilter{ 

	public void doFilter(JGServiceBox serviceBox_){
		System.out.println("yap!");
	}
	
	@Override
	public boolean acceptFilter(JGServiceBox serviceBox_){
		return true;
	}
}

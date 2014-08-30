package com.jg.service.element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jdom.Element;

import com.jg.util.JGStringUtils;

public class JGService{
	
	static public final String STR_ELEMENT_RESULT = "result";
	static public final String STR_ELEMENT_OTHERS = "others";
	
	static public final String STR_ATTRIBUTE_ISPRIMARY = "isPrimary";
	static public final String STR_ATTRIBUTE_PATTERN = "pattern";
	static public final String STR_ATTRIBUTE_ACTIONCLASSNAME = "actionClassName";
	static public final String STR_ATTRIBUTE_MAPPINGMETHOD = "mappingMethod";
	static public final String STR_ATTRIBUTE_FORWARDPATH = "forwardPath";
	static public final String STR_ATTRIBUTE_ISPRIVATE = "isPrivate";
	
	protected String _actionClassName = null;
	public String getActionClassName(){
		return _actionClassName;
	}
	protected void setActionClassName(String actionClassName_) {
		_actionClassName = actionClassName_;
	}
	
	protected String _mappingMethod = null;
	public String getMappingMethod(){
		return _mappingMethod;
	}
	protected void setMappingMethod(String mappingMethod_){
		_mappingMethod = mappingMethod_;
	}
	
	protected String _forwardPath = null;
	public String getForwardPath(){
		return _forwardPath;
	}
	protected void setForwardPath(String fPath_){
		_forwardPath = fPath_;
	}
	
	protected boolean _isPrimary = false;
	public boolean isPrimary(){
		return _isPrimary;
	}
	protected void setPrimary(boolean isPrimary_){
		_isPrimary = isPrimary_;
	}
	
	protected boolean _isPrivate = false;
	public boolean isPrivate(){
		return _isPrivate;
	}
	protected void setPrivate(boolean isPrivate_){
		_isPrivate = isPrivate_;
	}
	
	protected String _pattern = null;
	public String getPattern(){
		return _pattern;
	}
	protected void setPattern(String pattern_){
		_pattern = pattern_;
	}
	public boolean test(String str_){
		return Pattern.compile("^"+_pattern+"$").matcher(str_).find();
	}
	
	protected ArrayList<JGResult> _results = new ArrayList<JGResult>();
	public int sizeOfResults(){
		return _results.size();
	}
	
	protected void addResult(JGResult result_){
		int index_ = indexOfResultWithCode(result_._code);
		if(index_ >= 0) throw new RuntimeException("can't add result duplicated");
		_results.add(result_);
	}
	public JGResult getResult(int index_){
		return _results.get(index_);
	}
	public JGResult getResultWithCode(int code_){
		int index_ = indexOfResultWithCode(code_);
		if(index_ < 0) return null;
		return getResult(index_);
	}
	
	public int indexOfResultWithCode(int code_){
		int size_ = _results.size();
		for(int index_=0;index_<size_;++index_){
			JGResult result_ = _results.get(index_);
			if(result_._code == code_)
				return index_;
		}
		
		return -1;
	}
	
	private JGResult _otherResult = null;
	protected void setOtherResult(JGResult result_){
		_otherResult = result_;
	}
	public JGResult getOtherResult(){
		return _otherResult;
	}
	
	protected JGService(String pattern_){
		_pattern = pattern_;
	}

	public static JGService make(Element element_) throws Exception{
		String pattern_ = element_.getAttributeValue(STR_ATTRIBUTE_PATTERN);
		String actionClassName_ = element_.getAttributeValue(STR_ATTRIBUTE_ACTIONCLASSNAME);
		String mappingMethod_ = element_.getAttributeValue(STR_ATTRIBUTE_MAPPINGMETHOD);
		String forwardPath_ = element_.getAttributeValue(STR_ATTRIBUTE_FORWARDPATH);
		boolean isPrivate_ = JGStringUtils.getBoolean(element_.getAttributeValue(STR_ATTRIBUTE_ISPRIVATE), false);
		boolean isPrimary_ = JGStringUtils.getBoolean(element_.getAttributeValue(STR_ATTRIBUTE_ISPRIMARY), false);
		
		JGService service_ = new JGService(pattern_);
		service_.setActionClassName(actionClassName_);
		service_.setMappingMethod(mappingMethod_);
		service_.setForwardPath(forwardPath_);
		service_.setPrimary(isPrimary_);
		service_.setPrivate(isPrivate_);
		
		List<?> resultElements_ = element_.getChildren(STR_ELEMENT_RESULT);
		int countOfResultElements_ = resultElements_.size();
		for(int rIndex_=0;rIndex_<countOfResultElements_;++rIndex_){
			Element resultElement_ = (Element)resultElements_.get(rIndex_);
			JGResult result_ = JGResult.make(resultElement_);
			service_.addResult(result_);
		}
		
		Element otherResultElement_ = element_.getChild(STR_ELEMENT_OTHERS);
		if(otherResultElement_ != null){
			service_.setOtherResult(JGResult.make(otherResultElement_));
		}
		
		return service_;
	}
}

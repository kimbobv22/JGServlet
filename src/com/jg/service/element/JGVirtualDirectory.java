package com.jg.service.element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;


public class JGVirtualDirectory extends JGDirectory{
	
	static public final String STR_ELEMENT_VIRTUALDIRECTORY = "virtualDirectory";
	static public final String STR_ELEMENT_INCLUDES = "includes";
	static public final String STR_ELEMENT_EXCLUDES = "excludes";
	static public final String STR_ELEMENT_PATTERN = "pattern";
	
	static public final String STR_ATTRIBUTE_DATANAME = "dataName";
	
	protected String _dataName;
	public String getDataName(){
		return _dataName;
	}
	
	protected ArrayList<Pattern> _includePatterns = new ArrayList<Pattern>();
	protected ArrayList<Pattern> _excludePatterns = new ArrayList<Pattern>();
	
	public int countOfIncludePatterns(){
		return _includePatterns.size();
	}
	public int countOfExcludePatterns(){
		return _excludePatterns.size();
	}
	
	public void addIncludePattern(String pattern_){
		_includePatterns.add(Pattern.compile("^"+pattern_+"$"));
	}
	public void addExcludePattern(String pattern_){
		_excludePatterns.add(Pattern.compile("^"+pattern_+"$"));
	}
	
	public Pattern getIncludePattern(int index_){
		return _includePatterns.get(index_);
	}
	public Pattern getExcludePattern(int index_){
		return _includePatterns.get(index_);
	}

	@Override
	protected boolean test(String path_){
		boolean include_ = false;
		boolean exclude_ = false;
		
		int size_ = _includePatterns.size();
		for(int index_=0;index_<size_;++index_){
			Pattern pattern_ = _includePatterns.get(index_);
			if(pattern_.matcher(path_).find()){
				include_ = true;
				break;
			}
		}
		
		size_ = _excludePatterns.size();
		for(int index_=0;index_<size_;++index_){
			Pattern pattern_ = _excludePatterns.get(index_);
			if(pattern_.matcher(path_).find()){
				exclude_ = true;
				break;
			}
		}
		
		return (include_ && !exclude_);
	}
	
	public JGVirtualDirectory(){
		super(null, JGDirectoryType.Virtual);
	}
	
	public static JGVirtualDirectory make(File file_) throws Exception{
		if(file_ == null || !file_.isFile())
			throw new IllegalStateException("invalid virtual directory file");
		
		JGVirtualDirectory virtualDir_ = new JGVirtualDirectory();
		
		Document vDocument_ = new SAXBuilder().build(file_);
		Element rootElement_ = vDocument_.getRootElement();
		
		if(!rootElement_.getName().equals(STR_ELEMENT_VIRTUALDIRECTORY)){
			throw new IllegalStateException("invalid element name");
		}
		
		virtualDir_._dataName = rootElement_.getAttributeValue(STR_ATTRIBUTE_DATANAME);
		
		//add includes pattern
		Element includesElement_ = rootElement_.getChild(STR_ELEMENT_INCLUDES);
		if(includesElement_ == null)
			throw new IllegalStateException("includes element can't be null");
		
		List<?> iPatterns_ = includesElement_.getChildren(STR_ELEMENT_PATTERN);
		int countOfIPatterns_ = iPatterns_.size();
		if(countOfIPatterns_ == 0)
			throw new IllegalStateException("includes pattern can't be zero");
		
		for(int iIndex_=0;iIndex_<countOfIPatterns_;++iIndex_){
			Element iPattern_ = (Element)iPatterns_.get(iIndex_);
			virtualDir_.addIncludePattern(iPattern_.getValue());
		}
		
		//add excludes pattern
		Element excludesElement_ = rootElement_.getChild(STR_ELEMENT_EXCLUDES);
		if(excludesElement_ != null){
			List<?> ePatterns_ = excludesElement_.getChildren(STR_ELEMENT_PATTERN);
			int countOfEPatterns_ = ePatterns_.size();
			for(int eIndex_=0;eIndex_<countOfEPatterns_;++eIndex_){
				Element ePattern_ = (Element)ePatterns_.get(eIndex_);
				virtualDir_.addExcludePattern(ePattern_.getValue());
			}
		}
		
		return virtualDir_;
	}
	public static JGVirtualDirectory make(String filePath_) throws Exception{
		return make(new File(filePath_));
	}
}

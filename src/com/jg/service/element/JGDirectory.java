package com.jg.service.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.jg.main.JGMainConfig;
import com.jg.util.JGStringUtils;

public class JGDirectory{
	enum JGDirectoryType{
		Normal,
		Virtual,
		Map,
	}
	
	static public String STR_RESERVED_WORD_CURRENT_DIR = ".";
	static public String STR_RESERVED_WORD_PREV_DIR = "..";
	
	protected String _uuid = UUID.randomUUID().toString();
	public String getUUID(){
		return _uuid;
	}
	
	protected JGDirectory _parent= null;
	public JGDirectory getParent(){
		return _parent;
	}
	protected void setParent(JGDirectory parent_){
		_parent = parent_;
	}
	public boolean hasParent(){
		return (_parent != null);
	}
	
	private String _name = null;
	public String getName(){
		return _name;
	}
	
	protected JGDirectoryType _directoryType;
	public JGDirectoryType getDirectoryType(){
		return _directoryType;
	}
	
	ArrayList<JGDirectory> _children = new ArrayList<JGDirectory>();
	public int countOfChildren(){
		return _children.size();
	}
	
	public void addChild(JGDirectory directory_){
		int index_ = indexOfChild(directory_._uuid);
		if(index_ >= 0)
			throw new RuntimeException("child already exists");
		
		directory_._parent = this;
		_children.add(directory_);
	}
	public void removeChild(int index_){
		JGDirectory serviceNode_ = _children.get(index_);
		serviceNode_._parent = null;
		_children.remove(index_);
	}
	public void removeChild(String uuid_){
		int index_ = indexOfChild(uuid_);
		if(index_ < 0)
			throw new RuntimeException("child not found");
		removeChild(index_);
	}
	public void removeChild(JGDirectory directory_){
		removeChild(directory_._uuid);
	}
	public void removeChildFromParent(){
		if(_parent == null)
			throw new RuntimeException("parent not exists");
		_parent.removeChild(this);
	}
	
	public JGDirectory getChild(int index_){
		return _children.get(index_);
	}
	public JGDirectory getChild(String uuid_){
		int index_ = indexOfChild(uuid_);
		if(index_ < 0) return null;
		return getChild(index_);
		
	}
	public JGDirectory getChildWithName(String name_){
		int index_ = indexOfChildWithName(name_);
		if(index_ < 0) return null;
		return getChild(index_);
	}
	
	public int indexOfChild(String uuid_){
		int size_ = _children.size();
		for(int index_=0;index_<size_;++index_){
			JGDirectory directory_ = _children.get(index_);
			if(directory_._uuid.equals(uuid_))
				return index_;
		}
		return -1;
	}
	public int indexOfChild(JGDirectory directory_){
		return indexOfChild(directory_._uuid);
	}
	public int indexOfChildWithName(String name_){
		int size_ = _children.size();
		for(int index_=0;index_<size_;++index_){
			JGDirectory directory_ = _children.get(index_);
			if(directory_.test(name_))
				return index_;
		}
		return -1;
	}
	
	public JGServiceMap getPrimaryServiceMap(){
		int length_ = _children.size();
		for(int index_=0;index_<length_;++index_){
			JGDirectory directory_ = _children.get(index_);
			if((directory_._directoryType == JGDirectoryType.Map) && ((JGServiceMap)directory_)._isPrimary)
				return (JGServiceMap)directory_;
		}
		
		return null;
	}
	
	public JGDirectory getRoot(){
		JGDirectory currentDir_ = this;
		while(currentDir_.hasParent()){
			JGDirectory parent_ = currentDir_.getParent();
			if(parent_ == null)
				return currentDir_;
			currentDir_ = parent_; 
		}
		
		return currentDir_;
	}
	
	protected boolean test(String path_){
		return _name.equals(path_);
	}
	
	public JGDirectoryAnalysis analyze(String path_){
		String keySeparator_ = JGMainConfig.sharedConfig().getKeySeparator();
		String convertedPath_ = path_.trim();
		
		if(convertedPath_.indexOf(keySeparator_) == 0)
			convertedPath_ = convertedPath_.substring(1);
		
		int lastIndex_ = convertedPath_.lastIndexOf(keySeparator_);
		if(!JGStringUtils.isBlank(path_) && lastIndex_ == (convertedPath_.length() - 1)){
			convertedPath_ = convertedPath_.substring(0,lastIndex_);
		}
		String[] splitDirs_ = convertedPath_.split(keySeparator_);
		
		JGService findedService_ = null;
		HashMap<String, String> virtualDirectoryData_ = new HashMap<String, String>();
		
		boolean leftPath_ = true;
		int length_ = splitDirs_.length;
		JGDirectory tempDir_ = null;
		JGDirectory currentDir_ = path_.indexOf(keySeparator_) == 0 ? getRoot() : this;
		
		for(int index_=0;index_<length_;++index_){
			String directoryName_ = splitDirs_[index_];
			if(directoryName_.equals(STR_RESERVED_WORD_CURRENT_DIR)){
				tempDir_ = currentDir_;
			}else if(directoryName_.equals(STR_RESERVED_WORD_PREV_DIR)){
				tempDir_ = currentDir_.getParent();
			}else{
				tempDir_ = currentDir_.getChildWithName(directoryName_);
			}
			
			if(tempDir_ != null){
				leftPath_ = ((length_ - (index_+1)) > 0);
				JGDirectoryType tempDirType_ = tempDir_._directoryType;
				if(tempDirType_ == JGDirectoryType.Virtual){
					String vDDataName_ = ((JGVirtualDirectory)tempDir_).getDataName();
					if(vDDataName_ != null){
						virtualDirectoryData_.put(vDDataName_, directoryName_);
					}
				}else if(tempDirType_ == JGDirectoryType.Map){
					JGServiceMap findedServiceMap_ = (JGServiceMap)tempDir_;
					if(leftPath_){
						StringBuffer pattern_ = new StringBuffer();
						for(int pIndex_=index_+1; pIndex_ < length_; ++pIndex_){
							if(index_+1 != pIndex_) pattern_.append("/");
							pattern_.append(splitDirs_[pIndex_]);
						}
						findedService_ = findedServiceMap_.findService(pattern_.toString());
					}else findedService_ = findedServiceMap_.getPrimaryService();
					currentDir_ = tempDir_;
					break;
				}
				currentDir_ = tempDir_;
			}else break;
		}
		
		if(!leftPath_){
			JGServiceMap findedServiceMap_ = null;
			if(currentDir_._directoryType == JGDirectoryType.Map)
				findedServiceMap_ = (JGServiceMap)currentDir_;
			else{
				findedServiceMap_ = currentDir_.getPrimaryServiceMap();
				if(findedServiceMap_ != null)
					currentDir_ = findedServiceMap_;
			}
			
			if(findedServiceMap_ != null){
				findedService_ = findedServiceMap_.getPrimaryService();
			}
		}
		
		return new JGDirectoryAnalysis(currentDir_, findedService_, virtualDirectoryData_);
	}
	public void moveChildrenTo(JGDirectory directory_){
		int countOfChildren_ = countOfChildren();
		for(int index_=0;index_<countOfChildren_;++index_){
			JGDirectory child_ = _children.get(index_);
			child_._parent = directory_;
		}
		directory_._children.clear();
		directory_._children.addAll(_children);
		_children.clear();
	}
	
	protected JGDirectory(String name_, JGDirectoryType directoryType_){
		_name = name_;
		_directoryType = directoryType_;
	}
	public JGDirectory(String name_){
		this(name_, JGDirectoryType.Normal);
	}
}

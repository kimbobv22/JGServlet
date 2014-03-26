package com.jg.util;

import java.io.File;

public class JGFileUtils{
	
	static public String convertToRelativePath(String basePath_, String path_){
		return new File(basePath_).toURI().relativize(new File(path_).toURI()).getPath();
	}
	static public String removeSuffix(String filePath_){
		final int lastPeriodPos_ = filePath_.lastIndexOf('.');
		if(lastPeriodPos_ <= 0){
			return filePath_;
		}
		
		return filePath_.substring(0, lastPeriodPos_);
	}
	static public String getFileName(String filePath_, boolean removeSuffix_){
		File targetFile_ = new File(filePath_);
		
		if(targetFile_.isDirectory() || !removeSuffix_) return targetFile_.getName();
		else return removeSuffix(targetFile_.getName());
	}
	
	static public String getSuffix(String fileName_){
		int pos_ = fileName_.lastIndexOf('.');
		if(pos_ > 0 && pos_ < fileName_.length() - 1){
			return fileName_.substring(pos_+1);
		}
		
		return null;
	}
}

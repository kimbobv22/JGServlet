package com.jg.util;

import java.io.File;

public class JGFileUtils {
	static public String getFileName(String filePath_, boolean removeExtension_){
		File targetFile_ = new File(filePath_);
		
		if(targetFile_.isDirectory() || !removeExtension_) return targetFile_.getName();
		
		String fileName_ = targetFile_.getName(); 

		final int lastPeriodPos_ = fileName_.lastIndexOf('.');
		if(lastPeriodPos_ <= 0){
			return fileName_;
		}
		
		return fileName_.substring(0, lastPeriodPos_);
	}
	
	static public String getSuffix(String fileName_){
		int pos_ = fileName_.lastIndexOf('.');
		if(pos_ > 0 && pos_ < fileName_.length() - 1){
			return fileName_.substring(pos_+1);
		}
		
		return null;
	}
}

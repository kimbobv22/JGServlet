package com.jg.action.handler;

import java.util.HashMap;

import org.json.simple.JSONObject;

public class JGMultipartUploadResult {
	
	protected boolean _didUpload = false;
	public boolean didUpload(){
		return _didUpload;
	}
	protected String _originalPath = null;
	public String getOriginalPath(){
		return _originalPath;
	}
	
	protected String _uploadPath = null;
	public String getUploadPath(){
		return _uploadPath;
	}
	
	protected long _fileSize = -1;
	public long getFileSize(){
		return _fileSize;
	}
	
	public JSONObject toJSON(){
		HashMap<String, Object> result_ = new HashMap<String, Object>();
		
		result_.put("didUpload", _didUpload);
		result_.put("orgPath", _originalPath);
		if(_didUpload) result_.put("uploadPath", _uploadPath);
		result_.put("size", _fileSize);
		
		return new JSONObject(result_);
	}
}

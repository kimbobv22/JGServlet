package com.jg.service;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.json.simple.JSONObject;

public class JGMultipartUploadProgressListener implements ProgressListener{
	
	public static final String STR_KEY_CURRENTINDEX = "currentIndex";
	public static final String STR_KEY_BYTESREAD = "bytesRead";
	public static final String STR_KEY_TOTALLENGTH = "totalLength";
	
	static protected UUID _monitorKey = UUID.randomUUID();
	
	static public JGMultipartUploadProgressListener getListener(HttpSession session_){
		return (JGMultipartUploadProgressListener)session_.getAttribute(_monitorKey.toString());
	}
	static public JGMultipartUploadProgressListener getListener(HttpServletRequest request_){
		return getListener(request_.getSession());
	}
	static public JGMultipartUploadProgressListener getListener(JGServiceBox box_){
		return getListener(box_.getSession());
	}
	
	static protected void putListener(HttpSession session_, JGMultipartUploadProgressListener listener_){
		session_.setAttribute(_monitorKey.toString(), listener_);
	}
	static protected void putListener(HttpServletRequest request_, JGMultipartUploadProgressListener listener_){
		putListener(request_.getSession(), listener_);
	}
	static protected void putListener(JGServiceBox box_, JGMultipartUploadProgressListener listener_){
		putListener(box_.getSession(), listener_);
	}
	
	public class JGUploadProgressStatus{
		protected int _currentIndex = -1;
		public int getCurrentIndex(){
			return _currentIndex;
		}
		
		protected long _bytesRead = 0;
		public long getBytesRead(){
			return _bytesRead;
		}
		protected long _totalLength = 0;
		public long getTotalLength(){
			return _totalLength;
		}
		
		public double getCurrentRate(){
			return ((double)_bytesRead)/((double)_totalLength);
		}
		
		public JSONObject toJSON(){
			HashMap<String, Object> result_ = new HashMap<String, Object>();
			result_.put(STR_KEY_CURRENTINDEX, _currentIndex);
			result_.put(STR_KEY_BYTESREAD, _bytesRead);
			result_.put(STR_KEY_TOTALLENGTH, _totalLength);
			return new JSONObject(result_);
		}
	}
	protected JGUploadProgressStatus _status = new JGUploadProgressStatus();
	public JGUploadProgressStatus getProgressStatus(){
		return _status;
	}
	
	@Override
	public void update(long bytesRead_, long contentLength_, int itemIndex_) {
		_status._currentIndex = itemIndex_;
		_status._bytesRead = bytesRead_;
		_status._totalLength = contentLength_;
	}
}

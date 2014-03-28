package com.jg.action.handler;

import java.util.UUID;

import org.apache.commons.fileupload.FileItem;

public class JGMultipartDefaultUploadHandler extends JGMultipartUploadHandlerDef{
	protected boolean acceptUploadData(FileItem target_){
		return true;
	}
	protected String renameFile(FileItem target_){
		return UUID.randomUUID().toString();
	}
	
	protected void fileUploaded(FileItem target_, JGMultipartUploadResult result_){}
}
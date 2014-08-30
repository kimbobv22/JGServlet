package com.jg.service;

import org.apache.commons.fileupload.FileItem;

abstract public class JGMultipartUploadHandlerDef {
	abstract protected boolean acceptUploadData(FileItem target_);
	abstract protected String renameFile(FileItem target_);
	abstract protected void fileUploaded(FileItem target_, JGMultipartUploadResult result_);
}
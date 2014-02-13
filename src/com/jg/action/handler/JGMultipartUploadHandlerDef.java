package com.jg.action.handler;

import org.apache.commons.fileupload.FileItem;

abstract public class JGMultipartUploadHandlerDef {
	abstract protected boolean acceptUploadData(FileItem target_);
	abstract protected String renameFileName(FileItem target_);
}
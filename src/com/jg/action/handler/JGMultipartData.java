package com.jg.action.handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jg.action.handler.JGMultipartUploadProgressListener.JGUploadProgressStatus;
import com.jg.main.JGMainConfig;
import com.jg.util.JGCommonUtils;
import com.jg.util.JGDrawingUtils;
import com.jg.util.JGFileUtils;

public class JGMultipartData{
	protected JGServiceBox _parentServiceBox = null;
	public JGServiceBox getParentServiceBox(){
		return _parentServiceBox;
	}
	protected HashMap<String, String> _formFields = new HashMap<String, String>();
	public int sizeOfFormFields(){
		return _formFields.size();
	}
	public Set<String> keySetOfFormFields(){
		return _formFields.keySet();
	}
	public Collection<String> valuesOfFormFields(){
		return _formFields.values();
	}
	
	protected void setFormFieldValue(String key_, String value_){
		_formFields.put(key_, value_);
	}
	public String getFormFieldValue(String key_){
		return _formFields.get(key_);
	}
	
	protected ArrayList<FileItem> _uploadDataList = new ArrayList<FileItem>();
	public ArrayList<JGMultipartUploadResult> doWriteUploadData(JGMultipartUploadHandlerDef handler_) throws Exception{
		ArrayList<JGMultipartUploadResult> result_ = new ArrayList<JGMultipartUploadResult>();
		
		String fileUploadRootPath_ = JGMainConfig.sharedConfig().getFileRootPath();
		String targetFilePath_ = (String)JGCommonUtils.NVL(getFormFieldValue("path"), "");
		for(FileItem fileItem_ : _uploadDataList){
			JGMultipartUploadResult uploadResult_ = new JGMultipartUploadResult();
			String fileName_ = fileItem_.getName();
			String orgFilePath_ = targetFilePath_+fileName_;
			
			uploadResult_._originalPath = orgFilePath_;
			uploadResult_._fileSize = fileItem_.getSize();
			if(!handler_.acceptUploadData(fileItem_)){
				uploadResult_._didUpload = false;
			}else{
				//check file name by reject pattern
				Pattern rejectPattern_ = Pattern.compile(JGMainConfig.sharedConfig().getFileRejectRegexp());
				if(rejectPattern_.matcher(fileName_).find()){
					throw new IllegalAccessException("invalid file name : "+fileName_);
				}
				
				String newFileName_ = handler_.renameFileName(fileItem_);
				
				if(JGMainConfig.sharedConfig().isCompressionImage() && getParentServiceBox().getSession().getServletContext().getMimeType(fileName_).indexOf("image/") >= 0){
					BufferedImage orgImage_ = ImageIO.read(fileItem_.getInputStream());
					BufferedImage compedImage_ = JGDrawingUtils.resizeImage(orgImage_, orgImage_.getWidth(), orgImage_.getHeight());
					
					newFileName_ += ".jpg";
					File targetFile_ = new File(fileUploadRootPath_+targetFilePath_, newFileName_);
					targetFile_.mkdirs();
					ImageIO.write(compedImage_, "jpg", targetFile_);
				}else{
					newFileName_ += "."+JGFileUtils.getSuffix(fileName_);
					File targetFile_ = new File(fileUploadRootPath_+targetFilePath_, newFileName_);
					targetFile_.mkdirs();
					fileItem_.write(targetFile_);
				}
				
				uploadResult_._uploadPath = targetFilePath_+newFileName_;
				uploadResult_._didUpload = true;
			}
			
			result_.add(uploadResult_);
		}
		
		return result_;
	}
	public ArrayList<JGMultipartUploadResult> doWriteUploadData() throws Exception{
		return doWriteUploadData(new JGMultipartDefaultUploadHandler());
	}
	
	public JGUploadProgressStatus getUploadProgressStatus(){
		JGMultipartUploadProgressListener fileUploadListener_ = JGMultipartUploadProgressListener.getListener(_parentServiceBox);
		if(fileUploadListener_ == null){
			return null;
		}
		return fileUploadListener_._status;
	}
	
	protected JGMultipartData(JGServiceBox serviceBox_) throws Exception{
		_parentServiceBox = serviceBox_;
		if(!JGMultipartData.isMultipart(serviceBox_)){
			throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form");
		}
		
		try{
			ServletFileUpload uploadHandler_ = new ServletFileUpload(new DiskFileItemFactory());
			JGMultipartUploadProgressListener uploadListener_ = new JGMultipartUploadProgressListener();
			JGMultipartUploadProgressListener.putListener(serviceBox_, uploadListener_);
			uploadHandler_.setProgressListener(uploadListener_);
			
			List<FileItem> items_ = uploadHandler_.parseRequest(serviceBox_.getRequest());
			
			//add form field values from form
			for(FileItem item_ : items_){
				if(item_.isFormField()){
					String key_ = item_.getFieldName();
					String value_ = item_.getString();
					setFormFieldValue(key_, value_);
				}
			}
			
			//write files
			for(FileItem item_ : items_){
				if(!item_.isFormField()){
					_uploadDataList.add(item_);
				}
			}
		}catch(Exception ex_){
			_formFields.clear();
			throw ex_;
		}finally{
			JGMultipartUploadProgressListener.putListener(serviceBox_, null);
		}
	}
	
	static public boolean isMultipart(JGServiceBox serviceBox_){
		return ServletFileUpload.isMultipartContent(serviceBox_.getRequest());
	}
	
}

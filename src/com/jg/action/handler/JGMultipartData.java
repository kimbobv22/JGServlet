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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jg.action.handler.JGMultipartUploadProgressListener.JGUploadProgressStatus;
import com.jg.main.JGMainConfig;
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
		if(fileUploadRootPath_.lastIndexOf(File.separator) == fileUploadRootPath_.length()-1)
			fileUploadRootPath_ = fileUploadRootPath_.substring(0, fileUploadRootPath_.length()-1);
		
		for(FileItem fileItem_ : _uploadDataList){
			JGMultipartUploadResult uploadResult_ = new JGMultipartUploadResult();
			String fileName_ = fileItem_.getName();
			String orgFilePath_ = fileName_;
			
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
				
				String newFileName_ = handler_.renameFile(fileItem_);
				String newFilePath_ = null;
				
				int fileNameIndex_ = newFileName_.lastIndexOf(File.separator);
				if(fileNameIndex_ >= 0)
					newFilePath_ = newFileName_.substring(0,fileNameIndex_+1);  
				else newFilePath_ = File.separator;
				
				if(newFilePath_.indexOf(File.separator) < 0)
					newFilePath_ = File.separator+newFilePath_; 
				
				newFileName_ = new File(newFileName_).getName();
				
				if(JGMainConfig.sharedConfig().isCompressionImage()
						&& Pattern.compile(JGMainConfig.sharedConfig().getFileImageCompressionRegexp()).matcher(fileName_).find()){
					BufferedImage orgImage_ = ImageIO.read(fileItem_.getInputStream());
					BufferedImage compedImage_ = JGDrawingUtils.resizeImage(orgImage_, orgImage_.getWidth(), orgImage_.getHeight());
					
					newFileName_ += ".jpg";
					new File(fileUploadRootPath_+newFilePath_).mkdirs();
					File targetFile_ = new File(fileUploadRootPath_+newFilePath_, newFileName_);
					ImageIO.write(compedImage_, "jpg", targetFile_);
				}else{
					newFileName_ += "."+JGFileUtils.getSuffix(fileName_);
					
					new File(fileUploadRootPath_+newFilePath_).mkdirs();
					File targetFile_ = new File(fileUploadRootPath_+newFilePath_, newFileName_);
					fileItem_.write(targetFile_);
				}
				
				uploadResult_._uploadPath = newFilePath_+newFileName_;
				uploadResult_._didUpload = true;
				handler_.fileUploaded(fileItem_, uploadResult_);
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
	
	static public boolean isMultipart(HttpServletRequest request_){
		return ServletFileUpload.isMultipartContent(request_);
	}
	static public boolean isMultipart(JGServiceBox serviceBox_){
		return isMultipart(serviceBox_.getRequest());
	}
	
}

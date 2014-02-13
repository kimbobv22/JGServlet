package com.jg.main.file;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.jg.action.handler.JGServiceBox;
import com.jg.main.JGMainConfig;
import com.jg.util.JGDrawingUtils;
import com.jg.util.JGFileUtils;


public class JGFileHandler{
	
	private JGFileHandler(){}
	
	static public void sendFile(JGServiceBox box_, File file_, String mimeType_) throws Exception{
		ServletOutputStream outputStream_ = null;
		DataInputStream inputStream_ = null;
		try{
			HttpServletResponse response_ = box_.getResponse();
			outputStream_ = response_.getOutputStream();

			response_.setContentType(mimeType_);
			response_.setContentLength((int)file_.length());
			response_.setHeader("Content-Disposition", "inline; filename=\"" + file_.getName() + "\"");
			
			int bytes_ = 0;
			byte[] bbuf_ = new byte[1024];
			inputStream_ = new DataInputStream(new FileInputStream(file_));
			while((inputStream_ != null) && ((bytes_ = inputStream_.read(bbuf_)) != -1)){
				outputStream_.write(bbuf_, 0, bytes_);
			}
			
			outputStream_.flush();
		}catch(Exception ex_){
			throw new Exception("failed to send file",ex_);
		}finally{
			if(outputStream_ != null) outputStream_.close();
			if(inputStream_ != null) inputStream_.close();
		}
		
	}
	static public void sendFile(JGServiceBox box_, File file_) throws Exception{
		sendFile(box_, file_, MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file_));
	}
	static public void sendFile(JGServiceBox box_, String filePath_, String mimeType_) throws Exception{
		File targetFile_ = new File(JGMainConfig.sharedConfig().getFileUploadRootPath()+filePath_);
		if(!targetFile_.exists()){
			throw new FileNotFoundException("file not found : "+filePath_);
		}
		sendFile(box_, targetFile_, mimeType_);
	}
	static public void sendFile(JGServiceBox box_, String filePath_) throws Exception{
		sendFile(box_, filePath_, null);
	}
	
	static protected void sendResizedImage(JGServiceBox box_, String fileName_, BufferedImage image_, int width_, int height_, HashMap<RenderingHints.Key, Object> renderingHints_) throws Exception{
		ServletOutputStream servletOutputStream_ = null;
		try{
			BufferedImage resizedImage_ = JGDrawingUtils.resizeImage(image_, width_, height_, renderingHints_);
			ByteArrayOutputStream outputStream_ = new ByteArrayOutputStream();
			
			ImageIO.write(resizedImage_, "jpg", outputStream_);
			
			HttpServletResponse response_ = box_.getResponse();
			servletOutputStream_ = response_.getOutputStream();
			
			response_.setContentType("image/jpg");
			response_.setContentLength(outputStream_.size());
			response_.setHeader("Content-Disposition", "inline; filename=\"" + JGFileUtils.getFileName(fileName_, true)+".jpg\"");
			
			outputStream_.writeTo(servletOutputStream_);
			servletOutputStream_.flush();
		}catch(Exception ex_){
			throw new Exception("failed to send resized image",ex_);
		}finally{
			if(servletOutputStream_ != null) servletOutputStream_.close();
		}
	}
	
	static public void sendResizedImage(JGServiceBox box_, File imageFile_, int width_, int height_, HashMap<RenderingHints.Key, Object> renderingHints_) throws Exception{
		sendResizedImage(box_, imageFile_.getName(), ImageIO.read(imageFile_), width_, height_, renderingHints_);
	}
	static public void sendResizedImage(JGServiceBox box_, File imageFile_, int width_, int height_) throws Exception{
		sendResizedImage(box_, imageFile_, width_, height_, JGDrawingUtils.getDefaultRenderingHints());
	}
	
	static public void sendResizedImage(JGServiceBox box_, File imageFile_, double ratio_, HashMap<RenderingHints.Key, Object> renderingHints_) throws Exception{
		BufferedImage bufferedImage_ = ImageIO.read(imageFile_);
		sendResizedImage(box_, imageFile_.getName(),bufferedImage_, (int)(bufferedImage_.getWidth()*ratio_), (int)(bufferedImage_.getHeight()*ratio_), renderingHints_);
	}
	static public void sendResizedImage(JGServiceBox box_, File imageFile_, double ratio_) throws Exception{
		sendResizedImage(box_, imageFile_, ratio_, JGDrawingUtils.getDefaultRenderingHints());
	}
	
	static public void sendResizedImage(JGServiceBox box_, String imageFilePath_,  int width_, int height_, HashMap<RenderingHints.Key, Object> renderingHints_) throws Exception{
		File targetFile_ = new File(JGMainConfig.sharedConfig().getFileUploadRootPath()+imageFilePath_);
		if(!targetFile_.exists()){
			throw new FileNotFoundException("file not found : "+imageFilePath_);
		}
		sendResizedImage(box_, targetFile_, width_, height_, renderingHints_);
	}
	static public void sendResizedImage(JGServiceBox box_, String imageFilePath_, int width_, int height_) throws Exception{
		sendResizedImage(box_, imageFilePath_, width_, height_, JGDrawingUtils.getDefaultRenderingHints());
	}
	
	static public void sendResizedImage(JGServiceBox box_, String imageFilePath_, double ratio_, HashMap<RenderingHints.Key, Object> renderingHints_) throws Exception{
		File targetFile_ = new File(JGMainConfig.sharedConfig().getFileUploadRootPath()+imageFilePath_);
		if(!targetFile_.exists()){
			throw new FileNotFoundException("file not found : "+imageFilePath_);
		}
		sendResizedImage(box_, targetFile_, ratio_, renderingHints_);
	}
	static public void sendResizedImage(JGServiceBox box_, String imageFilePath_,  double ratio_) throws Exception{
		sendResizedImage(box_, imageFilePath_, ratio_, JGDrawingUtils.getDefaultRenderingHints());
	}
	
	static public void deleteFile(String filePath_) throws Exception{
		File targetFile_ = new File(JGMainConfig.sharedConfig().getFileUploadRootPath()+filePath_);
		if(!targetFile_.exists()){
			throw new FileNotFoundException("file not found : "+filePath_);
		}
		targetFile_.delete();
	}
}

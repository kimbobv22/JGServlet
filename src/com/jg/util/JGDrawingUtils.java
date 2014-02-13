package com.jg.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashMap;

public class JGDrawingUtils {
	
	static protected HashMap<RenderingHints.Key, Object> _defaultRenderingHints = null;
	static public void setDefaultRenderingHints(HashMap<RenderingHints.Key, Object> hints_){
		_defaultRenderingHints = hints_;
	}
	static public HashMap<RenderingHints.Key, Object> getDefaultRenderingHints(){
		if(_defaultRenderingHints == null){
			_defaultRenderingHints = new HashMap<RenderingHints.Key, Object>();
			_defaultRenderingHints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			_defaultRenderingHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
			_defaultRenderingHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		return _defaultRenderingHints;
	}
	
	static public BufferedImage resizeImage(BufferedImage source_, int width_, int height_, HashMap<RenderingHints.Key, Object> renderingHints_){
		int soruceWith_ = source_.getWidth();
		int soruceHeight_ = source_.getHeight();
		
		if(width_ <= 0){
			width_ = (int)(soruceWith_ * (((double)height_)/(double)soruceHeight_));
		}else if(height_ <= 0){
			height_ = (int)(soruceHeight_ *(((double)width_)/(double)soruceWith_));
		}
		
		double ratioWidth_ = (((double)width_)/(double)soruceWith_);
		double ratiotHeight_ = (((double)height_)/(double)soruceHeight_);
		
		BufferedImage resizedImage_ = new BufferedImage(width_, height_, source_.getType());
		Graphics2D gra2d_ = resizedImage_.createGraphics();
		gra2d_.setRenderingHints(renderingHints_);
		AffineTransform xform_ = AffineTransform.getScaleInstance(ratioWidth_, ratiotHeight_);
		gra2d_.fillRect(0, 0, width_, height_);
		gra2d_.drawRenderedImage((RenderedImage)source_, xform_);
		gra2d_.dispose();
		
		return resizedImage_;
	}
	static public BufferedImage resizeImage(BufferedImage source_, int width_, int height_){
		return resizeImage(source_, width_, height_, getDefaultRenderingHints());
	}
}

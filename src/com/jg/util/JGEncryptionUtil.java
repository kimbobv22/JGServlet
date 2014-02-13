package com.jg.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import com.jg.main.JGMainConfig;

public class JGEncryptionUtil{
	static private String _cryptKey = null;
	static public void setCryptKey(String key_){
		_cryptKey = key_;
	}
	static public String getCryptKey(){
		return _cryptKey;
	}
	
	static protected boolean isTripleDES(String key_){
		return key_.length() == 24;
	}
	static protected String getInstance(String key_){
		return isTripleDES(key_) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
	}
	static protected Key convertKey(String key_) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException{
		KeySpec keySpec_ = null;
		String algorithm_ = null;
		
		if(isTripleDES(key_)){
			keySpec_ = new DESedeKeySpec(key_.getBytes());
			algorithm_ = "DESede";
		}else{
			keySpec_ = new DESKeySpec(key_.getBytes());
			algorithm_ = "DES";
		}
		
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm_);
		return keyFactory.generateSecret(keySpec_);
	}
	
	static protected JGBase64CoderDef _base64Coder = null;
	static public void setBase64CoderDef(JGBase64CoderDef coderDef_){
		_base64Coder = coderDef_;
	}
	static public JGBase64CoderDef getBase64CoderDef(){
		return _base64Coder;
	}
	
	static protected Cipher getCipher(int opMode_, String key_) throws Exception{
		if(key_ == null){
			throw new Exception("Crypt key can't be null");
		}
		
		if(_base64Coder == null){
			throw new NullPointerException("Base64Coder is null");
		}
		
		Cipher cipher_ = null;
		try{
			cipher_ = Cipher.getInstance(getInstance(key_));
			cipher_.init(opMode_, convertKey(key_));
		}catch(NoSuchAlgorithmException ex_){
			throw new Exception("no such algorithm", ex_);
		}catch(NoSuchPaddingException ex_){
			throw new Exception("no such padding", ex_);
		}catch(InvalidKeyException ex_){
			throw new Exception("invalid key",ex_);
		}catch(InvalidKeySpecException ex_){
			throw new Exception("invalid key spec",ex_);
		}

		return cipher_;
	}
	static public String encrypt(String value_, String key_) throws Exception{
		try{
			Cipher cipher_ = getCipher(Cipher.ENCRYPT_MODE, key_);
			
			byte[] outputBytes_ = cipher_.doFinal(value_.getBytes(JGMainConfig.sharedConfig().getCharacterEncoding()));
			return String.valueOf(_base64Coder.encode(outputBytes_));
		}catch(Exception ex_){
			throw new Exception("failed to encrypt",ex_);
		}
	}
	static public String encrypt(String value_) throws Exception{
		return encrypt(value_, _cryptKey);
	}
	
	static public String decrypt(String value_, String key_) throws Exception{
		try{
			Cipher cipher_ = getCipher(Cipher.DECRYPT_MODE, key_);
		
			byte[] inputBytes_ = _base64Coder.decode(value_);
			byte[] outputBytes_ = cipher_.doFinal(inputBytes_);
			
			return new String(outputBytes_, JGMainConfig.sharedConfig().getCharacterEncoding());
		}catch(Exception ex_){
			throw new Exception("failed to decrypt",ex_);
		}
	}
	static public String decrypt(String value_) throws Exception{
		return decrypt(value_, _cryptKey);
	}
}

package com.jg.main;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jg.db.JGDBConfig;
import com.jg.util.JGStringUtils;

public class JGMainConfig{
	static private JGMainConfig _config = null;
	static public final JGMainConfig sharedConfig(){
		if(!isInitialized()){
			synchronized(JGMainConfig.class){
				try{
					_config = new JGMainConfig();
				}catch(Exception ex_){
					ex_.printStackTrace();
				}
			}
		}
		return _config;
	}
	static public final boolean isInitialized(){
		return (_config != null);
	}
	
	private ResourceBundle _configBundle = null;
	public ResourceBundle getConfigBundle(){
		if(_configBundle == null){
			_configBundle = ResourceBundle.getBundle("JGConfig");
		}
		return _configBundle;
	}
	
	protected int _debugLevel = 0;
	protected void setDebugLevel(int debugLevel_){
		_debugLevel = debugLevel_;
	}
	public int getDebugLevel(){
		return _debugLevel;
	}
	
	protected String _characterEncoding = "UTF-8";
	protected void setCharacterEncoding(String characterEncoding_){
		_characterEncoding = characterEncoding_;
	}
	public String getCharacterEncoding(){
		return _characterEncoding;
	}
	
	protected boolean _serviceEncryption = false;
	protected void setServiceEncryption(boolean bool_){
		_serviceEncryption = bool_;
	}
	public boolean isServiceEncryption(){
		return _serviceEncryption;
	}
	
	protected String _serviceDirectoryPath = null;
	protected void setServiceDirectoryPath(String path_){
		_serviceDirectoryPath = path_;
	}
	public String getServiceDirectoryPath(){
		return _serviceDirectoryPath;
	}
	
	protected String _DBXMLQueryDirectoryPath = null;
	protected void setDBXMLQueryDirectoryPath(String path_){
		_DBXMLQueryDirectoryPath = path_;
	}
	public String getDBXMLQueryDirectoryPath(){
		return _DBXMLQueryDirectoryPath;
	}
	
	protected String _fileUploadRootPath = null;
	protected void setFileUploadRootPath(String path_){
		_fileUploadRootPath = path_;
	}
	public String getFileUploadRootPath(){
		return _fileUploadRootPath;
	}
	protected String _fileUploadRejectRegexp = null;
	protected void setFileUploadRejectRegexp(String regexp_){
		_fileUploadRejectRegexp = regexp_;
	}
	public String getFileUploadRejectRegexp(){
		return _fileUploadRejectRegexp;
	}
	protected boolean _compressionUploadImage = false;
	protected void setCompressionUploadImage(boolean bool_){
		_compressionUploadImage = bool_;
	}
	public boolean isCompressionUploadImage(){
		return _compressionUploadImage;
	}
	
	protected JGDBConfigMap _dBConfigMap = new JGDBConfigMap();
	public JGDBConfigMap getDBConfigMap(){
		return _dBConfigMap;
	}
	
	protected HashMap<String, String> _customData = new HashMap<String, String>();
	public int sizeOfCustomData(){
		return _customData.size();
	}
	public void putCustomData(String key_, String value_){
		_customData.put(key_, value_);
	}
	public String getCustomData(String key_){
		return _customData.get(key_);
	}
	
	public JGMainConfig() throws Exception{
		getConfigBundle();
		
		//load common config
		String configNamePrefix_ = "jg.common.%s";

		try{
			setDebugLevel(Integer.valueOf(_configBundle.getString(String.format(configNamePrefix_, "debugLevel"))).intValue());
		}catch(Exception ex_){
			setDebugLevel(9);
		}
		
		_characterEncoding = _configBundle.getString(String.format(configNamePrefix_, "characterEncoding"));
		
		String rootPath_ = getClass().getResource("/").getPath();
		_serviceDirectoryPath = rootPath_+_configBundle.getString(String.format(configNamePrefix_, "serviceDirectoryPath"));
		_DBXMLQueryDirectoryPath = rootPath_+_configBundle.getString(String.format(configNamePrefix_, "DBXMLQueryDirectoryPath"));
		_fileUploadRootPath = _configBundle.getString(String.format(configNamePrefix_, "fileUploadRootPath"));
		_fileUploadRejectRegexp = _configBundle.getString(String.format(configNamePrefix_, "fileUploadRejectRegexp"));
		_compressionUploadImage = JGStringUtils.getBoolean(_configBundle.getString(String.format(configNamePrefix_, "compressionUploadImage")));
		
		//load Database connection configuration
		//get db config list
		configNamePrefix_ = "jg.db.config.%03d";
		for(int index_=0;index_<50;++index_){
				String targetName_ = String.format(configNamePrefix_, index_);
				
				String jdbcKeyName_ = targetName_+".JDBCClassName";
				if(!_configBundle.containsKey(jdbcKeyName_)){
					break;
				}
				
				try{
					String JDBCClassName_ = _configBundle.getString(jdbcKeyName_);
					String url_ = _configBundle.getString(targetName_+".url");
					String userName_ = _configBundle.getString(targetName_+".userName");
					String password_ = _configBundle.getString(targetName_+".password");
					String characterEncoding_ = _configBundle.getString(targetName_+".characterEncoding");
					
					_dBConfigMap.addDBConfig(new JGDBConfig(JDBCClassName_,url_,userName_,password_,characterEncoding_));
				}catch(Exception ex_){
					throw new Exception("not enough JGDBConfig parameters",ex_);
				}
				
		}
		
		configNamePrefix_ = "jg.custom.";
		Enumeration<String> allKeys_ = _configBundle.getKeys();
		while(allKeys_.hasMoreElements()){
			String KeyName_ = allKeys_.nextElement();
			if(KeyName_.indexOf(configNamePrefix_) >= 0){
				putCustomData(KeyName_, _configBundle.getString(KeyName_));
			}
		}
	}
	
	public class JGDBConfigMap{
		private ArrayList<JGDBConfig> _DBConfigList = new ArrayList<JGDBConfig>();
		protected void addDBConfig(JGDBConfig config_){
			_DBConfigList.add(config_);
		}
		public JGDBConfig getDBConfig(int index_){
			return _DBConfigList.get(index_);
		}
		
		protected String _XMLQueryFilePath = null;
		public String getXMLQueryFilePath(){
			return _XMLQueryFilePath;
		}
		
		protected JGDBConfigMap(){}
	}
}

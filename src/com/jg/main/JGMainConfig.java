package com.jg.main;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jg.db.JGDBConfig;
import com.jg.log.JGLog;
import com.jg.util.JGStringUtils;

public class JGMainConfig{
	
	static protected final String ELEMENT_COMMON = "common";
	static protected final String ELEMENT_DATABASE = "database";
	static protected final String ELEMENT_CUSTOM = "custom";
	static protected final String ELEMENT_OPTION = "option";
	
	static protected final String ATTR_NAME = "name";
	static protected final String ATTR_ISPRIMARY = "isPrimary";
	
	static protected final String ELEMENT_KEY_DEBUG_LEVEL = "debug-level";
	static protected final String ELEMENT_KEY_CHARACTER_ENCODING = "character-encoding";
	static protected final String ELEMENT_KEY_DIRPATH_SERVICE = "dirpath-service";
	static protected final String ELEMENT_KEY_DIRPATH_QUERY = "dirpath-query";
	static protected final String ELEMENT_KEY_FILE_ROOT_PATH = "file-root-path";
	static protected final String ELEMENT_KEY_FILE_REJECT_REGEXP = "file-reject-regexp";
	static protected final String ELEMENT_KEY_FILE_IMAGE_COMPRESSION = "file-image-compression";
	static protected final String ELEMENT_KEY_FILE_IMAGE_COMPRESSION_REGEXP = "file-image-compression-regexp";
	static protected final String ELEMENT_KEY_DB_JDBC_CLASS = "jdbc-class";
	static protected final String ELEMENT_KEY_DB_URL = "url";
	static protected final String ELEMENT_KEY_DB_USER_NAME = "user-name";
	static protected final String ELEMENT_KEY_DB_PASSWORD = "password";
	
	static private JGMainConfig _config = null;
	static public final JGMainConfig sharedConfig(){
		if(!isInitialized()){
			JGLog.error("JGMainConfig is not initialized", new NullPointerException());
			return null;
		}
		return _config;
	}
	static public final JGMainConfig makeSharedConfig(String configPath_) throws Exception{
		if(!isInitialized()){
			synchronized(JGMainConfig.class){
				try{
					_config = new JGMainConfig(configPath_);
				}catch(Exception ex_){
					throw new Exception("failed initialize JGMainConfig", ex_);
				}
				
				return _config;
			}
		}else throw new Exception("JGMainConfig already initialized");
	}
	
	static public final boolean isInitialized(){
		return (_config != null);
	}
	
	private Element _configElement = null;
	protected Element getConfigElement(String configPath_){
		if(_configElement == null){
			synchronized(JGMainConfig.class){
				try{
					
					_configElement = new SAXBuilder().build(new File(configPath_+"/JGConfig.xml")).getRootElement();
				}catch(Exception ex_){
					JGLog.error("failed to load JGConfig.xml", ex_);
					_configElement = null;
				}
			}
		}
		return _configElement;
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
	
	protected String _servicePath = null;
	protected void setServicePath(String path_){
		_servicePath = path_;
	}
	public String getServicePath(){
		return _servicePath;
	}
	
	protected String _queryPath = null;
	protected void setQueryPath(String path_){
		_queryPath = path_;
	}
	public String getQueryPath(){
		return _queryPath;
	}
	
	protected String _fileRootPath = null;
	protected void setFileRootPath(String path_){
		_fileRootPath = path_;
	}
	public String getFileRootPath(){
		return _fileRootPath;
	}
	protected String _fileRejectRegexp = null;
	protected void setFileRejectRegexp(String regexp_){
		_fileRejectRegexp = regexp_;
	}
	public String getFileRejectRegexp(){
		return _fileRejectRegexp;
	}
	protected boolean _fileImageCompression = false;
	protected void setFileImageCompression(boolean bool_){
		_fileImageCompression = bool_;
	}
	public boolean isCompressionImage(){
		return _fileImageCompression;
	}
	
	protected String _fileImageCompressionRegexp = "*";
	protected void setFileImageCompressionRegexp(String pattern_){
		_fileImageCompressionRegexp = pattern_;
	}
	public String getFileImageCompressionRegexp(){
		return _fileImageCompressionRegexp;
	}
	
	protected HashMap<String, JGDBConfig> _dBConfigMap = new HashMap<String, JGDBConfig>();
	public HashMap<String, JGDBConfig> getDBConfigMap(){
		return _dBConfigMap;
	}
	protected String _primaryDBName = null;
	protected void setPrimaryDBName(String name_){
		_primaryDBName = name_;
	}
	public String getPrimaryDBName(){
		return _primaryDBName;
	}
	public JGDBConfig getDBConfig(String name_){
		return _dBConfigMap.get(name_);
	}
	public JGDBConfig getDBConfig(){
		return getDBConfig(_primaryDBName);
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
	
	protected String _configPath = null;
	public final String getConfigPath(){
		return _configPath;
	}
	
	public JGMainConfig(String configPath_) throws Exception{
		_configPath = configPath_;
		getConfigElement(configPath_);
		reload();
	}
	
	public void reload() throws Exception{
		Element commonElement_ = _configElement.getChild(ELEMENT_COMMON);
		List<?> dbList_ = _configElement.getChildren(ELEMENT_DATABASE);
		List<?> customList_ = _configElement.getChild(ELEMENT_CUSTOM).getChildren(ELEMENT_OPTION);
		
		try{
			setDebugLevel(Integer.valueOf(commonElement_.getChild(ELEMENT_KEY_DEBUG_LEVEL).getValue()).intValue());
		}catch(Exception ex_){
			setDebugLevel(9);
		}
		
		_characterEncoding = commonElement_.getChild(ELEMENT_KEY_CHARACTER_ENCODING).getValue();
		_servicePath = _configPath+"/"+commonElement_.getChild(ELEMENT_KEY_DIRPATH_SERVICE).getValue();
		_queryPath = _configPath+"/"+commonElement_.getChild(ELEMENT_KEY_DIRPATH_QUERY).getValue();
		_fileRootPath = commonElement_.getChild(ELEMENT_KEY_FILE_ROOT_PATH).getValue();
		_fileRejectRegexp = commonElement_.getChild(ELEMENT_KEY_FILE_REJECT_REGEXP).getValue();
		_fileImageCompression = JGStringUtils.getBoolean(commonElement_.getChild(ELEMENT_KEY_FILE_IMAGE_COMPRESSION).getValue(), true);
		_fileImageCompressionRegexp = commonElement_.getChild(ELEMENT_KEY_FILE_IMAGE_COMPRESSION_REGEXP).getValue();
		
		Iterator<?> dbIterator_ = dbList_.iterator();
		while(dbIterator_.hasNext()){
			Element dbConfigElement_ = (Element)dbIterator_.next();
			
			String dbName_ = dbConfigElement_.getAttributeValue(ATTR_NAME);
			boolean isPrimary_ = JGStringUtils.getBoolean(dbConfigElement_.getAttributeValue(ATTR_ISPRIMARY), false);
			
			String jdbcClassName_ = dbConfigElement_.getChild(ELEMENT_KEY_DB_JDBC_CLASS).getValue();
			String url_ = dbConfigElement_.getChild(ELEMENT_KEY_DB_URL).getValue();
			String userName_ = dbConfigElement_.getChild(ELEMENT_KEY_DB_USER_NAME).getValue();
			String password_ = dbConfigElement_.getChild(ELEMENT_KEY_DB_PASSWORD).getValue();
			String characterEncoding_ = dbConfigElement_.getChild(ELEMENT_KEY_CHARACTER_ENCODING).getValue();
			
			_dBConfigMap.put(dbName_, new JGDBConfig(jdbcClassName_, url_, userName_, password_, characterEncoding_));
			if(isPrimary_ || _primaryDBName == null){
				_primaryDBName = dbName_;
			}
		}
		
		Iterator<?> customIterator_ = customList_.iterator();
		while(customIterator_.hasNext()){
			Element customElement_ = (Element)customIterator_.next();
			String name_ = customElement_.getAttributeValue(ATTR_NAME);
			String value_ = customElement_.getValue();
			putCustomData(name_, value_);
		}
	}
}

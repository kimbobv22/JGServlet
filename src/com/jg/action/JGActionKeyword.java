package com.jg.action;

public class JGActionKeyword{
	
	static public final String STR_SERVICEMAP = "srvMap";
	static public final String STR_SERVICEID = "srvID";
	static public final String STR_SERVICEID_SPLITTER = ".";
	static public final String STR_SERVICEID_SPLITTER_REGX = "\\"+STR_SERVICEID_SPLITTER; 
	
	static public final String STR_ELEMENT_SERVICESET = "serviceList";
	static public final String STR_ELEMENT_SERVICE = "service";
	static public final String STR_ELEMENT_ACTIONCLASSES = "actionClasses";
	static public final String STR_ELEMENT_CLASS = "class";
	static public final String STR_ELEMENT_RESULTPAGES = "resultPages";
	static public final String STR_ELEMENT_PAGE = "page";
	static public final String STR_ELEMENT_RESULT = "result";
	static public final String STR_ELEMENT_FILTERS = "filters";
	static public final String STR_ELEMENT_FILTER = "filter";
	
	static public final String STR_ATTRIBUTE_ISPRIMARY = "isPrimary";
	static public final String STR_ATTRIBUTE_NAME = "name";
	//static public final String STR_ATTRIBUTE_MAPPINGCLASS = "mappingClass";
	static public final String STR_ATTRIBUTE_SERVICEID = "serviceID";
	static public final String STR_ATTRIBUTE_FSERVICEID = "forwardServiceID";
	static public final String STR_ATTRIBUTE_ACTIONCLASSNAME = "actionClassName";
	static public final String STR_ATTRIBUTE_MAPPINGMETHOD = "mappingMethod";
	static public final String STR_ATTRIBUTE_CODE = "code";
	static public final String STR_ATTRIBUTE_PAGENAME = "pageName";
	static public final String STR_ATTRIBUTE_ISREDIRECT = "isRedirect";
	static public final String STR_ATTRIBUTE_ISPRIVATE = "isPrivate";
	static public final String STR_ATTRIBUTE_LOCALFILTER = "localFilter";
	
	static public final String STR_REGEX_MAPPING_STRING = "[\\w-\\.]";
	static private final String _STR_REGEX_MAPPING_STRING_GROUP = "\\{"+STR_REGEX_MAPPING_STRING+"+\\}";
	
	static public final String STR_REGEX_MAPPING_PARAMETER = "[$]"+_STR_REGEX_MAPPING_STRING_GROUP;
	static public final String STR_REGEX_MAPPING_ATTRIBUTE = "[#]"+_STR_REGEX_MAPPING_STRING_GROUP;
}

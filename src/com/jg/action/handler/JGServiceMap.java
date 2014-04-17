package com.jg.action.handler;

import java.util.ArrayList;

public class JGServiceMap{
	public class JGActionClassDef{
		protected String _name = null;
		public String getName(){
			return _name;
		}
		
		protected String _mappingClass = null;
		public String getMappingClass(){
			return _mappingClass;
		}
		
		public JGActionClassDef(String name_, String mappingClass_){
			_name = name_;
			_mappingClass = mappingClass_;
		}
	}
	
	protected ArrayList<JGServiceMap.JGActionClassDef> _actionClassDefs = new ArrayList<JGServiceMap.JGActionClassDef>();
	public int sizeOfActionClassDefs(){
		return _actionClassDefs.size();
	}
	protected void addActionClassDef(JGServiceMap.JGActionClassDef actionClassDef_){
		int index_ = indexOfActionClassDef(actionClassDef_.getName());
		if(index_ >= 0){
			removeActionClassDef(index_);
		}
		_actionClassDefs.add(actionClassDef_);
	}
	protected JGActionClassDef addActionClassDef(String name_, String mappingClass_){
		JGActionClassDef actionClass_ = new JGActionClassDef(name_, mappingClass_);
		addActionClassDef(actionClass_);
		return actionClass_;
	}
	protected void removeActionClassDef(int index_){
		_actionClassDefs.remove(index_);
	}
	protected void removeActionClassDef(JGActionClassDef actionClassDef_){
		removeActionClassDef(indexOfActionClassDef(actionClassDef_));
	}
	protected void removeActionClassDef(String name_){
		removeActionClassDef(indexOfActionClassDef(name_));
	}
	public JGActionClassDef getActionClassDef(int index_){
		return _actionClassDefs.get(index_);
	}
	public JGActionClassDef getActionClassDef(String name_){
		return getActionClassDef(indexOfActionClassDef(name_));
	}
	
	public int indexOfActionClassDef(JGActionClassDef actionClassDef_){
		return _actionClassDefs.indexOf(actionClassDef_);
	}
	public int indexOfActionClassDef(String name_){
		int count_ = _actionClassDefs.size();
		for(int index_=0;index_<count_;++index_){
			if(_actionClassDefs.get(index_).getName().equals(name_)){
				return index_;
			}
		}
		
		return -1;
	}

	public class JGResultPageDef{
		protected String _name = null;
		public String getName(){
			return _name;
		}
		
		protected String _page = null;
		public String getPage(){
			return _page;
		}
		
		public JGResultPageDef(String name_, String page_){
			_name = name_;
			_page = page_;
		}
	}
	
	protected ArrayList<JGServiceMap.JGResultPageDef> _resultPageDefs = new ArrayList<JGServiceMap.JGResultPageDef>();
	
	protected String _name = null;
	public String getName(){
		return _name;
	}
	
	protected boolean _isPrimary = false;
	public boolean isPrimary(){
		return _isPrimary;
	}
	protected void setPrimary(boolean bool_){
		_isPrimary = bool_;
	}
	public JGServiceMap(String mapName_, boolean isPrimary_){
		_name = mapName_;
		_isPrimary = isPrimary_;
	}
	
	public int sizeOfResultPageDefs(){
		return _resultPageDefs.size();
	}
	
	protected void addResultPageDef(JGResultPageDef resultPageDef_){
		int index_ = indexOfResultPageDef(resultPageDef_.getName());
		if(index_ >= 0){
			removeResultPageDef(index_);
		}
		_resultPageDefs.add(resultPageDef_);
	}
	protected JGResultPageDef addResultPageDef(String name_, String page_){
		JGResultPageDef resultPageDef_ = new JGResultPageDef(name_, page_);
		addResultPageDef(resultPageDef_);
		return resultPageDef_;
	}
	
	protected void removeResultPageDef(int index_){
		_resultPageDefs.remove(index_);
	}
	protected void removeResultPageDef(JGResultPageDef resultPageDef_){
		removeResultPageDef(indexOfResultPageDef(resultPageDef_));
	}
	protected void removeResultPageDef(String name_){
		removeResultPageDef(indexOfResultPageDef(name_));
	}
	
	public JGResultPageDef getResultPageDef(int index_){
		return _resultPageDefs.get(index_);
	}
	public JGResultPageDef getResultPageDef(String name_){
		return getResultPageDef(indexOfResultPageDef(name_));
	}
	
	public int indexOfResultPageDef(JGResultPageDef resultPageDef_){
		return _resultPageDefs.indexOf(resultPageDef_);
	}
	public int indexOfResultPageDef(String name_){
		int count_ = _resultPageDefs.size();
		for(int index_=0;index_<count_;++index_){
			if(_resultPageDefs.get(index_).getName().equals(name_)){
				return index_;
			}
		}
		
		return -1;
	}
	
	private ArrayList<JGService> _services = new ArrayList<JGService>();
	
	public int sizeOfService(){
		return _services.size();
	}
	
	protected void addService(JGService service_){
		int index_ = indexOfService(service_.getServiceKey().getServiceID());
		if(index_ >= 0){
			removeService(index_);
		}
		_services.add(service_);
		service_.setParentMap(this);
	}
	protected void removeService(int index_){
		JGService service_ = getService(index_);
		_services.remove(index_);
		service_.setParentMap(null);
	}
	protected void removeService(JGService service_){
		removeService(indexOfService(service_));
	}
	protected void removeService(String serviceID_){
		removeService(indexOfService(serviceID_));
	}
	public JGService getService(int index_){
		return _services.get(index_);
	}
	public JGService getService(String serviceID_){
		return getService(indexOfService(serviceID_));
	}
	public JGService getPrimaryService(){
		int length_ = _services.size();
		for(int index_=0;index_<length_;++index_){
			JGService service_ = getService(index_);
			if(service_.isPrimary()){
				return service_;
			}
		}
		
		return null;
	}
	
	public int indexOfService(JGService service_){
		return _services.indexOf(service_);
	}
	public int indexOfService(String serviceID_){
		int count_ = _services.size();
		for(int index_=0;index_<count_;++index_){
			JGService service_ = _services.get(index_);
			if(service_.getServiceKey().getServiceID().equals(serviceID_)){
				return index_;
			}
		}
		
		return -1;
	}
	
	protected ArrayList<JGVirtualMap> _virtualMaps = new ArrayList<JGVirtualMap>();
	public int sizeOfVirtualMap(){
		return _virtualMaps.size();
	}
	
	protected void addVirtualMap(JGVirtualMap map_){
		_virtualMaps.add(map_);
	}
	protected void removeVirtualMap(int index_){
		_virtualMaps.remove(index_);
	}
	protected void removeVirtualMap(JGVirtualMap map_){
		removeVirtualMap(indexOfVirtualMap(map_));
	}
	public JGVirtualMap getVirtualMap(int index_){
		return _virtualMaps.get(index_);
	}
	
	public int indexOfVirtualMap(JGVirtualMap map_){
		return _virtualMaps.indexOf(map_);
	}
	
	public JGVirtualMap getVirtualMapWithTest(String str_){
		int length_ = _virtualMaps.size();
		for(int index_=0;index_<length_;++index_){
			JGVirtualMap virtualMap_ = _virtualMaps.get(index_);
			if(virtualMap_.test(str_)) return virtualMap_;
		}
		
		return null;
	}
}

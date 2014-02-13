package com.jg.filter;

import java.util.ArrayList;

public class JGFilterChain<T,P> {

	protected T _target = null;
	public T getTarget(){
		return _target;
	}
	public JGFilterChain(T target_){
		_target = target_;
	}
	
	final private ArrayList<JGFilterDef<T,P>> _filterList = new ArrayList<JGFilterDef<T,P>>();
	public ArrayList<JGFilterDef<T,P>> getFilterList(){
		return _filterList;
	}
	public int size(){
		return _filterList.size();
	}
	
	public JGFilterChain<T,P> insertFilter(int toIndex_, JGFilterDef<T,P> filter_){
		_filterList.add(toIndex_, filter_);
		return this;
	}
	public JGFilterChain<T,P> addFilter(JGFilterDef<T,P> filter_){
		_filterList.add(filter_);
		return this;
	}
	public JGFilterChain<T,P> removeFilter(int index_){
		_filterList.remove(index_);
		return this;
	}
	public JGFilterChain<T,P> removeFilter(JGFilterDef<T,P> filter_){
		_filterList.remove(filter_);
		return this;
	}
	public JGFilterDef<T,P> getFilter(int index_){
		return _filterList.get(index_);
	}
	
	private int _currentIndex = -1;
	public int getCurrentIndex(){
		return _currentIndex;
	}
	
	public void doFilter(P parameter_) throws Exception{
		++_currentIndex;
		if(_currentIndex < 0 || _currentIndex >= size()){
			reset();
			return;
		}
		JGFilterDef<T,P> filter_ = getFilter(_currentIndex);
		if(filter_.acceptFilter(_target, this, parameter_)){
			getFilter(_currentIndex).doFilter(_target,this,parameter_);
		}
	}
	public void reset(){
		_currentIndex = -1;
	}

}

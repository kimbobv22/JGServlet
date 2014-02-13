package com.jg.action.handler;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.jg.action.JGAction;

public class JGActionSessionMap {

	static private UUID _sessionKey = UUID.randomUUID(); 
	protected HashMap<Class<? extends JGAction>, JGAction> _actionMap = new HashMap<Class<? extends JGAction>, JGAction>();
	
	static public JGActionSessionMap actionSessionMap(HttpSession session_){
		return (JGActionSessionMap)session_.getAttribute(_sessionKey.toString());
	}
	static protected void putActionMap(HttpSession session_, JGActionSessionMap actionMap_){
		session_.setAttribute(_sessionKey.toString(),actionMap_);
	}
	
	protected void putAction(JGAction action_){
		Class<? extends JGAction> actionClass_ = (Class<? extends JGAction>)action_.getClass();
		synchronized(_actionMap){
			_actionMap.put(actionClass_, action_);
		}
	}
	public JGAction getAction(Class<? extends JGAction> actionClass_){
		return _actionMap.get(actionClass_);
	}
	public JGAction getAction(String actionClassName_) throws ClassNotFoundException{
		@SuppressWarnings("unchecked")
		Class<? extends JGAction>actionClass_ = (Class<? extends JGAction>)Class.forName(actionClassName_);
		return getAction(actionClass_);
	}
	
	public void removeAction(Class<? extends JGAction> actionClass_){
		_actionMap.remove(actionClass_);
	}
	public void removeAction(String actionClassName_) throws ClassNotFoundException{
		@SuppressWarnings("unchecked")
		Class<? extends JGAction>actionClass_ = (Class<? extends JGAction>)Class.forName(actionClassName_);
		removeAction(actionClass_);
	}
	
}

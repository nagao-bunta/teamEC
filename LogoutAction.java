package com.internousdev.django.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware {
	private Map<String, Object> session;

	public String execute() {
		String userId = String.valueOf(session.get("userId"));
		String savedUserIdFlgCheck = String.valueOf(session.get("savedUserIdFlg"));
		boolean savedUserIdFlg = "null".equals(savedUserIdFlgCheck)? false : Boolean.valueOf(savedUserIdFlgCheck);
		session.clear();
		if (savedUserIdFlg) {
			session.put("savedUserIdFlg", savedUserIdFlg);
			session.put("userId", userId);
		}
		return SUCCESS;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

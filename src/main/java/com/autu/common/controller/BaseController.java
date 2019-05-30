package com.autu.common.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.autu.common.agentUser.AgentUserService;
import com.autu.common.model.AgentUser;
import com.autu.common.model.User;
import com.autu.user.LoginService;
import com.autu.user.UserService;
import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;

public class BaseController extends Controller {

	private User loginUser = null;
	private AgentUser agentUser=null;
 
	public static AgentUserService agentUserService=Aop.get(AgentUserService.class);
	 
	public static UserService userService=Aop.get(UserService.class);
 
	public static LoginService loginService=Aop.get(LoginService.class);
	
	@NotAction
	public void setAgentUser(AgentUser agentUser) {
		this.agentUser=agentUser;
	}
	@NotAction
	public AgentUser getAgentUser() {
		return agentUser;
	}
	@NotAction
	public boolean isAjax() {
		String requestType = getRequest().getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestType);
	}
	@NotAction
	public User getLoginUser() {
 		return loginUser;
	}
	@NotAction
	public void setLoginUser(User user) {
		this.loginUser=user;
	}
	@NotAction
	public boolean isLogin() {
		return getLoginUser() != null;
	}
	@NotAction
	public boolean notLogin() {
		return !isLogin();
	}
	@NotAction
	public <T> List<T> getModelList(Class<T> modelClass, String modelName, boolean skipConvertError) {
		Pattern p = Pattern.compile(modelName + "\\[\\d+\\].[a-zA-z0-9]+");
		Map<String, String[]> parasMap = getRequest().getParameterMap();
		String paraKey = null;
		List<T> resultList = new ArrayList<T>();
		String itemModelName = null;
		for (Entry<String, String[]> e : parasMap.entrySet()) {
			paraKey = e.getKey();
			if (p.matcher(paraKey).find()) {
				itemModelName = paraKey.split("\\.")[0];
				resultList.add(getModel(modelClass, itemModelName, skipConvertError));
			}
		}
		return resultList;
	}

	@NotAction
	public <T> List<T> getModelList(Class<T> modelClass, String modelName) {
		boolean skipConvertError = false;
		Pattern p = Pattern.compile(modelName + "\\[\\d+\\].[a-zA-z0-9]+");
		Map<String, String[]> parasMap = getRequest().getParameterMap();
		String paraKey;
		Set<String> modelPrefix = new HashSet<String>();
		for (Entry<String, String[]> e : parasMap.entrySet()) {
			paraKey = e.getKey();
			if (p.matcher(paraKey).find()) {
				modelPrefix.add(paraKey.split("\\.")[0]);
			}
		}
		List<T> resultList = new ArrayList<T>();
		for (String modelName2 : modelPrefix) {
			resultList.add(getModel(modelClass, modelName2, skipConvertError));
		}
		return resultList;
	}
	
	@NotAction
	public void notTheme() {
		setAttr("AUTU_BLOG_NOT_THEME", true);
	}
}

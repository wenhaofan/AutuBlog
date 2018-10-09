package com.autu.common.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.autu.agentUser.AgentUserService;
import com.autu.common.model.entity.AgentUser;
import com.autu.common.model.entity.User;
import com.autu.user.LoginService;
import com.autu.user.UserService;
import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.kit.StrKit;

public class BaseController extends Controller {

	private User loginUser = null;
	
 
	public static AgentUserService agentUserService=Aop.get(AgentUserService.class);
	 
	public static UserService userService=Aop.get(UserService.class);
 
	public static LoginService loginService=Aop.get(LoginService.class);
	
	@NotAction
	public AgentUser getAgentUser() {
		AgentUser agentUser = null;
	 
		String cookie = getCookie(AgentUserService.AGENT_USER_COOKIE_KEY);

		if (StrKit.notBlank(cookie) && getLoginUser() == null) {
			agentUser = agentUserService.get(cookie);
			if (agentUser != null) {
				return agentUser;
			}
		}

		cookie = StrKit.getRandomUUID();
		if (getLoginUser() != null) {
			agentUser = agentUserService.get(-1);
			if (agentUser == null)
				agentUser = new AgentUser();
			String userName = getLoginUser().getName();
			agentUser.setId(-1);
			agentUser.setName(StrKit.isBlank(userName) ? "系统管理员" : userName);
			agentUser.setEmail(getLoginUser().getEmail());
		} else {
			agentUser = new AgentUser();
		}
		agentUser.setCookie(cookie);

		if (agentUser.getId() != null) {
			agentUserService.update(agentUser);
		} else {
			agentUserService.save(agentUser);
		}

		// 设置cookie
		setCookie(AgentUserService.AGENT_USER_COOKIE_KEY, cookie, AgentUserService.AGENT_USER_COOKIE_AGE);
		return agentUser;

	}
	@NotAction
	public boolean isAjax() {
		String requestType = getRequest().getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestType);
	}
	@NotAction
	public User getLoginUser() {

		if (loginUser != null) {
			return loginUser;
		}

		String sessionId = getCookie(LoginService.sessionIdName);

		if (sessionId != null) {
			// 通过sessionId从缓存中获取登录用户
			User loginUser = loginService.getUserWithSessionId(sessionId);
			// 如果依然为空则从数据库中寻找有效的登录用户
			if (loginUser == null) {
				loginUser = loginService.loginWithSessionId(sessionId);
			}

			if (loginUser != null) {
				setAttr(LoginService.loginUserKey, loginUser);
			} else {
				// 为空则表示cookie无用，删之
				removeCookie(LoginService.sessionIdName);
				renderError(404);
			}
		}

		return getAttr(LoginService.loginUserKey);
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
}

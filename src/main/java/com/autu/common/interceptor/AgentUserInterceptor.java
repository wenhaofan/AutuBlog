package com.autu.common.interceptor;

import com.autu.common.agentUser.AgentUserService;
import com.autu.common.controller.BaseController;
import com.autu.common.model.AgentUser;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

public class AgentUserInterceptor implements Interceptor{

	@Inject
	private AgentUserService agentUserService;
	
	@Override
	public void intercept(Invocation inv) {
	BaseController c=(BaseController) inv.getController();
 
		AgentUser agentUser = c.getAgentUser();
		//判断是否为爬虫请求，如果为爬虫请求则不产生agentUser
		String userAgent=c.getRequest().getHeader("User-Agent");
		if(StrKit.notBlank(userAgent)) {
			if(userAgent.contains("bot")
					||userAgent.contains("Bot")
					||userAgent.contains("crawler")) {
					inv.invoke();
					return;
			}
		}
		
		
		String cookie = c.getCookie(AgentUserService.AGENT_USER_COOKIE_KEY);
		 
		if(StrKit.notBlank(cookie)&&agentUser==null) {
		 
				agentUser = agentUserService.get(cookie);
			 
		} 
		
		if(agentUser==null) {
			//cookie存在但是没有对应的用户则删掉cookie
			c.removeCookie(AgentUserService.AGENT_USER_COOKIE_KEY);
		}
		if(agentUser==null) {
			
			if(c.getLoginUser()==null) {
				agentUser = new AgentUser();
			}else {
				agentUser = agentUserService.get(-1);
				if (agentUser == null) {
					agentUser = new AgentUser();
				}
				String userName = c.getLoginUser().getName();
			 
				agentUser.setName(StrKit.isBlank(userName) ? "系统管理员" : userName);
				agentUser.setEmail(c.getLoginUser().getEmail());
			}
			
			cookie = StrKit.getRandomUUID();
			agentUser.setCookie(cookie);

			agentUserService.saveOrUpdate(agentUser);

			// 设置cookie
			c.setCookie(AgentUserService.AGENT_USER_COOKIE_KEY, cookie, AgentUserService.AGENT_USER_COOKIE_AGE); 
		}
		
 
		c.setAgentUser(agentUser);
		inv.invoke();
	}
}

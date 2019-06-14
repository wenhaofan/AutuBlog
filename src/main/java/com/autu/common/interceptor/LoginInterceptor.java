package com.autu.common.interceptor;

import com.autu.common.controller.BaseController;
import com.autu.common.model.User;
import com.autu.user.LoginService;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.Ret;

public class LoginInterceptor implements Interceptor{

	@Inject
	private LoginService loginService;
	
	/**
	 * 后台权限验证,未登录则不能访问
	 */
	public void intercept(Invocation inv) {
		
		BaseController c=(BaseController)inv.getController();
	
	    User loginUser=null;
		
	    String sessionId = c.getCookie(LoginService.sessionIdName);

		if (sessionId != null) {
			// 通过sessionId从缓存中获取登录用户
			    loginUser = loginService.getUserWithSessionId(sessionId);
			// 如果依然为空则从数据库中寻找有效的登录用户
			if (loginUser == null) {
				loginUser = loginService.loginWithSessionId(sessionId);
			}

			if (loginUser != null) {
				c.setLoginUser(loginUser);
			} else {
				// 为空则表示cookie无用，删之
				c.removeCookie(LoginService.sessionIdName);
			}
		}
 	    
		if(loginUser!=null) {
			c.setLoginUser(loginUser);
			inv.invoke();
		}else {
			String actionKey=inv.getActionKey();
			//只有登陆了才能访问后台
			if(!actionKey.contains("/admin")) {
				inv.invoke();
				return;
			}
			
			if(c.isAjax()) {
				c.renderJson(Ret.fail("msg", "请登录后访问！"));
			}else {
				c.redirect("/login");
			}
			
		}
		
		
	}

	
}

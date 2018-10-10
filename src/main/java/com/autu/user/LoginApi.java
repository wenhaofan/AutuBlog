package com.autu.user;

import com.autu.agentUser.AgentUserService;
import com.autu.common.controller.BaseController;
import com.autu.common.kit.IpKit;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月6日 下午12:38:51
*/
@Clear
public class LoginApi extends BaseController {

	@Inject
	private LoginService loginService;

	public void index() {
		if(isLogin()) {
			renderJson(Ret.ok());
		}
		String account=getPara("ac");
		String pwd=getPara("pwd");
		String ip=IpKit.getRealIp(getRequest());
		
		boolean isKepp=getParaToBoolean("k",false);
		
		Ret ret=loginService.login(account,pwd,isKepp,ip);
		
		if(ret.isOk()) {
			String sessionId=ret.getStr(LoginService.sessionIdName);
			int maxAge=ret.getInt("cookieMaxAge");
			setAttr(LoginService.loginUserKey,ret.get(LoginService.loginUserKey));
			setCookie(LoginService.sessionIdName,sessionId, maxAge,"/",PropKit.get("domain"),true);
			removeCookie(AgentUserService.AGENT_USER_COOKIE_KEY);
			renderJson(Ret.ok());
			return ;
		}
		
		renderJson(ret);	
	}
	
	
}

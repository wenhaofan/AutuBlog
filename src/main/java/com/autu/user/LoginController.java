package com.autu.user;

import com.autu.common.controller.BaseController;
import com.autu.common.interceptor.LoginInterceptor;
import com.autu.index.IndexSeoInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;

/**
 * 登录
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
@Clear(LoginInterceptor.class)
@Before(IndexSeoInterceptor.class)
public class LoginController extends BaseController{

	public void index(){
		if(isLogin()) {
			redirect("/admin");
			return;
		}
		render("login.html");
	}

	public void logout() {
		removeCookie(LoginService.sessionIdName);
		redirect("/");
	}
	
}


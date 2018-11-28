package com.autu._admin.loginRecord;

import com.autu.common.controller.BaseController;
import com.autu.user.LoginService;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年11月26日 上午11:04:56
*/
public class LoginRecordController extends BaseController {

	public void index() {
		setAttr("currentSessionId", getCookie(LoginService.sessionIdName));
		render("loginRecord.html");
	}
	
}

package com.autu._admin.user;

import com.autu.common.annotation.SysLog;
import com.autu.common.aop.Inject;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.User;

public class AdminUserApi  extends BaseController{

	@Inject
	private AdminUserService service;
	
	@SysLog(value="修改密码",action="user")
	public void editPwd() {
		renderJson(service.editPassword(getLoginUser(), getPara("oldPwd"), getPara("newPwd")));
	}
	@SysLog(value="修改用户信息",action="user")
	public void editInfo() {
		User user=getModel(User.class,"",true);
		renderJson(service.update(user));
	}
}

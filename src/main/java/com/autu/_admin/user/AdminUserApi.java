package com.autu._admin.user;

import com.autu.common.annotation.SysLogInfo;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.User;
import com.jfinal.aop.Inject;

public class AdminUserApi  extends BaseController{

	@Inject
	private AdminUserService service;
	
	@SysLogInfo(value="修改密码",action="user")
	public void editPwd() {
		renderJson(service.editPassword(getLoginUser(), getPara("oldPwd"), getPara("newPwd")));
	}
	@SysLogInfo(value="修改用户信息",action="user")
	public void editInfo() {
		User user=getModel(User.class,"",true);
		renderJson(service.update(user));
	}
}

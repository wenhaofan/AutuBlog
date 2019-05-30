package com.autu._admin.user;

import com.autu.common.controller.BaseController;
import com.autu.common.model.User;
import com.jfinal.aop.Inject;

public class AdminUserApi  extends BaseController{

	@Inject
	private AdminUserService service;
 
	public void editPwd() {
		renderJson(service.editPassword(getLoginUser(), getPara("oldPwd"), getPara("newPwd")));
	}
 
	public void editInfo() {
		User user=getModel(User.class,"",true);
		renderJson(service.update(user));
	}
}

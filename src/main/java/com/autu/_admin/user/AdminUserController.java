package com.autu._admin.user;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;

public class AdminUserController extends BaseController {

	@Inject
	private AdminUserService service;
	
	
	public void edit() {
		setAttr("user", service.getAdminUser());
		render("bloggerInfo.html");
	}
}

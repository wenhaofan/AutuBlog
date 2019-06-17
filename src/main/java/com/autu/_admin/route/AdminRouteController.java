package com.autu._admin.route;

import com.autu.common.controller.BaseController;

public class AdminRouteController extends BaseController{

	public void list() {
		render("routeList.html");
	}
	
	public void edit() {
		set("id", getPara());
		render("edit.html");
	}
	
	
	
}

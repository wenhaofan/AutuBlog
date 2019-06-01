package com.autu._admin.route;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminRouteApi extends BaseController {

	@Inject
	private AdminRouteService routeService;
	
	public void list() {
		renderJson(Ret.ok("status", 
				Ret.create("code",200).set("message","操作成功")
				).set("data", routeService.listAll()));
	}
	
	
}

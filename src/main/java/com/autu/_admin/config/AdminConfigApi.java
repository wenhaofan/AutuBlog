package com.autu._admin.config;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Config;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminConfigApi  extends BaseController {

	@Inject
	private AdminConfigService service;
	
	public void index() {
		renderJson(Ret.ok("config", service.get()));
	}
	
	public void edit() {
		renderJson(service.saveOrUpdate(getModel(Config.class,"",true)));
	}
}

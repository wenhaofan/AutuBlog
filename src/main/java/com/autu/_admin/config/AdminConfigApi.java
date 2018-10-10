package com.autu._admin.config;

import com.autu.common.annotation.SysLog;
import com.autu.common.aop.Inject;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Config;
import com.jfinal.kit.Ret;

public class AdminConfigApi  extends BaseController {

	@Inject
	private AdminConfigService service;
	
	public void index() {
		renderJson(Ret.ok("config", service.get()));
	}
	
	@SysLog(value="编辑系统配置",action="update")
	public void edit() {
		renderJson(service.saveOrUpdate(getBean(Config.class,"")));
	}
}

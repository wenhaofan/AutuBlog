package com.autu._admin.sysLog;

import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.SysLog;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

public class AdminSysLogApi extends BaseController {

	@Inject
	private AdminSysLogService service;
	
	public void listRecent() {
		Page<SysLog> logPage=service.listRecent(getParaToInt("page", 1), getParaToInt("limit",8));
		renderJson(Ret.ok("logPage", logPage));
	}
	
	public void page() {
		Integer pageNum = getParaToInt("page",1);
		Integer limit = getParaToInt("limit",10);
		Page<SysLog> sysLogPage=service.page(pageNum, limit, getBean(QuerySysLog.class,"",true));
		renderJson(Ret.ok("page", sysLogPage));
	}
	
}

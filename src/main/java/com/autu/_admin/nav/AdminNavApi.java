package com.autu._admin.nav;

import com.autu.common.annotation.SysLogInfo;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Nav;
import com.autu.nav.NavService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminNavApi extends BaseController{

	@Inject
	private AdminNavService  service;
	@Inject
	private NavService frontService;
	
	public void list() {
		renderJson(Ret.ok("list", frontService.list()));
	}
	
	public void get() {
		Nav nav=service.get(getParaToInt());
		renderJson(Ret.ok("nav", nav));
	}
	@SysLogInfo(value="删除导航",action="delete")
	public void delete() {
		renderJson(service.delete(getParaToInt("toId")));
	}
	@SysLogInfo(value="更新导航",action="update")
	public void update() {
		Nav nav=getBean(Nav.class,"",true);
		renderJson(service.update(nav));
	}
	@SysLogInfo(value="添加导航",action="save")
	public void add() {
		Nav nav =getModel(Nav.class,"",true);
		renderJson(service.save(nav));
	}
	
}

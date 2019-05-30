package com.autu._admin.nav;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Nav;
import com.autu.common.nav.NavService;
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
	 
	public void delete() {
		renderJson(service.delete(getParaToInt("toId")));
	}
 
	public void update() {
		Nav nav=getBean(Nav.class,"",true);
		renderJson(service.update(nav));
	}
	 
	public void add() {
		Nav nav =getModel(Nav.class,"",true);
		renderJson(service.save(nav));
	}
	
}

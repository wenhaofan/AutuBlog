package com.autu._admin.page;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;

public class AdminPageController extends BaseController{
	
	@Inject
	private AdminPageService pageService;
	
	public void list() {
		render("pageList.html");
	}
	
	public void edit() {
		set("page", pageService.get(getParaToInt()));
		render("pageEdit.html");
	}
	
	public void add() {
		render("pageEdit");
	}
 
}

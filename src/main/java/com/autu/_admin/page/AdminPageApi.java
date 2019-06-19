package com.autu._admin.page;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Page;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminPageApi extends BaseController {

	@Inject
	private AdminPageService pageService;
	
	public void list() {
		Page query = getModel(Page.class, "", true);
 
		Integer pageNum = getParaToInt("page");
		Integer pageSize = getParaToInt("limit",10);
		
		renderJson(Ret.ok("page", pageService.page(pageNum, pageSize, query)));
	}
	
	public void delete() {
		renderJson(pageService.delete(getParaToInt())?Ret.ok():Ret.fail());
	}
	
	public void bindArticle() {
		Integer articleId=getParaToInt("articleId");
		Integer routeId=getParaToInt();
		
		
		renderJson(pageService.bindArticle(articleId, routeId)?Ret.ok():Ret.fail());
	}
	
	public void edit() {
		Page page=getBean(Page.class,"",true);
		
		renderJson(pageService.addOrUpdate(page)?Ret.ok("page",page):Ret.fail());
	}
	
	public void content() {
		renderJson(Ret.ok("content", pageService.getContent(getParaToInt())));
	}
}

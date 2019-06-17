package com.autu._admin.route;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Route;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminRouteApi extends BaseController {

	@Inject
	private AdminRouteService routeService;
	
	public void list() {
		Route query = getModel(Route.class, "", true);
 
		Integer pageNum = getParaToInt("page");
		Integer pageSize = getParaToInt("limit",10);
		
		renderJson(Ret.ok("page", routeService.page(pageNum, pageSize, query)));
	}
	
	public void delete() {
		renderJson(routeService.delete(getParaToInt())?Ret.ok():Ret.fail());
	}
	
	public void bindArticle() {
		Integer articleId=getParaToInt("articleId");
		Integer routeId=getParaToInt();
		
		
		renderJson(routeService.bindArticle(articleId, routeId)?Ret.ok():Ret.fail());
	}
}

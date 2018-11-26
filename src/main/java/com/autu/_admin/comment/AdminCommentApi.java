package com.autu._admin.comment;

import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Comment;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

public class AdminCommentApi  extends BaseController{

	@Inject
	private AdminCommentService service;
	
	
	public void page() {
		QueryComment query=getBean(QueryComment.class,"",true);
		Integer pageNum = getParaToInt("page");
		Integer limit = getParaToInt("limit");
		Page<Comment> page=service.page(pageNum, limit, query);
 		renderJson(Ret.ok("page", page));
	}
	
	 
	public void reply() {
		renderJson(service.reply(getParaToInt("toId"), getPara("content"), getAgentUser()));
	}
 
	public void delete() {
		renderJson(service.delete(getParaToInt("toId")));
	}
	 
	public void aduit() {
		renderJson(service.aduit(getParaToInt("toId"),getParaToBoolean("aduit")));
	}
	public void listRecent() {
		renderJson(Ret.ok("list", service.listRecent(getParaToInt(0,8))));
	}
	
}

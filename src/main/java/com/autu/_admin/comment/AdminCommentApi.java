package com.autu._admin.comment;

import com.autu.common.annotation.SysLogInfo;
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
		Ret ret = Ret.ok().set("code", 0).set("data", page.getList()).set("count", page.getTotalRow());
		renderJson(ret);
	}
	
	@SysLogInfo(value="回复评论",action="save")
	public void reply() {
		renderJson(service.reply(getParaToInt("toId"), getPara("content"), getAgentUser()));
	}
	@SysLogInfo(value="删除评论",action="delete")
	public void delete() {
		renderJson(service.delete(getParaToInt("toId")));
	}
	@SysLogInfo(value="审核评论",action="update")
	public void aduit() {
		renderJson(service.aduit(getParaToInt("toId"),getParaToBoolean("aduit")));
	}
	public void listRecent() {
		renderJson(Ret.ok("list", service.listRecent(getParaToInt(0,8))));
	}
	
}

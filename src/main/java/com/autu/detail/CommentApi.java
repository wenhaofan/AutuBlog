package com.autu.detail;


import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.AgentUser;
import com.autu.common.model.entity.Comment;
import com.autu.common.safe.JsoupFilter;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class CommentApi extends BaseController {

	@Inject
	private CommentService service;
	
	@Inject
	private ArticleService articleService;
	
	public void save() {
		Comment comment=getModel(Comment.class,"",true);
		String name=JsoupFilter.getText(comment.getName());
		String content=JsoupFilter.getText(comment.getContent());
		
		comment.setName(name);
		comment.setContent(content);
		
		AgentUser agentUser=getAgentUser();
		service.save(comment,agentUser!=null?agentUser.getCookie():"empty");
		renderJson(Ret.ok());
	}
}

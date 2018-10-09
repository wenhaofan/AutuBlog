package com.autu.comment;


import com.autu.article.ArticleService;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.AgentUser;
import com.autu.common.model.entity.Comment;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class CommentApi extends BaseController {

	@Inject
	private CommentService service;
	
	@Inject
	private ArticleService articleService;
	
	public void save() {
		Comment comment=getModel(Comment.class,"",true);
		
		AgentUser agentUser=getAgentUser();
		service.save(comment,agentUser.getCookie());
		renderJson(Ret.ok());
	}
}

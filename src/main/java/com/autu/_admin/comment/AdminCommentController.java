package com.autu._admin.comment;


import com.autu._admin.article.AdminArticleService;
import com.autu.common.aop.Inject;
import com.autu.common.controller.BaseController;

public class AdminCommentController extends BaseController {

	@Inject
	private AdminArticleService articleService;
	
	public void index() {
		setAttr("articles", articleService.listSimpleArtilce());
		render("comments.html");
	}
	
}

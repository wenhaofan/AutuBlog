package com.autu.common.interceptor;

import com.autu.article.ArticleService;
import com.autu.blogroll.BlogrollService;
import com.autu.comment.CommentService;
import com.autu.common._config.BlogContext;
import com.autu.common.controller.BaseController;
import com.autu.meta.MetaService;
import com.autu.meta.MetaTypeEnum;
import com.autu.nav.NavService;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class FrontInterceptor implements Interceptor{

	@Inject
	private BlogrollService blogrollService;
	@Inject
	private ArticleService articleService;
	@Inject
	private MetaService metaService;
	@Inject
	private NavService navService;
	@Inject
	private CommentService commentService;
	
	@SuppressWarnings("static-access")
	@Override
	public void intercept(Invocation inv) {
		BaseController c=(BaseController) inv.getController();
		if(c.isAjax()) {
			inv.invoke();
			return ;
		}
		c.setAttr("agentUser", c.getAgentUser());
		c.setAttr("hotArticles",articleService.listHot(4));
		c.setAttr("tags",metaService.listMeta(MetaTypeEnum.TAG.toString()));
		c.setAttr("keyword", c.getPara("keyword"));
		c.setAttr("adminUser", c.userService.getAdminUser());
		c.setAttr("config", BlogContext.config);
		c.setAttr("navs", navService.list());
		c.setAttr("recentArticles",articleService.listRecent());
		c.setAttr("recentComments", commentService.listRecent());
		c.setAttr("blogrolls", blogrollService.list());
		inv.invoke();
	}

 
}

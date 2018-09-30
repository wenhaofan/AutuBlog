package com.autu.common.interceptor;

import com.autu.article.ArticleService;
import com.autu.blogroll.BlogrollService;
import com.autu.comment.CommentService;
import com.autu.common._config.BlogContext;
import com.autu.common.aop.AopFactory;
import com.autu.common.controller.BaseController;
import com.autu.meta.MetaService;
import com.autu.meta.MetaTypeEnum;
import com.autu.nav.NavService;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class FrontInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		BaseController c=(BaseController) inv.getController();
		if(c.isAjax()) {
			inv.invoke();
			return ;
		}
		c.setAttr("agentUser", c.getAgentUser());
		c.setAttr("hotArticles",getArticleService().listHot(4));
		c.setAttr("tags",getMetaService().listMeta(MetaTypeEnum.TAG.toString()));
		c.setAttr("keyword", c.getPara("keyword"));
		c.setAttr("adminUser", c.userService.getAdminUser());
		c.setAttr("config", BlogContext.config);
		c.setAttr("navs", getNavService().list());
		c.setAttr("recentArticles", getArticleService().listRecent());
		c.setAttr("recentComments", getCommentService().listRecent());
		c.setAttr("blogrolls", getBlogrollService().list());
		inv.invoke();
	}

	public BlogrollService getBlogrollService() {
		return AopFactory.getInject(BlogrollService.class);
	}
	
	
	public ArticleService getArticleService() {
		return AopFactory.getInject(ArticleService.class);
	}
	
	public MetaService getMetaService() {
		return AopFactory.getInject(MetaService.class);
	}
	
	public NavService getNavService() {
		return AopFactory.getInject(NavService.class);
	}
	
	public CommentService getCommentService() {
		return AopFactory.getInject(CommentService.class);
	}
}

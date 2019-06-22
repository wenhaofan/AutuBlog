package com.autu.common.interceptor;

import com.autu.common._config.BlogContext;
import com.autu.common.agentUser.AgentUserService;
import com.autu.common.controller.BaseController;
import com.autu.common.dto.Theme;
import com.autu.common.meta.MetaService;
import com.autu.common.meta.MetaTypeEnum;
import com.autu.common.nav.NavService;
import com.autu.detail.CommentService;
import com.autu.detail.DetailService;
import com.autu.user.UserService;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class FrontInterceptor implements Interceptor{
 
	@Inject
	private DetailService articleService;
	@Inject
	private MetaService metaService;
	@Inject
	private NavService navService;
	@Inject
	private CommentService commentService;
	@Inject
	private AgentUserService agentUserService;
	@Inject
	private UserService userService;
 
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
		c.setAttr("adminUser", userService.getAdminUser());
		c.setAttr("config", BlogContext.config);
		c.setAttr("navs", navService.list());
		c.setAttr("recentArticles",articleService.listRecent());
		c.setAttr("recentComments", commentService.listRecent());
	 
		c.setAttr("constant", new Object());
		c.setAttr("theme", new Theme());
		inv.invoke();
	}

 
}

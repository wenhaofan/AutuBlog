package com.autu.common._config;

import com.autu.article.ArticleApi;
import com.autu.article.ArticleController;
import com.autu.comment.CommentApi;
import com.autu.common.interceptor.ExceptionInterceptor;
import com.autu.common.interceptor.FrontInterceptor;
import com.autu.common.interceptor.ThemesInterceptor;
import com.autu.index.IndexController;
import com.autu.meta.MetaApi;
import com.autu.user.LoginApi;
import com.autu.user.LoginController;
import com.jfinal.config.Routes;

/**
 * 前端路由配置
 * @author fwh
 *
 */
public class FrontRoutes extends Routes{

	@Override
	public void config() {
		addInterceptor(new ThemesInterceptor());
		addInterceptor(new ExceptionInterceptor());
		addInterceptor(new FrontInterceptor());
		add("/api/login", LoginApi.class,"/");
		add("/login",LoginController.class,"/");
		add("/article",ArticleController.class,"/");
		add("/api/meta", MetaApi.class,"/");
		add("/api/article",ArticleApi.class,"/");
		add("/comment", CommentApi.class);
		add("/",IndexController.class,"/");
	}

}

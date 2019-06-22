package com.autu.common._config;

import com.autu.common.interceptor.ExceptionInterceptor;
import com.autu.common.interceptor.FrontInterceptor;
import com.autu.common.interceptor.ThemesInterceptor;
import com.autu.common.meta.MetaApi;
import com.autu.common.uplod.UploadApi;
import com.autu.detail.DetailApi;
import com.autu.detail.CommentApi;
import com.autu.detail.DetailController;
import com.autu.diy.DiyPageController;
import com.autu.index.IndexController;
import com.autu.search.SearchController;
import com.autu.timeline.TimelineController;
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
	 
		add("/api/meta", MetaApi.class,"/");
		add("/api/article",DetailApi.class,"/");
		add("/comment", CommentApi.class);
		add("/a", DetailController.class);
		add("/search", SearchController.class,"/");
		add("/",IndexController.class,"/");
		add("/api/upload", UploadApi.class,"/");
		add("/diy", DiyPageController.class,"");
		add("/timeline", TimelineController.class,"/");
	
	}

}

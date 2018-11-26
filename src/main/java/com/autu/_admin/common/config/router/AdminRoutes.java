package com.autu._admin.common.config.router;

import com.autu._admin.article.ArticleAdminApi;
import com.autu._admin.article.ArticleAdminController;
import com.autu._admin.blogroll.AdminBlogrollApi;
import com.autu._admin.comment.AdminCommentApi;
import com.autu._admin.comment.AdminCommentController;
import com.autu._admin.common.config.interceptor.AdminIndexInterceptor;
import com.autu._admin.config.AdminAdvancedConfigController;
import com.autu._admin.config.AdminBaiduConfigApi;
import com.autu._admin.config.AdminConfigApi;
import com.autu._admin.config.AdminConfigController;
import com.autu._admin.disk.DiskApi;
import com.autu._admin.disk.DiskController;
import com.autu._admin.index.IndexAdminController;
import com.autu._admin.loginRecord.LoginRecordApi;
import com.autu._admin.loginRecord.LoginRecordController;
import com.autu._admin.meta.AdminMetaApi;
import com.autu._admin.meta.CategoryRouter;
import com.autu._admin.metaweblog.AdminMetaWeblogConfigApi;
import com.autu._admin.nav.AdminNavApi;
import com.autu._admin.nav.AdminNavController;
import com.autu._admin.statistic.StatisticApi;
import com.autu._admin.statistic.StatisticController;
import com.autu._admin.themes.AdminThemesApi;
import com.autu._admin.themes.AdminThemesController;
import com.autu._admin.user.AdminUserApi;
import com.autu._admin.user.AdminUserController;
import com.autu.common.interceptor.ExceptionInterceptor;
import com.autu.common.uplod.FileUploadApi;
import com.jfinal.config.Routes;

/**
 * 后端路由配置
 * @author fwh
 *
 */
public class AdminRoutes extends Routes {
	
	@Override
	public void config() { 
	   addInterceptor(new AdminIndexInterceptor());
	   addInterceptor(new ExceptionInterceptor());
	   setBaseViewPath("/_view/admin/autumn/");
	   
	   add("/admin/api/themes",AdminThemesApi.class);
	   add("/admin/api/user",AdminUserApi.class);
	   add("/admin/api/nav", AdminNavApi.class);
	   add("/admin/api/upload",FileUploadApi.class);
	   add("/admin/api/meta",AdminMetaApi.class);
	   add("/admin/api/article",ArticleAdminApi.class);
	   add("/admin/api/loginRecord", LoginRecordApi.class);
	   add("/admin/api/statistic", StatisticApi.class);
	   add("/admin/api/disk",DiskApi.class);
	   add("/admin/api/comment",AdminCommentApi.class);
	   add("/admin/api/metaConfig",AdminMetaWeblogConfigApi.class);
	   add("/admin/api/baiduConfig",AdminBaiduConfigApi.class);
	   add("/admin/api/config", AdminConfigApi.class);
	   add("/admin/api/blogroll", AdminBlogrollApi.class);
	 
	   
	   add("/admin/advancedConfig",AdminAdvancedConfigController.class,"/");
	   add("/admin/config", AdminConfigController.class,"/");
	   add("/admin/meta", CategoryRouter.class,"/");
	   add("/admin/article", ArticleAdminController.class,"/");
	   add("/admin",IndexAdminController.class,"/");
 

	   add("/admin/statistic",StatisticController.class,"/");
	   add("/admin/disk", DiskController.class, "/");
	   add("/admin/comment", AdminCommentController.class, "/");
	   add("/admin/nav", AdminNavController.class, "/");
	   add("/admin/user",AdminUserController.class,"/");
	   add("/admin/themes",AdminThemesController.class,"/");
	   add("/admin/loginRecord", LoginRecordController.class,"/");
	}

}

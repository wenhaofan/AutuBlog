package com.autu._admin.common.config.router;

import com.autu._admin.article.ArticleAdminController;
import com.autu._admin.comment.AdminCommentController;
import com.autu._admin.common.config.interceptor.AdminIndexInterceptor;
import com.autu._admin.config.AdminAdvancedConfigController;
import com.autu._admin.config.AdminConfigController;
import com.autu._admin.disk.DiskController;
import com.autu._admin.index.IndexAdminController;
import com.autu._admin.loginRecord.LoginRecordController;
import com.autu._admin.meta.CategoryRouter;
import com.autu._admin.nav.AdminNavController;
import com.autu._admin.page.AdminPageController;
import com.autu._admin.statistic.StatisticController;
import com.autu._admin.theme.AdminThemeController;
import com.autu._admin.user.AdminUserController;
import com.autu.common.interceptor.ExceptionInterceptor;
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
	   add("/admin/themes",AdminThemeController.class,"/");
	   add("/admin/loginRecord", LoginRecordController.class,"/");
	  
	   add("/admin/page", AdminPageController.class,"/");
	}

}

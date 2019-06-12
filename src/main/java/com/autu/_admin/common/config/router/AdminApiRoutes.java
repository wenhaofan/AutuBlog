package com.autu._admin.common.config.router;

import com.autu._admin.article.ArticleAdminApi;
import com.autu._admin.blogroll.AdminBlogrollApi;
import com.autu._admin.comment.AdminCommentApi;
import com.autu._admin.common.config.interceptor.AdminIndexInterceptor;
import com.autu._admin.common.upload.AdminEditormdUploadApi;
import com.autu._admin.config.AdminBaiduConfigApi;
import com.autu._admin.config.AdminConfigApi;
import com.autu._admin.disk.DiskApi;
import com.autu._admin.loginRecord.LoginRecordApi;
import com.autu._admin.meta.AdminMetaApi;
import com.autu._admin.metaweblog.AdminMetaWeblogConfigApi;
import com.autu._admin.nav.AdminNavApi;
import com.autu._admin.route.AdminRouteApi;
import com.autu._admin.statistic.StatisticApi;
import com.autu._admin.theme.AdminThemeApi;
import com.autu._admin.user.AdminUserApi;
import com.autu.common.interceptor.ExceptionInterceptor;
import com.autu.common.uplod.UploadApi;
import com.jfinal.config.Routes;

public class AdminApiRoutes extends Routes{

	@Override
	public void config() {
 
	   addInterceptor(new AdminIndexInterceptor());
	   addInterceptor(new ExceptionInterceptor());
	 
	   add("/admin/api/themes",AdminThemeApi.class);
	   add("/admin/api/user",AdminUserApi.class);
	   add("/admin/api/nav", AdminNavApi.class);
	   add("/admin/api/upload",UploadApi.class);
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
	   add("/admin/api/route", AdminRouteApi.class);
	   add("/admin/api/upload/editormd", AdminEditormdUploadApi.class);
	}

}

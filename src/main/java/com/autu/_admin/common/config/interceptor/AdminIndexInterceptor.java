package com.autu._admin.common.config.interceptor;

import com.autu._admin.user.AdminUserService;
import com.autu.common.kit.StrKit;
import com.autu.common.model.User;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class AdminIndexInterceptor implements Interceptor {

	@Inject
	private AdminUserService adminUserService;

	@Override
	public void intercept(Invocation inv) {

		inv.invoke();

		Controller c = inv.getController();

		boolean isPjax = "true".equalsIgnoreCase(c.getHeader("X-PJAX"));

		if (isPjax) {
			return;
		}

		User adminUser = adminUserService.getAdminUser();

		c.setAttr("adminUser", adminUser);

		String viewPath = ((Controller) inv.getTarget()).getViewPath();

		if (c.getRender() != null) {
			viewPath += c.getRender().getView();
		}

		if (StrKit.notBlank(viewPath) && viewPath.endsWith(".html")) {
			c.setAttr("includeUrl", viewPath);
			c.render("/_view/admin/autumn/index.html");
		}

	}

}

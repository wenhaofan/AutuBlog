package com.autu._admin.theme;

import com.autu._admin.config.AdminConfigService;
import com.autu.common._config.BlogContext;
import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月13日 下午1:15:02
*/
public class AdminThemeApi extends BaseController {

	@Inject
	public AdminConfigService configService;
	
 
	public void change() {
		String themeName=getPara();
		configService.saveOrUpdate(BlogContext.config.setTheme(themeName));
		renderJson(Ret.ok());
	}
	
}

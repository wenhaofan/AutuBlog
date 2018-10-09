package com.autu._admin.themes;

import com.autu.common._config.BlogContext;
import com.autu.common.annotation.SysLogInfo;
import com.autu.common.controller.BaseController;
import com.autu.common.kit.PropertyKit;
import com.jfinal.kit.Ret;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月13日 下午1:15:02
*/
public class AdminThemesApi extends BaseController {

	@SysLogInfo(value="切换主题",action="udpate")
	public void change() {
		String themeName=getPara();
		BlogContext.setTheme(themeName);
		PropertyKit.updateValue(BlogContext.CONFIG_FILE_NAME, "theme", themeName);
		renderJson(Ret.ok());
	}
	
}

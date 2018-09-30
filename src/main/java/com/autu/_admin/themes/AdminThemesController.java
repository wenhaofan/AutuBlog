package com.autu._admin.themes;

import com.autu.common._config.BlogContext;
import com.autu.common.controller.BaseController;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月13日 下午1:14:54
*/
public class AdminThemesController extends BaseController  {

	public void index() {
		setAttr("currentTheme", BlogContext.getTheme());
		render("themes.html");
	}
	
}

package com.autu._admin.meta;

import com.autu.common.controller.BaseController;

public class CategoryRouter extends BaseController{
	/**
	 * 查询所有的分类信息
	 */
	public void list() {
		render("meta.html");
	}
}

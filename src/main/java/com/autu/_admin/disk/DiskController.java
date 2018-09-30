package com.autu._admin.disk;

import com.autu.common.controller.BaseController;

public class DiskController extends BaseController {

	public void index() {
		setAttr("folderId", getParaToInt("p",0));
		render("disk.html");
	}
	
}

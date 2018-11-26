package com.autu._admin.loginRecord;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年11月26日 上午11:04:47
*/
public class LoginRecordApi extends BaseController {

	@Inject
	private LoginRecordService loginRecordService;
	
	public void page() {
		Integer pageNum = getParaToInt("page");
		Integer limit = getParaToInt("limit");
		renderJson(Ret.ok("page", loginRecordService.page(pageNum, limit)));
	}
	
	public void listRecent() {
		renderJson(Ret.ok("list", loginRecordService.listRecent(getParaToInt(0,8))));
	}
}

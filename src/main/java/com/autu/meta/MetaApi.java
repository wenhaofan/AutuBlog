package com.autu.meta;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月6日 下午12:38:51
*/
public class MetaApi extends BaseController{
	@Inject
	private static MetaService mservice;
	
	public void list() {
		String type=getPara(0);
		renderJson(Ret.ok("metas",mservice.listMeta(type)));
	}

	public void article() {
		renderJson(Ret.ok("metas",mservice.listByCId(getParaToInt(1),getPara(0))));
	}
}

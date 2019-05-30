package com.autu._admin.metaweblog;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.model.MetaweblogConfig;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminMetaWeblogConfigApi extends BaseController {
	
	@Inject
	private MetaweblogHelper metaweblogService;
	
	
	public void mget() {
		renderJson(Ret.ok("config", metaweblogService.get(getParaToInt())));
	}
	/**
	 * 获取所有记录
	 */
	public void mList() {
		List<MetaweblogConfig> configs=metaweblogService.list();
		renderJson(Ret.ok("list", configs));
	}
 
	public void mconfigEdit() {
		MetaweblogConfig config=getModel(MetaweblogConfig.class,"",true);
		renderJson(metaweblogService.saveOrUpdate(config));
	}
 
	public void mconfigDelete() {
		renderJson(metaweblogService.delete(getParaToInt()));
	}
}

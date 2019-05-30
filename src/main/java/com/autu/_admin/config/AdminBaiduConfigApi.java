package com.autu._admin.config;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.model.BaiduSeoConfig;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminBaiduConfigApi extends BaseController {
	@Inject
	private AdminBaiduSeoService baiduSeoService;
	/**
	 * 根据id获取
	 */
	public void bget() {
		renderJson(Ret.ok("config", baiduSeoService.get(getParaToInt())));
	}
	
	public void bList() {
		List<BaiduSeoConfig> configs=baiduSeoService.list();
		renderJson(Ret.ok("list",configs));
	}
	
	 
	public void bconfigEdit() {
		BaiduSeoConfig config=getModel(BaiduSeoConfig.class,"",true);
		renderJson(baiduSeoService.saveOrUpdate(config));
	}
 
 
 
	public void bconfigDelete() {
		renderJson(baiduSeoService.delete(getParaToInt()));
	}
	
 
	public void pushBaiduLinks() {
		String links=getPara("links");
		baiduSeoService.pushLink(links);
		renderJson(Ret.ok());
	}
}

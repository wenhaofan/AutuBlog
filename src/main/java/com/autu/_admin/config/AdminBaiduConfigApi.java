package com.autu._admin.config;

import java.util.List;

import com.autu.common.annotation.SysLogInfo;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.BaiduSeoConfig;
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
		renderJson(Ret.ok().set("code", 0).set("data", configs));
	}
	
	@SysLogInfo(value="编辑百度接口配置",action="update")
	public void bconfigEdit() {
		BaiduSeoConfig config=getModel(BaiduSeoConfig.class,"",true);
		renderJson(baiduSeoService.saveOrUpdate(config));
	}
 
 
	@SysLogInfo(value="删除百度接口配置",action="update")
	public void bconfigDelete() {
		renderJson(baiduSeoService.delete(getParaToInt()));
	}
	
	@SysLogInfo(value="调用百度推送接口")
	public void pushBaiduLinks() {
		String links=getPara("links");
		baiduSeoService.pushLink(links);
		renderJson(Ret.ok());
	}
}

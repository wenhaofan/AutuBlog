package com.autu.detail;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Article;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class DetailApi extends BaseController{

	@Inject
	private DetailService service;
	
	//通过标识获取文章信息
	public void index() {
		String identify=getPara();
		Article article=null;
		if(getLoginUser()!=null) {
			article=service.getArticle(identify);
		}else {
			article=service.getPublishArticle(identify);
		}
	 
		renderJson(Ret.ok("article", article).toJson());
	}
 
 
	/**
	 * 增加阅读数量 
	 */
	public void addReadNum(){
		service.addReadNum(getPara());;
		renderJson(Ret.ok());
	}
	
}

package com.autu.detail;

import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Article;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class ArticleApi extends BaseController{

	@Inject
	private ArticleService service;
	
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
	 * 根据标签或分类id进行查询
	 */
	public void list() {
		Integer cid=getParaToInt(2);
		Integer pageNum = getParaToInt(0,1);
		Integer limit=getParaToInt(1, 10);
		boolean isTop=getParaToBoolean(3, false);
		renderJson(Ret.ok("articlePage", service.page(pageNum, limit, cid,isTop)));
	}
 
	/**
	 * 增加阅读数量 
	 */
	public void addReadNum(){
		service.addReadNum(getPara());;
		renderJson(Ret.ok());
	}
	
}

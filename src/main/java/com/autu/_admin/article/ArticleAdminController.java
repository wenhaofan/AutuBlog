package com.autu._admin.article;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.meta.MetaService;
import com.autu.common.meta.MetaTypeEnum;
import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Meta;
import com.jfinal.aop.Inject;

public class ArticleAdminController  extends BaseController{
	
	@Inject
	private AdminArticleService service; 

	@Inject
	private MetaService mService;
	
	/**
	 * 分页查询
	 */
	public void list() {
		render("article_list.html");
	}
	public void draft() {
		render("article_draft.html");
	}
	/**
	 * 跳转进文章编辑页面
	 */
	public void edit() {
		Integer id=getParaToInt();
		//所有的分类
		setAttr("allCategory", mService.listMeta(MetaTypeEnum.CATEGORY.toString()));
		
		if(id==null) {
			render("article_edit.html");
			return;
		}
		
		Article article=service.get(id);
		List<Meta> aTag=service.listTag(id);
		List<Meta> aCategory=service.listCategory(id);
		setAttr("article", article);
		//文章关联的分类和标签
		setAttr("aCategorys",aCategory );
	
		setAttr("aTags", aTag);
		render("article_edit.html");
	}
	

}

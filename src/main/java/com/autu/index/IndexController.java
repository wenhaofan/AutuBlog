package com.autu.index;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.meta.MetaService;
import com.autu.common.model.Article;
import com.autu.common.model.Meta;
import com.autu.detail.CommentService;
import com.autu.user.UserService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/**
 * 网站首页控制器
 * @author 作者:范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
@Before(IndexSeoInterceptor.class)
public class IndexController extends BaseController {

 
	@Inject
	private IndexService indexService;
	@Inject
	private CommentService commentService;
	@Inject
	private MetaService metaService;
	@Inject
	private UserService userService;
	
	public void index() {
		Integer limit=getParaToInt("limit", 12);
		Page<Article> articlePage=indexService.page(1, limit,null,false);
		List<Article> topArticleList=indexService.listTop();
 
		setAttr("articlePage",articlePage);
		setAttr("currentPageNum", 1);
		setAttr("articleTopList", topArticleList);
		render("index.html");
	}

	/**
	 * 分页
	 */
	public void p() {
		Integer limit=getParaToInt("limit", 12);
		Integer pageNum=getParaToInt(0);
		
		if(pageNum==null) {
			renderError(404);
			return;
		}
		
		if(pageNum==1) {
			index();
			return;
		}
		
		Page<Article> articlePage=indexService.page(pageNum, limit,null,false);
		 
 
		setAttr("articlePage",articlePage);
 
		render("index.html");
	}
 
	
	/**
	  *  分类
	 *  /category/{pageNum}-{id}-{limit}
	 */
	public void c() {
		Integer pageNum=getParaToInt(0);
		
		Integer id=getParaToInt(1);
	 
		if(pageNum==null||id==null) {
			renderError(404);
			return;
		}
		
		Integer limit=getParaToInt(2, 24);
		
		Page<Article> articlePage=indexService.page(pageNum, limit,id,null);
		
		Meta category=metaService.get(id);
		
		setAttr("category", category);
		setAttr("articlePage",articlePage);
 
		render("page-category.html");
	}
 
}

package com.autu.index;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.meta.MetaService;
import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Comment;
import com.autu.detail.ArticleService;
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
	private ArticleService articleService;
	@Inject
	private CommentService commentService;
	@Inject
	private MetaService metaService;
	@Inject
	private UserService userService;
	
	public void index() {
		Integer pageNum = getParaToInt(0,1);
		Integer limit=getParaToInt("limit", 12);
		Page<Article> articlePage=articleService.page(pageNum, limit,null,false);
		List<Article> topArticleList=articleService.listTop();
 
		setAttr("articlePage",articlePage);
		setAttr("currentPageNum", pageNum);
		setAttr("articleTopList", topArticleList);
		render("index.html");
	}

 
	public void profiles() {
		render("profiles/profiles.html");
	}

	public void about() {
		render("about.html");
	}
	
	public void search() {
		render("search.html");
	}
	
	public void links() {
		Page<Comment> commentPage=commentService.page(getParaToInt("p",1),8, "links");
		setAttr("article", new Article().setIdentify("links").setId(99999));
		setAttr("commentPage", commentPage);
		render("links.html");
	}
	

}

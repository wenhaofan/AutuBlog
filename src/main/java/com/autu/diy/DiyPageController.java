package com.autu.diy;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.dto.LastNextArticleDTO;
import com.autu.common.meta.MetaService;
import com.autu.common.model.Article;
import com.autu.common.model.Comment;
import com.autu.common.model.Meta;
import com.autu.common.model.Page;
import com.autu.detail.ArticleService;
import com.autu.detail.CommentService;
import com.jfinal.aop.Inject;

public class DiyPageController extends BaseController{

	@Inject
	private ArticleService articleService;
	
	@Inject
	private CommentService commentService;
	
	@Inject
	private MetaService metaService;
	
	public void page() {
		Page page=(Page) getRequest().getAttribute("page");
		notTheme();
		
		Article article=articleService.get(page.getArticleId());
  
		articleService.addReadNum(article.getIdentify());
		List<Meta> categorys=metaService.listByCId(article.getId(), "category");
		List<Meta> atags=metaService.listByCId(article.getId(), "tag");
		com.jfinal.plugin.activerecord.Page<Comment> commentPage=commentService.page(getParaToInt("p",1),6, article.getIdentify());
 
		LastNextArticleDTO lastNextArticle=articleService.lastNextArticle(article);
		setAttr("lastNextArticle", lastNextArticle);
 
		List<Article> aboutArticles=articleService.about(article, 10);
		 
		setAttr("aboutArticles", aboutArticles);
		setAttr("commentPage", commentPage);
		setAttr("acategorys",categorys);
		setAttr("atags",atags);
		setAttr("article", article);
	 
 
		render(page.getPath());
	}
	
}

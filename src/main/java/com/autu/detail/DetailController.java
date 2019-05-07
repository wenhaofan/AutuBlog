package com.autu.detail;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.meta.MetaService;
import com.autu.common.model.dto.LastNextArticleDTO;
import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Comment;
import com.autu.common.model.entity.Meta;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月1日-下午7:47:20 
*/
@Before(DetailSeoInterceptor.class)
public class DetailController extends BaseController {

	@Inject
	private ArticleService articleService;
	@Inject
	private MetaService metaService;
	@Inject
	private CommentService commentService;
	
	public void index() {
		String identify=getPara();
		
		Article article=null;
		
		if(getLoginUser()!=null) {
			article=articleService.getArticle(identify);
		}else {
			article=articleService.getPublishArticle(identify);
		}
		
		
		if(article==null) {
			renderError(404);
			return;
		}
	
		articleService.addReadNum(identify);
		List<Meta> categorys=metaService.listByCId(article.getId(), "category");
		List<Meta> atags=metaService.listByCId(article.getId(), "tag");
		Page<Comment> commentPage=commentService.page(getParaToInt("p",1),6, article.getIdentify());
 
		LastNextArticleDTO lastNextArticle=articleService.lastNextArticle(article);
		setAttr("lastNextArticle", lastNextArticle);
 
		List<Article> aboutArticles=articleService.about(article, 10);
		 
		setAttr("aboutArticles", aboutArticles);
		setAttr("commentPage", commentPage);
		setAttr("acategorys",categorys);
		setAttr("atags",atags);
		setAttr("article", article);
		setAttr("identify", identify);
		render("post.html");
	}
	
}

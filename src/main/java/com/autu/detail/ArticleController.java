package com.autu.detail;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.meta.MetaService;
import com.autu.common.model.dto.LastNextArticleDTO;
import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Comment;
import com.autu.common.model.entity.Meta;
import com.autu.search.ArticleLuceneIndexes;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
/**
 * 首页文章控制器
 * @author fwh
 *
 */
@Before(DetailSeoInterceptor.class)
public class ArticleController extends BaseController{

	@Inject
	private ArticleService articleService;
	@Inject
	private MetaService metaService;
	
	@Inject
	private ArticleLuceneIndexes articleLuceneIndexes;
	
	@Inject
	private CommentService commentService;
	
	public void category() {
		Integer cid=getParaToInt(1);
		if(cid==null) {
			forwardAction("/");
			return;
		}
		Integer pageNum = getParaToInt(0,1);
		Integer limit=getParaToInt(2, 12);
		Page<Article> articlePage=articleService.page(pageNum, limit,cid,null);
		
		Meta tag=metaService.get(cid);
		
		setAttr("tag", tag);
		setAttr("articlePage",articlePage);
		setAttr("currentPageNum", pageNum);
	 
		render("list.html");
	}
	
	
	
	public void index(){
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
 
		render("info.html");
	}
	
	public void search() {
		String queryStr=getPara("keyword");
		if(StrKit.isBlank(queryStr)) {
			renderError(404);
			return;
		}
		Integer pageNum=getParaToInt(0,1);
		Integer pageSize= getParaToInt(1,10);
		Page<Article>  articlePage=null;
		try {
			articlePage=articleLuceneIndexes.search(queryStr,pageNum,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
 
		setAttr("articlePage",articlePage);
		setAttr("currentPageNum", pageNum);
		
		render("list.html");
	}
}

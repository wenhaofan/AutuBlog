package com.autu._admin.article;

import java.util.List;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Article;
import com.autu.common.model.Meta;
import com.autu.common.model.User;
import com.autu.search.ArticleLuceneIndexes;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 文章后台管理的控制器
 * 
 * @author fwh
 *
 */
public class ArticleAdminApi extends BaseController {

	@Inject
	private AdminArticleService articleService;
	@Inject
	private AdminArticleLuceneIndexes luceneIndexes;
	@Inject
	private ArticleLuceneIndexes  articleLuceneIndexes;
	
	/**
	 * 根据索引查询
	 */
	public void search() {
		String queryStr=getPara("keyword");
		
		Integer pageNum=getParaToInt(0,1);
		Integer pageSize= getParaToInt(1,10);
		Page<Article>  articlePage=null;
		try {
			articlePage=articleLuceneIndexes.search(queryStr,pageNum,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
 
		renderJson(Ret.ok("page",articlePage));
	}
 
	public void createIndex() {
		luceneIndexes.resetArticleIndexes();
		renderJson(Ret.ok());
	}
	
	public void list() {
		
		/**
		 * 如果有搜索关键字，则使用Lucene进行查询
		 */
		if(StrKit.notBlank(getPara("keyword"))) {
			search();
			return ;
		}
		
		Article article = getModel(Article.class, "", true);
		Integer metaid = getParaToInt("categoryId");
		Integer pageNum = getParaToInt("page");
		Integer limit = getParaToInt("limit",10);
		
		Page<Article> articlePage = articleService.page(article, metaid, pageNum, limit);
		
		renderJson(Ret.ok("page",articlePage));
	}
	
 
	public void asyncMetaWeblog() {
		Integer id=getParaToInt();
		renderJson(articleService.asyncMetaWeblog(id));
	}
	
	/**
	 * 执行文章编辑
	 * 
	 * @throws Exception
	 */
	public void edit() {
		Article article = getModel(Article.class, "", true);
		List<Meta> tags=getModelList(Meta.class, "tag");
		List<Meta> categorys=getModelList(Meta.class, "category");
		User user=getLoginUser();
		article.setUserId(user.getId());
		articleService.saveOrUpdate(article, tags, categorys);
	
		renderJson(Ret.ok("article", article));
	}
	
	public void remove() {
		Integer id =getParaToInt(0);
		renderJson(articleService.remove(id));;
	}

	public void delete() {
		Integer id =getParaToInt(0);
		renderJson(articleService.delete(id));;
	}
	 
	public void recover() {
		Integer id = getParaToInt(0);
		renderJson(articleService.recover(id));;
	}

	/**
	 * 获取文章信息
	 */
	public void get() {
		renderJson(Ret.ok("article", articleService.get(getParaToInt(0))));
	}
 
	/**
	 * 获取浏览量最多的前几条
	 */
	public void listHot() {
		renderJson(Ret.ok("list", articleService.listHot(getParaToInt("num",8))));
	}
	
	
}

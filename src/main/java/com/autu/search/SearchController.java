package com.autu.search;

import com.autu.common.controller.BaseController;
import com.autu.common.model.Article;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月1日-下午7:47:05 
*/
public class SearchController extends BaseController {

	@Inject
	private ArticleLuceneIndexes articleLuceneIndexes;
	
	public void index() {
		
		String queryStr=getPara("keyword");
		if(StrKit.notBlank(queryStr)) {
			query();
			return;
		}
 
 
		render("search.html");
	}
	
	public void query() {
		String queryStr=getPara("keyword");
	
		Integer pageNum=getParaToInt(0,1);
		Integer pageSize= getParaToInt(1,10);
		Page<Article>  articlePage=null;
		try {
			articlePage=articleLuceneIndexes.search(queryStr,pageNum,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
 
		setAttr("articlePage",articlePage);
		render("list.html");
	}
}


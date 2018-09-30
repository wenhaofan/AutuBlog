package com.autu.article;

import java.util.List;

import com.autu._admin.article.AdminArticleLuceneIndexes;
import com.autu.common.aop.Inject;
import com.autu.common.lucene.LuceneHelper;
import com.autu.common.model.entity.Article;
import com.jfinal.plugin.activerecord.Page;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月11日 上午10:11:41
*/
public class ArticleLuceneIndexes {

	 
	private LuceneHelper luceneHelper=LuceneHelper.single();

	@Inject
	private ArticleService articleService;
	@Inject
	private AdminArticleLuceneIndexes adminArticleLuceneIndexes;
	
	/**
	 * 从引索中查询
	 * @param queryStr
	 * @param querySize
	 * @return
	 */
	public Page<Article> search(String queryStr,Integer pageNum,Integer pageSize){
		//adminArticleLuceneIndexes.resetArticleIndexes();
		Page<Article> articlePage=luceneHelper.readerIndex(queryStr, pageNum, pageSize);
		List<Article> articles=articlePage.getList();
		
		Article articleInfo=null;
		Article item=null;
		for(int i=0,size=articles.size();i<size;i++) {
			item=articles.get(i);
			articleInfo=articleService.getArtcileInfo(item.getId());
 
			item.setPv(articleInfo.getPv());
			item.setGmtCreate(articleInfo.getGmtCreate());
		    item.setThumbImg(articleInfo.getThumbImg());
		    item.setIdentify(articleInfo.getIdentify());
 
		}
		return articlePage;
	}
	
}

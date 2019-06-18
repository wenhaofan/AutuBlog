package com.autu.detail;

import java.util.ArrayList;
import java.util.List;

import com.autu.common.dto.LastNextArticleDTO;
import com.autu.common.meta.MetaService;
import com.autu.common.meta.MetaTypeEnum;
import com.autu.common.model.Article;
import com.autu.common.model.Meta;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;


public class ArticleService {
 
	@Inject
	private Article dao;
	 
	@Inject
	private MetaService metaService;
	/**
	 * 分页查询文章
	 * @param pageNumber
	 * @param pageSize
	 * @param metas
	 * @return
	 */
	public Page<Article> page(Integer pageNumber,Integer pageSize,Integer metaId,Boolean isTop){
		SqlPara sqlPara=dao.getSqlPara("article.page", Kv.by("metaId",metaId).set("isTop", isTop));
		Page<Article> page= dao.paginate(pageNumber, pageSize,sqlPara);
 
		for(Article article:page.getList()) {
			List<Meta> metas=metaService.listByCId(article.getId(), MetaTypeEnum.CATEGORY.toString());
			article.setMetas(metas);
		}
		return page;
	}
	
	/**
	 * 根据标识去获取
	 * @param identify
	 * @return
	 */
	public Article getArticle(String  identify) {
		return dao.findFirst("select * from article where identify =?  ",identify);
	}
	
	public Article getPublishArticle(String  identify) {
		return dao.findFirst("select * from article where identify =? and state=1  ",identify);
	}
	
	/**
	 * 获取指定文章
	 * @return
	 */
	public List<Article> listTop(){
		SqlPara sql=dao.getSqlPara("article.listTop");
		List<Article> artiles= dao.find(sql);
		for(Article article:artiles) {
			List<Meta> metas=metaService.listByCId(article.getId(), MetaTypeEnum.CATEGORY.toString());
			article.setMetas(metas);
		}
		return artiles;
	}
	
	/**
	 * 获取一篇文章的上一篇和下一篇
	 * @param article
	 * @return
	 */
	public LastNextArticleDTO lastNextArticle(Article queryArticle){
		SqlPara sqlPara=dao.getSqlPara("article.lastNextArticle",queryArticle.getId());
 		List<Article> articles=dao.find(sqlPara);
 		
 		LastNextArticleDTO result=	new LastNextArticleDTO();
 		
 		if(articles==null||articles.isEmpty()) {
 			List<Integer> notInIds=new ArrayList<>();
 			notInIds.add(queryArticle.getId());
 			articles=listRandomArticle(2,notInIds);
 		}else if(articles.size()==1) {
 			List<Integer> notInIds=new ArrayList<>();
 			notInIds.add(queryArticle.getId());
 			notInIds.add(articles.get(0).getId());
 			List<Article> randomArticles=listRandomArticle(1,notInIds);
 			if(!randomArticles.isEmpty()) {
 				articles.add(randomArticles.get(0));
 			}
 		}
 	
 		if(articles.size()==2) {
 			result.setLastArticle(articles.get(0)).setNextArticle(articles.get(1));
 		}else if(articles.size()==1) {
 			result.setLastArticle(articles.get(0));
 		}
 		
		return result;
	}
	
	public List<Article> listRandomArticle(Integer limit,List<Integer> notInIds){
		SqlPara sqlPara=dao.getSqlPara("article.randomArticle",
				Kv.by("limit", limit).set("notInIds", notInIds));
		return dao.find(sqlPara);
	}
	
	public List<Article> about(Article article,Integer size){
		SqlPara sqlPara=dao.getSqlPara("article.about", Kv.by("article", article).set("size", size));
		return dao.find(sqlPara);
	}
	
	public void addReadNum(String identify){
		Db.update("update article set pv=pv+1 where identify='"+identify+"'");
	}

	public List<Article> listRecent(){
		return dao.find("select title,identify,thumbImg,intro,content from article where state=1 order by gmtCreate desc limit 6");
	}
	
	public List<Article> listHot(Integer num){
		return dao.find("select title,identify,thumbImg,intro,content from article where state=1 order by pv desc limit  "+num);
	}
	
	/**
	 * 获取文章不包括content的信息
	 * @param id
	 * @return
	 */
	public Article getArtcileInfo(Integer id) {
		return dao.findFirst("select identify,thumbImg,pv,gmtCreate,isTop   from article where state=1 and id=? ",id);
	}
	
	public Article get(Integer id) {
		
		return dao.findById(id);
	}
	
}

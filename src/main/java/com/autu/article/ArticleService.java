package com.autu.article;

import java.util.ArrayList;
import java.util.List;

import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Meta;
import com.autu.meta.MetaService;
import com.autu.meta.MetaTypeEnum;
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
		Article article= dao.findFirst("select * from article where identify =? ",identify);
		return article;
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
	public List<Article> lastNextArticle(Article article){
 		List<Article> articles=new ArrayList<>();
 		articles.add(0, dao.findById(article.getId()-1));
 		articles.add(1, dao.findById(article.getId()+1));
		return articles;
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
}

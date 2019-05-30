package com.autu._admin.article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.autu._admin.config.AdminBaiduSeoService;
import com.autu._admin.meta.AdminMetaService;
import com.autu._admin.metaweblog.MetaweblogHelper;
import com.autu.common.exception.MsgException;
import com.autu.common.kit.ListKit;
import com.autu.common.kit.StrKit;
import com.autu.common.meta.MetaTypeEnum;
import com.autu.common.model.Article;
import com.autu.common.model.Meta;
import com.autu.common.safe.JsoupFilter;
import com.autu.detail.ArticleService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

/**
 * 文章service实现类
 * 
 * @author fwh
 *
 */
public class AdminArticleService {
	@Inject
	private Article  dao;
	@Inject
	private AdminMetaService mservice;
	@Inject
	private  ArticleService articleService;
	@Inject
	private AdminBaiduSeoService baiduSeoService;
	@Inject
	private MetaweblogHelper metaweblogService;
	@Inject
	private AdminArticleLuceneIndexes luceneIndexes;

	/**
	 * 获取文章的 title,id,identify
	 * @return
	 */
	public List<Article> listSimpleArtilce(){
		return dao.find("select title,id,identify from article");
	}
	
	/**
	 * 将指定文章推送至其他网站
	 * @param id
	 * @return
	 */
	public Ret asyncMetaWeblog(Integer id) {
		List<Meta> tags=listTag(id);
		Article article=dao.findById(id);
		new Thread(()-> {
			//向其他论坛推送
			metaweblogService.pushPostMetaweblog(article, tags);
		
		}).start();
		return Ret.ok();
	}
	
	public List<Meta> listCategory(Integer id){
		return mservice.listByCId(id,MetaTypeEnum.CATEGORY.toString());
	}
	
	public List<Meta> listTag(Integer id){
		return mservice.listByCId(id,MetaTypeEnum.TAG.toString());
	}
	
	/**
	 * 根据id是否为空判断执行update还是add
	 * @param article
	 * @param tags
	 * @param categorys
	 */
	public void saveOrUpdate(Article article,List<Meta> tags,List<Meta> categorys) {
		//如果简介为空则从content中取出100纯文字作为简介
		if(article.getIntro()==null) {
			JsoupFilter.filterArticle(article, 100);
		}
		if(article.getIsTop()==null) {
			article.setIsTop(false);
		}
		
		if(article.getId()==null) {
			save(article, tags, categorys);
			luceneIndexes.addIndex(article);
		}else {
			update(article, tags, categorys);
			luceneIndexes.update(article);
		}
		
		if(article.getState().equals(1)) {
			new Thread(()-> {
				//向百度推送该文章
				baiduSeoService.pushLink(article.getUrl());	
			}).start();;
		}
		
	}
	
	
	/**
	 * 添加一篇文章
	 * @param article
	 * @param categoryIds
	 */
	public void save(Article article,List<Meta> tags,List<Meta> categorys)  {
	  
		String identify=article.getIdentify();
		
	
		if(StrKit.notBlank(identify)) {
			Article tempArticle=articleService.getArticle(identify);
			if(tempArticle!=null&&!identify.equals(tempArticle.getIdentify())) {
				throw 	new MsgException("路径已存在！");
			}
		}else {
			//默认路径为创建时间
			article.setIdentify(genIdentify(new Date()));
		}
		
		article.setGmtCreate(new Date());
		article.save();
		
		int articleId=article.getId();
		//创建分类 标签的关联
		if(ListKit.notBlank(categorys)) {
			mservice.saveMetas(categorys, articleId);
		}
		if(ListKit.notBlank(tags)) {
			mservice.saveMetas(tags, articleId);
		}

	}

	private String genIdentify(Date date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date); 
	}
	/**
	 * 更新文章
	 * @param article
	 * @param tags
	 * @param categorys
	 */
	public void update(Article article,List<Meta> tags,List<Meta> categorys) {
		
		String identify=article.getIdentify();
		
		if(StrKit.notBlank(identify)) {
			Article tempArticle=articleService.getArticle(identify);
			if(tempArticle!=null&&!tempArticle.getId().equals(article.getId())) {
				throw new MsgException("路径已存在！");
			}
		}else {
			//默认路径为创建时间
			Article tempArticle=article.findById(article.getId());
			article.setIdentify(genIdentify(tempArticle.getGmtCreate()));
		}
		 
		article.update();
		
		int articleId=article.getId();
		
		//删除文章的关联
		mservice.deleteRelevanceByCid(articleId);
		
		//创建标签分类的关联
		if(ListKit.notBlank(categorys)) {
			mservice.saveMetas(categorys, articleId);
		}
		if(ListKit.notBlank(tags)) {
			mservice.saveMetas(tags, articleId);
		}
		
	}

	/**
	 * 真删除
	 * @param article
	 */
	public Ret delete(Integer id) {
		Article article=dao.findById(id);
		if(article==null) {
			return Ret.ok();
		}
		//删除文章的关联
		mservice.deleteRelevanceByCid(article.getId());
		luceneIndexes.delete(id);
		return article.delete()?Ret.ok():Ret.fail();
	}

	/**
	 * 假删除
	 * @param id
	 * @return
	 */
	public Ret remove(Integer id) {
		Article article=new Article();
		article.setId(id);
		article.setState(Article.STATE_DISCARD);
		luceneIndexes.delete(id);
		return article.update()?Ret.ok():Ret.fail();
	}
	/**
	 * 从删除状态修改为发布状态
	 * @param id
	 * @return
	 */
	public Ret recover(Integer id) {
		Article article=new Article();
		article.setId(id);
		article.setState(Article.STATE_PUBLISH);
		
		Article temp=dao.findById(id);
		if(temp!=null) {
			luceneIndexes.addIndex(temp);
		}
		return article.update()?Ret.ok():Ret.fail();
	}
		
	public Article get(int articleId) {
		Article article= dao.findById(articleId);
		return article;
	}

	/**
	 * 分页查询
	 * @param article
	 * @param metaId
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Article> page(Article article, Integer metaId, int pageNumber, int pageSize) {

		Kv kv=Kv.by("mid", metaId).set("article", article);
		
		SqlPara sql=dao.getSqlPara("adminArticle.page", kv);
		
		Page<Article> articlePage = dao.paginate(pageNumber, pageSize,sql);
		
		return articlePage;
	}
	
	public List<Article> listAll(Integer state){
		SqlPara sql=dao.getSqlPara("adminArticle.listAll",Kv.by("article", new Article().setState(state)));
		return dao.find(sql);
	}
	
	public List<Article> listHot(Integer num){
		return dao.find("select title,identify,thumbImg,intro,content from article where state=1 order by pv desc limit  "+num);
	}
}

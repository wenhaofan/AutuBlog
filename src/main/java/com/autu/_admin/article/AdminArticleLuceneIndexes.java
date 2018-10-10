package com.autu._admin.article;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;

import com.autu.common.aop.Inject;
import com.autu.common.kit.EmailKit;
import com.autu.common.log.SysLogActionEnum;
import com.autu.common.log.SysLogHelper;
import com.autu.common.lucene.LuceneHelper;
import com.autu.common.model.entity.Article;
import com.autu.common.safe.JsoupFilter;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;

public class AdminArticleLuceneIndexes {
	
	private static final Log log = Log.getLog(EmailKit.class);
 
	
	@Inject
	private AdminArticleService adminArticleService;
	
	public void resetArticleIndexes() {
		deleteAll();
		addAll();
	}
	
	public void deleteAll() {
		try {
			LuceneHelper.single().deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			SysLogHelper.addErrorLog("删除文章索引失败！", SysLogActionEnum.DELETE.getName(), e.getMessage());
		}
		
	}
	
	public void addAll() {
		try {
			List<Article> articleList=adminArticleService.listAll(1);
			addIndexs(articleList);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			SysLogHelper.addErrorLog("文章索引初始化失败！", SysLogActionEnum.SAVE.getName(), e.getMessage());
		}
		
	}
	
	/**
	 * 添加多篇文章的索引
	 * @param articles
	 */
	public void addIndexs(List<Article> articles) {
		List<Document> documents=new ArrayList<>();
		for(Article article:articles) {
			documents.add(getDocument(article));
		}
		
		LuceneHelper.single().createIndexs(documents);
	}
	
	/**
	 * 添加索引
	 * @param article
	 */
	public void addIndex(Article article) {
		//过滤html标签
		Document document = getDocument(article);
		 
		long count=LuceneHelper.single().createIndex(document);
	 
		if(count==0) {
			log.error("引索创建失败！id="+article.getId());
			SysLogHelper.addErrorLog("文章索引创建失败", SysLogActionEnum.SAVE.getName(), Kv.by("article", article).toJson());
		}
	}

	/**
	 * 将article转换为document
	 * @param article
	 * @return
	 */
	private Document getDocument(Article article) {
		
		String content=JsoupFilter.filterArticleContent(article.getContent());
		article.setContent(content);
		
		Document document = new Document();
	    FieldType type = new FieldType();
	    type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
	    type.setTokenized(false);
	    type.setStored(true);

	    Field idField=new Field("id", String.valueOf(article.getId()), type);
		Field  contentField = new TextField("content", article.getContent(), Store.YES);
		Field  titleField = new TextField("title", article.getTitle(), Store.YES);
		document.add(idField);
		document.add(contentField);
		document.add(titleField);
		return document;
	}
	
	public void delete(Integer id) {
		long count=LuceneHelper.single().deleteIndex("id", id.toString());
		if(count==0) {
			log.error("文章引索删除失败！id="+id);
			SysLogHelper.addErrorLog("文章索引删除失败", SysLogActionEnum.DELETE.getName(), Kv.by("articleId", id).toJson());
		}
	}
	
	public void update(Article article) {
		long count=LuceneHelper.single().updateIndex("id", article.getId().toString(), getDocument(article));
		if(count==0) {
			log.error("文章引索更新失败！id="+article.getId());
			SysLogHelper.addErrorLog("文章索引更新失败", SysLogActionEnum.UPDATE.getName(), Kv.by("article", article).toJson());
		}
	}
	
	 
}

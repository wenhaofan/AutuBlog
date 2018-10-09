package com.autu._admin.article;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;

import com.autu.common.annotation.SysLogInfo;
import com.autu.common.interceptor.SysLogInterceptor;
import com.autu.common.kit.EmailKit;
import com.autu.common.lucene.LuceneHelper;
import com.autu.common.model.entity.Article;
import com.autu.common.safe.JsoupFilter;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.log.Log;

@Before(SysLogInterceptor.class)
public class AdminArticleLuceneIndexes {
	
	private static final Log log = Log.getLog(EmailKit.class);
 
	
	@Inject
	private AdminArticleService adminArticleService;
	
	@SysLogInfo(value="重置索引",action="delete")
	public void resetArticleIndexes() {
		deleteAll();
		addAll();
	}

	
	public boolean deleteAll() {
		try {
			return LuceneHelper.single().deleteAll()>0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(),e);
			return false;
		}
		
	}
	
	public boolean addAll() {
		List<Article> articleList=adminArticleService.listAll(1);
		List<Document> documents=new ArrayList<>();
		for(Article article:articleList) {
			documents.add(getDocument(article));
		}
		return LuceneHelper.single().createIndexs(documents)>0;
	}

	@SysLogInfo(value="添加多个文章索引",action="save")
	public boolean addIndexs(List<Article> articles) {
		List<Document> documents=new ArrayList<>();
		for(Article article:articles) {
			documents.add(getDocument(article));
		}
		return LuceneHelper.single().createIndexs(documents)>0;
	}
	
	@SysLogInfo(value="添加文章索引",action="save")
	public boolean addIndex(Article article) {
		//过滤html标签
		Document document = getDocument(article);
		return LuceneHelper.single().createIndex(document)>0;
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
	
	
	@SysLogInfo(value="删除索引",action="delete")
	public boolean delete(Integer id) {
		return LuceneHelper.single().deleteIndex("id", id.toString())>0;
	 
	}
	
	@SysLogInfo(value="更新索引",action="update")
	public boolean update(Article article) {
		return LuceneHelper.single().updateIndex("id", article.getId().toString(), getDocument(article))>0;
	}
	
	 
}

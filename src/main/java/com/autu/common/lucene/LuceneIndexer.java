package com.autu.common.lucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;

import com.autu.common.model.entity.Article;

public class LuceneIndexer {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
    	LuceneHelper helper =LuceneHelper.single();
    	List<Document> documents=new ArrayList();
    	documents.add(getDocument(new Article().setId(1).setContent("早上好").setTitle("晚安")));
		 helper.createIndexs(documents);
    }
    private static Document getDocument(Article article) {
		Document document = new Document();
	 
	    FieldType type = new FieldType();
	    type.setIndexOptions(IndexOptions.DOCS);
	    type.setTokenized(false);
	    type.setStored(true);

	    document.add(new Field("id", String.valueOf(article.getId()), type));
		Field  idField = new StoredField("id",article.getId());
		Field  contentField = new TextField("content", article.getContent(), Store.YES);
		Field  titleField = new TextField("title", article.getTitle(), Store.YES);
		document.add(idField);
		document.add(contentField);
		document.add(titleField);
		return document;
	}
}
 
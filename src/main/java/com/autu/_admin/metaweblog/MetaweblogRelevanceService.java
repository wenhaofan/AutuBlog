package com.autu._admin.metaweblog;

import com.autu.common.aop.Inject;
import com.autu.common.model.entity.MetaweblogRelevance;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.SqlPara;

public class MetaweblogRelevanceService {

	@Inject
	private MetaweblogRelevance dao;
	
	public void add(String postId,Integer configId,Integer articleId) {
		MetaweblogRelevance relevance=new MetaweblogRelevance();
		relevance.setMetaweblogId(configId);
		relevance.setPostId(postId);
		relevance.setArticleId(articleId);
		relevance.save();
	}
	
	public MetaweblogRelevance get(Integer articleId,Integer metaweblogId) {
		SqlPara sql=dao.getSqlPara("adminMetaweblogRelevance.get", Kv.by("articleId", articleId).set("metaweblogId", metaweblogId));
		return dao.findFirst(sql);
	}
}

package com.autu._admin.statistic;

import java.util.ArrayList;
import java.util.List;

import com.autu.common.model.entity.Article;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;

public class StatisticArticleService {

	@Inject
	private Article dao;

	public Ret hotArticle(Integer size) {
		SqlPara sql=Db.getSqlPara("statistics.hotArticle", Kv.by("size", size));
		List<Article> articleList=dao.find(sql);
		
		List<String> titleList=new ArrayList<>();
		List<Integer> pvList=new ArrayList<>();
		
		for(Article article:articleList) {
			titleList.add(article.getTitle());
			pvList.add(article.getPv());
		}
		return Ret.ok("titleList", titleList).set("pvList", pvList);
	}

	
}

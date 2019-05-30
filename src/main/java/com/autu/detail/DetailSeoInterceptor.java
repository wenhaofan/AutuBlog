package com.autu.detail;

import java.util.List;

import com.autu.common.interceptor.BaseSeoInterceptor;
import com.autu.common.model.Article;
import com.autu.common.model.Config;
import com.autu.common.model.Meta;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class DetailSeoInterceptor extends BaseSeoInterceptor {

 
	@Override
	public void indexSeo(Invocation inv) {
		 
		Controller c=inv.getController();
		Config config=c.getAttr("config");
		setSeoKeyWords(c,config.getKeywords());
		setSeoTitle(c,config.getTitle());
		setSeoDescr(c, config.getDescription());
	 
	}

	@Override
	public void otherSeo(Invocation inv) {
		Controller c=inv.getController();
		Article article = c.getAttr("article");

		List<Meta> tags=c.getAttr("atags");
		List<Meta> categorys=c.getAttr("acategorys");

		
		setSeoTitle(c, article.getTitle());
		setSeoKeyWords(c,keywords(tags,categorys,article.getTitle())+",范文皓,范文皓的个人博客");
		setSeoDescr(c, article.getTitle()+",范文皓,范文皓的个人博客");
	}

	public String keywords(List<Meta> tags,List<Meta> categorys,String title) {
		StringBuilder sb=new StringBuilder();
		for(Meta meta:tags) {
			sb.append(meta.getName()+",");
		}
		for(Meta meta:categorys) {
			sb.append(meta.getName()+",");
		}
		sb.append(title);
		return sb.toString();
	}
}

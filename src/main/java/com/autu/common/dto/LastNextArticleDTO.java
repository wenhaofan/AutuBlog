package com.autu.common.dto;

import com.autu.common.model.Article;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年11月13日 下午6:26:16
*/
public class LastNextArticleDTO {

	private Article lastArticle;
	
	private Article nextArticle;

	public Article getLastArticle() {
		return lastArticle;
	}

	public LastNextArticleDTO setLastArticle(Article lastArticle) {
		this.lastArticle = lastArticle;
		return this;
	}

	public Article getNextArticle() {
		return nextArticle;
	}

	public LastNextArticleDTO setNextArticle(Article nextArticle) {
		this.nextArticle = nextArticle;
		return this;
	}
	
 
	
}

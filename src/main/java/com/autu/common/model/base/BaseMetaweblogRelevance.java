package com.autu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseMetaweblogRelevance<M extends BaseMetaweblogRelevance<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setMetaweblogId(java.lang.Integer metaweblogId) {
		set("metaweblogId", metaweblogId);
		return (M)this;
	}
	
	public java.lang.Integer getMetaweblogId() {
		return getInt("metaweblogId");
	}

	public M setPostId(java.lang.String postId) {
		set("postId", postId);
		return (M)this;
	}
	
	public java.lang.String getPostId() {
		return getStr("postId");
	}

	public M setArticleId(java.lang.Integer articleId) {
		set("articleId", articleId);
		return (M)this;
	}
	
	public java.lang.Integer getArticleId() {
		return getInt("articleId");
	}

}
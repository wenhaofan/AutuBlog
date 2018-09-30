package com.autu.common.model.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRelevancy<M extends BaseRelevancy<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setMid(java.lang.Integer mid) {
		set("mid", mid);
		return (M)this;
	}
	
	public java.lang.Integer getMid() {
		return getInt("mid");
	}

	public M setCid(java.lang.Integer cid) {
		set("cid", cid);
		return (M)this;
	}
	
	public java.lang.Integer getCid() {
		return getInt("cid");
	}

}

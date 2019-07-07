package com.autu.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseOperateLog<M extends BaseOperateLog<M>> extends Model<M> implements IBean {

	public M setPkId(java.lang.Integer pkId) {
		set("pkId", pkId);
		return (M)this;
	}
	
	public java.lang.Integer getPkId() {
		return getInt("pkId");
	}

	public M setGmtCreate(java.util.Date gmtCreate) {
		set("gmtCreate", gmtCreate);
		return (M)this;
	}
	
	public java.util.Date getGmtCreate() {
		return get("gmtCreate");
	}

	public M setUserId(java.lang.Integer userId) {
		set("userId", userId);
		return (M)this;
	}
	
	public java.lang.Integer getUserId() {
		return getInt("userId");
	}

	public M setType(java.lang.Integer type) {
		set("type", type);
		return (M)this;
	}
	
	public java.lang.Integer getType() {
		return getInt("type");
	}

	public M setData(java.lang.String data) {
		set("data", data);
		return (M)this;
	}
	
	public java.lang.String getData() {
		return getStr("data");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}
	
	public java.lang.String getUrl() {
		return getStr("url");
	}

	public M setModuleName(java.lang.String moduleName) {
		set("moduleName", moduleName);
		return (M)this;
	}
	
	public java.lang.String getModuleName() {
		return getStr("moduleName");
	}

}

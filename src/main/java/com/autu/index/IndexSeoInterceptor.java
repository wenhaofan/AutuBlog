package com.autu.index;

import com.autu.common.interceptor.BaseSeoInterceptor;
import com.autu.common.model.Config;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 
 * @author 作者:范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class IndexSeoInterceptor extends BaseSeoInterceptor {

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
		
	}
}

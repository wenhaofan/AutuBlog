package com.autu.common.interceptor;

import java.util.ArrayList;

import com.autu.common.dto.SEO;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public abstract class BaseSeoInterceptor implements Interceptor {
	public static final String SEO_TITLE = "seoTitle";
	public static final String SEO_KEYWORDS = "seoKeywords";
	public static final String SEO_DESCR = "seoDescr";
	private SEO seo = new SEO();
	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	private static ArrayList<String> indexSeoUrl = new ArrayList() {
		{
			add("/");
			add("/search");
			add("/article/category");
			add("/article/search");
		}
	};

	@Override
	public void intercept(Invocation inv) {
		inv.invoke();
		String controllerKey = inv.getActionKey();

		if (indexSeoUrl.contains(controllerKey)) {
			indexSeo(inv);
		} else {
			otherSeo(inv);
		}

		inv.getController().setAttr("seo", seo);
	}

	public void setSeoTitle(Controller c, String seoTitle) {
		seo.setTitle(seoTitle);
	}

	public void setSeoKeyWords(Controller c, String seoKeywords) {
		seo.setKeywords(seoKeywords);
	}

	public void setSeoDescr(Controller c, String seoDescr) {
		seo.setDscr(seoDescr);
	}

	public abstract void indexSeo(Invocation inv);

	public abstract void otherSeo(Invocation inv);
}

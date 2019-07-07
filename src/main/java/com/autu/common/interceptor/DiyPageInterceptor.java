package com.autu.common.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.autu.common.model.Page;
import com.autu.diy.PageService;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class DiyPageInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {

		Controller c = inv.getController();

		String target = c.getRequest().getRequestURI();

		if (c.getRequest().getAttribute("page") != null) {
			inv.invoke();
			return;
		}

		PageService pageService = Aop.get(PageService.class);

		Page currPage = null;

		for (Page page : pageService.all()) {

			Pattern pattern = Pattern.compile(page.getPattern());
			Matcher matcher = pattern.matcher(target);

			if (matcher.find()) {

				if (target.startsWith(matcher.group())) {
					currPage = page;
					break;
				}
			}

		}

		if (currPage == null) {
			inv.invoke();
			return;
		}
		c.set("page", currPage);

		c.forwardAction("/diy/page");

	}

}

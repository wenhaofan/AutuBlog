package com.autu.common.interceptor;
/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月13日 下午2:02:49
*/

import com.autu.common._config.BlogContext;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.render.Render;
import com.jfinal.render.TemplateRender;

public class ThemesInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
			inv.invoke();
			Render r = inv.getController().getRender();
			if (r instanceof TemplateRender) {
			TemplateRender render = (TemplateRender)r;
			String view=render.getView();
			String themePath="/_view/templates/"+BlogContext.getTheme()+"/";		
			render.setView(themePath+view);
		}
	}

}

package com.autu.common.model;

import java.util.List;

import com.autu.common.model.base.BaseArticle;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Article extends BaseArticle<Article> {

	public static Integer STATE_DRAFT=0;
	public static Integer STATE_PUBLISH=1;
	public static Integer STATE_DISCARD=2;

	public String url;

	public void setUrl(String url) {
		this.url = url;
	}

	private List<Meta> metas;

	public List<Meta> getMetas() {
		return metas;
	}

	public void setMetas(List<Meta> metas) {
		this.metas = metas;
	}
	
	public String getUrl() {
		if(StrKit.notBlank(url)) {
			return url;
		}
		String projectPath=PropKit.get("projectPath");
		if(!projectPath.endsWith("/")) {
			projectPath+="/";
		}
		url= projectPath+"article/"+getIdentify();
		
		return url;
	}
	
	public Meta getFirstMeta() {
		if( getMetas()==null|| getMetas().isEmpty()) {
			return new Meta();
		}
		return getMetas().get(0);
	}
	
}

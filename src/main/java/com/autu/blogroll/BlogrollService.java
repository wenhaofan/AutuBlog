package com.autu.blogroll;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月14日 下午2:44:05
*/

import java.util.List;

import com.autu.common._config.BlogContext;
import com.autu.common.model.Blogroll;
import com.jfinal.aop.Inject;

public class BlogrollService {

	@Inject
	private Blogroll dao;
	
	public List<Blogroll> list(){
		return dao.findByCache(BlogContext.CacheNameEnum.BLOGROLL.name(), "list", "select * from blogroll order by sort desc");
	}
}

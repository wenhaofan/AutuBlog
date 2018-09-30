package com.autu._admin.blogroll;

import java.util.List;

import com.autu.common._config.BlogContext;
import com.autu.common.aop.Inject;
import com.autu.common.model.entity.Blogroll;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 友情链接的实现类
 * @author fwh
 *
 */
public class AdminBlogrollService{

 
	@Inject
	private  Blogroll dao;
	
	/**
	 * 更新或删除
	 * @param blogroll
	 */
	public void saveOrUpdate(Blogroll blogroll) {
		if(blogroll.getId()!=null) {
			blogroll.update();
		}else{
			blogroll.save();
		}
		CacheKit.removeAll(BlogContext.CacheNameEnum.BLOGROLL.name());
	}

	public void remove(Integer id) {
		dao.deleteById(id);
		CacheKit.removeAll(BlogContext.CacheNameEnum.BLOGROLL.name());
	}

	public void update(Blogroll blogroll) {
		blogroll.update();
		CacheKit.removeAll(BlogContext.CacheNameEnum.BLOGROLL.name());
	}

	public List<Blogroll> listBlogroll() {
		SqlPara sqlPara=dao.getSqlPara("adminBlogroll.list");
		return dao.findByCache(BlogContext.CacheNameEnum.BLOGROLL.name(),"adminList",sqlPara.getSql());
	}
	
	public Blogroll getBlogrollById(Integer id) {
		return dao.findById(id);
	}

}

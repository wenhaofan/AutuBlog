package com.autu.user;

import com.autu.common._config.BlogContext;
import com.autu.common.model.entity.User;
import com.jfinal.aop.Inject;

/**
 *  
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class UserService {

	@Inject
	public User dao;
	
	public User getAdminUser() {
		User user=dao.findFirstByCache(BlogContext.CacheNameEnum.BLOGGER.name(), "getAdminUser", "select * from user");
		return user.setPwd("");
	}
	
}

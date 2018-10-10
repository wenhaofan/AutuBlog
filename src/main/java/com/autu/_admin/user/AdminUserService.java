package com.autu._admin.user;

import com.autu.common._config.BlogContext;
import com.autu.common.aop.Inject;
import com.autu.common.kit.StrKit;
import com.autu.common.model.entity.User;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.ehcache.CacheKit;

public class AdminUserService {

	@Inject
	private User dao;
	
	public Ret editPassword(User user,String oladpassword,String newPassword) {
		user=dao.findById(user.getId());
		String oladpasswordMd5=HashKit.md5(oladpassword);
	
		if(!StrKit.equals(oladpasswordMd5, user.getPwd())) {
			return Ret.fail("msg", "原密码错误！");
		}
		user.setPwd(HashKit.md5(newPassword));
		user.update();
		return Ret.ok("msg", "密码修改成功！");
	}
	
 
	public Ret update(User user) {
		user.setId(getAdminUser().getId());
		user.update();
		CacheKit.remove(BlogContext.CacheNameEnum.BLOGGER.name(),  "getAdminUser");
		return Ret.ok();
	}
	
	public User getAdminUser() {
		User user=dao.findFirstByCache(BlogContext.CacheNameEnum.BLOGGER.name(), "getAdminUser", "select * from user");
		return user.setPwd("");
	}
	
}

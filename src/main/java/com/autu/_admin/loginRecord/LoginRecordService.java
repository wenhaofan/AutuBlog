package com.autu._admin.loginRecord;
/**
* @author 作者:范文皓
* @createDate 创建时间：2018年11月26日 上午11:05:02
*/

import java.util.List;

import com.autu.common.model.LoginRecord;
import com.autu.common.model.Session;
import com.autu.user.LoginService;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.ehcache.CacheKit;

public class LoginRecordService {

	@Inject
	private LoginRecord loginRecordDao;
	@Inject
	private Session sessionDao;

	public Page<LoginRecord> page(Integer pageNum, Integer pageSize) {
		SqlPara sql = loginRecordDao.getSqlPara("adminLoginRecord.page");
		return loginRecordDao.paginate(pageNum, pageSize, sql);
	}

	public List<LoginRecord> listRecent(Integer limit) {
		SqlPara sqlPara = loginRecordDao.getSqlPara("adminLoginRecord.listRecent", limit);
		return loginRecordDao.find(sqlPara);
	}

	public boolean downline(Integer id) {
		LoginRecord loginRecord = loginRecordDao.findById(id);
		loginRecord.setIsValid(false);

		boolean b = loginRecord.update();
		if (!b) {
			return b;
		}
		b = sessionDao.deleteById(loginRecord.getSessionId());
		
		CacheKit.remove(LoginService.sessionCacheKey, loginRecord.getSessionId());
		return b;
	}
}

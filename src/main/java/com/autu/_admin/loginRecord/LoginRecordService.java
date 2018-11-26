package com.autu._admin.loginRecord;
/**
* @author 作者:范文皓
* @createDate 创建时间：2018年11月26日 上午11:05:02
*/

import java.util.List;

import com.autu.common.model.entity.LoginRecord;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class LoginRecordService {

	@Inject
	private LoginRecord loginRecordDao;
	
	public Page<LoginRecord> page(Integer pageNum,Integer pageSize){
		SqlPara sql=loginRecordDao.getSqlPara("adminLoginRecord.page");
		return loginRecordDao.paginate(pageNum, pageSize,sql);
	}
	
	public List<LoginRecord> listRecent(Integer limit){
		SqlPara sqlPara=loginRecordDao.getSqlPara("adminLoginRecord.listRecent",limit);
		return loginRecordDao.find(sqlPara);
	}
	
}

package com.autu._admin.config;

import com.autu.common._config.BlogContext;
import com.autu.common.model.Config;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.SqlPara;

public class AdminConfigService {

	@Inject
	private Config dao;
	
	public Config get() {
		SqlPara sqlPara=dao.getSqlPara("adminConfig.get");
		return dao.findFirst(sqlPara);
	}
	
	public Ret saveOrUpdate(Config config) {
		if(config.getId()!=null) {
			config.update();
		}else {
			config.save();
		}
		BlogContext.reset(get());
		
		return Ret.ok();
	}
	 
}

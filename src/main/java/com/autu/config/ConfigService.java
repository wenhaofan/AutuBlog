package com.autu.config;

import com.autu.common.aop.Inject;
import com.autu.common.model.entity.Config;

public class ConfigService {

	@Inject
	private Config dao;
	
	public Config get() {
		Config config= dao.findFirst("select * from  config order by gmtCreate desc limit 1");
		return config==null?new Config():config;
	}
}

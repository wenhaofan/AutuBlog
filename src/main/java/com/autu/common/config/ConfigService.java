package com.autu.common.config;

import com.autu.common.model.Config;
import com.jfinal.aop.Inject;

public class ConfigService {

	@Inject
	private Config dao;
	
	public Config get() {
		Config config= dao.findFirst("select * from  config order by gmtCreate desc limit 1");
		return config==null?new Config():config;
	}
}
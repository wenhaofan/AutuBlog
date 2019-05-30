package com.autu._admin.theme;

import java.util.List;

import com.autu.common.model.ThemeConfig;
import com.jfinal.aop.Inject;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月16日-下午5:30:55 
*/
public class AdminThemeService {

	@Inject
	private ThemeConfig themeConfig;
	
	public List<ThemeConfig> list(){
		//return themeConfig.findAll();
		return null;
	}
	
	public boolean update(ThemeConfig themeConfig) {
		return themeConfig.update();
	}
	
	
	
}

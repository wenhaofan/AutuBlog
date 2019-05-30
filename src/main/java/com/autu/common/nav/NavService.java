package com.autu.common.nav;

import java.util.List;

import com.autu.common.model.Nav;
import com.jfinal.aop.Inject;

/**
 * 后台页面控制器
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class NavService {

	@Inject
	private Nav dao;
	
	public List<Nav> list(){
		return dao.find("select * from nav order by sort desc");
	}
}

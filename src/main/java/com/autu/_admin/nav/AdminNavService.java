package com.autu._admin.nav;

import com.autu.common.model.entity.Nav;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;

public class AdminNavService {

	@Inject
	private Nav dao;
	
	public Ret save(Nav nav) {
		nav.save();
		return Ret.ok();
	}
	
	public Ret delete(Integer toId) {
		dao.deleteById(toId);
		return Ret.ok();
	}
	
	public Ret update(Nav nav) {
		nav.update();
		return Ret.ok();
	}
	
	public Nav get(Integer id) {
		return dao.findById(id);
	}
}

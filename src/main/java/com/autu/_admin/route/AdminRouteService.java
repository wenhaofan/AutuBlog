package com.autu._admin.route;

import com.autu.common.exception.MsgException;
import com.autu.common.model.Route;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class AdminRouteService {

	private static Route routeDao=new Route().dao();
	
	public Page<Route> page(Integer pageNum,Integer pageSize,Route query){
		 
		SqlPara sqlPara=Db.getSqlPara("route.page", Kv.by("query", query));
		return routeDao.paginate(pageNum, pageSize, sqlPara);
	}
	
	public boolean delete(Integer id) {
		Route route=routeDao.findById(id);
		if(route==null) {
			throw new MsgException("页面不存在");
		}
		return route.setIsDeleted(true).update();
	}
}

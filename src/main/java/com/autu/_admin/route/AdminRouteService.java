package com.autu._admin.route;

import java.util.List;

import com.autu.common.model.RouteMenu;

public class AdminRouteService {

	private static RouteMenu menuDao=new RouteMenu().dao();
	
	public List<RouteMenu> listAll(){
		return menuDao.find("select * from route_menu where isDeleted=0");
	}
	
}

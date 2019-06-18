package com.autu.diy;

import java.util.List;

import com.autu.common.model.Page;

public class PageService {
 
	private Page pageDao=new Page().dao();
	
	public List<Page> all(){
		return pageDao.find("select * from page where isDeleted =0");
	}
	
}

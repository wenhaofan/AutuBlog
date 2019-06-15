package com.autu.timeline;

import java.util.List;

import com.autu.common.model.Article;
 
public class TimelineService {

	private static Article dao=new Article().dao();
	
	public List<Article> all(){
		return dao.findAll();
	}
	
}

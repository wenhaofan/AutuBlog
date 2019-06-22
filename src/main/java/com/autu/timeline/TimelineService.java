package com.autu.timeline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.autu.common.model.Article;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
 
public class TimelineService {

	private static Article dao=new Article().dao();
	
	public Map<String,List<Article>> all(Integer pageSize,Integer pageNum){
		
			if(pageSize<=0) {
				pageSize=1;
			}
		
		 SqlPara sqlPara= Db.getSqlPara("timeline.list", Kv.by("pageSize", pageSize).set("pageNum", pageNum));
		
		 List<Article> articles= dao.find(sqlPara);
		
		 return groupByGmtCreate(articles);
		 
	}
	
	public Map<String,List<Article>> groupByGmtCreate( List<Article> articles){
		Map<String,List<Article>> map =new LinkedHashMap<>();
		
		if(articles.isEmpty()) {
			return map;
		}
		
		articles.forEach( article ->{
			List<Article> values=map.get(article.getTimelineGmtCreate());
			
			if(values==null) {
				values=new ArrayList<>();
				map.put(article.getTimelineGmtCreate(), values);
			}
			
			values.add(article);
		});
		
		return map;
	}
	
}

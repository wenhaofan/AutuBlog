package com.autu._admin.statistic;

import java.util.ArrayList;
import java.util.List;

import com.autu.common.model.entity.AccessLog;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

public class StatisticAccessLogService {

	@Inject
	private AccessLog dao;
	
	public Ret accessNum(Integer days){
		
		SqlPara sql=Db.getSqlPara("statistics.beforeNum", Kv.by("days", days));
		
		List<Record> list= Db.find(sql);
		
		List<String> dayList=new ArrayList<>();
		List<Integer> numList=new ArrayList<>();
		for(Record item:list) {
			dayList.add(item.getStr("gmtcreate"));
			numList.add(item.getInt("count"));
		}
		
		return Ret.ok("dayList", dayList).set("numList", numList);
	}
	
}

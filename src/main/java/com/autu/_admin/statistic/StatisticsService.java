package com.autu._admin.statistic;

import java.util.Date;

import com.autu.common.kit.Ret;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;

public class StatisticsService {
	/**
	 * 获取创建时间为时间区间内的记录总数
	 * @param tableName
	 * @param gmtStart
	 * @param gmtEnd
	 * @return
	 */
	public Ret countByDate(String tableName,Date gmtStart,Date gmtEnd) {
		SqlPara sql=Db.getSqlPara("statistics.countByDate", 
				Kv.by("tableName",tableName).set("gmtStart", gmtStart).set("gmtEnd", gmtEnd));
		Integer count=Db.queryInt(sql.getSql(),sql.getPara());
		return Ret.ok("count", count);
	}
}

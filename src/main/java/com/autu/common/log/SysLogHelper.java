package com.autu.common.log;

import com.autu.common.model.entity.SysLog;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月4日 下午4:29:36
*/
public class SysLogHelper {
	
 
	public static void saveInfoLog(String content,String action,String data) {
		saveSysLog(content, data, action, null, null,null,SysLogLevelEnum.INFO.getValue());
	}
	public static void saveWarnLog(String content,String action,String data) {
		saveSysLog(content, data, action, null, null,null,SysLogLevelEnum.WARN.getValue());
	}
	public static void saveErrorLog(String content,String action,String data) {
		saveSysLog(content, data, action, null, null,null,SysLogLevelEnum.ERROR.getValue());
	}
	public static void saveSysLog(String content,String action,String data,Integer level) {
		saveSysLog(content, data, action, null, null,null,level);
	}
	public static void saveSysLog(String content,String data,
			String action,String ip,String url,Integer userId,Integer level) {
		SysLog sysLog=new SysLog();
		sysLog.setContent(content);
		sysLog.setAction(action);
		sysLog.setData(data);
		sysLog.setIp(ip);
		sysLog.setUserId(userId);
		sysLog.setLevel(level);
		sysLog.setUrl(url);
		saveSysLog(sysLog);
	}
	
	public static void saveSysLog(SysLog sysLog) {
		sysLog.save();
	}
}

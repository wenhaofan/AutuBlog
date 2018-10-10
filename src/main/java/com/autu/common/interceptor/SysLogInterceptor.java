package com.autu.common.interceptor;

import java.lang.reflect.Method;

import com.autu.common.annotation.SysLogInfo;
import com.autu.common.controller.BaseController;
import com.autu.common.kit.IpKit;
import com.autu.common.log.SysLogHelper;
import com.autu.common.log.SysLogLevelEnum;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.Kv;

public class SysLogInterceptor implements   Interceptor {

	@SuppressWarnings("unchecked")
	@Override
	public void intercept(Invocation inv) {
		inv.invoke();
		
		Method method=inv.getMethod();
		
		SysLogInfo sysLog=method.getAnnotation(SysLogInfo.class);
		
		if(sysLog==null) {
			return;
		}
		
		BaseController c=(BaseController)inv.getController();
		String content=sysLog.value();
		String action=sysLog.action();
		String ip=null;
		String data=null;
		Integer userId=null;
		String url=null;
		Kv dataMap=Kv.create();
		
		if(inv.isActionInvocation()) {
			ip=IpKit.getRealIp(c.getRequest());
			if(!inv.getControllerKey().contains("upload")) {
				dataMap.putAll(c.getParaMap());
			}
			userId=c.getLoginUser().getId();
			url=c.getRequest().getRequestURL().toString();
		}else {
			dataMap.set("methodArgs", inv.getArgs());
		}
		data=dataMap.toJson();
		SysLogHelper.saveSysLog(content, data, action, ip,url,userId,SysLogLevelEnum.INFO.getValue());
	}

}

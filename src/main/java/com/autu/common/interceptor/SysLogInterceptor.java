package com.autu.common.interceptor;

import com.autu.common.annotation.SysLogInfo;
import com.autu.common.controller.BaseController;
import com.autu.common.kit.IpKit;
import com.autu.common.log.SysLogHelper;
import com.autu.common.log.SysLogLevelEnum;
import com.autu.common.model.entity.SysLog;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import java.lang.reflect.Method;

public class SysLogInterceptor implements Interceptor {
	public void intercept(Invocation inv) {
		Exception e = null;

		try {
			inv.invoke();
		} catch (Exception arg4) {
			e = arg4;
		}

		Method method = inv.getMethod();
		SysLogInfo sysLogInfo = (SysLogInfo) method.getAnnotation(SysLogInfo.class);
		
		if(sysLogInfo!=null) {
			if (inv.isActionInvocation()) {
				this.doControllerSysLog(method, inv, e);
			} else {
				this.doServiceSysLog(method, inv, e);
			}
		}

		if (e != null) {
			throw new RuntimeException(e);
		}
	}

	public void doServiceSysLog(Method method, Invocation inv, Exception e) {
		Object returnValue = inv.getReturnValue();
		Integer level = SysLogLevelEnum.INFO.getValue();
		String data = this.getDataMap(inv, e).toJson();
		if (e != null) {
			level = SysLogLevelEnum.WARN.getValue();
		}

		if (returnValue instanceof Ret && ((Ret) returnValue).isFail()) {
			level = SysLogLevelEnum.WARN.getValue();
		} else if (returnValue instanceof Boolean && !((Boolean) returnValue).booleanValue()) {
			level = SysLogLevelEnum.WARN.getValue();
		}

		SysLogInfo sysLogInfo = (SysLogInfo) method.getAnnotation(SysLogInfo.class);
		SysLog sysLog = this.getSysLog(sysLogInfo);
		sysLog.setLevel(level);
		sysLog.setData(data);
		SysLogHelper.saveSysLog(sysLog);
	}

	public Kv getDataMap(Invocation inv, Exception e) {
		Kv dataMap = Kv.by("methodArgs", inv.getArgs()).set("returnValue", inv.getReturnValue());
		if (e != null) {
			dataMap.set("exception", e.getMessage());
		}

		if (inv.isActionInvocation() && !inv.getControllerKey().contains("upload")) {
			dataMap.set("paras", inv.getController().getParaMap());
		}

		return dataMap;
	}

	private SysLog getSysLog(SysLogInfo sysLogInfo) {
		SysLog sysLog = new SysLog();
		if (sysLogInfo == null) {
			return sysLog;
		} else {
			String content = sysLogInfo.value();
			String action = sysLogInfo.action();
			sysLog.setContent(content);
			sysLog.setAction(action);
			return sysLog;
		}
	}

	public void doControllerSysLog(Method method, Invocation inv, Exception e) {
		SysLogInfo sysLogInfo = (SysLogInfo) method.getAnnotation(SysLogInfo.class);
		BaseController c = (BaseController) inv.getController();
		Integer level = SysLogLevelEnum.INFO.getValue();
		if (e != null) {
			level = SysLogLevelEnum.WARN.getValue();
		}

		String ip = IpKit.getRealIp(c.getRequest());
		Integer userId = c.getLoginUser().getId();
		String url = c.getRequest().getRequestURL().toString();
		String data = this.getDataMap(inv, e).toJson();
		SysLog sysLog = this.getSysLog(sysLogInfo);
		sysLog.setIp(ip);
		sysLog.setData(data);
		sysLog.setUserId(userId);
		sysLog.setUrl(url);
		sysLog.setLevel(level);
		SysLogHelper.saveSysLog(sysLog);
	}
}
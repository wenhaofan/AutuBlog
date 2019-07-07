package com.autu._admin.common.config.interceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.autu._admin.common.annotation.OLog;
import com.autu._admin.operateLog.OperateLogService;
import com.autu.common.controller.BaseController;
import com.autu.common.model.OperateLog;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class OperateLogInterceptor implements Interceptor {

	@Inject
	private OperateLogService operateLogService;

	@Override
	public void intercept(Invocation inv) {

		if (!inv.isActionInvocation()) {
			inv.invoke();
			return;
		}

		Method targetMethod = inv.getMethod();

		// 判断目标方法是否有注解
		OLog sysLog = targetMethod.getAnnotation(OLog.class);

		if (sysLog == null) {
			inv.invoke();
			return;
		}

		HttpServletRequest request = inv.getController().getRequest();

		String targetUrl = getTargetUrl(request);
		
		String params = getParams(request);
		String moduleName = sysLog.moduleName();
		int type = sysLog.type().getValue();
		
		// 获取当前登录id
		Integer userId = ((BaseController) inv.getController()).getLoginUser().getId();

		OperateLog log = new OperateLog();
		log.setUserId(userId);
		log.setType(type);
		log.setData(params);
		log.setUrl(targetUrl);
		log.setModuleName(moduleName);

		// 保存操作记录
		operateLogService.save(log);

		inv.invoke();
	}

	private String getTargetUrl(HttpServletRequest request) {
		String queryParams = request.getQueryString();
		String targetUrl = request.getRequestURI() + (queryParams != null ? queryParams : "");
		return targetUrl;
	}

	public String getParams(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();

	 
		Enumeration<String> e = request.getParameterNames();

		if (e.hasMoreElements()) {

			while (e.hasMoreElements()) {
				String name = e.nextElement();
				String[] values = request.getParameterValues(name);
				if (values.length == 1) {
					map.put(name, values[0]);

				} else {
					StringBuilder sb = new StringBuilder();
					sb.append("{");
					for (int i = 0; i < values.length; i++) {
						if (i > 0)
							sb.append(",");
						sb.append(values[i]);
					}
					sb.append("}");

					map.put(name, sb.toString());
				}

			}

		}
		return JSONObject.toJSONString(map);
	}
}
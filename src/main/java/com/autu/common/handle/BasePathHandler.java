 
package com.autu.common.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

 
public class BasePathHandler extends Handler {
	
	private String basePathName;
	
	public BasePathHandler() {
		basePathName = "CONTEXT_PATH";
	}
	
	public BasePathHandler(String basePathName) {
		if (StrKit.isBlank(basePathName)) {
			throw new IllegalArgumentException("contextPathName can not be blank.");
		}
		this.basePathName = basePathName;
	}
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		
		String path = request.getContextPath();
		String port=request.getServerPort()==80?"":":"+request.getServerPort();
		String basePath = request.getScheme()+"://"+request.getServerName()+port+path+"/";
		request.setAttribute(basePathName, basePath);
		next.handle(target, request, response, isHandled);
	}
}

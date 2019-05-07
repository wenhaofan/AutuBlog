package com.autu.common.interceptor;

import com.autu.common.exception.MsgException;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.ActionException;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;

public class ExceptionInterceptor implements Interceptor {

	private static Log log=Log.getLog(ExceptionInterceptor.class);
	

	@Override
	public void intercept(Invocation inv) {
		try {
			inv.invoke();
		} catch (MsgException e) {
			inv.getController().renderJson(Ret.fail("msg", e.getMessage()));
			e.printStackTrace();
			log.info(e.getMessage(), e);
		}catch (Exception e) {
			if(e instanceof ActionException) {
				throw e;
			}
			e.printStackTrace();
			log.error(e.getMessage(),e);
	 
			if(isAjaxRequest(inv)) {
				inv.getController().renderJson(Ret.fail("msg", "出bug了！"));
			}else {
				inv.getController().renderError(404);
			}
		}
	
	}
 

	public boolean isAjaxRequest(Invocation inv) {
		return "XMLHttpRequest".equalsIgnoreCase(inv.getController().getRequest().getHeader("X-Requested-With"));
	}
}

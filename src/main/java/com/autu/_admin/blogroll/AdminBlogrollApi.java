package com.autu._admin.blogroll;

import java.util.List;

import com.autu.common.annotation.SysLog;
import com.autu.common.aop.Inject;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Blogroll;
import com.jfinal.kit.Ret;

public class AdminBlogrollApi extends BaseController {
	
	@Inject
	private AdminBlogrollService blogrollService;
	
	public void get() {
		Blogroll blogroll=blogrollService.getBlogrollById(getParaToInt());
		renderJson(Ret.ok("blogroll", blogroll));
	}
	
	public void list() {
		List<Blogroll> blogrolls=blogrollService.listBlogroll();
		renderJson( Ret.ok("code", 0).set("data",blogrolls));
	}
	
	@SysLog(value="编辑友链",action="saveOrUpdate")
	public void saveOrUpdate(){
		Blogroll blogroll=getModel(Blogroll.class,"",true);
		blogrollService.saveOrUpdate(blogroll);
		renderJson(Ret.ok());
	}
	
	@SysLog(value="删除友链",action="delete")
	public void remove(){
		Integer id=getParaToInt(0);
		blogrollService.remove(id);
		renderJson(Ret.ok());
	}
	
 
}

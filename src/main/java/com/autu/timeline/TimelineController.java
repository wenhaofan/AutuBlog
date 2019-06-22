package com.autu.timeline;

import com.autu.common.controller.BaseController;
import com.jfinal.aop.Inject;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月16日-下午3:04:39 
*/
public class TimelineController extends BaseController {

	@Inject
	private TimelineService TimelineService;
	
	public void index() {
		Integer pageSize=getParaToInt("pageSize", 6);
		Integer pageNum=getParaToInt("pageNum",1);
		set("timelineMap", TimelineService.all(pageSize,pageNum));
		render("page-timeline.html");
	}
	
}

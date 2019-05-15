package com.autu.resume;

import com.autu.common.controller.BaseController;
import com.jfinal.render.Render;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月16日-下午3:04:56 
*/
public class ResumeController extends BaseController {

	public void index() {
		notTheme();
		
		render("/_view/resume/foryou/resume/index.html");
	}
	
	
}

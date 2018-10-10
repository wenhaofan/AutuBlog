package com.autu._admin.meta;

import com.autu.common.annotation.SysLog;
import com.autu.common.aop.Inject;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Meta;
import com.jfinal.kit.Ret;

/**
 * 分类管理的控制器
 * 
 * @author fwh
 *
 */
public class AdminMetaApi  extends BaseController {

	@Inject
	private AdminMetaService metaService;
	
	@SysLog(value="删除分类或标签",action="delete")
	public void remove() {
		Integer id = getParaToInt(0);
		Meta meta=new Meta();
		meta.setId(id);
		metaService.delete(meta);
		renderJson(Ret.ok());
	}
	
	@SysLog(value="编辑分类或标签",action="saveOrUpdate")
	public void update() {
		Meta meta=getModel(Meta.class,"",true);
		metaService.saveOrUpdate(meta);
		renderJson(Ret.ok().toJson());
	}
	
	@SysLog(value="添加分类或标签",action="saveOrUpdate")
	public void add() {
		Meta meta=getModel(Meta.class,"",true);
		metaService.saveOrUpdate(meta);
		renderJson(Ret.ok().toJson());
	}

}

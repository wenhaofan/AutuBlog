package com.autu._admin.meta;

import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.Meta;
import com.jfinal.aop.Inject;
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
	
 
	public void remove() {
		Integer id = getParaToInt(0);
		Meta meta=new Meta();
		meta.setId(id);
		metaService.delete(meta);
		renderJson(Ret.ok());
	}
	
 
	public void update() {
		Meta meta=getModel(Meta.class,"",true);
		metaService.saveOrUpdate(meta);
		renderJson(Ret.ok().toJson());
	}
 
	public void add() {
		Meta meta=getModel(Meta.class,"",true);
		metaService.saveOrUpdate(meta);
		renderJson(Ret.ok().toJson());
	}

}

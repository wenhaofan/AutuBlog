package com.autu.meta;

import java.util.List;

import com.autu.common.aop.Inject;
import com.autu.common.model.entity.Meta;
import com.autu.common.model.entity.Relevancy;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.SqlPara;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月6日 下午12:38:51
*/
public class MetaService {

	@Inject
	private static Meta mdao;
	@Inject
	private static Relevancy rdao;


	public List<Meta> listByCId(Integer id, String type) {
		if (id == null) {
			return null;
		}
		Kv kv=Kv.create().set("cid", id).set("type", type);
		SqlPara sql=mdao.getSqlPara("meta.listByArticleId",kv);
		return mdao.find(sql);
	}

	/**
	 * 根据类型查询项目列表
	 *
	 * @param type
	 *            类型，tag or category
	 */
	public List<Meta> listMeta(String type) {
		if (StrKit.notBlank(type)) {
			SqlPara sql=mdao.getSqlPara("meta.list",Kv.by("type", type));
			return mdao.find(sql);
		}
		return null;
	}

	public Meta get(Integer id) {
		return mdao.findById(id);
	}
}

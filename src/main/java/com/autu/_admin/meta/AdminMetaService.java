package com.autu._admin.meta;

import java.util.List;

import com.autu.common.exception.MsgException;
import com.autu.common.kit.ListKit;
import com.autu.common.model.Meta;
import com.autu.common.model.Relevancy;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;

/**
* @author 作者:范文皓
* @createDate 创建时间：2018年9月20日 下午5:02:36
*/
public class AdminMetaService {
	@Inject
	private Meta mdao;
	@Inject
	private Relevancy rdao;
	
	public List<Meta> listByCId(Integer id, String type) {
		if (id == null) {
			throw new MsgException("内容id不能为空！");
		}
		Kv kv=Kv.create().set("cid", id).set("type", type);
		SqlPara sql=mdao.getSqlPara("adminMeta.listByArticleId",kv);
		return mdao.find(sql);
	}
	
	public Meta saveOrUpdate(Meta meta) {
		if (meta == null) {
			throw new MsgException("资源不能为空！");
		}
		if (StrKit.isBlank(meta.getName())) {
			throw new MsgException("数据name不能为空！");
		}
		if (StrKit.isBlank(meta.getType())) {
			throw new MsgException("数据type不能为空！");
		}

		if (meta.getId() == null) {
			meta.save();
			return meta;
		} else {
			meta.update();
			return mdao.findById(meta.getId());
		}

	}
	
	/**
	 * 保存多条数据
	 * 
	 * @param metas
	 * @param cid
	 */
	public void saveMetas(List<Meta> metas, Integer cid) {
		if (null == cid) {
			throw new MsgException("数据关联id不能为空!");
		}

		if (ListKit.isBlank(metas)) {
			throw new MsgException("数据不能为空!");
		}

		for (int i = 0, size = metas.size(); i < size; i++) {
			save(metas.get(i));
			saveRelevancy(cid, metas.get(i));
		}

	}
	/**
	 * 根据类型和名称获取资源
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public Meta getMeta(Meta meta) {

		if (meta.getId() != null) {
			return mdao.findById(meta.getId());
		}

		if (StrKit.notBlank(meta.getType(), meta.getName())) {
			SqlPara sqlPara=mdao.getSqlPara("adminMeta.get", Kv.by("type", meta.getType()).set("mname",  meta.getName()));
			return mdao.findFirst(sqlPara);
		}
		return null;
	}
	/**
	 * 保存资源
	 * 
	 * @param meta
	 */
	public void save(Meta meta) {

		Meta oldMeta = getMeta(meta);

		if (oldMeta != null) {
			meta.setId(oldMeta.getId());
			return;
		}

		if (!meta.save()) {
			throw new MsgException("保存失败！");
		}
	}

	/**
	 * 添加资源和内容的关联
	 * 
	 * @param cid
	 * @param meta
	 */
	public void saveRelevancy(Integer cid, Meta meta) {
		// 获取资源id
		Integer mid = meta.getId();
		// 如果资源id不为null则保存该资源和cid的关联
		if (mid != null) {
			SqlPara sqlPara=mdao.getSqlPara("adminMetaRelevance.isRelevance", Kv.by("cid", cid).set("mid", mid));
			Integer count = rdao.findFirst(sqlPara).getInt("count");
			// 不存在关联才创建关联
			if (count == 0) {
				Relevancy rel = new Relevancy();
				rel.setCid(cid);
				rel.setMid(mid);
				rel.save();
			}
		}
	}

	/**
	 * 删除
	 * 
	 * @param meta
	 */
	public void delete(Meta meta) {
  
		mdao.deleteById(meta.getId());
	}

	/**
	 * 删除数据 以及和数据的关联 
	 * 
	 * @param mid
	 * @param meta
	 */
	public void delete(Integer cid, Meta meta) {
		// 首先删除资源
		delete(meta);
		// 然后删除资源和内容的关联
		deleteRelevanceByMid(meta);
	}

	/**
	 * 根据meta id删除关联记录
	 * @param meta
	 */
	private void deleteRelevanceByMid(Meta meta) {
		SqlPara sqlPara=rdao.getSqlPara("adminMetaRelevance.delete",Kv.by("mid", meta.getId()));
		
		Db.update(sqlPara);
	}
	
 

	/**
	 * 删除和素材的关联
	 * 
	 * @param mid
	 * @param meta
	 */
	public void deleteRelevanceByCid(Integer cid) {
		SqlPara sqlPara=rdao.getSqlPara("adminMetaRelevance.delete",Kv.by("cid", cid));
		Db.update(sqlPara);
	}
}

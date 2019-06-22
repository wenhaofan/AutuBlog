package com.autu.index;

import java.util.List;

import com.autu.common.meta.MetaService;
import com.autu.common.meta.MetaTypeEnum;
import com.autu.common.model.Article;
import com.autu.common.model.Meta;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class IndexService {
	
	private static Article articleDao=new Article().dao();

	@Inject
	private MetaService metaService;
	
	/**
	 * 分页查询文章
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param metas
	 * @return
	 */
	public Page<Article> page(Integer pageNumber, Integer pageSize, Integer metaId, Boolean isTop) {
		SqlPara sqlPara = articleDao.getSqlPara("index.page", Kv.by("metaId", metaId).set("isTop", isTop));
		Page<Article> page = articleDao.paginate(pageNumber, pageSize, sqlPara);

		for (Article article : page.getList()) {
			List<Meta> metas = metaService.listByCId(article.getId(), MetaTypeEnum.CATEGORY.toString());
			article.setMetas(metas);
		}
		return page;
	}

	
	/**
	 * 获取指定文章
	 * 
	 * @return
	 */
	public List<Article> listTop() {
		SqlPara sql = articleDao.getSqlPara("index.listTop");
		List<Article> artiles = articleDao.find(sql);
		for (Article article : artiles) {
			List<Meta> metas = metaService.listByCId(article.getId(), MetaTypeEnum.CATEGORY.toString());
			article.setMetas(metas);
		}
		return artiles;
	}
}

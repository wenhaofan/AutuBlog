package com.autu._admin.config;

import java.util.List;

import com.autu.common.model.BaiduSeoConfig;
import com.jfinal.aop.Inject;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.SqlPara;

public class AdminBaiduSeoService {

	@Inject
	private BaiduSeoConfig dao;
	
	public BaiduSeoConfig get(Integer id) {
		return dao.findById(id);
	}
	
	public List<BaiduSeoConfig> list(){
		SqlPara sql=dao.getSqlPara("adminBaiduseo.list");
		return dao.find(sql);
	}
	
	public void pushLink(String link) {
		pushLink(list(),link);
	}
	
	@SuppressWarnings("unchecked")
	public void pushLink(String link,String site,String token) {
		String url="http://data.zz.baidu.com/urls?site="+site+"&token="+token;
		Kv headers=Kv.by("User-Agent", "curl/7.12.1 ").set("Host", "data.zz.baidu.com").set("Content-Type", "text/plain").set("Content-Length", "83");
		HttpKit.post(url,link,headers);
	}
	
	public boolean pushLink(List<BaiduSeoConfig> configs,String link) {
		try {
			configs.stream().forEach((BaiduSeoConfig config)->{
				pushLink(link,config.getSite(),config.getToken());
			}); 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Ret delete(Integer toId) {
		dao.deleteById(toId);
		return Ret.ok();
	}
	
	 
	public Ret saveOrUpdate(BaiduSeoConfig config) {
		if(config.getId()!=null) {
			config.update();
		}else {
			config.save();
		}
		return Ret.ok();
	}
}

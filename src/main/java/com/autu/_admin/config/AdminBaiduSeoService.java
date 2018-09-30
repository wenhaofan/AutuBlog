package com.autu._admin.config;

import java.util.List;

import com.autu.common.aop.Inject;
import com.autu.common.log.SysLogActionEnum;
import com.autu.common.log.SysLogHelper;
import com.autu.common.model.entity.BaiduSeoConfig;
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
	
	public void pushLink(List<BaiduSeoConfig> configs,String link) {
		configs.stream().forEach((BaiduSeoConfig config)->{
			try {
				pushLink(link,config.getSite(),config.getToken());
			} catch (Exception e) {
				e.printStackTrace();
				SysLogHelper.addWarnLog("百度推送接口异常！", SysLogActionEnum.OTHER.getName(), Kv.by("config", config).set("link", link).toJson());
			}
		}); 
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

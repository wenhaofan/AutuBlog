package com.autu._admin.metaweblog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.autu.common.aop.Inject;
import com.autu.common.log.SysLogActionEnum;
import com.autu.common.log.SysLogHelper;
import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Meta;
import com.autu.common.model.entity.MetaweblogConfig;
import com.autu.common.model.entity.MetaweblogRelevance;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;

public class MetaweblogHelper {

	@Inject
	private MetaweblogConfig dao;
	@Inject
	private MetaweblogRelevanceService relevanceService;
	
	public MetaweblogConfig get(Integer id) {
		return dao.findById(id);
	}
	
	
	public Ret saveOrUpdate(MetaweblogConfig config) {
		if(config.getId()!=null) {
			config.update();
		}else {
			config.save();
		}
		
		return Ret.ok();
	} 
	
	public Ret delete(Integer toId) {
		dao.deleteById(toId);
		return Ret.ok();
	}
	
 
	
	public List<MetaweblogConfig> list(){
		return dao.find("select * from metaweblog_config");
	}
	
	/**
	 * 执行文章推送
	 * @param article
	 * @param tags
	 * @return
	 */
	public Ret pushPostMetaweblog(Article article,List<Meta> tags) {
		List<MetaweblogConfig> configs= list();
		Ret result=null;
 
		Integer successCount=0;
		Integer failCount=0;
		
		Kv successKv=Kv.create();
		Kv failKv=Kv.create();
		for(MetaweblogConfig config:configs) {
			result=pushNewPost(config,article,tags);
			String type=result.getStr("type");
			if(result.isOk()) {
				successCount++;
				successKv.set(config.getWebsite(), config).set("type", type);
			}else {
				failCount++;
				failKv.set(config.getWebsite(), config).set("type", type);
				SysLogHelper.addWarnLog("metaweblog接口异常！", SysLogActionEnum.OTHER.getName(),Ret.by("config", config).toJson());

			}
		}
		
		return Ret.ok("success",successKv).set("fail", failKv);
	}
	/**
	 * 推送文章，拼接标签
	 * @param config
	 * @param article
	 * @param tags
	 * @return
	 */
	public Ret pushNewPost(MetaweblogConfig config,Article article,List<Meta> tags) {
		StringBuilder sb=new StringBuilder();
		 
		tags.forEach(new Consumer<Meta>() {
		    @Override
		    public void accept(Meta item) {
		    	sb.append(","+item.getName());
		    }
		});
		
		return pushPost(config,article,sb.toString());
	}
	
	/**
	 * 推送文章
	 * @param mconfig
	 * @param article
	 * @param keywords
	 * @return
	 */
	public Ret pushPost(MetaweblogConfig mconfig,Article article,String keywords) {
		MetaweblogRelevance  metaweblogRelevance =relevanceService.get(article.getId(),mconfig.getId());
		
		if(metaweblogRelevance!=null) {
			return editPost(mconfig, article, keywords,metaweblogRelevance);
		}else {
			return pushNewPost(mconfig, article, keywords);
		}
		
	}
	/**
	 * 推送新文章
	 * @param mconfig
	 * @param article
	 * @param keywords
	 * @return
	 */
	public Ret pushNewPost(MetaweblogConfig mconfig,Article article,String keywords) {
 
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(mconfig.getUrl()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
 
		Map<String, String> m = new HashMap<String, String>();
		m.put("title", article.getTitle());
		m.put("mt_keywords", keywords);
		m.put("description", signContent(article.getContent(),article.getUrl()));
		 
		Object[] params = new Object[]{"default", mconfig.getUserName(),mconfig.getPassword(), m, true};
		 
	
		try {
			String postId= client.execute("metaWeblog.newPost", params).toString();
			relevanceService.add(postId, mconfig.getId(), article.getId());
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Ret.fail();
		}
		
		return Ret.ok("type","newPost");
	}
	
	/**
	 * 执行修改
	 * @param mconfig
	 * @param article
	 * @param keywords
	 * @param metaweblogRelevance
	 * @return
	 */
	public Ret editPost(MetaweblogConfig mconfig,Article article,String keywords,MetaweblogRelevance  metaweblogRelevance) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(mconfig.getUrl()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
 
		Map<String, String> m = new HashMap<String, String>();
		m.put("title", article.getTitle());
		m.put("mt_keywords", keywords);
		m.put("description", signContent(article.getContent(),article.getUrl()));
		 
		Object[] params = new Object[]{metaweblogRelevance.getPostId(), mconfig.getUserName(),mconfig.getPassword(), m, true};
		 
		try {
			String postId= client.execute("metaWeblog.editPost", params).toString();
			System.out.println(postId);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Ret.fail();
		}
		
		return Ret.ok("type","editPost");
	}
	
	public String signContent(String content,String link){
		return "<br><a href='"+link+"'>个人博客 地址:"+link+" </a>"+content;
	}
	
}

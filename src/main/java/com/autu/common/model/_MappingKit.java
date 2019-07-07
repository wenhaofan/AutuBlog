package com.autu.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * 
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("article", "id", Article.class);
	 
		arp.addMapping("login_record", "id", LoginRecord.class);
		arp.addMapping("user", "id", User.class);
		arp.addMapping("session", "id", Session.class);	
		arp.addMapping("meta", "id",Meta.class);
		arp.addMapping("relevancy","id",Relevancy.class);
 
		arp.addMapping("access_log", "id", AccessLog.class);
		arp.addMapping("disk", "id", Disk.class);
		arp.addMapping("agent_user", "id", AgentUser.class);
		arp.addMapping("comment", "id", Comment.class);
		arp.addMapping("nav", "id", Nav.class);
		arp.addMapping("metaweblog_config", "id", MetaweblogConfig.class);
		arp.addMapping("baidu_seo_config", "id", BaiduSeoConfig.class);
		arp.addMapping("metaweblog_relevance", "id", MetaweblogRelevance.class);
		arp.addMapping("config", "id",Config.class);
		arp.addMapping("page", "id", Page.class);
		arp.addMapping("operate_log", "pkId", OperateLog.class);
	}

	 
}
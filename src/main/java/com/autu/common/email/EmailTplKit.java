package com.autu.common.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.jfinal.template.source.ISource;

/**
 * Email Kit
 */
public class EmailTplKit {
	
	static final String SQL_TEMPLATE_MAP_KEY = "_SQL_TEMPLATE_MAP_";
	static final String SQL_PARA_KEY = "_SQL_PARA_";
	public static final String MAIN_CONFIG_NAME = "main";
	public String configName;
	private boolean devMode;
	private Engine engine;
	private List<KeySource> keySourceList = new ArrayList<KeySource>();
	private Map<String, Template> emailTemplateMap;
	
	public EmailTplKit(boolean devMode) {
		this(MAIN_CONFIG_NAME,devMode);
	}
	
	public EmailTplKit(String configName, boolean devMode) {
		this.configName = configName;
		this.devMode = devMode;
		
		engine = new Engine(configName);
		engine.setDevMode(devMode);
		engine.addDirective("namespace", NameSpaceDirective.class);
		engine.addDirective("key", KeyDirective.class);
		engine.addSharedMethod(new StrKit());
	 
 	}
	
	public EmailTplKit(String configName) {
		this(configName, false);
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
		engine.setDevMode(devMode);
	}
	
	public void setBaseSqlTemplatePath(String baseSqlTemplatePath) {
		engine.setBaseTemplatePath(baseSqlTemplatePath);
	}
	
	public void addTemplate(String sqlTemplate) {
		if (StrKit.isBlank(sqlTemplate)) {
			throw new IllegalArgumentException("sqlTemplate can not be blank");
		}
		keySourceList.add(new KeySource(sqlTemplate));
	}
	
	public void addTemplate(ISource sqlTemplate) {
		if (sqlTemplate == null) {
			throw new IllegalArgumentException("sqlTemplate can not be null");
		}
		keySourceList.add(new KeySource(sqlTemplate));
	}
	
	public synchronized void parseSqlTemplate() {
		Map<String, Template> emailTemplateMap = new HashMap<String, Template>(512, 0.5F);
		for (KeySource ss : keySourceList) {
			Template template = ss.isFile() ? engine.getTemplate(ss.file) : engine.getTemplate(ss.source);
			Map<Object, Object> data = new HashMap<Object, Object>();
			data.put(SQL_TEMPLATE_MAP_KEY, emailTemplateMap);
			template.renderToString(data);
		}
		this.emailTemplateMap = emailTemplateMap;
	}
	
	private void reloadModifiedSqlTemplate() {
		engine.removeAllTemplateCache();	// 去除 Engine 中的缓存，以免 get 出来后重新判断 isModified
		parseSqlTemplate();
	}
	
	private boolean isSqlTemplateModified() {
		for (Template template : emailTemplateMap.values()) {
			if (template.isModified()) {
				return true;
			}
		}
		return false;
	}
	
	private Template getEmailTemplate(String key) {
		Template template = emailTemplateMap.get(key);
		if (template == null) {	// 此 if 分支，处理起初没有定义，但后续不断追加 sql 的情况
			if ( !devMode ) {
				return null;
			}
			if (isSqlTemplateModified()) {
				synchronized (this) {
					if (isSqlTemplateModified()) {
						reloadModifiedSqlTemplate();
						template = emailTemplateMap.get(key);
					}
				}
			}
			return template;
		}
		
		if (devMode && template.isModified()) {
			synchronized (this) {
				template = emailTemplateMap.get(key);
				if (template.isModified()) {
					reloadModifiedSqlTemplate();
					template = emailTemplateMap.get(key);
				}
			}
		}
		return template;
	}
	
 
 
	/**
	 * 示例：
	 * 1：模板 定义
	 * 	#key("key")
	 * 		
	 *	#end
	 *
	 * 2：java 代码
	 *	getEmailContent("key", Kv);
	 */
	public String getContent(String key,Kv kv) {
		Template template = getEmailTemplate(key);
		if (template == null) {
			return null;
		}
		return template.renderToString(kv);
	}
	
	public java.util.Set<java.util.Map.Entry<String, Template>> getSqlMapEntrySet() {
		return emailTemplateMap.entrySet();
	}
	
	public String toString() {
		return "EmailTplKit for config : " + configName;
	}
}






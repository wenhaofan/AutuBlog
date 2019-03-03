package com.autu.common.keys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.jfinal.template.source.ISource;
import com.jfinal.template.stat.ParseException;

/**
 * Email Kit
 */
public class KeyKit {

	static final String KEY_TEMPLATE_MAP_KEY = "_KEY_TEMPLATE_MAP_";
	static final boolean DEFAULT_DEV_MODE=false;
	public static final String MAIN_CONFIG_NAME = "keys";
	public String configName;
	private  boolean devMode=false;
	private Engine engine;
	private List<KeySource> keySourceList = new ArrayList<KeySource>();
	public Map<String, Template> keyTemplateMap;

	private static final Map<String,KeyKit> keyKitMap=new HashMap<>();
	
	public static KeyKit use(String configName) {
		return keyKitMap.get(configName);
	}
	
	public static KeyKit use() {
		return use(MAIN_CONFIG_NAME);
	}
	
	private KeyKit(boolean devMode) {
		this(MAIN_CONFIG_NAME, devMode);
	}

	public static KeyKit load(String configName) {
		
		if(keyKitMap.containsKey(configName)) {
			return keyKitMap.get(configName);
		}
		
		return new KeyKit(configName,DEFAULT_DEV_MODE);
	}
	
	public static KeyKit load(boolean devMode) {
		if(keyKitMap.containsKey(MAIN_CONFIG_NAME)) {
			return keyKitMap.get(MAIN_CONFIG_NAME);
		}
		return new KeyKit(devMode);
	}

	public static KeyKit load(String configName, boolean devMode) {
		if(keyKitMap.containsKey(configName)) {
			return keyKitMap.get(configName);
		}
		return new KeyKit(configName, devMode);
	}

	private KeyKit(String configName, boolean devMode) {
		this.configName = configName;
		this.devMode = devMode;

		if(keyKitMap.containsKey(configName)) {
			throw new ParseException("Key already exists", null );
		}
		
		engine = new Engine(configName);
		engine.setDevMode(devMode);
		engine.addDirective("key", KeyDirective.class);
		engine.addDirective("keys", KeysDirective.class);
		engine.addSharedMethod(new StrKit());
		
		
		keyKitMap.put(configName, this);
	}

	public KeyKit(String configName) {
		this(configName, false);
	}

	public Engine getEngine() {
		return engine;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
		engine.setDevMode(devMode);
	}

	public KeyKit setBaseKeyTemplatePath(String baseKeyTemplatePath) {
		engine.setBaseTemplatePath(baseKeyTemplatePath);
		return this;
	}

	public KeyKit addTemplate(String KeyTemplate) {
		if (StrKit.isBlank(KeyTemplate)) {
			throw new IllegalArgumentException("keyTemplate can not be blank");
		}
		keySourceList.add(new KeySource(KeyTemplate));
		return this;
	}

	public void addTemplate(ISource keyTemplate) {
		if (keyTemplate == null) {
			throw new IllegalArgumentException("keyTemplate can not be null");
		}
		keySourceList.add(new KeySource(keyTemplate));
	}

	public synchronized KeyKit parseKeysTemplate() {
		Map<String, Template> keyTemplateMap = new HashMap<String, Template>(512, 0.5F);
		for (KeySource ss : keySourceList) {
			Template template = ss.isFile() ? engine.getTemplate(ss.file) : engine.getTemplate(ss.source);
			Map<Object, Object> data = new HashMap<Object, Object>();
			data.put(KEY_TEMPLATE_MAP_KEY, keyTemplateMap);
			template.renderToString(data);
		}
		this.keyTemplateMap = keyTemplateMap;
		return this;
	}

	private void reloadModifiedKeyTemplate() {
		engine.removeAllTemplateCache(); // 去除 Engine 中的缓存，以免 get 出来后重新判断 isModified
		parseKeysTemplate();
	}

	private boolean isKeyTemplateModified() {
		for (Template template : keyTemplateMap.values()) {
			if (template.isModified()) {
				return true;
			}
		}
		return false;
	}

	private Template getKeyTemplate(String key) {
		Template template = keyTemplateMap.get(key);
		if (template == null) { // 此 if 分支，处理起初没有定义，但后续不断追加 key 的情况
			if (!devMode) {
				return null;
			}
			if (isKeyTemplateModified()) {
				synchronized (this) {
					if (isKeyTemplateModified()) {
						reloadModifiedKeyTemplate();
						template = keyTemplateMap.get(key);
					}
				}
			}
			return template;
		}

		if (devMode && template.isModified()) {
			synchronized (this) {
				template = keyTemplateMap.get(key);
				if (template.isModified()) {
					reloadModifiedKeyTemplate();
					template = keyTemplateMap.get(key);
				}
			}
		}
		return template;
	}
 
	
	/**
	 * 示例： 1：模板 定义 #key("key")
	 * 
	 * #end
	 *
	 * 2：java 代码 getContent("key", Kv);
	 */
	public String getContent(String key, Kv kv) {
		Template template = getKeyTemplate(key);
		if (template == null) {
			return null;
		}
		return template.renderToString(kv);
	}

	
	public java.util.Set<java.util.Map.Entry<String, Template>> getKeyMapEntrySet() {
		return keyTemplateMap.entrySet();
	}

	public String toString() {
		return "KeyTplKit for config : " + configName;
	}
}

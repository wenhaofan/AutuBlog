package com.autu.common._config;

import com.autu.common.model.entity.Config;
import com.jfinal.kit.PropKit;

public class BlogContext {
	
	public static  EmailConfig emailConfig;
	public static Config config;
	public static String CONFIG_FILE_NAME;
	
	public static String getProjectPath(){
		return PropKit.get("projectPath");
	}

	public static void reset(Config config) {
		emailConfig=new BlogContext().new EmailConfig(config);
		BlogContext.config=config;
	}
	
	public class EmailConfig{
		Config config;
		
		public EmailConfig(Config config) {
			this.config=config;
		}
		
		public String getEmailServer() {
			return config.getEmailServer();
		}
		public String getEmailPassword() {
			return config.getEmailPassword();
		}
		public String getFromEmail() {
			return config.getFromEmail();
		}
	}
	
	public enum CacheNameEnum {
		ARTICLE,
		BLOGROLL,
		CONFIG,
		BLOGGER
	}
	
 
	public static String getTheme() {
		return config.getTheme();
	}
}



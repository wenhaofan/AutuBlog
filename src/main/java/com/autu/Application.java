package com.autu;

import com.autu.common._config.BlogConfig;
import com.jfinal.server.undertow.UndertowServer;

/**
 *  
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class Application {
	public static void main(String[] args) {
		UndertowServer.create(BlogConfig.class).configWeb( builder -> {
			// 配置 Filter
			builder.addFilter("htmlFilter", "com.autu.common.filter.HtmlFilter");
			builder.addFilterUrlMapping("htmlFilter", "/*");
		}).start();
		
		 
	}
}	
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
		UndertowServer.start(BlogConfig.class);
	}
}	

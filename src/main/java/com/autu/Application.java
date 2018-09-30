package com.autu;

import com.jfinal.core.JFinal;

/**
 *  
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class Application {
	public static void main(String[] args) {
		
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
		// JFinal.start("src/main/webapp", 80, "/", 5);
		
		/**
		 * 特别注意：IDEA 或高版本Eclipse 下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		 JFinal.start("src/main/webapp", 80, "/");
		 
	}
}	

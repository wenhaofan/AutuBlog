package com.autu;

import com.jfinal.core.JFinal;

/**
 *  
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class Application {
	public static void main(String[] args) {
		 JFinal.start("src/main/webapp", 80, "/", 5);
	}
}	

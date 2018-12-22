package com.autu.common.email;

 
import com.jfinal.template.source.ISource;

/**
 * 封装 sql 模板源
 */
class KeySource {
	
	String file;
	ISource source;
	
	KeySource(String file) {
		this.file = file;
		this.source = null;
	}
	
	KeySource(ISource source) {
		this.file = null;
		this.source = source;
	}
	
	boolean isFile() {
		return file != null;
	}
}




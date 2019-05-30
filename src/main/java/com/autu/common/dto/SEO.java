package com.autu.common.dto;

/** 
* @author 作者:范文皓
* @createDate 创建时间：2019年4月16日-下午1:25:59 
*/
public class SEO {

	private String title;
	private String keywords;
	private String dscr;
	
	
	public String getDscr() {
		return dscr;
	}
	public void setDscr(String dscr) {
		this.dscr = dscr;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}

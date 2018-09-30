package com.autu._admin.comment;

import java.util.Date;

public class QueryComment {

	private Date gmtStart;
	private Date gmtEnd;
	private String identify;
	private Integer state;
	
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getGmtStart() {
		return gmtStart;
	}
	public void setGmtStart(Date gmtStart) {
		this.gmtStart = gmtStart;
	}
	public Date getGmtEnd() {
		return gmtEnd;
	}
	public void setGmtEnd(Date gmtEnd) {
		this.gmtEnd = gmtEnd;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	
	
	
	
}

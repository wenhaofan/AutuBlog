package com.autu._admin.disk;

public class AsyncResultDTO {
	private Integer existNum;
	
	private Integer notExistNum;
	
	private boolean isOk;
	
	
	public boolean IsOk() {
		return isOk;
	}
	
	public void setFail() {
		this.isOk=false;
	}
	public void setOk() {
		this.isOk=true;
	}
	public Integer getExistNum() {
		return existNum;
	}
	public void setExistNum(Integer existNum) {
		this.existNum = existNum;
	}
	public Integer getNotExistNum() {
		return notExistNum;
	}
	public void setNotExistNum(Integer notExistNum) {
		this.notExistNum = notExistNum;
	}
}

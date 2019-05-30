package com.autu._admin.disk;

import java.util.List;

import com.autu.common.model.Disk;

public class CompareDiskResult {
	private List<Disk> existList;
	private List<Disk> notExistList;
	public List<Disk> getExistList() {
		return existList;
	}
	public void setExistList(List<Disk> existList) {
		this.existList = existList;
	}
	public List<Disk> getNotExistList() {
		return notExistList;
	}
	public void setNotExistList(List<Disk> notExistList) {
		this.notExistList = notExistList;
	}
}

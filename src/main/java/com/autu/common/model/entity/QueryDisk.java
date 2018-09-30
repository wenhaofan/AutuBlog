package com.autu.common.model.entity;

public class QueryDisk {

	private String order;
	
	private String orderType;
	
	private String parentId;
	
	private boolean isSizeOrder;
	
	private Integer state;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isSizeOrder() {
		return isSizeOrder;
	}

	public void setSizeOrder(boolean isSizeOrder) {
		this.isSizeOrder = isSizeOrder;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
}

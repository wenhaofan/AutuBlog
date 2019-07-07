package com.autu._admin.common.annotation;

public enum OTypeEnum {
	//增
	ADD(0),
	//删
	DELETE(1),
	//改
	UPDATE(2);
	
	private int value;
	
	OTypeEnum(int val) {
		value=val;
	}
	
	public int getValue() {
		return value;
	}
	
}

package com.autu.common.log;

public enum SysLogLevelEnum {

	INFO(0),
	WARN(1),
	ERROR(2);
	
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private SysLogLevelEnum(Integer value) {
		this.value = value;
	}
	
	
}

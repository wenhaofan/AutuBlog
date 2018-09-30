package com.autu.common.log;

public enum SysLogActionEnum {
	/**
	 * 删除
	 */
	DELETE("delete"),
	/**
	 * 新增
	 */
	SAVE("save"),
	/**
	 * 更新
	 */
	UPDATE("update"),
	SAVE_OR_UPDATE("saveOrUpdate"),
	/**
	 * 上传
	 */
	UPLOAD("upload"),
	/**
	 * 其他类型的日志
	 */
	OTHER("other");
	
	private String name;

	
	
	private SysLogActionEnum(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	 
	
	
}

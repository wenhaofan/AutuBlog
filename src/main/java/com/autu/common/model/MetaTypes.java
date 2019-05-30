package com.autu.common.model;

public enum MetaTypes {

	CATEGORY,
	TAG;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}

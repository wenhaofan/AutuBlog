package com.autu.common.kit;

public class StrKit extends com.jfinal.kit.StrKit{

	public static String filterNull(String str) {
		return str.replaceAll(" ", "");
	}
}

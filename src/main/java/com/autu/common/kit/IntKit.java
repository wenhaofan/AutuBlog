package com.autu.common.kit;

public class IntKit {

	public static Integer parseInt(Object source) {
		if(source instanceof String){
			return Integer.parseInt(source.toString());
		}
		
		return null;
	}
	
}

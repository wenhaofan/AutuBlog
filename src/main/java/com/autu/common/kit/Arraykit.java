package com.autu.common.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arraykit {

	public static boolean isBlank(Object[] objs) {
		return objs==null||objs.length==0;
	}

	
	public static <T>List<T> remove(T[] arr,Object target) {
 
		List<T> list=new ArrayList<T>();
		
		list.addAll(Arrays.asList(arr));
		
		while(list.remove(target)) {
			
		}
		while(list.remove(null)) {
			
		}
		return list;
	}
	
	public static Integer[] splitToIntArr(String source,String regex) {
		
		if(StrKit.isBlank(source)) {
			return null;
		}
		
		String[] strArr=source.split(regex);
		
		Integer[] intArr=null;
 
		
		intArr=new Integer[strArr.length];
		
		String str=null;
		Integer intVal=null;
		for(int i=0,size=strArr.length;i<size;i++) {
			str=strArr[i];
			if(StrKit.notBlank(str)) {
				intVal=IntKit.parseInt(str);
				intArr[i]=intVal;
			}
		}
		return intArr;
	}
}

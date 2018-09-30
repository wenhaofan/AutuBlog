package com.autu.common.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListKit {

	public static boolean isBlank(List<? extends Object> list) {
		return list == null ? true : list.isEmpty();
	}

	public static boolean notBlank(List<? extends Object> list) {
		return !isBlank(list);
	}

	/**
	 * Set转换List
	 * 
	 * @param set集合
	 * @return list集合
	 */
	public static <T> List<T> ListCourseChapterSet(Set<T> set) {
		if (set != null && set.size() != 0) {
			return new ArrayList<T>(set);
		}
		return null;
	}
}

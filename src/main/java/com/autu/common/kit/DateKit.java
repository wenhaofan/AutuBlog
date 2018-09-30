package com.autu.common.kit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateKit {

	/**
	 * 根据格式格式化时间
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date,String pattern) {
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
}

package com.autu.common.kit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class AgentUserKit {
	private static String pattern = "^Mozilla/\\d\\.\\d\\s+\\(+.+?\\)";
	private static String pattern2 = "\\(+.+?\\)";
	private static Pattern r = Pattern.compile(pattern);
	private static Pattern r2 = Pattern.compile(pattern2);

	public static String getDeviceInfo(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		return getDeviceInfo(userAgent);
	}

	private static String getDeviceInfo(String userAgent) {
		Matcher m = r.matcher(userAgent);
		String result = null;
		if (m.find()) {
			result = m.group(0);
		}
		if(result==null) {
			return null;
		}
		Matcher m2 = r2.matcher(result);
		if (m2.find()) {
			result = m2.group(0);
		}
		result = result.replace("(", "");
		result = result.replace(")", "");
		return filterDeviceInfo(result);
	}

	public static String filterDeviceInfo(String result) {
		if (StrKit.isBlank(result)) {
			return null;
		}
		result = result.replace(" U;", "");
		result = result.replace(" zh-cn;", "");
		return result;
	}

	public static void main(String[] args) {
		System.out.println(getDeviceInfo(
				"Mozilla/5.0 (Linux; U; Android 7.0; zh-cn; MI 5s Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.128 Mobile Safari/537.36 XiaoMi/MiuiBrowser/10.2.2"));
	}
}

package com.jrj.jd.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/**
 * @description cookie工具类
 * @author qiushun.sun
 * @date 2018.03.15
 *
 */
public class CookiesUtil {
	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}

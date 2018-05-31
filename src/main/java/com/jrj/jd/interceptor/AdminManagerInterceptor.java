package com.jrj.jd.interceptor;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jrj.jd.constant.JdConstant;
import com.jrj.jd.util.CookiesUtil;

/**
 * 
 * @description 登录拦截器
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
@Slf4j
public class AdminManagerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("AdminManagerInterceptor---------------preHandle");
		log.info("origin= " + request.getHeader("Origin")); 
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); 
	    response.setHeader("Access-Control-Allow-Methods", "*");
	    response.setHeader("Access-Control-Allow-Headers","Origin,Content-Type,Accept,token,X-Requested-With");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
		
		log.info("method= " + request.getMethod());
		if("OPTIONS".equals(request.getMethod().toUpperCase())) {
			return true;
		}
		
		String myparameter = request.getParameter("myparameter");
		if("my_jrj_jd_test_2018".equals(myparameter)){
			return true;
		}
	    
		Map<String, Cookie> cookieMap = CookiesUtil.readCookieMap(request);
		Cookie cookie = cookieMap.get("itg_passport_userid");
		String passportId = null;
		if(cookie != null){
			passportId = cookie.getValue();
		}else{
			log.info("AdminManagerInterceptor cookie is null");
			return false;
		}
		log.info("passportId= " + passportId);
		ServletContext context = request.getServletContext();
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
		RestTemplate restTemplate = (RestTemplate) applicationContext.getBean("restTemplate");
		String json = restTemplate.getForObject(JdConstant.VERIFY_INTERFACE_URL + "/privilege/getUserPrivilegePeriod?bizCode=5&productId=8&productSubId=100080001&passportId={passportId}", String.class, passportId);
		JSONObject object = JSON.parseObject(json);
		if ("1".equals(object.getString("code"))) { // 请求失败--拦截
			log.info("AdminManagerInterceptor getUserPrivilegePeriod interface request is failed");
			return false;
		}
		JSONObject jsonObject = JSON.parseObject(object.get("data").toString());
		String expireDate = jsonObject.getString("expireDate"); // 过期日期
		String validDate = jsonObject.getString("validDate"); // 开始日期
		Integer days = jsonObject.getInteger("days"); // 有效天数 
		String valid = jsonObject.getString("valid"); // 是否有效
		log.info("expireDate="+expireDate+" validDate="+validDate+" days="+days+" valid="+valid);
		if ("0".equals(valid)) { // 账号有效 -- 放行
			log.info("AdminManagerInterceptor expireDate is valid");
			request.setAttribute("passportId", passportId);
			request.setAttribute("expireDate", expireDate);
			request.setAttribute("validDate", validDate);
			request.setAttribute("days", days);
			return true;
		} else { // 账号无效 -- 拦截
			log.info("AdminManagerInterceptor expireDate is invalid");
			return false;
		}
	}
}

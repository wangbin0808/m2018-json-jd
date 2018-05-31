package com.jrj.jd.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jrj.jd.interceptor.AdminManagerInterceptor;


/**
 * 
 * @description 注册拦截器
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
@Slf4j
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("----------------MyWebMvcConfigurerAdapter 拦截器---------------------");
		InterceptorRegistration registration = registry.addInterceptor(new AdminManagerInterceptor());
		registration.addPathPatterns("/**");
		registration.excludePathPatterns("/json/jd/astock/headline/userStatusOfPay", "/json/jd/astock/headline/userPermission");
		// registration.excludePathPatterns("/json/jd/astock/headline/userStatusOfPay", "/json/jd/astock/headline/userPermission", "/json/jd/astock/headline/getAStockHeadlines", "/json/jd/astock/headline/getHeadlineContent"); 
		
		// 配置多个拦截器类
		/*InterceptorRegistration registration1 = registry.addInterceptor(new AdminManagerInterceptor1());
		registration1.addPathPatterns("/**");
		registration1.excludePathPatterns("/json/jd/astock/headline/userStatusOfPay"); */
	}
}

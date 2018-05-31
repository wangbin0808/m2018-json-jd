package com.jrj.jd.config;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

/**
 * @description 数据库查询分页配置类
 * @author qiushun.sun
 * @Date 2018.03.15
 *
 */
@Slf4j
@Configuration
public class PageHelperConfiguration {

	@Bean
	public PageHelper pageHelper() {
		log.info("PageHelperConfiguration.pageHelper()");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}
}

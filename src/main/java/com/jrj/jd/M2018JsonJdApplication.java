package com.jrj.jd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @description A股票头条精华版 -- 启动类
 * @author qiushun.sun
 * @date 2018.03.13
 *
 */
@MapperScan("com.jrj.jd.dao")
@SpringBootApplication
public class M2018JsonJdApplication { 

	// 设置为允许ajax全局跨域请求
	@Bean
	public WebMvcConfigurer corsConfigurer() { 
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(M2018JsonJdApplication.class, args);
	}
}

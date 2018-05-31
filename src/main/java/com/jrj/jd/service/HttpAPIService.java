package com.jrj.jd.service;

import java.util.Map;
import com.jrj.jd.bean.HttpResult;

/**
 * @description httpClient 服务类
 * @author qiushun.sun
 * @date 2018.03.17
 *
 */
public interface HttpAPIService {
	
	/**
	 * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception;

	/**
	 * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url, Map<String, Object> map) throws Exception;

	/**
	 * 带参数的post请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url, Map<String, Object> map) throws Exception;

	/**
	 * 不带参数post请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url) throws Exception;

}

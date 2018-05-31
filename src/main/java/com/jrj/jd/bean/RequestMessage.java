package com.jrj.jd.bean;

import lombok.Data;

/**
 * @description 接口调用返回信息封装类
 * @author qiushun.sun
 * @date 2018.03.20
 *
 */
@Data
public class RequestMessage {
	
	private String msg; // 接口调用信息
	private String hasPermission; // 账号是否有权限  0：有  1：无
	private String retCode; // 访问成功的代码  0：成功  1：失败
	private String isFreeUsered; // 是否已经免费体验过 0：未体验	 1：已体验
	
}

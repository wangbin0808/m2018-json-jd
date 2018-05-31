package com.jrj.jd.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.jrj.jd.bean.PageMessage;

/**
 * 
 * @description 数据和分页信息类
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
@Data
public class PageMessageAndData<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String days; // 有效天数
	private String validDate; // 开始日期
	private String expireDate;// 结束日期
	private List<T> data; // 每一条记录
	private PageMessage pageMessage; // 分页信息

}

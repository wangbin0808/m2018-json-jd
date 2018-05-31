package com.jrj.jd.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @description 分页信息类
 * @author qiushun.sun
 * @Date 2018.03.14
 *
 */
@Data
public class PageMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private int startRow; // 开始行
	private int endRow; // 结束行
	private int pageNum; // 当前页
	private int pages; // 总的页数
	private int pageSize; // 每页大小
	private long total; // 数据总量

}

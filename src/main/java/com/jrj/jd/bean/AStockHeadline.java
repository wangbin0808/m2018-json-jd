package com.jrj.jd.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @description A股票头条精华版类
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
@Data
public class AStockHeadline implements Serializable {

	private static final long serialVersionUID = 1L;
	private String iiid = ""; // 主键
	private String title = ""; // 标题
	private String makeDate = ""; // 发布日期
	private String paperMediaSource; // 媒体来源ID
	private String infoUrl = ""; // 生成专题的URL
	private String imgUrl = ""; // 图片网址

}

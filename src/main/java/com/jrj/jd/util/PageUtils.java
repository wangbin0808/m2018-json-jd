package com.jrj.jd.util;

import com.github.pagehelper.Page;
import com.jrj.jd.bean.PageMessage;
import com.jrj.jd.vo.PageMessageAndData;

/**
 * @description 分页信息、过滤信息和数据封装工具类
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
public class PageUtils {
	/**
	 * 
	 * @param expireDate
	 * @param validDate
	 * @param days
	 * @param page
	 * @return
	 */
	public static <T> PageMessageAndData<T> setPageMessageAndData(String expireDate, String validDate, String days, Page<T> page) {
		PageMessageAndData<T> dataAndSelMess = new PageMessageAndData<T>();
		// 设置对象信息
		dataAndSelMess.setData(page);
		// 设置有效字段
		dataAndSelMess.setExpireDate(expireDate);
		dataAndSelMess.setValidDate(validDate);
		dataAndSelMess.setDays(days);
		// 设置分页查询信息
		PageMessage pageMessage = new PageMessage();
		pageMessage.setEndRow(page.getEndRow());
		pageMessage.setPageNum(page.getPageNum());
		pageMessage.setPages(page.getPages());
		pageMessage.setPageSize(page.getPageSize());
		pageMessage.setStartRow(page.getStartRow());
		pageMessage.setTotal(page.getTotal());
		dataAndSelMess.setPageMessage(pageMessage);
		return dataAndSelMess;
	}

}
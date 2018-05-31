package com.jrj.jd.service;

import com.jrj.jd.bean.AStockHeadline;
import com.jrj.jd.vo.PageMessageAndData;

/**
 * @description A股票头条精华版 -- service接口
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
public interface AStockHeadlineService {
	/**
	 * 获取headline列表
	 * 
	 * @return
	 */
	PageMessageAndData<AStockHeadline> getHeadlines(Integer curPage, String expireDate, String days, String validDate);

	/**
	 * 获取头条的具体内容 
	 * 
	 * @return
	 */
	String getHeadlineContent(String iiid);

	/**
	 * 用户的体验或者购买状态
	 * @param status
	 * @return
	 */
	String getUserStatusOfPay(String status, String uid);

	/**
	 * 用户权限判断
	 * @param passportId 
	 * 
	 */
	String userPermissionService(String passportId);

}

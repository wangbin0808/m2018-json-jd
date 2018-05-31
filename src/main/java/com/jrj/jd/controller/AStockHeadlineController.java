package com.jrj.jd.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jrj.jd.bean.AStockHeadline;
import com.jrj.jd.bean.RequestMessage;
import com.jrj.jd.service.AStockHeadlineService;
import com.jrj.jd.util.CookiesUtil;
import com.jrj.jd.util.MyJsonConverter;
import com.jrj.jd.vo.PageMessageAndData;

/**
 * @description A股票头条精华版
 * @author qiushun.sun
 * @date 2018.03.13
 *
 */
@Slf4j
@RestController
@RequestMapping("/json/jd/astock/headline")
public class AStockHeadlineController {

	@Autowired
	private AStockHeadlineService aStockHeadlineService;

	/**
	 * 获取A股头条精华的列表数据
	 * 
	 * @param curPage
	 *            当前页数
	 * @param validDate
	 *            开始日期
	 * @param expireDate
	 *            结束日期
	 * @param passportId
	 *            用户ID
	 * @return
	 */
	@RequestMapping("/getAStockHeadlines")
	public String getAStockHeadlines(HttpServletRequest req, @RequestParam(value = "curPage", required = true) Integer curPage) {
		// 正式数据
		String days = req.getAttribute("days").toString();
		String validDate = req.getAttribute("validDate").toString();
		String expireDate = req.getAttribute("expireDate").toString();
		// String passportId = req.getAttribute("passportId").toString();

		// 测试数据
		/*String days = "30";
		String validDate = "2018-01-23";
		String expireDate = "2018-03-30";*/

		log.info("getAStockHeadlines curPage=" + curPage + "days=" + days + "validDate=" + validDate + "expireDate=" + expireDate);
		PageMessageAndData<AStockHeadline> messageAndData = aStockHeadlineService.getHeadlines(curPage, expireDate, days, validDate);
		return MyJsonConverter.objectToString(messageAndData);
	}

	/**
	 * 获取A股头头条的详细内容 iiid 数据id号
	 * 
	 * @return
	 */
	@RequestMapping(value="/getHeadlineContent", produces={"application/json;charset=UTF-8"})
	public String getHeadlineContent(@RequestParam(value = "iiid", required = true) String iiid) {
		log.info("getHeadlineContent iiid= " + iiid);
		return aStockHeadlineService.getHeadlineContent(iiid);
	}
	
	/**
	 * 获取用户当前的体验状态 
	 * status 用户的付费状态：0免费体验；非0已购买
	 * 
	 * @return
	 */
	@RequestMapping("/userStatusOfPay")
	public String userStatusOfPay(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "status", required = true) String status) {
		Map<String, Cookie> cookieMap = CookiesUtil.readCookieMap(request);
		Cookie cookie = cookieMap.get("itg_passport_userid");
		RequestMessage message = new RequestMessage();
		String uid = null;
		if (cookie != null) {
			uid = cookie.getValue();
		} else {
			log.info("userStatusOfPay cookie is null");
			message.setMsg("cookie is null");
			message.setRetCode("1");
			return MyJsonConverter.objectToString(message);
		}
		if (StringUtils.isEmpty(uid)) {
			log.info("userStatusOfPay passportId is null");
			message.setMsg("passportId is null");
			message.setRetCode("1");
			return MyJsonConverter.objectToString(message);
		}
		// String uid = "161209010071197990"; // 测试数据
		log.info("uid= " + uid + " status= " + status);
		return aStockHeadlineService.getUserStatusOfPay(status, uid);
	}

	/**
	 * 用户权限判断
	 * 
	 * @return
	 */
	@RequestMapping("/userPermission")
	public String userPermission(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = CookiesUtil.readCookieMap(request);
		Cookie cookie = cookieMap.get("itg_passport_userid");
		RequestMessage message = new RequestMessage();
		String passportId = null;
		if (cookie != null) {
			passportId = cookie.getValue();
		} else {
			log.info("userPermission cookie is null");
			message.setMsg("userPermission cookie is null");
			message.setRetCode("1");
			return MyJsonConverter.objectToString(message);
		}
		if (StringUtils.isEmpty(passportId)) {
			log.info("userPermission passportId is null");
			message.setMsg("userPermission passportId is null");
			message.setRetCode("1");
			return MyJsonConverter.objectToString(message);
		}
		// String passportId = "161209010071197990"; // 测试数据
		return aStockHeadlineService.userPermissionService(passportId);
	}

}
package com.jrj.jd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jrj.jd.bean.AStockHeadline;
import com.jrj.jd.bean.OrderList;
import com.jrj.jd.bean.RequestMessage;
import com.jrj.jd.bean.root;
import com.jrj.jd.constant.JdConstant;
import com.jrj.jd.dao.AStockHeadlineDao;
import com.jrj.jd.service.AStockHeadlineService;
import com.jrj.jd.service.HttpAPIService;
import com.jrj.jd.util.MyJsonConverter;
import com.jrj.jd.util.PageUtils;
import com.jrj.jd.util.RedisCache;
import com.jrj.jd.util.Xml2ObjUtil;
import com.jrj.jd.vo.OrderListMessVo;
import com.jrj.jd.vo.PageMessageAndData;

/**
 * @description A股票头条精华版 -- service实现类
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
@Slf4j
@Service
public class AStockHeadlineServiceImpl implements AStockHeadlineService {

	@Autowired
	private RedisCache redisCache;
	@Autowired
	private AStockHeadlineDao aStockHeadlineDao;
	@Resource
	private HttpAPIService httpAPIService;
	@Autowired
	private RestTemplate restTemplate;

	private static final String key = "jrj:jd:com.jrj.jd.service.impl.AStockHeadlineServiceImpl";

	@SuppressWarnings("unchecked")
	@Override
	public PageMessageAndData<AStockHeadline> getHeadlines(Integer curPage, String expireDate, String days, String validDate) {
		String keys = key + "getHeadlines" + curPage + expireDate;
		// redisCache.deleteObj(keys);// 上线时关闭
		PageMessageAndData<AStockHeadline> messageAndData = null;
		if (redisCache.hasKey(keys)) {
			log.info("getHeadlines redisCache.hasKey(keys)");
			messageAndData = redisCache.getObj(keys, PageMessageAndData.class);
		} else {
			PageHelper.startPage(curPage, JdConstant.PAGE_SIZE);
			Page<AStockHeadline> headlines = aStockHeadlineDao.getHeadlinesDao(expireDate + " 23:59:59");
			messageAndData = PageUtils.setPageMessageAndData(expireDate, validDate, days, headlines);
			redisCache.setObj(keys, messageAndData, 1*60*60, PageMessageAndData.class);
		}
		log.info("curPage:" + curPage + " expireDate:" + expireDate + " messageAndData:" + messageAndData);
		return messageAndData;
	}

	@Override
	public String getHeadlineContent(String iiid) {
		String xml = null;
		root roots = null;
		String keys = key + "getHeadlineContent" + iiid;
		// redisCache.deleteObj(keys);// 上线时关闭
		if (redisCache.hasKey(keys)) {
			log.info("getHeadlineContent redisCache.hasKey(keys)");
			roots = redisCache.getObj(keys, root.class);
		}else{
			try {
				// 使用httpClient请求
				xml = httpAPIService.doGet(JdConstant.CMS_DATA_URL + "?iiid="+iiid);
				roots = (root) Xml2ObjUtil.convertXmlStrToObject(root.class, xml);
				redisCache.setObj(keys, roots, 1*60*60, root.class);
				log.info("httpAPIService roots= " + roots); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return MyJsonConverter.objectToString(roots);
	}
	
	@Override
	public String getUserStatusOfPay(String status, String uid) {
		String json = null;
		RequestMessage message = new RequestMessage();
		try{
			json = restTemplate.getForObject(JdConstant.BUY_LIST_URL + "?productSubId=100080001&uid={uid}", String.class, uid);
		}catch(Exception e){
			log.info("订单接口调用失败！");
			message.setMsg("订单接口调用失败！");
			message.setRetCode("1"); // 
			return MyJsonConverter.objectToString(message);
		}
		OrderListMessVo orderListMesses = MyJsonConverter.stringToObject(json, OrderListMessVo.class);
		List<OrderList> orderList = orderListMesses.getOrderList();
		message.setIsFreeUsered("0");
		message.setRetCode("0");
		for (OrderList order : orderList) {
			if(order.getTotalAmount() == 0){
				message.setIsFreeUsered("1");
				break;
			}
		}
		if("0".equals(status)){ // 校验是否体验过
			log.info("校验是否体验过 " + message.getIsFreeUsered());
			message.setMsg("成功！");
		}else{ // 申请体验权限
			if("1".equals(message.getIsFreeUsered())){
				log.info("已经体验过，不可以再次体验！");
				message.setMsg("已经体验过，不可以再次体验！");
				return MyJsonConverter.objectToString(message);
			}
			String addZeroJson = null; 
			try{
				Long clientOrderId = System.currentTimeMillis();
				log.info("System.currentTimeMillis()" + clientOrderId);
				log.info("url= " + JdConstant.ADD_ZERO_URL + "bizCode=5&productId=8&productSubId=100080001&type=6&source=jdapp&reason=免费体验赠送&buyerId="+uid+"&clientOrderId="+clientOrderId);
				addZeroJson = restTemplate.getForObject(JdConstant.ADD_ZERO_URL + "?bizCode=5&productId=8&productSubId=100080001&type=6&source=jdapp&reason=免费体验赠送&buyerId="+uid+"&clientOrderId="+clientOrderId, String.class);
			}catch(Exception e){
				log.info("Exception 调用新增订单接口调用失败！" + e);
				message.setMsg("Exception 调用新增订单接口调用失败！"); 
				message.setRetCode("1");
				return MyJsonConverter.objectToString(message);
			}
			log.info("addZeroJson= " + addZeroJson);
			JSONObject obj = JSON.parseObject(addZeroJson);
			message.setMsg(obj.getString("msg"));
			message.setRetCode(obj.getString("retCode"));
		}
		return MyJsonConverter.objectToString(message);
	}

	@Override
	public String userPermissionService(String passportId) {
		String json = null;
		RequestMessage resultMes = new RequestMessage();
		// 调用接口获取用户权限信息
		try{
			json = restTemplate.getForObject(JdConstant.VERIFY_INTERFACE_URL + "/privilege/getUserPrivilegePeriod?bizCode=5&productId=8&productSubId=100080001&passportId={passportId}", String.class, passportId);
		}catch(Exception e){
			log.info("userPermissionService getUserPrivilegePeriod interface request is failed");
			resultMes.setRetCode("1");
			resultMes.setMsg("调用权限接口失败，请检查您的网络！");
			return MyJsonConverter.objectToString(resultMes);
		}
		
		JSONObject object = JSON.parseObject(json);
		if ("1".equals(object.getString("code"))) { // 请求失败
			log.info("userPermissionService getUserPrivilegePeriod interface request is failed");
			resultMes.setRetCode("1"); // 请求失败
			resultMes.setMsg("请输入用效的passportId！");
			return MyJsonConverter.objectToString(resultMes);
		}
		
		// 获取用户权限信息
		resultMes = getUserPerssion(object, passportId, resultMes);
		return MyJsonConverter.objectToString(resultMes);
	}

	/**
	 * 获取用的权限信息
	 * @param object
	 * @param resultMes
	 * @return
	 */
	private RequestMessage getUserPerssion(JSONObject object, String passportId, RequestMessage resultMes) {
		JSONObject jsonObject = JSON.parseObject(object.get("data").toString());
		String valid = jsonObject.getString("valid"); // 权限标记
		if ("0".equals(valid)) { // 账号有权限
			log.info("getUserPerssion account is valid");
			resultMes.setRetCode("0"); // 请求成功
			resultMes.setHasPermission("0"); // 账号有权限
			resultMes.setMsg("账号有权限");
			resultMes.setIsFreeUsered(""); // 用户体验设置为空
		} else { // 账号无权限
			log.info("getUserPerssion account is invalid");
			resultMes.setRetCode("0"); // 请求成功
			resultMes.setHasPermission("1"); // 账号无权限
			String json = null;
			try{
				json = restTemplate.getForObject(JdConstant.BUY_LIST_URL + "?productSubId=100080001&uid={passportId}", String.class, passportId);
			}catch(Exception e){
				log.info("订单接口调用失败！");
				resultMes.setMsg("订单接口调用失败！");
				resultMes.setRetCode("1"); // 
				return resultMes;
			}
			OrderListMessVo orderListMesses = MyJsonConverter.stringToObject(json, OrderListMessVo.class);
			List<OrderList> orderList = orderListMesses.getOrderList();
			if(orderList!= null && orderList.size()>0){
				resultMes.setMsg("账号无权限，且该账号已经免费体验过！");
				resultMes.setIsFreeUsered("1"); // 用户已体验过
			}else{
				resultMes.setMsg("账号无权限，但账号未免费体验过！");
				resultMes.setIsFreeUsered("0"); // 用户未体验过
			}
		}
		return resultMes;
	}

}

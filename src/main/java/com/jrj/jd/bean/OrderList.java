package com.jrj.jd.bean;

import lombok.Data;

/**
 * @description 订单信息类
 * @author qiushun.sun
 * @date 2018.03.20
 *
 */
@Data
public class OrderList {
	
	private String prepaySign; // 订单校验
	private String contractFid; // 合同文件id
	private String payOrderNo; // 支付/退款唯一号
	private String bizCode; // 业务线ID
	private String contractNo;
	private String completeTime; // 支付完成时间
	private String buyerId; // 购买者ID对应passportId
	private int totalAmount; // 支付或退款金额
	private String orderTime; // 下单时间
	private int price; // 价格
	private String productSubId; // 收费项目ID
	private String sellerOrderId; // 商家订单唯一号
	private String productType; // 收费项目类型
	private int nums; // 购买个数
	private String status; // 支付状态
	private String productSubName; // 收费项目名称

}

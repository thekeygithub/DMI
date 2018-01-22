package com.pay.cloud.pay.escrow.alipay.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.pay.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.pay.cloud.pay.escrow.alipay.exception.ParamLengthException;
import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

/**
 * 移动支付请求
 * @author yanjie.ji
 * @date 2016年8月15日 
 * @time 下午4:34:06
 */
public class AlipayAppPayRequest extends AlipayPublicRequest {

	/**
	 * 对一笔交易的具体描述信息  
	 * 非必填
	 */
	private String body;
	/**
	 * 商品的标题/交易标题/订单标题 
	 * 必填
	 */
	private String subject;
	/**
	 * 商户网站唯一订单号  
	 * 必填
	 */
	private String out_trade_no;
	/**
	 * 该笔订单允许的最晚付款时间，逾期将关闭交易 
	 * 非必填
	 */
	private String timeout_express;
	/**
	 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]  
	 * 必填
	 */
	private String total_amount;
	/**
	 * 收款支付宝用户ID
	 * 非必填
	 */
	private String seller_id;
	/**
	 * 销售产品码，商家和支付宝签约的产品码 
	 * 必填
	 */
	private String product_code;
	
	@Override
	protected boolean checkSelf() {
		List<String> emptyList = new ArrayList<String>();
		if(StringUtils.isEmpty(this.getSubject())) emptyList.add("subject");
		if(StringUtils.isEmpty(this.getOut_trade_no())) emptyList.add("out_trade_no");
		if(StringUtils.isEmpty(this.getTotal_amount())) emptyList.add("total_amount");
		if(StringUtils.isEmpty(this.getProduct_code())) emptyList.add("product_code");
		if(!emptyList.isEmpty()) 
			throw new ParamEmptyException(emptyList);
		
		List<String> lengthList = new ArrayList<String>();
		if(StringUtils.isNotEmpty(this.getBody())&&this.getBody().length()>128) lengthList.add("body");
		if(this.getSubject().length()>256) lengthList.add("subject");
		if(this.getOut_trade_no().length()>64) lengthList.add("out_trade_no");
		if(StringUtils.isNotEmpty(this.getTimeout_express())&&this.getTimeout_express().length()>6) lengthList.add("timeout_express");
		if(this.getTotal_amount().length()>9) lengthList.add("total_amount");
		if(StringUtils.isNotEmpty(this.getSeller_id())&&this.getSeller_id().length()>16) lengthList.add("seller_id");
		if(this.getProduct_code().length()>64) lengthList.add("product_code");
		if(!lengthList.isEmpty()) 
			throw new ParamLengthException(emptyList);
		
		List<String> paramList = new ArrayList<String>();
		if(!NumberUtils.isNumber(this.getTotal_amount()))
			paramList.add("total_amount");
		if(!paramList.isEmpty()) 
			throw new UnmatchedParamException(paramList);
		
		double amount = Double.parseDouble(this.getTotal_amount());
		if(amount<0.01||amount>100000000) paramList.add("total_amount");
		if(!paramList.isEmpty()) 
			throw new UnmatchedParamException(paramList);
		
		return true;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getTimeout_express() {
		return timeout_express;
	}
	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
}

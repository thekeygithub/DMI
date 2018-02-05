package com.models.cloud.pay.escrow.alipay.param;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import com.models.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

/**
 * 阿里移动支付接口参数
 * @author yanjie.ji
 * @date 2016年8月17日 
 * @time 下午12:02:09
 */
public class AlipayMobileParam implements AlipayParam{

	/**
	 * 服务器异步通知页面路径
	 * 必填
	 */
	private String notify_url;
	/**
	 * 客户端号
	 */
	private String app_id;
	/**
	 * 客户端来源
	 */
	private String appenv;
	/**
	 * 商户网站唯一订单号
	 * 必填
	 */
	private String out_trade_no;
	/**
	 * 商品名称
	 * 必填
	 */
	private String subject;
	/**
	 * 支付类型
	 * 必填
	 */
	private String payment_type;
	/**
	 * 账户id
	 */
	private String seller_id;
	/**
	 * 总金额
	 * 必填
	 */
	private double total_fee;
	/**
	 * 商品详情
	 * 必填
	 */
	private String body;
	/**
	 * 商品类型
	 */
	private String goods_type;
	/**
	 * 花呗分期参数
	 */
	private String hb_fq_param;
	/**
	 * 是否发起实名校验
	 */
	private String rn_check;
	/**
	 * 未付款交易的超时时间
	 */
	private String it_b_pay;
	/**
	 * 授权令牌
	 */
	private String extern_token;
	/**
	 * 商户优惠活动参数
	 */
	private String promo_params;
	
	@Override
	public boolean checkSelf() throws UnmatchedParamException {
		List<String> emptyList = new ArrayList<String>();
		//判空
		if(StringUtils.isEmpty(this.getNotify_url()))emptyList.add("notify_url");
		if(StringUtils.isEmpty(this.getOut_trade_no()))emptyList.add("out_trade_no");
		if(StringUtils.isEmpty(this.getSubject()))emptyList.add("subject");
		if(StringUtils.isEmpty(this.getPayment_type()))emptyList.add("payment_type");
		if(this.getTotal_fee()==0)emptyList.add("total_fee");
		if(StringUtils.isEmpty(this.getBody()))emptyList.add("body");
		if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
		
		List<String> paramList = new ArrayList<String>();
		if(this.getTotal_fee()<0.01||this.getTotal_fee()>100000000) paramList.add("total_fee");
		if(!paramList.isEmpty()) throw new UnmatchedParamException(paramList);
		
		return true;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getAppenv() {
		return appenv;
	}

	public void setAppenv(String appenv) {
		this.appenv = appenv;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public double getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(double total_fee) {
		this.total_fee = total_fee;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getHb_fq_param() {
		return hb_fq_param;
	}

	public void setHb_fq_param(String hb_fq_param) {
		this.hb_fq_param = hb_fq_param;
	}

	public String getRn_check() {
		return rn_check;
	}

	public void setRn_check(String rn_check) {
		this.rn_check = rn_check;
	}

	public String getIt_b_pay() {
		return it_b_pay;
	}

	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}

	public String getExtern_token() {
		return extern_token;
	}

	public void setExtern_token(String extern_token) {
		this.extern_token = extern_token;
	}

	public String getPromo_params() {
		return promo_params;
	}

	public void setPromo_params(String promo_params) {
		this.promo_params = promo_params;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
}

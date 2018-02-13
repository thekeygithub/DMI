package com.models.cloud.pay.escrow.alipay.response;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 移动支付回调结果
 * @author yanjie.ji
 * @date 2016年8月17日 
 * @time 下午2:45:20
 */
public class AlipayMoblieResponse extends AlipayNotifyResponse{
	/**
	 * 商户网站唯一订单号
	 */
	private String out_trade_no;
	/**
	 * 商品名称
	 */
	private String subject;
	/**
	 * 支付类型
	 */
	private String payment_type;
	/**
	 * 支付宝交易号
	 * 必填
	 */
	private String trade_no;
	/**
	 * 交易状态
	 * 必填
	 */
	private String trade_status;
	/**
	 * 卖家支付宝用户号
	 * 必填
	 */
	private String seller_id;
	/**
	 * 卖家支付宝账号
	 * 必填
	 */
	private String seller_email;
	/**
	 * 买家支付宝用户号
	 * 必填
	 */
	private String buyer_id;
	/**
	 * 买家支付宝账号
	 * 必填
	 */
	private String buyer_email;
	/**
	 * 交易金额
	 * 必填
	 */
	private double total_fee;
	/**
	 * 购买数量
	 */
	private int quantity;
	/**
	 * 商品单价
	 */
	private double price;
	/**
	 * 商品描述
	 */
	private String body;
	/**
	 * 交易创建时间
	 */
	private String gmt_create;
	/**
	 * 交易付款时间
	 */
	private String gmt_payment;
	/**
	 * 是否调整总价
	 */
	private String is_total_fee_adjust;
	/**
	 * 是否使用红包买家
	 */
	private String use_coupon;
	/**
	 * 折扣
	 */
	private String discount;
	/**
	 * 退款状态
	 */
	private String refund_status;
	/**
	 * 退款时间
	 */
	private String gmt_refund;

	@Override
	public boolean checkself() throws UnmatchedParamException {
		//判空
		List<String> emptyList = new ArrayList<String>();
		if(StringUtils.isEmpty(this.getNotify_time()))emptyList.add("notify_time");
		if(StringUtils.isEmpty(this.getNotify_type()))emptyList.add("notify_type");
		if(StringUtils.isEmpty(this.getNotify_id()))emptyList.add("notify_id");
		if(StringUtils.isEmpty(this.getSign_type()))emptyList.add("sign_type");
		if(StringUtils.isEmpty(this.getSign()))emptyList.add("sign");
		if(StringUtils.isEmpty(this.getTrade_no()))emptyList.add("trade_no");
		if(StringUtils.isEmpty(this.getTrade_status()))emptyList.add("trade_status");
		if(StringUtils.isEmpty(this.getSeller_id()))emptyList.add("seller_id");
		if(StringUtils.isEmpty(this.getSeller_email()))emptyList.add("seller_email");
		if(StringUtils.isEmpty(this.getBuyer_id()))emptyList.add("buyer_id");
		if(StringUtils.isEmpty(this.getBuyer_email()))emptyList.add("buyer_email");
		if(this.getTotal_fee()==0)emptyList.add("total_fee");
		if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
		//判断数据
		List<String> paramList = new ArrayList<String>();
		if(this.getTotal_fee()<0.01||this.getTotal_fee()>100000000) paramList.add("total_fee");
		if(!paramList.isEmpty()) throw new UnmatchedParamException(paramList);
		
		return true;
	}
	
	@Override
	public TreeMap<String, Object> getProperties() {
		TreeMap<String, Object> map = super.getProties();
		map.put("out_trade_no", this.getOut_trade_no());
		map.put("subject", this.getSubject());
		map.put("payment_type", this.getPayment_type());
		map.put("trade_no", this.getTrade_no());
		map.put("trade_status", this.getTrade_status());
		map.put("seller_id", this.getSeller_id());
		map.put("seller_email", this.getSeller_email());
		map.put("buyer_id", this.getBuyer_id());
		map.put("buyer_email", this.getBuyer_email());
		map.put("total_fee", this.getTotal_fee());
		map.put("quantity", this.getQuantity());
		map.put("price", this.getPrice());
		map.put("body", this.getBody());
		map.put("gmt_create", this.getGmt_create());
		map.put("gmt_payment", this.getGmt_payment());
		map.put("is_total_fee_adjust", this.getIs_total_fee_adjust());
		map.put("use_coupon", this.getUse_coupon());
		map.put("discount", this.getDiscount());
		map.put("refund_status", this.getRefund_status());
		map.put("gmt_refund", this.getGmt_refund());
		return map;
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

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public double getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(double total_fee) {
		this.total_fee = total_fee;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public String getGmt_payment() {
		return gmt_payment;
	}

	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}

	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}

	public String getUse_coupon() {
		return use_coupon;
	}

	public void setUse_coupon(String use_coupon) {
		this.use_coupon = use_coupon;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public String getGmt_refund() {
		return gmt_refund;
	}

	public void setGmt_refund(String gmt_refund) {
		this.gmt_refund = gmt_refund;
	}
}

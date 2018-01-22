package com.pay.cloud.pay.escrow.alipay.param;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 即时到账接口参数
 * @author yanjie.ji
 * @date 2016年9月26日 
 * @time 下午5:22:45
 */
public class AlipayImmediatePayParam implements AlipayParam {
	/**
	 * 服务器异步通知页面路径
	 */
	private String notify_url;
	/**
	 * 页面跳转同步通知页面路径
	 */
	private String return_url;
	/**
	 * 商户网站唯一订单号
	 */
	private String out_trade_no;
	/**
	 * 商品名称
	 */
	private String subject;
	/**
	 * 交易金额
	 */
	private double total_fee;
	/**
	 * 卖家支付宝用户号
	 */
	private String seller_id;
	/**
	 * 卖家支付宝账号
	 */
	private String seller_email;
	/**
	 * 卖家支付宝账号别名
	 */
	private String seller_account_name;
	/**
	 * 买家支付宝用户号
	 */
	private String buyer_id;
	/**
	 * 买家支付宝账号
	 */
	private String buyer_email;
	/**
	 * 买家支付宝账号别名
	 */
	private String buyer_account_name;
	/**
	 * 商品单价
	 */
	private String price;
	/**
	 * 购买数量
	 */
	private String quantity;
	/**
	 * 商品描述
	 */
	private String body;
	/**
	 * 商品展示网址
	 */
	private String show_url;
	/**
	 * 默认支付方式
	 */
	private String paymethod;
	/**
	 * 支付渠道
	 */
	private String enable_paymethod;
	/**
	 * 防钓鱼时间戳
	 */
	private String anti_phishing_key;
	/**
	 * 客户端IP
	 */
	private String exter_invoke_ip;
	/**
	 * 公用回传参数
	 */
	private String extra_common_param;
	/**
	 * 超时时间
	 */
	private String it_b_pay;
	/**
	 * 快捷登录授权令牌
	 */
	private String token;
	/**
	 * 扫码支付方式
	 */
	private String qr_pay_mode;
	/**
	 * 商户自定二维码宽度
	 */
	private String qrcode_width;
	/**
	 * 是否需要买家实名认证
	 */
	private String need_buyer_realnamed;
	/**
	 * 花呗分期参数
	 */
	private String hb_fq_param;
	/**
	 * 商品类型
	 */
	private String goods_type;

	@Override
	public boolean checkSelf() throws UnmatchedParamException {
		List<String> emptyList = new ArrayList<String>();
		if(StringUtils.isEmpty(this.getOut_trade_no())) emptyList.add("out_trade_no");
		if(StringUtils.isEmpty(this.getSubject())) emptyList.add("subject");
		if(!emptyList.isEmpty()) throw new ParamEmptyException(emptyList);
		
		List<String> paramList = new ArrayList<String>();
		if(this.getTotal_fee()<=0||this.getTotal_fee()>100000000) paramList.add("total_fee");
		if(!paramList.isEmpty())
			throw new UnmatchedParamException(paramList);
		
		return true;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
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

	public double getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(double total_fee) {
		this.total_fee = total_fee;
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

	public String getSeller_account_name() {
		return seller_account_name;
	}

	public void setSeller_account_name(String seller_account_name) {
		this.seller_account_name = seller_account_name;
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

	public String getBuyer_account_name() {
		return buyer_account_name;
	}

	public void setBuyer_account_name(String buyer_account_name) {
		this.buyer_account_name = buyer_account_name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getShow_url() {
		return show_url;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getEnable_paymethod() {
		return enable_paymethod;
	}

	public void setEnable_paymethod(String enable_paymethod) {
		this.enable_paymethod = enable_paymethod;
	}

	public String getAnti_phishing_key() {
		return anti_phishing_key;
	}

	public void setAnti_phishing_key(String anti_phishing_key) {
		this.anti_phishing_key = anti_phishing_key;
	}

	public String getExter_invoke_ip() {
		return exter_invoke_ip;
	}

	public void setExter_invoke_ip(String exter_invoke_ip) {
		this.exter_invoke_ip = exter_invoke_ip;
	}

	public String getExtra_common_param() {
		return extra_common_param;
	}

	public void setExtra_common_param(String extra_common_param) {
		this.extra_common_param = extra_common_param;
	}

	public String getIt_b_pay() {
		return it_b_pay;
	}

	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQr_pay_mode() {
		return qr_pay_mode;
	}

	public void setQr_pay_mode(String qr_pay_mode) {
		this.qr_pay_mode = qr_pay_mode;
	}

	public String getQrcode_width() {
		return qrcode_width;
	}

	public void setQrcode_width(String qrcode_width) {
		this.qrcode_width = qrcode_width;
	}

	public String getNeed_buyer_realnamed() {
		return need_buyer_realnamed;
	}

	public void setNeed_buyer_realnamed(String need_buyer_realnamed) {
		this.need_buyer_realnamed = need_buyer_realnamed;
	}

	public String getHb_fq_param() {
		return hb_fq_param;
	}

	public void setHb_fq_param(String hb_fq_param) {
		this.hb_fq_param = hb_fq_param;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
}

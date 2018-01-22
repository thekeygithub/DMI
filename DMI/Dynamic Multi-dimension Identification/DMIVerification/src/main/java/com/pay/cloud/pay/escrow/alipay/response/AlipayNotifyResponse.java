package com.pay.cloud.pay.escrow.alipay.response;

import java.util.TreeMap;
/**
 * 阿里异步反馈信息基础类
 * @author yanjie.ji
 * @date 2016年8月31日 
 * @time 下午6:05:31
 */
public abstract class AlipayNotifyResponse implements AlipayResponse{
	/**
	 * 通知时间
	 * 必填
	 */
	private String notify_time;
	/**
	 * 通知类型
	 * 必填
	 */
	private String notify_type;
	/**
	 * 通知校验ID
	 * 必填
	 */
	private String notify_id;
	/**
	 * 签名方式
	 * 必填
	 */
	private String sign_type;
	/**
	 * 签名
	 * 必填
	 */
	private String sign;
	
	protected TreeMap<String,Object> getProties(){
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("notify_time", this.getNotify_time());
		map.put("notify_type", this.getNotify_type());
		map.put("notify_id", this.getNotify_id());
		map.put("sign_type", this.getSign_type());
		map.put("sign", this.getSign());
		return map;
	}
	
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}
	public String getNotify_type() {
		return notify_type;
	}
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}

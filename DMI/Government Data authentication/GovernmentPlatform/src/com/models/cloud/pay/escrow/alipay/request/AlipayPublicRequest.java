package com.models.cloud.pay.escrow.alipay.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.models.cloud.pay.escrow.alipay.exception.ParamLengthException;
import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;


/**
 * 阿里支付公共参数
 * @author yanjie.ji
 * @date 2016年8月11日 
 * @time 下午1:35:45
 */
public abstract class AlipayPublicRequest {

	/**
	 * 支付宝分配给开发者的应用ID
	 * 必填
	 */
	private String app_id;
	/**
	 * 接口名称
	 * 必填
	 */
	private String method;
	/**
	 * 仅支持JSON
	 */
	private String format="JSON";
	/**
	 * 请求使用的编码格式，如utf-8
	 * 必填
	 */
	private String charset="UTF-8";
	/**
	 * 商户生成签名字符串所使用的签名算法类型，目前支持RSA
	 * 必填
	 */
	private String sign_type="RSA";
	/**
	 * 商户请求参数的签名串
	 * 必填
	 */
	private String sign;
	/**
	 * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
	 * 必填
	 */
	private String timestamp;
	/**
	 * 调用的接口版本，固定为：1.0
	 * 必填
	 */
	private String version="1.0";
	/**
	 * 应用授权
	 */
	private String app_auth_token;
	/**
	 * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
	 * 必填
	 */
	private String biz_content;
	
	
	public boolean check()throws UnmatchedParamException{
		List<String> emptyList = new ArrayList<String>();
		List<String> lengthList = new ArrayList<String>();
		//判空
		if(StringUtils.isEmpty(this.getApp_id()))emptyList.add("app_id");
		if(StringUtils.isEmpty(this.getMethod()))emptyList.add("method");
		if(StringUtils.isEmpty(this.getCharset()))emptyList.add("charset");
		if(StringUtils.isEmpty(this.getSign_type()))emptyList.add("sign_type");
		if(StringUtils.isEmpty(this.getSign()))emptyList.add("sign");
		if(StringUtils.isEmpty(this.getTimestamp()))emptyList.add("timestamp");
		if(StringUtils.isEmpty(this.getVersion()))emptyList.add("version");
		if(StringUtils.isEmpty(this.getBiz_content()))emptyList.add("biz_content");
		if(!emptyList.isEmpty()) 
			throw new ParamEmptyException(emptyList);
		//校验长度
		if(this.getApp_id().length()>32) lengthList.add("app_id");
		if(this.getMethod().length()>32) lengthList.add("method");
		if(StringUtils.isNotEmpty(this.getFormat())&&this.getFormat().length()>40) lengthList.add("format");
		if(this.getCharset().length()>10) lengthList.add("charset");
		if(this.getSign_type().length()>10) lengthList.add("sign_type");
		if(this.getSign().length()>256) lengthList.add("sign");
		if(this.getTimestamp().length()>19) lengthList.add("timestamp");
		if(this.getVersion().length()>3) lengthList.add("version");
		if(!lengthList.isEmpty()) 
			throw new ParamLengthException(lengthList);
		
		if(!checkSelf()) return false;
		
		return true;
	}
	
	protected abstract boolean checkSelf();
	
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
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
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getApp_auth_token() {
		return app_auth_token;
	}
	public void setApp_auth_token(String app_auth_token) {
		this.app_auth_token = app_auth_token;
	}
	public String getBiz_content() {
		return biz_content;
	}
	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}
}

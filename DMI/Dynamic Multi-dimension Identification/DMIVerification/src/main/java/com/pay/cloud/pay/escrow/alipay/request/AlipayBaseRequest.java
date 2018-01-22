package com.pay.cloud.pay.escrow.alipay.request;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
import com.pay.cloud.pay.escrow.alipay.utils.AlipayConstants;

public abstract class AlipayBaseRequest implements AlipayRequest{

	/**
	 * 接口名称
	 * 必填
	 */
	private String service;
	/**
	 * 合作者身份ID
	 * 必填
	 */
	private String partner;
	/**
	 * 参数编码字符集
	 * 必填
	 */
	private String _input_charset=AlipayConstants.CHARSET_UTF8;
	/**
	 * 签名方式
	 * 必填
	 */
	private String sign_type=AlipayConstants.SIGN_TYPE;
	/**
	 * 签名
	 * 必填
	 */
	private String sign;
	
	@Override
	public boolean checkself() throws UnmatchedParamException {
		List<String> emptyList = new ArrayList<String>();
		//判空
		if(StringUtils.isEmpty(this.getService()))emptyList.add("service");
		if(StringUtils.isEmpty(this.getPartner()))emptyList.add("partner");
		if(StringUtils.isEmpty(this.get_input_charset()))emptyList.add("_input_charset");
		if(StringUtils.isEmpty(this.getSign_type()))emptyList.add("sign_type");
//		if(StringUtils.isEmpty(this.getSign()))emptyList.add("sign");
		if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
		return check();
	
	}
	
	protected abstract boolean check()throws UnmatchedParamException;
	
	
	@Override
	public TreeMap<String, String> getProperties(){
		TreeMap<String, String> map = new TreeMap<String,String>();
		map.put("service", this.getService());
		map.put("partner", this.getPartner());
		map.put("_input_charset", this.get_input_charset());
		map.put("sign_type", this.getSign_type());
		map.put("sign", this.getSign());
		map.putAll(getSelfProperties());
		return map;
	}
	
	protected abstract TreeMap<String, String> getSelfProperties();
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String get_input_charset() {
		return _input_charset;
	}
	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
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

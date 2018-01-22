package com.pay.cloud.cert.supplier.hf.info;

import com.pay.cloud.cert.exception.CodeException;

/**
 * 华付四要素接口参数
 * @author yanjie.ji
 * @date 2016年7月15日 
 * @time 上午10:39:01
 */
public class HfFourParam extends HfBaseParam {
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 身份证号码
	 */
	private String certNo;
	/**
	 * 银行卡号
	 */
	private String cardNo;
	/**
	 * 手机号
	 */
	private String phoneNo;
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	@Override
	public boolean check() throws CodeException {
		// TODO Auto-generated method stub
		return false;
	}
}

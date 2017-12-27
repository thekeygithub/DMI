package com.ebmi.std.bussiness.dto;

public class SiCardInfo {

	/** 手机号*/
	private String mobile;
	/** 固定电话*/
	private String phone;
	/** 电子邮件*/
	private String email;
	/** 通讯地址*/
	private String addr;
	/** 邮政编码*/
	private String postCode;
	/**主键*/
	private String pMiId;
	/**社保卡号*/
	private String SiCardNo;
	
	
	public String getSiCardNo() {
		return SiCardNo;
	}
	public void setSiCardNo(String siCardNo) {
		SiCardNo = siCardNo;
	}
	public String getpMiId() {
		return pMiId;
	}
	public void setpMiId(String pMiId) {
		this.pMiId = pMiId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
}

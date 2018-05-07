package com.models.cloud.cert.info;

/**
 * 实名认证--四要素
 * @author yanjie.ji
 * @date 2016年7月12日 
 * @time 上午10:07:18
 */
public class EbaoFourParam{

	private String realname;
	private String idcard;
	private String bankcard;
	private String mobile;
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}

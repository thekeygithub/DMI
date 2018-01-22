package com.pay.cloud.pay.escrow.mi.pangu.request;


/**
 * 门诊收费结算回退
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 上午9:48:30
 */
public class MzTollReq extends BaseReq{
	
	private String serialID; // 门诊流水号
	private String dealID;//交易流水号
	private String personID; // 个人ID
	private String perCardID;//身份证号
	private String perName;//姓名
	private String jbr; // 经办人
	private String telephone;   //联系电话
	
	public String getSerialID() {
		return serialID;
	}
	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}
	public String getDealID() {
		return dealID;
	}
	public void setDealID(String dealID) {
		this.dealID = dealID;
	}
	public String getPersonID() {
		return personID;
	}
	public void setPersonID(String personID) {
		this.personID = personID;
	}
	public String getPerCardID() {
		return perCardID;
	}
	public void setPerCardID(String perCardID) {
		this.perCardID = perCardID;
	}
	public String getPerName() {
		return perName;
	}
	public void setPerName(String perName) {
		this.perName = perName;
	}
	public String getJbr() {
		return jbr;
	}
	public void setJbr(String jbr) {
		this.jbr = jbr;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}

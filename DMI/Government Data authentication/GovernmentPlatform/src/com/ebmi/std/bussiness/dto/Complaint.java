package com.ebmi.std.bussiness.dto;

public class Complaint {

	 /** 姓名 */
	 private String pName;	 
	 /** 身份证号*/
	 private String pCertNo;
	 /** 手机号码*/
	 private String mobile;
	 /** 涉及单位名称*/
	 private String copp;
	 /** 单位负责人*/
	 private String name;
	 /** 单位联系电话*/
	 private String phone;
	 /** 单位地址*/
	 private String addr;
	 /** 组织机构代码*/
	 private String org;
	 /** 投诉(举报)事实*/
	 private String content;
	 /** 投诉请求*/
	 private String request;
	 /** 匿名*/ 
	 private String anonymityFlg;
	 /** 集体投诉举报*/
	 private String teamFlg;
	 
	 /** 记录ID*/
	 private String recordId;
	 /** 投诉时间*/
	 private String recordDate;
	 /** 投诉状态  0已受理待回复、1已回复*/
	 private String state;
	 /** 回复*/
	 private String reback; 
	 /** 回复日期*/
	 private String rebackDate;
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpCertNo() {
		return pCertNo;
	}
	public void setpCertNo(String pCertNo) {
		this.pCertNo = pCertNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCopp() {
		return copp;
	}
	public void setCopp(String copp) {
		this.copp = copp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getAnonymityFlg() {
		return anonymityFlg;
	}
	public void setAnonymityFlg(String anonymityFlg) {
		this.anonymityFlg = anonymityFlg;
	}
	public String getTeamFlg() {
		return teamFlg;
	}
	public void setTeamFlg(String teamFlg) {
		this.teamFlg = teamFlg;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReback() {
		return reback;
	}
	public void setReback(String reback) {
		this.reback = reback;
	}
	public String getRebackDate() {
		return rebackDate;
	}
	public void setRebackDate(String rebackDate) {
		this.rebackDate = rebackDate;
	}
	
	 
	 
}

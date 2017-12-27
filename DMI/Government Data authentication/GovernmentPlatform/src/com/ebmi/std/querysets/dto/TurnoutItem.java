package com.ebmi.std.querysets.dto;

import java.util.Date;

public class TurnoutItem {

	// 审批日期
	private Date approval_date;
	// 所在医院
	private String now_hospital;
	// 转往医院
	private String to_hospital;
	// 目前所在科室
	private String hospital_section;
	// 省内省外
	private String province_in_out;
	// 转出医生
	private String turnout_doctor;
	// 临床诊断病因
	private String clinical_cause;
	
	public Date getApproval_date() {
		return approval_date;
	}
	
	public void setApproval_date(Date approval_date) {
		this.approval_date = approval_date;
	}
	
	public String getNow_hospital() {
		return now_hospital;
	}
	
	public void setNow_hospital(String now_hospital) {
		this.now_hospital = now_hospital;
	}
	
	public String getTo_hospital() {
		return to_hospital;
	}
	
	public void setTo_hospital(String to_hospital) {
		this.to_hospital = to_hospital;
	}
	
	public String getHospital_section() {
		return hospital_section;
	}
	
	public void setHospital_section(String hospital_section) {
		this.hospital_section = hospital_section;
	}
	
	public String getProvince_in_out() {
		return province_in_out;
	}
	public void setProvince_in_out(String province_in_out) {
		this.province_in_out = province_in_out;
	}
	
	public String getTurnout_doctor() {
		return turnout_doctor;
	}
	
	public void setTurnout_doctor(String turnout_doctor) {
		this.turnout_doctor = turnout_doctor;
	}
	
	public String getClinical_cause() {
		return clinical_cause;
	}
	
	public void setClinical_cause(String clinical_cause) {
		this.clinical_cause = clinical_cause;
	}
	
}

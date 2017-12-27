package com.ebmi.std.pclaimevlist.dto;

public class PClaimModel {

	private String p_mi_id;//个人编号
	private String claim_id;//流水号
	private String claim_date;//结算时间
	private String ent_name;//消费地点
	private String med_type_name;//医疗类别
	private String hospFee;//就诊费用
	private String org_code;//机构编号
	
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getClaim_id() {
		return claim_id;
	}
	public void setClaim_id(String claim_id) {
		this.claim_id = claim_id;
	}
	public String getClaim_date() {
		return claim_date;
	}
	public void setClaim_date(String claim_date) {
		this.claim_date = claim_date;
	}
	public String getEnt_name() {
		return ent_name;
	}
	public void setEnt_name(String ent_name) {
		this.ent_name = ent_name;
	}
	public String getMed_type_name() {
		return med_type_name;
	}
	public void setMed_type_name(String med_type_name) {
		this.med_type_name = med_type_name;
	}
	public String getHospFee() {
		return hospFee;
	}
	public void setHospFee(String hospFee) {
		this.hospFee = hospFee;
	}
	public String getOrg_code() {
		return org_code;
	}
	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}
	
}
package com.aghit.task.algorithm.domain;

import java.util.*;

/**
 * 费用清单
 * */
public class Diagnosis extends Payment {

	// 结算单ID
	private long clm_id;

	// 身份证号
	private String p_id_no;

	// 数据来源
	private long data_src_id;

	// 医疗编码
	private String hosp_code;

	// 患者出生年月日
	private Date p_dob;

	// 患者年龄
	private int p_age;

	// 患者性别
	private long p_gend_id;

//	// 就诊日期
//	private Date med_date;

	// 看诊医师编码
	private String dr_code;

	// 看诊医师名称
//	private String dr_name;

	// 费用总额
	private double pay_tot;

	// 支付类别
	private long mi_schm_id;

	/** 医师职称 */
	private String doc_lvl;
	
	/**
	 * @return 联合主键（数据来源ID_结算单ID）
	 */
	public String getUnionId(){
		return data_src_id + "_" + clm_id;
	}
	
	public long getP_gend_id() {
		return p_gend_id;
	}

	public void setP_gend_id(long pGendId) {
		p_gend_id = pGendId;
	}

	public String getP_id_no() {
		return p_id_no;
	}

	public void setP_id_no(String pIdNo) {
		p_id_no = pIdNo;
	}

	public long getClm_id() {
		return clm_id;
	}

	public void setClm_id(long clmId) {
		clm_id = clmId;
	}

	public Date getP_dob() {
		return p_dob;
	}

	public void setP_dob(Date pDob) {
		p_dob = pDob;
	}

	public int getP_age() {
		return p_age;
	}

	public void setP_age(int pAge) {
		p_age = pAge;
	}

//	public Date getMed_date() {
//		return med_date;
//	}
//
//	public void setMed_date(Date medDate) {
//		med_date = medDate;
//	}

	public String getDr_code() {
		return dr_code;
	}

	public void setDr_code(String drCode) {
		dr_code = drCode;
	}

//	public String getDr_name() {
//		return dr_name;
//	}
//
//	public void setDr_name(String drName) {
//		dr_name = drName;
//	}

	public double getPay_tot() {
		return pay_tot;
	}

	public void setPay_tot(double payTot) {
		pay_tot = payTot;
	}

	public long getMi_schm_id() {
		return mi_schm_id;
	}

	public void setMi_schm_id(long miSchmId) {
		mi_schm_id = miSchmId;
	}

	public long getData_src_id() {
		return data_src_id;
	}

	public void setData_src_id(long data_src_id) {
		this.data_src_id = data_src_id;
	}

	public String getHosp_code() {
		return hosp_code;
	}

	public void setHosp_code(String hosp_code) {
		this.hosp_code = hosp_code;
	}

	public String getDoc_lvl() {
		return doc_lvl;
	}

	public void setDoc_lvl(String doc_lvl) {
		this.doc_lvl = doc_lvl;
	}

	
	// /** 患者身份证 */
	// private String patient_id_number;
	// /** 服务机构名称 */
	// private String serv_org_name;
	// /** 医疗机构类别 */
	// private String hospital_tier;
	// /** 生日 */
	// private Date patient_birthday;
	// /** 就诊科室编码 */
	// private String hospital_dep_code;
	// /** 就诊科室名称 */
	// private String hospital_dep_name;
	// /** 看诊医师编码 */
	// private String doctor_code;
	// /** 看诊医师姓名 */
	// private String doctor_name;
	// // /** 疾病诊断编码 */
	// // private String[] diagnosis_code;
	// /** 疾病诊断名称 */
	// private String[] diagnosis_name;
	//	
	// // /** 医院结算日期 */
	// // private Date hosp_clearing_cost_date;
	//	
	// // /** 医保结算编号 */
	// // private String si_clearing_cost_no;
	// /** 费用总额 */
	// private double payment_total;
	// /** 支付类别 */
	// private String si_scheme_code;
	// /** 就诊结算方式 */
	// private String clearing_form;
	// /** 起付线 */
	// private double deductible;
	// /** 进入起付线部分 */
	// private double deductible_part;
	// /** 患者自费总额 */
	// private double out_of_pocket_cost;
	// /** 患者自付总额 */
	// private double copayment;
	// /** 个人帐户支付总额 */
	// private double personal_account_payment;
	// /** 社保基金帐户支付金额 */
	// private double si_account_payment;
	// /** 补充医疗保险支付金额 */
	// private double supple_insurance_payment;
	// /** 报销类型 */
	// private String reimb_type;
	// /** 报销比例 */
	// private double reimb_ratio;
	// /** 清算分中心 */
	// private String liquidate_agency_no;
	//	
	//	
	// /** 社保卡号 */
	// private String patient_si_number;
	//
	// /** 性别 */
	// private String patient_gender;

}

package com.aghit.task.algorithm.domain;

import java.util.Date;
import java.util.Map;

/**
 * 医嘱清单
 * 
 */
public class DoctorAdviceRecord extends Payment{

	/** 医嘱类型(诊疗、耗材) */
	private String order_type;
	
	/** 诊疗(或耗材)名称 */
	private String order_name;

	/** 数量 */
	private double order_amount;
	/** 生日 */
	private Date patient_birthday;
	/** 患者身份证 */
	private String patient_id_number;
	/** 耗材本体ID */
	private long ag_id;
	
	/** 医嘱ID */
	private long ord_id;
	/** 医嘱执行时间 */
	private Date ord_exe_date;
	//医师级别
	private long dr_lvl_detail;
	//是否医保范围内
	private int a_mi_flag;
	//单价
	private double ord_unit_price;
	//金额
	private double ord_fee;
	//是否审核
	private int chk_flag;
	//合计类集合
	private Map<String,Object> total;

	public java.lang.String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(java.lang.String orderType) {
		order_type = orderType;
	}

	public String getOrder_name() {
		return order_name;
	}

	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}

	public double getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(double orderAmount) {
		order_amount = orderAmount;
	}


	public Date getPatient_birthday() {
		return patient_birthday;
	}

	public void setPatient_birthday(Date patient_birthday) {
		this.patient_birthday = patient_birthday;
	}

	public String getPatient_id_number() {
		return patient_id_number;
	}

	public void setPatient_id_number(String patient_id_number) {
		this.patient_id_number = patient_id_number;
	}

	public long getOrd_id() {
		return ord_id;
	}

	public void setOrd_id(long ord_id) {
		this.ord_id = ord_id;
	}

	public long getAg_id() {
		return ag_id;
	}

	public void setAg_id(long ag_id) {
		this.ag_id = ag_id;
	}

	public Date getOrd_exe_date() {
		return ord_exe_date;
	}

	public void setOrd_exe_date(Date ord_exe_date) {
		this.ord_exe_date = ord_exe_date;
	}

	public long getDr_lvl_detail() {
		return dr_lvl_detail;
	}

	public void setDr_lvl_detail(long dr_lvl_detail) {
		this.dr_lvl_detail = dr_lvl_detail;
	}

	public int getA_mi_flag() {
		return a_mi_flag;
	}

	public void setA_mi_flag(int a_mi_flag) {
		this.a_mi_flag = a_mi_flag;
	}

	public double getOrd_unit_price() {
		return ord_unit_price;
	}

	public void setOrd_unit_price(double ord_unit_price) {
		this.ord_unit_price = ord_unit_price;
	}

	public double getOrd_fee() {
		return ord_fee;
	}

	public void setOrd_fee(double ord_fee) {
		this.ord_fee = ord_fee;
	}

	public int getChk_flag() {
		return chk_flag;
	}

	public void setChk_flag(int chk_flag) {
		this.chk_flag = chk_flag;
	}

	public Map<String, Object> getTotal() {
		return total;
	}

	public void setTotal(Map<String, Object> total) {
		this.total = total;
	}
	
}
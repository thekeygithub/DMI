package com.ebmi.std.clm.dto;

import java.util.Date;
import java.util.List;

/**
 * 查询医保消费列表 对应实体
 * @author xuhai
 *
 */
public class PClaimConsume {
	
	private Date date;//结算日期
	private Double all_pay;//总额
	private Double cash_pay;//现金支付
	private Double personal_pay;//个人账户
	private String ent;//消费地点
	private String med_type;//医疗类别
	private Double cur_all_pay;//本次发生医疗费 总额
	private Double cur_cash_pay;//本次个人账户支付金额
	private Double cur_personal_pay;//本次个人现金支付金额
	private String clm_id;//结算单编号
	
	//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
	private Double pay_five;//本次统筹金支付金额
	private Double pay_seven;//本次公务员基金支付金额
	private Double pay_eight;//本次大额医疗补助支付金额
	
	private String org_code;//机构编号
	private String hosptial_server_code;//定点医疗服务机构编号
	private List pay_list;
	
	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getHosptial_server_code() {
		return hosptial_server_code;
	}

	public void setHosptial_server_code(String hosptial_server_code) {
		this.hosptial_server_code = hosptial_server_code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getAll_pay() {
		return all_pay;
	}

	public void setAll_pay(Double all_pay) {
		this.all_pay = all_pay;
	}

	public Double getCash_pay() {
		return cash_pay;
	}

	public void setCash_pay(Double cash_pay) {
		this.cash_pay = cash_pay;
	}

	public Double getPersonal_pay() {
		return personal_pay;
	}

	public void setPersonal_pay(Double personal_pay) {
		this.personal_pay = personal_pay;
	}

	public String getEnt() {
		return ent;
	}

	public void setEnt(String ent) {
		this.ent = ent;
	}

	public String getMed_type() {
		return med_type;
	}

	public void setMed_type(String med_type) {
		this.med_type = med_type;
	}

	public Double getCur_all_pay() {
		return cur_all_pay;
	}

	public void setCur_all_pay(Double cur_all_pay) {
		this.cur_all_pay = cur_all_pay;
	}

	public Double getCur_cash_pay() {
		return cur_cash_pay;
	}

	public void setCur_cash_pay(Double cur_cash_pay) {
		this.cur_cash_pay = cur_cash_pay;
	}

	public Double getCur_personal_pay() {
		return cur_personal_pay;
	}

	public void setCur_personal_pay(Double cur_personal_pay) {
		this.cur_personal_pay = cur_personal_pay;
	}

	public String getClm_id() {
		return clm_id;
	}

	public void setClm_id(String clm_id) {
		this.clm_id = clm_id;
	}

	public Double getPay_five() {
		return pay_five;
	}

	public void setPay_five(Double pay_five) {
		this.pay_five = pay_five;
	}

	public Double getPay_seven() {
		return pay_seven;
	}

	public void setPay_seven(Double pay_seven) {
		this.pay_seven = pay_seven;
	}

	public Double getPay_eight() {
		return pay_eight;
	}

	public void setPay_eight(Double pay_eight) {
		this.pay_eight = pay_eight;
	}

	public List getPay_list() {
		return pay_list;
	}

	public void setPay_list(List pay_list) {
		this.pay_list = pay_list;
	}
	
}
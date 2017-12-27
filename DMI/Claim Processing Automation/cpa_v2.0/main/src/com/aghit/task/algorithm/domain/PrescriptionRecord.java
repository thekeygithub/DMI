package com.aghit.task.algorithm.domain;

import java.util.Date;
import java.util.Map;

/**
 * 处方清单
 */

public class PrescriptionRecord extends Payment{

	/** 标准化前的药名 */
	private String lgcy_drug_desc;
	/** 药品用法频次 */
	private double drug_usg_val;
	/** 药品用量 */
	private double drug_usg_amt;
	/** 药品用量单位 */
	private long drug_usg_unit;
	/** 给药天数 */
	private int drug_days;
	/** 给药途径*/
	private long drug_adm_appro;
	/** 数量 */
	private double drug_amount;
	/** 药品标准化ID */
	private long drug_id;
	/** 处方ID */
	private long pre_Id;
	/** 执行时间  */
	private Date ord_exe_date;
	//医师级别
	private long dr_lvl_detail;
	//是否医保范围内
	private int a_mi_flag;
	//单价
	private double drug_unit_price;
	//处方单号
	private String lgcy_pre_code;
	//医嘱药品组号
	private String pre_seq_no;
	//药品费用
	private double drug_fee;
	//是否审核
	private int chk_flag;
	//合计类集合
	private Map<String,Object> total;
	

	public long getDrug_id() {
		return drug_id;
	}

	public void setDrug_id(long drug_id) {
		this.drug_id = drug_id;
	}

	public double getDrug_amount() {
		return drug_amount;
	}

	public void setDrug_amount(double drugAmount) {
		drug_amount = drugAmount;
	}

	public String getLgcy_drug_desc() {
		return lgcy_drug_desc;
	}

	public void setLgcy_drug_desc(String lgcy_drug_desc) {
		this.lgcy_drug_desc = lgcy_drug_desc;
	}

	public double getDrug_usg_val() {
		return drug_usg_val;
	}

	public void setDrug_usg_val(double drug_usg_val) {
		this.drug_usg_val = drug_usg_val;
	}

	public double getDrug_usg_amt() {
		return drug_usg_amt;
	}

	public void setDrug_usg_amt(double drug_usg_amt) {
		this.drug_usg_amt = drug_usg_amt;
	}

	public long getDrug_usg_unit() {
		return drug_usg_unit;
	}

	public void setDrug_usg_unit(long drug_usg_unit) {
		this.drug_usg_unit = drug_usg_unit;
	}

	public int getDrug_days() {
		return drug_days;
	}

	public void setDrug_days(int drug_days) {
		this.drug_days = drug_days;
	}

	public long getDrug_adm_appro() {
		return drug_adm_appro;
	}

	public void setDrug_adm_appro(long drug_adm_appro) {
		this.drug_adm_appro = drug_adm_appro;
	}

	public long getPre_Id() {
		return pre_Id;
	}

	public void setPre_Id(long pre_Id) {
		this.pre_Id = pre_Id;
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

	public double getDrug_unit_price() {
		return drug_unit_price;
	}

	public void setDrug_unit_price(double drug_unit_price) {
		this.drug_unit_price = drug_unit_price;
	}

	public String getLgcy_pre_code() {
		return lgcy_pre_code;
	}

	public void setLgcy_pre_code(String lgcy_pre_code) {
		this.lgcy_pre_code = lgcy_pre_code;
	}

	public String getPre_seq_no() {
		return pre_seq_no;
	}

	public void setPre_seq_no(String pre_seq_no) {
		this.pre_seq_no = pre_seq_no;
	}

	public double getDrug_fee() {
		return drug_fee;
	}

	public void setDrug_fee(double drug_fee) {
		this.drug_fee = drug_fee;
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
package com.ebmi.std.clm.dto;

import java.util.Date;

/**
 * 结算单项目明细
 * @author yanjie.ji
 * @date 2015-12-22
 * @time 上午11:03:57
 *
 */
public class PClaimItem{
	/**
	 * 明细项目ID
	 */
	private String item_id;
	/**
	 * 结算单ID
	 */
	private String clm_id;
	/**
	 * 原始明细项目ID
	 */
	private String orig_item_id;
	/**
	 * 原始结算单编号
	 */
	private String orig_clm_code;
	/**
	 * 结算日期
	 */
	private String clm_date;
	/**
	 * 参保人ID
	 */
	private String p_mi_id;
	/**
	 * 明细项目类别
	 */
	private Short item_cat_id;
	/**
	 * 收费类别
	 */
	private Short chrg_type_id;
	/**
	 * 自负类别
	 */
	private Short copay_type_id;
	/**
	 * 原始医嘱编号
	 */
	private String orig_ord_code;
	/**
	 * 原始处方编号
	 */
	private String orig_pre_code;
	/**
	 * 医嘱开立时间
	 */
	private Date ord_crt_time;
	/**
	 * 医嘱执行时间
	 */
	private Date ord_exe_time;
	/**
	 * 收费时间
	 */
	private Date chrg_time;
	/**
	 * 收费流水号
	 */
	private String chrg_seq;
	/**
	 * 医保项目编码
	 */
	private String mi_item_code;
	/**
	 * 医保项目名称
	 */
	private String mi_item_name;
	/**
	 * 医保剂型
	 */
	private String mi_form_name;
	/**
	 * 医院项目编码
	 */
	private String hosp_item_code;
	/**
	 * 医院项目名称
	 */
	private String hosp_item_name;
	/**
	 * 医院剂型
	 */
	private String hosp_form_name;
	/**
	 * 医保项目规格
	 */
	private String mi_spec_desc;
	/**
	 * 医院项目规格
	 */
	private String hosp_spec_desc;
	/**
	 * 处方药标识
	 */
	private Short pre_drug_flag;
	/**
	 * 包内制剂数量
	 */
	private Integer dose_cnt;
	/**
	 * 销售单位
	 */
	private String sale_unit;
	/**
	 * 数量
	 */
	private Integer cnt;
	/**
	 * 单价
	 */
	private Double unit_price;
	/**
	 * 实际金额
	 */
	private Double fee;
	/**
	 * 基金支付限价
	 */
	private Double fund_max;
	/**
	 * 超限自费
	 */
	private Double overrun_self;
	/**
	 * 自付比例
	 */
	private Double self_ratio;
	/**
	 * 自费金额
	 */
	private Double out_of_pock;
	/**
	 * 自负金额
	 */
	private Double copay;
	/**
	 * 可报金额
	 */
	private Double mi_scope;
	/**
	 * 更新时间
	 */
	private Date upd_time;
	/**
	 * 创建时间
	 */
	private Date crt_time;
	/**
	 * ETL批次
	 */
	private String etl_batch;
	/**
	 * ETL时间
	 */
	private Date etl_time;
	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getClm_id() {
		return clm_id;
	}
	public void setClm_id(String clm_id) {
		this.clm_id = clm_id;
	}
	public String getOrig_item_id() {
		return orig_item_id;
	}
	public void setOrig_item_id(String orig_item_id) {
		this.orig_item_id = orig_item_id;
	}
	public String getOrig_clm_code() {
		return orig_clm_code;
	}
	public void setOrig_clm_code(String orig_clm_code) {
		this.orig_clm_code = orig_clm_code;
	}
	public String getClm_date() {
		return clm_date;
	}
	public void setClm_date(String clm_date) {
		this.clm_date = clm_date;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public Short getItem_cat_id() {
		return item_cat_id;
	}
	public void setItem_cat_id(Short item_cat_id) {
		this.item_cat_id = item_cat_id;
	}
	public Short getChrg_type_id() {
		return chrg_type_id;
	}
	public void setChrg_type_id(Short chrg_type_id) {
		this.chrg_type_id = chrg_type_id;
	}
	public Short getCopay_type_id() {
		return copay_type_id;
	}
	public void setCopay_type_id(Short copay_type_id) {
		this.copay_type_id = copay_type_id;
	}
	public String getOrig_ord_code() {
		return orig_ord_code;
	}
	public void setOrig_ord_code(String orig_ord_code) {
		this.orig_ord_code = orig_ord_code;
	}
	public String getOrig_pre_code() {
		return orig_pre_code;
	}
	public void setOrig_pre_code(String orig_pre_code) {
		this.orig_pre_code = orig_pre_code;
	}
	public Date getOrd_crt_time() {
		return ord_crt_time;
	}
	public void setOrd_crt_time(Date ord_crt_time) {
		this.ord_crt_time = ord_crt_time;
	}
	public Date getOrd_exe_time() {
		return ord_exe_time;
	}
	public void setOrd_exe_time(Date ord_exe_time) {
		this.ord_exe_time = ord_exe_time;
	}
	public Date getChrg_time() {
		return chrg_time;
	}
	public void setChrg_time(Date chrg_time) {
		this.chrg_time = chrg_time;
	}
	public String getChrg_seq() {
		return chrg_seq;
	}
	public void setChrg_seq(String chrg_seq) {
		this.chrg_seq = chrg_seq;
	}
	public String getMi_item_code() {
		return mi_item_code;
	}
	public void setMi_item_code(String mi_item_code) {
		this.mi_item_code = mi_item_code;
	}
	public String getMi_item_name() {
		return mi_item_name;
	}
	public void setMi_item_name(String mi_item_name) {
		this.mi_item_name = mi_item_name;
	}
	public String getMi_form_name() {
		return mi_form_name;
	}
	public void setMi_form_name(String mi_form_name) {
		this.mi_form_name = mi_form_name;
	}
	public String getHosp_item_code() {
		return hosp_item_code;
	}
	public void setHosp_item_code(String hosp_item_code) {
		this.hosp_item_code = hosp_item_code;
	}
	public String getHosp_item_name() {
		return hosp_item_name;
	}
	public void setHosp_item_name(String hosp_item_name) {
		this.hosp_item_name = hosp_item_name;
	}
	public String getHosp_form_name() {
		return hosp_form_name;
	}
	public void setHosp_form_name(String hosp_form_name) {
		this.hosp_form_name = hosp_form_name;
	}
	public String getMi_spec_desc() {
		return mi_spec_desc;
	}
	public void setMi_spec_desc(String mi_spec_desc) {
		this.mi_spec_desc = mi_spec_desc;
	}
	public String getHosp_spec_desc() {
		return hosp_spec_desc;
	}
	public void setHosp_spec_desc(String hosp_spec_desc) {
		this.hosp_spec_desc = hosp_spec_desc;
	}
	public Short getPre_drug_flag() {
		return pre_drug_flag;
	}
	public void setPre_drug_flag(Short pre_drug_flag) {
		this.pre_drug_flag = pre_drug_flag;
	}
	public Integer getDose_cnt() {
		return dose_cnt;
	}
	public void setDose_cnt(Integer dose_cnt) {
		this.dose_cnt = dose_cnt;
	}
	public String getSale_unit() {
		return sale_unit;
	}
	public void setSale_unit(String sale_unit) {
		this.sale_unit = sale_unit;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public Double getFund_max() {
		return fund_max;
	}
	public void setFund_max(Double fund_max) {
		this.fund_max = fund_max;
	}
	public Double getOverrun_self() {
		return overrun_self;
	}
	public void setOverrun_self(Double overrun_self) {
		this.overrun_self = overrun_self;
	}
	public Double getSelf_ratio() {
		return self_ratio;
	}
	public void setSelf_ratio(Double self_ratio) {
		this.self_ratio = self_ratio;
	}
	public Double getOut_of_pock() {
		return out_of_pock;
	}
	public void setOut_of_pock(Double out_of_pock) {
		this.out_of_pock = out_of_pock;
	}
	public Double getCopay() {
		return copay;
	}
	public void setCopay(Double copay) {
		this.copay = copay;
	}
	public Double getMi_scope() {
		return mi_scope;
	}
	public void setMi_scope(Double mi_scope) {
		this.mi_scope = mi_scope;
	}
	public Date getUpd_time() {
		return upd_time;
	}
	public void setUpd_time(Date upd_time) {
		this.upd_time = upd_time;
	}
	public Date getCrt_time() {
		return crt_time;
	}
	public void setCrt_time(Date crt_time) {
		this.crt_time = crt_time;
	}
	public String getEtl_batch() {
		return etl_batch;
	}
	public void setEtl_batch(String etl_batch) {
		this.etl_batch = etl_batch;
	}
	public Date getEtl_time() {
		return etl_time;
	}
	public void setEtl_time(Date etl_time) {
		this.etl_time = etl_time;
	}
}

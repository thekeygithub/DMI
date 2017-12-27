package com.ebmi.std.clm.cond;

import java.util.Date;

public class ClmItemCondition {
	/**
	 * 结算单ID
	 */
	private String clm_id;
	/**
	 * 原始结算单编号
	 */
	private String orig_clm_code;
	/**
	 * 参保人ID
	 */
	private String p_mi_id;
	/**
	 * 明细项目类别
	 */
	private String item_cat_id;
	/**
	 * 收费时间start
	 */
	private Date chrg_time_start;
	/**
	 * 收费时间end
	 */
	private Date chrg_time_end;
	
	public String getClm_id() {
		return clm_id;
	}
	public void setClm_id(String clm_id) {
		this.clm_id = clm_id;
	}
	public String getOrig_clm_code() {
		return orig_clm_code;
	}
	public void setOrig_clm_code(String orig_clm_code) {
		this.orig_clm_code = orig_clm_code;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getItem_cat_id() {
		return item_cat_id;
	}
	public void setItem_cat_id(String item_cat_id) {
		this.item_cat_id = item_cat_id;
	}
	public Date getChrg_time_start() {
		return chrg_time_start;
	}
	public void setChrg_time_start(Date chrg_time_start) {
		this.chrg_time_start = chrg_time_start;
	}
	public Date getChrg_time_end() {
		return chrg_time_end;
	}
	public void setChrg_time_end(Date chrg_time_end) {
		this.chrg_time_end = chrg_time_end;
	}
}

package com.ebmi.std.clm.cond;

import java.util.Date;

public class ClmInfoCondition {

	/**
	 * 结算单ID
	 */
	private String clm_id;

	/**
	 * 原始结算单ID
	 */
	private String orig_clm_code;

	/**
	 * 参保人ID
	 */
	private String p_mi_id;

	/**
	 * 结算时间start
	 */
	private Date clm_date_start;

	/**
	 * 结算时间end
	 */
	private Date clm_date_end;

	/**
	 * ETL批次
	 */
	private String etl_batch;

	/**
	 * 排序字段
	 */
	private String order_by;

	/**
	 * 起始记录
	 */
	private Integer recordStart;

	/**
	 * 获取条数
	 */
	private Integer recordCount;
	
	/**
	 * 就医方式
	 */
	private String medType;

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

	public String getEtl_batch() {
		return etl_batch;
	}

	public void setEtl_batch(String etl_batch) {
		this.etl_batch = etl_batch;
	}

	public Date getClm_date_start() {
		return clm_date_start;
	}

	public void setClm_date_start(Date clm_date_start) {
		this.clm_date_start = clm_date_start;
	}

	public Date getClm_date_end() {
		return clm_date_end;
	}

	public void setClm_date_end(Date clm_date_end) {
		this.clm_date_end = clm_date_end;
	}

	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	public Integer getRecordStart() {
		return recordStart;
	}

	public void setRecordStart(Integer recordStart) {
		this.recordStart = recordStart;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public String getMedType() {
		return medType;
	}

	public void setMedType(String medType) {
		this.medType = medType;
	}
	
	

}

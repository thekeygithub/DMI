package com.ebmi.std.clm.dto;

import java.util.Date;

/**
 * 结算单收费类别汇总实体类
 * @author yanjie.ji
 * @date 2016-1-5
 * @time 下午1:15:33
 *
 */
public class PClaimChrgSum {
	
	private String clm_id;
	private Integer chrg_type_id;
	private String p_mi_id;
	private Double fee;
	private Double out_of_pock;
	private Double copay;
	private Date upd_time;
	private Date crt_time;
	private String orig_dbid;
	private String etl_batch;
	private Date etl_time;
	
	public String getClm_id() {
		return clm_id;
	}
	public void setClm_id(String clm_id) {
		this.clm_id = clm_id;
	}
	public Integer getChrg_type_id() {
		return chrg_type_id;
	}
	public void setChrg_type_id(Integer chrg_type_id) {
		this.chrg_type_id = chrg_type_id;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
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
	public String getOrig_dbid() {
		return orig_dbid;
	}
	public void setOrig_dbid(String orig_dbid) {
		this.orig_dbid = orig_dbid;
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

package com.ebmi.std.clm.dto;

import java.util.Date;
/**
 * 个人结算单诊断标化
 * @author yanjie.ji
 * @date 2016-1-4
 * @time 下午5:35:54
 *
 */
public class PClaimDiagStd {

	private String claim_diag_id;
	private String clm_id;
	private String p_mi_id;
	private String diag_col_name;
	private String nlp_diag;
	private String diag_id;
	private Integer diag_ord;
	private Date upd_time;
	private Date crt_time;
	private String etl_batch;
	private Date etl_time;
	public String getClaim_diag_id() {
		return claim_diag_id;
	}
	public void setClaim_diag_id(String claim_diag_id) {
		this.claim_diag_id = claim_diag_id;
	}
	public String getClm_id() {
		return clm_id;
	}
	public void setClm_id(String clm_id) {
		this.clm_id = clm_id;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getDiag_col_name() {
		return diag_col_name;
	}
	public void setDiag_col_name(String diag_col_name) {
		this.diag_col_name = diag_col_name;
	}
	public String getNlp_diag() {
		return nlp_diag;
	}
	public void setNlp_diag(String nlp_diag) {
		this.nlp_diag = nlp_diag;
	}
	public String getDiag_id() {
		return diag_id;
	}
	public void setDiag_id(String diag_id) {
		this.diag_id = diag_id;
	}
	public Integer getDiag_ord() {
		return diag_ord;
	}
	public void setDiag_ord(Integer diag_ord) {
		this.diag_ord = diag_ord;
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

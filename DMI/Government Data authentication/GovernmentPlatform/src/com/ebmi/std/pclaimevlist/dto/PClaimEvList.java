package com.ebmi.std.pclaimevlist.dto;

import java.util.Date;

public class PClaimEvList {

	/**结算单id*/
	private String clm_id;
	/**参保人id*/
	private String p_mi_id;
	/**企业id*/
	private String ent_id;
	/**企业名称*/
	private String ent_name;
	/**就诊类型*/
	private Integer med_type_id;
	/**就诊开始时间*/
	private Date med_date;
	/**就诊结束时间*/
	private Date med_date_end;
	/**评价列表状态*/
	private Integer ev_list_stat_id;
	/**更新时间*/
	private Date upd_time;
	/**创建时间*/
	private Date crt_time;
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
	public String getEnt_id() {
		return ent_id;
	}
	public void setEnt_id(String ent_id) {
		this.ent_id = ent_id;
	}
	public String getEnt_name() {
		return ent_name;
	}
	public void setEnt_name(String ent_name) {
		this.ent_name = ent_name;
	}
	public Integer getMed_type_id() {
		return med_type_id;
	}
	public void setMed_type_id(Integer med_type_id) {
		this.med_type_id = med_type_id;
	}
	public Date getMed_date() {
		return med_date;
	}
	public void setMed_date(Date med_date) {
		this.med_date = med_date;
	}
	public Date getMed_date_end() {
		return med_date_end;
	}
	public void setMed_date_end(Date med_date_end) {
		this.med_date_end = med_date_end;
	}
	public Integer getEv_list_stat_id() {
		return ev_list_stat_id;
	}
	public void setEv_list_stat_id(Integer ev_list_stat_id) {
		this.ev_list_stat_id = ev_list_stat_id;
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
	
}

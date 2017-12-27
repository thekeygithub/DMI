package com.ebmi.std.pbase.dto;

import java.util.Date;

/**
 * 参保人社保情况
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:10:22
 *
 */
public class PSiStat {
	private String dbid;
	private String p_mi_id;
	private Integer si_type_id;
	private Integer coll_stat_id;
	private Integer tot_coll_month;
	private Integer conti_coll_month;
	private String si_org_id;
	private String si_org_name;
	private String regn_tc_id;
	private Date upd_time;
	private Date crt_time;
	private String etl_batch;
	private Date etl_time;
	
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public Integer getSi_type_id() {
		return si_type_id;
	}
	public void setSi_type_id(Integer si_type_id) {
		this.si_type_id = si_type_id;
	}
	public Integer getColl_stat_id() {
		return coll_stat_id;
	}
	public void setColl_stat_id(Integer coll_stat_id) {
		this.coll_stat_id = coll_stat_id;
	}
	public Integer getTot_coll_month() {
		return tot_coll_month;
	}
	public void setTot_coll_month(Integer tot_coll_month) {
		this.tot_coll_month = tot_coll_month;
	}
	public Integer getConti_coll_month() {
		return conti_coll_month;
	}
	public void setConti_coll_month(Integer conti_coll_month) {
		this.conti_coll_month = conti_coll_month;
	}
	public String getSi_org_id() {
		return si_org_id;
	}
	public void setSi_org_id(String si_org_id) {
		this.si_org_id = si_org_id;
	}
	public String getSi_org_name() {
		return si_org_name;
	}
	public void setSi_org_name(String si_org_name) {
		this.si_org_name = si_org_name;
	}
	public String getRegn_tc_id() {
		return regn_tc_id;
	}
	public void setRegn_tc_id(String regn_tc_id) {
		this.regn_tc_id = regn_tc_id;
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

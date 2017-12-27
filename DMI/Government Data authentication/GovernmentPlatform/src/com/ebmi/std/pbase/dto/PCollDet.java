package com.ebmi.std.pbase.dto;

import java.util.Date;

/**
 * 参保人缴费明细
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:13:36
 *
 */
public class PCollDet {
	private String coll_det_id;
	private String p_mi_id;
	private String coll_mon;
	private Date arrive_date;
	private String si_cat_id;
	private String si_org_id;
	private String si_org_name;
	private String regn_tc_id;
	private String coll_regn_id;
	private Integer coll_stat_id;
	private String coll_type_id;
	private Double coll_base;//缴费基数
	private String coll_lvl_id;
	private Double org_coll;//单位缴费
	private Double self_coll;
	private Double other_coll;
	private Integer low_live_flag;
	private Date upd_time;
	private Date crt_time;
	private String etl_batch;
	private Date etl_time;
	
	//xuhai add 2016-5-17 17:36:43
	//private String coll_base;//缴费基数
	//private String org_coll;//单位缴费
	private String month;//应缴属期
	private String self_num;//个人编号
	private String treat_type;//待遇类别
	private Double big_pay_coll;//大病缴费金额
    private String coll_stat;// 缴费状态
    /**
     * 灵活就业人员，新增字段：缴费金额=单位缴费+个人缴费+大额医疗缴费
     * 居民：缴费金额为接口中：应缴合计
     */
    private Double total_pay_coll;//total_pay_coll	缴费金额
  //xuhai add 2016-5-17 17:36:43 end z
    
	public String getColl_stat() {
		return coll_stat;
	}
	public Double getTotal_pay_coll() {
		return total_pay_coll;
	}
	public void setTotal_pay_coll(Double total_pay_coll) {
		this.total_pay_coll = total_pay_coll;
	}
	public void setColl_stat(String coll_stat) {
		this.coll_stat = coll_stat;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getSelf_num() {
		return self_num;
	}
	public void setSelf_num(String self_num) {
		this.self_num = self_num;
	}
	public String getTreat_type() {
		return treat_type;
	}
	public void setTreat_type(String treat_type) {
		this.treat_type = treat_type;
	}
	public Double getBig_pay_coll() {
		return big_pay_coll;
	}
	public void setBig_pay_coll(Double big_pay_coll) {
		this.big_pay_coll = big_pay_coll;
	}
	public String getColl_det_id() {
		return coll_det_id;
	}
	public void setColl_det_id(String coll_det_id) {
		this.coll_det_id = coll_det_id;
	}
	public String getP_mi_id() {
		return p_mi_id;
	}
	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}
	public String getColl_mon() {
		return coll_mon;
	}
	public void setColl_mon(String coll_mon) {
		this.coll_mon = coll_mon;
	}
	public Date getArrive_date() {
		return arrive_date;
	}
	public void setArrive_date(Date arrive_date) {
		this.arrive_date = arrive_date;
	}
	public String getSi_cat_id() {
		return si_cat_id;
	}
	public void setSi_cat_id(String si_cat_id) {
		this.si_cat_id = si_cat_id;
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
	public String getColl_regn_id() {
		return coll_regn_id;
	}
	public void setColl_regn_id(String coll_regn_id) {
		this.coll_regn_id = coll_regn_id;
	}
	public Integer getColl_stat_id() {
		return coll_stat_id;
	}
	public void setColl_stat_id(Integer coll_stat_id) {
		this.coll_stat_id = coll_stat_id;
	}
	public Double getColl_base() {
		return coll_base;
	}
	public void setColl_base(Double coll_base) {
		this.coll_base = coll_base;
	}
	public String getColl_lvl_id() {
		return coll_lvl_id;
	}
	public void setColl_lvl_id(String coll_lvl_id) {
		this.coll_lvl_id = coll_lvl_id;
	}
	public Double getOrg_coll() {
		return org_coll;
	}
	public void setOrg_coll(Double org_coll) {
		this.org_coll = org_coll;
	}
	public Double getSelf_coll() {
		return self_coll;
	}
	public void setSelf_coll(Double self_coll) {
		this.self_coll = self_coll;
	}
	public Double getOther_coll() {
		return other_coll;
	}
	public void setOther_coll(Double other_coll) {
		this.other_coll = other_coll;
	}
	public Integer getLow_live_flag() {
		return low_live_flag;
	}
	public void setLow_live_flag(Integer low_live_flag) {
		this.low_live_flag = low_live_flag;
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
	public String getColl_type_id() {
		return coll_type_id;
	}
	public void setColl_type_id(String coll_type_id) {
		this.coll_type_id = coll_type_id;
	}
}

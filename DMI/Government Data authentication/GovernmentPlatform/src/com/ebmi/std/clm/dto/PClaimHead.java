package com.ebmi.std.clm.dto;

import java.util.Date;

/**
 * 个人就诊结算单
 * @author yanjie.ji
 * @date 2015-12-22
 * @time 上午11:00:28
 *
 */
public class PClaimHead{
	private String clm_id;
	private String orig_clm_code;
	private String mic_id;
	private String p_mi_id;
	private String ent_id;
	private Integer ent_type_id;
	private String ent_mi_code;
	private String orig_ent_name;
	private Integer med_type_id;
	private String orig_med_type_id;
	private Integer p_mi_cat_id;
	private Integer p_mi_type_id;
	private Integer clm_type_id;
	private Date clm_date;
	private Date med_date;
	private Date entr_date;
	private Date disch_date;
	private String orig_dr_name;
	private String hosp_dept;
	private String oup_diag;
	private String sd_desc;
	private String sd_diag;
	private String inp_in_diag;
	private String inp_out_diag;
	private Double pay_tot;
	private Double p_acct_left;
	private Double pay_p_acct;
	private Double pay_p_cur_acct;
	private Double pay_p_his_acct;
	private Double pay_c_tot;
	private Double pay_c_out_of_pock;
	private Double pay_c_copay;
	private Double pay_mi_scope;
	private Double deduc;
	private Double reimb_ratio;
	private Double pay_tc_para;
	private Double pay_f_tot;
	private Double pay_fa_tot;
	private Double pay_fa_a;
	private Double pay_fa_b;
	private Double pay_fa_c;
	private Double pay_fb_tot;
	private Double pay_fb_a;
	private Double pay_fb_b;
	private Double pay_fb_c;
	private Double pay_fb_d;
	private Double pay_fb_e;
	private Double pay_fc_tot;
	private Double pay_fc_a;
	private Double pay_fc_b;
	private Double pay_fc_c;
	private Double pay_fc_d;
	private Double pay_fc_e;
	private Double pay_bk1;
	private Double pay_bk2;
	private Double pay_bk3;
	private String etl_batch;
	private Date etl_time;
	private Date crt_time;
	private Date upd_time;
	private Double drug_fee;
	private Double exam_fee;
	private Double csum_fee;
	
	private String personNum ;
	
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
	public String getMic_id() {
		return mic_id;
	}
	public void setMic_id(String mic_id) {
		this.mic_id = mic_id;
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
	public Integer getEnt_type_id() {
		return ent_type_id;
	}
	public void setEnt_type_id(Integer ent_type_id) {
		this.ent_type_id = ent_type_id;
	}
	public String getEnt_mi_code() {
		return ent_mi_code;
	}
	public void setEnt_mi_code(String ent_mi_code) {
		this.ent_mi_code = ent_mi_code;
	}
	public String getOrig_ent_name() {
		return orig_ent_name;
	}
	public void setOrig_ent_name(String orig_ent_name) {
		this.orig_ent_name = orig_ent_name;
	}
	public Integer getMed_type_id() {
		return med_type_id;
	}
	public void setMed_type_id(Integer med_type_id) {
		this.med_type_id = med_type_id;
	}
	public String getOrig_med_type_id() {
		return orig_med_type_id;
	}
	public void setOrig_med_type_id(String orig_med_type_id) {
		this.orig_med_type_id = orig_med_type_id;
	}
	public Integer getP_mi_cat_id() {
		return p_mi_cat_id;
	}
	public void setP_mi_cat_id(Integer p_mi_cat_id) {
		this.p_mi_cat_id = p_mi_cat_id;
	}
	public Integer getP_mi_type_id() {
		return p_mi_type_id;
	}
	public void setP_mi_type_id(Integer p_mi_type_id) {
		this.p_mi_type_id = p_mi_type_id;
	}
	public Integer getClm_type_id() {
		return clm_type_id;
	}
	public void setClm_type_id(Integer clm_type_id) {
		this.clm_type_id = clm_type_id;
	}
	public Date getClm_date() {
		return clm_date;
	}
	public void setClm_date(Date clm_date) {
		this.clm_date = clm_date;
	}
	public Date getMed_date() {
		return med_date;
	}
	public void setMed_date(Date med_date) {
		this.med_date = med_date;
	}
	public Date getEntr_date() {
		return entr_date;
	}
	public void setEntr_date(Date entr_date) {
		this.entr_date = entr_date;
	}
	public Date getDisch_date() {
		return disch_date;
	}
	public void setDisch_date(Date disch_date) {
		this.disch_date = disch_date;
	}
	public String getOrig_dr_name() {
		return orig_dr_name;
	}
	public void setOrig_dr_name(String orig_dr_name) {
		this.orig_dr_name = orig_dr_name;
	}
	public String getHosp_dept() {
		return hosp_dept;
	}
	public void setHosp_dept(String hosp_dept) {
		this.hosp_dept = hosp_dept;
	}
	public String getOup_diag() {
		return oup_diag;
	}
	public void setOup_diag(String oup_diag) {
		this.oup_diag = oup_diag;
	}
	public String getSd_desc() {
		return sd_desc;
	}
	public void setSd_desc(String sd_desc) {
		this.sd_desc = sd_desc;
	}
	public String getSd_diag() {
		return sd_diag;
	}
	public void setSd_diag(String sd_diag) {
		this.sd_diag = sd_diag;
	}
	public String getInp_in_diag() {
		return inp_in_diag;
	}
	public void setInp_in_diag(String inp_in_diag) {
		this.inp_in_diag = inp_in_diag;
	}
	public String getInp_out_diag() {
		return inp_out_diag;
	}
	public void setInp_out_diag(String inp_out_diag) {
		this.inp_out_diag = inp_out_diag;
	}
	public Double getPay_tot() {
		return pay_tot;
	}
	public void setPay_tot(Double pay_tot) {
		this.pay_tot = pay_tot;
	}
	public Double getP_acct_left() {
		return p_acct_left;
	}
	public void setP_acct_left(Double p_acct_left) {
		this.p_acct_left = p_acct_left;
	}
	public Double getPay_p_acct() {
		return pay_p_acct;
	}
	public void setPay_p_acct(Double pay_p_acct) {
		this.pay_p_acct = pay_p_acct;
	}
	public Double getPay_p_cur_acct() {
		return pay_p_cur_acct;
	}
	public void setPay_p_cur_acct(Double pay_p_cur_acct) {
		this.pay_p_cur_acct = pay_p_cur_acct;
	}
	public Double getPay_p_his_acct() {
		return pay_p_his_acct;
	}
	public void setPay_p_his_acct(Double pay_p_his_acct) {
		this.pay_p_his_acct = pay_p_his_acct;
	}
	public Double getPay_c_tot() {
		return pay_c_tot;
	}
	public void setPay_c_tot(Double pay_c_tot) {
		this.pay_c_tot = pay_c_tot;
	}
	public Double getPay_c_out_of_pock() {
		return pay_c_out_of_pock;
	}
	public void setPay_c_out_of_pock(Double pay_c_out_of_pock) {
		this.pay_c_out_of_pock = pay_c_out_of_pock;
	}
	public Double getPay_c_copay() {
		return pay_c_copay;
	}
	public void setPay_c_copay(Double pay_c_copay) {
		this.pay_c_copay = pay_c_copay;
	}
	public Double getPay_mi_scope() {
		return pay_mi_scope;
	}
	public void setPay_mi_scope(Double pay_mi_scope) {
		this.pay_mi_scope = pay_mi_scope;
	}
	public Double getDeduc() {
		return deduc;
	}
	public void setDeduc(Double deduc) {
		this.deduc = deduc;
	}
	public Double getReimb_ratio() {
		return reimb_ratio;
	}
	public void setReimb_ratio(Double reimb_ratio) {
		this.reimb_ratio = reimb_ratio;
	}
	public Double getPay_f_tot() {
		return pay_f_tot;
	}
	public void setPay_f_tot(Double pay_f_tot) {
		this.pay_f_tot = pay_f_tot;
	}
	public Double getPay_fa_tot() {
		return pay_fa_tot;
	}
	public void setPay_fa_tot(Double pay_fa_tot) {
		this.pay_fa_tot = pay_fa_tot;
	}
	public Double getPay_fa_a() {
		return pay_fa_a;
	}
	public void setPay_fa_a(Double pay_fa_a) {
		this.pay_fa_a = pay_fa_a;
	}
	public Double getPay_fa_b() {
		return pay_fa_b;
	}
	public void setPay_fa_b(Double pay_fa_b) {
		this.pay_fa_b = pay_fa_b;
	}
	public Double getPay_fa_c() {
		return pay_fa_c;
	}
	public void setPay_fa_c(Double pay_fa_c) {
		this.pay_fa_c = pay_fa_c;
	}
	public Double getPay_fb_tot() {
		return pay_fb_tot;
	}
	public void setPay_fb_tot(Double pay_fb_tot) {
		this.pay_fb_tot = pay_fb_tot;
	}
	public Double getPay_fb_a() {
		return pay_fb_a;
	}
	public void setPay_fb_a(Double pay_fb_a) {
		this.pay_fb_a = pay_fb_a;
	}
	public Double getPay_fb_b() {
		return pay_fb_b;
	}
	public void setPay_fb_b(Double pay_fb_b) {
		this.pay_fb_b = pay_fb_b;
	}
	public Double getPay_fb_c() {
		return pay_fb_c;
	}
	public void setPay_fb_c(Double pay_fb_c) {
		this.pay_fb_c = pay_fb_c;
	}
	public Double getPay_fb_d() {
		return pay_fb_d;
	}
	public void setPay_fb_d(Double pay_fb_d) {
		this.pay_fb_d = pay_fb_d;
	}
	public Double getPay_fb_e() {
		return pay_fb_e;
	}
	public void setPay_fb_e(Double pay_fb_e) {
		this.pay_fb_e = pay_fb_e;
	}
	public Double getPay_fc_tot() {
		return pay_fc_tot;
	}
	public void setPay_fc_tot(Double pay_fc_tot) {
		this.pay_fc_tot = pay_fc_tot;
	}
	public Double getPay_fc_a() {
		return pay_fc_a;
	}
	public void setPay_fc_a(Double pay_fc_a) {
		this.pay_fc_a = pay_fc_a;
	}
	public Double getPay_fc_b() {
		return pay_fc_b;
	}
	public void setPay_fc_b(Double pay_fc_b) {
		this.pay_fc_b = pay_fc_b;
	}
	public Double getPay_fc_c() {
		return pay_fc_c;
	}
	public void setPay_fc_c(Double pay_fc_c) {
		this.pay_fc_c = pay_fc_c;
	}
	public Double getPay_fc_d() {
		return pay_fc_d;
	}
	public void setPay_fc_d(Double pay_fc_d) {
		this.pay_fc_d = pay_fc_d;
	}
	public Double getPay_fc_e() {
		return pay_fc_e;
	}
	public void setPay_fc_e(Double pay_fc_e) {
		this.pay_fc_e = pay_fc_e;
	}
	public Double getPay_bk1() {
		return pay_bk1;
	}
	public void setPay_bk1(Double pay_bk1) {
		this.pay_bk1 = pay_bk1;
	}
	public Double getPay_bk2() {
		return pay_bk2;
	}
	public void setPay_bk2(Double pay_bk2) {
		this.pay_bk2 = pay_bk2;
	}
	public Double getPay_bk3() {
		return pay_bk3;
	}
	public void setPay_bk3(Double pay_bk3) {
		this.pay_bk3 = pay_bk3;
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
	public Date getCrt_time() {
		return crt_time;
	}
	public void setCrt_time(Date crt_time) {
		this.crt_time = crt_time;
	}
	public Date getUpd_time() {
		return upd_time;
	}
	public void setUpd_time(Date upd_time) {
		this.upd_time = upd_time;
	}
	public Double getPay_tc_para() {
		return pay_tc_para;
	}
	public void setPay_tc_para(Double pay_tc_para) {
		this.pay_tc_para = pay_tc_para;
	}
	public Double getDrug_fee() {
		return drug_fee;
	}
	public void setDrug_fee(Double drug_fee) {
		this.drug_fee = drug_fee;
	}
	public Double getExam_fee() {
		return exam_fee;
	}
	public void setExam_fee(Double exam_fee) {
		this.exam_fee = exam_fee;
	}
	public Double getCsum_fee() {
		return csum_fee;
	}
	public void setCsum_fee(Double csum_fee) {
		this.csum_fee = csum_fee;
	}
	public String getPersonNum() {
		return personNum;
	}
	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}
}

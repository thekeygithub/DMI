package com.ebmi.std.pbase.dto;

import java.util.Date;

import com.ebmi.jms.entity.ViewGrjbxxGs;

/**
 * 参保人基本信息
 * 
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午4:04:11
 */
public class PMiBaseInfo {
	//xuhai add 2016-5-17 10:20:01
	private String p_edu;//文化程度
	private String p_identity;//个人身份
	private String p_gend_name;//性别 名称 
	private String  p_ethnic_ame;//民族 名称 
	private String  p_si_cat_name;//参保类别=待遇类别
	private String regn_tc_name;//统筹区
	private Date p_cb_date;//参保日期
	private String p_cb_stat;//参保状态
	//xuhai add 2016-5-17 10:20:01 end
	
	private String p_mi_id;
	private String orig_p_mi_id;
	private String mi_p_code;
	private String mic_id;
	private Integer emp_city_flag;
	private Integer p_si_cat_id;
	private String si_card_no;
	private String p_name;
	private Integer p_gend_id;
	private Integer p_ethnic_id;
	private Integer hukou_type_id;
	private Date p_birth;
	private Date begin_date;
	private Integer valid_flag;
	private String p_addr;
	private String addr_post_code;//居住地邮编
	private Integer p_cert_type_id;
	private String p_cert_no;
	private String phone;
	private String mobile;
	private String p_corp;
	private String image_id;
	private Integer p_sd_flag;
	private String sd_desc;
	private String sd_diag;
	private Date work_time;
	private Integer retire_flag;
	private Date retire_time;
	private Integer edu_lvl_id;
	private String hukou_addr;
	private String hukou_post_code;
	private String email;
	private String aab001;
	private String agency_id;
	private String aab034;
	private Long tm_role_id;
	private String regn_tc_id;
	private Integer mi_treat_type_id;
	private Integer mi_treat_stat_id;
	private Integer mi_self_sup_flag;
	private Integer mi_self_flag;
	private Integer endow_treat_stat_id;
	private Double endow_acct_tot;
	private Integer endow_mon_tot;
	private Date upd_time;
	private Date crt_time;
	private String etl_batch;
	private Date etl_time;
	private String p_num ;
 
	private String nationality;
	
	// 工伤生育
	private ViewGrjbxxGs sybx ;
	private ViewGrjbxxGs gsbx ;

	
	
	

	public String getP_num() {
		return p_num;
	}

	public void setP_num(String p_num) {
		this.p_num = p_num;
	}

	public ViewGrjbxxGs getSybx() {
		return sybx;
	}

	public void setSybx(ViewGrjbxxGs sybx) {
		this.sybx = sybx;
	}

	public ViewGrjbxxGs getGsbx() {
		return gsbx;
	}

	public void setGsbx(ViewGrjbxxGs gsbx) {
		this.gsbx = gsbx;
	}

	public Date getP_cb_date() {
		return p_cb_date;
	}

	public void setP_cb_date(Date p_cb_date) {
		this.p_cb_date = p_cb_date;
	}

	

	public String getP_cb_stat() {
		return p_cb_stat;
	}

	public void setP_cb_stat(String p_cb_stat) {
		this.p_cb_stat = p_cb_stat;
	}

	public String getRegn_tc_name() {
		return regn_tc_name;
	}

	public void setRegn_tc_name(String regn_tc_name) {
		this.regn_tc_name = regn_tc_name;
	}

	public String getP_si_cat_name() {
		return p_si_cat_name;
	}

	public void setP_si_cat_name(String p_si_cat_name) {
		this.p_si_cat_name = p_si_cat_name;
	}

	public String getP_edu() {
		return p_edu;
	}

	public void setP_edu(String p_edu) {
		this.p_edu = p_edu;
	}

	public String getP_identity() {
		return p_identity;
	}

	public void setP_identity(String p_identity) {
		this.p_identity = p_identity;
	}

	public String getP_mi_id() {
		return p_mi_id;
	}

	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}

	public String getOrig_p_mi_id() {
		return orig_p_mi_id;
	}

	public void setOrig_p_mi_id(String orig_p_mi_id) {
		this.orig_p_mi_id = orig_p_mi_id;
	}

	public String getMi_p_code() {
		return mi_p_code;
	}

	public void setMi_p_code(String mi_p_code) {
		this.mi_p_code = mi_p_code;
	}

	public String getMic_id() {
		return mic_id;
	}

	public void setMic_id(String mic_id) {
		this.mic_id = mic_id;
	}

	public Integer getEmp_city_flag() {
		return emp_city_flag;
	}

	public void setEmp_city_flag(Integer emp_city_flag) {
		this.emp_city_flag = emp_city_flag;
	}

	public Integer getP_si_cat_id() {
		return p_si_cat_id;
	}

	public void setP_si_cat_id(Integer p_si_cat_id) {
		this.p_si_cat_id = p_si_cat_id;
	}

	public String getSi_card_no() {
		return si_card_no;
	}

	public void setSi_card_no(String si_card_no) {
		this.si_card_no = si_card_no;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public Integer getP_gend_id() {
		return p_gend_id;
	}

	public String getP_gend_name() {
		return p_gend_name;
	}

	public void setP_gend_name(String p_gend_name) {
		this.p_gend_name = p_gend_name;
	}

	public void setP_gend_id(Integer p_gend_id) {
		this.p_gend_id = p_gend_id;
	}

	public Integer getP_ethnic_id() {
		return p_ethnic_id;
	}

	public String getP_ethnic_ame() {
		return p_ethnic_ame;
	}

	public void setP_ethnic_ame(String p_ethnic_ame) {
		this.p_ethnic_ame = p_ethnic_ame;
	}

	public void setP_ethnic_id(Integer p_ethnic_id) {
		this.p_ethnic_id = p_ethnic_id;
	}

	public Integer getHukou_type_id() {
		return hukou_type_id;
	}

	public void setHukou_type_id(Integer hukou_type_id) {
		this.hukou_type_id = hukou_type_id;
	}

	public Date getP_birth() {
		return p_birth;
	}

	public void setP_birth(Date p_birth) {
		this.p_birth = p_birth;
	}

	public Date getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}

	public Integer getValid_flag() {
		return valid_flag;
	}

	public void setValid_flag(Integer valid_flag) {
		this.valid_flag = valid_flag;
	}

	public String getP_addr() {
		return p_addr;
	}

	public void setP_addr(String p_addr) {
		this.p_addr = p_addr;
	}

	public String getAddr_post_code() {
		return addr_post_code;
	}

	public void setAddr_post_code(String addr_post_code) {
		this.addr_post_code = addr_post_code;
	}

	public Integer getP_cert_type_id() {
		return p_cert_type_id;
	}

	public void setP_cert_type_id(Integer p_cert_type_id) {
		this.p_cert_type_id = p_cert_type_id;
	}

	public String getP_cert_no() {
		return p_cert_no;
	}

	public void setP_cert_no(String p_cert_no) {
		this.p_cert_no = p_cert_no;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getP_corp() {
		return p_corp;
	}

	public void setP_corp(String p_corp) {
		this.p_corp = p_corp;
	}

	public String getImage_id() {
		return image_id;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public Integer getP_sd_flag() {
		return p_sd_flag;
	}

	public void setP_sd_flag(Integer p_sd_flag) {
		this.p_sd_flag = p_sd_flag;
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

	public Date getWork_time() {
		return work_time;
	}

	public void setWork_time(Date work_time) {
		this.work_time = work_time;
	}

	public Integer getRetire_flag() {
		return retire_flag;
	}

	public void setRetire_flag(Integer retire_flag) {
		this.retire_flag = retire_flag;
	}

	public Date getRetire_time() {
		return retire_time;
	}

	public void setRetire_time(Date retire_time) {
		this.retire_time = retire_time;
	}

	public Integer getEdu_lvl_id() {
		return edu_lvl_id;
	}

	public void setEdu_lvl_id(Integer edu_lvl_id) {
		this.edu_lvl_id = edu_lvl_id;
	}

	public String getHukou_addr() {
		return hukou_addr;
	}

	public void setHukou_addr(String hukou_addr) {
		this.hukou_addr = hukou_addr;
	}

	public String getHukou_post_code() {
		return hukou_post_code;
	}

	public void setHukou_post_code(String hukou_post_code) {
		this.hukou_post_code = hukou_post_code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAab001() {
		return aab001;
	}

	public void setAab001(String aab001) {
		this.aab001 = aab001;
	}

	public String getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(String agency_id) {
		this.agency_id = agency_id;
	}

	public String getAab034() {
		return aab034;
	}

	public void setAab034(String aab034) {
		this.aab034 = aab034;
	}

	public Long getTm_role_id() {
		return tm_role_id;
	}

	public void setTm_role_id(Long tm_role_id) {
		this.tm_role_id = tm_role_id;
	}

	public String getRegn_tc_id() {
		return regn_tc_id;
	}

	public void setRegn_tc_id(String regn_tc_id) {
		this.regn_tc_id = regn_tc_id;
	}

	public Integer getMi_treat_type_id() {
		return mi_treat_type_id;
	}

	public void setMi_treat_type_id(Integer mi_treat_type_id) {
		this.mi_treat_type_id = mi_treat_type_id;
	}

	public Integer getMi_treat_stat_id() {
		return mi_treat_stat_id;
	}

	public void setMi_treat_stat_id(Integer mi_treat_stat_id) {
		this.mi_treat_stat_id = mi_treat_stat_id;
	}

	public Integer getMi_self_sup_flag() {
		return mi_self_sup_flag;
	}

	public void setMi_self_sup_flag(Integer mi_self_sup_flag) {
		this.mi_self_sup_flag = mi_self_sup_flag;
	}

	public Integer getMi_self_flag() {
		return mi_self_flag;
	}

	public void setMi_self_flag(Integer mi_self_flag) {
		this.mi_self_flag = mi_self_flag;
	}

	public Integer getEndow_treat_stat_id() {
		return endow_treat_stat_id;
	}

	public void setEndow_treat_stat_id(Integer endow_treat_stat_id) {
		this.endow_treat_stat_id = endow_treat_stat_id;
	}

	public Double getEndow_acct_tot() {
		return endow_acct_tot;
	}

	public void setEndow_acct_tot(Double endow_acct_tot) {
		this.endow_acct_tot = endow_acct_tot;
	}

	public Integer getEndow_mon_tot() {
		return endow_mon_tot;
	}

	public void setEndow_mon_tot(Integer endow_mon_tot) {
		this.endow_mon_tot = endow_mon_tot;
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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
}

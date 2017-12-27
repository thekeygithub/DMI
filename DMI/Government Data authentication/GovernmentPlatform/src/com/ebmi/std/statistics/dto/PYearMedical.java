/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.statistics.dto;

import java.sql.Date;

/**
 * 个人就诊年度统计信息
 * 
 * @author xiangfeng.guan
 */
public class PYearMedical {

	private String p_mi_id;// 参保人ID

	private String year;// 年份

	private Double tot_cost;// 就诊总费用

	private Integer tot_num;// 就诊总次数

	private Double pay_a;// A类支付

	private Double pay_b;// B类支付

	private Double pay_c;// C类支付

	private Double pay_d;// D类支付

	private Double drug_fee;// 药品费用

	private Double exam_fee;// 诊疗费用

	private Double csum_fee;// 材料费用

	private Double inp_fee;// 住院费用

	private Double ous_fee;// 门特费用

	private Double oup_fee;// 门诊费用

	private Double shop_fee;// 药店购药费用

	private Integer inp_cnt;// 住院次数

	private Integer oup_cnt;// 门诊次数

	private Integer ous_cnt;// 门特次数

	private Integer shop_cnt;// 药店购药次数

	private Date upd_time;// 更新时间

	private Date crt_time;// 创建时间
	
	private double pay_tot ;
	
	private double pay_fa_tot ;
	private double pay_fa_a ;
	private double pay_fa_b ;
	private double pay_fa_c ;
	
	private double pay_fb_tot ;
	private double pay_fb_a ;
	private double pay_fb_b ;

	public String getP_mi_id() {
		return p_mi_id;
	}

	public void setP_mi_id(String p_mi_id) {
		this.p_mi_id = p_mi_id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Double getTot_cost() {
		return tot_cost;
	}

	public void setTot_cost(Double tot_cost) {
		this.tot_cost = tot_cost;
	}

	public Integer getTot_num() {
		return tot_num;
	}

	public void setTot_num(Integer tot_num) {
		this.tot_num = tot_num;
	}

	public Double getPay_a() {
		return pay_a;
	}

	public void setPay_a(Double pay_a) {
		this.pay_a = pay_a;
	}

	public Double getPay_b() {
		return pay_b;
	}

	public void setPay_b(Double pay_b) {
		this.pay_b = pay_b;
	}

	public Double getPay_c() {
		return pay_c;
	}

	public void setPay_c(Double pay_c) {
		this.pay_c = pay_c;
	}

	public Double getPay_d() {
		return pay_d;
	}

	public void setPay_d(Double pay_d) {
		this.pay_d = pay_d;
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

	public Double getInp_fee() {
		return inp_fee;
	}

	public void setInp_fee(Double inp_fee) {
		this.inp_fee = inp_fee;
	}

	public Double getOus_fee() {
		return ous_fee;
	}

	public void setOus_fee(Double ous_fee) {
		this.ous_fee = ous_fee;
	}

	public Double getOup_fee() {
		return oup_fee;
	}

	public void setOup_fee(Double oup_fee) {
		this.oup_fee = oup_fee;
	}

	public Double getShop_fee() {
		return shop_fee;
	}

	public void setShop_fee(Double shop_fee) {
		this.shop_fee = shop_fee;
	}

	public Integer getInp_cnt() {
		return inp_cnt;
	}

	public void setInp_cnt(Integer inp_cnt) {
		this.inp_cnt = inp_cnt;
	}

	public Integer getOup_cnt() {
		return oup_cnt;
	}

	public void setOup_cnt(Integer oup_cnt) {
		this.oup_cnt = oup_cnt;
	}

	public Integer getOus_cnt() {
		return ous_cnt;
	}

	public void setOus_cnt(Integer ous_cnt) {
		this.ous_cnt = ous_cnt;
	}

	public Integer getShop_cnt() {
		return shop_cnt;
	}

	public void setShop_cnt(Integer shop_cnt) {
		this.shop_cnt = shop_cnt;
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

	public double getPay_fa_a() {
		return pay_fa_a;
	}

	public void setPay_fa_a(double pay_fa_a) {
		this.pay_fa_a = pay_fa_a;
	}

	public double getPay_fa_b() {
		return pay_fa_b;
	}

	public void setPay_fa_b(double pay_fa_b) {
		this.pay_fa_b = pay_fa_b;
	}

	public double getPay_fa_c() {
		return pay_fa_c;
	}

	public void setPay_fa_c(double pay_fa_c) {
		this.pay_fa_c = pay_fa_c;
	}

	public double getPay_fb_a() {
		return pay_fb_a;
	}

	public void setPay_fb_a(double pay_fb_a) {
		this.pay_fb_a = pay_fb_a;
	}

	public double getPay_fb_b() {
		return pay_fb_b;
	}

	public void setPay_fb_b(double pay_fb_b) {
		this.pay_fb_b = pay_fb_b;
	}

	public double getPay_tot() {
		return pay_tot;
	}

	public void setPay_tot(double pay_tot) {
		this.pay_tot = pay_tot;
	}

	public double getPay_fa_tot() {
		return pay_fa_tot;
	}

	public void setPay_fa_tot(double pay_fa_tot) {
		this.pay_fa_tot = pay_fa_tot;
	}

	public double getPay_fb_tot() {
		return pay_fb_tot;
	}

	public void setPay_fb_tot(double pay_fb_tot) {
		this.pay_fb_tot = pay_fb_tot;
	}
}

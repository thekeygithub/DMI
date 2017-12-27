package com.ebmi.std.querysets.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MaternReimbItem {
	
	// 单位名称
	private String work_station;
	
	// 生育时间
	private Date bear_date;
	
	// 津贴基数
	private BigDecimal benefit_base;
	
	// 产假天数
	private Integer matern_leavedays;
	
	// 医疗费
	private BigDecimal medical_fee;
	
	// 津贴
	private BigDecimal benefit;
	
	// 补助
	private BigDecimal subsidy;
	
	// 生育待遇总计
	private BigDecimal matern_fee_sum;
	
	// 报销总金额
	private BigDecimal reimb_sum;
	
	// 日期
	private Date reimb_date;

	public String getWork_station() {
		return work_station;
	}

	public void setWork_station(String work_station) {
		this.work_station = work_station;
	}

	public Date getBear_date() {
		return bear_date;
	}

	public void setBear_date(Date bear_date) {
		this.bear_date = bear_date;
	}

	public BigDecimal getBenefit_base() {
		return benefit_base;
	}

	public void setBenefit_base(BigDecimal benefit_base) {
		this.benefit_base = benefit_base;
	}

	public Integer getMatern_leavedays() {
		return matern_leavedays;
	}

	public void setMatern_leavedays(Integer matern_leavedays) {
		this.matern_leavedays = matern_leavedays;
	}

	public BigDecimal getMedical_fee() {
		return medical_fee;
	}

	public void setMedical_fee(BigDecimal medical_fee) {
		this.medical_fee = medical_fee;
	}

	public BigDecimal getBenefit() {
		return benefit;
	}

	public void setBenefit(BigDecimal benefit) {
		this.benefit = benefit;
	}

	public BigDecimal getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(BigDecimal subsidy) {
		this.subsidy = subsidy;
	}

	public BigDecimal getMatern_fee_sum() {
		return matern_fee_sum;
	}

	public void setMatern_fee_sum(BigDecimal matern_fee_sum) {
		this.matern_fee_sum = matern_fee_sum;
	}

	public BigDecimal getReimb_sum() {
		return reimb_sum;
	}

	public void setReimb_sum(BigDecimal reimb_sum) {
		this.reimb_sum = reimb_sum;
	}

	public Date getReimb_date() {
		return reimb_date;
	}

	public void setReimb_date(Date reimb_date) {
		this.reimb_date = reimb_date;
	}
	
}

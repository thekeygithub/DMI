package com.aghit.task.algorithm.domain;

import java.util.Date;

public abstract class Payment {

	// private String[] codes; //编码
	// private String[] names; //对应编码的名字
	// private Date hosp_clearing_cost_date; //医院结算日期

	private ComparedEntity[] compEntities;// 对象对象集合

	// 结算单日期
	private Date clm_date;

	private boolean sign; // 结算数据是否参与计算的标记

	private String si_clearing_cost_no; // 结算单编号(联合主键)

	// 1处方、2诊断、 3医嘱 、4住院
	private int type;

	// from 门诊、门特、住院
	private int fromType;

	// private String[] codes_temp; //codes的临时存储变量
	private ComparedEntity[] compEntities_tmp;// 对象对象集合

	/** 2013.09.25 新增加的共同属性 **/
	// 患者年龄
	private int age;
	// 医院
	private String h_code;
	// 医院级别(医疗机构类别)
	private long h_tier;
	// 医师职称
	private long dr_lvl;
	// 支付类别
	private long mi_schm_id;

	private Date enterDate;// 入院时间

	private Date leaveDate;// 出院时间

	private long p_gend_id; // 性别
	
	private double pay_tot; //金额
	
	private String p_name;  //患者姓名
	
	private int inpatient_day;  // 住院天数
	
	private long hosp_org_type_id; //医院类型
	
	private long rownum;    //序号
	
	private long cle_form_id;  //就诊结算方式
	
	private long p_mi_cat_id;  //参保人员类别
	
	public String getP_name() {
		return p_name;
	}

	public void setP_name(String pName) {
		p_name = pName;
	}

	public double getPay_tot() {
		return pay_tot;
	}

	public void setPay_tot(double payTot) {
		pay_tot = payTot;
	}

	public long getP_gend_id() {
		return p_gend_id;
	}

	public void setP_gend_id(long pGendId) {
		p_gend_id = pGendId;
	}

	public int getFromType() {
		return fromType;
	}

	public void setFromType(int fromType) {
		this.fromType = fromType;
	}

	public Date getClm_date() {
		return clm_date;
	}

	public void setClm_date(Date clmDate) {
		clm_date = clmDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSi_clearing_cost_no() {
		return si_clearing_cost_no;
	}

	public void setSi_clearing_cost_no(String siClearingCostNo) {
		si_clearing_cost_no = siClearingCostNo;
	}

	// public String[] getCodes() {
	// return codes;
	// }
	// public void setCodes(String[] codes) {
	// this.codes = codes;
	// }
	// public Date getHosp_clearing_cost_date() {
	// return hosp_clearing_cost_date;
	// }
	// public void setHosp_clearing_cost_date(Date hospClearingCostDate) {
	// hosp_clearing_cost_date = hospClearingCostDate;
	// }
	public boolean getSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}

	// public String[] getCodes_temp() {
	// return codes_temp;
	// }
	// public void setCodes_temp(String[] codesTemp) {
	// codes_temp = codesTemp;
	// }
	// public String[] getNames() {
	// return names;
	// }
	// public void setNames(String[] names) {
	// this.names = names;
	// }
	public ComparedEntity[] getCompEntities() {
		return compEntities;
	}

	public void setCompEntities(ComparedEntity[] compEntities) {
		this.compEntities = compEntities;
	}

	public ComparedEntity[] getCompEntities_tmp() {
		return compEntities_tmp;
	}

	public void setCompEntities_tmp(ComparedEntity[] compEntities_tmp) {
		this.compEntities_tmp = compEntities_tmp;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getH_code() {
		return h_code;
	}

	public void setH_code(String h_code) {
		this.h_code = h_code;
	}

	public long getH_tier() {
		return h_tier;
	}

	public void setH_tier(long h_tier) {
		this.h_tier = h_tier;
	}

	public long getDr_lvl() {
		return dr_lvl;
	}

	public void setDr_lvl(long dr_lvl) {
		this.dr_lvl = dr_lvl;
	}

	public long getMi_schm_id() {
		return mi_schm_id;
	}

	public void setMi_schm_id(long mi_schm_id) {
		this.mi_schm_id = mi_schm_id;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public int getInpatient_day() {
		return inpatient_day;
	}

	public void setInpatient_day(int inpatient_day) {
		this.inpatient_day = inpatient_day;
	}

	public long getHosp_org_type_id() {
		return hosp_org_type_id;
	}

	public void setHosp_org_type_id(long hosp_org_type_id) {
		this.hosp_org_type_id = hosp_org_type_id;
	}

	public long getRownum() {
		return rownum;
	}

	public void setRownum(long rownum) {
		this.rownum = rownum;
	}

	public long getCle_form_id() {
		return cle_form_id;
	}

	public void setCle_form_id(long cle_form_id) {
		this.cle_form_id = cle_form_id;
	}

	public long getP_mi_cat_id() {
		return p_mi_cat_id;
	}

	public void setP_mi_cat_id(long p_mi_cat_id) {
		this.p_mi_cat_id = p_mi_cat_id;
	}
	
}

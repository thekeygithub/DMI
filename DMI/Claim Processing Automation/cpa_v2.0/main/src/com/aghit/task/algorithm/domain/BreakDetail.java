package com.aghit.task.algorithm.domain;

import java.util.Date;
import java.util.Set;

/**
 * CPA审核结果实体映射类
 * @author Administrator
 *
 */
public class BreakDetail {

	private long rec_id; // 违规记录id
	private String break_type; // 方案执行类型，0:定时执行1:手动执行
	private long rule_id; // 违规规则id
	private long scenario_id; // 执行方案id
	private String batch_code; // 批次号
	private long clm_id; // 原始数据id
	private long data_src_id;// 数据来源
	private String break_detail; // 违规明细说明
	private long crt_user_id; // 创建人id
	private Date crt_dttm; // 创建日期
	private long pre_id; // 处方ID（或者医嘱ID、或者结算单ID（诊断时））
	private int errData_type; // 错误数据类型(1诊断、2处方、3医嘱、4分解住院)
	private String med_type_id;// 就医类型（1门诊、2门特、3住院）
	
	private long k_id;//知识ID
	private String err_elem_ids=""; //违规元素ids
	private String err_category_ids=""; //违规分类ids
	
	private long log_id; //方案执行日志的id
	
	//2014-01-03 新增
	private double pay_tot; //违规金额
	private String err_item_name;  //违规项目名称
	private String p_id;	// 患者ID
	private String p_name;	// 患者卡号
	private Date clm_Dt;//结算单日期
	private String lgcy_org_code;//医院编号
	
	//互斥项目违规记录违规项目
	private Set<String> errItems;
	
	public double getPay_tot() {
		return pay_tot;
	}

	public void setPay_tot(double payTot) {
		pay_tot = payTot;
	}

	public String getErr_item_name() {
		return err_item_name;
	}

	public void setErr_item_name(String errItemName) {
		err_item_name = errItemName;
	}

	public long getLog_id() {
		return log_id;
	}

	public void setLog_id(long logId) {
		log_id = logId;
	}

	public long getRec_id() {
		return rec_id;
	}

	public void setRec_id(long recId) {
		rec_id = recId;
	}

	public String getBreak_type() {
		return break_type;
	}

	public void setBreak_type(String breakType) {
		break_type = breakType;
	}

	public long getRule_id() {
		return rule_id;
	}

	public void setRule_id(long ruleId) {
		rule_id = ruleId;
	}

	public long getScenario_id() {
		return scenario_id;
	}

	public void setScenario_id(long scenarioId) {
		scenario_id = scenarioId;
	}

	public String getBatch_code() {
		return batch_code;
	}

	public void setBatch_code(String batchCode) {
		batch_code = batchCode;
	}

	public long getClm_id() {
		return clm_id;
	}

	public void setClm_id(long clmId) {
		clm_id = clmId;
	}

	public long getData_src_id() {
		return data_src_id;
	}

	public void setData_src_id(long dataSrcId) {
		data_src_id = dataSrcId;
	}

	public String getBreak_detail() {
		return break_detail;
	}

	public void setBreak_detail(String breakDetail) {
		break_detail = breakDetail;
	}

	public long getCrt_user_id() {
		return crt_user_id;
	}

	public void setCrt_user_id(long crtUserId) {
		crt_user_id = crtUserId;
	}

	public Date getCrt_dttm() {
		return crt_dttm;
	}

	public void setCrt_dttm(Date crtDttm) {
		crt_dttm = crtDttm;
	}

	public long getPre_id() {
		return pre_id;
	}

	public void setPre_id(long pre_id) {
		this.pre_id = pre_id;
	}

	public int getErrData_type() {
		return errData_type;
	}

	public void setErrData_type(int errData_type) {
		this.errData_type = errData_type;
	}

	public String getMed_type_id() {
		return med_type_id;
	}

	public void setMed_type_id(String med_type_id) {
		this.med_type_id = med_type_id;
	}

	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public Date getClm_Dt() {
		return clm_Dt;
	}

	public void setClm_Dt(Date clm_Dt) {
		this.clm_Dt = clm_Dt;
	}

	public String getLgcy_org_code() {
		return lgcy_org_code;
	}

	public void setLgcy_org_code(String lgcy_org_code) {
		this.lgcy_org_code = lgcy_org_code;
	}

	public long getK_id() {
		return k_id;
	}

	public void setK_id(long k_id) {
		this.k_id = k_id;
	}

	public String getErr_elem_ids() {
		return err_elem_ids;
	}

	public void setErr_elem_ids(String err_elem_ids) {
		this.err_elem_ids = err_elem_ids;
	}

	public String getErr_category_ids() {
		return err_category_ids;
	}

	public void setErr_category_ids(String err_category_ids) {
		this.err_category_ids = err_category_ids;
	}

	public Set<String> getErritem() {
		return errItems;
	}

	public void setErritem(Set<String> erritem) {
		this.errItems = erritem;
	}

}

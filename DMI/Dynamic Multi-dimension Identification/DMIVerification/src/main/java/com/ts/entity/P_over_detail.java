package com.ts.entity;

import java.io.Serializable;

/**
 * 
 * ProjectName：API
 * ClassName：P_ovew_detail
 * Description：TODO(超限明细表)
 * @Copyright：
 * @Company：
 * @author：Lee 李世博
 * @version 
 * Create Date：2017年3月28日 下午2:54:26
 */
public class P_over_detail implements Serializable{
	
	/**
	 * @Fields serialVersionUID : TODO(说明)
	 */ 
	private static final long serialVersionUID = 1L;
	private String ID; //	主键ID
	private String IN_ID;//接口信息ID
	private String TYPE;//药品诊疗类型
	private String CODE;//医院项目编码
	private Double NUM; //超限数量
	private Double SELF_FEE; //超限自费金额
	private Double TOTAL_FEE;//合计自费金额
	private Double REA_CODE; //1:单价限额 2：单次限额 3：单次限量 4：年度限量
	private String REA_DESC; //超限原因说明
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getIN_ID() {
		return IN_ID;
	}
	public void setIN_ID(String iN_ID) {
		IN_ID = iN_ID;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public Double getNUM() {
		return NUM;
	}
	public void setNUM(Double nUM) {
		NUM = nUM;
	}
	public Double getSELF_FEE() {
		return SELF_FEE;
	}
	public void setSELF_FEE(Double sELF_FEE) {
		SELF_FEE = sELF_FEE;
	}
	public Double getTOTAL_FEE() {
		return TOTAL_FEE;
	}
	public void setTOTAL_FEE(Double tOTAL_FEE) {
		TOTAL_FEE = tOTAL_FEE;
	}
	public Double getREA_CODE() {
		return REA_CODE;
	}
	public void setREA_CODE(Double rEA_CODE) {
		REA_CODE = rEA_CODE;
	}
	public String getREA_DESC() {
		return REA_DESC;
	}
	public void setREA_DESC(String rEA_DESC) {
		REA_DESC = rEA_DESC;
	}

}
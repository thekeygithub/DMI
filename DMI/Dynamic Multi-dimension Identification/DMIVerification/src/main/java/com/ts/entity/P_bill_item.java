package com.ts.entity;

import java.io.Serializable;

/**
 * 
* @ClassName: P_bill_item 
* @Description: TODO(收费项目) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_bill_item implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID						
	private String B_ID;//结算单据ID					
	private String TYPE;//药品诊疗类型(1药品，2诊疗)		
	private String CODE;//医院编码（医院项目编码）				
	private String NAME;//医院名称（医院项目名称)			
	private String UNIT;//医院单位						
	private String SPEC;//医院规格						
	private String FORM;//医院剂型						
	private String RECIPE_NO;//处方号					
	private Double PRICE;//医院单价					
	private Double P_NUM;//贴数						
	private Double NUM;//数量						
	private Double FEE;//总金额						
	private Double USE_DAY;//数量						
	private Integer OVER_FLAG;//0:正常 1：超限自费		
	private String CEN_CODE;//中心编码（药品诊疗中心编码）	
	private String COM;//医院生产厂家（产地）				
	private String PACK;//医院转换比（包装量）
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getB_ID() {
		return B_ID;
	}
	public void setB_ID(String b_ID) {
		B_ID = b_ID;
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
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	public String getSPEC() {
		return SPEC;
	}
	public void setSPEC(String sPEC) {
		SPEC = sPEC;
	}
	public String getFORM() {
		return FORM;
	}
	public void setFORM(String fORM) {
		FORM = fORM;
	}
	public String getRECIPE_NO() {
		return RECIPE_NO;
	}
	public void setRECIPE_NO(String rECIPE_NO) {
		RECIPE_NO = rECIPE_NO;
	}
	public Double getPRICE() {
		return PRICE;
	}
	public void setPRICE(Double pRICE) {
		PRICE = pRICE;
	}
	public Double getP_NUM() {
		return P_NUM;
	}
	public void setP_NUM(Double p_NUM) {
		P_NUM = p_NUM;
	}
	public Double getNUM() {
		return NUM;
	}
	public void setNUM(Double nUM) {
		NUM = nUM;
	}
	public Double getFEE() {
		return FEE;
	}
	public void setFEE(Double fEE) {
		FEE = fEE;
	}
	public Double getUSE_DAY() {
		return USE_DAY;
	}
	public void setUSE_DAY(Double uSE_DAY) {
		USE_DAY = uSE_DAY;
	}
	public Integer getOVER_FLAG() {
		return OVER_FLAG;
	}
	public void setOVER_FLAG(Integer oVER_FLAG) {
		OVER_FLAG = oVER_FLAG;
	}
	public String getCEN_CODE() {
		return CEN_CODE;
	}
	public void setCEN_CODE(String cEN_CODE) {
		CEN_CODE = cEN_CODE;
	}
	public String getCOM() {
		return COM;
	}
	public void setCOM(String cOM) {
		COM = cOM;
	}
	public String getPACK() {
		return PACK;
	}
	public void setPACK(String pACK) {
		PACK = pACK;
	}
	
}
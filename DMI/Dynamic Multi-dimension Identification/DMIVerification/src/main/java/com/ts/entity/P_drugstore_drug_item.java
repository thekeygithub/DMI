package com.ts.entity;

import java.io.Serializable;

/**
 * 药品的信息
 * @ClassName:P_drugstore_drug_item
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月19日下午5:11:35
 */
public class P_drugstore_drug_item implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID						
	private String IN_ID;//接口信息ID					
	private Integer TYPE;//服务结构收费类别编码（1西药费2中成药费3中草药费）	
	private String CODE;//服务结构药品编号			
	private String NAME;//服务结构药品名称	
	private String CENT_CODE;//服务结构中心端编号			
	private String CENT_NAME;//服务结构中心端名称	
	private Double UNIT_PRICE;//药品单价
	private Double COUNT;//数量
	private Double PRICE;//金额
	private String FORM_NO;//剂型编码
	private String DOSE;//每次用量
	private String FREQUENCY;//药品使用频次
	private Double DAYS;//执行天数
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
	public Integer getTYPE() {
		return TYPE;
	}
	public void setTYPE(Integer tYPE) {
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
	public String getCENT_CODE() {
		return CENT_CODE;
	}
	public void setCENT_CODE(String cENT_CODE) {
		CENT_CODE = cENT_CODE;
	}
	public String getCENT_NAME() {
		return CENT_NAME;
	}
	public void setCENT_NAME(String cENT_NAME) {
		CENT_NAME = cENT_NAME;
	}
	public Double getUNIT_PRICE() {
		return UNIT_PRICE;
	}
	public void setUNIT_PRICE(Double uNIT_PRICE) {
		UNIT_PRICE = uNIT_PRICE;
	}
	public Double getCOUNT() {
		return COUNT;
	}
	public void setCOUNT(Double cOUNT) {
		COUNT = cOUNT;
	}
	public Double getPRICE() {
		return PRICE;
	}
	public void setPRICE(Double pRICE) {
		PRICE = pRICE;
	}
	public String getFORM_NO() {
		return FORM_NO;
	}
	public void setFORM_NO(String fORM_NO) {
		FORM_NO = fORM_NO;
	}
	public String getDOSE() {
		return DOSE;
	}
	public void setDOSE(String dOSE) {
		DOSE = dOSE;
	}
	public String getFREQUENCY() {
		return FREQUENCY;
	}
	public void setFREQUENCY(String fREQUENCY) {
		FREQUENCY = fREQUENCY;
	}
	public Double getDAYS() {
		return DAYS;
	}
	public void setDAYS(Double dAYS) {
		DAYS = dAYS;
	}
	
}
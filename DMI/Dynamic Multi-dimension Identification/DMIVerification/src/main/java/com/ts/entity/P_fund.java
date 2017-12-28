package com.ts.entity;

import java.io.Serializable;

/**
 * 
* @ClassName: P_fund 
* @Description: TODO(基金分段信息) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_fund implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID					
	private String IN_ID;//接口信息ID				
	private String CODE;//分段编码					
	private String NAME;//分段名称					
	private Double AMOUNT;//进入额度				
	private Double PAY_AMOUNT;//分段基金支付金额		
	private Double RATIO;//报销比例
	private Double SELF_AMOUNT;//分段自负金额		
	private Double NEG_AMOUNT;//分段自费金额		
	
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
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public Double getPAY_AMOUNT() {
		return PAY_AMOUNT;
	}
	public void setPAY_AMOUNT(Double pAY_AMOUNT) {
		PAY_AMOUNT = pAY_AMOUNT;
	}
	public Double getRATIO() {
		return RATIO;
	}
	public void setRATIO(Double rATIO) {
		RATIO = rATIO;
	}
	public Double getSELF_AMOUNT() {
		return SELF_AMOUNT;
	}
	public void setSELF_AMOUNT(Double sELF_AMOUNT) {
		SELF_AMOUNT = sELF_AMOUNT;
	}
	public Double getNEG_AMOUNT() {
		return NEG_AMOUNT;
	}
	public void setNEG_AMOUNT(Double nEG_AMOUNT) {
		NEG_AMOUNT = nEG_AMOUNT;
	}
	
}
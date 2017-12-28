package com.ts.entity;

import java.io.Serializable;
/**
 * 计算结果信息
 * @ClassName:P_drugstore_result
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月19日下午5:28:58
 */
public class P_drugstore_result  implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO(说明)
	 */ 
	private static final long serialVersionUID = 1L;
	private String ID; //主键ID
	private String IN_ID;	//接口信息ID
	private Integer RECEIPT_COUNT;//收据张数
	private Double LAR_PAY;	//大额补助支付金额
	private Double PLAN_PAY;	//统筹金支付金额
	private Double SER_PAY;	//公务员补助支付金额
	private Double SELF_ACCOUNT_PAY;     //个人帐户支付金额
	private Double SELF_CASH_PAY;	//个人现金支付金额
	private Double B_BALANCE;	//结算前帐户余额
	private Double A_BALANCE;	//结算后帐户余额
	private String INFO;//药店预结算提示
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
	public Integer getRECEIPT_COUNT() {
		return RECEIPT_COUNT;
	}
	public void setRECEIPT_COUNT(Integer rECEIPT_COUNT) {
		RECEIPT_COUNT = rECEIPT_COUNT;
	}
	public Double getLAR_PAY() {
		return LAR_PAY;
	}
	public void setLAR_PAY(Double lAR_PAY) {
		LAR_PAY = lAR_PAY;
	}
	public Double getPLAN_PAY() {
		return PLAN_PAY;
	}
	public void setPLAN_PAY(Double pLAN_PAY) {
		PLAN_PAY = pLAN_PAY;
	}
	public Double getSER_PAY() {
		return SER_PAY;
	}
	public void setSER_PAY(Double sER_PAY) {
		SER_PAY = sER_PAY;
	}
	public Double getSELF_ACCOUNT_PAY() {
		return SELF_ACCOUNT_PAY;
	}
	public void setSELF_ACCOUNT_PAY(Double sELF_ACCOUNT_PAY) {
		SELF_ACCOUNT_PAY = sELF_ACCOUNT_PAY;
	}
	public Double getSELF_CASH_PAY() {
		return SELF_CASH_PAY;
	}
	public void setSELF_CASH_PAY(Double sELF_CASH_PAY) {
		SELF_CASH_PAY = sELF_CASH_PAY;
	}
	public Double getB_BALANCE() {
		return B_BALANCE;
	}
	public void setB_BALANCE(Double b_BALANCE) {
		B_BALANCE = b_BALANCE;
	}
	public Double getA_BALANCE() {
		return A_BALANCE;
	}
	public void setA_BALANCE(Double a_BALANCE) {
		A_BALANCE = a_BALANCE;
	}
	public String getINFO() {
		return INFO;
	}
	public void setINFO(String iNFO) {
		INFO = iNFO;
	}
	
}
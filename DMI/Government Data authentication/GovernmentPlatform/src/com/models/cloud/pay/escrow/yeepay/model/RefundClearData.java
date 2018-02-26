package com.models.cloud.pay.escrow.yeepay.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退费清算对账单实体类
 */
public class RefundClearData {

	
	private String recvActCode;    //收款账号编号
	private String liqDate;          //清算日期
	private String retApplyTime;     //提交退款时间
	private String retFinishTime;    //退款完成时间
	private String custRetCode;    //客户退款订单号
	private String retSeqCode;     //退款交易流水号
	private String origOrdCode;    //原消费交易流水号
	private String retTypeName;    //退款类型
	private String retAmt;     //退款金额
	private String chrgTypeName;   //收费方式
	private String retFee;     //退款退费
	private String realCutAmt; //账户实际扣款金额
	private String payProd;        //支付产品
	private String goodsType;     //商品类别
	private String goodsName;      //商品名称
	private String payActType;     //支付卡类型
	public String getRecvActCode() {
		return recvActCode;
	}
	public void setRecvActCode(String recvActCode) {
		this.recvActCode = recvActCode;
	}
	public String getLiqDate() {
		return liqDate;
	}
	public void setLiqDate(String liqDate) {
		this.liqDate = liqDate;
	}
	public String getRetApplyTime() {
		return retApplyTime;
	}
	public void setRetApplyTime(String retApplyTime) {
		this.retApplyTime = retApplyTime;
	}
	public String getRetFinishTime() {
		return retFinishTime;
	}
	public void setRetFinishTime(String retFinishTime) {
		this.retFinishTime = retFinishTime;
	}
	public String getCustRetCode() {
		return custRetCode;
	}
	public void setCustRetCode(String custRetCode) {
		this.custRetCode = custRetCode;
	}
	public String getRetSeqCode() {
		return retSeqCode;
	}
	public void setRetSeqCode(String retSeqCode) {
		this.retSeqCode = retSeqCode;
	}
	public String getOrigOrdCode() {
		return origOrdCode;
	}
	public void setOrigOrdCode(String origOrdCode) {
		this.origOrdCode = origOrdCode;
	}
	public String getRetTypeName() {
		return retTypeName;
	}
	public void setRetTypeName(String retTypeName) {
		this.retTypeName = retTypeName;
	}
	public String getRetAmt() {
		return retAmt;
	}
	public void setRetAmt(String retAmt) {
		this.retAmt = retAmt;
	}
	public String getChrgTypeName() {
		return chrgTypeName;
	}
	public void setChrgTypeName(String chrgTypeName) {
		this.chrgTypeName = chrgTypeName;
	}
	public String getRetFee() {
		return retFee;
	}
	public void setRetFee(String retFee) {
		this.retFee = retFee;
	}
	public String getRealCutAmt() {
		return realCutAmt;
	}
	public void setRealCutAmt(String realCutAmt) {
		this.realCutAmt = realCutAmt;
	}
	public String getPayProd() {
		return payProd;
	}
	public void setPayProd(String payProd) {
		this.payProd = payProd;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPayActType() {
		return payActType;
	}
	public void setPayActType(String payActType) {
		this.payActType = payActType;
	}
	

}

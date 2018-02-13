package com.models.cloud.pay.escrow.yeepay.model;

/**
 * 消费清算对账单实体类
 */
public class PayClearData {
    
	//商户账户编号
    private String ebaoActCode;
    //清算日期
    private String clearDate;
    //下单日期
    private String xdTime;
    //交易时间
    private String tradeTime;
    //客户消费订单号
    private String customOrdCode;
    //消费交易流水号
    private String customTdCode;
    //消费金额
    private String tdAmt;
    //实收金额
    private String realAmt;
    //收费方式
    private String chrgType;
    //收费方手续费
    private String servChrgAmt;
    //支付产品
    private String prodName;
    //商品类别
    private String goodsType;
    //商品名称
    private String goodsName;
    //支付类型
    private String payActType;

	public String getEbaoActCode() {
		return ebaoActCode;
	}

	public void setEbaoActCode(String ebaoActCode) {
		this.ebaoActCode = ebaoActCode;
	}

	public String getClearDate() {
		return clearDate;
	}

	public void setClearDate(String clearDate) {
		this.clearDate = clearDate;
	}

	public String getXdTime() {
		return xdTime;
	}

	public void setXdTime(String xdTime) {
		this.xdTime = xdTime;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getCustomOrdCode() {
		return customOrdCode;
	}

	public void setCustomOrdCode(String customOrdCode) {
		this.customOrdCode = customOrdCode;
	}

	public String getCustomTdCode() {
		return customTdCode;
	}

	public void setCustomTdCode(String customTdCode) {
		this.customTdCode = customTdCode;
	}

	public String getTdAmt() {
		return tdAmt;
	}

	public void setTdAmt(String tdAmt) {
		this.tdAmt = tdAmt;
	}

	public String getRealAmt() {
		return realAmt;
	}

	public void setRealAmt(String realAmt) {
		this.realAmt = realAmt;
	}

	public String getChrgType() {
		return chrgType;
	}

	public void setChrgType(String chrgType) {
		this.chrgType = chrgType;
	}

	public String getServChrgAmt() {
		return servChrgAmt;
	}

	public void setServChrgAmt(String servChrgAmt) {
		this.servChrgAmt = servChrgAmt;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
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
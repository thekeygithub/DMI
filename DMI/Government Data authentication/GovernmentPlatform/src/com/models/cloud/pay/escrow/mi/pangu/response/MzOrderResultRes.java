package com.models.cloud.pay.escrow.mi.pangu.response;

/**
 * 门诊交易记录  结果
 * @author yanjie.ji
 * @date 2016年12月1日 
 * @time 下午3:10:21
 */
public class MzOrderResultRes extends BaseResponse {
	private String dealID;//交易流水号
	private String balance;//余额
	private String ylsum;//医疗费总额
	private String tcpay;//统筹支付
	private String bigpay;//大额支付
	private String gwybz;//公务员补助
	private String qybcpay;//企业补充基金支付
	private String zhpay;//账户支付
	private String cashpay;//现金支付
	private String zfproject;//自费项目
	private String perpay;//个人自付
	private String cfstand;//超付标准
	private String propayplan;//按比例自付（统筹）
	private String propaybig;//按比例自付（大额）
	private String superbigpay;//超大额自付
	private String jingbr;//经办人
	public String getQybcpay() {
		return qybcpay;
	}
	public void setQybcpay(String qybcpay) {
		this.qybcpay = qybcpay;
	}
	public String getDealID() {
		return dealID;
	}
	public void setDealID(String dealID) {
		this.dealID = dealID;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getYlsum() {
		return ylsum;
	}
	public void setYlsum(String ylsum) {
		this.ylsum = ylsum;
	}
	public String getTcpay() {
		return tcpay;
	}
	public void setTcpay(String tcpay) {
		this.tcpay = tcpay;
	}
	public String getBigpay() {
		return bigpay;
	}
	public void setBigpay(String bigpay) {
		this.bigpay = bigpay;
	}
	public String getGwybz() {
		return gwybz;
	}
	public void setGwybz(String gwybz) {
		this.gwybz = gwybz;
	}
	public String getZhpay() {
		return zhpay;
	}
	public void setZhpay(String zhpay) {
		this.zhpay = zhpay;
	}
	public String getCashpay() {
		return cashpay;
	}
	public void setCashpay(String cashpay) {
		this.cashpay = cashpay;
	}
	public String getZfproject() {
		return zfproject;
	}
	public void setZfproject(String zfproject) {
		this.zfproject = zfproject;
	}
	public String getPerpay() {
		return perpay;
	}
	public void setPerpay(String perpay) {
		this.perpay = perpay;
	}
	public String getCfstand() {
		return cfstand;
	}
	public void setCfstand(String cfstand) {
		this.cfstand = cfstand;
	}
	public String getPropayplan() {
		return propayplan;
	}
	public void setPropayplan(String propayplan) {
		this.propayplan = propayplan;
	}
	public String getPropaybig() {
		return propaybig;
	}
	public void setPropaybig(String propaybig) {
		this.propaybig = propaybig;
	}
	public String getSuperbigpay() {
		return superbigpay;
	}
	public void setSuperbigpay(String superbigpay) {
		this.superbigpay = superbigpay;
	}
	public String getJingbr() {
		return jingbr;
	}
	public void setJingbr(String jingbr) {
		this.jingbr = jingbr;
	}
}

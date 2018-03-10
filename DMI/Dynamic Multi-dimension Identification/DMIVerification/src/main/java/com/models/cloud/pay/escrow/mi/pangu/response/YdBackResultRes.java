package com.models.cloud.pay.escrow.mi.pangu.response;


/**
 * 药店结算回退结果
 * @author yanjie.ji
 * @date 2016年11月20日 
 * @time 下午7:51:51
 */
public class YdBackResultRes extends BaseResponse {
	private String balance;//余额
    private String ylsum;//金额
    private String zhpay;//账户支付
    private String cashpay;//现金支付
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
}

package com.models.cloud.pay.escrow.mi.pangu.response;

/**
 * 药店预结算结果
 * @author yanjie.ji
 * @date 2016年12月1日 
 * @time 下午3:07:34
 */
public class YdYSfmxRes extends BaseResponse{
    private String ylsum;//金额
    private String zhpay;//账户支付
    private String cashpay;//现金支付
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

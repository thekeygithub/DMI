package com.ebmi.seng;


public class BalanceApp {
    /**
     * 当年账户余额
     */
	private Double cur_balance;
	/**
	 * 历年账户余额
	 */
	private Double past_balance;
	public Double getCur_balance() {
		return cur_balance;
	}
	public void setCur_balance(Double cur_balance) {
		this.cur_balance = cur_balance;
	}
	public Double getPast_balance() {
		return past_balance;
	}
	public void setPast_balance(Double past_balance) {
		this.past_balance = past_balance;
	} 

	
	
}

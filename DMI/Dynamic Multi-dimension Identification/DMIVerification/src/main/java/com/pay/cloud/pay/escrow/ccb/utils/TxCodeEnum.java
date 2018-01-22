package com.pay.cloud.pay.escrow.ccb.utils;

/**
 * 加密秘钥枚举
 */
public enum TxCodeEnum {
	
	
	/**
	 * 反钓鱼支付
	 */
	ANTIPHISHINGPAYMENT("520100","ANTIPHISHINGPAYMENT");
	
	//交易码
	private final String txCode;
	//交易码描述
	private final String txDesc;
	
	//构造函数
	private TxCodeEnum(String txCode,String txDesc) {
    	this.txCode = txCode;
    	this.txDesc = txDesc;
    }

	/**
	 * 读取AES key
	 * @return
	 */
	public String getTxCode() {
		return txCode;
	}
    /**
     * 读取AES 补码
     * @return
     */
	public String getTxDesc() {
		return txDesc;
	}

}

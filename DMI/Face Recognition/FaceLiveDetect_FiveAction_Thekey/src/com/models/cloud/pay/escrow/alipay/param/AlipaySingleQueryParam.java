package com.models.cloud.pay.escrow.alipay.param;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

public class AlipaySingleQueryParam implements AlipayParam {
	
	/**
	 * 支付宝交易号
	 * 效率高
	 */
	private String trade_no;
	/**
	 * 商户唯一订单号
	 */
	private String out_trade_no;

	@Override
	public boolean checkSelf() throws UnmatchedParamException {
		if(StringUtils.isEmpty(this.getTrade_no())
				&&StringUtils.isEmpty(this.getOut_trade_no()))
			throw new UnmatchedParamException("trade_no和out_trade_no不能均为空");
		return true;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
}

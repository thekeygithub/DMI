package com.models.cloud.pay.escrow.alipay.param;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 阿里退款查询参数
 * @author yanjie.ji
 * @date 2016年9月2日 
 * @time 下午1:47:16
 */
public class AlipayRefundQueryParam implements AlipayParam {
	/**
	 * 批量退款批次号
	 * 与trade_no 必填其一
	 */
	private String batch_no;
	/**
	 * 支付宝交易号
	 * 与batch_no 必填其一
	 */
	private String trade_no;
	
	@Override
	public boolean checkSelf() throws UnmatchedParamException {
		if(StringUtils.isEmpty(this.getTrade_no())
				&&StringUtils.isEmpty(this.getBatch_no()))
			throw new UnmatchedParamException("trade_no和batch_no不能均为空");
		return true;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
}

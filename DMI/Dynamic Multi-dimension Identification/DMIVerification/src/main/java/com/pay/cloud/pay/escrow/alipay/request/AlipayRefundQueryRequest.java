package com.pay.cloud.pay.escrow.alipay.request;

import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 批量瑞款查询接口
 * @author yanjie.ji
 * @date 2016年9月2日 
 * @time 下午1:35:30
 */
public class AlipayRefundQueryRequest extends AlipayBaseRequest {
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
	protected boolean check() throws UnmatchedParamException {
		if(StringUtils.isEmpty(this.getTrade_no())
				&&StringUtils.isEmpty(this.getBatch_no()))
			throw new UnmatchedParamException("trade_no和batch_no不能均为空");
		return true;
	}

	@Override
	protected TreeMap<String, String> getSelfProperties() {
		TreeMap<String, String> map = new TreeMap<String,String>();
		map.put("trade_no", this.getTrade_no());
		map.put("batch_no", this.getBatch_no());
		return map;
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

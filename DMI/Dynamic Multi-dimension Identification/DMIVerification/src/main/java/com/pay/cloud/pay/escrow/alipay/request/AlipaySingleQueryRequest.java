package com.pay.cloud.pay.escrow.alipay.request;

import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 阿里单笔交易查询请求参数
 * @author yanjie.ji
 * @date 2016年8月24日 
 * @time 下午2:13:55
 */
public class AlipaySingleQueryRequest extends AlipayBaseRequest {
	
	/**
	 * 支付宝交易号
	 */
	private String trade_no;
	/**
	 * 商户网站唯一订单号
	 */
	private String out_trade_no;

	@Override
	protected boolean check() throws UnmatchedParamException {
		if(StringUtils.isEmpty(this.getTrade_no())
				&&StringUtils.isEmpty(this.getOut_trade_no()))
			throw new UnmatchedParamException("trade_no和out_trade_no不能均为空");
		return true;
	}

	@Override
	protected TreeMap<String, String> getSelfProperties() {
		TreeMap<String, String> map = new TreeMap<String,String>();
		map.put("trade_no", this.getTrade_no());
		map.put("out_trade_no", this.getOut_trade_no());
		return map;
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

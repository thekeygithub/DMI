package com.pay.cloud.pay.escrow.alipay.response;

import java.util.TreeMap;

import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

/**
 * 阿里退款同步反馈信息
 * @author yanjie.ji
 * @date 2016年8月31日 
 * @time 下午5:12:51
 */
public class AlipaySynchroRefundResponse implements AlipayResponse{

	/**
	 * 是否成功
	 */
	protected String is_success;
	/**
	 * 错误编码
	 */
	protected String error;

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public boolean checkself() throws UnmatchedParamException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TreeMap<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}
}

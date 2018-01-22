package com.pay.cloud.pay.escrow.alipay.response;

import java.util.TreeMap;

import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

public class AlipayRefundResponse extends AlipayNotifyResponse{
	/**
	 * 退款批次号
	 */
	private String batch_no;
	/**
	 * 退款成功总数
	 */
	private String success_num;
	/**
	 * 处理结果详情
	 */
	private String result_details;
	/**
	 * 解冻结果明细
	 */
	private String unfreezed_details;

	@Override
	public boolean checkself() throws UnmatchedParamException {
		return false;
	}

	@Override
	public TreeMap<String, Object> getProperties() {
		TreeMap<String, Object> map = super.getProties();
		map.put("batch_no", this.getBatch_no());
		map.put("success_num", this.getSuccess_num());
		map.put("result_details", this.getResult_details());
		map.put("unfreezed_details", this.getUnfreezed_details());
		return map;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getSuccess_num() {
		return success_num;
	}

	public void setSuccess_num(String success_num) {
		this.success_num = success_num;
	}

	public String getResult_details() {
		return result_details;
	}

	public void setResult_details(String result_details) {
		this.result_details = result_details;
	}

	public String getUnfreezed_details() {
		return unfreezed_details;
	}

	public void setUnfreezed_details(String unfreezed_details) {
		this.unfreezed_details = unfreezed_details;
	}
}

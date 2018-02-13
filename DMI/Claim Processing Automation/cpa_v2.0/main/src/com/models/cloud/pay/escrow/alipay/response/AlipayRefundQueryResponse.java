package com.models.cloud.pay.escrow.alipay.response;

import java.util.TreeMap;

import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 批量退款查询返回结果
 * @author yanjie.ji
 * @date 2016年9月2日 
 * @time 下午1:42:26
 */
public class AlipayRefundQueryResponse implements AlipayResponse {
	/**
	 * 是否成功
	 * T：查询成功;F：查询失败
	 */
	private String is_success;
	/**
	 * 退款结果明细说明
	 */
	private String result_details;
	/**
	 * 解冻结果明细
	 */
	private String unfreezed_details;
	/**
	 * 错误代码
	 */
	private String error_code;
	/**
	 * 签名方式
	 */
	private String sign_type;
	/**
	 * 签名
	 */
	private String sign;

	@Override
	public boolean checkself() throws UnmatchedParamException {
		return false;
	}

	@Override
	public TreeMap<String, Object> getProperties() {
		return null;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
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

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}

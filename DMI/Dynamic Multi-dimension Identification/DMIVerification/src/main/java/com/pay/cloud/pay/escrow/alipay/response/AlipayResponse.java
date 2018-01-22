package com.pay.cloud.pay.escrow.alipay.response;

import java.util.TreeMap;

import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;

public interface AlipayResponse {
	
	/**
	 * 检查自身
	 * @return
	 * @throws UnmatchedParamException
	 */
	public boolean checkself()throws UnmatchedParamException;
	/**
	 * 得到排序的参数
	 * @return
	 */
	public TreeMap<String,Object> getProperties();
}

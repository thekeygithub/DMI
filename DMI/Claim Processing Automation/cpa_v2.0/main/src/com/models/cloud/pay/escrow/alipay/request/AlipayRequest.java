package com.models.cloud.pay.escrow.alipay.request;

import java.util.TreeMap;

import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 阿里支付请求
 * @author yanjie.ji
 * @date 2016年8月17日 
 * @time 上午10:51:20
 */
public interface AlipayRequest {
	
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
	public TreeMap<String,String> getProperties();
	
}

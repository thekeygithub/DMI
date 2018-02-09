package com.models.cloud.pay.escrow.alipay.service;

import java.util.Map;

import com.models.cloud.pay.escrow.alipay.dto.BaseDTO;
import com.models.cloud.pay.escrow.alipay.param.AlipayImmediatePayParam;
import com.models.cloud.pay.escrow.alipay.param.AlipayMobileParam;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundParam;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundQueryParam;
import com.models.cloud.pay.escrow.alipay.param.AlipaySingleQueryParam;
import com.models.cloud.pay.escrow.alipay.response.AlipayRefundQueryResponse;
import com.models.cloud.pay.escrow.alipay.response.AlipaySingleQueryResponse.SingleQueryResponseTrade;

/**
 * 阿里支付service
 * @author yanjie.ji
 * @date 2016年8月17日 
 * @time 下午2:27:06
 */
public interface AlipayService {
	/**
	 * 对支付宝返回数据进行验签
	 * @param params
	 * @return
	 */
	public BaseDTO<Boolean> verify(Map<String, Object> param,String fund_id);

	/**
	 * 获取移动支付所需的url
	 * @param param
	 * @return
	 * @throws RuntimeException
	 */
	public BaseDTO<String> getMobilePayUrl(AlipayMobileParam param,String fund_id);
	/**
	 * 支付交易查询（单笔）
	 * @param param
	 * @param fund_id
	 */
	public BaseDTO<SingleQueryResponseTrade> querySingleTrade(AlipaySingleQueryParam param,String fund_id);
	/**
	 * 退款
	 * @param param
	 * @param fund_id
	 * @return
	 */
	public BaseDTO<Boolean> refundTrade(AlipayRefundParam param,String fund_id);

	/**
	 * 退款查询
	 * @param param
	 * @param fund_id
	 * @return
	 */
	public BaseDTO<AlipayRefundQueryResponse> refundQuery(AlipayRefundQueryParam param,String fund_id);

	/**
	 * 即时到账交易接口
	 * @param param
	 * @return
	 */
	public BaseDTO<String> getImmediatePay(AlipayImmediatePayParam param,String fund_id);
}

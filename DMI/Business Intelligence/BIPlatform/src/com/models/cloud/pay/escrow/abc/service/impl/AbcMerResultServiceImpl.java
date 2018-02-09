package com.models.cloud.pay.escrow.abc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.Base64;
import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.PaymentResult;
import com.abc.trustpay.client.ebus.QueryOrderRequest;
import com.models.cloud.pay.escrow.abc.service.AbcPayService;
import com.models.cloud.pay.escrow.abc.utils.AbcConstants;
import com.models.cloud.pay.escrow.abc.utils.AbcParaUtils;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 支付结果接收处理
 */
@Service("abcMerResultServiceImpl")
public class AbcMerResultServiceImpl implements AbcPayService {
	
	//日志
	private static Logger logger = Logger.getLogger(AbcMerResultServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.abc.service.AbcPayService#perform(java.util.Map)
	 */
	@Override
	public Map<String, Object> perform(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### AbcMerResultServiceImpl.perform() #####");
		logger.info(receiveMap);
		
		//结果值
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		//返回值
		Map<String, Object> returnMap	= new HashMap<String, Object>();
		try{	
			//校验入口参数是否合法
			//资金平台ID
			if(false == ValidateUtils.checkParam(receiveMap,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			} 
			//OrderNo 订单编号 必须设定
			if(false == ValidateUtils.checkParam(receiveMap,"MSG",ValidateUtils.TYPE_STRING,false,3000,null,null,resultMap)){
				return resultMap;
			}
			//资金平台
			String fundId = ConvertUtils.getString(receiveMap.get("FundId")).trim();
			//商户顺序号
			int merNo = Integer.parseInt(AbcParaUtils.getMerNo(fundId));
			String msg = ConvertUtils.getString(receiveMap.get("MSG")).trim();
			//调用农业银行结果处理接口
			PaymentResult tResult = new PaymentResult(msg);
			//判断支付结果状态，进行后续操作
			if (tResult.isSuccess()) {
				//支付成功并且验签、解析成功
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
	            resultMap.put("TrxType",  tResult.getValue("TrxType"));
	            resultMap.put("OrderNo",  tResult.getValue("OrderNo"));
	            resultMap.put("Amount",  tResult.getValue("Amount"));
	            resultMap.put("BatchNo",  tResult.getValue("BatchNo"));
	            resultMap.put("VoucherNo",  tResult.getValue("VoucherNo"));
	            resultMap.put("HostDate",  tResult.getValue("HostDate"));
	            resultMap.put("HostTime",  tResult.getValue("HostTime"));
	            resultMap.put("MerchantRemarks",  tResult.getValue("MerchantRemarks"));
	            resultMap.put("PayType",  tResult.getValue("PayType"));
	            resultMap.put("NotifyType",  tResult.getValue("NotifyType"));
	            resultMap.put("iRspRef",  tResult.getValue("iRspRef"));
			}else {
				//支付成功但是由于验签或者解析报文等操作失败
				resultMap.put("resultCode", tResult.getReturnCode());
	            resultMap.put("resultDesc", tResult.getErrorMessage());
	            returnMap.put("ReturnCode",tResult.getReturnCode());
	            returnMap.put("ErrorMessage",tResult.getErrorMessage());
			}
			//结果加入返回Map中
			resultMap.putAll(returnMap);
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		//记录返回结果
        logger.info(resultMap);
		return resultMap;
	}

}

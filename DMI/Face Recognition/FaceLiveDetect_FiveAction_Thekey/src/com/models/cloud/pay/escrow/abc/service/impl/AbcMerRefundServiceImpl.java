package com.models.cloud.pay.escrow.abc.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.PaymentRequest;
import com.abc.trustpay.client.ebus.RefundRequest;
import com.models.cloud.pay.escrow.abc.service.AbcPayService;
import com.models.cloud.pay.escrow.abc.utils.AbcConstants;
import com.models.cloud.pay.escrow.abc.utils.AbcParaUtils;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 农行退款接口
 */
@Service("abcMerRefundServiceImpl")
public class AbcMerRefundServiceImpl implements AbcPayService {
	
	//日志
	private static Logger logger = Logger.getLogger(AbcMerRefundServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.abc.service.AbcPayService#perform(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> perform(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### AbcMerRefundServiceImpl.perform() #####");
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
			//OrderDate 订单日期 必须设定 ，YYYY/MM/DD
			if(false == ValidateUtils.checkParam(receiveMap,"OrderDate",ValidateUtils.TYPE_DATE,false,10,null,"yyyy/MM/dd",resultMap)){
				return resultMap;
			}
			//OrderTime 订单时间 必须设定 ，HH:MM:SS
			if(false == ValidateUtils.checkParam(receiveMap,"OrderTime",ValidateUtils.TYPE_DATE,false,8,null,"HH:mm:ss",resultMap)){
				return resultMap;
			}
			//MerRefundAccountNo 商户退款账号 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"MerRefundAccountNo",ValidateUtils.TYPE_STRING,true,50,null,null,resultMap)){
				return resultMap;
			}
			//MerRefundAccountName 商户退款名 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"MerRefundAccountName",ValidateUtils.TYPE_CHINESE,true,50,null,null,resultMap)){
				return resultMap;
			}
			//OrderNo 原交易编号 必须设定
			if(false == ValidateUtils.checkParam(receiveMap,"OrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//NewOrderNo 交易编号 必须设定
			if(false == ValidateUtils.checkParam(receiveMap,"NewOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//TrxAmount 退货金额 必须设定
			if(false == ValidateUtils.checkParam(receiveMap, "TrxAmount", ValidateUtils.TYPE_MONEY, false, 20, null, null,resultMap)){
				return resultMap;
			}
			//MerchantRemarks 附言 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"MerchantRemarks",ValidateUtils.TYPE_CHINESE,true,100,null,null,resultMap)){
				return resultMap;
			}
			//资金平台
			String fundId = ConvertUtils.getString(receiveMap.get("FundId")).trim();
			//商户顺序号
			int merNo = Integer.parseInt(AbcParaUtils.getMerNo(fundId));
			//生成退款请求对象
			RefundRequest tRequest = new RefundRequest();
	        tRequest.dicRequest.put("OrderDate", ConvertUtils.getString(receiveMap.get("OrderDate")).trim());  //订单日期（必要信息）
	        tRequest.dicRequest.put("OrderTime", ConvertUtils.getString(receiveMap.get("OrderTime")).trim()); //订单时间（必要信息）
	        tRequest.dicRequest.put("MerRefundAccountNo", ConvertUtils.getString(receiveMap.get("MerRefundAccountNo")).trim());  //商户退款账号
	        tRequest.dicRequest.put("MerRefundAccountName", ConvertUtils.getString(receiveMap.get("MerRefundAccountName")).trim()); //商户退款名
	        tRequest.dicRequest.put("OrderNo", ConvertUtils.getString(receiveMap.get("OrderNo")).trim()); //原交易编号（必要信息）
	        tRequest.dicRequest.put("NewOrderNo",ConvertUtils.getString(receiveMap.get("NewOrderNo")).trim()); //交易编号（必要信息）
	        tRequest.dicRequest.put("CurrencyCode",  AbcConstants.CURRENCYCODE_CNY); //交易币种（必要信息）
	        tRequest.dicRequest.put("TrxAmount", ConvertUtils.getString(receiveMap.get("TrxAmount")).trim()); //退货金额 （必要信息）
	        tRequest.dicRequest.put("MerchantRemarks", ConvertUtils.getString(receiveMap.get("MerchantRemarks")).trim());  //附言
	        //传送退款请求并取得退货结果
//	        JSON json = tRequest.postRequest();
	        JSON json = tRequest.extendPostRequest(merNo);
	        //判断退款结果状态，进行后续操作
	        String returnCode = json.GetKeyValue("ReturnCode");
	        String errorMessage = json.GetKeyValue("ErrorMessage");
	        if (AbcConstants.RETURN_CODE_SUCCESS.equals(returnCode)){
	        	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
	            returnMap.put("ReturnCode", json.GetKeyValue("ReturnCode"));
	            returnMap.put("ErrorMessage", json.GetKeyValue("ErrorMessage"));
	            returnMap.put("OrderNo", json.GetKeyValue("OrderNo"));
	            returnMap.put("NewOrderNo", json.GetKeyValue("NewOrderNo"));
	            returnMap.put("TrxAmount", json.GetKeyValue("TrxAmount"));
	            returnMap.put("BatchNo", json.GetKeyValue("BatchNo"));
	            returnMap.put("VoucherNo", json.GetKeyValue("VoucherNo"));
	            returnMap.put("HostDate", json.GetKeyValue("HostDate"));
	            returnMap.put("HostTime", json.GetKeyValue("HostTime"));
	            returnMap.put("iRspRef", json.GetKeyValue("iRspRef"));
	        }else{
	        	resultMap.put("resultCode", returnCode);
	            resultMap.put("resultDesc", errorMessage);
	            returnMap.put("ReturnCode",returnCode);
	            returnMap.put("ErrorMessage",errorMessage);
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

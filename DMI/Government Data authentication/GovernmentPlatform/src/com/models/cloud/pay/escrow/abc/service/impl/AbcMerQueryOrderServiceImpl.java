package com.models.cloud.pay.escrow.abc.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.Base64;
import com.abc.trustpay.client.JSON;
//import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.QueryOrderRequest;
import com.models.cloud.pay.escrow.abc.service.AbcPayService;
import com.models.cloud.pay.escrow.abc.utils.AbcConstants;
import com.models.cloud.pay.escrow.abc.utils.AbcParaUtils;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 农行交易查询实现类
 */
@Service("abcMerQueryOrderServiceImpl")
public class AbcMerQueryOrderServiceImpl implements AbcPayService {
	
	//日志
	private static Logger logger = Logger.getLogger(AbcMerQueryOrderServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.abc.service.AbcPaymentService#perform(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> perform(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### AbcMerQueryOrderServiceImpl.perform() #####");
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
			if(false == ValidateUtils.checkParam(receiveMap,"OrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//QueryDetail 是否查询详细信息 必须设定, 0：状态查询； 1：详细查询
			if(false == ValidateUtils.checkParam(receiveMap,"QueryDetail",ValidateUtils.TYPE_CHAR,false,1,new char[]{'0','1'},null,resultMap)){
				return resultMap;
			}
			//资金平台
			String fundId = ConvertUtils.getString(receiveMap.get("FundId")).trim();
			//商户顺序号
			int merNo = Integer.parseInt(AbcParaUtils.getMerNo(fundId));
			String orderNo = ConvertUtils.getString(receiveMap.get("OrderNo")).trim();
			String queryDetail = ConvertUtils.getString(receiveMap.get("QueryDetail")).trim();
			String payTypeId = AbcConstants.PAYTYPEID_IMMEDIATEPAY;
			//调用农业银行查询接口
			QueryOrderRequest tQueryRequest = new QueryOrderRequest();
			tQueryRequest.queryRequest.put("PayTypeID", payTypeId);    //设定交易类型
			tQueryRequest.queryRequest.put("OrderNo", orderNo);    //设定订单编号 （必要信息）
			tQueryRequest.queryRequest.put("QueryDetail", queryDetail);//设定查询方式
//			JSON json = tQueryRequest.postRequest();
			JSON json = tQueryRequest.extendPostRequest(merNo);
		    //返回值
			String returnCode = json.GetKeyValue("ReturnCode");
			String errorMessage = json.GetKeyValue("ErrorMessage");
			if (AbcConstants.RETURN_CODE_SUCCESS.equals(returnCode)){
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			    //获取结果信息
			    String orderInfo = json.GetKeyValue("Order");
			    if (orderInfo.length() < 1){
			    	logger.info("农行查询订单接口查询结果为空");
			    }else{
			        //还原经过base64编码的信息 
			    	Base64 tBase64 = new Base64();
			  		String orderDetail = new String(tBase64.decode(orderInfo));
			  		logger.info("农行查询接口返回值：" + orderDetail);
			  		returnMap = com.alibaba.fastjson.JSON.parseObject(orderDetail);
			  		String orderItems =  ConvertUtils.getString(returnMap.get("OrderItems")).trim();
			  		if(false == StringUtils.isBlank(orderItems)){
			  			List<Map<String,Object>> lst =  com.alibaba.fastjson.JSON.parseObject(orderItems,List.class);
				  		//替换OrderItems
				  		returnMap.remove("OrderItems");
				  		returnMap.put("OrderItems", lst);
			  		}		     
			    }
			}else{
				//商户订单查询失败
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

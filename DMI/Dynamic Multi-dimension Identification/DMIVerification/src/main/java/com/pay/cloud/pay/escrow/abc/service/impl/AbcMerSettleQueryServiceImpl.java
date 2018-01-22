package com.pay.cloud.pay.escrow.abc.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.SettleRequest;
import com.pay.cloud.pay.escrow.abc.service.AbcPayService;
import com.pay.cloud.pay.escrow.abc.utils.AbcConstants;
import com.pay.cloud.pay.escrow.abc.utils.AbcParaUtils;
import com.pay.cloud.pay.escrow.abc.utils.ZipUtils;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 对账单查询
 */
@Service("abcMerSettleQueryServiceImpl")
public class AbcMerSettleQueryServiceImpl implements AbcPayService {
	
	//日志
	private static Logger logger = Logger.getLogger(AbcMerSettleQueryServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.abc.service.AbcPayService#perform(java.util.Map)
	 */
	@Override
	public Map<String, Object> perform(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### AbcMerSettleQueryServiceImpl.perform() #####");
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
			//SettleDate 查询日期 必须设定，YYYY/MM/DD
			if(false == ValidateUtils.checkParam(receiveMap,"SettleDate",ValidateUtils.TYPE_DATE,false,10,null,"yyyy/MM/dd",resultMap)){
				return resultMap;
			}
			//ZIP 压缩标识 必须设定，1：压缩，0：不压缩
			if(false == ValidateUtils.checkParam(receiveMap,"ZIP",ValidateUtils.TYPE_CHAR,false,1,new char[]{'0','1'},null,resultMap)){
				return resultMap;
			}
			//资金平台
			String fundId = ConvertUtils.getString(receiveMap.get("FundId")).trim();
			//商户顺序号
			int merNo = Integer.parseInt(AbcParaUtils.getMerNo(fundId));
			//压缩标识
			String zip = ConvertUtils.getString(receiveMap.get("ZIP")).trim();
			//生成交易流水查询请求对象
			SettleRequest tRequest = new SettleRequest();
			tRequest.dicRequest.put("SettleDate",ConvertUtils.getString(receiveMap.get("SettleDate")).trim());  //对账日期YYYY/MM/DD （必要信息）
			tRequest.dicRequest.put("ZIP",zip);
			//传送商户对账单下载请求并取得对账单
//			JSON json = tRequest.postRequest();
			JSON json = tRequest.extendPostRequest(merNo);
			logger.info(json.toString());
			//判断商户对账单下载结果状态，进行后续操作
			String returnCode = json.GetKeyValue("ReturnCode");
			String errorMessage = json.GetKeyValue("ErrorMessage");
			if (AbcConstants.RETURN_CODE_SUCCESS.equals(returnCode)){
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			    //商户对账单下载成功，生成对账单对象
				returnMap.put("ReturnCode", json.GetKeyValue("ReturnCode"));
				returnMap.put("ErrorMessage", json.GetKeyValue("ErrorMessage"));
				returnMap.put("TrxType", json.GetKeyValue("TrxType"));
				returnMap.put("SettleDate", json.GetKeyValue("SettleDate"));
				returnMap.put("SettleType", json.GetKeyValue("SettleType"));
				returnMap.put("NumOfPayments", json.GetKeyValue("NumOfPayments"));
				returnMap.put("SumOfPayAmount", json.GetKeyValue("SumOfPayAmount"));
				returnMap.put("NumOfRefunds", json.GetKeyValue("NumOfRefunds"));
				returnMap.put("SumOfRefundAmount", json.GetKeyValue("SumOfRefundAmount"));
				returnMap.put("ZIPDetailRecords", json.GetKeyValue("ZIPDetailRecords"));//请求报文指定压缩时该字段有值
				if(AbcConstants.ZIP.equals(zip)){
					returnMap.put("DetailRecords", ZipUtils.gunzip(json.GetKeyValue("ZIPDetailRecords")));//请求报文压缩时解压缩
				}else{
					returnMap.put("DetailRecords", json.GetKeyValue("DetailRecords"));//请求报文不指定压缩时该字段有值
				}	
			}
			else {
			    //商户账单下载失败
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

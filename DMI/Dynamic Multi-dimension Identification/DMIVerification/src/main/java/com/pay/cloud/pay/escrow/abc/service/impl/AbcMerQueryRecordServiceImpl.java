package com.pay.cloud.pay.escrow.abc.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.QueryTrnxRecords;
import com.pay.cloud.pay.escrow.abc.service.AbcPayService;
import com.pay.cloud.pay.escrow.abc.utils.AbcConstants;
import com.pay.cloud.pay.escrow.abc.utils.AbcParaUtils;
import com.pay.cloud.pay.escrow.abc.utils.ZipUtils;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 交易流水查询
 */
@Service("abcMerQueryRecordServiceImpl")
public class AbcMerQueryRecordServiceImpl implements AbcPayService {

	//日志
	private static Logger logger = Logger.getLogger(AbcMerQueryRecordServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.abc.service.AbcPayService#perform(java.util.Map)
	 */
	@Override
	public Map<String, Object> perform(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### AbcMerQueryRecordServiceImpl.perform() #####");
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
			//SettleStartHour 查询开始时间 必须设定，时间段0-23点
			if(false == ValidateUtils.checkParam(receiveMap,"SettleStartHour",ValidateUtils.TYPE_INTEGER,false,2,null,null,resultMap)){
				return resultMap;
			}
			//SettleEndHour 查询结束时间 必须设定，时间段0-23点
			if(false == ValidateUtils.checkParam(receiveMap,"SettleEndHour",ValidateUtils.TYPE_INTEGER,false,2,null,null,resultMap)){
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
			QueryTrnxRecords tRequest = new QueryTrnxRecords();
			tRequest.dicRequest.put("SettleDate",ConvertUtils.getString(receiveMap.get("SettleDate")).trim());  //查询日期YYYY/MM/DD （必要信息）
			tRequest.dicRequest.put("SettleStartHour",ConvertUtils.getString(receiveMap.get("SettleStartHour")).trim());  //查询开始时间段（0-23）
			tRequest.dicRequest.put("SettleEndHour",ConvertUtils.getString(receiveMap.get("SettleEndHour")).trim());  //查询截止时间段（0-23）
			tRequest.dicRequest.put("ZIP",ConvertUtils.getString(receiveMap.get("ZIP")).trim());
			//传送交易流水查询请求并取得交易流水
//			JSON json = tRequest.postRequest();
			JSON json = tRequest.extendPostRequest(merNo);
			logger.info(json.toString());
			//判断交易流水查询结果状态，进行后续操作
			String returnCode = json.GetKeyValue("ReturnCode");
			String errorMessage = json.GetKeyValue("ErrorMessage");
			if (AbcConstants.RETURN_CODE_SUCCESS.equals(returnCode))
			{
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			    //交易流水查询成功，生成交易流水对象
				returnMap.put("ReturnCode", json.GetKeyValue("ReturnCode"));
				returnMap.put("ErrorMessage", json.GetKeyValue("ErrorMessage"));
				returnMap.put("TrxType", json.GetKeyValue("TrxType"));
				returnMap.put("ZIPDetailRecords", json.GetKeyValue("ZIPDetailRecords"));//请求报文指定压缩时该字段有值
				if(AbcConstants.ZIP.equals(zip)){
					returnMap.put("DetailRecords", ZipUtils.gunzip(json.GetKeyValue("ZIPDetailRecords")));//请求报文压缩时解压缩
				}else{
					returnMap.put("DetailRecords", json.GetKeyValue("DetailRecords"));//请求报文不指定压缩时该字段有值
				}
			}
			else {
			    //交易流水查询失败
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

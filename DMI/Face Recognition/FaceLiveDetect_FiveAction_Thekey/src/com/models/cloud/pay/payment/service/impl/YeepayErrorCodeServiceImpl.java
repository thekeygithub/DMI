package com.models.cloud.pay.payment.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.models.cloud.pay.payment.service.ErrorCodeService;
import com.models.cloud.util.hint.Hint;


/**
 * yeepay错误处理统一服务
 */
@Service("yeepayErrorCodeServiceImpl")
public class YeepayErrorCodeServiceImpl implements ErrorCodeService {

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.ErrorCodeService#getResultMap(java.util.Map)
	 */
	@Override
	public Map<String, String> getSupplyResultMap(Map<String, String> result) throws Exception {
		Map<String,String> resultMap = new HashMap<String,String>();
		//默认返回值
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc",Hint.SYS_SUCCESS.getMessage());
		//校验入口参数
		if (null == result || 0==result.size() || result.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
		}
		if (result.containsKey("error_code")){
			String errorCode =  String.valueOf(result.get("error_code")).trim();
			if("200000".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14101_SYSTEM_ABNORMAL.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14101_SYSTEM_ABNORMAL.getMessage());
			}else if("200001".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14102_PARAMS_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14102_PARAMS_INVALID.getMessage());
			}else if("200002".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14103_NO_DATA_RETURN.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14103_NO_DATA_RETURN.getMessage());
			}else if("200012".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14104_ACCOUNT_FROZEN.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14104_ACCOUNT_FROZEN.getMessage());
			}else if("200014".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14105_ACCOUNT_NOT_EXISTS.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14105_ACCOUNT_NOT_EXISTS.getMessage());
			}else if("200024".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14106_DECRYPT_FAIL.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14106_DECRYPT_FAIL.getMessage());
			}else if("200025".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
			}else if("200026".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14108_CHECK_PARAM_FAIL.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14108_CHECK_PARAM_FAIL.getMessage());
			}else if("200027".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14109_NO_ORDER.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14109_NO_ORDER.getMessage());
			}else if("200028".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14110_NSUFFICIENT_BALANCE.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14110_NSUFFICIENT_BALANCE.getMessage());
			}else if("200029".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14111_NO_REFUND_ORDER.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14111_NO_REFUND_ORDER.getMessage());
			}else if("200030".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14112_REPEAT_REFUND_ORDER.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14112_REPEAT_REFUND_ORDER.getMessage());
			}else if("200038".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14113_METHOD_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14113_METHOD_INVALID.getMessage());
			}else if("200039".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14114_INTERVAL_GTR_31.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14114_INTERVAL_GTR_31.getMessage());
			}else if("200040".equals(errorCode)){
				resultMap.put("resultCode", Hint.SP_14115_DATA_TOO_MUCH.getCodeString());
				resultMap.put("resultDesc", Hint.SP_14115_DATA_TOO_MUCH.getMessage());
			}else{
				resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			}
		}
		
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.payment.service.ErrorCodeService#getPaymentResultMap(java.util.Map)
	 */
	@Override
	public Map<String, String> getPaymentResultMap(Map<String, String> result) throws Exception {
		return null;
	}

}

package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.TdOrderRefundServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 退款记录查询
 */
@Service("searchPayOrderRefundInterfaceImpl")
public class SearchPayOrderRefundInterfaceImpl implements DoServiceInterface {

	//日志
	private static final Logger logger = Logger.getLogger(SearchPayOrderRefundInterfaceImpl.class);

	//商户退款接口
	@Resource(name = "tdOrderRefundServiceGWImpl")
    private TdOrderRefundServiceGW tdOrderRefundServiceGW;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		//定义返回结果
	    Map<String, Object> resultMap = new HashMap<String,Object>();
		//入口参数校验
		if(!checkParam(receiveMap,resultMap)){
			return resultMap;
		}
		try {
			resultMap = tdOrderRefundServiceGW.payOrderRefundQuery(receiveMap);
		} catch (Exception e) {
			logger.error("系统错误：" + e.getMessage(), e);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
	    return resultMap;
	}
	
	//校验参数是否合法
	private boolean checkParam(Map<String, Object> receiveMap,Map<String, Object> resultMap) {
		//校验退款交易单号是否为空
		if(ValidateUtils.isBlank(receiveMap, "payRefundOrderId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payRefundOrderId"));
            return false;
		}
		//校验退款交易单号是否位数字
		if(false == ValidateUtils.isNumber(receiveMap.get("payRefundOrderId"))
				|| false == ValidateUtils.checkLength(receiveMap.get("payRefundOrderId").toString().trim(), 10, 19)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "payRefundOrderId"));
            return false;
		}
		//校验订单号是否为空
		if(ValidateUtils.isBlank(receiveMap, "merOrderId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "merOrderId"));
            return false;
		}
		//校验订单号是否合法
		if( false == ValidateUtils.isRegex(receiveMap.get("merOrderId").toString().trim(),"[A-Za-z0-9_]+")
				|| false == ValidateUtils.checkLength(receiveMap.get("merOrderId").toString().trim(), 1, 100)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "merOrderId"));
            return false;
		}
		return true;
	}
}

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
 * Created by yacheng.ji on 2016/4/21.
 */
@Service("payOrderRefundInterfaceImpl")
public class PayOrderRefundInterfaceImpl implements DoServiceInterface {
    
	//日志处理
    private static final Logger logger = Logger.getLogger(PayOrderRefundInterfaceImpl.class);

	//商户退款接口
	@Resource(name = "tdOrderRefundServiceGWImpl")
    private TdOrderRefundServiceGW tdOrderRefundServiceGW;

	@Override
    public Map<String, Object> doService(Map<String, Object> receiveMap){
		
		//定义返回结果
		Map<String, Object> resultMap = new HashMap<String, Object>();	
		//入口参数校验
		if(!checkParam(receiveMap,resultMap)){
			return resultMap;
		}
        try {
            //生成订单，后续又任务列表处理
        	resultMap = tdOrderRefundServiceGW.prePayOrderRefund(receiveMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
	
	//校验入口参数是否合法
	private boolean checkParam(Map<String, Object> receiveMap,Map<String, Object> resultMap) {
		if(ValidateUtils.isBlank(receiveMap, "oriTdId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "oriTdId"));
            return false;
		}
		//校验退款交易单号是否位数字
		if(false == ValidateUtils.isNumber(receiveMap.get("oriTdId"))
				|| false == ValidateUtils.checkLength(receiveMap.get("oriTdId").toString().trim(), 10, 19)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "oriTdId"));
            return false;
		}
		if(ValidateUtils.isBlank(receiveMap, "merOrderId")){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "merOrderId"));
            return false;
		}
		//校验订单号是否合法
		if(false == ValidateUtils.isRegex(receiveMap.get("merOrderId").toString().trim(),"[A-Za-z0-9_]+")
				|| false == ValidateUtils.checkLength(receiveMap.get("merOrderId").toString().trim(), 1, 100)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "merOrderId"));
            return false;
		}
		if(ValidateUtils.isBlank(receiveMap, "cause") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cause"));
            return false;
		}
		if(false == ValidateUtils.checkLength(receiveMap.get("cause").toString().trim(), 1, 100)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "cause"));
            return false;
		}
		if(ValidateUtils.isBlank(receiveMap, "payRefundMoney") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payRefundMoney"));
            return false;
		}
		if(!ValidateUtils.isMoney(receiveMap.get("payRefundMoney").toString(), false)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "payRefundMoney"));
            return false;
		}
		if(ValidateUtils.isBlank(receiveMap, "transBusiType") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "transBusiType"));
            return false;
		}
		//校验交易业务类型是否位数字
		if(false == ValidateUtils.isNumber(receiveMap.get("transBusiType"))
				|| false == ValidateUtils.checkLength(receiveMap.get("transBusiType").toString().trim(), 1, 5)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "transBusiType"));
            return false;
		}
		if(ValidateUtils.isBlank(receiveMap, "terminalType") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "terminalType"));
            return false;
		}
		//校验终端类型是否位数字
		if(false == ValidateUtils.isNumber(receiveMap.get("terminalType"))
				|| false == ValidateUtils.checkLength(receiveMap.get("terminalType").toString().trim(), 1, 5)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "terminalType"));
            return false;
		}
		if(ValidateUtils.isBlank(receiveMap, "systemId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "systemId"));
            return false;
		}	
		if(false == ValidateUtils.isRegex(receiveMap.get("systemId").toString().trim(),"[A-Za-z0-9_]+")
				|| false == ValidateUtils.checkLength(receiveMap.get("systemId").toString().trim(), 1, 20)){
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "systemId"));
            return false;
		}
		return true;
	}
}

package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.models.cloud.gw.service.supplier.SupplierServiceGW;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

@Service("freezeSupplierInterfaceImpl")
public class FreezeSupplierInterfaceImpl implements DoServiceInterface {
	
	@Resource(name = "actServIntfServiceGWImpl")
	private ActServIntfServiceGW actServIntfServiceGW;

	@Resource(name = "supplierServiceGWImpl")
	protected SupplierServiceGW supplierServiceGW;

	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (!checkParam(receiveMap, resultMap)) {
			return resultMap;
		}
		resultMap = actServIntfServiceGW.checkPrivilege(receiveMap);
		if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			return resultMap;
		}
		return execute(receiveMap);
	}

	public boolean checkParam(Map<String, Object> receiveMap,
			Map<String, Object> resultMap) {
		
//		操作用户id	operId	String	管理员或者渠道商户用户	N
//		账户id	actId	String		N
//		状态	state	String	1冻结2解冻	N
//		备注	remark	String		Y
		if(ValidateUtils.isEmpty(receiveMap, "operId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "operId"));
            return false;
		}

		if(ValidateUtils.isEmpty(receiveMap, "actId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "actId"));
            return false;
		}

		if(ValidateUtils.isEmpty(receiveMap, "state") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "state"));
            return false;
		}
		// TODO
		if(receiveMap.get("operId").toString().equals(receiveMap.get("actId").toString())){
			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
            return false;
		}
		return true;
	}

	public Map<String, Object> execute(Map<String, Object> receiveMap) {
		return supplierServiceGW.freezeSupplier(receiveMap);
	}

}

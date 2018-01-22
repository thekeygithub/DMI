package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.pay.cloud.gw.service.supplier.SupplierServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

@Service("subSupplierQueryInterfaceImpl")
public class SubSupplierQueryInterfaceImpl implements DoServiceInterface {

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
		if(ValidateUtils.isEmpty(receiveMap, "operId")){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "operId"));
            return false;
		}
		if(ValidateUtils.isEmpty(receiveMap, "actId")){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "actId"));
            return false;
		}
		return true;
	}


	public Map<String, Object> execute(Map<String, Object> receiveMap) {
		return supplierServiceGW.subSupplierQuery(receiveMap);
	}

}

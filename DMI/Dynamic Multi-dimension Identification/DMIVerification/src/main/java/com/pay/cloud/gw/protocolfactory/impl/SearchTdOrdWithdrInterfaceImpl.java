package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.pay.cloud.gw.service.trade.SupplierTradeServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

@Service("searchTdOrdWithdrInterfaceImpl")
public class SearchTdOrdWithdrInterfaceImpl implements DoServiceInterface {
	

	@Resource(name = "actServIntfServiceGWImpl")
	private ActServIntfServiceGW actServIntfServiceGW;	
	@Resource(name="supplierTradeServiceGWImpl")
	private SupplierTradeServiceGW supplierTradeServiceGW;
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
		return supplierTradeServiceGW.searchTdOrdWithdr(receiveMap);
	}

	public boolean checkParam(Map<String, Object> receiveMap,
			Map<String, Object> resultMap) {
		if(ValidateUtils.isEmpty(receiveMap, "operId") ){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
	        resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "operId"));
	        return false;
		}
		
		return true;

	}
}

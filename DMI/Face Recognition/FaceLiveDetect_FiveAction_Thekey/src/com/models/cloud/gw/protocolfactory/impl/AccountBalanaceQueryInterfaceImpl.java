package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.models.cloud.gw.service.trade.SupplierTradeServiceGW;
import com.models.cloud.util.hint.Hint;

/**
 * 商户账户余额查询接口
 * @author qingsong.li
 *
 */
@Service("accountBalanaceQueryInterfaceImpl")
public class AccountBalanaceQueryInterfaceImpl implements DoServiceInterface {

	@Resource(name = "actServIntfServiceGWImpl")
	private ActServIntfServiceGW actServIntfServiceGW;
	
	@Resource(name = "supplierTradeServiceGWImpl")
	private SupplierTradeServiceGW supplierTradeServiceGW;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = actServIntfServiceGW.checkPrivilege(receiveMap);
		if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			return resultMap;
		}
		return supplierTradeServiceGW.accountBalanaceQuery(receiveMap);
	}

}

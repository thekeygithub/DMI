package com.pay.cloud.gw.protocolfactory.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.pay.cloud.gw.service.trade.SupplierTradeServiceGW;
import com.pay.cloud.util.hint.Hint;

@Service("refundClearDataInterfaceImpl")
public class RefundClearDataInterfaceImpl  implements DoServiceInterface  {

	@Resource(name = "actServIntfServiceGWImpl")
	private ActServIntfServiceGW actServIntfServiceGW;
	@Resource(name="supplierTradeServiceGWImpl")
	private SupplierTradeServiceGW SupplierTradeServiceGW;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = actServIntfServiceGW.checkPrivilege(receiveMap);
		if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			return resultMap;
		}


		String startdate = receiveMap.get("startdate")==null?"":receiveMap.get("startdate").toString();
		String enddate = receiveMap.get("enddate")==null?"":receiveMap.get("enddate").toString();
		
		if(startdate.equals("")){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "startdate"));
			return resultMap;
		}
		if(enddate.equals("")){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "enddate"));
			return resultMap;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			 format.setLenient(false);
			 format.parse(startdate);
		} catch (Exception e) {
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "startdate"));
			return resultMap;
		} 
		
		try {
			 format.setLenient(false);
			 format.parse(enddate);
		} catch (Exception e) {
			resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "enddate"));
			return resultMap;
		}
		
		try{
			Map<String, Object> map = SupplierTradeServiceGW.refundClearData(receiveMap);
			return map;
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
		
	}

}

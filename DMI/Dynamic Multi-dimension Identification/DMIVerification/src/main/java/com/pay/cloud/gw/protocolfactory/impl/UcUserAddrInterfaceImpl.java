package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.UcUserAddrServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;
/***
 * 用户中心-添加，修改地址
 * @author enjuan.ren
 *
 */
@Service("ucUserAddrInterfaceImpl")
public class UcUserAddrInterfaceImpl implements DoServiceInterface{
	
	private static final Logger logger = Logger.getLogger(UcUserAddrInterfaceImpl.class);
	
	@Resource(name="ucUserAddrServiceGWImpl")
	private UcUserAddrServiceGW ucUserAddrServiceGWImpl;
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 String payUserId = String.valueOf(receiveMap.get("userId")).trim();
		 if(ValidateUtils.isEmpty(payUserId)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32003_NOT_NULL_PAY_USER_ID.getMessage().replace("{param}", "userId"));
	            return resultMap;
	        }
		 String recvAddrDet=String.valueOf(receiveMap.get("recvAddrDet")).trim();
		 if(ValidateUtils.isEmpty(recvAddrDet)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32005_NOT_NULL_RECV_ADDR_DET.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32005_NOT_NULL_RECV_ADDR_DET.getMessage().replace("{param}", "recvAddrDet"));
	            return resultMap;
	       }
		 if (recvAddrDet.length()>500) {
			 resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
             resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "recvAddrDet"));
             return resultMap;
		}
		 String phone=String.valueOf(receiveMap.get("phone")).trim();
		 if(ValidateUtils.isEmpty(phone)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32006_NOT_NULL_PHONE.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32006_NOT_NULL_PHONE.getMessage().replace("{param}", "phone"));
	            return resultMap;
	       }
		 if(!ValidateUtils.isPhoneNumber(phone)){
	            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "phone"));
	            return resultMap;
	        }
		 String recvMan=String.valueOf(receiveMap.get("recvMan")).trim();
		 if(ValidateUtils.isEmpty(recvMan)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32007_NOT_NULL_RECV_MAN.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32007_NOT_NULL_RECV_MAN.getMessage().replace("{param}", "recvMan"));
	            return resultMap;
	       }
		 if (recvMan.length()>30) {
			 resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
	         resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "recvMan"));
	         return resultMap;
		}
		 String ucAddrStatId=String.valueOf(receiveMap.get("ucAddrStatId")).trim();
		 if(ValidateUtils.isEmpty(ucAddrStatId)){
	            resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32008_NOT_NULL_UCADDR_STATID.getCodeString());
	            resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32008_NOT_NULL_UCADDR_STATID.getMessage().replace("{param}", "ucAddrStatId"));
	            return resultMap;
	       }
		 if (!ValidateUtils.isNumber(ucAddrStatId)|| (!"1".equals(ucAddrStatId) && !"2".equals(ucAddrStatId) && !"3".equals(ucAddrStatId))) {
			 resultMap.put("resultCode", Hint.UC_CENETER_ADDR_32011_NUMBER_FORMAT_UCADDR_STATID.getCodeString());
			  resultMap.put("resultDesc", Hint.UC_CENETER_ADDR_32011_NUMBER_FORMAT_UCADDR_STATID.getMessage().replace("{param}", "ucAddrStatId"));
			  return resultMap;
		}
		 String pGendId=String.valueOf(receiveMap.get("pGendId")).trim();
		 if (!("").equals(pGendId) &&pGendId!=null) {
			 if (!ValidateUtils.isNumber(pGendId)|| (!"0".equals(pGendId) && !"1".equals(pGendId)) ) {
				 resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32009_NUMBER_FORMAT_PGEND_ID.getCodeString());
				 resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32009_NUMBER_FORMAT_PGEND_ID.getMessage().replace("{param}", "pGendId"));
				 return resultMap;
			 }
		}
		 String chanActId=String.valueOf(receiveMap.get("chanActId")).trim();
		 if (!("").equals(chanActId) && chanActId!=null) {
			 if (!ValidateUtils.isNumber(chanActId)) {
				 resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32010_NUMBER_FORMAT_CHAN_ACTID.getCodeString());
				 resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32010_NUMBER_FORMAT_CHAN_ACTID.getMessage().replace("{param}", "chanActId"));
				 return resultMap;
			 }
		}
		 String provId=String.valueOf(receiveMap.get("provId")).trim();
		 if (!("").equals(provId)&&provId!=null) {
			 if (provId.length()>8) {
				 resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32012_NUMBER_FORMAT_PROV_ID.getCodeString());
				 resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32012_NUMBER_FORMAT_PROV_ID.getMessage().replace("{param}", "provId"));
				 return resultMap;
			}
		}
		 String cityId=String.valueOf(receiveMap.get("cityId")).trim();
		 if (!("").equals(cityId)&&cityId!=null) {
			 if (cityId.length()>8) {
				 resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32013_NUMBER_FORMAT_CITY_ID.getCodeString());
				 resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32013_NUMBER_FORMAT_CITY_ID.getMessage().replace("{param}", "cityId"));
				 return resultMap;
			 }
		}
		 String distId=String.valueOf(receiveMap.get("distId")).trim();
		 if (!("").equals(distId)&&distId!=null) {
			 if (distId.length()>8) {
				 resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32014_NUMBER_FORMAT_DIST_ID.getCodeString());
				 resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32014_NUMBER_FORMAT_DIST_ID.getMessage().replace("{param}", "distId"));
				 return resultMap;
			 }
		}
		 String commId=String.valueOf(receiveMap.get("commId")).trim();
		 if (!("").equals(commId)&&commId!=null) {
			 if (commId.length()>8) {
				 resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32015_NUMBER_FORMAT_COMM_ID.getCodeString());
				 resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32015_NUMBER_FORMAT_COMM_ID.getMessage().replace("{param}", "commId"));
				 return resultMap;
			 }
		}
		 try {
			resultMap=ucUserAddrServiceGWImpl.saveUcUserAddr(receiveMap);
		} catch (Exception e) {
			 logger.error("系统错误：" + e.getMessage(), e);
			 resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
	         resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		return resultMap;
	}

}

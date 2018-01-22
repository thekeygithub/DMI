package com.pay.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.supplier.ActServIntfServiceGW;
import com.pay.cloud.gw.service.supplier.SupplierServiceGW;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.util.hint.Propertie;

@Service("addSupplierInterfaceImpl")
public class AddSupplierInterfaceImpl implements DoServiceInterface {
	
	@Resource(name = "actServIntfServiceGWImpl")
	private ActServIntfServiceGW actServIntfServiceGW;

	@Resource(name = "supplierServiceGWImpl")
	protected SupplierServiceGW supplierServiceGW;

	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			if (!checkParam(receiveMap, resultMap)) {
				return resultMap;
			}
			resultMap = actServIntfServiceGW.checkPrivilege(receiveMap);
			if (!resultMap.get("resultCode").equals(
					Hint.SYS_SUCCESS.getCodeString())) {
				return resultMap;
			}
			return execute(receiveMap);
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
			return resultMap;
		}
	
	}

	public boolean checkParam(Map<String, Object> receiveMap,
			Map<String, Object> resultMap) {
		// 操作用户id operId String 管理员用户id或者渠道商户账户id N
		if (ValidateUtils.isEmpty(receiveMap, "operId")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "operId"));
			return false;
		}
		
		
		// SYS_10009_PARAM_INVALID_ERROR
		if(!ValidateUtils.isEmpty(receiveMap, "telNo")){
			if(!ValidateUtils.isPhoneNumber(receiveMap.get("telNo"))){
				resultMap.put("resultCode",
						Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR
						.getMessage().replace("{param}", "telNo"));
				return false;
			}
		}
		// 医保中心ID micId String
		if (ValidateUtils.isEmpty(receiveMap, "micId")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "micId"));
			return false;
		}
		// 商户类型 supplyTypeId String 1 渠道商户 0合作商户
		if (ValidateUtils.isEmpty(receiveMap, "supplyTypeId")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "supplyTypeId"));
			return false;
		}
		String supplyTypeId = receiveMap.get("supplyTypeId").toString();
		if((!String.valueOf(BaseDictConstant.CHAN_ACT_FLAG_YES).equals(supplyTypeId))&&!String.valueOf(BaseDictConstant.CHAN_ACT_FLAG_NO).equals(supplyTypeId)){
			resultMap.put("resultCode",
					Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR
					.getMessage().replace("{param}", "supplyTypeId"));
			return false;
		}
		
		if(String.valueOf(BaseDictConstant.CHAN_ACT_FLAG_YES).equals(supplyTypeId)){
			// 手机号 telNo
			if (ValidateUtils.isEmpty(receiveMap, "telNo")) {
				resultMap.put("resultCode",
						Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
						.getMessage().replace("{param}", "telNo"));
				return false;
			}
		}
		
		// 企业名称 entName String N
		if (ValidateUtils.isEmpty(receiveMap, "entName")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "entName"));
			return false;
		}
		
		//保证金 guart
		if(!ValidateUtils.isEmpty(receiveMap, "guart")){
			if(!ValidateUtils.isMoney(receiveMap.get("guart").toString(), false)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "guart"));
				return false;
			}
		}

		// 经度 longitude String
		// 纬度 latitude String
		// 企业类型 entType String 1.网站，2药店 N
		if (ValidateUtils.isEmpty(receiveMap, "entType")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "entType"));
			return false;
		}
		// 企业法人 bussinessEntity String Y
		// 社保局编码 miEntCode String Y
		// 公司地址 entAddr String Y
		// 邮政编码 zipCode String Y
		if(receiveMap!=null && receiveMap.get("zipCode")!=null && !"".equals(receiveMap.get("zipCode"))){
			if(!ValidateUtils.checkLength(receiveMap.get("zipCode").toString(), 6, 6) || !ValidateUtils.isNumber(receiveMap.get("zipCode"))){
				resultMap.put("resultCode",
						Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR
						.getMessage().replace("{param}", "zipCode"));
				return false;
			}
		}
		// 联系人 linkMan String N
		if (ValidateUtils.isEmpty(receiveMap, "linkMan")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "linkMan"));
			return false;
		}
		// 联系电话1 phone1 String N
		if (ValidateUtils.isEmpty(receiveMap, "phone1")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "phone1"));
			return false;
		}
//		String phone1 = receiveMap.get("phone1").toString();
//		if(!ValidateUtils.isPhoneNumber(phone1)){
//			resultMap.put("resultCode",
//					Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR
//					.getMessage().replace("{param}", "phone1"));
//			return false;
//		}
		// 联系电话2 phone2 String Y TODO
//		String phone2 = receiveMap.get("phone2").toString();
//		if((!ValidateUtils.isEmpty(receiveMap, "phone2"))&&!ValidateUtils.isPhoneNumber(phone2)){
//			resultMap.put("resultCode",
//					Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR
//					.getMessage().replace("{param}", "phone2"));
//			return false;
//		}
//		String phone3 = receiveMap.get("phone3").toString();
//		if((!ValidateUtils.isEmpty(receiveMap, "phone3"))&&!ValidateUtils.isPhoneNumber(phone3)){
//			resultMap.put("resultCode",
//					Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR
//					.getMessage().replace("{param}", "phone3"));
//			return false;
//		}
		// 联系邮箱 entEmail String Y
		if((!ValidateUtils.isEmpty(receiveMap, "entEmail"))&&!ValidateUtils.isEmail(receiveMap.get("entEmail").toString())){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "entEmail"));
            return false;
        }
		// 经营范围 entScope String Y
		// 有效时间开始日期 entStartDate String yyyyMMdd N
		if (ValidateUtils.isEmpty(receiveMap, "entStartDate")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "entStartDate"));
			return false;
		}
		// 有效时间结束日期 entEndDate String yyyyMMdd N
		if (ValidateUtils.isEmpty(receiveMap, "entEndDate")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "entEndDate"));
			return false;
		}
		if (!ValidateUtils.isDate(receiveMap.get("entStartDate").toString(), "yyyyMMdd")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR
					.getMessage()+ "entStartDate");
			return false;
		}
		if (!ValidateUtils.isDate(receiveMap.get("entEndDate").toString(), "yyyyMMdd")) {
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR
					.getMessage()+ "entEndDate");
			return false;
		}
		if(Integer.parseInt(receiveMap.get("entStartDate").toString())>=Integer.parseInt(receiveMap.get("entEndDate").toString())){
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR
					.getMessage()+ "entEndDate>entStartDate");
			return false;
		}
		// 企业图片 imageId String Y
		// 备注 remark String Y

		// 银行卡号 bankAccount String Y
		// 银行卡所有人姓名 actName String Y
		if (!(ValidateUtils.isEmpty(receiveMap, "bankAccount")
				&& ValidateUtils.isEmpty(receiveMap, "actName") || (!ValidateUtils
				.isEmpty(receiveMap, "bankAccount"))
				&& (!ValidateUtils.isEmpty(receiveMap, "actName")))) {
			resultMap.put("resultCode",
					Hint.SP_12019_ACCOUNT_NUM_NAME_REQUIRED.getCodeString());
			resultMap.put("resultDesc",
					Hint.SP_12019_ACCOUNT_NUM_NAME_REQUIRED.getMessage());
			return false;
		}
		// 银行编号 bankCode Y
		// 银行支行名称 branchBankName String Y
		// 银行id或者名称选其一
		if((!ValidateUtils.isEmpty(receiveMap, "bankAccount"))&&ValidateUtils.isEmpty(receiveMap, "bankCode")&&ValidateUtils.isEmpty(receiveMap, "bankName")){
			resultMap.put("resultCode",
					Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR
					.getMessage().replace("{param}", "bankCode||bankName"));
			return false;
		}		
		// 账户类型 accountType 
		// 默认对私 Y

		// 固定账户只能能生成渠道账户,渠道账户只能生成合作账户
//		 if((receiveMap.get("operId").toString().equals(Propertie.APPLICATION.value("cms.interface.spActId")) && "0".equals(supplyTypeId))
//				 || (!receiveMap.get("operId").toString().equals(Propertie.APPLICATION.value("cms.interface.spActId")) && !"1".equals(supplyTypeId))){
//		
//			resultMap.put("resultCode",
//						Hint.SYS_10002_PARAM_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR
//						.getMessage()+ "operId||supplyTypeId");
//			return false;
//		 }
		Long operId = Long.parseLong(receiveMap.get("operId")==null?"-1":receiveMap.get("operId").toString());
		 if(operId.toString().equals(Propertie.APPLICATION.value("cms.interface.spActId")) && "1".equals(supplyTypeId)){
			 
		 }else if(!operId.toString().equals(Propertie.APPLICATION.value("cms.interface.spActId")) && "0".equals(supplyTypeId)){
			 
		 }else{
			 resultMap.put("resultCode",
						Hint.SYS_10002_PARAM_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR
						.getMessage()+ "operId||supplyTypeId");
			return false;
		 }
		
		return true;
	}

	public Map<String, Object> execute(Map<String, Object> receiveMap) {

		return supplierServiceGW.addSupplier(receiveMap);
	}

}

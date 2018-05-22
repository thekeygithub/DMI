package com.models.cloud.gw.service.payuser.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.common.dict.entity.DimBankCardBase;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.gw.service.payuser.RealNameServiceGW;
import com.models.cloud.pay.payuser.entity.ActPerson;
import com.models.cloud.pay.payuser.entity.PayUser;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.proplat.service.BankImageService;
import com.models.cloud.util.*;
import com.models.cloud.util.hint.Hint;

@Service("realNameServiceGWImpl")
public class RealNameServiceGWImpl implements RealNameServiceGW {

	private static Logger logger = Logger.getLogger(RealNameServiceGWImpl.class);
	
	@Resource(name="payUserServiceImpl")
	private PayUserService payUserService;
	@Resource
	private BankImageService bankImageServiceImpl;
	
	@Override
	public Map<String, Object> queryRealNameInfoGW(Map<String, Object> receiveMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String userId = String.valueOf(receiveMap.get("userId")).trim();
		logger.info("查询实名认证账号:" + userId);
		PayUser payUser = payUserService.findByPayUserId(userId);
		if(payUser == null || payUser.getActId() == null){
			resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
			resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
			return resultMap;
		}
		ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
		if(null == actPerson){
			resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
			return resultMap;
		}
		String realNameFlag = String.valueOf(actPerson.getRealNameFlag()).trim();
		if(!realNameFlag.equals("1")){
			realNameFlag = "0";
		}
		if(realNameFlag.equals("1")){
			logger.info("已实名认证");
			String realNameChannel = DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString();
			if(null != actPerson.getpCertTypeId()){
				realNameChannel = actPerson.getpCertTypeId().toString();
			}
			String bankName = "";
			String bankTypeName = "";
			String bankIcon = "";
			String bankNo = "";
			String mobile = "";
			if(realNameChannel.equals(DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString())){
				String bankCode = EncryptUtils.aesDecrypt(actPerson.getAuthBankAccount(), CipherAesEnum.CARDNOKEY);//银行卡号
				DimBankCardBase dimBankCardBase = CacheUtils.getBankCardInfo(bankCode);
				if(null != dimBankCardBase){
					bankName = dimBankCardBase.getBankName();
					String cardTypeDesc = String.valueOf(dimBankCardBase.getCardTypeDesc()).trim();
					if("借记卡".equals(cardTypeDesc)){
						bankTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_DEBIT_CARD_INT;
					}else if("贷记卡".equals(cardTypeDesc)){
						bankTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_CREDIT_CARD_INT;
					}else {
						bankTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_UNKNOWN;
					}
					if(dimBankCardBase.getBankId() != null){
						bankIcon = bankImageServiceImpl.getBankImageUrl(dimBankCardBase.getBankId());
					}
				}
				bankNo = EncryptUtils.aesDecrypt(actPerson.getAuthBankAccount(), CipherAesEnum.CARDNOKEY);
				bankNo = "********".concat(bankNo.substring(bankNo.length() - 4, bankNo.length()));
				mobile = actPerson.getAuthMobile();
			}else if(realNameChannel.equals(DimDictEnum.REAL_NAME_THREE_ELEMENTS_HUIYUE.getCodeString())){
				bankNo = "";
				mobile = payUser.getPayUserCode().trim();
			}
			String idNumber = actPerson.getpCertNo();//身份证号
			idNumber = idNumber.substring(0, 1).concat("****************").concat(idNumber.substring(idNumber.length() - 1, idNumber.length()));
			resultMap.put("realNameName", actPerson.getpName());//某某某
			resultMap.put("realNameCardNo", idNumber);//1********************1
			resultMap.put("userIdCardNo", actPerson.getpCertNo());
			resultMap.put("userIcon", "");//用户头像
			resultMap.put("realNameBankNo", bankNo);//**************6278
			resultMap.put("realNamePhone", mobile);//15810011006
			resultMap.put("bankName", bankName);
			resultMap.put("bankTypeName", bankTypeName);
			resultMap.put("bankIcon", bankIcon);
			resultMap.put("realNameChannel", realNameChannel);
		}else{
			logger.info("未实名认证！");
		}
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("realNameStatus", realNameFlag);
		return resultMap;
	}
}

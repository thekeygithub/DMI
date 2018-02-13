package com.models.cloud.gw.service.payuser.impl;

import java.util.*;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.gw.service.payuser.CardServiceGW;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.pay.payment.service.impl.YeepayPaymentServiceImpl;
import com.models.cloud.pay.payuser.entity.ActPBank;
import com.models.cloud.pay.payuser.service.ActPBankService;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.proplat.service.BankImageService;
import com.models.cloud.util.*;
import com.models.cloud.util.hint.Hint;

@Service("cardServiceGWImpl")
public class CardServiceGWImpl implements CardServiceGW {
	private static Logger logger = Logger.getLogger(CardServiceGWImpl.class);
	
	@Resource
	private YeepayPaymentServiceImpl yeepayPaymentServiceImpl;
	@Resource
	private PayUserServiceGW payUserServiceGWImpl;
	@Resource
	private BankImageService bankImageServiceImpl;
	@Resource
	private PayUserService payUserServiceImpl;
	@Resource
	private ActPBankService actPBankServiceImpl;

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryCardList(Map<String, Object> inputMap)
			throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("请求查询绑卡信息接口，请求参数："+inputMap);
		}
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payToken = (String)inputMap.get("payToken");//通行证
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//硬件id
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(payToken != null && !"".equals(payToken)){
			payToken = String.valueOf(inputMap.get("payToken")).trim();//通行证
			if(logger.isInfoEnabled()){
				logger.info("验证支付通行证是否合法");
			}
			Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
			if(logger.isInfoEnabled()){
				logger.info("验证支付通行证是否合法,redisPayToken:"+redisPayToken);
			}
			if(null == redisPayToken || redisPayToken.size() == 0){
				resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
				return resultMap;
			}
			if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
				resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
				return resultMap;
			}
			if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
				resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
				return resultMap;
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("验证账户是否登录");
		}
		/*Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			if(logger.isInfoEnabled()){
				logger.info("验证账户是否登录，返回失败");
			}
			return checkPayUserMap;
		}*/
		
		Map<String, String> responseMap = new HashMap<String, String>();
		String identityType = BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID;
		if(logger.isInfoEnabled()){
			logger.info("请求易宝调用查询绑卡信息列表接口，请求参数：accountId:"+accountId+" ,identityType:"+identityType);
		}
		responseMap = yeepayPaymentServiceImpl.bankCardBindQuery(accountId, identityType);
		if(logger.isInfoEnabled()){
			logger.info("请求易宝调用查询绑卡信息列表接口，返回结果:"+responseMap);
		}
		
		if(responseMap.isEmpty() || responseMap.size()==0){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}else if(responseMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}else if(responseMap.containsKey("error_code") || responseMap.containsKey("error_msg")){
			resultMap.put("resultCode", responseMap.get("error_code"));
			resultMap.put("resultDesc", responseMap.get("error_msg"));
			return resultMap;
		}else if(!responseMap.containsKey("error_code") && !responseMap.containsKey("error_msg") && !responseMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			resultMap.put("accountId", accountId);
			
			String cardlistJson = responseMap.get("cardlist");
			if(logger.isInfoEnabled()){
				logger.info("cardlistJson: "+cardlistJson);
			}
			if(cardlistJson != null && !"".equals(cardlistJson) && !cardlistJson.contains("[]")){
				List<Map<String,Object>> cardList = JSON.parseObject(cardlistJson, List.class);
				List<Map<String,Object>> newCardList = new ArrayList<Map<String,Object>>(cardList.size());
				Map<String, Map<String,Object>> newCardMap = new HashMap<String, Map<String,Object>>();
				/**
				 * 绑卡列表只包括有效期内和未解绑的
				 */
				if(cardList.size() > 0){
					for(Map<String,Object> map : cardList){
						if(ValidateUtils.isBlank((Integer)map.get("bindvalidthru"))){
							long cardTime = Long.valueOf((Integer)map.get("bindvalidthru"))*1000L;
							String validDate = DateUtils.dateToString(new Date(cardTime), "yyyy-MM-dd");
							String curTime = DateUtils.dateToString(new Date(), "yyyy-MM-dd");
							if(logger.isInfoEnabled()){
								logger.info(map.get("bindid") + " 卡有效期："+validDate +" 当前时间："+curTime);
							}
							TreeMap<String,Object> newMap = new TreeMap<String,Object>();
							newMap.put("bindId", map.get("bindid"));//绑卡 ID
							newMap.put("cardTop", map.get("card_top"));//卡号前 6 位
							newMap.put("cardLast", map.get("card_last"));//卡号后 4 位
							newMap.put("cardName", map.get("card_name"));//卡名称
							newMap.put("bindValidthru", validDate);//绑卡有效期
							String phone = (String)map.get("phone");
							phone = phone.replace(phone.substring(3,7), "****");
							newMap.put("phone", phone);//银行预留手机号
							newMap.put("bankCode", map.get("bankcode"));//银行缩写
							
							//卡图片信息
							String payActTypeId = "";
							String cardTypeName = "";
							String cardType = String.valueOf(map.get("cardtype"));
							if(String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_DEBIT_CARD_INT).equals(cardType)){
								payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_DEBIT_CARD.getCode());
								cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_DEBIT_CARD_INT;
							}else if(String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_CREDIT_CARD_INT).equals(cardType)){
								payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_CREDIT_CARD.getCode());
								cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_CREDIT_CARD_INT;
							}else{
								cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_UNKNOWN;
							}
							
							newMap.put("cardType", cardType);//卡类型
							newMap.put("cardTypeName", cardTypeName);//卡类型名称
							newMap.put("bankIconUrl", bankImageServiceImpl.getBankImageUrl(CacheUtils.getDimSysConfConfValue
									(BaseDictConstant.CUR_PP_FUND_ID), (String)map.get("bankcode"), payActTypeId));//卡图片地址
							
							newCardList.add(newMap);
							newCardMap.put(newMap.get("bindId").toString(), newMap);
						}
					}
				}

				if(newCardList.size() > 0){
					ActPBank selectActPBank = new ActPBank();
					selectActPBank.setFundId(CacheUtils.getDimSysConfConfValue(BaseDictConstant.CUR_PP_FUND_ID));
					selectActPBank.setFromActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_PRIVATE.getCode())));//平台账户类型
					selectActPBank.setActId(Long.valueOf(accountId));
					selectActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
					selectActPBank.setUpdTime(new Date());
					List<ActPBank> actPBankList = actPBankServiceImpl.findActPBankList(selectActPBank);
					if(null != actPBankList && actPBankList.size() > 0){
						String bindId = String.valueOf(actPBankList.get(0).getPayPropC()).trim();
						if(ValidateUtils.isNotEmpty(bindId) && newCardMap.containsKey(bindId)){
							newCardList = new ArrayList<Map<String, Object>>(cardList.size());
							newCardList.add(newCardMap.remove(bindId));
							for(String key : newCardMap.keySet()){
								newCardList.add(newCardMap.get(key));
							}
						}
					}
				}

				resultMap.put("cardList", newCardList);
			}
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> unBindCard(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("请求解绑银行卡接口，请求参数："+inputMap);
		}
		String bindId = String.valueOf(inputMap.get("bindId")).trim();//绑卡 ID
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//通行证
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//硬件id
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(logger.isInfoEnabled()){
			logger.info("验证支付通行证是否合法");
		}
		Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
		if(null == redisPayToken || redisPayToken.size() == 0){
			resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
			resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
			return resultMap;
		}
		if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
			resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("验证账户是否登录");
		}
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			if(logger.isInfoEnabled()){
				logger.info("验证账户是否登录，返回失败");
			}
			return checkPayUserMap;
		}
		
		Map<String, String> responseMap = new HashMap<String, String>();
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("bindid", bindId);
		requestMap.put("identityid", accountId);
		requestMap.put("identitytype", BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID);
		if(logger.isInfoEnabled()){
			logger.info("请求易宝解绑卡接口，请求参数:"+requestMap);
		}
		responseMap = yeepayPaymentServiceImpl.unbindBankcard(requestMap);
		if(logger.isInfoEnabled()){
			logger.info("请求易宝解绑卡接口，返回结果:"+responseMap);
		}
		
		if(null == responseMap || responseMap.size() == 0){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		if(responseMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		if(responseMap.containsKey("error_code")){
			resultMap.put("resultCode", responseMap.get("error_code"));
			resultMap.put("resultDesc", responseMap.get("error_msg"));
			return resultMap;
		}

		actPBankServiceImpl.updateStatusForUnbind(Long.valueOf(accountId), bindId);

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("bindId", bindId);
		resultMap.put("accountId", accountId);
		return resultMap;
	}
	

}

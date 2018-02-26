package com.models.cloud.gw.service.payuser.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.models.cloud.cert.utils.HttpsNoCert;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.pay.escrow.huiyue.constant.HyConstant;
import com.models.cloud.pay.escrow.huiyue.model.HyRequest;
import com.models.cloud.pay.escrow.huiyue.model.HyResponse;
import com.models.cloud.pay.escrow.huiyue.service.HuiYueService;
import com.models.cloud.pay.escrow.huiyue.utils.HyErrorCodeEnum;
import com.models.cloud.pay.payment.service.PaymentService;
import com.models.cloud.pay.payuser.entity.ActPFin;
import com.models.cloud.pay.payuser.entity.ActPerson;
import com.models.cloud.pay.payuser.entity.DimBank;
import com.models.cloud.pay.payuser.entity.PayUser;
import com.models.cloud.pay.payuser.entity.PayUserDevice;
import com.models.cloud.pay.payuser.service.ActPBankService;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.proplat.service.BankImageService;
import com.models.cloud.pay.seq.service.SeqService;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.DateUtils;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.EncryptUtils;
import com.models.cloud.util.IdCreatorUtils;
import com.models.cloud.util.Md5SaltUtils;
import com.models.cloud.util.Md5Util;
import com.models.cloud.util.SmsUtil;
import com.models.cloud.util.UserUtil;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.OperateType;
import com.models.cloud.util.hint.Propertie;
import com.models.secrity.crypto.AesCipher;

@Service("payUserServiceGWImpl")
public class PayUserServiceGWImpl implements PayUserServiceGW {

	@Resource(name="payUserServiceImpl")
	private PayUserService payUserService;
	@Resource(name="redisService")
	private RedisService redisService;
	@Resource(name="yeepayPaymentServiceImpl")
	private PaymentService yeepayPaymentServiceImpl;
	@Resource(name="seqServiceImpl")
	private SeqService seqServiceImpl;
	@Resource(name="bankImageServiceImpl")
	private BankImageService bankImageServiceImpl;
	@Resource
	private ActPBankService actPBankServiceImpl;
	@Resource
	private PayUserService payUserServiceImpl;
	@Resource
	private HuiYueService huiYueServiceImpl;

	private static final Logger logger = Logger.getLogger(PayUserServiceGWImpl.class);

	/**
	 * 创建用户支付通行证
	 * @param accountId
	 * @param hardwareId
	 * @return
	 * @throws Exception
	 */
	public String setPayTokenInRedis(Long accountId, String hardwareId) throws Exception {
		String payTokenSrc = IdCreatorUtils.getPayToken();
		String payTokenMD5 = Md5Util.getKeyedDigest(payTokenSrc, "MD5", String.valueOf(accountId), "UTF-8");
		if(logger.isInfoEnabled()){
			logger.info("生成支付通行证 payTokenSrc=" + payTokenSrc + ",accountId=" + accountId + ",payTokenMD5=" + payTokenMD5);
		}
		String redisKey = "paymentToken_".concat(String.valueOf(accountId));
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("payToken", payTokenMD5);
		valueMap.put("hardwareId", hardwareId);
		int payTokenRedisExpire = Integer.parseInt(Propertie.APPLICATION.value("payToken.redis.expire"));
		if(logger.isInfoEnabled()){
			logger.info("存入Redis中 redisKey=" + redisKey + ",redisValue=" + valueMap + ",payTokenRedisExpire=" + payTokenRedisExpire);
		}
		redisService.setMap(redisKey, valueMap);
		redisService.expire(redisKey, payTokenRedisExpire);
		return payTokenMD5;
	}

	/**
	 * 获取支付通行证
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getPayTokenInRedis(String accountId) throws Exception {
		String redisKey = "paymentToken_".concat(accountId);
		Map<String, String> payTokenMap = redisService.getMap(redisKey);
		if(logger.isInfoEnabled()){
			logger.info("获取支付通行证 redisKey=" + redisKey + ",redisValue=" + payTokenMap);
		}
		return payTokenMap;
	}

	/**
	 * 清除Redis中支付通行证
	 * @param accountId
	 * @throws Exception
     */
	public void removePayTokenInRedis(String accountId) throws Exception {
		String redisKey = "paymentToken_".concat(accountId);
		redisService.delete(redisKey);
	}

	/**
	 * 验证用户易保支付密码
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> verifyUserPaymentPassword(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> verifyUserPaymentPassword 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String payPassword = String.valueOf(inputMap.get("payPassword")).trim().toLowerCase();//支付密码
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID

		String payPwdAllowInputCnt = CacheUtils.getDimSysConfConfValue(BaseDictConstant.PAY_PWD_FAIL_CNT);
		String payPwdLockTime = CacheUtils.getDimSysConfConfValue(BaseDictConstant.PAY_PWD_LOCK_TIME);
		String payPwdLockTimeRedisKey = "payPwdLockTime_".concat(accountId);
		String payPwdLockTimeRedisValue = String.valueOf(redisService.get(payPwdLockTimeRedisKey)).trim();
		if(logger.isInfoEnabled()){
			logger.info("检测当前用户支付密码是否被锁定 redisKey=" + payPwdLockTimeRedisKey + ",redisValue=" + payPwdLockTimeRedisValue);
		}
		if("1".equals(payPwdLockTimeRedisValue)){
			this.removePayTokenInRedis(accountId);
			resultMap.put("resultCode", Hint.USER_11010_PAY_PASSWORD_LOCKED.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11010_PAY_PASSWORD_LOCKED.getMessage().replace("{X}", payPwdAllowInputCnt).replace("{Y}", payPwdLockTime));
			return resultMap;
		}

		Map<String, Object> checkPayUserMap = payUserService.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}
		ActPerson actPerson = (ActPerson) checkPayUserMap.get("actPerson");
		String payPasswordDb = String.valueOf(actPerson.getPayPwd()).trim();
		String payPasswordMd5Salt = Md5SaltUtils.encodeMd5Salt(payPassword, actPerson.getSaltVal());
		if(logger.isInfoEnabled()){
			logger.info("用户输入的支付密码：" + payPassword + " DM5加盐后：" + payPasswordMd5Salt + " 用户设置的支付密码：" + payPasswordDb);
		}
		if(ValidateUtils.isEmpty(payPasswordDb)){
			resultMap.put("resultCode", Hint.USER_11009_NOT_SET_PAY_PASSWORD.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11009_NOT_SET_PAY_PASSWORD.getMessage());
			return resultMap;
		}

		this.removePayTokenInRedis(accountId);
		String payPwdSurplusCntRedisKey = "payPwdSurplusCnt_".concat(accountId);
		if(!payPasswordDb.toLowerCase().equals(payPasswordMd5Salt.toLowerCase())){
			String payPwdSurplusCntRedisValue = String.valueOf(redisService.get(payPwdSurplusCntRedisKey)).trim();
			int payPwdSurplusCnt;
			if(ValidateUtils.isEmpty(payPwdSurplusCntRedisValue)){
				payPwdSurplusCnt = Integer.parseInt(payPwdAllowInputCnt) - 1;
			}else {
				payPwdSurplusCnt = Integer.parseInt(payPwdSurplusCntRedisValue) - 1;
			}
			if(logger.isInfoEnabled()){
				logger.info("用户输入的支付密码不正确 支付密码允许输入错误次数=" + payPwdAllowInputCnt +
						",支付密码剩余可输入次数=" + payPwdSurplusCnt + ",支付密码锁定时长=" + payPwdLockTime);
			}
			if(payPwdSurplusCnt <= 0){
				resultMap.put("resultCode", Hint.USER_11010_PAY_PASSWORD_LOCKED.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11010_PAY_PASSWORD_LOCKED.getMessage().replace("{X}", payPwdAllowInputCnt).replace("{Y}", payPwdLockTime));
				redisService.delete(payPwdSurplusCntRedisKey);
				redisService.set(payPwdLockTimeRedisKey, "1");
				redisService.expire(payPwdLockTimeRedisKey, Integer.parseInt(payPwdLockTime) * 60);
			}else{
				redisService.set(payPwdSurplusCntRedisKey, String.valueOf(payPwdSurplusCnt));
				redisService.expire(payPwdSurplusCntRedisKey, Integer.parseInt(Propertie.APPLICATION.value("payToken.redis.expire")));
				resultMap.put("resultCode", Hint.USER_11005_PAY_PASSWORD_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11005_PAY_PASSWORD_ERROR.getMessage().replace("{X}", String.valueOf(payPwdSurplusCnt)));
			}
			return resultMap;
		}
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("accountId", accountId);
		resultMap.put("payToken", this.setPayTokenInRedis(actPerson.getActId(), hardwareId));
		redisService.delete(payPwdSurplusCntRedisKey);
		return resultMap;
	}

	/**
	 * 判断银行卡类型
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> bankCardNoType(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> bankCardNoType 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String cardNo = String.valueOf(inputMap.get("cardNo")).trim();//卡号
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//支付通行证
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID
		if(logger.isInfoEnabled()){
			logger.info("验证支付通行证是否合法");
		}
		Map<String, String> redisPayToken = this.getPayTokenInRedis(accountId);
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
		Map<String, Object> checkPayUserMap = payUserService.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}

		/*********************调用接口判断银行卡类型 begin***********************/
		if(logger.isInfoEnabled()){
			logger.info("请求易宝接口查询银行卡类型，请求业务参数：" + cardNo);
		}
		Map<String, String> yeepayResultMap = yeepayPaymentServiceImpl.bankCardCheck(EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY));
		if(null != yeepayResultMap){
			yeepayResultMap.remove("cardno");
		}
		if(logger.isInfoEnabled()){
			logger.info("接口响应报文：" + yeepayResultMap);
		}
		if(null == yeepayResultMap || yeepayResultMap.size() == 0 || yeepayResultMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		if(yeepayResultMap.containsKey("error_code")){
			String error_code = String.valueOf(yeepayResultMap.get("error_code")).trim();
			//请求频次过高，请过几秒重试
			if("601101".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13049_601101.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13049_601101.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
		/*********************调用接口判断银行卡类型 end***********************/

		String cardType = String.valueOf(yeepayResultMap.get("cardtype")).trim();
		if(ValidateUtils.isEmpty(cardType)){
			cardType = "-1";
		}
		String cardTypeName;
		String payActTypeId = "";
		if(String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_DEBIT_CARD_INT).equals(cardType)){
			cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_DEBIT_CARD_INT;
			payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_DEBIT_CARD.getCode());
		}else if(String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_CREDIT_CARD_INT).equals(cardType)){
			cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_CREDIT_CARD_INT;
			payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_CREDIT_CARD.getCode());
		}else{
			cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_UNKNOWN;
		}

		String bankName = yeepayResultMap.get("bankname");
		if(ValidateUtils.isEmpty(bankName)){
			bankName = "";
		}
		String bankCode = yeepayResultMap.get("bankcode");
		if(ValidateUtils.isEmpty(bankCode)){
			bankCode = "";
		}
		String isValid = yeepayResultMap.get("isvalid");
		if(ValidateUtils.isEmpty(isValid)){
			isValid = "";
		}

		String fundId = CacheUtils.getDimSysConfConfValue(BaseDictConstant.CUR_PP_FUND_ID);

		if((String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_DEBIT_CARD_INT).equals(cardType) ||
		   String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_CREDIT_CARD_INT).equals(cardType)) && ValidateUtils.isNotEmpty(bankCode)){
			DimBank selectDimBank = new DimBank();
			selectDimBank.setFundId(fundId);
			List<DimBank> dimBankList = payUserService.queryDimBankList(selectDimBank);
			if(null == dimBankList || dimBankList.size() == 0){
				if(logger.isInfoEnabled()){
					logger.info("当前资金平台(" + fundId + ")下未找到支持的银行列表信息");
				}
				resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
				return resultMap;
			}
			boolean isSupport = false;
			for(DimBank dimBank : dimBankList){
				if(bankCode.equals(dimBank.getPpBankCode()) && dimBank.getPayActTypeId().intValue() == Short.valueOf(payActTypeId).intValue()){
					isSupport = true;
					break;
				}
			}
			if(!isSupport){
				resultMap.put("resultCode", Hint.BANK_CARD_CHECK_13077.getCodeString());
				resultMap.put("resultDesc", Hint.BANK_CARD_CHECK_13077.getMessage());
				return resultMap;
			}
		}

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());

		String cardNoTemp = EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY);
		String cardNoBefore = EncryptUtils.aesEncrypt(cardNoTemp.substring(0, cardNoTemp.length() - 4), CipherAesEnum.CARDNOKEY);
		String cardNoLastFour = cardNoTemp.substring(cardNoTemp.length() - 4);
		resultMap.put("cardNo", cardNoBefore.concat("|").concat(cardNoLastFour));

		resultMap.put("cardType", cardType);
		resultMap.put("cardTypeName", cardTypeName);
		resultMap.put("bankName", bankName.trim());
		resultMap.put("bankCode", bankCode.trim());
		resultMap.put("bankIconUrl", bankImageServiceImpl.getBankImageUrl(fundId, bankCode, payActTypeId));
		resultMap.put("isValid", isValid.trim());
		return resultMap;
	}
	
	/**
	 * 验证登录验证码
	 * @param receiveMap
	 * @return
	 */
	public Map<String, Object> checkVerCode(Map<String, Object> receiveMap){
		
		String userCode = receiveMap.get("userCode")==null?"":receiveMap.get("userCode").toString();
		String verifyCode = receiveMap.get("verifyCode")==null?"":receiveMap.get("verifyCode").toString();
		String hardwareId = receiveMap.get("hardwareId").toString();
		
		String verifyCodeListKey = "verifyCodeList_"+hardwareId;
		
		String errorCount = Propertie.APPLICATION.value("login.error.count")==null?"3":Propertie.APPLICATION.value("login.error.count");
		int errorCount_i = Integer.parseInt(errorCount.trim());
		
		//redis 获取硬件id错误次数
		String hardwareIdCount = redisService.get("verifyCodeList_count_"+hardwareId);
		hardwareIdCount = hardwareIdCount ==null?"1":hardwareIdCount;
		int hardwareIdCount_i = Integer.parseInt(hardwareIdCount.trim());
		
		int phoneCount_i = 1;
		//redis 获取手机错误次数
		String phoneCount = redisService.get("verifyCodeList_count_"+userCode);
		phoneCount = phoneCount ==null?"1":phoneCount;
		phoneCount_i = Integer.parseInt(phoneCount.trim());

		String verifyCodeList_str = redisService.get(verifyCodeListKey);
		logger.info("获取验证码信息："+verifyCodeList_str);
		//如果错误次数超过预定次数
		logger.info("判断错误次数，硬件id"+hardwareIdCount_i+"手机号错误次数："+phoneCount_i);
		if(hardwareIdCount_i>errorCount_i || phoneCount_i>errorCount_i){
			if(verifyCodeList_str==null || "".equals(verifyCodeList_str)){

				Map<String, Object> returnMap = new HashMap<String,Object>();
				returnMap.put("resultCode", Hint.LOGIN_VERIFYCODE_35001_ERROR.getCodeString());
		        returnMap.put("resultDesc", Hint.LOGIN_VERIFYCODE_35001_ERROR.getMessage());
				return returnMap;
			}else{
				Map<String,Object> verifyCodeListMap = JSONArray.parseObject(verifyCodeList_str);
				
				List<String> verifyCodeList = verifyCodeListMap==null?new ArrayList<String>():(List<String>)verifyCodeListMap.get("verifyCodeList");
				if(verifyCodeList!=null && verifyCodeList.size()>0){
					for(int i = 0;i < verifyCodeList.size();i ++){
						String verifyCode_ = verifyCodeList.get(i);
						if(verifyCode.equals("")){
							Map<String, Object> returnMap = new HashMap<String,Object>();
							returnMap.put("resultCode", Hint.LOGIN_VERIFYCODE_35001_ERROR.getCodeString());
					        returnMap.put("resultDesc", Hint.LOGIN_VERIFYCODE_35001_ERROR.getMessage());
							return returnMap;
						}
						if(verifyCode_.toLowerCase().equals(verifyCode.toLowerCase())){
							Map<String, Object> returnMap = new HashMap<String,Object>();
							returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					        returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
							return returnMap;
						}
					}
					Map<String, Object> returnMap = new HashMap<String,Object>();
					returnMap.put("resultCode", Hint.LOGIN_VERIFYCODE_35002_ERROR.getCodeString());
			        returnMap.put("resultDesc", Hint.LOGIN_VERIFYCODE_35002_ERROR.getMessage());
					return returnMap;
				}else{
					Map<String, Object> returnMap = new HashMap<String,Object>();
					returnMap.put("resultCode", Hint.LOGIN_VERIFYCODE_35001_ERROR.getCodeString());
			        returnMap.put("resultDesc", Hint.LOGIN_VERIFYCODE_35001_ERROR.getMessage());
					return returnMap;
				}
			}
		}
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("failCount", hardwareIdCount_i>phoneCount_i?hardwareIdCount_i:phoneCount_i);
		returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return returnMap;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> receiveMap) throws Exception {
		//本地查询用户
		PayUser user = payUserService.findByPayUserPhone(receiveMap.get("userCode").toString());
		
		//检查验证码
		Map<String, Object> verCodeMap = checkVerCode(receiveMap);
		if(!verCodeMap.get("resultCode").toString().equals(Hint.SYS_SUCCESS.getCodeString())){
			return verCodeMap;
		}
		
		if(user!= null){
			
			/**密码连续输错次数*/
			int TM_PWD_FAIL_CNT = Integer.valueOf(CacheUtils.getDimSysConfConfValue("APP_PWD_FAIL_CNT")); 
			/**密码连续输错计数时间段*/
			int TM_PWD_FAIL_TIME = Integer.valueOf(CacheUtils.getDimSysConfConfValue("APP_PWD_FAIL_TIME"));
			/**密码连续输错锁定时间段*/
			int TM_PWD_LOCK_TIME = Integer.valueOf(CacheUtils.getDimSysConfConfValue("APP_PWD_LOCK_TIME"));
			
			
			if(user.getPwdFailCnt()==TM_PWD_FAIL_CNT&&System.currentTimeMillis() - user.getPwdFailTime().getTime() >= Long.parseLong(TM_PWD_LOCK_TIME+"") * 60000){
				//锁定时间超出，解锁
				payUserService.updatePassErrorCount(user.getPayUserId(),0);//记录密码错误次数清零
				user.setPwdFailCnt(0);//当前用户错误次数清零
			}else if(user.getPwdFailCnt()==TM_PWD_FAIL_CNT&&System.currentTimeMillis() - user.getPwdFailTime().getTime() <= Long.parseLong(TM_PWD_LOCK_TIME+"") * 60000){
				//登录失败，锁定时间内，已锁定，密码第5次输入错误,已五次输入错误密码，请等待十五分钟后再试
				payUserService.saveLoginLog(0,1,"密码输入错误5次，账号已被锁定，请15分钟之后重试",receiveMap, user);
				
				Map<String, Object> returnMap = new HashMap<String,Object>();
				returnMap.put("resultCode", Hint.USER_25014_PASS_CONTINUOUS_ERRO.getCodeString());
		        returnMap.put("resultDesc", java.text.MessageFormat.format(Hint.USER_25014_PASS_CONTINUOUS_ERRO.getMessage(), TM_PWD_FAIL_CNT ,TM_PWD_LOCK_TIME));
				return returnMap;
			}
			
			//获取账户
			ActPerson actPerson=payUserService.findActPersonById(user.getActId());
			String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
			if(UserUtil.comparePass(pass,user)){

				Hint hint =UserUtil.isUserFalse(user);
				if(hint.getCode()!=0){
					//登录失败，用户被禁用或注销
					payUserService.saveLoginLog(0,1,"用户被禁用或注销",receiveMap, user);
					return ConvertUtils.genReturnMap(hint);
				}
				
				
				//更新最后登录时间
				Date date=new Date();
				user.setUpdTime(date);
				user.setLastLoginTime(date);
				user.setDeviceCode(receiveMap.get("hardwareId").toString());
				
				//记录登录日志
				payUserService.saveLoginLog(1,1,"登录成功",receiveMap, user);
				//更新最后登录时间
				payUserService.updateLastLoginTime(user);
				//记录密码错误次数清零
				payUserService.updatePassErrorCount(user.getPayUserId(),0);
				user.setPwdFailCnt(0);//当前用户错误次数清零
				//记录终端设备标识
				PayUserDevice payUserDevice=new PayUserDevice();
				payUserDevice.setActId(user.getActId());
				payUserDevice.setUserAlias("");//别名
				payUserDevice.setDeviceCode(receiveMap.get("hardwareId").toString());//设备号
				PayUserDevice pud=payUserService.findPayUserDevice(payUserDevice);
				if(pud==null){
					//记录不存在创建
					payUserDevice.setPayUserId(user.getPayUserId());//支付平台用户ID
					payUserDevice.setUpdTime(date);
					payUserDevice.setCrtTime(date);	
					payUserDevice.setLastLoginTime(date);//最近登录时间
					payUserService.savePayUserDevice(payUserDevice);
				}else{
					pud.setUpdTime(date);
					pud.setLastLoginTime(date);//最近登录时间
					payUserService.updatePayUserDevice(pud);
				}
				
				Map<String, Object> returnMap = new HashMap<String,Object>();
				returnMap.put("phone", user.getMobile());
				returnMap.put("userId", user.getPayUserId());
				returnMap.put("userCode",ConvertUtils.conceal(user.getPayUserCode(), 3, 6));
				returnMap.put("accountId", user.getActId());
				returnMap.put("realNameStatus", "0");
				returnMap.put("realNameName", "");
				returnMap.put("realNameCardNo", "");
				if(actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag().toString().equals(String.valueOf(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE))){
					returnMap.put("realNameStatus", actPerson.getRealNameFlag());
					String pname = actPerson.getpName();
					if(pname!=null && pname.length()>0){
						returnMap.put("realNameName", "*"+pname.substring(1));
					}
					String certNo = actPerson.getpCertNo();
					if(certNo!=null && certNo.length()>0){
						returnMap.put("realNameCardNo", certNo.substring(0,1)+"****************"+certNo.substring(certNo.length()-1, certNo.length()));
					}
					String realNameChannel = DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString();
					if(null != actPerson.getpCertTypeId()){
						realNameChannel = actPerson.getpCertTypeId().toString();
					}
					returnMap.put("realNameChannel", realNameChannel);
				}
				if(actPerson.getPayPwd().trim().equals("")){
					returnMap.put("allowPament", "1");//不允许支付
				}else{
					returnMap.put("allowPament", "0");//允许支付
				}
				return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
			}else{
				
				String userCode = receiveMap.get("userCode")==null?"":receiveMap.get("userCode").toString();
				String hardwareId = receiveMap.get("hardwareId").toString();
				
				String hardwareIdCountKey = "verifyCodeList_count_"+hardwareId;
				//redis 获取硬件id错误次数
				String hardwareIdCount = redisService.get(hardwareIdCountKey);
				hardwareIdCount = hardwareIdCount ==null?"1":hardwareIdCount;
				int hardwareIdCount_i = Integer.parseInt(hardwareIdCount.trim());
				
				String phoneCountKey = "verifyCodeList_count_"+userCode;
				int phoneCount_i = 1;
				//redis 获取手机错误次数
				String phoneCount = redisService.get(phoneCountKey);
				phoneCount = phoneCount ==null?"1":phoneCount;
				phoneCount_i = Integer.parseInt(phoneCount.trim());
				//验证码添加
				redisService.set(hardwareIdCountKey,(hardwareIdCount_i+1)+"");
				redisService.expire(hardwareIdCountKey, DateUtils.getDaySurplusSecond());
				redisService.set(phoneCountKey,(phoneCount_i+1)+"");
				redisService.expire(phoneCountKey, DateUtils.getDaySurplusSecond());
				
				
				//密码错误
				int num=0;
				if(user.getPwdFailTime()==null||user.getPwdFailCnt()==0){
					//计数不存在，初次计数
					num=1;
				}else{
					if(System.currentTimeMillis() - user.getPwdFailTime().getTime() <= Long.parseLong(TM_PWD_FAIL_TIME+"") * 60000){
						//计数时间在固定时间段内，叠加计数
						num=user.getPwdFailCnt()+1;
					}else{
						//计数时间在固定时间段外，重新计数
						num=1;
					}
				}
				payUserService.updatePassErrorCount(user.getPayUserId(),num);//记录密码错误次数
				
				Map<String, Object> returnMap = new HashMap<String,Object>();
				returnMap.put("failCount",num);
				returnMap.put("failCnt", TM_PWD_FAIL_CNT-num);
				if(num==TM_PWD_FAIL_CNT){
					//密码第5次输入错误,已五次输入错误密码，请等待十五分钟后再试
					payUserService.saveLoginLog(0,1,"密码输入错误5次，账号已被锁定，请15分钟之后重试",receiveMap, user);
					returnMap.put("resultCode", Hint.USER_25014_PASS_CONTINUOUS_ERRO.getCodeString());
			        returnMap.put("resultDesc", java.text.MessageFormat.format(Hint.USER_25014_PASS_CONTINUOUS_ERRO.getMessage(), TM_PWD_FAIL_CNT ,TM_PWD_LOCK_TIME));
					return returnMap;
				}else{
					payUserService.saveLoginLog(0,1,"密码验证不通过",receiveMap, user);
//					return ConvertUtils.genReturnMap(returnMap,Hint.USER_25008_VALIDATEPASS_FAILED);
					returnMap.put("resultCode", Hint.USER_25010_USER_LOGIN_FAILED.getCodeString());
			        returnMap.put("resultDesc", Hint.USER_25010_USER_LOGIN_FAILED.getMessage());
					return returnMap;
					
				}
			}
		}else{
			//用户不存在
//			return ConvertUtils.genReturnMap(Hint.USER_25009_USER_UNDEFINED);
			return ConvertUtils.genReturnMap(Hint.USER_25010_USER_LOGIN_FAILED);
		}
	}
	
	@Override
	public Map<String, Object> unifiedLogin(Map<String, Object> receiveMap) throws Exception {
		return payUserService.unifiedLogin(receiveMap);
	}

	public Map<String, Object> getLoginToken(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> getLoginToken 输入参数：" + inputMap);
		}
		String redisKey = new AesCipher().generateKeyToBase64(256);
		String redisValue = JSON.toJSONString(inputMap);
		String token = EncryptUtils.aesEncrypt(redisKey, CipherAesEnum.H5LOGINTOKEN);
		int loginTokenRedisExpire = Integer.parseInt(Propertie.APPLICATION.value("loginToken.redis.expire"));
		if(logger.isInfoEnabled()){
			logger.info("用户相关信息存入Redis中 token=" + token + ",redisKey=" + redisKey + ",redisValue=" + redisValue + ",loginTokenRedisExpire=" + loginTokenRedisExpire);
		}
		redisService.set(redisKey, redisValue);
		redisService.expire(redisKey, loginTokenRedisExpire);
		if(logger.isInfoEnabled()){
			logger.info("<<保存成功");
		}
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		responseMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		responseMap.put("token", token);
		return responseMap;
	}
	
	public Map<String, Object> loginForToken(Map<String, Object> inputMap) throws Exception {
		return payUserService.loginForToken(inputMap);
	}
	
	@Override
	public Map<String, Object> loginOut(Map<String, Object> receiveMap) throws Exception {
		//查询用户
		PayUser user = payUserService.findByPayUserId(receiveMap.get("userId").toString());

//		Map<String, Object> returnMap = isLogin(user,receiveMap.get("hardwareId").toString());
//		if(returnMap.get("resultCode").toString().equals("0")){
			//登录状态有效，登出系统
			payUserService.loginOut(user.getPayUserId());
			//登录状态有效，记录登出日志
			payUserService.saveLoginLog(1,2,"登出系统成功",receiveMap, user);
			
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("userId", receiveMap.get("userId").toString());
			return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
//		}
//		return returnMap;
	}
	
	@Override
	public Map<String, Object> queryBankCardList(Map<String, Object> receiveMap) throws Exception {
		DimBank dimBank=new DimBank();
		dimBank.setFundId(CacheUtils.getDimSysConfConfValue("CUR_PP_FUND_ID"));
		Map<String, Object> returnMap =payUserService.queryBankCardList(dimBank);
		return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
	}
	
	@Override
	public Map<String, Object> queryConfiguration(Map<String, Object> receiveMap) throws Exception {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("systime", DateUtils.dateToString(new Date(), "yyyyMMddHHmmss"));
		returnMap.put("bankcnt", CacheUtils.getDimSysConfConfValue("P_BIND_BANK_MAX"));
		returnMap.put("messagecnt", CacheUtils.getDimSysConfConfValue("SMS_CHK_MAX"));
		returnMap.put("logoUrl", CacheUtils.getDimSysConfConfValue("LOGO_IMAGE_ID"));
		return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
	}

	public Map<String, Object> checkUserCode(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> checkUserCode 输入参数：" + inputMap);
		}
		String userCode = String.valueOf(inputMap.get("userCode")).trim();

		PayUser checkPayUser = payUserService.findByPayUserPhone(userCode);
		if(null != checkPayUser){
			if(logger.isInfoEnabled()){
				logger.info("该用户账号已经被注册 userCode=" + userCode);
			}
			return ConvertUtils.genReturnMap(Hint.SMS_60007_THEPHONE_REGISTERED);
		}
		Map<String, Object> resultMap = ConvertUtils.genReturnMap(Hint.SYS_SUCCESS);
		resultMap.put("userCode", userCode);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> register(Map<String, Object> receiveMap) throws Exception {
		Hint hint=SmsUtil.smsCodeCheck(receiveMap.get("phone").toString(),receiveMap.get("hardwareId").toString(), receiveMap.get("verifyCode").toString(), redisService);
		if(hint.getCode()==0){
			//验证码验证通过
			PayUser tu = payUserService.findByPayUserPhone(receiveMap.get("phone").toString());
			if(tu==null){
				ActSp actSp = payUserService.findByChannelAppId(String.valueOf(receiveMap.get("appId")).trim());//渠道商户ID
				if(null == actSp){
					//获取医保项目ID失败	 
					return ConvertUtils.genReturnMap(Hint.USER_25034_OBTAINMIC_FAILED);
				}
				Date date=new Date();
				//新加账户
				Long actId=seqServiceImpl.updateSeqActId();//SEQUENCE获取账户ID
				ActPerson actPerson=new ActPerson();
				actPerson.setMicId(actSp.getMicId());//医保项目ID	 
				actPerson.setSiChkFlag(Short.valueOf("0"));//社保身份验证标识	0	未验证,1	已验证通过
				actPerson.setRealNameFlag(Short.valueOf("0"));//是否实名认证，1是0否
				actPerson.setActStatId(Short.valueOf("1"));//平台账户状态,1	正常,9	冻结
				actPerson.setValidFlag(Short.valueOf("1"));//是否有效,	1	有效,	0	无效
				actPerson.setPayPwd("");//支付密码初始化为空
				actPerson.setActId(actId);
				actPerson.setSaltVal(Md5SaltUtils.getSalt());
				actPerson.setUpdTime(date);
				actPerson.setCrtTime(date);
				payUserService.saveActPerson(actPerson);
				
				//添加账户资金表
				ActPFin actPFin=new ActPFin();
				actPFin.setActId(actId);
				actPFin.setBal(BigDecimal.ZERO);
				actPFin.setAvalBal(BigDecimal.ZERO);
				actPFin.setGuartInit(BigDecimal.ZERO);
				actPFin.setGuartBal(BigDecimal.ZERO);
				actPFin.setTravel(BigDecimal.ZERO);
				actPFin.setFrz(BigDecimal.ZERO);
				actPFin.setLimPT(BigDecimal.ZERO);
				actPFin.setLimPDay(BigDecimal.ZERO);
				actPFin.setLimWT(BigDecimal.ZERO);
				actPFin.setLimWDay(BigDecimal.ZERO);
				actPFin.setWithDayAmt(BigDecimal.ZERO);
				actPFin.setUpdTime(date);
				actPFin.setCrtTime(date);
				payUserService.saveActPFin(actPFin);//平台私人账户资金属性表
					
				//新加用户
				Long payUserId= seqServiceImpl.updateSeqUserId();//SEQUENCE获取用户ID 
				PayUser u=new PayUser();
				u.setPayUserId(payUserId);
				u.setActId(actId);
				u.setPayUserCode(receiveMap.get("userCode").toString());
				u.setMobile(receiveMap.get("phone").toString());
				String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
				u.setPwd(pass);
				u.setUpdTime(date);
				u.setCrtTime(date);
				u.setLastLoginTime(date);
				u.setDeviceCode(receiveMap.get("hardwareId").toString());//设备号
				u.setChanActId(actSp.getActId());
				payUserService.savePayUser(u);//注册用户并登录成功
					
				//记录登录日志
				payUserService.saveLoginLog(1,1,"登录成功",receiveMap, u);
				//记录终端设备标识
				PayUserDevice payUserDevice=new PayUserDevice();
				payUserDevice.setActId(actId);
				payUserDevice.setUserAlias("");//别名
				payUserDevice.setDeviceCode(receiveMap.get("hardwareId").toString());//设备号
				PayUserDevice pud=payUserService.findPayUserDevice(payUserDevice);
				if(pud==null){
					//记录不存在创建
					payUserDevice.setPayUserId(payUserId);//支付平台用户ID
					payUserDevice.setUpdTime(date);
					payUserDevice.setCrtTime(date);	
					payUserDevice.setLastLoginTime(date);//最近登录时间
					payUserService.savePayUserDevice(payUserDevice);
				}else{
					pud.setUpdTime(date);
					pud.setLastLoginTime(date);//最近登录时间
					payUserService.updatePayUserDevice(pud);
				}
				
				Map<String, Object> returnMap = new HashMap<String,Object>();
				returnMap.put("phone", u.getMobile());
				returnMap.put("userId", u.getPayUserId());
				returnMap.put("userCode", ConvertUtils.conceal(u.getPayUserCode(), 3, 6));
				returnMap.put("accountId", u.getActId());
				return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
			}else{
				//手机号重复
	            return ConvertUtils.genReturnMap(Hint.SMS_60007_THEPHONE_REGISTERED);
			}
		}else{
			//验证码验证不通过
			 return ConvertUtils.genReturnMap(hint);
		}
	}
	
	/**
	 * 统一注册
	 */
	@Override
	public Map<String, Object> register2(Map<String, Object> receiveMap) throws Exception {
		return payUserService.register2(receiveMap);
	}

	public Map<String, Object> syncUser(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> syncUser 输入参数：" + inputMap);
		}
		String userCode = String.valueOf(inputMap.get("userCode")).trim();
		String password = String.valueOf(inputMap.get("password")).trim();
		String phone = String.valueOf(inputMap.get("phone")).trim();
		String appId = String.valueOf(inputMap.get("appId")).trim();
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();

		PayUser checkPayUser = payUserService.findByPayUserPhone(userCode);
		if(null != checkPayUser){
			if(logger.isInfoEnabled()){
				logger.info("注册用户已存在 userCode=" + userCode);
			}
			return ConvertUtils.genReturnMap(Hint.SMS_60007_THEPHONE_REGISTERED);
		}
		ActSp actSp = payUserService.findByChannelAppId(appId);//渠道商户ID
		if(null == actSp){
			//获取医保项目ID失败
			return ConvertUtils.genReturnMap(Hint.USER_25034_OBTAINMIC_FAILED);
		}
		Date date = new Date();
		//新加账户
		Long actId = seqServiceImpl.updateSeqActId();//SEQUENCE获取账户ID
		ActPerson actPerson = new ActPerson();
		actPerson.setMicId(actSp.getMicId());//医保项目ID
		actPerson.setSiChkFlag(Short.valueOf("0"));//社保身份验证标识	0	未验证,1	已验证通过
		actPerson.setRealNameFlag(Short.valueOf("0"));//是否实名认证，1是0否
		actPerson.setActStatId(Short.valueOf("1"));//平台账户状态,1	正常,9	冻结
		actPerson.setValidFlag(Short.valueOf("1"));//是否有效,	1	有效,	0	无效
		actPerson.setPayPwd("");//支付密码初始化为空
		actPerson.setActId(actId);
		actPerson.setSaltVal(Md5SaltUtils.getSalt());
		actPerson.setUpdTime(date);
		actPerson.setCrtTime(date);
		payUserService.saveActPerson(actPerson);

		//添加账户资金表
		ActPFin actPFin = new ActPFin();
		actPFin.setActId(actId);
		actPFin.setBal(BigDecimal.ZERO);
		actPFin.setAvalBal(BigDecimal.ZERO);
		actPFin.setGuartInit(BigDecimal.ZERO);
		actPFin.setGuartBal(BigDecimal.ZERO);
		actPFin.setTravel(BigDecimal.ZERO);
		actPFin.setFrz(BigDecimal.ZERO);
		actPFin.setLimPT(BigDecimal.ZERO);
		actPFin.setLimPDay(BigDecimal.ZERO);
		actPFin.setLimWT(BigDecimal.ZERO);
		actPFin.setLimWDay(BigDecimal.ZERO);
		actPFin.setWithDayAmt(BigDecimal.ZERO);
		actPFin.setUpdTime(date);
		actPFin.setCrtTime(date);
		payUserService.saveActPFin(actPFin);//平台私人账户资金属性表

		//新加用户
		Long payUserId = seqServiceImpl.updateSeqUserId();//SEQUENCE获取用户ID
		PayUser u = new PayUser();
		u.setPayUserId(payUserId);
		u.setActId(actId);
		u.setPayUserCode(userCode);
		u.setMobile(phone);
		u.setPwd(Md5SaltUtils.encodeMd5Salt(password.toLowerCase(), actPerson.getSaltVal()));
		u.setUpdTime(date);
		u.setCrtTime(date);
		u.setLastLoginTime(date);
		u.setDeviceCode(hardwareId);//设备号
		u.setChanActId(actSp.getActId());
		payUserService.savePayUser(u);

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("phone", u.getMobile());
		returnMap.put("userId", u.getPayUserId());
		returnMap.put("userCode", ConvertUtils.conceal(u.getPayUserCode(), 3, 6));
		returnMap.put("accountId", u.getActId());
		return ConvertUtils.genReturnMap(returnMap, Hint.SYS_SUCCESS);
	}
	
	@Override
	public Map<String, Object> restLoginPass(Map<String, Object> receiveMap) throws Exception {
		Hint hint=SmsUtil.smsCodeCheckNoVerifyCode(receiveMap.get("phone").toString(),receiveMap.get("hardwareId").toString(), OperateType.CALLBACKPASS.getCode(), redisService);
		if(hint.getCode()==0){
			String phone=receiveMap.get("phone").toString();
			//获取用户
			PayUser user = payUserService.findByPayUserPhone(phone);
			if(user==null){
				return ConvertUtils.genReturnMap(Hint.USER_25009_USER_UNDEFINED);
			}
			//获取账户
			ActPerson actPerson=payUserService.findActPersonById(user.getActId());
			if(actPerson==null){
				return  ConvertUtils.genReturnMap(Hint.USER_25030_ACCOUNTID_FAILED);
			}
			String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
			payUserService.restLoginPass(phone, pass);
			return ConvertUtils.genReturnMap(Hint.SYS_SUCCESS);
		}else{
			//验证码验证不通过
			 return ConvertUtils.genReturnMap(hint);
		}
	}
	
	@Override
	public Map<String, Object> unifiedResetPassword(Map<String, Object> receiveMap) throws Exception {

//		Hint hint=SmsUtil.smsCodeCheck(receiveMap.get("userCode").toString(),receiveMap.get("hardwareId").toString(), receiveMap.get("verifyCode").toString(), redisService);
//		if(hint.getCode()==0){
			String phone=receiveMap.get("userCode").toString();
			//获取用户
			PayUser user = payUserService.findByPayUserPhone(phone);
			if(user==null){
				return ConvertUtils.genReturnMap(Hint.USER_25009_USER_UNDEFINED);
			}
			//获取账户
			ActPerson actPerson=payUserService.findActPersonById(user.getActId());
			if(actPerson==null){
				return ConvertUtils.genReturnMap(Hint.USER_25030_ACCOUNTID_FAILED);
			}
			String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
			payUserService.restLoginPass(phone, pass);
			return ConvertUtils.genReturnMap(Hint.SYS_SUCCESS);
//		}else{
//			//验证码验证不通过
//			 return ConvertUtils.genReturnMap(hint);
//		}
	}
	
	@Override
	public Map<String, Object> setPaymentPass(Map<String, Object> receiveMap) throws Exception {
		//查询用户
		PayUser user = payUserService.findByPayUserId(receiveMap.get("userId").toString());
//		Map<String, Object> returnMap = isLogin(user,receiveMap.get("hardwareId").toString());
//		if(returnMap.get("resultCode").toString().equals("0")){
			//登录状态有效，修改支付密码		
			//获取账户
			ActPerson actPerson=payUserService.findActPersonById(user.getActId());
			if(actPerson.getPayPwd().trim().equals("")){
				String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
				payUserService.updatePaymentPass(user.getActId(), pass);
				
				Map<String, Object> returnMap = new HashMap<String,Object>();
				returnMap.put("accountId", user.getActId().toString());
				return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
			}else{
				//支付密码存在，不能设置支付密码
				return ConvertUtils.genReturnMap(Hint.USER_25033_SETPAYMENTPASS_FAILED);
			}
//		}
//		return returnMap;
	}
	
	@Override
	public Map<String, Object> setPaymentPass2(Map<String, Object> receiveMap) throws Exception {
		Hint hint=SmsUtil.smsCodeCheckNoVerifyCode(receiveMap.get("phone").toString(),receiveMap.get("hardwareId").toString(), OperateType.SETPAYMENTPASS.getCode(), redisService);
		if(hint.getCode()==0){
			return setPaymentPassCommon(receiveMap);
		}else{
			//验证码验证不通过
			return ConvertUtils.genReturnMap(hint);
		}
	}

	@Override
	public Map<String, Object> setPaymentPass3(Map<String, Object> receiveMap) throws Exception {
		Hint hint=SmsUtil.smsCodeCheckNoVerifyCode(receiveMap.get("phone").toString(),receiveMap.get("hardwareId").toString(), OperateType.REALVALID.getCode(), redisService);
		if(hint.getCode()==0){
			return setPaymentPassCommon(receiveMap);
		}else{
			//验证码验证不通过
			return ConvertUtils.genReturnMap(hint);
		}
	}
	
	private Map<String, Object> setPaymentPassCommon(Map<String, Object> receiveMap) throws Exception {
		//查询用户
		PayUser user = payUserService.findByPayUserId(receiveMap.get("userId").toString());
//		Map<String, Object> returnMap = isLogin(user,receiveMap.get("hardwareId").toString());
//		if(returnMap.get("resultCode").toString().equals("0")){
			//登录状态有效，修改支付密码		
			if(receiveMap.get("interfaceCode").toString().equals("setPayPassword3")){
				if(!unBindAllCard(user.getActId().toString(),BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID)){
					return ConvertUtils.genReturnMap(Hint.USER_25035_UNBINDBACKCARD_FAILED);
				}
			}
			//获取账户
			ActPerson actPerson=payUserService.findActPersonById(user.getActId());
			String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
			payUserService.updatePaymentPass(user.getActId(), pass);
			//删除支付密码错误计数
			redisService.delete("payPwdSurplusCnt_".concat(user.getActId().toString()));
			
			Map<String, Object> returnMap = new HashMap<String,Object>();
			returnMap.put("accountId", user.getActId().toString());
			return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
//		}
//		return returnMap;
	}
	
	/**
	 * 
	 * @Description: 解绑银行卡
	 *  1、先查询绑卡列表
	 *  2、解绑
	 * @Title: unBindAllCard 
	 * @param accountId 账户编号
	 * @param identityType 用户标识类型码：2-用户ID
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean unBindAllCard(String accountId,String identityType) throws Exception{
		Map<String, String> responseMap = new HashMap<String, String>();
		boolean result = false;
		if(logger.isInfoEnabled()){
			logger.info("请求易宝调用查询绑卡信息列表接口，请求参数：accountId:"+accountId+" ,identityType:"+identityType);
		}
		responseMap = yeepayPaymentServiceImpl.bankCardBindQuery(accountId, identityType);
		if(logger.isInfoEnabled()){
			logger.info("请求易宝调用查询绑卡信息列表接口，返回结果:"+responseMap);
		}
		if(responseMap.isEmpty() || responseMap.size()==0
				|| (responseMap != null && responseMap.containsKey("customError")
				|| responseMap != null && (responseMap.containsKey("error_code") || responseMap.containsKey("error_msg")))){
			
			return result;
		}else if(responseMap != null && !responseMap.containsKey("error_code") 
				&& !responseMap.containsKey("error_msg") && !responseMap.containsKey("customError")){
			
			String cardlistJson = responseMap.get("cardlist");
			if(logger.isInfoEnabled()){
				logger.info("cardlistJson: "+cardlistJson);
			}
			if(cardlistJson != null && !"".equals(cardlistJson) && !cardlistJson.contains("[]")){
				List<Map<String,Object>> cardList = JSON.parseObject(cardlistJson, List.class);
				String bindId;
				for(Map<String,Object> map : cardList){
					Map<String, String> requestMap = new HashMap<String, String>();
					bindId = (String)map.get("bindid");
					requestMap.put("bindid", bindId);
					requestMap.put("identityid", accountId);
					requestMap.put("identitytype", identityType);
					if(logger.isInfoEnabled()){
						logger.info("请求易宝解绑卡接口，请求参数:"+requestMap);
					}
					responseMap = yeepayPaymentServiceImpl.unbindBankcard(requestMap);
					if(logger.isInfoEnabled()){
						logger.info("请求易宝解绑卡接口，返回结果:"+responseMap);
					}
					if(responseMap.isEmpty() || responseMap.size()==0
							|| (responseMap != null && responseMap.containsKey("customError")
							|| responseMap != null && (responseMap.containsKey("error_code") || responseMap.containsKey("error_msg")))){
						
						result = false;
						break;
					}else if(responseMap != null && !responseMap.containsKey("error_code") 
							&& !responseMap.containsKey("error_msg") && !responseMap.containsKey("customError")){
						
						result = true;

						/***************************修改私人银行账号表状态 modify by jiyc 2015-05-25 15:23 begin***************************/
						actPBankServiceImpl.updateStatusForUnbind(Long.valueOf(accountId), bindId);
						/***************************修改私人银行账号表状态 modify by jiyc 2015-05-25 15:23 end***************************/
					}
				}
				
			}else{
				if(logger.isInfoEnabled()){
					logger.info("账户编号 "+ accountId+" 没有绑定的卡");
				}
				result = true;
			}
			
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Description: 登录判断
	 * @Title: isLogin 
	 * @param userId 用户编号
	 * @param hardwareId 硬件ID
	 * @return Hint
	 * @throws Exception 
	 */
	public Map<String, Object> isLogin(String userId,String hardwareId) throws Exception{
		//查询用户
		PayUser user = payUserService.findByPayUserId(userId);
		if(user==null){
			//用户不存在
			return ConvertUtils.genReturnMap(Hint.USER_25009_USER_UNDEFINED);
		}
//		if(!hardwareId.equals(user.getDeviceCode())){
//			//设备终端不统一，请重新登录
//			Map<String, Object> returnMap = new HashMap<String,Object>();
//			//查询获取最近一次登录设备信息
//			LogPayUserLogin logPayUserLogin=payUserService.selectByActIdLastLogin(user.getActId());
//			returnMap.put("resultCode", Hint.USER_25011_DEVICEDIFFERENT_FAILED.getCodeString());
//	        returnMap.put("resultDesc", java.text.MessageFormat.format(Hint.USER_25011_DEVICEDIFFERENT_FAILED.getMessage(),DateUtils.dateToString(logPayUserLogin.getLogTime(), "yyyy-MM-dd HH:mm:ss") ,CacheUtils.getDimDictDictName("TD_OPER_CHAN_TYPE_ID",logPayUserLogin.getPhoneOsTypeId().toString())));
//			return returnMap;	
//		}
//		if(user.getCurLoginFlag()==0){
//			//用户未登录
//			return ConvertUtils.genReturnMap(Hint.SYS_1000_NOT_LOGIN_ERROR);
//		}
		//user.getCurLoginFlag()==1&&DateUtils.getDateLength(user.getLastLoginTime(), new Date())<=Integer.valueOf(CacheUtils.getDimSysConfConfValue("P_LOGIN_DAY_MAX"))
		if(user.getCurLoginFlag() == 1){
			return ConvertUtils.genReturnMap(Hint.SYS_SUCCESS);
		}else{
			//登录状态超时失效，请重新登录
			return ConvertUtils.genReturnMap(Hint.USER_25001_LOGINSTATUS_FAILED);
		}
	}

	@Override
	public Map<String, Object> checkPayPwd(Long accountId) {
		//获取账户
		ActPerson actPerson;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			actPerson = payUserService.findActPersonById(accountId);
			if(actPerson != null && actPerson.getPayPwd().trim().equals("")){
				resultMap.put("resultCode", Hint.USER_11009_NOT_SET_PAY_PASSWORD.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11009_NOT_SET_PAY_PASSWORD.getMessage());
			}else{
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			}
			return resultMap;
		} catch (Exception e) {
			logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> realNameImportGW(Map<String, Object> receiveMap) {
		return payUserService.realNameImport(receiveMap);
	}
	
	@Override
	public Map<String, Object> realNameGW(Map<String, Object> receiveMap) {
		return payUserService.realName(receiveMap);
	}

	@Override
	public Map<String, Object> resetPayPasswordForRealNameGW(Map<String, Object> receiveMap) {
		return payUserService.resetPayPasswordForRealName(receiveMap);
	}

	@Override
	public Map<String, Object> changeUserCodeGW(Map<String, Object> receiveMap) {
		return payUserService.changeUserCode(receiveMap);
	}

	@Override
	public Map<String, Object> verifyCodeListGW(Map<String, Object> receiveMap) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//错误次数需要验证码
			String errorCount = Propertie.APPLICATION.value("login.error.count")==null?"3":Propertie.APPLICATION.value("login.error.count");
			int errorCount_i = Integer.parseInt(errorCount.trim());
			//每次返回错误码个数
			String verifyCodeCount = Propertie.APPLICATION.value("login.verifyCode.count")==null?"10":Propertie.APPLICATION.value("login.verifyCode.count");
			int verifyCodeCount_i = Integer.parseInt(verifyCodeCount.trim());
			
			String hardwareId = receiveMap.get("hardwareId").toString();
			String phone = receiveMap.get("phone")==null?"":receiveMap.get("phone").toString();
			
			//redis 获取硬件id错误次数
			String hardwareIdCount = redisService.get("verifyCodeList_count_"+hardwareId);
			hardwareIdCount = hardwareIdCount ==null?"1":hardwareIdCount;
			int hardwareIdCount_i = Integer.parseInt(hardwareIdCount);
			
			int phoneCount_i = 1;
			if(!phone.equals("")){
				//redis 获取手机错误次数
				String phoneCount = redisService.get("verifyCodeList_count_"+phone);
				phoneCount = phoneCount ==null?"1":phoneCount;
				phoneCount_i = Integer.parseInt(phoneCount);
				
			}
			Map<String,Object> redisVerifyCodeMap = new HashMap<String,Object>();
			List<String> verifyCodeList = new ArrayList<String>();
			redisVerifyCodeMap.put("verifyCodeList", verifyCodeList);
			if(hardwareIdCount_i>errorCount_i || phoneCount_i>errorCount_i){
				
				String verifyCodeSize = Propertie.APPLICATION.value("login.verifyCode.size")==null?"4":Propertie.APPLICATION.value("login.verifyCode.size");
				int verifyCodeSize_i = Integer.parseInt(verifyCodeSize);
				String verifyCodeSeconds = Propertie.APPLICATION.value("login.verifyCode.seconds")==null?"300":Propertie.APPLICATION.value("login.verifyCode.seconds");
				int verifyCodeSeconds_i = Integer.parseInt(verifyCodeSeconds);
				
				String verifyCodeListKey = "verifyCodeList_"+hardwareId;
				for(int i = 0;i < verifyCodeCount_i;i ++){
					verifyCodeList.add(Md5SaltUtils.getRandomString(verifyCodeSize_i));
				}
				redisVerifyCodeMap.put("verifyCodeList", verifyCodeList);
				String verifyCodeList_str = JSONArray.toJSONString(redisVerifyCodeMap);
				redisService.set(verifyCodeListKey, verifyCodeList_str);
				redisService.expire(verifyCodeListKey, verifyCodeSeconds_i);
			}
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
            resultMap.putAll(redisVerifyCodeMap);
			return resultMap;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("系统错误：" + ex.getMessage(), ex);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		return resultMap;
	}

//	public Map<String, Object> faceRecognition(Map<String, Object> inputMap) throws Exception {
//		if(logger.isInfoEnabled()){
//			logger.info("PayUserServiceGWImpl --> faceRecognition 输入参数：" + inputMap);
//		}
//		Map<String, Object> resultMap = new HashMap<>();
//		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
//		String imageBase64 = String.valueOf(inputMap.get("imageBase64")).trim();//图片Base64
//		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();
//		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();
//
//		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
//		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
//			return checkPayUserMap;
//		}
//
//		String redisKey = "BHFaceRecognition_".concat(accountId);
//		String redisValue = redisService.get(redisKey);
//		if(logger.isInfoEnabled()){
//			logger.info("redisKey=" + redisKey + ",redisValue=" + redisValue);
//		}
//		int faceCount;
//		int dayValidateCount = Integer.parseInt(CacheUtils.getDimSysConfConfValue("FACE_DAY_VALIDATE_COUNT"));
//		if(ValidateUtils.isEmpty(redisValue)){
//			faceCount = 0;
//		}else {
//			faceCount = Integer.parseInt(redisValue);
//		}
//		if(logger.isInfoEnabled()){
//			logger.info("[博宏]当前已验证次数faceCount=" + faceCount + ",日限制错误次数dayValidateCount=" + dayValidateCount);
//		}
//		if(faceCount >= dayValidateCount){
//			resultMap.put("resultCode", Hint.USER_11013_FACE_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.USER_11013_FACE_ERROR.getMessage().concat(BaseDictConstant.FACE_RECOGNITION_MAX_COUNT_TIPS).replace("{X}", String.valueOf(dayValidateCount)));
//			return resultMap;
//		}
//		ActPerson actPerson = (ActPerson) checkPayUserMap.get("actPerson");
//		int type;//类型 4-ios,5-android
//		if(DimDictEnum.TD_OPER_CHAN_TYPE_IOS.getCodeString().equals(terminalType)){
//			type = 4;
//		}else{
//			type = 5;
//		}
//		Map<String, Object> faceInputMap = new HashMap<>();
//		faceInputMap.put("aid", Propertie.APPLICATION.value("bhFaceRecognitionAid"));//访问id 默认：yb1101
//		faceInputMap.put("id", actPerson.getpCertNo());//身份证号
//		faceInputMap.put("name", actPerson.getpName());//姓名
//		faceInputMap.put("type", type);//类型 4-ios 5-android
//		List<String> imgList = new ArrayList<>();
//		imgList.add(imageBase64);
//		faceInputMap.put("imgList", imgList);//照片 base64
//		faceInputMap.put("appid", hardwareId);//使用者(手机端硬件ID)
//		if(logger.isInfoEnabled()){
//			logger.info("请求博宏人脸识别平台，请求报文：" + faceInputMap);
//		}
//		String faceResult = this.sendOkHttpPost(Propertie.APPLICATION.value("bhFaceRecognitionUrl"), JsonStringUtils.objectToJsonString(faceInputMap));
//		if(logger.isInfoEnabled()){
//			logger.info("<<响应报文：" + faceResult);
//		}
//		if(ValidateUtils.isEmpty(faceResult) || "ERROR".equals(faceResult)){
//			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
//			return resultMap;
//		}
//		Map faceResultMap = JsonStringUtils.jsonStringToObject(faceResult, Map.class);
//		if(null == faceResultMap || faceResultMap.size() == 0){
//			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
//			return resultMap;
//		}
//		String exist = String.valueOf(faceResultMap.get("exist")).trim();//0-是该人员不存在 1-该人员存在 2-没有访问权限
//		String result = String.valueOf(faceResultMap.get("result")).trim();//0-认证失败 1-认证成功
//		boolean isValidate = false;
//		if(!("0".equals(exist) || "1".equals(exist) || "2".equals(exist))){
//			if(logger.isInfoEnabled()){
//				logger.info("人员状态异常，认证失败");
//			}
//		}else {
//			if("0".equals(exist) || "2".equals(exist)){
//				if(logger.isInfoEnabled()){
//					logger.info("该人员不存在或没有访问权限，认证失败");
//				}
//			}else {
//				if("1".equals(result)){
//					if(logger.isInfoEnabled()){
//						logger.info("认证成功");
//					}
//					isValidate = true;
//				}else {
//					if(logger.isInfoEnabled()){
//						logger.info("认证失败");
//					}
//				}
//			}
//		}
//		if(!isValidate){
//			faceCount ++;
//			this.setAlreadyVerifyCountInRedis(faceCount, redisKey);
//			resultMap.put("resultCode", Hint.USER_11014_FACE_ERROR.getCodeString());
//			resultMap.put("resultDesc", Hint.USER_11014_FACE_ERROR.getMessage());
//			return resultMap;
//		}
//		redisService.delete(redisKey);
//		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
//		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
//		return resultMap;
//	}

	/**
	 * 国民人脸识别接口
	 */
	public Map<String, Object> faceRecognition(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> faceRecognition 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String imageBase64 = String.valueOf(inputMap.get("imageBase64")).trim();//图片Base64
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();
		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();

		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}

		String redisKey = "GMFaceRecognition_".concat(accountId);
		String redisValue = redisService.get(redisKey);
		if(logger.isInfoEnabled()){
			logger.info("redisKey=" + redisKey + ",redisValue=" + redisValue);
		}
		int faceCount;
		int dayValidateCount = Integer.parseInt(CacheUtils.getDimSysConfConfValue("FACE_DAY_VALIDATE_COUNT"));
		if(ValidateUtils.isEmpty(redisValue)){
			faceCount = 0;
		}else {
			faceCount = Integer.parseInt(redisValue);
		}
		if(logger.isInfoEnabled()){
			logger.info("[国民]当前已验证次数faceCount=" + faceCount + ",日限制错误次数dayValidateCount=" + dayValidateCount);
		}
		if(faceCount >= dayValidateCount){
			resultMap.put("resultCode", Hint.USER_11013_FACE_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11013_FACE_ERROR.getMessage().concat(BaseDictConstant.FACE_RECOGNITION_MAX_COUNT_TIPS).replace("{X}", String.valueOf(dayValidateCount)));
			return resultMap;
		}
		ActPerson actPerson = (ActPerson) checkPayUserMap.get("actPerson");
		int type;//类型 4-ios,5-android
		if(DimDictEnum.TD_OPER_CHAN_TYPE_IOS.getCodeString().equals(terminalType)){
			type = 4;
		}else{
			type = 5;
		}
		Map<String, Object> faceInputMap = new HashMap<>();
		faceInputMap.put("idNo", actPerson.getpCertNo());//身份证号
		faceInputMap.put("realName", actPerson.getpName());//姓名
		faceInputMap.put("photo", imageBase64);//照片 base64
		if(logger.isInfoEnabled()){
			logger.info("请求国民人脸识别平台，请求报文：" + faceInputMap);
		}
		String faceResult = "";
		String interfaceUrl = Propertie.APPLICATION.value("gmFaceRecognitionUrl");
	    if(interfaceUrl.startsWith("https")){
	    	Map<String,String> propertys = new HashMap<String,String>();
			propertys.put("mapdata", JsonStringUtils.objectToJsonString(faceInputMap));
			//实际参数
			String allParam = urlEncode(propertys);
	    	faceResult =  HttpsNoCert.doPost(interfaceUrl, allParam);
        }else {
        	faceResult = this.sendOkHttpPost(interfaceUrl, JsonStringUtils.objectToJsonString(faceInputMap));
        }
		if(logger.isInfoEnabled()){
			logger.info("<<响应报文：" + faceResult);
		}
		if(ValidateUtils.isEmpty(faceResult) || "ERROR".equals(faceResult)){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		Map faceResultMap = JsonStringUtils.jsonStringToObject(faceResult, Map.class);
		if(null == faceResultMap || faceResultMap.size() == 0){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		//0 成功
		//2002	实名认证失败
		//2004	实名认证国民系统连接失败
		//2005	实名认证卡中心系统连接失败
		//2006	活体认证相似度低于阈值
		//2007     实名认证卡中心数据不存在
		String code = String.valueOf(faceResultMap.get("code")).trim();
		String msg = String.valueOf(faceResultMap.get("message")).trim();
		if(logger.isInfoEnabled()){
			logger.info("国民认证结果:"+ actPerson.getpCertNo() + actPerson.getpName() +";"+ msg);
		}
		//返回错误为2004 2005 则返回数据连接失败，请稍候再试 不记录识别次数
		if("2004".equals(code) || "2005".equals(code)){
			resultMap.put("resultCode", Hint.GM_11030_HTRZ_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.GM_11030_HTRZ_ERROR.getMessage());
			return resultMap;
		}
		if (false == "0".equals(code)){
			faceCount ++;
			this.setAlreadyVerifyCountInRedis(faceCount, redisKey);
			if("2007".equals(code)){
				resultMap.put("resultCode", Hint.GM_11032_HTRZ_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.GM_11032_HTRZ_ERROR.getMessage());
			}else if("2006".equals(code)){
				resultMap.put("resultCode", Hint.GM_11031_HTRZ_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.GM_11031_HTRZ_ERROR.getMessage());
			}else{
				resultMap.put("resultCode", Hint.USER_11014_FACE_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11014_FACE_ERROR.getMessage());
			}
			return resultMap;
		}
		redisService.delete(redisKey);
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	/**
	 * 请求国民进行人脸识别
	 * @param url 请求地址
	 * @param json 数据
	 * @return
	 * @throws Exception
     */
	private String sendOkHttpPost(String url, String json) throws Exception {
		FormBody mBody = new FormBody.Builder().add("mapdata", json).build();
		Request request = new Request.Builder().url(url).post(mBody).build();
		OkHttpClient client = new OkHttpClient();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		}else {
			return "ERROR";
		}
	}
	
	private String urlEncode(Map<String,String> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String,String> i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public Map<String, Object> realPersonVerify(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("PayUserServiceGWImpl --> realPersonVerify 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String userName = String.valueOf(inputMap.get("userName")).trim();//用户姓名
		String idNumber = String.valueOf(inputMap.get("idNumber")).trim();//身份证号
		String photoBase64 = String.valueOf(inputMap.get("photoBase64")).trim();//用户照片,Base64字符串
		String appId = String.valueOf(inputMap.get("appId")).trim();
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, "", false);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}
		ActPerson actPerson = (ActPerson) checkPayUserMap.get("actPerson");
		if(null != actPerson.getRealNameFlag() && actPerson.getRealNameFlag() == 1){
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", "用户已实名");
			return resultMap;
		}
		int dayValidateCount = Integer.parseInt(CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_COUNT));
		int dayScoreZeroCount = Integer.parseInt(CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_ZERO_COUNT));
		String redisKey = HyConstant.HY_REAL_PERSON_VERIFY_COUNT.concat("_").concat(accountId);
		String redisValue = String.valueOf(redisService.get(redisKey)).trim();
		if(logger.isInfoEnabled()){
			logger.info("redisKey=" + redisKey + ",redisValue=" + redisValue);
		}
		int alreadyCount;
		int scoreZeroCount;
		if(ValidateUtils.isEmpty(redisValue)){
			alreadyCount = 0;
			scoreZeroCount = 0;
		}else {
			String[] countArray = redisValue.split("_");
			alreadyCount = Integer.parseInt(countArray[0]);
			scoreZeroCount = Integer.parseInt(countArray[1]);
		}
		if(logger.isInfoEnabled()){
			logger.info("[慧阅]当前已验证次数alreadyCount=" + alreadyCount + ",scoreZeroCount=" + scoreZeroCount +
					",日限制错误次数dayValidateCount=" + dayValidateCount + ",dayScoreZeroCount=" + dayScoreZeroCount);
		}
		if(scoreZeroCount >= dayScoreZeroCount){
			resultMap.put("resultCode", Hint.HY_11015_HTRZ_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.HY_11018_HTRZ_ERROR.getMessage());
			return resultMap;
		}
		if(alreadyCount >= dayValidateCount){
			resultMap.put("resultCode", Hint.HY_11015_HTRZ_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.HY_11015_HTRZ_ERROR.getMessage());
			return resultMap;
		}
		HyRequest hyRequest = new HyRequest();
		hyRequest.setUserName(userName);
		hyRequest.setIdNumber(idNumber);
		hyRequest.setUserPhotoBase64(photoBase64);
		HyResponse hyResponse = huiYueServiceImpl.realPersonVerifySyncScore(hyRequest);
		if(null == hyResponse){
			resultMap.put("resultCode", Hint.HY_11016_HTRZ_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.HY_11016_HTRZ_ERROR.getMessage());
			return resultMap;
		}
		String result = String.valueOf(hyResponse.getResult()).trim();
		if(!ValidateUtils.isFloatNumber(result)){
			if(HyConstant.HY_REAL_PERSON_VERIFY_RNINCONSISTENT.equalsIgnoreCase(result)){//用户认证信息不一致(扣费)
				alreadyCount ++;
				redisValue = String.valueOf(alreadyCount).concat("_").concat(String.valueOf(scoreZeroCount));
				this.setAlreadyVerifyCountInRedis(redisValue, redisKey);
				payUserServiceImpl.saveActPCertRec(appId, actPerson.getActId(), userName, idNumber,
						BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_FALSE, result.concat("|").concat(String.valueOf(hyResponse.getErrmsg()).trim()), false);
				if(alreadyCount >= dayValidateCount){
					resultMap.put("resultCode", Hint.HY_11015_HTRZ_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.HY_11015_HTRZ_ERROR.getMessage());
					return resultMap;
				}
				resultMap.put("resultCode", Hint.HY_11017_HTRZ_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.HY_11017_HTRZ_ERROR.getMessage());
				return resultMap;
			}
			//库无 or 身份照片不存在(不扣费)
			if(HyConstant.HY_REAL_PERSON_VERIFY_NOLIB.equalsIgnoreCase(result) ||
			   HyConstant.HY_REAL_PERSON_VERIFY_IDPHOTONEXIST.equalsIgnoreCase(result)){
				if(HyConstant.HY_REAL_PERSON_VERIFY_NOLIB.equalsIgnoreCase(result)){
					resultMap.put("resultCode", Hint.HY_11017_HTRZ_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.HY_11017_HTRZ_ERROR.getMessage());
					return resultMap;
				}
				resultMap.put("resultCode", Hint.HY_11016_HTRZ_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.HY_11016_HTRZ_ERROR.getMessage());
				return resultMap;
			}
			//其他失败 FAIL
			if(HyConstant.RESPONSE_RETURN_CODE_FAIL.equalsIgnoreCase(result)){
				if(HyErrorCodeEnum.ERR1001.getErrorCode().equalsIgnoreCase(String.valueOf(hyResponse.getErrmsg()).trim())){
					resultMap.put("resultCode", Hint.HY_11017_HTRZ_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.HY_11017_HTRZ_ERROR.getMessage());
					return resultMap;
				}
			}
			resultMap.put("resultCode", Hint.HY_11016_HTRZ_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.HY_11016_HTRZ_ERROR.getMessage());
			return resultMap;
		}
		String hyRealPersonVerifyScore = CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_SCORE);
		if(logger.isInfoEnabled()){
			logger.info("设置照片通过标准分值：" + hyRealPersonVerifyScore + ",本次验证所得分值：" + result);
		}
		if(BigDecimal.valueOf(Double.parseDouble(result)).compareTo(BigDecimal.valueOf(Double.parseDouble(hyRealPersonVerifyScore))) < 0){
			alreadyCount ++;
			if(BigDecimal.valueOf(Double.parseDouble(result)).compareTo(BigDecimal.ZERO) <= 0){
				scoreZeroCount ++;
			}
			redisValue = String.valueOf(alreadyCount).concat("_").concat(String.valueOf(scoreZeroCount));
			this.setAlreadyVerifyCountInRedis(redisValue, redisKey);
			payUserServiceImpl.saveActPCertRec(appId, actPerson.getActId(), userName, idNumber,
					BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_FALSE, "PHOTO_MATCH_FAIL|".concat(hyRealPersonVerifyScore).concat("_").concat(result), false);
			if(scoreZeroCount >= dayScoreZeroCount){
				resultMap.put("resultCode", Hint.HY_11015_HTRZ_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.HY_11018_HTRZ_ERROR.getMessage());
				return resultMap;
			}
			if(alreadyCount >= dayValidateCount){
				resultMap.put("resultCode", Hint.HY_11015_HTRZ_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.HY_11015_HTRZ_ERROR.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", Hint.HY_11016_HTRZ_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.HY_11016_HTRZ_ERROR.getMessage());
			return resultMap;
		}
		redisService.delete(redisKey);
		payUserServiceImpl.saveActPCertRec(appId, actPerson.getActId(), userName, idNumber,
				BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE, HyConstant.RESPONSE_RETURN_CODE_SUCC.concat("|").concat(hyRealPersonVerifyScore).concat("_").concat(result), true);
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	private void setAlreadyVerifyCountInRedis(Object alreadyCount, String redisKey) throws Exception {
		String beginDate = DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.strToDate(DateUtils.getNowDate("yyyy-MM-dd").concat(" 23:59:59"), "yyyy-MM-dd HH:mm:ss");
		int second = DateUtils.getSecondByDate(DateUtils.strToDate(beginDate, "yyyy-MM-dd HH:mm:ss"), endDate);
		if(logger.isInfoEnabled()){
			logger.info("redisKey=" + redisKey + ",alreadyCount=" + alreadyCount + ",beginDate=" + beginDate + ",endDate=" +
					DateUtils.dateToString(endDate, "yyyy-MM-dd HH:mm:ss") + ",second=" + second);
		}
		redisService.set(redisKey, String.valueOf(alreadyCount));
		redisService.expire(redisKey, second);
	}
}
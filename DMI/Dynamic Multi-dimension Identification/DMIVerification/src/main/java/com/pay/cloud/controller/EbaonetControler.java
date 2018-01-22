package com.pay.cloud.controller;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.alibaba.fastjson.JSON;
import com.pay.cloud.common.cache.account.entity.ActSpInfo;
import com.pay.cloud.common.cache.service.AccountCacheService;
import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.pay.cloud.util.*;
import com.pay.cloud.util.hint.Hint;
import com.pay.secrity.crypto.AesCipher;
import com.pay.secrity.crypto.RSA;

/**
 * 接口统一入口
 * @author qingsong.li
 */
@Controller
@RequestMapping(value = "/ebaonet")
public class EbaonetControler {

    private static final Logger logger = Logger.getLogger(EbaonetControler.class);

    @Resource(name="accountCacheServiceImpl")
    private AccountCacheService accountCacheServiceImpl;
	@Resource
	private RedisService redisService;
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGWImpl payUserServiceGWImpl;
    /**
     * 网关入口
     * @param map 输入map
     * @param request request对象
     * @return 处理结果
     */
    @RequestMapping(value = "gw", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String gw(@RequestBody Map<String, Object> map, HttpServletRequest request,HttpServletResponse response){

    	
    	/**
    	 * 测试cookie
    	 
    	DefaultEbaoCookie defaultEbaoCookie = new DefaultEbaoCookie();
    	List<String> val = defaultEbaoCookie.readValue(request, "test");
    	if(val!=null && val.size()>0){
    		logger.info("cookie is :"+val.get(0));
    	}else{
    		logger.info("write cookie");
    		Cookie cookies[]=request.getCookies();
        	
        	Cookie cookie = new Cookie("test", "test");
        	cookie.setMaxAge(10000);

        	defaultEbaoCookie.writeCookie(request, response, cookie);
    	}*/
    	HttpSession session = request.getSession();
    	String sessionTime = CacheUtils.getDimSysConfConfValue("P_LOGIN_DAY_MAX")==null?"1":CacheUtils.getDimSysConfConfValue("P_LOGIN_DAY_MAX");
    	int sessionTime_i = Integer.parseInt(sessionTime);
    	session.setMaxInactiveInterval(60*60*24*sessionTime_i);//60*60*10
        Map<String, String> returnMap = new HashMap<String, String>();
        logger.info("接收加密参数为:"+map);
        if(null == map || map.size() == 0){
            logger.info("请求接口异常，输入参数：" + map);
            returnMap.put("resultCode", Hint.SYS_10010_REQUEST_PARAM_NULL_ERROR.getCodeString());
            returnMap.put("resultDesc", Hint.SYS_10010_REQUEST_PARAM_NULL_ERROR.getMessage());
            return response(returnMap);
        }

		String appid = String.valueOf(map.get("appid")).trim();
        if(ValidateUtils.isEmpty(appid)){
        	returnMap.put("resultCode", Hint.SYS_10010_REQUEST_PARAM_NULL_ERROR.getCodeString());
            returnMap.put("resultDesc", Hint.SYS_10010_REQUEST_PARAM_NULL_ERROR.getMessage());
            return response(returnMap);
        }
        
        /**
         * 通过appid查询密钥对
         */
        ActSpInfo actSpInfo = accountCacheServiceImpl.findActSpInfoByAppId(appid);
		if(null == actSpInfo){
			returnMap.put("resultCode", Hint.SYS_10012_APPID_ERROR.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10012_APPID_ERROR.getMessage());
			return response(returnMap);
		}
		String ybPrivateKey = String.valueOf(actSpInfo.getServerKeyPri()).trim();
		String shPublicKey = String.valueOf(actSpInfo.getSpKeyPub()).trim();
		if(actSpInfo.getValidFlag() != BaseDictConstant.VALID_FLAG_YES ||
		   ValidateUtils.isEmpty(ybPrivateKey) || ValidateUtils.isEmpty(shPublicKey)){
			returnMap.put("resultCode", Hint.SYS_10013_VALID_FLAG_NO.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10013_VALID_FLAG_NO.getMessage());
			return response(returnMap);
		}
        
        Map<String,Object> decryptMap = this.decrypt(map,ybPrivateKey,shPublicKey);
     
        logger.debug("验签结果：" + decryptMap);
        if(decryptMap != null && decryptMap.get("resultCode") != null && "0".equals(String.valueOf(decryptMap.get("resultCode")))){
        	logger.info("验签成功");
        }else{
        	logger.info("验签失败");
//        	returnMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
//            returnMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
            return response(decryptMap);
        }
       
        /**
         * 全局参数验证
         */
        Map<String,Object> globalValidate = globalValidate(decryptMap,appid);
        if(!globalValidate.get("resultCode").toString().equals(Hint.SYS_SUCCESS.getCodeString())){
        	return response(globalValidate);
        }
        
        String interfaceCode = String.valueOf(decryptMap.get("interfaceCode")).trim();
        logger.info("开始访问" + interfaceCode + "接口");
        try {
            Map<String, Object> beanNameMap = ReadInterfaceXmlUtil.beansMap(interfaceCode);
            
            /**
             * 判断接口是否需要认证
             */
            String auth = beanNameMap.get("auth")==null?"1":beanNameMap.get("auth").toString();
            if(auth.equals("1")){
            	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
            	
            	if(sessionMap!=null && sessionMap.get("auth")!=null && sessionMap.get("auth").equals("1")){
            		//登录验证判断
            		Map<String, Object> mm=payUserServiceGWImpl.isLogin(sessionMap.get("userId").toString(), decryptMap.get("hardwareId").toString());
            		if(!mm.get("resultCode").toString().equals("0")){
            			 return response(mm);
            		}
            		logger.info("用户已登陆！");
            	}else{
            		logger.info("用户未登陆或时间过长！");
            		returnMap.put("resultCode", Hint.USER_25001_LOGINSTATUS_FAILED.getCodeString());
                    returnMap.put("resultDesc", Hint.USER_25001_LOGINSTATUS_FAILED.getMessage());
                    return response(returnMap);
            	}
            	
            }
            
            //获取session中用户手机号添加到前台传过来的参数Map中。
            if(session.getAttribute(SmsConstant.SESSION_USER_KEY)!=null){
            	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
            	if(sessionMap.size()>0 && sessionMap.containsKey("phone")){
            		decryptMap.put("userPhone", sessionMap.get("phone"));
            	}
            }
            
            logger.info("beanNameMap=" + beanNameMap);
            if (!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(beanNameMap.get("resultCode")))) {//读取interfacebean.xml配置文件有误
                returnMap.put("resultCode", Hint.SYS_10006_ACCESS_INTERFACE_LIMIT_ERROR.getCodeString());
                returnMap.put("resultDesc", Hint.SYS_10006_ACCESS_INTERFACE_LIMIT_ERROR.getMessage());
                return response(returnMap);
            }
            String beanName = beanNameMap.get("beanName").toString();
            logger.info("spring注入beanName为：" + beanName);
            WebApplicationContext webApp = RequestContextUtils.getWebApplicationContext(request, request.getSession().getServletContext());
            DoServiceInterface doServiceInterface = (DoServiceInterface) webApp.getBean(beanName);
            if (doServiceInterface == null) {
                returnMap.put("resultCode", Hint.SYS_10007_ACCESS_INTERFACE_NOTEXIST_ERROR.getCodeString());
                returnMap.put("resultDesc", Hint.SYS_10007_ACCESS_INTERFACE_NOTEXIST_ERROR.getMessage());
                return response(returnMap);
            }
            decryptMap.put("remoteAddr", GetRemoteIpAddr.getIpAddr(request));//获取客户端IP
			String preventConcurrentKey = ConvertUtils.getPreventConcurrentKey(interfaceCode, decryptMap);
			logger.info("接口防并发处理，interfaceCode=" + interfaceCode + ",redisKey=" + preventConcurrentKey);
			boolean isControl = false;
			if(ValidateUtils.isNotEmpty(preventConcurrentKey)){
				String setValue = String.valueOf(System.currentTimeMillis());
				Long returnValue = redisService.setNx(preventConcurrentKey, setValue, 60);
				logger.info("setKey=" + preventConcurrentKey + ",setValue=" + setValue + ",returnValue=" + returnValue);
				if(null == returnValue || returnValue == 0){
					returnMap.put("resultCode", Hint.SYS_10015_INTERFACE_CONCURRENT_PREVENT.getCodeString());
					returnMap.put("resultDesc", Hint.SYS_10015_INTERFACE_CONCURRENT_PREVENT.getMessage());
					return response(returnMap);
				}
				isControl = true;
			}
			Map<String,Object> respMap = doServiceInterface.doService(decryptMap);
			if(isControl){
				redisService.delete(preventConcurrentKey);
			}
			logger.info("接口响应结果：" + respMap);
            if(respMap!=null && respMap.get("resultCode")!=null && respMap.get("resultCode").toString().equals("0")){
            	Map<String,Object> encryptMap = encrypt(respMap,ybPrivateKey,shPublicKey);
            	
            	//登陆，登出操作,注册成功自动登录
            	if(interfaceCode.equals("userlogin") || interfaceCode.equals("registeruser") || interfaceCode.equals("loginForToken")){
            		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
            		if(sessionMap==null){
            			sessionMap = new HashMap<String,Object>();
            		}
            		sessionMap.put("auth", "1");
            		//获取登录成功用户信息存储session中
            		sessionMap.put("phone", respMap.get("userCode"));
            		sessionMap.put("userId", respMap.get("userId"));
            		sessionMap.put("userCode", respMap.get("userCode"));
            		sessionMap.put("accountId", respMap.get("accountId"));
            		sessionMap.put("allowPament", respMap.get("allowPament"));
            		
            		session.setAttribute(SmsConstant.SESSION_USER_KEY, sessionMap);
            		logger.info("登陆成功");
            	}else if(interfaceCode.equals("loginout")){
            		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
            		if(sessionMap!=null && sessionMap.get("auth")!=null){
            			sessionMap.remove("auth");
            			session.setAttribute(SmsConstant.SESSION_USER_KEY, sessionMap);
            		}
            		logger.info("退出成功");
            	}
            	
            	encryptMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
            	encryptMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
            	
            	/**
            	 * 如果为H5调用预下单接口
            	 */
				String terminalType = String.valueOf(decryptMap.get("terminalType")).trim();
            	if(("createPayOrder".equals(interfaceCode) && ValidateUtils.isNotEmpty(terminalType) &&
					terminalType.equals(DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCodeString())) || 
            			("createPayOrderParam".equals(interfaceCode) && ValidateUtils.isNotEmpty(terminalType) &&
            					terminalType.equals(DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCodeString()))){
            		encryptMap.put("appid",appid);
            		String key = EncryptUtils.aesEncrypt(JSON.toJSONString(encryptMap), CipherAesEnum.H5PAYORDER);
					if(ValidateUtils.isEmpty(key)){
						key = "";
					}
            		String payOrderId = String.valueOf(respMap.get("payOrderId")).trim();
					if(ValidateUtils.isEmpty(payOrderId)){
						payOrderId = "";
					}
            		Map<String, Object> h5PayOrderMap = new HashMap<String, Object>();
					h5PayOrderMap.put("resultCode", respMap.get("resultCode"));
					h5PayOrderMap.put("resultDesc", respMap.get("resultDesc"));
					String md5Key = Md5SaltUtils.encodeMd5(key);
            		redisService.set(md5Key, key);
            		redisService.expire(md5Key, 60*60*24);
            		if(("createPayOrder".equals(interfaceCode) && ValidateUtils.isNotEmpty(terminalType) &&
					terminalType.equals(DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCodeString()))){
            			h5PayOrderMap.put("key", md5Key);
            		}else{
            			h5PayOrderMap.put("key", key);
            		}
            		
            		h5PayOrderMap.put("payOrderId", payOrderId);
            		return response(h5PayOrderMap);
            	}
            	return response(encryptMap);
            	
            }else{
            	if(respMap == null){
            		logger.info("业务返回参数为空");
            		returnMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
                    returnMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
                    return response(returnMap);
            	}else if(respMap.get("resultCode")==null){
            		logger.info("业务返回码不存在");
            		returnMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
                    returnMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
                    return response(returnMap);
            	}
            	return response(respMap);
            }
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            returnMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            returnMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return response(returnMap);
        }
    }

    private String response(Object object){
        return JSON.toJSONString(object);
    }
    
    /**
     * 全局参数验证
     * @param map
     * @return
     */
    public static Map<String,Object> globalValidate(Map<String,Object> map,String appid){
    	Map<String,Object> globalValidateMap = new HashMap<String,Object>();
    	String appId = map.get("appId")==null?"":map.get("appId").toString();
    	if(!appId.equals(appid) && !ValidateUtils.checkLength(appId, 20, 25)){
    		globalValidateMap.put("resultCode",  Hint.SYS_10014_VALID_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10014_VALID_ERROR.getMessage().replaceAll("param", "appId"));
    		return globalValidateMap;
    	}
    	String reqTime = map.get("reqTime")==null?"":map.get("reqTime").toString();
    	if(reqTime.equals("")){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "reqTime"));
    		return globalValidateMap;
    	}
    	String returnType = map.get("returnType")==null?"":map.get("returnType").toString();
    	if(returnType.equals("")){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "returnType"));
    		return globalValidateMap;
    	}
    	if(returnType.equals("2") || returnType.equals("1")){
    		logger.info("returnType参数值为："+returnType);
    	}else{
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "returnType"));
    		return globalValidateMap;
    	}
    	String version = map.get("version")==null?"":map.get("version").toString();
    	if(version.equals("") && !ValidateUtils.checkLength(version, 1, 50)){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "version"));
    		return globalValidateMap;
    	}
    	String systemId = map.get("systemId")==null?"":map.get("systemId").toString();
    	if(systemId.equals("") && !ValidateUtils.checkLength(systemId,1,50)){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "systemId"));
    		return globalValidateMap;
    	}
    	String terminalType = map.get("terminalType")==null?"":map.get("terminalType").toString();
    	if(terminalType.equals("") && !ValidateUtils.checkLength(terminalType,1,50)){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "terminalType"));
    		return globalValidateMap;
    	}
    	String uaInfo = map.get("uaInfo")==null?"":map.get("uaInfo").toString();
    	if(uaInfo.equals("")  && !ValidateUtils.checkLength(uaInfo,1,200)){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "uaInfo"));
    		return globalValidateMap;
    	}
    	String interfaceCode = map.get("interfaceCode")==null?"":map.get("interfaceCode").toString();
    	if(interfaceCode.equals("") && !ValidateUtils.checkLength(interfaceCode,1,50) ){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "interfaceCode"));
    		return globalValidateMap;
    	}
    	String hardwareId = map.get("hardwareId")==null?"":map.get("hardwareId").toString();
    	if(hardwareId.equals("") && !ValidateUtils.checkLength(hardwareId,1,100) ){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "hardwareId"));
    		return globalValidateMap;
    	}
    	String ipInfo = map.get("ipInfo")==null?"":map.get("ipInfo").toString();
    	if(ipInfo.equals("") && !ValidateUtils.checkIP(ipInfo)){
    		globalValidateMap.put("resultCode",  Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
    		globalValidateMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replaceAll("param", "ipInfo"));
    		return globalValidateMap;
    	}
    	globalValidateMap.put("resultCode",  Hint.SYS_SUCCESS.getCodeString());
		globalValidateMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    	return globalValidateMap;
    }
    /**
     * 返回数据加密
     * @param map
     * @return
     */
    public Map<String,Object> encrypt(Map<String,Object> map,String ybPrivateKey,String shPublicKey){
    	Map<String,Object> encryptMap = new HashMap<String,Object>();
    	try{
    		
    		String dataJson = JSON.toJSONString(map);
			/**
			 * 生成AESkey和AESIv秘钥
			 */
			AesCipher aes = new AesCipher();
			String aesKey = aes.generateKeyToBase64(128);
			String aesIv =  aes.generateIvBytesToBase64();//生成aeskey 和 aesiv
			Map<String,Object> keysMap = new HashMap<String,Object>();
			keysMap.put("aesKey", aesKey);
			keysMap.put("aesIv", aesIv);
			String keysJson = JSON.toJSONString(keysMap);
			
			/**
			 * RSA易保私密解密
			 * 输入：动态AES KEY,动态 AES Iv
			 * 输出 ：keys(RSA加密 AES密钥)，ybPrivateKEY(RSA易保PrivateKEY)
			 */
			String keysEncrypt = RSA.encrypt(keysJson, shPublicKey);
			/**
			 * 签名数据
			 * 输入：原文数据，易保RSA privateKey 
			 * 输出：密文
			 */
			String signEncrypt = RSA.sign(dataJson, ybPrivateKey);
			/**
			 * 数据加密
			 * 输入：原文数据，动态AES KEY,动态 AES Iv
			 */
			String dataEncrypt = aes.encrypt(dataJson,aesKey,aesIv);
			
			encryptMap.put("sign", signEncrypt);
			encryptMap.put("keys", keysEncrypt);
			encryptMap.put("data", dataEncrypt);
			
    		return encryptMap;
    	}catch(Exception ex){
    		ex.printStackTrace();
    		encryptMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
    		encryptMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
    		return encryptMap;
    	}
    	
		
    }
    
    
    /**
     * 解密验签
     * @param map
     * @return
     */
    public Map<String,Object> decrypt(Map<String,Object> map,String ybPrivateKey,String shPublicKey){
    	Map<String,Object> signMap = new HashMap<String,Object>();
    	try{
    		String appid = (String) (map.get("appid")==null?"":map.get("appid"));
    		String sign = (String) (map.get("sign")==null?"":map.get("sign"));
    		String keys = (String) (map.get("keys")==null?"":map.get("keys"));
    		String data = (String) (map.get("data")==null?"":map.get("data"));
    		if(ValidateUtils.isNotEmpty(appid) && ValidateUtils.isNotEmpty(sign) && ValidateUtils.isNotEmpty(keys) && ValidateUtils.isNotEmpty(data)){
    			
    			/**
    			 * 通过appid查询易宝privateKey和商户publicKey
    			 */
//    			ybPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI/61cLYHjW0Upl6DWBGXAq9DGVUw7fiMYxtNIH+Uiw5BAzzDGWz7PkPk31wnVNjdh6SLfQuHGNfs0Q2tlqtofxnFCmdkm1JbQ3et8ERVhZvdvTdwxsFUJrOAIHEbgbtTzdy1FYRfJr4EpPQa+X24TWqzogkFKwH1mVNwYpKNt/fAgMBAAECgYAWsbjj32DTojqqylw1YqG7Z5T+m9/FqjeZJNVz7/xP0CTdTaoUlcmycEwVJ50EudayduQbavE6kKkXBXC01k2D1vb/fYjAXs7QECJUPNvmOfTjQDOgu1+EXMuf9BaoQx9khRFrqkfA3m/eiRsznhNQGdp12+PC9gRJ/XYSXkeA4QJBAPV/IqMaJJn9ExWlCfmAbKfJT2sFGtqFFIzzDEuqpo0pOD+yn658wn759aIdnsMscoBbZeaVJCMWa2ls8RStWokCQQCWI8+aD6fozbvdYoEv62UjDCK4Tw1s9RDgv9KPjDPfSiNezKRNmhxry2wMEDSiHMsqlaM/ZBnEWnfXV9MKxC0nAkEA0l/1bbG8hd1SWm0IpC/CbPJdRUeloHcEiljJQ5pkiXk2Q5etsRgmkCthrGAXqgdPw+EO7M8Hna3btAdwfJiliQJASNzWOkEQzrVRT64nXdcSPQD9tI6AyHjiGQBeslVrMxjqs24C6ro4wsN1CyGuOWJZ4q++CtyMeLMi57nybI4qDwJAIM4ylb7emaAWuov+1rDKwK2F5fdegQffCShPJtfTouljnjulcfmlDVKI4WfIZhdKvrLaXOg5p7qHqHqPZagmKQ==";
//    			shPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCe+qLbvOo1q4mU6fiZvuvB5tBhIfTiMnQdseBounWwFgDeuVBhRAuQCRWXd84NGIQse7j9ORnM6bcI77TNnr9+NlPB8+8bh1h377IoiVGwnr3rwfUwk63t6jXBrPcqbgMullsVWsqmVg51RAuJiJyjQ+Lh6P91zNBKLW105JChiQIDAQAB";
    			/**
    			 * RSA易保私密解密
    			 * 输入 ：keys(RSA加密 AES密钥)，ybPrivateKEY(RSA易保PrivateKEY)
    			 * 输出：动态AES KEY,动态 AES Iv
    			 */
    			logger.info("RSA易保私密解密keys");
    			String keysJson = RSA.decrypt(keys, ybPrivateKey);
    			/**
    			 * 转换成map类型
    			 */
    			logger.info("解密keys:"+keysJson);
    			Map<String,String> keysMaps = (Map<String, String>) JSON.parse(keysJson);
    			String aeskey = keysMaps.get("aesKey");
    			String aesIv = keysMaps.get("aesIv");
    			AesCipher aes = new AesCipher();
    			/**
    			 * AES动态安全KEY解密
    			 * 输入：AES加密数据，动态AES KEY,动态 AES Iv
    			 * 输出：商户明文数据
    			 */
    			logger.info(" AES动态安全KEY解密");
    			String dataJson = aes.decrypt(data, aeskey, aesIv);
//    			logger.info("解密data:"+dataJson);
    			Map<String,Object> dataMap = (Map<String, Object>) JSON.parse(dataJson);
				/**
				 * RSA数据验签
				 * 商户明文数据，数据签名，RSA商户publicKey
				 */
    			if(RSA.checkSign(dataJson, sign, shPublicKey)){
    				dataMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
    				dataMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
    				return dataMap;
    			}
    			signMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
    			signMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
    			return signMap;
    		}
    		signMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
    		signMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
    		return signMap;
    	}catch(Exception ex){
    		ex.printStackTrace();
    		signMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
    		signMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
    		return signMap;
    	}
    	
    }
}

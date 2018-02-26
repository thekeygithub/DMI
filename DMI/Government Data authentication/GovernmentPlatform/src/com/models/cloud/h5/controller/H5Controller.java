package com.models.cloud.h5.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.alibaba.fastjson.JSON;
import com.models.cloud.common.cache.account.entity.ActSpInfo;
import com.models.cloud.common.cache.service.AccountCacheService;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.constants.SmsConstant;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.protocolfactory.impl.LoginForTokenInterfaceImpl;
import com.models.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.EncryptUtils;
import com.models.cloud.util.ReadH5PageXmlUtil;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;
import com.models.secrity.crypto.AesCipher;
import com.models.secrity.crypto.RSA;

/**
 * H5入口，页面跳转
 * @author qingsong.li
 *
 */
@Controller
@RequestMapping(value = "/h5")
public class H5Controller {

	 private static final Logger logger = Logger.getLogger(H5Controller.class);
	 
	    @Resource(name="accountCacheServiceImpl")
	    private AccountCacheService accountCacheServiceImpl;
	    @Resource
		private RedisService redisService;
		@Resource(name="payUserServiceGWImpl")
		private PayUserServiceGWImpl payUserServiceGWImpl;
		@Resource(name = "loginForTokenInterfaceImpl")
		private LoginForTokenInterfaceImpl loginForTokenInterfaceImpl;
		
	 /**
	  * 1.预下单完成后，返回加密串（包含订单信息和第三方appid等信息）
	  * 2.get请求url?key=加密串
	  * 判断key的有效性，跳转到支付页面
	  * @return
	  */
	 @RequestMapping(value = "entrance")
	 public String entrancePage(Model model,@RequestParam(value="key",defaultValue="1",required=false)String key,HttpServletRequest request){
		try{
			
			String userId = request.getParameter("userId")==null?"":request.getParameter("userId");
			String userCode = request.getParameter("userCode")==null?"":request.getParameter("userCode");
			String token = request.getParameter("token")==null?"":request.getParameter("token");
			String hardwareId = request.getParameter("hardwareId")==null?"":request.getParameter("hardwareId");
			if(token!=null && !"".equals(token)){
				Map<String,Object> tokenMap = new HashMap<String,Object>();
				tokenMap.put("userId", userId);
				tokenMap.put("userCode", userCode);
				tokenMap.put("token", token);
				tokenMap.put("hardwareId", hardwareId);

				Map<String, Object> token_return = loginForTokenInterfaceImpl.doService(tokenMap);
				
				logger.info("自动登录userId:"+userId);
				logger.info("自动登录userCode:"+userCode);
				logger.info("自动登录token:"+token);
				logger.info("自动登录hardwareId:"+hardwareId);
				if(!token_return.get("resultCode").toString().equals("0")){
					model.addAttribute("message","授权失败！");
					return "error/error";
				}else{
					token_return.put("auth", "1");
					request.getSession().setAttribute(SmsConstant.SESSION_USER_KEY, token_return);
				}
//				try{
//					String token_d = EncryptUtils.aesDecrypt(token, CipherAesEnum.H5LOGINTOKEN);
//					String tokenMessage = redisService.get(token_d);
//					if(tokenMessage!=null && !"".equals(tokenMessage)){
//						logger.info("token信息："+tokenMessage);
//						Map<String,Object> tokenMap = JSON.parseObject(tokenMessage);
//						tokenMap.put("hardwareId", request.getSession().getId());
//						Map<String, Object> token_return  = payUserServiceGWImpl.login(tokenMap);
//						logger.info("自动登录返回值："+token_return);
//						if(token_return!=null && token_return.get("resultCode")!=null && token_return.get("resultCode").toString().equals("0")){
//							token_return.put("auth", "1");
//							request.getSession().setAttribute(SmsConstant.SESSION_USER_KEY, token_return);
//						}
//
//					}
//
//				}catch(Exception ex){
//					ex.printStackTrace();
//				}

			}else{
				model.addAttribute("message","未知授权信息！");
				return "error/error";
			}
			
			
			String returnCallback = request.getParameter("return_callback")==null?"":request.getParameter("return_callback");
			logger.info("returnCallback: "+returnCallback);
			//解密key 判断key的有效性
			 if(key!=null){//如果有效
				 
				 key = URLDecoder.decode(key,"UTF-8");
				 
				 String keyValue = redisService.get(key);
				 
				 keyValue = EncryptUtils.aesDecrypt(keyValue,CipherAesEnum.H5PAYORDER);
				 
				 Map<String,Object> map = (Map<String, Object>) JSON.parse(keyValue);
				String appid = map.get("appid")==null?"":map.get("appid").toString();
				if(appid.equals("")){
					model.addAttribute("message","未知订单渠道！");
					return "error/error";
				}
				
				 /**
		         * 通过appid查询密钥对
		         */
		        ActSpInfo actSpInfo = accountCacheServiceImpl.findActSpInfoByAppId(appid);
				if(null == actSpInfo){
					model.addAttribute("message","未知渠道appid");
					return "error/error";
				}
				String ybPublicKey = String.valueOf(actSpInfo.getServerKeyPub()).trim();
				String shPrivateKey = String.valueOf(actSpInfo.getSpKeyPri()).trim();
				if(actSpInfo.getValidFlag() != BaseDictConstant.VALID_FLAG_YES ||
				   ValidateUtils.isEmpty(ybPublicKey) || ValidateUtils.isEmpty(shPrivateKey)){
					model.addAttribute("message","解密出现错误！");
					return "error/error";
				}
				if(map!=null && map.get("resultCode")!=null && map.get("resultCode").toString().equals("0")){
					String resultDecryptKeys = map.get("keys").toString();
					String resulSign = map.get("sign").toString();
					String resultKeys = RSA.decrypt(resultDecryptKeys, shPrivateKey);
					Map<String,Object> keysMap = (Map<String, Object>) JSON.parse(resultKeys);
					String raesKey = keysMap.get("aesKey").toString();
					String raesIv = keysMap.get("aesIv").toString();
					String resultDecryptData = map.get("data").toString();
					AesCipher aes = new AesCipher();
					String resultData = aes.decrypt(resultDecryptData, raesKey, raesIv);
					System.out.println("返回数据为："+resultData);
					System.out.println("验签值："+RSA.checkSign(resultData, resulSign, ybPublicKey));
					if(RSA.checkSign(resultData, resulSign, ybPublicKey)){
						Map<String,Object> decryptMap = JSON.parseObject(resultData);
						 if(decryptMap != null && decryptMap.get("resultCode") != null && "0".equals(String.valueOf(decryptMap.get("resultCode")))){
					        	logger.info("验签成功");
					        
					            
					        	HttpSession session = request.getSession();
					        	//设置session有效时间
					        	String sessionTime = CacheUtils.getDimSysConfConfValue("P_LOGIN_DAY_MAX")==null?"1":CacheUtils.getDimSysConfConfValue("P_LOGIN_DAY_MAX");
					        	int sessionTime_i = Integer.parseInt(sessionTime);
					        	session.setMaxInactiveInterval(60*60*24*sessionTime_i);//60*60*10
					        	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
					        	if(sessionOrderMap==null){
					        		sessionOrderMap = new HashMap<String,Object>();
					        	}
					        	decryptMap.put("returnCallback", returnCallback);
					        	decryptMap.put("appid", appid);

							 	String businessPayTypes = String.valueOf(decryptMap.get("businessPayTypes")).trim();
							 	List businessPayTypesList = Collections.EMPTY_LIST;
							 	if(ValidateUtils.isNotEmpty(businessPayTypes)){
									businessPayTypesList = JSON.parseObject(businessPayTypes, List.class);
								}
							 	decryptMap.put("businessPayTypes", businessPayTypesList);

							    String medicarePayTypes = String.valueOf(decryptMap.get("medicarePayTypes")).trim();
							 	List medicarePayTypesList = Collections.EMPTY_LIST;
							 	if(ValidateUtils.isNotEmpty(medicarePayTypes)){
									medicarePayTypesList = JSON.parseObject(medicarePayTypes, List.class);
								}
							 	decryptMap.put("medicarePayTypes", medicarePayTypesList);

					        	decryptMap.remove("resultCode");
					        	decryptMap.remove("resultDesc");
					        	sessionOrderMap = decryptMap;
				        		session.setAttribute(SmsConstant.SESSION_USER_ORDER_KEY, decryptMap);
					        	logger.info("orderinfosession : "+decryptMap);
					        	
					        	model.addAttribute("orderInfo",decryptMap);
					        	
				        		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
				        		if(sessionMap!=null && sessionMap.get("auth")!=null && sessionMap.get("auth").equals("1")){
				        			model.addAttribute("userInfo",sessionMap);
									if(sessionMap.size() > 0){
										if(ValidateUtils.isNotEmpty(userCode)){
											String showPayUserCodeBegin = userCode.substring(0, 3);
											String showPayUserCodeEnd = userCode.substring(7);
											sessionMap.put("showPayUserCode", showPayUserCodeBegin.concat("****").concat(showPayUserCodeEnd));
										}
									}
				        			String createOrderTime= sessionOrderMap.get("createOrderTime")==null?"0":sessionOrderMap.get("createOrderTime").toString();
				        			String payTimes = sessionOrderMap.get("payTimes")==null?"0":sessionOrderMap.get("payTimes").toString();
//				        			logger.info("预下单 倒计时paytime: "+payTimes);
				        			if(!createOrderTime.equals("") && !payTimes.equals("")){
				        				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        				Date createOrderTime_d = sdf.parse(createOrderTime);
//				        				logger.info("预下单 倒计时 下单日期: "+createOrderTime);
				        				Long createOrderTime_l = createOrderTime_d.getTime();
				        				Date now = new Date();
//				        				logger.info("预下单 倒计时 当前日期: "+sdf.format(now));
				        				Long now_l = now.getTime();
				        				int payTomes_i = Integer.parseInt(payTimes);
				        				long loseTimes = (payTomes_i - (now_l-createOrderTime_l)/1000);
//				        				logger.info("预下单 倒计时 剩余时间loseTimes: "+loseTimes);
				        				model.addAttribute("payTimes",loseTimes);
				        			}
				        			return "trade/payOrder";
				        		}
					        	return "user/login";
					        }
					}
				}
		     
	        	logger.info("验签失败");
	        	model.addAttribute("message","验签失败");
				return "error/error";
		        
			 }else{//如果无效
				 model.addAttribute("message", "无效参数");
				 return "error/error";
			 }
		}catch(Exception ex){
			ex.printStackTrace();
			 model.addAttribute("message", "系统繁忙！");
			 return "error/error";
		}
		 
	 }

	/**
	 * 业务模块页面
	 * @param model
	 * @param request
	 * @param response
	 * @param pageName
     * @return
     */
	 @RequestMapping(value = "page/{pageName}")
	 public String businessPage(Model model,HttpServletRequest request,HttpServletResponse response,@PathVariable String pageName){
		 
		 try{
			 /**
			  * 判断sesion中是否有必须的参数 appid，payOrderId，accountId，workSpActId
			  */
			 HttpSession session = request.getSession();
			 Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
			 Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
			 
			 if(!sessionOrderCheck(session)){
				 model.addAttribute("message","订单超时，请重新支付！");
				 
	             return "error/error"; 
			 }
			 

			 /**
			  * 需要验证登录信息
			  */

			 Map<String, Object> beanNameMap = ReadH5PageXmlUtil.beansMap(pageName);

			 String auth = beanNameMap.get("auth")==null?"1":beanNameMap.get("auth").toString();
			 if(auth.equals("1")){
				 if(sessionMap!=null && sessionMap.get("auth")!=null && sessionMap.get("auth").equals("1")){
					 //登录验证判断
//					 Map<String, Object> mm=payUserServiceGWImpl.isLogin(sessionMap.get("userId").toString(), session.getId());
//					 if(!mm.get("resultCode").toString().equals("0")){
//						 return "user/login";
//					 }
					 logger.info("用户已登陆！");
				 }else{
					 logger.info("用户未登陆或时间过长！");
					 return "user/login";
				 }
				 if(!sessionCheck(session)){
					 logger.info("登录超时");
					 return "user/login"; 
				 }
			 }

			 
			
            logger.info("beanNameMap=" + beanNameMap);
            if (!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(beanNameMap.get("resultCode")))) {//读取 配置文件有误
            	model.addAttribute("message","系统繁忙");
                return "error/error";
            }
            
            String pagePath = beanNameMap.get("pagePath")==null?"":beanNameMap.get("pagePath").toString();	
            if(pagePath.equals("")){
            	model.addAttribute("message","页面不存在！");
                return "error/error";
            }
            if(auth.equals("1")){
            	if(!sessionCheck(session)){
            		logger.info("未登录");
            		return "user/login";
            	}
            }
            
            String doService = beanNameMap.get("doService")==null?"":beanNameMap.get("doService").toString();
            //1：处理业务，0：不处理业务直接跳转页面
            if(doService.equals("1")){
            	String beanName = beanNameMap.get("beanName").toString();
            	logger.info("spring注入beanName为：" + beanName);
            	WebApplicationContext webApp = RequestContextUtils.getWebApplicationContext(request, request.getSession().getServletContext());
            	DoPageService doPageService = (DoPageService) webApp.getBean(beanName);
            	if (doPageService == null) {
            		model.addAttribute("message","系统繁忙");
            		return "error/error";
            	}
            	Map<String, Object> map = new HashMap<String,Object>();
            	
            	//设置全局参数：
                String ipInfo = request.getRemoteAddr();
                String uaInfo = request.getHeader("User-Agent");
                String reqTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String returnType = "2";
                String version = "0.1.0.0";
                String appId = sessionOrderMap.get("appid").toString();//session中获取
                String systemId = "H5";
                String terminalType = "21";
                String hardwareId = request.getSession().getId();

                map.put("ipInfo", ipInfo);
                map.put("uaInfo", uaInfo);
                map.put("reqTime", reqTime);
                map.put("returnType", returnType);
                map.put("version", version);
                map.put("appId", appId);
                map.put("systemId", systemId);
                map.put("terminalType", terminalType);
                map.put("hardwareId", hardwareId);
            	
            	Map<String, Object> returnMap = doPageService.returnData(map,model, request);
            	
            	String resultCode = returnMap.get("resultCode")==null?"":returnMap.get("resultCode").toString();
            	
            	if(!resultCode.equals("0")){
            		model.addAttribute("message","操作错误");
            		return "error/error";
            	}
            	model.addAttribute("data", returnMap);
            	model.addAttribute("userInfo", sessionMap);
				if(null != sessionMap && sessionMap.size() > 0){
					String userCode = String.valueOf(sessionMap.get("userCode")).trim();
					if(ValidateUtils.isNotEmpty(userCode)){
						String showPayUserCodeBegin = userCode.substring(0, 3);
						String showPayUserCodeEnd = userCode.substring(7);
						sessionMap.put("showPayUserCode", showPayUserCodeBegin.concat("****").concat(showPayUserCodeEnd));
					}
				}
            	model.addAttribute("orderInfo", sessionOrderMap);
            	String createOrderTime= sessionOrderMap.get("createOrderTime")==null?"0":sessionOrderMap.get("createOrderTime").toString();
    			String payTimes = sessionOrderMap.get("payTimes")==null?"0":sessionOrderMap.get("payTimes").toString();
    			logger.info("倒计时paytime: "+payTimes);
    			if(!createOrderTime.equals("") && !payTimes.equals("")){
    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    				Date createOrderTime_d = sdf.parse(createOrderTime);
    				logger.info("倒计时 下单日期: "+createOrderTime);
    				Long createOrderTime_l = createOrderTime_d.getTime();
    				Date now = new Date();
    				logger.info("倒计时 当前日期: "+sdf.format(now));
    				Long now_l = now.getTime();
    				int payTomes_i = Integer.parseInt(payTimes);
    				long loseTimes = (payTomes_i - (now_l-createOrderTime_l)/1000);
    				logger.info("倒计时 剩余时间loseTimes: "+loseTimes);
    				model.addAttribute("payTimes",loseTimes);
    			}
            }else{
            	Enumeration<String> et=request.getParameterNames();
            	String str ="";
            	while (et.hasMoreElements()) {
					str = (String) et.nextElement();
					model.addAttribute(str, request.getParameter(str));
				}
            }
            
            return pagePath;
		 }catch(Exception ex){
			 ex.printStackTrace();
			 model.addAttribute("message","系统繁忙");
			 return "error/error";
		 }
		 
	 }
	 //
	 @RequestMapping(value = "ajax/{pageName}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	 @ResponseBody
	 public String getDataJson(@RequestBody Map<String, Object> map, HttpServletRequest request,@PathVariable String pageName){
		 Map<String, Object> returnMap = new HashMap<String, Object>();
		 try{
			 Map<String, Object> beanNameMap = ReadH5PageXmlUtil.beansMap(pageName);
			 
			 HttpSession session = request.getSession();
			 Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
			 Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
			 
			 

			 String auth = beanNameMap.get("auth")==null?"1":beanNameMap.get("auth").toString();
			 if(auth.equals("1")){
				 if(sessionMap!=null && sessionMap.get("auth")!=null && sessionMap.get("auth").equals("1")){
					 //登录验证判断
					 Map<String, Object> mm=payUserServiceGWImpl.isLogin(sessionMap.get("userId").toString(), session.getId());
					 if(!mm.get("resultCode").toString().equals("0")){
						 return JSON.toJSONString(mm);
					 }
					 logger.info("用户已登陆！");
				 }else{
					 logger.info("用户未登陆或时间过长！");
					 returnMap.put("resultCode", Hint.USER_25001_LOGINSTATUS_FAILED.getCodeString());
					 returnMap.put("resultDesc", Hint.USER_25001_LOGINSTATUS_FAILED.getMessage());
					 return JSON.toJSONString(returnMap);
				 }
			 }

	          if(auth.equals("1")){
	          	if(!sessionCheck(session)){
	          		logger.info("未登录");
	          		 returnMap.put("resultCode", Hint.USER_25001_LOGINSTATUS_FAILED.getCodeString());
					 returnMap.put("resultDesc", Hint.USER_25001_LOGINSTATUS_FAILED.getMessage());
					 return JSON.toJSONString(returnMap);
	          	}
	          }
	          
	          /**
	           * 检查session是否有订单信息
	           */
	          if(!sessionOrderCheck(session)){
	        	  returnMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
	        	  returnMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
	        	  return JSON.toJSONString(returnMap); 
	          }
			 
			 logger.info("beanNameMap=" + beanNameMap);
	         if (!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(beanNameMap.get("resultCode")))) {//读取interfacebean.xml配置文件有误
	        	 returnMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
	             returnMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
	        	 return JSON.toJSONString(returnMap);
	         }
	         String beanName = beanNameMap.get("beanName").toString();
	         logger.info("spring注入beanName为：" + beanName);
	         WebApplicationContext webApp = RequestContextUtils.getWebApplicationContext(request, request.getSession().getServletContext());
	         DoPageService doServiceInterface = (DoPageService) webApp.getBean(beanName);
	         if (doServiceInterface == null) {
	        	 returnMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
	             returnMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
	        	 return JSON.toJSONString(returnMap);
	         }
	         
	         //设置全局参数：
	         String ipInfo = request.getRemoteAddr();
	         String uaInfo = request.getHeader("User-Agent");
	         String reqTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	         String returnType = "2";
	         String version = "1.0.0";
	         String appId = sessionOrderMap.get("appid").toString();//session中获取
	         String systemId = "H5";
	         String terminalType = "21";
	         String hardwareId = request.getSession().getId();
	         
	         map.put("ipInfo", ipInfo);
	         map.put("uaInfo", uaInfo);
	         map.put("reqTime", reqTime);
	         map.put("returnType", returnType);
	         map.put("version", version);
	         map.put("appId", appId);
	         map.put("systemId", systemId);
	         map.put("terminalType", terminalType);
	         map.put("hardwareId", hardwareId);
	         
	         returnMap = doServiceInterface.returnDataByMap(map, request);
	         
	         return JSON.toJSONString(returnMap);
		 }catch(Exception ex){
			 ex.printStackTrace();
			 returnMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
             returnMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
        	 return JSON.toJSONString(returnMap);
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
    			logger.info("解密data:"+dataJson);
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
     * 检查session是否有订单信息
     * @param session
     * @return
     */
    public boolean sessionOrderCheck(HttpSession session){
		 Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
		 if(sessionOrderMap!=null){
			 String appid = sessionOrderMap.get("appid")==null?"": sessionOrderMap.get("appid").toString();
			 if(appid.equals("")){
				return false;
			 }
			String payOrderId = sessionOrderMap.get("payOrderId")==null?"": sessionOrderMap.get("payOrderId").toString();
			if(payOrderId.equals("")){
				return false;
			}
   		
	   		String workSpActId = sessionOrderMap.get("workSpActId")==null?"": sessionOrderMap.get("workSpActId").toString();
	   		if(workSpActId.equals("")){
				return false;
	   		}
		 }else{
            return false; 
		 }
	
    	return true;
    }
    
    /**
     * 检查session是否有订单信息
     * @param session
     * @return
     */
    public boolean sessionCheck(HttpSession session){
		 Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		 if(sessionMap!=null){
			 String accountId = sessionMap.get("accountId")==null?"": sessionMap.get("accountId").toString();
			 if(accountId.equals("")){
				return false;
			 }
			 String userCode = sessionMap.get("userCode")==null?"": sessionMap.get("userCode").toString();
			 if(userCode.equals("")){
				return false;
			 }
			 String userId = sessionMap.get("userId")==null?"": sessionMap.get("userId").toString();
			 if(userId.equals("")){
				return false;
			 }
			String sessionAuth = sessionMap.get("auth")==null?"":sessionMap.get("auth").toString();
			if(!sessionAuth.equals("1")){
				return false;
			}
		 }else{
            return false; 
		 }
	
    	return true;
    }
}

package com.models.cloud.pay.escrow.yeepay.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.models.cloud.pay.common.http.model.HttpResponseBean;
import com.models.cloud.pay.common.http.utils.HttpUtils;

/**
 * 一键支付接口范例--API版
 * @author	：   
 * @since	：
 */
public class InstantPayService {
	
	//日志
	private static Logger log = Logger.getLogger(InstantPayService.class);
	
	/**
	 * 取得商户编号
	 */
	public static String getMerchantAccount() {
		return Configuration.getInstance().getValue("merchantAccount");
	}
	
	/**
	 * 取得商户私钥
	 */
	public static String getMerchantPrivateKey() {
		return Configuration.getInstance().getValue("merchantPrivateKey");
	}

	/**
	 * 取得商户AESKey
	 */
	public static String getMerchantAESKey() {
		return (RandomUtil.getRandom(16));
	}

	/**
	 * 取得易宝公玥
	 */
	public static String getYeepayPublicKey() {
		return Configuration.getInstance().getValue("yeepayPublicKey");
	}

	/**
	 * 格式化字符串
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}

	/**
	 * String2Integer
	 */
	public static int String2Int(String text) throws NumberFormatException {
		return text == null ? 0 : Integer.valueOf(text);
	}

	/**
	 * 5.1 信用卡支付请求地址
	 */
	public static String getCreditRequestURL() {
		return Configuration.getInstance().getValue("creditRequestURL");
	}

	/**
	 * 5.2 储蓄卡支付请求地址
	 */
	public static String getDebitRequestURL() {
		return Configuration.getInstance().getValue("debitRequestURL");
	}

	/**
	 * 5.3 查询绑卡信息列表请求地址
	 */
	public static String getBankcardBindQueryURL() {
		return Configuration.getInstance().getValue("bankcardBindQueryURL");
	}

	/**
	 * 5.4 绑卡支付请求请求地址
	 */
	public static String getBindidRequestURL() {
		return Configuration.getInstance().getValue("bindidRequestURL");
	}
	
	/**
	 * 5.5 发送短信验证码请求地址
	 */
	public static String getSendMessageURL() {
		return Configuration.getInstance().getValue("sendMessageURL");
	}

	/**
	 * 5.6 确认支付请求地址
	 */
	public static String getPaymentConfirmURL() {
		return Configuration.getInstance().getValue("paymentConfirmURL");
	}
	
	/**
	 * 5.7 支付结果查询请求地址
	 */
	public static String getPayapiQueryURL() {
		return Configuration.getInstance().getValue("payapiQueryURL");
	}

	/**
	 * 订单单笔查询请求地址
	 */
	public static String getSingleQueryURL() {
		return Configuration.getInstance().getValue("singleQueryURL");
	}

	/**
	 * 消费对账文件下载请求地址
	 */
	public static String getPayClearDataURL() {
		return Configuration.getInstance().getValue("payClearDataURL");
	}

	/**
	 * 单笔退款请求地址
	 */
	public static String getRefundURL() {
		return Configuration.getInstance().getValue("refundURL");
	}

	/**
	 * 单笔退款查询请求地址
	 */
	public static String getRefundQueryURL() {
		return Configuration.getInstance().getValue("refundQueryURL");
	}

	/**
	 * 退款对账文件下载请求地址
	 */
	public static String getRefundClearDataURL() {
		return Configuration.getInstance().getValue("refundClearDataURL");
	}

	/**
	 * 查询银行卡信息请求地址
	 */
	public static String getBankCardCheckURL() {
		return Configuration.getInstance().getValue("bankCardCheckURL");
	}

	/**
	 * 银行卡解绑请求地址
	 */
	public static String getUnbindBankcardURL() {
		return Configuration.getInstance().getValue("unbindBankcardURL");
	}


	/**
	 * 解析http请求返回
	 */
	public static Map<String, String> parseHttpResponseBody(int statusCode, String responseBody) throws Exception {

		String merchantPrivateKey	= getMerchantPrivateKey();
		String yeepayPublicKey		= getYeepayPublicKey();

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";

		if(statusCode != 200) {
			customError	= "Request failed, response code : " + statusCode;
			result.put("customError", customError);
			return (result);
		}

		Map<String, String> jsonMap	= JSON.parseObject(responseBody, 
											new TypeReference<TreeMap<String, String>>() {});

		if(jsonMap.containsKey("error_code")) {
			result	= jsonMap;
			return (result);
		}

		String dataFromYeepay		= formatString(jsonMap.get("data"));
		String encryptkeyFromYeepay	= formatString(jsonMap.get("encryptkey"));

		boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay, 
										yeepayPublicKey, merchantPrivateKey);
		if(!signMatch) {
			customError	= "Sign not match error";
			result.put("customError",	customError);
			return (result);
		}

		String yeepayAESKey		= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
		String decryptData		= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);

		result	= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		return(result);
	}
	
	public static void main(String[] avg){
	
//		Map<String,String> pa = new HashMap<String,String>();
//		pa.put("orderid", "test001");
//		pa.put("cardno", "62260902365823659");
//		pa.put("validthru", "0321");
//		pa.put("cvv2", "321");
//		pa.put("phone", "17025366985");
//		pa.put("productcatalog", "11");
//		pa.put("productname", "测试商品");
//		pa.put("productdesc", "测试商品描述");
//		pa.put("identityid", "ebao001");
//		pa.put("userip", "192.168.1.2");
//		pa.put("terminalid", "1111");
//		pa.put("identityid", "ebao001");
//		pa.put("callbackurl", "ebao001");
//		Map<String, String>  t = creditRequest(pa);
		
		Map<String, String>  t = bankcardBindQuery("testdddd","2");
				
				
		
		
	}
	
	/**
	 * HttpPost调用
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,String> httpPost(String requestURL,Map<String,String> params) throws Exception{
		Map<String, String> result	= new HashMap<String, String>();
		try {	
			HttpResponseBean bean = HttpUtils.httpPost(requestURL, params,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
            result= parseHttpResponseBody(bean.getStatusCode(), bean.getEntityContent());
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			String customError	= "Caught an Exception." + e.toString();
			throw new Exception(customError);
		} 
		return result;
	}
	
	/**
	 * HttpGet调用
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,String> httpGet(String requestURL) throws Exception{
		Map<String, String> result	= new HashMap<String, String>();
		try {	
			HttpResponseBean bean = HttpUtils.httpGet(requestURL,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
            result= parseHttpResponseBody(bean.getStatusCode(), bean.getEntityContent());
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			String customError	= "Caught an Exception." + e.toString();
			throw new Exception(customError);
		} 
		return result;
	}



	/**
	 * creditRequest() : 5.1 信用卡支付请求
	 */

	public static Map<String, String> creditRequest(Map<String, String> params) {

		log.info("##### creditRequest() #####");

		Map<String, String> result	= new HashMap<String, String>();
        String customError	  		= "";	//自定义，非接口返回
				
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String creditRequestURL		= getCreditRequestURL();

		String orderid              = formatString(params.get("orderid"));
		String cardno               = formatString(params.get("cardno"));
		String validthru            = formatString(params.get("validthru"));
		String cvv2                 = formatString(params.get("cvv2"));
		String phone                = formatString(params.get("phone"));
		String productcatalog       = formatString(params.get("productcatalog"));
		String productname          = formatString(params.get("productname"));
		String productdesc          = formatString(params.get("productdesc"));
		String identityid           = formatString(params.get("identityid"));
		String userip               = formatString(params.get("userip"));
		String terminalid           = formatString(params.get("terminalid"));
		String callbackurl          = formatString(params.get("callbackurl"));

		int terminaltype			= 0;
		int transtime				= 0;
		int amount					= 0;
		int identitytype			= 0;
		int currency				= 0;
		int orderexpdate			= 0;

		try {
			if(params.get("terminaltype") == null) {
				throw new Exception("terminaltype is null!!!!!");
			} else {
				terminaltype    = String2Int(params.get("terminaltype"));
			}

			if(params.get("transtime") == null) {
				throw new Exception("transtime is null!!!!!");
			} else {
				transtime		= String2Int(params.get("transtime"));
			}

			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount          = String2Int(params.get("amount"));
			}

			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype    = String2Int(params.get("identitytype"));
			}

			if(params.get("orderexpdate") == null) {
				throw new Exception("orderexpdate is null!!!!!");
			} else {
				orderexpdate    = String2Int(params.get("orderexpdate"));
			}

			currency	        = String2Int(params.get("currency"));

		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			return result;
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid", 			orderid);
		dataMap.put("amount", 			amount);
		dataMap.put("transtime", 		transtime);
		dataMap.put("cardno", 			cardno);
		dataMap.put("validthru", 		validthru);
		dataMap.put("cvv2", 			cvv2);
		dataMap.put("phone", 			phone);
		dataMap.put("productcatalog", 	productcatalog);
		dataMap.put("productname", 		productname);
		dataMap.put("productdesc",		productdesc);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("identityid", 		identityid);
		dataMap.put("userip", 			userip);
		dataMap.put("terminaltype", 	terminaltype);
		dataMap.put("terminalid", 		terminalid);
		dataMap.put("callbackurl",		callbackurl);
		dataMap.put("currency", 		currency);
		dataMap.put("orderexpdate", 	orderexpdate);
						
		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
        
		log.debug("creditRequestURL : " + creditRequestURL);
		log.debug("dataMap : " + dataMap);

		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(creditRequestURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.debug("result : " + result);
		return result;

	}


	/**
	 * debitRequest() : 5.2 储蓄卡支付请求
	 */
	public static Map<String, String> debitRequest(Map<String, String> params) {

		log.info("##### debitRequest() #####");
				
		Map<String, String> result	= new HashMap<String, String>();
        String customError	  		= "";	//自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String debitRequestURL		= getDebitRequestURL();

		String cardno               = formatString(params.get("cardno"));
		String phone                = formatString(params.get("phone"));
		String orderid              = formatString(params.get("orderid"));
		String productcatalog       = formatString(params.get("productcatalog"));
		String productname          = formatString(params.get("productname"));
		String identityid           = formatString(params.get("identityid"));
		String userip               = formatString(params.get("userip"));
		String terminalid           = formatString(params.get("terminalid"));
		String idcardtype           = formatString(params.get("idcardtype"));
		String idcard               = formatString(params.get("idcard"));
		String owner                = formatString(params.get("owner"));
		String productdesc          = formatString(params.get("productdesc"));
		String callbackurl          = formatString(params.get("callbackurl"));

		int terminaltype			= 0;
		int transtime				= 0;
		int amount					= 0;
		int identitytype			= 0;
		int currency				= 0;
		int orderexpdate			= 0;

		try {
			if(params.get("terminaltype") == null) {
				throw new Exception("terminaltype is null!!!!!");
			} else {
				terminaltype        = String2Int(params.get("terminaltype"));
			}

			if(params.get("transtime") == null) {
				throw new Exception("transtime is null!!!!!");
			} else {
				transtime			= String2Int(params.get("transtime"));
			}

			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount              = String2Int(params.get("amount"));
			}

			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype        = String2Int(params.get("identitytype"));
			}

			if(params.get("orderexpdate") == null) {
				throw new Exception("orderexpdate is null!!!!!");
			} else {
				orderexpdate        = String2Int(params.get("orderexpdate"));
			}

			currency	        	= String2Int(params.get("currency"));

		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			return result;
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("cardno", 			cardno);
		dataMap.put("phone", 			phone);
		dataMap.put("orderid", 			orderid);
		dataMap.put("productcatalog", 	productcatalog);
		dataMap.put("productname", 		productname);
		dataMap.put("identityid", 		identityid);
		dataMap.put("userip", 			userip);
		dataMap.put("terminalid", 		terminalid);
		dataMap.put("idcardtype", 		idcardtype);
		dataMap.put("idcard", 			idcard);
		dataMap.put("owner", 			owner);
		dataMap.put("productdesc",		productdesc);
		dataMap.put("callbackurl",		callbackurl);
		dataMap.put("terminaltype", 	terminaltype);
		dataMap.put("transtime", 		transtime);
		dataMap.put("amount", 			amount);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("currency", 		currency);
		dataMap.put("orderexpdate", 	orderexpdate);
						
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.debug("debitRequestURL : " + debitRequestURL);
		log.debug("dataMap : " + dataMap);
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(debitRequestURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.debug("result : " + result);
		return result;

	}


	/**
	 * bankcardBindQuery() : 5.3 查询绑卡信息列表
	 */

	public static Map<String, String> bankcardBindQuery(String identityid, String identitytype) {
		
		log.info("##### bankcardBindQuery() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= ""; // 自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bankcardBindQueryURL	= getBankcardBindQueryURL();
		
		int identitytype2Int		= -1;
		
		try {
			identitytype2Int		= String2Int(identitytype);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			return result;
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", merchantaccount);
		dataMap.put("identityid", identityid);
		dataMap.put("identitytype", identitytype2Int);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		log.debug("bankcardBindQueryURL : " + bankcardBindQueryURL);
		log.debug("dataMap : " + dataMap);
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			String url = bankcardBindQueryURL + "?merchantaccount="
					+ URLEncoder.encode(merchantaccount, "UTF-8") + "&data="
					+ URLEncoder.encode(data, "UTF-8") + "&encryptkey="
					+ URLEncoder.encode(encryptkey, "UTF-8");
			result = httpGet(url);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;
		
	}

		
	/**
	 * bindidRequest() : 5.4 绑卡支付请求
	 */

	public static Map<String, String> bindidRequest(Map<String, String> params) {
		
		log.info("##### bindidRequest() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= ""; //自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bindidRequestURL		= getBindidRequestURL();
		
		String bindid				= formatString(params.get("bindid"));
		String orderid              = formatString(params.get("orderid"));
		String productcatalog       = formatString(params.get("productcatalog"));
		String productname          = formatString(params.get("productname"));
		String identityid           = formatString(params.get("identityid"));
		String terminalid           = formatString(params.get("terminalid"));
		String userip               = formatString(params.get("userip"));
		String callbackurl          = formatString(params.get("callbackurl"));
		String productdesc          = formatString(params.get("productdesc"));
		String other                = formatString(params.get("other"));

		int transtime				= 0; 
		int amount              	= 0; 
		int identitytype        	= 0; 
		int terminaltype        	= 0; 
		int currency	        	= 0; 
		int orderexpdate        	= 0; 
		
		try {
			if(params.get("transtime") == null) {
				throw new Exception("transtime is null!!!!!");
			} else {
				transtime	    = String2Int(params.get("transtime"));
			}

			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype   	= String2Int(params.get("identitytype"));
			}

			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount        	= String2Int(params.get("amount"));
			}

			if(params.get("orderexpdate") == null) {
				throw new Exception("orderexpdate is null!!!!!");
			} else {
				orderexpdate     = String2Int(params.get("orderexpdate"));
			}

			terminaltype       	= String2Int(params.get("terminaltype"));
			currency	       	= String2Int(params.get("currency"));
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError = "Caught an Exception. " + e.toString();
			result.put("customError", customError);
//			log.info("params: " + params);
			return (result);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("bindid", 			bindid);
		dataMap.put("orderid", 			orderid);
		dataMap.put("productcatalog", 	productcatalog);
		dataMap.put("productname", 		productname);
		dataMap.put("identityid", 		identityid);
		dataMap.put("terminalid", 		terminalid);
		dataMap.put("userip", 			userip);
		dataMap.put("transtime", 		transtime);
		dataMap.put("amount", 			amount);
		dataMap.put("identitytype", 	identitytype);
		dataMap.put("terminaltype", 	terminaltype);
		dataMap.put("callbackurl", 		callbackurl);
		dataMap.put("productdesc", 		productdesc);
		dataMap.put("currency", 		currency);
		dataMap.put("other", 			other);
		dataMap.put("orderexpdate", 	orderexpdate);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.info("bindidRequestURL : " + bindidRequestURL);
		log.info("dataMap : " + dataMap);
		
		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(bindidRequestURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;
	}


	/**
	 * sendMessage() : 5.5 发送短信验证码
	 */

	public static Map<String, String> sendMessage(String orderid) {
		
		log.info("##### sendMessage() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String sendMessageURL		= getSendMessageURL();

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
        dataMap.put("orderid", 			orderid);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.info("sendMessageURL : " + sendMessageURL);
		log.info("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= ""; //自定义，非接口返回
		
		try {
			String jsonStr			= JSON.toJSONString(dataMap);
			String data				= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey		= RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(sendMessageURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;
		
	}


	/**
	 * paymentConfirm() : 5.6 确认支付
	 */

	public static Map<String, String> paymentConfirm(Map<String, String> params) {
		
		log.info("##### payapiPayConfirm() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String paymentConfirmURL	= getPaymentConfirmURL();

		String validatecode			= params.get("validatecode");
		String orderid          	= params.get("orderid");

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("validatecode", 	validatecode);
        dataMap.put("orderid", 			orderid);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.info("paymentConfirmURL : " + paymentConfirmURL);
		log.info("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(paymentConfirmURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;
	}

	/**
	 * payapiQueryByOrderid() : 5.7 支付结果查询
	 */
	
	public static Map<String, String> payapiQueryByOrderid(String orderid) {
		
		log.info("##### payapiQueryByOrderid() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String payapiQueryURL		= getPayapiQueryURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid",			orderid);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		log.info("payapiQueryURL : " + payapiQueryURL);
		log.info("dataMap : " + dataMap);
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			String url = payapiQueryURL + "?merchantaccount="
					+ URLEncoder.encode(merchantaccount, "UTF-8") + "&data="
					+ URLEncoder.encode(data, "UTF-8") + "&encryptkey="
					+ URLEncoder.encode(encryptkey, "UTF-8");
			result = httpGet(url);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;
	}
	

	/**
	 * decryptCallbackData() : 返回支付回调参数
	 */

	public static Map<String, String> decryptCallbackData(String data, String encryptkey) {
		
		log.info("##### decryptCallbackData() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回

		try {
			boolean signMatch = EncryUtil.checkDecryptAndSign(data, encryptkey, yeepayPublicKey, merchantPrivateKey);

			if(!signMatch) {
				customError	= "Sign not match error";
				result.put("customError",	customError);
				return result;
			}

			String yeepayAESKey	= RSA.decrypt(encryptkey, merchantPrivateKey);
			String decryptData	= AES.decryptFromBase64(data, yeepayAESKey);

			result	= JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError = "Caught an Exception. " + e.toString();
			result.put("customError", customError);
			
		}

		log.info("result : " + result);

		return result;
	}

			
	/**
	 * singleQuery() : 单笔查询
	 */

	public static Map<String, String> singleQuery(String orderid, String yborderid) {
		
		log.info("##### singleQuery() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String singleQueryURL		= getSingleQueryURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid",			orderid);
		dataMap.put("yborderid",		yborderid);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		log.info("singleQueryURL : " + singleQueryURL);
		log.info("dataMap : " + dataMap);
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			String url = singleQueryURL + "?merchantaccount="
					+ URLEncoder.encode(merchantaccount, "UTF-8") + "&data="
					+ URLEncoder.encode(data, "UTF-8") + "&encryptkey="
					+ URLEncoder.encode(encryptkey, "UTF-8");
			result = httpGet(url);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;

	}

	/**
	 * getPathOfPayClearData() 
	 *
	 * 参数说明：
	 *
	 * merchantaccount		- 商户编号
	 * merchantPrivateKey	- 商户私钥
	 * merchantAESKey		- 商户随机生成的AESKey
	 * yeepayPublicKey		- 易宝公玥
	 *
	 * 接口请求参数：所有的请求参数名，均是大小写敏感的，如：merchantaccount，为小写无大写。
	 *
	 * merchantaccount		- string	- 必填		- 商户编号
	 * startdate			- string	- 必填		- 查询起始时间，格式：2015-01-01
	 * enddate				- string	- 必填		- 查询终止时间，格式：2015-01-31
	 * sign                 - string	- 必填		- 签名信息
	 *
	 * 返回说明：
	 *
	 * filePath				- 批量查询结果文件的路径
	 * error_code			- 错误返回码
	 * error				- 错误信息
	 * customError			- 自定义，非接口返回
	 *
	 */

	public static Map<String, String> getPathOfPayClearData(String startdate, String enddate, String sysPath) {
		
		log.info("##### getPathOfPayClearData() #####");

		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String payClearDataURL		= getPayClearDataURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 			enddate);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		Map<String, String> queryResult	= new HashMap<String, String>();
		String filePath					= "";
		String error_code              	= "";
		String error                   	= "";
		String customError				= "";
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= payClearDataURL + 
										  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") + 
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
            HttpResponseBean bean = HttpUtils.httpGet(url,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
            int statusCode=bean.getStatusCode();
			if(statusCode != 200) {
				customError = "Get request failed, response code = " + statusCode;
				queryResult.put("customError", customError);
				return (queryResult);
			}

			InputStream	responseStream =  new ByteArrayInputStream(bean.getEntityContent().getBytes("UTF-8")); 
			BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));

			String line					= reader.readLine();
			if(line.startsWith("{")) {
				Map<String, Object> jsonMap	= JSON.parseObject(line, TreeMap.class);

				if(jsonMap.containsKey("error_code")) {
					error_code					= formatString((String)jsonMap.get("error_code"));
					error						= formatString((String)jsonMap.get("error"));
				} else {
					String dataFromYeepay		= formatString((String)jsonMap.get("data"));
					String encryptkeyFromYeepay	= formatString((String)jsonMap.get("encryptkey")); 

					String yeepayAESKey					= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
					String decryptData					= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
					Map<String, Object> decryptDataMap	= JSON.parseObject(decryptData, TreeMap.class);

					error_code 					= formatString((String)decryptDataMap.get("error_code"));
					error						= formatString((String)decryptDataMap.get("error"));

					log.info("decryptData : " + decryptData);
				}
			} else {
				String outputFilePath				= sysPath + File.separator + "clearData";
				File file							= new File(outputFilePath);
				file.mkdir();

				String time							= String.valueOf(System.currentTimeMillis());
				String fileName						= "payClearData_" + startdate + "_" + enddate + "_" + time + ".txt";
				String absolutePathOfOutputFile		= outputFilePath + File.separator + fileName;
				filePath							= absolutePathOfOutputFile;

				File outputFile						= new File(absolutePathOfOutputFile);
				FileWriter fileWriter				= new FileWriter(outputFile);
				BufferedWriter writer				= new BufferedWriter(fileWriter);

				log.info("filePath : " + filePath);

				writer.write(line);
				writer.write(System.getProperty("line.separator"));
				while((line = reader.readLine()) != null) {
					writer.write(line);
					writer.write(System.getProperty("line.separator"));
				}
				
				writer.close();
			}
		} catch(Exception e) {
			customError = "Caught an Exception. " + e.toString();
			e.printStackTrace();
		} 
		
		queryResult.put("filePath",		filePath);
		queryResult.put("error_code",	error_code);
		queryResult.put("error",		error);
		queryResult.put("customError",	customError);

		return (queryResult);
	}
			
	
	/**
	 * refund() : 单笔退款方法
	 */

	public static Map<String, String> refund(Map<String, String> params) {

		log.info("##### refund() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundURL			= getRefundURL();
		
		String origyborderid		= formatString(params.get("origyborderid"));
		String orderid          	= formatString(params.get("orderid"));
		String cause            	= formatString(params.get("cause"));
		
		int amount              	= 0; 
		int currency	        	= 0; 
		
		try {
			//amount、currency是必填参数
			if(params.get("amount") == null) {
				throw new Exception("amount is null!!!!!");
			} else {
				amount 	 = String2Int(params.get("amount"));
			}

			if(params.get("currency") == null) {
				throw new Exception("currency is null!!!!!");
			} else {
				currency  = String2Int(params.get("currency"));
			}

		} catch(Exception e) {
			e.printStackTrace();
			customError	= "******input params error : String to Int Exception - " +
								"], amount=[" + amount +
								"], currency=[" + currency + "]" + e.toString();
			result.put("customError", customError);
			return (result);
		}

		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("origyborderid",	origyborderid);
		dataMap.put("orderid", 			orderid);
		dataMap.put("cause",			cause);
		dataMap.put("amount", 			amount);
		dataMap.put("currency", 		currency);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.info("params : " + params);
		log.info("refundURL : " + refundURL);
		log.info("dataMap : " + dataMap);
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(refundURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;

	}

	/**
	 * refundQuery() : 退款查询
	 */

	public static Map<String, String> refundQuery(String orderid, String yborderid) {
		
		log.info("##### refundQuery() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundQueryURL		= getRefundQueryURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("orderid",			orderid);
		dataMap.put("yborderid",		yborderid);

		String sign	= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		log.info("refundQueryURL : " + refundQueryURL);
		log.info("dataMap : " + dataMap);
		
		Map<String, String> result	= new HashMap<String, String>();
    	String customError        	= ""; 
    	
    	try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url = refundQueryURL + "?merchantaccount="
					+ URLEncoder.encode(merchantaccount, "UTF-8") + "&data="
					+ URLEncoder.encode(data, "UTF-8") + "&encryptkey="
					+ URLEncoder.encode(encryptkey, "UTF-8");
			result = httpGet(url);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;

	}


	/**
	 * getPathOfRefundClearData() 
	 *
	 * 参数说明：
	 *
	 * merchantaccount		- 商户编号
	 * merchantPrivateKey	- 商户私钥
	 * merchantAESKey		- 商户随机生成的AESKey
	 * yeepayPublicKey		- 易宝公玥
	 *
	 * 接口请求参数：所有的请求参数名，均是大小写敏感的，如：merchantaccount，为小写无大写。
	 *
	 * merchantaccount		- string	- 必填		- 商户编号
	 * startdate			- string	- 必填		- 查询起始时间，格式：2015-01-01
	 * enddate				- string	- 必填		- 查询终止时间，格式：2015-01-31
	 * sign                 - string	- 必填		- 签名信息
	 *
	 * 返回说明：
	 *
	 * filePath				- 批量查询结果文件的路径
	 * error_code			- 错误返回码
	 * error				- 错误信息
	 * customError			- 自定义，非接口返回
	 *
	 */

	public static Map<String, String> getPathOfRefundClearData(String startdate, String enddate, String sysPath) {
		
		log.info("##### getPathOfRefundClearData() #####");

		
		String merchantaccount			= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String refundClearDataURL		= getRefundClearDataURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 			enddate);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		log.info("refundClearDataURL : " + refundClearDataURL);
		log.info("dataMap : " + dataMap);
		
		Map<String, String> queryResult	= new HashMap<String, String>();
		String filePath					= "";
		String error_code              	= "";
		String error                   	= "";
		String customError             	= "";
		
		try {
			String jsonStr				= JSON.toJSONString(dataMap);
			String data					= AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey			= RSA.encrypt(merchantAESKey, yeepayPublicKey);

			String url					= refundClearDataURL + 
										  "?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") + 
										  "&data=" + URLEncoder.encode(data, "UTF-8") +
										  "&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");

			HttpResponseBean bean = HttpUtils.httpGet(url,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
			int statusCode				= bean.getStatusCode();
			
			if(statusCode != 200) {
				customError = "Get request failed, response code = " + statusCode;
				queryResult.put("customError", customError);
				return queryResult;
			}
			
			InputStream	responseStream =  new ByteArrayInputStream(bean.getEntityContent().getBytes("UTF-8")); 
			BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));

			String line					= reader.readLine();
			if(line.startsWith("{")) {
				Map<String, Object> jsonMap	= JSON.parseObject(line, TreeMap.class);

				if(jsonMap.containsKey("error_code")) {
					error_code					= formatString((String)jsonMap.get("error_code"));
					error						= formatString((String)jsonMap.get("error"));
				} else {
					String dataFromYeepay		= formatString((String)jsonMap.get("data"));
					String encryptkeyFromYeepay	= formatString((String)jsonMap.get("encryptkey")); 

					String yeepayAESKey					= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
					String decryptData					= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
					Map<String, Object> decryptDataMap	= JSON.parseObject(decryptData, TreeMap.class);

					error_code 				= formatString((String)decryptDataMap.get("error_code"));
					error					= formatString((String)decryptDataMap.get("error"));

					log.info("decryptData : " + decryptData);
				}
			} else {
				String outputFilePath			= sysPath + File.separator + "clearData";
				File file						= new File(outputFilePath);
				file.mkdir();

				String time						= String.valueOf(System.currentTimeMillis());
				String fileName					= "refundClearData_" + startdate + "_" + enddate + "_" + time + ".txt";
				String absolutePathOfOutputFile	= outputFilePath + File.separator + fileName;
				filePath						= absolutePathOfOutputFile;

				File outputFile					= new File(absolutePathOfOutputFile);
				FileWriter fileWriter			= new FileWriter(outputFile);
				BufferedWriter writer			= new BufferedWriter(fileWriter);

				log.info("filePath : " + filePath);

				writer.write(line);
				writer.write(System.getProperty("line.separator"));
				while((line = reader.readLine()) != null) {
					writer.write(line);
					writer.write(System.getProperty("line.separator"));
				}
				
				writer.close();
			}
		} catch(Exception e) {
			log.error(e);
			customError	= "Caught an Exception. " + e.toString();
			e.printStackTrace();
			
		} 

		queryResult.put("filePath",		filePath);
		queryResult.put("error_code",	error_code);
		queryResult.put("error",		error);
		queryResult.put("customError", customError);

		return (queryResult);
	}

	/**
	 * bankCardCheck() : 银行卡查询方法
	 */

	public static Map<String, String> bankCardCheck(String cardno) {
		
		log.info("##### bankCardCheck() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bankCardCheckURL		= getBankCardCheckURL();
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("cardno", 			cardno);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.info("bankCardCheckURL : " + bankCardCheckURL);
		log.info("dataMap : " + dataMap);

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(bankCardCheckURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;

	}

	/**
	 * unbindBankcard() : 银行卡解绑方法
	 */

	public static Map<String, String> unbindBankcard(Map<String, String> params) {
		
		log.info("##### unbindBankcard() #####");

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String unbindBankcardURL	= getUnbindBankcardURL();

		String bindid				= formatString(params.get("bindid"));
		String identityid			= formatString(params.get("identityid"));

		int identitytype	        = 0; 
		
		try {
			if(params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype 	 = String2Int(params.get("identitytype"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			customError	= "******input params error : String to Int Exception - " +
								" identitytype=[" + formatString(params.get("identitytype")) + "]" + e.toString();
			result.put("customError", customError);
			return (result);
		}
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("bindid", 			bindid);
		dataMap.put("identityid", 		identityid);
		dataMap.put("identitytype", 	identitytype);
				
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		log.info("unbindBankcardURL : " + unbindBankcardURL);
		log.info("dataMap : " + dataMap);
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
			Map<String,String> formparams = new HashMap<String,String>();
			formparams.put("merchantaccount", merchantaccount);
			formparams.put("data", data);
			formparams.put("encryptkey", encryptkey);
			result = httpPost(unbindBankcardURL,formparams);
		} catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			customError	= "Caught an Exception." + e.toString();
			result.put("customError", customError);
		}
		log.info("result : " + result);
		return result;

	}
}

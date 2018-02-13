package com.models.cloud.pay.escrow.yeepay.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.models.cloud.pay.common.http.model.HttpResponseBean;
import com.models.cloud.pay.common.http.utils.HttpUtils;
import com.models.cloud.pay.escrow.yeepay.model.PayClearData;
import com.models.cloud.pay.escrow.yeepay.model.RefundClearData;
import com.models.cloud.pay.escrow.yeepay.service.InstantPayService;
import com.models.cloud.pay.escrow.yeepay.utils.AES;
import com.models.cloud.pay.escrow.yeepay.utils.Constants;
import com.models.cloud.pay.escrow.yeepay.utils.EncryUtil;
import com.models.cloud.pay.escrow.yeepay.utils.RSA;
import com.models.cloud.pay.escrow.yeepay.utils.RandomUtil;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.StringReplaceUtil;

/**
 * 一键支付接口实现类
 */
@Service("instantPayServiceImpl")
public class InstantPayServiceImpl implements InstantPayService {
	
	
	//日志
	private static Logger log = Logger.getLogger(InstantPayServiceImpl.class);
	
	/**
	 * 取得商户编号
	 */
	private String getMerchantAccount() {
		return CacheUtils.getPpIntfProp(Constants.FUND_ID).getPpSpCode();
	}
	
	/**
	 * 取得商户私钥
	 */
	private String getMerchantPrivateKey() {
		return CacheUtils.getPpIntfProp(Constants.FUND_ID).getEbaoPriKey();
	}

	/**
	 * 取得商户AESKey
	 */
	private String getMerchantAESKey() {
		return (RandomUtil.getRandom(16));
	}

	/**
	 * 取得易宝公玥
	 */
	private String getYeepayPublicKey() {
		return  CacheUtils.getPpIntfProp(Constants.FUND_ID).getPpPubKey();
	}

	/**
	 * 格式化字符串
	 */
	private String formatString(String text) {
		return (text == null ? "" : text.trim());
	}

	/**
	 * String2Integer
	 */
	private int String2Int(String text) throws NumberFormatException {
		return text == null ? 0 : Integer.valueOf(text);
	}
	
	/**
	 * 解析http请求返回
	 */
	private Map<String, String> parseHttpResponseBody(int statusCode, String responseBody) throws Exception {

		String merchantPrivateKey = getMerchantPrivateKey();
		String yeepayPublicKey = getYeepayPublicKey();

		Map<String, String> result = new HashMap<String, String>();
		String customError = "";

		if(statusCode != 200) {
			customError	= "Request failed, response code : " + statusCode;
			result.put("customError", customError);
			return result;
		}

		Map<String, String> jsonMap	= JSON.parseObject(responseBody, new TypeReference<TreeMap<String, String>>() {});

		if(jsonMap.containsKey("error_code")) {
			result	= jsonMap;
			return result;
		}

		String dataFromYeepay = formatString(jsonMap.get("data"));
		String encryptkeyFromYeepay	= formatString(jsonMap.get("encryptkey"));

		boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay, yeepayPublicKey, merchantPrivateKey);
		if(!signMatch) {
			customError	= "Sign not match error";
			result.put("customError",	customError);
			return result;
		}

		String yeepayAESKey	= RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
		String decryptData	= AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);

		result = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		return result;
	}
	
	/**
	 * HttpPost调用
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public Map<String,String> httpPost(String requestURL,Map<String,String> params) throws Exception{
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
	public Map<String,String> httpGet(String requestURL) throws Exception{
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


	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#creditRequest(java.util.Map)
	 */
	@Override
	public Map<String, String> creditRequest(Map<String, String> params) throws Exception {
		log.info("##### creditRequest() #####");

		Map<String, String> result	= new HashMap<String, String>();
        String customError	  		= "";	//自定义，非接口返回
				
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String creditRequestURL		= CacheUtils.getDimSysConfConfValue("CREDIT_REQUEST_URL");

		String orderid              = formatString(params.get("orderid"));
		String cardno               = formatString(params.get("cardno"));
		String validthru            = formatString(params.get("validthru"));
		String cvv2                 = formatString(params.get("cvv2"));
		String phone                = formatString(params.get("phone"));
		String owner                = formatString(params.get("owner"));
		String idcard               = formatString(params.get("idcard"));
		String idcardtype           = "01"; //身份证类型，暂时支持01 身份证
		String productcatalog       = formatString(params.get("productcatalog"));
		String productname          = formatString(params.get("productname"));
		String productdesc          = formatString(params.get("productdesc"));
		String identityid           = formatString(params.get("identityid"));
		String userip               = formatString(params.get("userip"));
		String terminalid           = formatString(params.get("terminalid"));
		String callbackurl          = CacheUtils.getDimSysConfConfValue("INSTANT_PAY_CALLBACK_URL");

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
		dataMap.put("owner", 			owner);
		dataMap.put("idcard", 			idcard);	
		dataMap.put("idcardtype", 		idcardtype);
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
        try{
        	//日志处理
        	TreeMap<String, Object> logMap	= new TreeMap<String, Object>();
        	logMap.putAll(dataMap);
        	logMap.put("cardno", StringReplaceUtil.bankCardReplaceWithStar(logMap.get("cardno")));
        	logMap.put("validthru", StringReplaceUtil.StrReplaceWithStar(logMap.get("validthru")));
        	logMap.put("cvv2", StringReplaceUtil.StrReplaceWithStar(logMap.get("cvv2")));
        	log.debug("creditRequestURL : " + creditRequestURL);
    		log.debug("dataMap : " + logMap);
        }catch(Exception e){
        	log.error(e.getMessage());
        }
		
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
	
    /*
     * (non-Javadoc)
     * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#debitRequest(java.util.Map)
     */
	@Override
	public Map<String, String> debitRequest(Map<String, String> params)throws Exception {
		log.info("##### debitRequest() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
        String customError	  		= "";	//自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String debitRequestURL		= CacheUtils.getDimSysConfConfValue("DEBIT_REQUEST_URL");

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
		String callbackurl          = CacheUtils.getDimSysConfConfValue("INSTANT_PAY_CALLBACK_URL");

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

		try{
        	//日志处理
        	TreeMap<String, Object> logMap	= new TreeMap<String, Object>();
        	logMap.putAll(dataMap);
        	logMap.put("cardno", StringReplaceUtil.bankCardReplaceWithStar(logMap.get("cardno")));
        	log.debug("debitRequestURL : " + debitRequestURL);
    		log.debug("dataMap : " + logMap);
        }catch(Exception e){
        	log.error(e.getMessage());
        }
		
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#bankcardBindQuery(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> bankcardBindQuery(String identityid,String identitytype) throws Exception {
		log.info("##### bankcardBindQuery() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= ""; // 自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bankcardBindQueryURL	= CacheUtils.getDimSysConfConfValue("BANKCARD_BIND_QUERY_URL");
		
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
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#bindidRequest(java.util.Map)
	 */
	@Override
	public Map<String, String> bindidRequest(Map<String, String> params) throws Exception {
		log.info("##### bindidRequest() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= ""; //自定义，非接口返回

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bindidRequestURL		= CacheUtils.getDimSysConfConfValue("BINDID_REQUEST_URL");
		
		String bindid				= formatString(params.get("bindid"));
		String orderid              = formatString(params.get("orderid"));
		String productcatalog       = formatString(params.get("productcatalog"));
		String productname          = formatString(params.get("productname"));
		String identityid           = formatString(params.get("identityid"));
		String terminalid           = formatString(params.get("terminalid"));
		String userip               = formatString(params.get("userip"));
		String callbackurl          = CacheUtils.getDimSysConfConfValue("INSTANT_PAY_CALLBACK_URL");
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#sendMessage(java.lang.String)
	 */
	@Override
	public Map<String, String> sendMessage(String orderid) throws Exception {
		log.info("##### sendMessage() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String sendMessageURL		= CacheUtils.getDimSysConfConfValue("SEND_MESSAGE_URL");

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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#paymentConfirm(java.util.Map)
	 */
	@Override
	public Map<String, String> paymentConfirm(Map<String, String> params) throws Exception {
		log.info("##### payapiPayConfirm() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String paymentConfirmURL	= CacheUtils.getDimSysConfConfValue("PAYMENT_CONFIRM_URL");

		String validatecode			= params.get("validatecode");
		String orderid          	= params.get("orderid");
		String idcard = formatString(params.get("idcard"));
		String owner = formatString(params.get("owner"));
		String cvv2 = formatString(params.get("cvv2"));
		String validthru = formatString(params.get("validthru"));
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("validatecode", 	validatecode);
        dataMap.put("orderid", 			orderid);
        if(false == StringUtils.isBlank(idcard)){
        	String idcardtype = "01"; //身份证类型，暂时支持01 身份证
        	dataMap.put("idcardtype",idcardtype);
        	dataMap.put("idcard",idcard);
        }
        if(false == StringUtils.isBlank(owner)){
        	dataMap.put("owner",owner);
        }
        if(false == StringUtils.isBlank(cvv2)){
        	dataMap.put("cvv2",cvv2);
        }
        if(false == StringUtils.isBlank(validthru)){
        	dataMap.put("validthru",validthru);
        }		
		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);

		try{
        	//日志处理
        	TreeMap<String, Object> logMap	= new TreeMap<String, Object>();
        	logMap.putAll(dataMap);
        	if(false == StringUtils.isBlank(validthru)){
        		logMap.put("validthru", StringReplaceUtil.StrReplaceWithStar(logMap.get("validthru")));
        	}
        	if(false == StringUtils.isBlank(cvv2)){
        		logMap.put("cvv2", StringReplaceUtil.StrReplaceWithStar(logMap.get("cvv2")));
        	}
        	log.debug("paymentConfirmURL : " + paymentConfirmURL);
    		log.debug("dataMap : " + logMap);
        }catch(Exception e){
        	log.error(e.getMessage());
        }

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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#payapiQueryByOrderid(java.lang.String)
	 */
	@Override
	public Map<String, String> payapiQueryByOrderid(String orderid) throws Exception {
		log.info("##### payapiQueryByOrderid() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String payapiQueryURL		= CacheUtils.getDimSysConfConfValue("PAYAPI_QUERY_URL");
		
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#decryptCallbackData(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> decryptCallbackData(String data, String encryptkey) throws Exception {
		log.info("##### decryptCallbackData() #####");
		
//		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
//		String merchantAESKey		= getMerchantAESKey();
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#singleQuery(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> singleQuery(String orderid, String yborderid) throws Exception {
        log.info("##### singleQuery() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String singleQueryURL		= CacheUtils.getDimSysConfConfValue("SINGLE_QUERY_URL");
		
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#getPathOfPayClearData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getPayClearData(String startdate, String enddate) throws Exception {
		log.info("##### getPayClearData() #####");

		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String payClearDataURL		= CacheUtils.getDimSysConfConfValue("PAY_CLEAR_DATA_URL");
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 			enddate);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		Map<String, String> queryResult	= new HashMap<String, String>();
		String error_code              	= "1";
		String error                   	= "";
		String customError				= "";
		//消费清算对账单List
		List<PayClearData> lst = new ArrayList<PayClearData>();
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
			if(null != line){
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
					//逐行读取对账单记录
					while((line = reader.readLine()) != null) {
						//分割line
						String[] arrLine = line.split(",");
						if(null != arrLine && arrLine.length != 14){
							log.info("消费清算对账单" + line + "无法转换为对象");
							continue;
						}	
						PayClearData en = new PayClearData();
						en.setEbaoActCode(arrLine[0]);
						en.setClearDate(arrLine[1]);
						en.setXdTime(arrLine[2]);
						en.setTradeTime(arrLine[3]);
						en.setCustomOrdCode(arrLine[4]);
						en.setCustomTdCode(arrLine[5]);
						en.setTdAmt(arrLine[6]);
						en.setRealAmt(arrLine[7]);
						en.setChrgType(arrLine[8]);
						en.setServChrgAmt(arrLine[9]);
						en.setProdName(arrLine[10]);
						en.setGoodsType(arrLine[11]);
						en.setGoodsName(arrLine[12]);
						en.setPayActType(arrLine[13]);
						lst.add(en);
					}
				}
			}
		} catch(Exception e) {
			customError = "Caught an Exception. " + e.toString();
			e.printStackTrace();
		} 
		
		queryResult.put("payClearDate",JSON.toJSONString(lst));
		queryResult.put("error_code",error_code);
		queryResult.put("error",error);
		queryResult.put("customError",customError);

		return (queryResult);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#refund(java.util.Map)
	 */
	@Override
	public Map<String, String> refund(Map<String, String> params) throws Exception {
		log.info("##### refund() #####");
		
		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundURL			= CacheUtils.getDimSysConfConfValue("REFUND_URL");
		
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#refundQuery(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> refundQuery(String orderid, String yborderid) throws Exception {
		log.info("##### refundQuery() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String refundQueryURL		= CacheUtils.getDimSysConfConfValue("REFUND_QUERY_URL");
		
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#getPathOfRefundClearData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getRefundClearData(String startdate,String enddate) throws Exception {
        log.info("##### getRefundClearData() #####");

		
		String merchantaccount			= getMerchantAccount();
		String merchantPrivateKey		= getMerchantPrivateKey();
		String merchantAESKey			= getMerchantAESKey();
		String yeepayPublicKey			= getYeepayPublicKey();
		String refundClearDataURL		= CacheUtils.getDimSysConfConfValue("REFUND_CLEAR_DATA_URL");
		
		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", 	merchantaccount);
		dataMap.put("startdate", 		startdate);
		dataMap.put("enddate", 			enddate);

		String sign					= EncryUtil.handleRSA(dataMap, merchantPrivateKey);
		dataMap.put("sign", sign);
				
		log.info("refundClearDataURL : " + refundClearDataURL);
		log.info("dataMap : " + dataMap);
		
		Map<String, String> queryResult	= new HashMap<String, String>();
		String error_code              	= "1";
		String error                   	= "";
		String customError             	= "";
		//消费清算对账单List
		List<RefundClearData> lst = new ArrayList<RefundClearData>();
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
			if(null!=line){
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
					//逐行读取对账单记录
					while((line = reader.readLine()) != null) {
						//分割line
						String[] arrLine = line.split(",");
						if(null != arrLine && arrLine.length != 16){
							log.info("退款清算对账单" + line + "无法转换为对象");
							continue;
						}	

						RefundClearData en = new RefundClearData();
						en.setRecvActCode(arrLine[0]);
						en.setLiqDate(arrLine[1]);
						en.setRetApplyTime(arrLine[2]);
						en.setRetFinishTime(arrLine[3]);
						en.setCustRetCode(arrLine[4]);
						en.setRetSeqCode(arrLine[5]);
						en.setOrigOrdCode(arrLine[6]);
						en.setRetTypeName(arrLine[7]);
						en.setRetAmt(arrLine[8]);
						en.setChrgTypeName(arrLine[9]);
						en.setRetFee(arrLine[10]);
						en.setRealCutAmt(arrLine[11]);
						en.setPayProd(arrLine[12]);
						en.setGoodsType(arrLine[13]);
						en.setGoodsName(arrLine[14]);
						en.setPayActType(arrLine[15]);
						lst.add(en);
					}
				}
			}
		} catch(Exception e) {
			log.error(e);
			customError	= "Caught an Exception. " + e.toString();
			e.printStackTrace();	
		} 

		queryResult.put("refundClearDate",JSON.toJSONString(lst));
		queryResult.put("error_code",	error_code);
		queryResult.put("error",		error);
		queryResult.put("customError", customError);

		return (queryResult);

	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#bankCardCheck(java.lang.String)
	 */
	@Override
	public Map<String, String> bankCardCheck(String cardno) throws Exception {
        log.info("##### bankCardCheck() #####");
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String bankCardCheckURL		= CacheUtils.getDimSysConfConfValue("BANKCARD_CHECK_URL");
		
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

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.InstantPayService#unbindBankcard(java.util.Map)
	 */
	@Override
	public Map<String, String> unbindBankcard(Map<String, String> params)
			throws Exception {
		log.info("##### unbindBankcard() #####");

		Map<String, String> result	= new HashMap<String, String>();
		String customError			= "";	// 自定义，非接口返回
		
		String merchantaccount		= getMerchantAccount();
		String merchantPrivateKey	= getMerchantPrivateKey();
		String merchantAESKey		= getMerchantAESKey();
		String yeepayPublicKey		= getYeepayPublicKey();
		String unbindBankcardURL	= CacheUtils.getDimSysConfConfValue("UNBIND_BANKCARD_URL");

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

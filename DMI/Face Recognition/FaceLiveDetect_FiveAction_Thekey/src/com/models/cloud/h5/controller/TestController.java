package com.models.cloud.h5.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.models.cloud.common.cache.account.entity.ActSpInfo;
import com.models.cloud.common.cache.service.AccountCacheService;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.util.*;
import com.models.secrity.crypto.AesCipher;
import com.models.secrity.crypto.RSA;


/**
 * H5测试入口
 * @author qingsong.li
 *
 */

@Controller
@RequestMapping(value = "/h5Test")
public class TestController {

	@Resource(name="accountCacheServiceImpl")
    private AccountCacheService accountCacheServiceImpl;

	 private static final Logger logger = Logger.getLogger(TestController.class);
//	 String url = "http://10.10.10.129:8022/ebaonet-pay/ebaonet/gw.htm";
     String url = "http://localhost:8080/ebaonet-pay/ebaonet/gw.htm";

	 @RequestMapping(value = "payorder", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	 @ResponseBody
	 public String businessPage(@RequestBody Map<String, Object> map,HttpServletRequest request,HttpServletResponse response) throws Exception{

         String appId = String.valueOf(map.get("appId")).trim();

		 ActSpInfo actSpInfo = accountCacheServiceImpl.findActSpInfoByAppId(appId);

			//String ybPrivateKey = String.valueOf(actSpInfo.getServerKeyPri()).trim();
			String ybPublicKey = String.valueOf(actSpInfo.getServerKeyPub()).trim();
			String shPrivateKey = String.valueOf(actSpInfo.getSpKeyPri()).trim();

			map.put("reqTime", DateUtils.getNowDate("yyyyMMddHHmmss"));
			map.put("returnType", "2");
			map.put("version", "V1.2");
			map.put("systemId", "testSys001");
		 	String terminalType = String.valueOf(map.get("terminalType")).trim();
		 	if(terminalType.equalsIgnoreCase("web")){
				map.put("terminalType", DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCodeString());
				map.put("interfaceCode", "createPayOrderParam");
			}else if(terminalType.equals("wx")){
				map.put("terminalType", DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCodeString());
				map.put("interfaceCode", "createPayOrder");
			}
			map.put("uaInfo", "iPhone; U; CPU iPhone OS 3_1_2 like Mac OS X; zh-cn");
			map.put("ipInfo", "192.168.62.50");

            map.put("origOrderId", "");
			map.put("merOrderId", String.valueOf(System.currentTimeMillis()));

			//将参数转换成json
			String json = JSON.toJSONString(map);

			Map<String,String> data = new HashMap<String,String>();
			data.put("appid", appId);
			data.put("sign",  this.sign(json,shPrivateKey));//将json+商户privateKey生成加密串
			AesCipher aes = new AesCipher();
			String aesKey = aes.generateKeyToBase64(128);
			String aesIv =  aes.generateIvBytesToBase64();//生成aeskey 和 aesiv
			data.put("keys", this.AESCodeToRSA(aesKey,aesIv,ybPublicKey));//通过易保publickey加密
			data.put("data", this.aesData(json,aesKey,aesIv));//通过aeskey和aesiv加密

//			String url = "http://localhost:8080/ebaonet-pay/ebaonet/gw.htm";
//			String url = "http://124.193.81.78:8888/ebaonet-pay/ebaonet/gw.htm";
//			String url = "http://10.10.13.127:9008/ebaonet-pay/ebaonet/gw.htm";
//			String url = "http://localhost:8080/ebaonet-pay/ebaonet/gw.htm";
			String json2 = JSON.toJSONString(data);
			String resultStr = HttpRequest.doPostJson(url, json2);
			logger.info("返回json:"+resultStr);
			Map<String,Object> m = JSON.parseObject(resultStr);
		 	String resultCode = String.valueOf(m.get("resultCode")).trim();
		 	if("0".equals(resultCode)){
				logger.info("返回订单号："+m.get("payOrderId"));
				logger.info("调用H5的key："+m.get("key"));
				logger.info("key:"+URLEncoder.encode(URLEncoder.encode(m.get("key").toString(),"UTF-8"),"UTF-8"));
				return URLEncoder.encode(URLEncoder.encode(m.get("key").toString(),"UTF-8"),"UTF-8");
			}else{
				return String.valueOf(m.get("resultDesc")).concat("|error");
			}
	 }



	 @RequestMapping(value = "zc", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	 @ResponseBody
	 public String zc(@RequestBody Map<String, Object> map,HttpServletRequest request,HttpServletResponse response) throws Exception{


		 	ActSpInfo actSpInfo = accountCacheServiceImpl.findActSpInfoByAppId("1605039610214514385183");

			//String ybPrivateKey = String.valueOf(actSpInfo.getServerKeyPri()).trim();
			String ybPublicKey = String.valueOf(actSpInfo.getServerKeyPub()).trim();
			String shPrivateKey = String.valueOf(actSpInfo.getSpKeyPri()).trim();

			map.put("reqTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			map.put("returnType", 2);
			map.put("version", "1.0.0");
			map.put("appId", "1605039610214514385183");
			map.put("systemId", "00001");
			map.put("terminalType", "21");
			map.put("uaInfo", "iPhone; U; CPU iPhone OS 3_1_2 like Mac OS X; zh-cn");
			map.put("ipInfo", "192.168.62.50");
			map.put("hardwareId", request.getSession().getId());

			map.put("interfaceCode", "syncUser");
			map.put("userCode", map.get("phone"));
			map.put("password", Md5SaltUtils.encodeMd5(map.get("password")==null?"":map.get("password").toString()));

			//将参数转换成json
			String json = JSON.toJSONString(map);

			Map<String,String> data = new HashMap<String,String>();
			data.put("appid", "1605039610214514385183");
			data.put("sign",  this.sign(json,shPrivateKey));//将json+商户privateKey生成加密串
			AesCipher aes = new AesCipher();
			String aesKey = aes.generateKeyToBase64(128);
			String aesIv =  aes.generateIvBytesToBase64();//生成aeskey 和 aesiv
			data.put("keys", this.AESCodeToRSA(aesKey,aesIv,ybPublicKey));//通过易保publickey加密
			data.put("data", this.aesData(json,aesKey,aesIv));//通过aeskey和aesiv加密

			String json2 = JSON.toJSONString(data);
			String resultStr = HttpRequest.doPostJson(url, json2);
			logger.info("返回json:"+resultStr);
			Map<String,Object> m = JSON.parseObject(resultStr);
		 	String resultCode = String.valueOf(m.get("resultCode")).trim();
		 	if("0".equals(resultCode)){
		 		return jiemi( shPrivateKey,  ybPublicKey,resultStr);
			}else{
				return String.valueOf(m.get("resultDesc")).concat("|error");
			}
	 }

	 @RequestMapping(value = "dl", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	 @ResponseBody
	 public String dl(@RequestBody Map<String, Object> map,HttpServletRequest request,HttpServletResponse response) throws Exception{


		 	ActSpInfo actSpInfo = accountCacheServiceImpl.findActSpInfoByAppId("1605039610214514385183");

			//String ybPrivateKey = String.valueOf(actSpInfo.getServerKeyPri()).trim();
			String ybPublicKey = String.valueOf(actSpInfo.getServerKeyPub()).trim();
			String shPrivateKey = String.valueOf(actSpInfo.getSpKeyPri()).trim();

			map.put("reqTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			map.put("returnType", 2);
			map.put("version", "1.0.0");
			map.put("appId", "1605039610214514385183");
			map.put("systemId", "00001");
			map.put("terminalType", "21");
			map.put("uaInfo", "iPhone; U; CPU iPhone OS 3_1_2 like Mac OS X; zh-cn");
			map.put("ipInfo", "192.168.62.50");
			map.put("hardwareId", request.getSession().getId());

			map.put("interfaceCode", "getLoginToken");
			map.put("userCode", map.get("phone"));
			map.put("password", Md5SaltUtils.encodeMd5(map.get("password")==null?"":map.get("password").toString()));

			//将参数转换成json
			String json = JSON.toJSONString(map);

			Map<String,String> data = new HashMap<String,String>();
			data.put("appid", "1605039610214514385183");
			data.put("sign",  this.sign(json,shPrivateKey));//将json+商户privateKey生成加密串
			AesCipher aes = new AesCipher();
			String aesKey = aes.generateKeyToBase64(128);
			String aesIv =  aes.generateIvBytesToBase64();//生成aeskey 和 aesiv
			data.put("keys", this.AESCodeToRSA(aesKey,aesIv,ybPublicKey));//通过易保publickey加密
			data.put("data", this.aesData(json,aesKey,aesIv));//通过aeskey和aesiv加密

			String json2 = JSON.toJSONString(data);
			String resultStr = HttpRequest.doPostJson(url, json2);
			logger.info("返回json:"+resultStr);
			Map<String,Object> m = JSON.parseObject(resultStr);
		 	String resultCode = String.valueOf(m.get("resultCode")).trim();
		 	if("0".equals(resultCode)){
		 		return jiemi( shPrivateKey,  ybPublicKey,resultStr);
			}else{
				return String.valueOf(m.get("resultDesc")).concat("|error");
			}
	 }


	 /**
		 * {appid:1231,sign:s2034234-2kewlj,keys:w39230sfiojew9w,data:9siojfoidsjf93r2==}
		 * {appid:1231,sign:{appid:,userid:,password:},keys:{aesKey:afds,aesIv:afsfsd},data:{systemId:}}
		 *
		 *
		 *
		 * RSA数据签名
		 * @param json
	  	 * @param shPrivateKey
		 * @return
		 * @throws Exception
		 */
		public static String sign(String json,String shPrivateKey) throws Exception{

//			String json = JsonStringUtils.objectToJsonString(map);
			String sign = RSA.sign(json, shPrivateKey);//(json, shPrivateKey);
			return sign;
		}

		/**
		 * RSA易保公钥加密
		 * RSA加密
		 * AES秘钥
		 * @throws Exception
		 */
		public static String AESCodeToRSA(String aesKey,String aesIv,String ybPublicKey) throws Exception{

//			String aesKey = aes.generateIvBytesToBase64();
//			String aesIv =  aes.generateIvBytesToBase64();
			Map<String,String> map = new HashMap<String,String>();
			map.put("aesKey", aesKey);
			map.put("aesIv", aesIv);
			return RSA.encrypt(JsonStringUtils.objectToJsonString(map), ybPublicKey);
		}

		/**
		 * AES动态安全KEY加密
		 * @param data
		 * @param aesKey
		 * @param aesIv
		 * @return
		 */
		public static String aesData(String data,String aesKey,String aesIv){
			AesCipher aes = new AesCipher();
			return aes.encrypt(data,aesKey,aesIv);
		}

		public static String jiemi(String shPrivateKey, String ybPublicKey,String resultStr) throws Exception{
			Map<String,Object> resultMap = (Map<String, Object>) JSON.parse(resultStr);
			if(resultMap!=null && resultMap.get("resultCode")!=null && resultMap.get("resultCode").toString().equals("0")){
				String resultDecryptKeys = resultMap.get("keys").toString();
				String resulSign = resultMap.get("sign").toString();
				String resultKeys = RSA.decrypt(resultDecryptKeys, shPrivateKey);
				Map<String,Object> keysMap = (Map<String, Object>) JSON.parse(resultKeys);
				String raesKey = keysMap.get("aesKey").toString();
				String raesIv = keysMap.get("aesIv").toString();
				String resultDecryptData = resultMap.get("data").toString();
				AesCipher aes = new AesCipher();
				String resultData = aes.decrypt(resultDecryptData, raesKey, raesIv);
				System.out.println("返回数据为："+resultData);

				System.out.println("验签值："+RSA.checkSign(resultData, resulSign, ybPublicKey));
				if(RSA.checkSign(resultData, resulSign, ybPublicKey)){
					return resultData;
				}
			}
			System.out.println(resultStr);
			return resultStr;
		}


}

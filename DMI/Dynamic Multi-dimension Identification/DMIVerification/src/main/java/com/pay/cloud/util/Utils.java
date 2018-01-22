package com.pay.cloud.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pay.cloud.core.common.JsonStringUtils;
import com.pay.secrity.crypto.AesCipher;
import com.pay.secrity.crypto.RSA;

public class Utils {

	/**
	 * {appid:1231,sign:s2034234-2kewlj,keys:w39230sfiojew9w,data:9siojfoidsjf93r2==}
	 * {appid:1231,sign:{appid:,userid:,password:},keys:{aesKey:afds,aesIv:afsfsd},data:{systemId:}}
	 * 
	 * 
	 * 
	 * RSA数据签名
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String sign(String json,String shPrivateKey) throws Exception{
		
//		String json = JsonStringUtils.objectToJsonString(map);
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
	
//		String aesKey = aes.generateIvBytesToBase64();
//		String aesIv =  aes.generateIvBytesToBase64();
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
		}
		System.out.println(resultStr);
		return resultStr;
	}
}

package com.models.cloud.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;

/**
 * 发送短信工具类（梦网）
 *
 */
public class SendSmsMWUtil {
	private static String smsUrl = Propertie.APPLICATION.value("mw.smsMwUrl");
	private static String smsSn = Propertie.APPLICATION.value("mw.smsMwSn");
	private static String smsPwd = Propertie.APPLICATION.value("mw.smsMwPwd");
	private static String charset = Propertie.APPLICATION.value("mw.charset");
	private static String smsMwQueryResultUrl = Propertie.APPLICATION.value("mw.smsMwQueryResultUrl");
	
	private static final Logger logger = Logger.getLogger(SendSmsMWUtil.class);
	
	
	/**
	 * 请求短信服务商（梦网），发送短信
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static HashMap<String, Object> requestSendSmsWebservice(String mobile, String content) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("下发短信内容："+content);
		}
		HashMap<String, Object> respMap = new HashMap<String, Object>();
		String isRealSendSms = Propertie.APPLICATION.value("mw.isRealSendSms");
		if(!isRealSendSms.equals("0")) {//不真正下发短信
			if(logger.isInfoEnabled()){
				logger.info("不真正下发短信");
			}
			respMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			respMap.put("resultDesc", "发送短信成功");
			return respMap;
		}
		
		try{
			String responseModule = "";
			String rrid = IdCreatorUtils.getSMSId();
			StringBuffer param= new StringBuffer();
			param.append("userId=").append(URLEncoder.encode(smsSn, charset));
			param.append("&password=").append(URLEncoder.encode(smsPwd, charset));
			param.append("&pszMobis=").append(URLEncoder.encode(mobile, charset));
			param.append("&pszMsg=").append(URLEncoder.encode(content, charset));
			param.append("&iMobiCount=").append(URLEncoder.encode(String.valueOf(mobile.split(",").length), charset));
			param.append("&pszSubPort=").append(URLEncoder.encode("*", charset));
			param.append("&MsgId=").append(URLEncoder.encode(rrid, charset));
			
			if(logger.isInfoEnabled()){
				logger.info("请求梦网发送短信接口，请求参数："+smsUrl+"?"+param.toString());
			}
			responseModule = HttpRequest.sendGet(smsUrl, param.toString());
			if(logger.isInfoEnabled()){
				logger.info("请求梦网发送短信接口，返回参数："+responseModule);
			}
			
			Document doc = DocumentHelper.parseText(responseModule);
			Element root = doc.getRootElement();
			responseModule = root.getStringValue();
			if(responseModule == null || "".equals(responseModule) || responseModule.length()<15){
				if(logger.isInfoEnabled()){
					logger.info("发送短信失败，返回结果："+responseModule);
				}
				respMap.put("resultCode", Hint.SMS_60019_SEND_FAILED.getCodeString());
				respMap.put("resultDesc", Hint.SMS_60019_SEND_FAILED.getMessage());
				return respMap;
			}
			
			respMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			if(logger.isInfoEnabled()){
				respMap.put("resultDesc", "发送短信成功");
			}
			return respMap;
			
		}catch(Exception e){
			logger.error("调用梦网发送短信【抛出异常】--"+e);
			respMap.put("resultCode", Hint.SMS_60019_SEND_FAILED.getCodeString());
			respMap.put("resultDesc", Hint.SMS_60019_SEND_FAILED.getMessage());
			return respMap;
		}
		
	}
	
	public static HashMap<String, Object> queryResult() throws UnsupportedEncodingException{
		HashMap<String, Object> respMap = new HashMap<String, Object>();
		try{
			String responseModule = "";
			StringBuffer param2= new StringBuffer();
			param2.append("userId=").append(URLEncoder.encode(smsSn, charset));
			param2.append("&password=").append(URLEncoder.encode(smsPwd, charset));
			param2.append("&iReqType=2");
			logger.info("请求梦网查询状态接口，请求参数："+smsMwQueryResultUrl+"?"+param2.toString());
			responseModule = HttpRequest.sendGet(smsMwQueryResultUrl, param2.toString());
			
			logger.info("请求梦网查询状态接口，返回参数："+responseModule);
			respMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			respMap.put("resultDesc", "查询短信成功");
			respMap.put("data", responseModule);
			return respMap;
			
		}catch(Exception e){
			logger.error("调用梦网查询短信接口【抛出异常】--"+e);
			respMap.put("resultCode", Hint.SMS_60019_SEND_FAILED.getCodeString());
			respMap.put("resultDesc", Hint.SMS_60019_SEND_FAILED.getMessage());
			return respMap;
		}
		
	}
	
	public static void main(String[] args) {
		String mobile = "18611667914";
		String content = "发送的易保短信!";
		try {
			HashMap<String, Object> map = SendSmsMWUtil.requestSendSmsWebservice(mobile, content);
			System.out.println("map: "+map);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    /**
     * Object to Json
     * @param object 待转换对象
     * @return 返回json
     * @throws Exception
     */
	private static ObjectMapper objectMapper = new ObjectMapper();
    public static String objectToJson(Object object) throws Exception {
        if(null == object){
            return null;
        }
        if(null == objectMapper) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper.writeValueAsString(object);
    }
}

package com.ts.controller.app.SearchAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.RSAUtil.RSASignaturer256;
import com.ts.RSAUtil.RSAVerifier256;
import com.ts.controller.app.SearchAPI.BusinessAPI.util.ReadPropertiesFiles;
import com.ts.controller.base.BaseController;
import com.ts.entity.app.AppOrganiKey;
import com.ts.service.system.appuser.AppuserManager;
import com.ts.util.OperatorLog;
import com.ts.util.PageData;
import com.ts.util.ValidateParams;
import com.ts.util.app.SessionAppMap;
import com.ts.util.Enum.EnumStatus;

import net.sf.json.JSONObject;

/**
 * 对外验签
 * @author autumn
 */
@Controller
@RequestMapping(value = "/app")
public class BusinessHandlerAPI extends BaseController {
	
	private static boolean   signatureflag = false;  
	static{
		
		signatureflag =  "true".equals(ReadPropertiesFiles.getValue("signatureflag"))?true:false;
	}
	
	/* 添加用户登录ip  */
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	/* 业务转发适配器  */
	@Resource(name="adapterHandler")
	private AdapterHandler adapHandler;
	/* 编码格式 */
	private final String  charSet = "UTF-8";
	@RequestMapping(value = "/BusinessSignHandler", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String BusinessHandler(@RequestBody String json) {
		
		// 业务返回结果信息组
		Map<String, Object> rsJson  = new HashMap<String, Object>();
		//实际返回data 结果
		String rsData = "";
		// 签名 
		String rsSignature = "";
		//请求状态
		String status = "";
		// 获取客户端ip
		String ip = getRequest().getRemoteAddr(); 
		
		Long l = System.currentTimeMillis();
		String apitype = null;
		String userID = null;
		String resNo = null;
		String msg = null;
		JSONObject  inputJson = new JSONObject();
		try
		{
			inputJson =  this.getJsonObject(json);
			// 机构code
			String  organicode  = (String)inputJson.get("organicode");
			// 签名 
			String signature   = (String)inputJson.get("signature");
			// 传入入参 data 
			JSONObject dataJson = inputJson.getJSONObject("data");
			/// 根据机构号获得名字
			AppOrganiKey aokey = SessionAppMap.getAppOrgani(organicode);
			try{
				resNo = dataJson.getString("requestno");
			}catch (Exception e) {
				resNo = "";
				dataJson.put("requestno", resNo);
			}
			try {
				apitype = dataJson.getString("apitype");
			} catch (Exception e) {
				logger.info("接口参数apitype错误："+organicode);
				msg = "参数 apitype 异常！";
				status =  EnumStatus.Parameter_error.getEnumValue();
				return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
			}
			try {
				String timestamp = dataJson.getString("timestamp");
			} catch (Exception e) {
				logger.info("接口参数timestamp错误："+organicode);
				msg = "参数 timestamp 异常！";
				status =  EnumStatus.Parameter_error.getEnumValue();
				return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
			}
			if(aokey == null){
				logger.info("error该机构未注册："+organicode);
				status =  EnumStatus.illegal_identity.getEnumValue();
				return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
				//new RuntimeException("error该机构未注册："+organicode);
			}  
			userID = aokey.getAppUser().getUSER_ID();
			boolean isSignatrue =  false;
			
			if(aokey != null && this.RSAVerifier(aokey.getAppUser().getPublicKeyUser(), signature, dataJson.toString())) 
				isSignatrue = true;

			if(!isSignatrue) logBefore(logger, "验签未通过! organicode:" + organicode + ",signature:" + signature + ",data:" + dataJson.toString() + ",key:" + aokey.getAppUser().getPublicKeyUser());
			// 配置中增加是否开始验签功能 
			if(!BusinessHandlerAPI.signatureflag) isSignatrue = true;
			//验签失败 返回
			if(!isSignatrue){
				logger.info("isSignatrue fail");
				status =  EnumStatus.Check_error.getEnumValue();
				return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
			}
			// 判断用户是否有访问该业务的权限
			Set<String> list = aokey.getAppUser().getApitype();
			if (!list.contains(apitype)) {
				logger.info("无该业务的访问权限");
				msg = "无该业务的访问权限";
				status =  EnumStatus.Unauthorized_access.getEnumValue();
				return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
			}
			//验证参数
			Map<String, String> validMap = ValidateParams.validateParams(dataJson, aokey.getAppUser().getShowfield().get(apitype));
			if(!"true".equals(validMap.get("result"))){
				logger.info("params validata fail");
				status =  EnumStatus.Parameter_error.getEnumValue();
				msg = validMap.get("msg");
				return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
			}
			
			dataJson.put("IP", ip);
			dataJson.put("userID",userID);
			dataJson.put("GROUP_ID", organicode);
			dataJson.put("organicode", organicode);
			//修改用户登录时间和登录IP
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			PageData pd = new PageData();
			pd.put("USER_ID", aokey.getAppUser().getUSER_ID());
			pd.put("IP", ip);
			pd.put("LAST_LOGIN", sf.format(new Date()));
			appuserService.editIpAndTime(pd);
			//接口适配处理
			rsJson = adapHandler.adapter(dataJson,aokey);
			if(rsJson.get("status") != null){//返回失败状态
				status = String.valueOf(rsJson.get("status"));
				if(!"".equals(status) && !EnumStatus.success.getEnumValue().equals(status)){
					logger.info("adapter fail");
					return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
				}
			}
			//获取data信息 
			rsData = rsJson.containsKey("data") ? rsJson.get("data").toString():"{}";
			//TODO 从配置文件中获取  自己的私钥 
			String privateKey =  ReadPropertiesFiles.getValue("localPrivateKey");
			rsSignature = this.RSASignature(rsData, privateKey);
			status = EnumStatus.success.getEnumValue();
		} catch(Exception e ) {
			status =  EnumStatus.internal_server_error.getEnumValue();
			logBefore(logger, "参数错误");
			logger.error(e.getMessage(), e);
			return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
		}
		return getReturnStr(msg, userID, ip, resNo, inputJson, rsSignature, status, rsData, l, apitype);
	}
	/**获取返回结果字符串
	 * @param signature
	 * @param status
	 * @param rsData
	 * @param l
	 * @param apitype
	 * @return
	 */
	private String getReturnStr(String msg, String userID, String ip, String resNo, JSONObject inputJson, String signature, String status, String rsData, Long l, String apitype){
		// 结果返回
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("signature", signature);
		jsonObject.put("status", status);
		jsonObject.put("data", rsData);
		jsonObject.put("msg", msg);
		System.out.println("执行时间:" +  (System.currentTimeMillis() - l ));
		BaseController.logBefore(logger, apitype+"执行时间:" +  (System.currentTimeMillis() - l ));
		//保存日志
		OperatorLog.setInterLog(apitype, userID, ip, resNo, inputJson, jsonObject.toString());
		return jsonObject.toString();
	}
	/**
	 * 签名  
	 * @param data
	 * @param privateKey
	 * @return
	 */
	private String RSASignature(String data , String privateKey )
	{
		String signature = "";
		try {
			RSASignaturer256 rsaSignaturer = new RSASignaturer256();
			rsaSignaturer.setKeyType("BASE64");
			rsaSignaturer.setPrivateKey(privateKey);//设置私钥
			rsaSignaturer.init();
			signature = (String) rsaSignaturer.sign(data);//签名
			
			if (signature == null) {
				return null;
			}
            
			//将签名数据转为base64格式
			byte[] buff = Hex.decodeHex((signature.toCharArray()));
			signature = new String(Base64.encodeBase64(buff), charSet);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return signature	;
	}
	
	/**
	 * 验签
	 * @param publicKey
	 * @param signature
	 * @param data
	 * @return
	 */
	private boolean RSAVerifier(String publicKey , String signature,String data){
	    String mysignature =  signature;
		//验证签名
		boolean isPass  = false;
		try {
			RSAVerifier256 rsaVerifier = new RSAVerifier256();
			rsaVerifier.setKeyType("BASE64");
			rsaVerifier.setPublicKey(publicKey);//设置公钥
			rsaVerifier.init();
		
			//将base64格式签名数据串转为bcd码
			byte[] buff = Base64.decodeBase64(mysignature);
			mysignature = Hex.encodeHexString(buff); //转BCD码 
		     //*/
			isPass = rsaVerifier.verify(data, mysignature);
		} 
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return isPass;
	}
	

}

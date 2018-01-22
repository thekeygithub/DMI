package com.pay.cloud.pay.escrow.ebao.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.cert.utils.HttpsNoCert;
import com.pay.cloud.constants.PropertiesConstant;
import com.pay.cloud.core.common.JsonStringUtils;
import com.pay.cloud.pay.common.http.model.HttpResponseBean;
import com.pay.cloud.pay.common.http.utils.HttpUtils;
import com.pay.cloud.pay.escrow.ebao.response.SocialCardInfo;
import com.pay.cloud.pay.escrow.ebao.service.EbaoService;
import com.pay.cloud.pay.escrow.ebao.utils.EbaoAes;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.XmlToMap;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.util.hint.Propertie;

@Service
public class EbaoServiceImpl implements EbaoService {

	private static final Logger logger = Logger.getLogger(EbaoServiceImpl.class);

	@Override
	public SocialCardInfo querySocialCard(String user_code) throws Exception {
		String url = String.valueOf(Propertie.APPLICATION.value(PropertiesConstant.EBAO123_SERVICE_URL)).trim();
		String aesKey = String.valueOf(Propertie.APPLICATION.value(PropertiesConstant.EBAO123_AESKEY)).trim();
		Map<String, String> map = new HashMap<>();
		map.put("phone_no", EbaoAes.setEncrypt(user_code, aesKey));
		if(logger.isInfoEnabled()){
			logger.info("url=" + url + ",map=" + map);
		}
		String response;
		if(url.startsWith("https")){
			response = HttpsNoCert.doPost(url, urlEncode(map));
		}else {
			HttpResponseBean bean = HttpUtils.httpPost(url, map, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
			response = bean.getEntityContent();
		}
		if(logger.isInfoEnabled()){
			logger.info("response=" + response);
		}
		response = EbaoAes.getDecrypt(response, aesKey);
		return JsonStringUtils.jsonToObjectIgnorePro(response, SocialCardInfo.class);
	}

	/**
	 * 卡数据查询
	 * @param fullName 姓名
	 * @param idNumber 身份证号
	 * @return 结果map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> cardDataQuery(String fullName, String idNumber) throws Exception {
		String url = String.valueOf(Propertie.APPLICATION.value("cardTubeDataQueryUrl")).trim();
		String aesKey = String.valueOf(Propertie.APPLICATION.value(PropertiesConstant.EBAO123_AESKEY)).trim();
		String response;
		if("TEST".equals(url)){//线下测试
			response = EbaoAes.setEncrypt("<ERR>OK</ERR><AAZ500>B123456789</AAZ500>", aesKey);
		}else {
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("name", fullName);
			dataMap.put("cardNo", idNumber);
			Map<String, String> param = new HashMap<>();
			param.put("param", EbaoAes.setEncrypt(JsonStringUtils.objectToJsonString(dataMap), aesKey));
			if(logger.isInfoEnabled()){
				logger.info("url=" + url + ",param=" + param);
			}
			if(url.startsWith("https")){
				response = HttpsNoCert.doPost(url, urlEncode(param));
			}else {
				HttpResponseBean httpResponseBean = HttpUtils.httpPost(url, param, HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
				response = httpResponseBean.getEntityContent();
			}
		}
		if(logger.isInfoEnabled()){
			logger.info("response=" + response);
		}
		Map<String, Object> returnMap = new HashMap<>();
		if(ValidateUtils.isEmpty(response)){
			returnMap.put("code", "1");
			returnMap.put("desc", "请求异常");
			return returnMap;
		}
		response = EbaoAes.getDecrypt(response, aesKey);
		if(ValidateUtils.isEmpty(response)){
			returnMap.put("code", "1");
			returnMap.put("desc", "解密失败");
			return returnMap;
		}
		response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA>".concat(response).concat("</DATA>");
		returnMap = XmlToMap.xml2mapWithAttr(response, false);
		if(null == returnMap || returnMap.size() == 0){
			returnMap = new HashMap<>();
			returnMap.put("code", "1");
			returnMap.put("desc", "转换XML数据为Map异常");
			return returnMap;
		}
		returnMap.put("code", Hint.SYS_SUCCESS.getCodeString());
		returnMap.put("desc", Hint.SYS_SUCCESS.getMessage());
		return returnMap;
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
		if(logger.isInfoEnabled()){
			logger.info("sb=" + sb.toString());
		}
		return sb.toString();
	}
}

package com.ebmi.std.common.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ebmi.std.common.finalkey.Constant;

/**
 * 
 * @Description: 基础服务类
 * @ClassName: BaseService 
 * @author: zhengping.hu
 * @date: 2015年11月23日 下午5:10:32
 */
@Service
public abstract class TPBaseService {
	
	@Resource(name = "restTemplate")
	private RestTemplate restTemplate;

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public <T> T execMiMethod(String url, Map<String, Object> paramMap, Class<T> typeClass) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
//		params.add(TICKET, ApplicationProperties.getPropertyValue(AppPropNameConst.MI_TECKET));
		if (paramMap != null) {
			for (Entry<String, Object> e : paramMap.entrySet()) {
				if (e.getValue() != null) {
					params.add(e.getKey(), e.getValue()+"");
				}
			}
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params,
				headers);
		return (T) getRestTemplate().postForObject(Constant.WEBSERVICE_URL, request, typeClass);
	}
	public <T> T execMiMethod( Map<String, Object> paramMap, Class<T> typeClass) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
//		params.add(TICKET, ApplicationProperties.getPropertyValue(AppPropNameConst.MI_TECKET));
		if (paramMap != null) {
			for (Entry<String, Object> e : paramMap.entrySet()) {
				if (e.getValue() != null) {
					params.add(e.getKey(), e.getValue()+"");
				}
			}
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params,
				headers);
		return (T) getRestTemplate().postForObject(Constant.WEBSERVICE_URL, request, typeClass);
	}
}
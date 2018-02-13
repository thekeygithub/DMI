package com.models.cloud.h5.controller.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.gw.protocolfactory.impl.SocialSecurityConfigInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
/***
 * 绑定社保卡选择城市
 * @author enjuan.ren
 *
 */
@Service("selectOfCityImpl")
public class SelectOfCityImpl implements DoPageService{
	private static final Logger logger = Logger.getLogger(SelectOfCityImpl.class);
	
	@Resource(name="socialSecurityConfigInterfaceImpl")
	SocialSecurityConfigInterfaceImpl socialSecurityConfigImpl;
	@Override
	public Map<String, Object> returnData(Map<String, Object> receiveMap, Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("社保绑卡字段配置");
		}
		String cityId=request.getParameter("cityId");
		Map<String, Object> returnMap=socialSecurityConfigImpl.doService(receiveMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+receiveMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
		}
		@SuppressWarnings("unchecked")
		List<String> list=(List<String>) returnMap.get("cityList");
		returnMap.put("cityList", list);
		if (cityId!=null) {
			returnMap.put("cityId", cityId);
		}
		return ConvertUtils.getMappingHintMessage("socialSecurityConfig", returnMap);
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		return null;
	}

}

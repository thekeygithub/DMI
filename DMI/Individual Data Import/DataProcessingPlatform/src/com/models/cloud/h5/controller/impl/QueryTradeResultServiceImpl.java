package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.QueryTradeInfoInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.SearchPayOrderInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;
import com.mysql.jdbc.StringUtils;

/**
 * 查询交易单，返回交易结果
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月22日 
 * @time 下午2:00:12
 * @version V1.0
 * @修改记录
 *
 */
@Service("queryTradeResultImplServiceImpl")
public class QueryTradeResultServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(QueryTradeResultServiceImpl.class);
	
	@Resource
	private SearchPayOrderInterfaceImpl searchPayOrderInterfaceImpl;
	@Resource
	private QueryTradeInfoInterfaceImpl queryTradeInfoInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("查询交易单 ");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		//查询医保支付结果信息
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("interfaceCode","queryTradeInfo");
		queryMap.put("accountId", String.valueOf((Long) sessionMap.get("accountId")));
		queryMap.put("payOrderId", (String) sessionOrderMap.get("payOrderId"));
		queryMap.put("hardwareId", String.valueOf(map.get("hardwareId")).trim());
		if(logger.isInfoEnabled()){
			logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，请求参数："+queryMap);
		}
		resultMap = queryTradeInfoInterfaceImpl.doService(queryMap);
		if(logger.isInfoEnabled()){
			logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		resultMap = ConvertUtils.getMappingHintMessage((String)queryMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		return resultMap;
	}

}

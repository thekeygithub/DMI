package com.pay.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.gw.protocolfactory.impl.SearchPayOrderInterfaceImpl;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.web.controller.DoWebPageService;

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
@Service("pcToResultSuccessPageImplServiceImpl")
public class ToResultSuccessPageServiceImpl implements DoWebPageService {
	private Logger logger = Logger.getLogger(UnBindCardServiceImpl.class);
	
	@Resource
	private SearchPayOrderInterfaceImpl searchPayOrderInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("跳转到结果页面 ");
		}
		String amount = request.getParameter("amount");
		String status = request.getParameter("status");
		String description = request.getParameter("description");
		
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("amount", amount);
		reqMap.put("status", status);
		reqMap.put("description", description);
		
		if(logger.isInfoEnabled()){
			logger.info("跳转到结果页面，传入参数："+reqMap);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("amount", amount);
		resultMap.put("status", status);
		resultMap.put("description", description);
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		return null;
	}

}

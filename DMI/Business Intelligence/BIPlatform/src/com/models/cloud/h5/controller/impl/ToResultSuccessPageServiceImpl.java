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
import com.models.cloud.gw.protocolfactory.impl.SearchPayOrderInterfaceImpl;
import com.models.cloud.h5.controller.DoPageService;
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
@Service("toResultSuccessPageImplServiceImpl")
public class ToResultSuccessPageServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(UnBindCardServiceImpl.class);
	
	@Resource
	private SearchPayOrderInterfaceImpl searchPayOrderInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("跳转到结果页面 ");
		}
		String amount = request.getParameter("amount");
		String medicarePersonPayMoney = request.getParameter("medicarePersonPayMoney");
		String insuranceFundPayMoney = request.getParameter("insuranceFundPayMoney");
		String personalPayMoney = request.getParameter("personalPayMoney");
		String status = request.getParameter("status");
		String busiStatus = request.getParameter("busiStatus");
		String siStatus = request.getParameter("siStatus");
		String payTypeId = request.getParameter("payTypeId");
		String description = request.getParameter("description");
		
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("amount", amount);
		resultMap.put("medicarePersonPayMoney", medicarePersonPayMoney);
		resultMap.put("insuranceFundPayMoney", insuranceFundPayMoney);
		resultMap.put("personalPayMoney", personalPayMoney);
		resultMap.put("status", status);
		resultMap.put("busiStatus", busiStatus);
		resultMap.put("siStatus", siStatus);
		resultMap.put("payTypeId", payTypeId);
		resultMap.put("description", description);
		
		if(logger.isInfoEnabled()){
			logger.info("跳转到结果页面，传入参数："+resultMap);
		}
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		return null;
	}

}

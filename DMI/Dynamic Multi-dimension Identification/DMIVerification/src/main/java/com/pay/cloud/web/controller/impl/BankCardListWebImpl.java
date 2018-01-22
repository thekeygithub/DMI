package com.pay.cloud.web.controller.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.gw.protocolfactory.impl.BankCardListImpl;
import com.pay.cloud.gw.service.payuser.PayUserServiceGW;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.web.controller.DoWebPageService;

/**
 * 
 * @Description: 支持银行卡列表
 * @ClassName: BankCardListWebImpl 
 * @author: zhengping.hu
 * @date: 2016年5月17日 下午4:50:06
 */
@Service("bankCardListWebImpl")
public class BankCardListWebImpl implements DoWebPageService{
	
	private static final Logger logger = Logger.getLogger(BankCardListImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		try {
			Map<String,Object> returnMap =  payUserServiceGWImpl.queryBankCardList(null);
			return ConvertUtils.getMappingHintMessage("servicebank", returnMap);
		} catch (Exception e) {
			logger.error("获取支持银行卡列表错误：" + e.getMessage(), e);
			Map<String,Object> returnMap =  ConvertUtils.genReturnMap(Hint.USER_25023_BANKCARDLIST_EXCEPTION);
            return ConvertUtils.getMappingHintMessage("servicebank", returnMap);
		}
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description: 支持银行卡列表
 * @ClassName: BankCardListImpl 
 * @author: zhengping.hu
 * @date: 2016年4月8日 下午3:55:03
 */
@Service("bankCardListImpl")
public class BankCardListImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(BankCardListImpl.class);
	
	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGW payUserServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
//		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
//			returnMap.put("resultCode", Hint.USER_25022_USERID_FAILED.getCodeString());
//			returnMap.put("resultDesc", Hint.USER_25022_USERID_FAILED.getMessage());
//			return returnMap;
//		}
		try {
			return payUserServiceGWImpl.queryBankCardList(receiveMap);
		} catch (Exception e) {
			logger.error("获取支持银行卡列表错误：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.USER_25023_BANKCARDLIST_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.USER_25023_BANKCARDLIST_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}

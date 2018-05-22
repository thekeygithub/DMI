package com.models.cloud.gw.protocolfactory.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.impl.RealNameServiceGWImpl;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 *  查询实名认证信息
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年8月10日 
 * @time 下午2:17:10
 * @version V1.0
 * @修改记录
 *
 */
@Service("queryRealNameInfoInterfaceImpl")
public class QueryRealNameInfoInterfaceImpl implements DoServiceInterface {
	
	private static final Logger logger = Logger.getLogger(QueryRealNameInfoInterfaceImpl.class);
	
	@Resource
	private RealNameServiceGWImpl realNameServiceGWImpl;
	
	@Override
	public Map<String, Object> doService(Map<String, Object> receiveMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		if(!ValidateUtils.isBlank(receiveMap.get("userId"))){			
			returnMap.put("resultCode", Hint.SYS_10017_ERROR_USER_ID.getCodeString());
			returnMap.put("resultDesc", Hint.SYS_10017_ERROR_USER_ID.getMessage());
			return returnMap;
		}
		try {
			return realNameServiceGWImpl.queryRealNameInfoGW(receiveMap);
		} catch (Exception e) {
			logger.error("查询实名认证接口：" + e.getMessage(), e);
			returnMap.put("resultCode", Hint.TD_13037_REAL_NAME_WITH_EXCEPTION.getCodeString());
            returnMap.put("resultDesc", Hint.TD_13037_REAL_NAME_WITH_EXCEPTION.getMessage());
			return returnMap;
		}
	}
}

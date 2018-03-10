package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.hint.Hint;
import com.mysql.jdbc.StringUtils;
/**
 * 输入银行卡号
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年5月19日 
 * @time 下午3:11:38
 * @version V1.0
 * @修改记录
 *
 */
@Service("inputCardNoPageServiceImpl")
public class InputCardNoPageServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(InputCardNoPageServiceImpl.class);
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("进入输入银行卡号页面");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		
		return resultMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}

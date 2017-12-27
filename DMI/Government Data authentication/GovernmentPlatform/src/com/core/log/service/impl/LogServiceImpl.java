/**   
 * . 
 * All rights reserved.
 */
package com.core.log.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.core.db.jdbc.JdbcService;
import com.core.log.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	private static final Logger logger = LoggerFactory.getLogger(LogService.class);

	@Resource
	private JdbcService jdbcService;

	@Override
	public void showLog(String url, String param, String result, boolean isSuccess, long timeCost, String ipAddr)
			throws Exception {
		if (logger.isInfoEnabled()) {
			StringBuilder log = new StringBuilder();
			log.append("【访问日志】接口[").append(url).append("]被访问，详情如下：").append("\n");
			log.append("------------------------------------------------").append("\n");
			log.append("\t结果：").append(isSuccess ? "成功！" : "失败！").append("\n");
			log.append("\t耗时：").append(timeCost).append("ms").append("\n");
			log.append("\t参数：").append(param).append("\n");
			log.append("\t返回：").append(result).append("\n");
			log.append("\t地址：").append(ipAddr).append("\n");
			log.append("------------------------------------------------").append("\n");
			logger.info(log.toString());
		}
	}
}

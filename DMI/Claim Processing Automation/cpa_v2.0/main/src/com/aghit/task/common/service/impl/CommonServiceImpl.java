package com.aghit.task.common.service.impl;

import javax.annotation.Resource;

import com.aghit.base.BaseService;
import com.aghit.scheduler.client.AutoConnectClient;
import com.aghit.task.common.service.CommonService;


/**
 * 系统的启动类，负责启动TCP监听
 * @author wenjie.wang
 *
 */
public class CommonServiceImpl extends BaseService implements CommonService {

	@Resource
	private AutoConnectClient client;
	
	@Override
	public void startCPA() {
		try {
			logger.info("准备连接到 调度。");
			client.start(true);
		} catch (Exception e) {
			logger.error("启动服务出错"+e.getMessage());
			e.printStackTrace();
		}
	}
}
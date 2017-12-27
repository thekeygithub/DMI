/**   
 * . 
 * All rights reserved.
 */
package com.core.log.service;

/**
 * 日志服务
 * 
 * @author: xiangfeng.guan
 * @date: 2015年12月10日 下午5:15:16
 */
public interface LogService {

	/**
	 * 显示日志
	 * 
	 * @return 日志ID
	 */
	void showLog(String url, String param, String result, boolean isSuccess, long timeCost, String ipAddr)
			throws Exception;
}

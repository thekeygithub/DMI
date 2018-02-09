package com.models.cloud.gw.service.payuser;

import java.util.Map;

/**
 *  实名认证
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang, haiyan.zhang@ebaonet.cn

 * @date 2016-4-19 
 * @time 上午10:29:48
 * @version V1.0
 * @修改记录
 *
 */
public interface RealNameServiceGW {
	/**
	 * 查询实名认证信息
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> queryRealNameInfoGW(Map<String, Object> receiveMap) throws Exception;
}

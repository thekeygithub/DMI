package com.models.cloud.gw.service.payuser;

import java.util.Map;

/**
 * 查询绑定的银行卡
 * @encoding UTF-8 
 * @author haiyan.zhang, haiyan.zhang@ebaonet.cn

 * @date 2016-4-19 
 * @time 下午02:32:34
 * @version V1.0
 * @修改记录
 *
 */
public interface CardServiceGW {
	/**
	 * 查询绑卡信息
	 * 
	 * @date 2016-4-19 
	 * @param @param inputMap
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	public Map<String, Object> queryCardList(Map<String, Object> inputMap) throws Exception;
	/**
	 * 解绑银行卡
	 * 
	 * @date 2016-4-19 
	 * @param @param inputMap
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	public Map<String, Object> unBindCard(Map<String, Object> inputMap) throws Exception;

}

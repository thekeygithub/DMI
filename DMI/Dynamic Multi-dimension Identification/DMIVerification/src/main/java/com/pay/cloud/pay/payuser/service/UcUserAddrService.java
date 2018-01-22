package com.pay.cloud.pay.payuser.service;

import java.util.Map;


public interface UcUserAddrService {
	/***
	 * 用户中心-保存地址
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> saveUcUserAddr(Map<String, Object> receiveMap);
	/***
	 * 通过用户id查询改用户的所有地址列表
	 * @param receiveMap
	 * @return
	 */
	Map<String, Object> ucUserAddrQuery(Map<String, Object> receiveMap);

}

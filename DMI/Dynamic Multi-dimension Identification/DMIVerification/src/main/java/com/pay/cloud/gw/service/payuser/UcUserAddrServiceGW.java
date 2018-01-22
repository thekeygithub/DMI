package com.pay.cloud.gw.service.payuser;

import java.util.Map;

/***
 * 用户中心-添加，修改地址
 * @author enjuan.ren
 *
 */
public interface UcUserAddrServiceGW {
	/***
	 * 添加地址
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveUcUserAddr(Map<String, Object> inputMap) throws Exception;
	/**
	 * 查询改用户下所有地址
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> ucUserAddrQuery(Map<String, Object> inputMap) throws Exception;

}

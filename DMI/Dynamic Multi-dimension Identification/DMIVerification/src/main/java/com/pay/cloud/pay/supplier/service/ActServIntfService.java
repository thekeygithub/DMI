package com.pay.cloud.pay.supplier.service;

import java.util.List;

public interface ActServIntfService {

	/**
	 * 
	 * @Description: 查询用户是否有接口权限
	 * @Title: hasPrivilege 
	 * @param actId
	 * @param interfaceCode
	 * @return boolean
	 */
	public boolean hasPrivilege(Long actId, String interfaceCode);
	
	/**
	 * 
	 * @Description: 查询用户可调用的接口列表
	 * @Title: getServIntfByActId 
	 * @param interfaceCode
	 * @return List<String>
	 */
	public List<String> selectActIdByIntfCode(String interfaceCode);
}

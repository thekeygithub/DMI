package com.models.cloud.gw.service.supplier;

import java.util.Map;

import com.models.cloud.pay.supplier.entity.ActSp;

public interface SupplierServiceGW {
	/**
	 * 
	 * @Description: 商户开户
	 * @Title: addSupplier 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> addSupplier(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 商户冻结
	 * @Title: freezeSpUser 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> freezeSupplier(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 合作商户查询
	 * @Title: subSupplierQuery 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> subSupplierQuery(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 綁定銀行卡
	 * @Title: bindBankCard 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> bindBankCard(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 解綁銀行卡
	 * @Title: operBankCard 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> operBankCard(Map<String,Object> receiveMap);
	
	/**
	 * 
	 * @Description: 查询账户信息
	 * @Title: subSupplierQuery 
	 * @param receiveMap
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	public ActSp findSpActByActId(Long actId) throws Exception;

	Map<String, Object> withdrawResultConfirm(Map<String, Object> params) throws Exception;
}

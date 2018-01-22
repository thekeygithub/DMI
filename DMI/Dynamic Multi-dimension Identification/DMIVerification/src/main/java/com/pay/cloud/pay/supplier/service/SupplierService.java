package com.pay.cloud.pay.supplier.service;

import java.util.List;
import java.util.Map;

import com.pay.cloud.pay.supplier.entity.ActSp;
import com.pay.cloud.pay.supplier.entity.ActSpBank;
import com.pay.cloud.pay.supplier.entity.ActSpExt;
/**
 * 
 * @Description: 商户服务
 * @ClassName: SpUserService 
 * @author: danni.liao
 * @date: 2016年4月7日 下午2:56:07
 */
public interface SupplierService {

	/**
	 * 
	 * @Description: 商户开户
	 * @Title: addSupplier 
	 * @param receiveMap
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	public Map<String,Object> addSupplier(Map<String,Object> receiveMap) throws Exception;
	
	/**
	 * 
	 * @Description: 商户冻结
	 * @Title: updateSupplierState 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> updateSupplierState(Map<String,Object> receiveMap) throws Exception ;
	
	/**
	 * 
	 * @Description: 合作商户查询
	 * @Title: subSupplierQuery 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> subSupplierQuery(Map<String,Object> receiveMap) throws Exception ;
	
	/**
	 * 
	 * @Description: 綁定銀行卡
	 * @Title: bindBankCard 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> bindBankCard(Map<String,Object> receiveMap) throws Exception ;
	
	/**
	 * 
	 * @Description: 解綁銀行卡
	 * @Title: unbindBankCard 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String,Object> operBankCard(Map<String,Object> receiveMap) throws Exception ;
	
	/**
	 * 
	 * @Description: 合作商户查询
	 * @Title: findActSpByChanActId 
	 * @param chanActId
	 * @return List<ActSp>
	 */
	public List<ActSp> findActSpByChanActId(Long chanActId) throws Exception;
	
	/**
	 * 
	 * @Description: 查询账户信息
	 * @Title: subSupplierQuery 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public ActSp findSpActByActId(Long actId)throws Exception;

	/**
	 * 
	 * @Description: 查询账户绑卡信息列表
	 * @Title: findByBankActId 
	 * @param bankActId
	 * @return ActSpBank
	 */
	public List<ActSpBank> getBankCardListByActId(Long actId) throws Exception;
	
	/**
	 * 
	 * @Description:根据账户查询绑卡信息
	 * @Title: findActSpBankByActId 
	 * @param actId
	 * @return ActSpBank
	 */
	public  ActSpBank findActSpBankByActId(Long actId);
	
	/**
	 * 
	 * @Description: 根据账户查询密钥信息
	 * @Title: findActSpExtByActId 
	 * @param actId
	 * @return ActSpExt
	 */
	public ActSpExt findActSpExtByActId(Long actId) throws Exception;
	
}

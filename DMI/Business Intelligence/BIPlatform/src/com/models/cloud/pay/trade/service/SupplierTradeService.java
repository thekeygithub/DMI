package com.models.cloud.pay.trade.service;

import java.util.Map;

import com.models.cloud.pay.trade.entity.TdOrder;
/**
 * 
 * @Description: TODO
 * @ClassName: SpUserService 
 * @author: danni.liao
 * @date: 2016年4月7日 下午2:56:07
 */
public interface SupplierTradeService {
	
	/**
	 * 保存提现交易
	 * @Description: 保存提现交易
	 * @Title: saveTdOrdWithdr 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String, Object> saveTdOrdWithdr(Map<String,Object> receiveMap) throws Exception;
	
	/**
	 * 
	 * @Description: 预扣款（扣除可以余额, 在途资金）
	 * @Title: updatePreWithdraw 
	 * @param receiveMap
	 * @return Map<String,Object>
	 */
	public Map<String, Object> updatePreWithdraw(Map<String,Object> receiveMap)throws Exception;

	/**
	 * 
	 * @Description: 扣款失败, 在途资金回滚
	 * @Title: updateWithdrawFail 
	 * @param receiveMap void
	 */
	public void updateWithdrawFail(Map<String,Object> receiveMap)throws Exception;
	
	/**
	 * 
	 * @Description:  资金到账,扣款
	 * @Title: updateWithdrawSuccess 
	 * @param receiveMap 
	 *     boolean
	 */
	public boolean updateWithdrawSuccess(Map<String,Object> receiveMap)throws Exception;

	/**
	 * 
	 * @Description: 更新交易失败
	 * @Title: updateTdOrdWithdrFail 
	 * @param receiveMap
	 * @return
	 */
	public void updateTdOrdWithdrFail(Map<String,Object> receiveMap)throws Exception;
	

	/**
	 * 
	 * @Description: 更新交易异常
	 * @Title: updateTdOrdWithdrException 
	 * @param receiveMap
	 * @return
	 */
	public void updateTdOrdWithdrException(Map<String,Object> receiveMap)throws Exception;
	
	
	/**
	 * 
	 * @Description: 查询提现交易
	 * @Title: searchTdOrdWithdr 
	 * @param receiveMap
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> searchTdOrdWithdr(Map<String,Object> receiveMap) throws Exception;
	
	/**
	 * 
	 * @Description: 根据交易单查在交易信息
	 * @Title: findTdOrderByTdId 
	 * @param tdId
	 * @return
	 * @throws Exception TdOrder
	 */
	public TdOrder findTdOrderByTdId(Long tdId)throws Exception;
	
	/**
	 * 
	 * @Description: 生成批次号  yeepay使用
	 * @Title: updateBatchNo 
	 * @return
	 * @throws Exception String
	 */
	public String updateBatchNo() throws Exception;
	 
}

package com.ts.service.pay;

import java.util.Date;
import java.util.List;

import com.ts.util.PageData;

/**
 * 对账数据升迁
 * @author autumn
 *
 */
public interface SettlementManager {
    
    /**
     * 
     * @param date
     * @throws Exception
     */
	void saveSettleTotalDetail(Date date) throws Exception;

	/**
	 * 
	 * @param date
	 * @throws Exception
	 */
	void saveSettleDetail(Date date) throws Exception;
	
	/**获取退费接口I31信息数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getI31DataByDate(PageData pd) throws Exception ;
	
	/**获取结算确认接口I49信息数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getI49DataByDate(PageData pd) throws Exception ;

	/**升迁数据
	 * @param date
	 * @throws Exception
	 */
	public void settlement(Date startDate, Date endDate, String groupId, String hospCode) throws Exception ;
	
	/**获取医院列表
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getHospList() throws Exception ;
	
	/**获取机构列表
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getGroupList() throws Exception ;
}
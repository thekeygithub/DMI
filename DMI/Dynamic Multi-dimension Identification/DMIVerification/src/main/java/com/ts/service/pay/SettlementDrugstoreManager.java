package com.ts.service.pay;

import java.util.Date;
import java.util.List;

import com.ts.util.PageData;

/**
 * 药店对账数据升迁
 * @ClassName:SettlementDrugstoreManager
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月27日上午9:37:14
 */
public interface SettlementDrugstoreManager {
    
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
	
	/**获取退费接口D04信息数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getD04DataByDate(PageData pd) throws Exception ;
	
	/**获取结算接口D02信息数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> getD02DataByDate(PageData pd) throws Exception ;

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
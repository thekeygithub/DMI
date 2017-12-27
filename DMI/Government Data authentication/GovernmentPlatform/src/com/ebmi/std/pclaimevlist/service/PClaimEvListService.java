package com.ebmi.std.pclaimevlist.service;

import java.util.List;

import com.ebmi.std.pclaimevlist.dto.PClaimEvList;
import com.ebmi.std.pclaimevlist.dto.PClaimModel;

public interface PClaimEvListService {
	/**
	 * 根据社保ID查询未评价列表
	 * 
	 * @param pMiId 社保ID
	 * @return 未评价数量
	 * @throws Exception
	 */
	List<PClaimEvList> queryPMiClaimEvList(String p_mi_id,  String ent_id, Integer stat_id, Integer days, Integer start, Integer count) throws Exception;

	List<PClaimModel> queryPMiClaimEvListNew(String p_mi_id, String userName, String startDate, String endDate, String clmId) throws Exception;
	/**
	 * 
	 * @Description:查询用户评价列表条数
	 * @Title: queryPMiClaimEvListCount 
	 * @param p_mi_id
	 * @param stat_id
	 * @return
	 * @throws Exception String
	 */
	String queryPMiClaimEvListCount(String p_mi_id, Integer stat_id) throws Exception;

	/**
	 * 
	 * @Description: 更新评价队列状态(多条)
	 * @Title: updatePMiClaimEvListState 
	 * @param list
	 * @throws Exception void
	 */
	void updatePMiClaimEvListState(List<PClaimEvList> list) throws Exception;
	
	/**
	 * 
	 * @Description: 更新评价队列状态(单条)
	 * @Title: updatePMiClaimEvListState 
	 * @param pClaimEvList
	 * @throws Exception void
	 */
	void updatePMiClaimEvListState(PClaimEvList pClaimEvList) throws Exception;

}

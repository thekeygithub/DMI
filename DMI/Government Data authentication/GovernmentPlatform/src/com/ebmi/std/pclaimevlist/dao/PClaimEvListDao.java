package com.ebmi.std.pclaimevlist.dao;

import java.util.Date;
import java.util.List;

import com.ebmi.std.pclaimevlist.dto.PClaimEvList;


public interface PClaimEvListDao {


	
	/**
	 * 根据社保ID查询未评价列表
	 * 
	 * @param pMiId 社保ID
	 * @return 未评价数量
	 * @throws Exception
	 */
	List<PClaimEvList> queryPMiClaimEvList(String p_mi_id,  String ent_id,Integer stat_id,  Date crt_time, Integer start, Integer count) throws Exception;

	/**
	 * 
	 * @Description: 查询用户评价列表条数
	 * @Title: queryPMiClaimEvListCount 
	 * @param p_mi_id
	 * @param stat_id
	 * @return
	 * @throws Exception String
	 */
	String queryPMiClaimEvListCount(String p_mi_id, Integer stat_id) throws Exception;
	
	/**
	 * 批量更新评价队列状态
	 * 
	 * @param pMiId 社保ID
	 * @param clmId 就诊ID
	 * @param evState 评价状态 1.待评价 2.已评价
	 * @return
	 */
	void updatePMiClaimEvListState(List<Object[]> list) throws Exception;
	
	/**
	 * 批量更新评价队列状态
	 * 
	 * @param pMiId 社保ID
	 * @param clmId 就诊ID
	 * @param evState 评价状态 1.待评价 2.已评价
	 * @return
	 */
	void updatePMiClaimEvListState(String p_mi_id,  String clm_id, Date date, Integer stat_id) throws Exception;

}

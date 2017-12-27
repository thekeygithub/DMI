package com.ebmi.std.clm.service;

import java.util.List;

import com.ebmi.std.clm.cond.ClmChrgSumCond;
import com.ebmi.std.clm.cond.ClmDiagStdCondition;
import com.ebmi.std.clm.cond.ClmInfoCondition;
import com.ebmi.std.clm.cond.ClmItemCondition;
import com.ebmi.std.clm.dto.PClaimChrgSum;
import com.ebmi.std.clm.dto.PClaimDiagStd;
import com.ebmi.std.clm.dto.PClaimHead;
import com.ebmi.std.clm.dto.PClaimItem;
/**
 * 结算单Service
 * @author yanjie.ji
 * @date 2015-12-22
 * @time 下午3:58:37
 *
 */
public interface ClmService {
	/**
	 * 查询个人就诊结算单
	 * @param condition
	 * @param jdbc_type
	 * @param need_trans 
	 * @return
	 * @throws Exception
	 */
	public List<PClaimHead> queryClmInfoList(ClmInfoCondition condition)throws Exception;
	/**
	 * 查询个人结算单项目明细
	 * @param condition
	 * @param jdbc_type
	 * @param need_trans 
	 * @return
	 * @throws Exception
	 */
	public List<PClaimItem> queryClmItemList(ClmItemCondition condition)throws Exception;
	/**
	 * 查询结算单标化结果
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	public List<PClaimDiagStd> queryClmDiagStd(ClmDiagStdCondition cond)throws Exception;
	
	/**
	 * 查询结算单汇总数据
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	public List<PClaimChrgSum> queryClmChrgSum(ClmChrgSumCond cond)throws Exception;

	public PClaimHead findPClaimHead(String clm_id, String p_mi_id) throws Exception;

	//public List<PClaimHead> queryClmInfoList(String[] clm_id, String p_mi_id)
		//	throws Exception;
}

package com.ebmi.std.clm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ebmi.std.clm.cond.ClmChrgSumCond;
import com.ebmi.std.clm.cond.ClmDiagStdCondition;
import com.ebmi.std.clm.cond.ClmInfoCondition;
import com.ebmi.std.clm.cond.ClmItemCondition;
import com.ebmi.std.clm.dao.ClmDao;
import com.ebmi.std.clm.dto.PClaimChrgSum;
import com.ebmi.std.clm.dto.PClaimDiagStd;
import com.ebmi.std.clm.dto.PClaimHead;
import com.ebmi.std.clm.dto.PClaimItem;
import com.ebmi.std.clm.service.ClmService;

@Service("clmServiceImpl")
public class ClmServiceImpl implements ClmService {

	@Resource(name="clmDaoImpl")
	private ClmDao clmDao;
	
	@Override
	public List<PClaimHead> queryClmInfoList(ClmInfoCondition condition)
			throws Exception {
		return clmDao.queryClmInfoList(condition);
	}

	@Override
	public List<PClaimItem> queryClmItemList(ClmItemCondition condition)
			throws Exception {
		return clmDao.queryClmItemList(condition);
	}
	
	@Override
	public List<PClaimDiagStd> queryClmDiagStd(ClmDiagStdCondition cond)
			throws Exception {
		return clmDao.queryClmDiagStd(cond);
	}
	
	@Override
	public List<PClaimChrgSum> queryClmChrgSum(ClmChrgSumCond cond)
			throws Exception {
		return clmDao.queryClmChrgSum(cond);
	}
	
	@Override
	public PClaimHead findPClaimHead(String clm_id, String p_mi_id)
			throws Exception {
		return clmDao.findPClaimHead(clm_id, p_mi_id);
	}

}

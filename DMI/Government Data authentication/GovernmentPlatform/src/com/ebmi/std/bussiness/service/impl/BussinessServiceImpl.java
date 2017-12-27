package com.ebmi.std.bussiness.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ebmi.std.bussiness.dao.BussinessDao;
import com.ebmi.std.bussiness.dto.Complaint;
import com.ebmi.std.bussiness.dto.SiCardInfo;
import com.ebmi.std.bussiness.dto.SiCardStateInfo;
import com.ebmi.std.bussiness.dto.SiCardStateQueryInfo;
import com.ebmi.std.bussiness.service.BussinessService;

@Service("bussinessService")
public class BussinessServiceImpl implements BussinessService {
	@Resource
	private BussinessDao bussinessDao;

	@Override
	public SiCardInfo getSiCardInfo(String siCardNo, String pMiId) throws Exception {
		return bussinessDao.getSiCardInfo(siCardNo, pMiId);
	}

	@Override
	public void updateInfo(SiCardInfo sicardInfo) throws Exception {
		// 更新本地信息
		bussinessDao.updateLocalInfo(sicardInfo);
		//调用第三方更新
		if(! bussinessDao.updateInfo(sicardInfo)){
			throw new Exception("第三方更新失败，本地回滚");
		}
		
	}

	@Override
	public boolean lostSiCard(String siCardNo, String pMiId) throws Exception {
		return bussinessDao.lostSiCard(siCardNo, pMiId);
	}

	@Override
	public Complaint saveComplaint(Complaint complaint) throws Exception {
		return bussinessDao.saveComplaint(complaint);
	}

	@Override
	public SiCardStateInfo getSiCardStateInfo(String siCardNo, String pMiId)
			throws Exception {
		return bussinessDao.getSiCardStateInfo(siCardNo, pMiId);
	}

	@Override
	public SiCardStateQueryInfo queryMakeCardState(String siCardNo)
			throws Exception {
		return bussinessDao.queryMakeCardState(siCardNo);
	}

	@Override
	public Complaint getComplaint(String recordId) throws Exception {
		return bussinessDao.getComplaint(recordId);
	}
}

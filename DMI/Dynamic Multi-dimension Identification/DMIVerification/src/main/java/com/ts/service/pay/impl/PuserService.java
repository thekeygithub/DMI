package com.ts.service.pay.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_user;
import com.ts.service.pay.PuserManager;
import com.ts.util.PageData;

@Service("puserService")
public class PuserService implements PuserManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	

	@Override
	public void save(P_user  pd) throws Exception {
	 dao.save("PuserMapper.save", pd);
	}
	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PuserMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PuserMapper.findById", pd);
	}

	@Override
	public Integer findUserID() throws Exception {
		return (Integer)dao.findForObject("PuserMapper.findUserID", null);
	}
	
	@Override
	public PageData searchUserId(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PuserMapper.searchUserId", pd);
	}
	
}
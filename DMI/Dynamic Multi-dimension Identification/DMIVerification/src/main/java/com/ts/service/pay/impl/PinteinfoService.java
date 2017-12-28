package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_inte_info;
import com.ts.entity.Page;
import com.ts.service.pay.PinteinfoManager;
import com.ts.util.PageData;

@Service("pinteinfoService")
public class PinteinfoService implements PinteinfoManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(P_inte_info pd) throws Exception {
		dao.save("PinteinfoMapper.save", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PinteinfoMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PinteinfoMapper.findById", pd);
	}

	@Override
	public Integer findUserID() throws Exception {
		return (Integer)dao.findForObject("PinteinfoMapper.findUserID", null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryInterList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PinteinfoMapper.interlistPage", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryInterListAll(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("PinteinfoMapper.interPageAll", pd);
	}

}
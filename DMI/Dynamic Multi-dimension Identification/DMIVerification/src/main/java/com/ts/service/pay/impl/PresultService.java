package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_result;
import com.ts.service.pay.PresultManager;
import com.ts.util.PageData;

@Service("presultService")
public class PresultService implements PresultManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(P_result pd) throws Exception {
		dao.save("PresultMapper.save", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PresultMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PresultMapper.findById", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryResultList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PresultMapper.searchResultAll", pd);
	}
	
}
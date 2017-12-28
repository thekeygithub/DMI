package com.ts.service.pay.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.service.pay.PdicttypeManager;
import com.ts.util.PageData;

@Service("pdicttyeService")
public class PdicttypeService implements PdicttypeManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(PageData pd) throws Exception {
		dao.save("PdicttypeMapper.save", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PdicttypeMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PdicttypeMapper.findById", pd);
	}
	
}
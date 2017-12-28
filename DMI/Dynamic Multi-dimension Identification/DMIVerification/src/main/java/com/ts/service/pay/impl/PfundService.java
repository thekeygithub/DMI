package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_fund;
import com.ts.entity.Page;
import com.ts.service.pay.PfundManager;
import com.ts.util.PageData;

@Service("pfundService")
public class PfundService implements PfundManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(P_fund pd) throws Exception {
		dao.save("PfundMapper.save", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PfundMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PfundMapper.findById", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryFundList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PfundMapper.fundlistPage", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> searchFundListAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PfundMapper.fundlistAll", pd);
	}

}
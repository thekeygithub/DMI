package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_fee;
import com.ts.entity.Page;
import com.ts.service.pay.PfeeManager;
import com.ts.util.PageData;

@Service("pfeeService")
public class PfeeService implements PfeeManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(P_fee pd) throws Exception {
		dao.save("PfeeMapper.save", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PfeeMapper.edit", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PfeeMapper.findById", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryFeeList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PfeeMapper.feelistPage", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> searchFeeListAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PfeeMapper.feelistAll", pd);
	}
	
}
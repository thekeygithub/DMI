package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.service.pay.PdictManager;
import com.ts.util.PageData;

@Service("pdictService")
public class PdictService implements PdictManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public void save(PageData pd) throws Exception {
		dao.save("PdictMapper.save", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PdictMapper.findById", pd);
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("PdictMapper.edit", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryDictList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PdictMapper.dictListAll", pd);
	}

}
package com.ts.service.pay.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.Page;
import com.ts.service.pay.PdrugstoreManager;
import com.ts.util.PageData;

@Service("pdrugstoreService")
public class PdrugstoreService implements PdrugstoreManager{

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	@Override
	public PageData findById_ds(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PdrugstoreinteinfoMapper.findById", pd);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryDrugstoreInterList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PdrugstoreinteinfoMapper.interlistPage", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryDrugstoreInterListAll(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("PdrugstoreinteinfoMapper.interPageAll", pd);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryDrugItemList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PdrugstoredrugitemMapper.druglistPage", page);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryDrugItemListAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PdrugstoredrugitemMapper.druglistAll", pd);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryResultList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("PdrugstoreresultMapper.searchResultAll", pd);
	}
}
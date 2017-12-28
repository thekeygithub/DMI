package com.ts.service.pay.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_return_detail;
import com.ts.entity.P_total_detail;
import com.ts.entity.Page;
import com.ts.entity.Reconciliation.S01InputTotalReconciliationBean;
import com.ts.entity.Reconciliation.S02InputDetailReconciliationBean;
import com.ts.entity.Reconciliation.S03InputRefundTotalBean;
import com.ts.entity.Reconciliation.S04InputRefundDetailBean;
import com.ts.service.pay.PrecordManager;
import com.ts.util.PageData;

@Service("precordService")
public class PrecordService implements PrecordManager {

	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;
	
	//============================================== jie kou =========================================================
	@SuppressWarnings("unchecked")
	@Override
	public List<P_total_detail> queryTotalRecord(S01InputTotalReconciliationBean model) {
		List<P_total_detail> totalList = new ArrayList<P_total_detail>();
		try{
			totalList = (List<P_total_detail>) dao.findForList("PtotaldetailMapper.totalQuery", model);
		}catch(Exception e){
			e.printStackTrace();
		}
		return totalList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P_total_detail> queryDetailRecord(S02InputDetailReconciliationBean model) {
		List<P_total_detail> detailList = new ArrayList<P_total_detail>();
		try{
			detailList = (List<P_total_detail>) dao.findForList("PtotaldetailMapper.detailQuery", model);
		}catch(Exception e){
			e.printStackTrace();
		}
		return detailList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P_return_detail> queryReTotalRecord(S03InputRefundTotalBean model) {
		List<P_return_detail> reTotalList = new ArrayList<P_return_detail>();
		try{
			reTotalList = (List<P_return_detail>) dao.findForList("PreturndetailMapper.reTotalQuery", model);
		}catch(Exception e){
			e.printStackTrace();
		}
		return reTotalList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P_return_detail> queryReDetailRecord(S04InputRefundDetailBean model) {
		List<P_return_detail> reDetailList = new ArrayList<P_return_detail>();
		try{
			reDetailList = (List<P_return_detail>) dao.findForList("PreturndetailMapper.reDetailQuery", model);
		}catch(Exception e){
			e.printStackTrace();
		}
		return reDetailList;
	}

	//===================================== ye mian =======================================================
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> searchTotalList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PtotaldetailMapper.totallistPage", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> searchTotalListAll(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("PtotaldetailMapper.totalPageAll", pd);
	}

	@Override
	public PageData findTotalById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PtotaldetailMapper.findById", pd);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> searchTotalDetail(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PtotaldetailMapper.totalDetaillistPage", page);
	}

	/***** 退费 *****/
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryReTotalList(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PreturndetailMapper.reTotallistPage", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryReTotalListAll(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("PreturndetailMapper.reTotalPageAll", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> queryReDetail(Page page) throws Exception {
		return (List<PageData>) dao.findForList("PreturndetailMapper.reDetaillistPage", page);
	}
	
	@Override
	public PageData findReturnById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("PreturndetailMapper.findById", pd);
	}

}
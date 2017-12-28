package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_return_detail;
import com.ts.entity.P_total_detail;
import com.ts.entity.Page;
import com.ts.entity.Reconciliation.S01InputTotalReconciliationBean;
import com.ts.entity.Reconciliation.S02InputDetailReconciliationBean;
import com.ts.entity.Reconciliation.S03InputRefundTotalBean;
import com.ts.entity.Reconciliation.S04InputRefundDetailBean;
import com.ts.util.PageData;

public interface PrecordManager {

	//================================== jie kou ============================================
	public List<P_total_detail> queryTotalRecord(S01InputTotalReconciliationBean model);

	public List<P_total_detail> queryDetailRecord(S02InputDetailReconciliationBean model);
	
	public List<P_return_detail> queryReTotalRecord(S03InputRefundTotalBean model);
	
	public List<P_return_detail> queryReDetailRecord(S04InputRefundDetailBean model);
	
	//================================= ye mian =============================================
	public List<PageData> searchTotalList(Page page) throws Exception;
	
	public List<PageData> searchTotalListAll(PageData pd) throws Exception;
	
	public List<PageData> searchTotalDetail(Page page) throws Exception;
	
	public List<PageData> queryReTotalList(Page page) throws Exception;
	
	public List<PageData> queryReTotalListAll(PageData pd) throws Exception;

	public List<PageData> queryReDetail(Page page) throws Exception;
	
	public PageData findTotalById(PageData pd) throws Exception;
	
	public PageData findReturnById(PageData pd) throws Exception;
}
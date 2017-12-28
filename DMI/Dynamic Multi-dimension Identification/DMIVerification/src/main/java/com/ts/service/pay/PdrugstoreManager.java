package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_inte_info;
import com.ts.entity.Page;
import com.ts.util.PageData;

public interface PdrugstoreManager {
	
	//根据ID获取接口的详细信息
	public PageData findById_ds(PageData pd) throws Exception;
	
	//获取接口的列表信息
	public List<PageData> queryDrugstoreInterList(Page page) throws Exception;
	
	//获取接口的全部信息
	public List<PageData> queryDrugstoreInterListAll(PageData pd) throws Exception;
	
	//获取药品的列表信息
	public List<PageData> queryDrugItemList(Page page) throws Exception;
	
	//获取药品的全部列表信息
	public List<PageData> queryDrugItemListAll(PageData pd) throws Exception;
	
	//获取计算的结果信息
	public List<PageData> queryResultList(PageData pd) throws Exception;
	
}
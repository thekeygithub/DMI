package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_fund;
import com.ts.entity.Page;
import com.ts.util.PageData;

public interface PfundManager {

	public void save(P_fund pd) throws Exception;
	
	public void edit(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
	
	public List<PageData> queryFundList(Page page) throws Exception;
	
	public List<PageData> searchFundListAll(PageData pd) throws Exception;
}
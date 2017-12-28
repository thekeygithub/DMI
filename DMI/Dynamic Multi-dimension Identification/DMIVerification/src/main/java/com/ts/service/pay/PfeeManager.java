package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_fee;
import com.ts.entity.Page;
import com.ts.util.PageData;

public interface PfeeManager {

	public void save(P_fee pd) throws Exception;
	
	public void edit(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
	
	public List<PageData> queryFeeList(Page page) throws Exception;
	
	public List<PageData> searchFeeListAll(PageData pd) throws Exception;
}
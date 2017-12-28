package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_result;
import com.ts.util.PageData;

public interface PresultManager {

	public void save(P_result pd) throws Exception;
	
	public void edit(PageData pd)throws Exception;
	
	public PageData findById(PageData pd)throws Exception;
	
	public List<PageData> queryResultList(PageData pd) throws Exception;
}
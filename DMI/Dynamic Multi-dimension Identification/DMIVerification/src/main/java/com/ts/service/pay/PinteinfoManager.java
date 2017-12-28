package com.ts.service.pay;

import java.util.List;

import com.ts.entity.P_inte_info;
import com.ts.entity.Page;
import com.ts.util.PageData;

public interface PinteinfoManager {
	
	public void save(P_inte_info pd) throws Exception;

	public void edit(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
	
	public Integer findUserID() throws Exception;

	public List<PageData> queryInterList(Page page) throws Exception;
	
	public List<PageData> queryInterListAll(PageData pd) throws Exception;
}
package com.ts.service.pay;

import com.ts.util.PageData;

public interface PdicttypeManager {

	public void save(PageData pd) throws Exception;
	
	public void edit(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
}
package com.ts.service.pay;

import java.util.List;

import com.ts.util.PageData;

public interface PdictManager {

	public void save(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
	
	public void edit(PageData pd) throws Exception;
	
	public List<PageData> queryDictList(PageData pd) throws Exception;
}
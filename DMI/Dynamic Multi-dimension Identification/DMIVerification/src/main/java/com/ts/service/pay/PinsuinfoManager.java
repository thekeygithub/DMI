package com.ts.service.pay;

import com.ts.entity.P_insu_info;
import com.ts.util.PageData;

public interface PinsuinfoManager {

	public void save(P_insu_info pd) throws Exception;
	
	public void edit(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
	
	public PageData findByUser(PageData pd) throws Exception;
}
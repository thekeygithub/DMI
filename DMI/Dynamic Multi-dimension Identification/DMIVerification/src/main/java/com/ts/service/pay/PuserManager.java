package com.ts.service.pay;

import com.ts.entity.P_user;
import com.ts.util.PageData;

public interface PuserManager {

	public void save(P_user pd) throws Exception;
	
	public void edit(PageData pd) throws Exception;
	
	public PageData findById(PageData pd) throws Exception;
	
	public PageData searchUserId(PageData pd) throws Exception;
	
	public Integer findUserID() throws Exception;
}
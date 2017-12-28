package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.OInterver;
import com.ts.entity.mts.OTerm;
import com.ts.util.PageData;

public interface DataOInterverManger {
	
	

		
	public List<OInterver> findAllOInver() throws Exception  ;
	
	public void addOInver(OInterver oInterver) throws Exception;
	
	public List<PageData> findOInverlistPage(Page page) throws Exception ;
	
	
}

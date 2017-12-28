package com.ts.service.mts.matchrule;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.OTerm;
import com.ts.util.PageData;

public interface DataOTermManger {
	
	
	public List<OTerm> findByFlag(String flag) throws Exception ;
		
	public List<OTerm> findAll() throws Exception  ;
	
	public void addOTerm(OTerm oTerm) throws Exception;
	
	public List<PageData> findOtermlistPage(Page page) throws Exception ;
	
	public List<String> findBySPType(String flag) throws Exception ;
}

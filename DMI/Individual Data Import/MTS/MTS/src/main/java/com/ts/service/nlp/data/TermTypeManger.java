package com.ts.service.nlp.data;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.NlpTerm;
import com.ts.entity.mts.OTerm;
import com.ts.entity.nlp.NlpData;
import com.ts.util.PageData;

public interface TermTypeManger {
	
		
	public List<String> findAllTerm() throws Exception ;
	public List<NlpTerm> findAllLoadTerm() throws Exception ;
	
	public NlpTerm findNlpTermByName(String name) throws Exception;
}

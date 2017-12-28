package com.ts.service.nlp.data;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.OTerm;
import com.ts.entity.nlp.NlpData;
import com.ts.util.PageData;

public interface NlpDataManger {
	
	
	
	public List<PageData> findNlplistPage(Page page) throws Exception ;
	
	public List<PageData> findByType() throws Exception ;
	
	public void addNLPData(NlpData ndata) throws Exception ;
	
	public void updNLPData(NlpData ndata) throws Exception ;
	
	public void deleteNLPData(NlpData ndata) throws Exception ;
	
	public void deleteAllU(int[] NLP_IDS)throws Exception ;
	
	public int findByName(String nlpName) throws Exception ;
}

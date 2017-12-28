package com.ts.service.nlp.data.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.NlpTerm;
import com.ts.entity.nlp.NlpData;
import com.ts.service.nlp.data.NlpDataManger;
import com.ts.service.nlp.data.TermTypeManger;
import com.ts.util.PageData;


@Service("TermTypeService")
public class TermTypeService implements TermTypeManger{
	
	
	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	
	@SuppressWarnings("unchecked")
	public List<String> findAllTerm() throws Exception {
		
		return (List<String>) dao.findForList("TermTypeMapper.findAllTerm", null);
	}


	
	@SuppressWarnings("unchecked")
	public List<NlpTerm> findAllLoadTerm() throws Exception {
		
		return (List<NlpTerm>) dao.findForList("TermTypeMapper.findAllLoadTerm", null);
	}
	
	
	@SuppressWarnings("unchecked")
	public NlpTerm findNlpTermByName(String name) throws Exception {
		return (NlpTerm) dao.findForObject("TermTypeMapper.findNlpTermByName", name);
	}
	
    
	

}

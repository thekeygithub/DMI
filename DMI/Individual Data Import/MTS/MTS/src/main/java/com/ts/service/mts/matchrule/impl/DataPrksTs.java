package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.service.mts.matchrule.DataPrksTsManger;


@Service("DataPrksTs")
public class DataPrksTs implements DataPrksTsManger{


	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	
	@SuppressWarnings("unchecked")
	public List<String> findJP() throws Exception {
		return (List<String>) dao.findForList("MtsPrksTs.findJP", null);
	}

	
	@SuppressWarnings("unchecked")
	public List<String> findOP() throws Exception {		
		return (List<String>) dao.findForList("MtsPrksTs.findOP", null);
	}

	
	@SuppressWarnings("unchecked")
	public List<String> findZLZY() throws Exception {
		return (List<String>) dao.findForList("MtsPrksTs.findZLZY", null);
	}
	
}

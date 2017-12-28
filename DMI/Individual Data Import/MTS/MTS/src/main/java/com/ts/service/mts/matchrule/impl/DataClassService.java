package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataClass;
import com.ts.service.mts.matchrule.DataClassManger;
import com.ts.util.PageData;

@Service("DataClassService")
public class DataClassService implements DataClassManger{
	
	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	 
	
	@SuppressWarnings("unchecked")
	public List<MtsDataClass> listAllDataClass() throws Exception {
		
		return (List<MtsDataClass>) dao.findForList("MtsDataClassMapper.listAllDataClass", null);
	}


	
	@SuppressWarnings("unchecked")
	public List<PageData> DataClasslistPage(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsDataClassMapper.dataClasslistPage", page);
	}


	
	public void addDataClass(MtsDataClass mdc) throws Exception {
		dao.save("MtsDataClassMapper.addDataClass", mdc);
		
	}


	
	public void editDataClass(MtsDataClass mdc) throws Exception {
		dao.update("MtsDataClassMapper.editDataClass", mdc);
		
	}


	
	public void deleteDataClass(String datatype) throws Exception {
		dao.delete("MtsDataClassMapper.deleteDataClass", datatype);
		
	}


	
	public String maxDataClass() throws Exception {
		
		return (String) dao.findForObject("MtsDataClassMapper.maxDataClass", null);
	}



	
	public MtsDataClass dataClassById(String clid) throws Exception {
		
		return  (MtsDataClass) dao.findForObject("MtsDataClassMapper.dataClassById", clid);
	}



	
	public String maxDataCode() throws Exception {
		
		return (String) dao.findForObject("MtsDataClassMapper.maxDataCode", null);
	}



	
	public MtsDataClass codeByCode(String code) throws Exception {
		
		return (MtsDataClass) dao.findForObject("MtsDataClassMapper.codeByCode", code);
	}



	
	public MtsDataClass nameByName(String name) throws Exception {
		
		return (MtsDataClass) dao.findForObject("MtsDataClassMapper.nameByName", name);
	}

}

package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.mts.MtsConfigTest;
import com.ts.service.mts.matchrule.MtsConfigTestService;
import com.ts.util.PageData;

@Service("DataMtsConfigTest")
public class DataMtsConfigTest implements MtsConfigTestService {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;



	@SuppressWarnings("unchecked")	
	public List<MtsConfigTest> findByPT(String pt) throws Exception {
		
		return (List<MtsConfigTest>) dao.findForList("MtsConfigTestMapper.findByPT",pt);
	}


	
	@SuppressWarnings("unchecked")
	public List<PageData> findPT() throws Exception {
		return (List<PageData>) dao.findForList("MtsConfigTestMapper.findPT",null);
	}


	
	public void addConfigTest(MtsConfigTest mct) throws Exception {
		dao.save("MtsConfigTestMapper.addConfigTest", mct);
		
	}


	
	public void editConfigTest(MtsConfigTest mct) throws Exception {
		dao.update("MtsConfigTestMapper.editConfigTest", mct);
		
	}


	
	public void deleteConfigTest(int[] PT_IDS) throws Exception {		
		
		dao.delete("MtsConfigTestMapper.deleteConfigTest",PT_IDS);
		
		
	}



}

package com.ts.service.mts.matchrule.impl;



import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.service.mts.matchrule.DataRelationManger;


@Service("DataRelationService")
public class DataRelationService implements DataRelationManger {

		@Resource(name = "daoSupportMts")
		private DaoSupportMts dao;
		


		public Integer relationCount(String claid) throws Exception {
			
			return (Integer) dao.findForObject("MtsDataRelationMapper.relationCount", claid);
		}

		
		public void addDataRelation(Page pg) throws Exception {
			dao.save("MtsDataRelationMapper.addDataRelation", pg);
			
		}

		
		public void deleteDataRelation(String datatype) throws Exception {
			dao.delete("MtsDataRelationMapper.deleteDataRelation", datatype);
			
		}

	
		public String maxDataRelation() throws Exception {
			
			return (String) dao.findForObject("MtsDataRelationMapper.maxDataRelation", null);
		}

	
		public String findRelationid(String datatype) throws Exception {
			
			return (String) dao.findForObject("MtsDataRelationMapper.relationbytype", datatype);
		}
		
		





}

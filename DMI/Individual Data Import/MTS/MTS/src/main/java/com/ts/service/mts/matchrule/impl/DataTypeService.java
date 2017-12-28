package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataType;
import com.ts.service.mts.matchrule.DataTypeManger;
import com.ts.util.PageData;

@Service("DataTypeService")
public class DataTypeService implements DataTypeManger {
	

		

		@Resource(name = "daoSupportMts")
		private DaoSupportMts dao;
		
		/**
		 * 根据标化代码，查询标化id
		 * @param bhlx
		 * @return
		 * @throws Exception
		 */
		public String findByCode(String code) throws Exception  {
			return (String) dao.findForObject("MtsDataTypeMapper.findByCode", code);
		
		}
		
		/**
		 * 获取所有标准化类型列表
		 * @param bhlx
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		public List<MtsDataType> listAllDataType() throws Exception  {
			return (List<MtsDataType>) dao.findForList("MtsDataTypeMapper.listAllDataType",null);
		
		}

		
		@SuppressWarnings("unchecked")
		public List<MtsDataType> listClassDataType(String classcode) throws Exception {
			
			return (List<MtsDataType>) dao.findForList("MtsDataTypeMapper.listClassDataType",classcode);
		}
		
		@SuppressWarnings("unchecked")
		public List<MtsDataType> listClassDataTypeById(String classid) throws Exception {
			
			return (List<MtsDataType>) dao.findForList("MtsDataTypeMapper.listClassDataTypeById",classid);
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<PageData> DataTypelistPage(Page page) throws Exception {
			
			return (List<PageData>) dao.findForList("MtsDataTypeMapper.DataTypelistPage", page);
		}

		@Override
		public void addDataType(MtsDataType mdt) throws Exception {

			dao.save("MtsDataTypeMapper.addDataType", mdt);
		}

		@Override
		public void editDataType(MtsDataType mdt) throws Exception {
			
			dao.update("MtsDataTypeMapper.editDataType", mdt);
		}

		@Override
		public void deleteDataType(String typeid) throws Exception {
			dao.delete("MtsDataTypeMapper.deleteDataType", typeid);
			
		}

		
		public String maxDataType() throws Exception {
			
			return (String) dao.findForObject("MtsDataTypeMapper.maxDataType", null);
		}

		
		public MtsDataType findById(String tyid) throws Exception {
			
			return (MtsDataType) dao.findForObject("MtsDataTypeMapper.findById", tyid);
		}

		
		public String maxDataCode() throws Exception {
			
			return (String) dao.findForObject("MtsDataTypeMapper.maxDataCode", null);
		}

		
		public MtsDataType codeByCode(String code) throws Exception {
			
			return (MtsDataType) dao.findForObject("MtsDataTypeMapper.codeByCode", code);
		}

		
		public MtsDataType nameByName(String name) throws Exception {
			
			return (MtsDataType) dao.findForObject("MtsDataTypeMapper.nameByName", name);
		}



}

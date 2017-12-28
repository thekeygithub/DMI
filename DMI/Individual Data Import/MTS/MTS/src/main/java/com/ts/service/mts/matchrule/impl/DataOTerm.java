package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.OTerm;
import com.ts.service.mts.matchrule.DataOTermManger;
import com.ts.util.PageData;

@Service("DataOTerm")
public class DataOTerm implements DataOTermManger {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	/**
	 * 查询无用术语列表
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OTerm> findByFlag(String flag) throws Exception  {
		return (List<OTerm>) dao.findForList("MtsOTermMapper.findByFlag", flag);
	
	}
	
	/**
	 * 获取全表数据 
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OTerm> findAll() throws Exception  {
		return (List<OTerm>) dao.findForList("MtsOTermMapper.findAll", null);
	
	}
	
	/**
	 * 添加无用术语 
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */

	public void addOTerm(OTerm oTerm) throws Exception {
		dao.save("MtsOTermMapper.addOTerm", oTerm);
		
	}

	/**
	 * 获取全表数据分页显示 
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findOtermlistPage(Page page) throws Exception {		
		return (List<PageData>) dao.findForList("MtsOTermMapper.findOtermlistPage", page);
	}

	/**
	 * 通过SP类型获取限定词列表
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> findBySPType(String SPType) throws Exception {		
		return (List<String>) dao.findForList("MtsOTermMapper.findBySPType", SPType);
	}

}

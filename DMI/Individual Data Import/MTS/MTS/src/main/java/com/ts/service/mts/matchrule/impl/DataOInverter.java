package com.ts.service.mts.matchrule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.OInterver;
import com.ts.service.mts.matchrule.DataOInterverManger;
import com.ts.util.PageData;

@Service("DataOInverter")
public class DataOInverter implements DataOInterverManger{


	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	/**
	 * 查询所有无法干预数据，用于加载缓存
	 */
	@SuppressWarnings("unchecked")
	public List<OInterver> findAllOInver() throws Exception {
		
		return (List<OInterver>) dao.findForList("MtsOInterver.findAllOInver", null);
	}

	/***
	 * 添加无法干预数据
	 */
	public void addOInver(OInterver oInterver) throws Exception {
		
		dao.save("MtsOInterver.addOInver", oInterver);
	}

	/**
	 * 分页查询无法干预数据，用于页面展示
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findOInverlistPage(Page page) throws Exception {
		
		return (List<PageData>) dao.findForList("MtsOInterver.findOInverlistPage", page);
	}

}

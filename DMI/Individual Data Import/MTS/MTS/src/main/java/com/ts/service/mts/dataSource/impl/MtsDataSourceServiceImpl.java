package com.ts.service.mts.dataSource.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataSource;
import com.ts.service.mts.dataSource.MtsDataSourceService;
import com.ts.util.PageData;

@Service("MtsDataSourceService")
public class MtsDataSourceServiceImpl implements MtsDataSourceService{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	@Override
	public void addMtsDataSource(MtsDataSource mtsDataSource) throws Exception {
		dao.save("MtsDataSourceMapper.addMtsDataSource", mtsDataSource);		
	}

	@Override
	public void editMtsDataSource(MtsDataSource mtsDataSource) throws Exception {
		dao.update("MtsDataSourceMapper.editMtsDataSource", mtsDataSource);
	}

	@Override
	public void deleteMtsDataSource(MtsDataSource mtsDataSource) throws Exception {
		dao.delete("MtsDataSourceMapper.deleteMtsDataSource", mtsDataSource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDataSource> findMtsDataSource(MtsDataSource mtsDataSource) throws Exception {
		return (List<MtsDataSource>) dao.findForList("MtsDataSourceMapper.findMtsDataSource", mtsDataSource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDataSourcelistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDataSourceMapper.mtsDataSourcelistPage", page);
		return list;
	}
}

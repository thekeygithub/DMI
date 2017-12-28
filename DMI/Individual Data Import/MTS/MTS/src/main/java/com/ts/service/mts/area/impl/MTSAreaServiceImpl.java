package com.ts.service.mts.area.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.service.mts.area.MTSAreaService;
import com.ts.util.PageData;

@Service("MTSAreaService")
public class MTSAreaServiceImpl implements MTSAreaService {

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	@Override
	public void addMtsArea(MtsArea mtsArea) throws Exception {
		dao.save("MtsAreaMapper.addMtsArea", mtsArea);
	}

	@Override
	public void editMtsArea(MtsArea mtsArea) throws Exception {
		dao.update("MtsAreaMapper.editMtsArea", mtsArea);
	}

	@Override
	public void deleteMtsArea(MtsArea mtsArea) throws Exception {
		dao.delete("MtsAreaMapper.deleteMtsArea", mtsArea);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsArea> findMtsArea(MtsArea mtsArea) throws Exception {
		return (List<MtsArea>) dao.findForList("MtsAreaMapper.findMtsArea", mtsArea);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsAreaListPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsAreaMapper.mtsArealistPage", page);
		return list;
	}
}

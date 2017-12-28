package com.ts.service.mts.visitType.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsVisitType;
import com.ts.service.mts.visitType.MtsVisitTypeService;
import com.ts.util.PageData;

@Service("MtsVisitTypeService")
public class MtsVisitTypeServiceImpl implements MtsVisitTypeService{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	@Override
	public void addMtsVisitType(MtsVisitType mtsVisitType) throws Exception {
		dao.save("MtsVisitTypeMapper.addMtsVisitType", mtsVisitType);		
	}

	@Override
	public void editMtsVisitType(MtsVisitType mtsVisitType) throws Exception {
		dao.update("MtsVisitTypeMapper.editMtsVisitType", mtsVisitType);
	}

	@Override
	public void deleteMtsVisitType(MtsVisitType mtsVisitType) throws Exception {
		dao.delete("MtsVisitTypeMapper.deleteMtsVisitType", mtsVisitType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsVisitType> findMtsVisitType(MtsVisitType mtsVisitType) throws Exception {
		return (List<MtsVisitType>) dao.findForList("MtsVisitTypeMapper.findMtsVisitType", mtsVisitType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsVisitTypelistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsVisitTypeMapper.MtsVisitTypelistPage", page);
		return list;
	}
}

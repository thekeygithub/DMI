package com.ts.service.mts.dbdata.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDBDataUnstructured;
import com.ts.entity.mts.MtsDBDataUnstructuredDetail;
import com.ts.entity.mts.MtsDBDataUnstructuredRelevance;
import com.ts.entity.mts.MtsDBDataUnstructuredResult;
import com.ts.service.mts.dbdata.MTSDBDataUnstructuredService;
import com.ts.util.PageData;

@Service("MTSDBDataUnstructuredService")
public class MTSDBDataUnstructuredServiceImpl implements MTSDBDataUnstructuredService{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	@Override
	public void addMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception {
		dao.save("MtsDBDataUnstructuredMapper.addMtsDBDataUnstructured", mtsDBDataUnstructured);
	}

	@Override
	public void editMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception {
		dao.update("MtsDBDataUnstructuredMapper.editMtsDBDataUnstructured", mtsDBDataUnstructured);
	}

	@Override
	public void deleteMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception {
		dao.delete("MtsDBDataUnstructuredMapper.deleteMtsDBDataUnstructured", mtsDBDataUnstructured);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBDataUnstructured> findMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured)
			throws Exception {
		return (List<MtsDBDataUnstructured>) dao.findForList("MtsDBDataUnstructuredMapper.findMtsDBDataUnstructured", mtsDBDataUnstructured);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDataUnstructuredlistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataUnstructuredMapper.mtsDBDataUnstructuredlistPage", page);
		return list;
	}

	@Override
	public void addMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail)
			throws Exception {
		dao.save("MtsDBDataUnstructuredDetailMapper.addMtsDBDataUnstructuredDetail", mtsDBDataUnstructuredDetail);
	}

	@Override
	public void editMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail)
			throws Exception {
		dao.update("MtsDBDataUnstructuredDetailMapper.editMtsDBDataUnstructuredDetail", mtsDBDataUnstructuredDetail);
	}

	@Override
	public void deleteMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail)
			throws Exception {
		dao.delete("MtsDBDataUnstructuredDetailMapper.deleteMtsDBDataUnstructuredDetail", mtsDBDataUnstructuredDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBDataUnstructuredDetail> findMtsDBDataUnstructuredDetail(
			MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail) throws Exception {
		return (List<MtsDBDataUnstructuredDetail>) dao.findForList("MtsDBDataUnstructuredDetailMapper.findMtsDBDataUnstructuredDetail", mtsDBDataUnstructuredDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDataUnstructuredDetaillistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataUnstructuredDetailMapper.mtsDBDataUnstructuredDetaillistPage", page);
		return list;
	}

	@Override
	public void addMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult)
			throws Exception {
		dao.save("MtsDBDataUnstructuredResultMapper.addMtsDBDataUnstructuredResult", mtsDBDataUnstructuredResult);
	}

	@Override
	public void editMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult)
			throws Exception {
		dao.update("MtsDBDataUnstructuredResultMapper.editMtsDBDataUnstructuredResult", mtsDBDataUnstructuredResult);
	}

	@Override
	public void deleteMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult)
			throws Exception {
		dao.delete("MtsDBDataUnstructuredResultMapper.deleteMtsDBDataUnstructuredResult", mtsDBDataUnstructuredResult);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBDataUnstructuredResult> findMtsDBDataUnstructuredResult(
			MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult) throws Exception {
		return (List<MtsDBDataUnstructuredResult>) dao.findForList("MtsDBDataUnstructuredResultMapper.findMtsDBDataUnstructuredResult", mtsDBDataUnstructuredResult);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDataUnstructuredResultlistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataUnstructuredResultMapper.mtsDBDataUnstructuredResultlistPage", page);
		return list;
	}

	@Override
	public void addMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance)
			throws Exception {
		dao.save("MtsDBDataUnstructuredRelevanceMapper.addMtsDBDataUnstructuredRelevance", mtsDBDataUnstructuredRelevance);
	}

	@Override
	public void editMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance)
			throws Exception {
		dao.update("MtsDBDataUnstructuredRelevanceMapper.editMtsDBDataUnstructuredRelevance", mtsDBDataUnstructuredRelevance);
	}

	@Override
	public void deleteMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance)
			throws Exception {
		dao.delete("MtsDBDataUnstructuredRelevanceMapper.deleteMtsDBDataUnstructuredRelevance", mtsDBDataUnstructuredRelevance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBDataUnstructuredRelevance> findMtsDBDataUnstructuredRelevance(
			MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance) throws Exception {
		return (List<MtsDBDataUnstructuredRelevance>) dao.findForList("MtsDBDataUnstructuredRelevanceMapper.findMtsDBDataUnstructuredRelevance", mtsDBDataUnstructuredRelevance);
	}

	@Override
	public List<PageData> mtsDBDataUnstructuredRelevancelistPage(Page page) throws Exception {
		@SuppressWarnings("unchecked")
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataUnstructuredRelevanceMapper.mtsDBDataUnstructuredRelevancelistPage", page);
		return list;
	}
}

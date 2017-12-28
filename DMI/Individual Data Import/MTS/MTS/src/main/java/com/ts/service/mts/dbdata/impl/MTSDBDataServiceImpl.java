package com.ts.service.mts.dbdata.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsDBData;
import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.entity.mts.MtsDBDataResult;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.util.PageData;

@Service("MTSDBDataService")
public class MTSDBDataServiceImpl implements MTSDBDataService{

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	@Override
	public void addMTSDBData(MtsDBData mtsDBData) throws Exception {
		dao.save("MtsDBDataMapper.addMtsDBData", mtsDBData);
	}

	@Override
	public void editMTSDBData(MtsDBData mtsDBData) throws Exception {
		dao.update("MtsDBDataMapper.editMtsDBData", mtsDBData);
	}

	@Override
	public void deleteMTSDBData(MtsDBData mtsDBData) throws Exception {
		dao.delete("MtsDBDataMapper.deleteMtsDBData", mtsDBData);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBData> findMTSDBData(MtsDBData mtsDBData) throws Exception {
		return (List<MtsDBData>) dao.findForList("MtsDBDataMapper.findMtsDBData", mtsDBData);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDatalistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataMapper.mtsDBDatalistPage", page);
		return list;
	}

	@Override
	public void addMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception {
		dao.save("MtsDBDataDetailMapper.addMtsDBDataDetail", mtsDBDataDetail);
	}

	@Override
	public void editMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception {
		dao.update("MtsDBDataDetailMapper.editMtsDBDataDetail", mtsDBDataDetail);
	}

	@Override
	public void deleteMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception {
		dao.delete("MtsDBDataDetailMapper.deleteMtsDBDataDetail", mtsDBDataDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBDataDetail> findMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception {
		return (List<MtsDBDataDetail>) dao.findForList("MtsDBDataDetailMapper.findMtsDBDataDetail", mtsDBDataDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDataDetaillistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataDetailMapper.mtsDBDataDetaillistPage", page);
		return list;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDataDetailslistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataDetailMapper.mtsDBDataDetailslistPage", page);
		return list;
	}*/
	
	@Override
	public void addMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception {
		dao.save("MtsDBDataResultMapper.addMtsDBDataResult", mtsDBDataResult);
	}

	@Override
	public void editMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception {
		dao.update("MtsDBDataResultMapper.editMtsDBDataResult", mtsDBDataResult);
	}

	@Override
	public void deleteMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception {
		dao.delete("MtsDBDataResultMapper.deleteMtsDBDataResult", mtsDBDataResult);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MtsDBDataResult> findMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception {
		return (List<MtsDBDataResult>) dao.findForList("MtsDBDataResultMapper.findMtsDBDataResult", mtsDBDataResult);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> mtsDBDataResultlistPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDBDataResultMapper.mtsDBDataResultlistPage", page);
		return list;
	}

	@Override
	public int findMtsDBDataDetailCount(MtsDBDataDetail mtsDBDataDetail) throws Exception {
		return (int) dao.findForObject("MtsDBDataDetailMapper.findMtsDBDataDetailCount", mtsDBDataDetail);
	}
}

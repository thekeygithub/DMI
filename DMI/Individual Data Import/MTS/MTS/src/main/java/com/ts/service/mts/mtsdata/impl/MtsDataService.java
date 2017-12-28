package com.ts.service.mts.mtsdata.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.LoadRule;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsData;
import com.ts.listener.RedisDataLoadListener;
import com.ts.service.mts.mtsdata.MtsDataManager;
import com.ts.util.PageData;

/**
 * 系统用户
 * 
 * @author 修改时间：2015.11.2
 */
@Service("mtsDataService")
public class MtsDataService implements MtsDataManager {
	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;

	/**
	 * 用户列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listMtsData(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDataMapper.mtsDatalistPage", page);
		return list;
	}

	/**
	 * @方法名称: listBatchNoByArea
	 * @功能描述: 根据区域查询批次号
	 * @作者:李巍
	 * @创建时间:2017年3月9日 下午2:25:39
	 * @param AREA_ID
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtsdata.MtsDataManager#listBatchNoByArea(java.lang.String)
	 */
	@Override
	public List<PageData> listBatchNoByArea(String AREA_ID) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsAreaBatchMapper.listBatchNoByArea", AREA_ID);
		return list;
	}

	@Override
	public Object saveMtsData(MtsData md) throws Exception {

		return dao.save("MtsDataMapper.saveMtsData", md);
	}

	@Override
	public Object saveAreaBatch(PageData pd) throws Exception {
		return dao.save("MtsAreaBatchMapper.saveAreaBatch", pd);
	}

	/**
	 * 保存用户
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd) throws Exception {

	}

	@Override
	public String maxData() throws Exception {
		return (String) dao.findForObject("MtsDataMapper.maxData", null);
	}

	@Override
	public String maxAreaBatchId() throws Exception {
		return (String) dao.findForObject("MtsAreaBatchMapper.maxAreaBatch", null);
	}

	@Override
	public String maxNowBatchNo() throws Exception {
		return (String) dao.findForObject("MtsDataMapper.maxNowBatchNo", null);
	}
	
	@Override
	public String maxBatchNo() throws Exception {
		return (String) dao.findForObject("MtsDataMapper.maxBatchNo", null);
	}

	/**
	 * 
	 * @方法名称: findById
	 * @功能描述:
	 * @作者:李巍
	 * @创建时间:2016年12月15日 下午1:33:27
	 * @param dataid
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtsdata.MtsDataManager#findById(java.lang.String)
	 */
	@Override
	public Object selectOneByName(String dataid) throws Exception {
		return dao.findForObject("MtsDataMapper.selectOneByName", dataid);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PageData> listAllBatch(PageData pd) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDataMapper.listAllBatch", pd);
		return list;
	}

	/**
	 * 获取所有批次号
	 * 
	 * @param bhlx
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PageData> listBatchWithNoDel(PageData pd) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDataMapper.listBatchWithNoDel", pd);
		return list;
	}
	
	@Override
	public List<PageData> listBatchWithNoLoad(PageData pd) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDataMapper.listBatchWithNoLoad", pd);
		return list;
	}
	
	
	/**
	 * 
	 * @方法名称: delBatchData
	 * @功能描述: 按批次号删除数据
	 * @作者:李巍
	 * @创建时间:2017年2月13日 下午2:04:07
	 * @param batchNo
	 * @return
	 * @throws Exception
	 * @see com.ts.service.mts.mtsdata.MtsDataManager#delBatchData(java.lang.String)
	 */
	@Override
	public Object delBatchData(String batchNo) throws Exception {
		dao.update("MtsDataMapper.delBatchData", batchNo);
		dao.update("MtsAreaBatchMapper.delBatchData", batchNo);
		return "success";
	}
	
	
	@Override
	public MtsData findMtsDataById(String dataid) throws Exception {
		return (MtsData) dao.findForObject("MtsDataMapper.findMtsDataById", dataid);
	}

	@Override
	public void editMtsData(MtsData md) throws Exception {
		dao.update("MtsDataMapper.editMtsData", md);
	}

	@Override
	public List<PageData> listTempMtsData() throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsDataMapper.listTempMtsData", null);
		return list;
	}

	


}

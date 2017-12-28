package com.ts.service.mts.mtsdata;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.LoadRule;
import com.ts.entity.mts.MtsData;
import com.ts.util.PageData;

/**
 * 用户接口类
 * 
 * @author 修改时间：2015.11.2
 */
public interface MtsDataManager {

	/**
	 * 加载规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listMtsData(Page page) throws Exception;
	
	
	public List<PageData> listBatchNoByArea(String areaid)throws Exception;

	/**
	 * 
	 * @方法名称: saveMtsData
	 * @功能描述: 保存MTS本体数据
	 * @作者:李巍
	 * @创建时间:2016年11月2日 下午1:22:18
	 * @param md
	 *            void
	 */
	public Object saveMtsData(MtsData md) throws Exception;

	/**
	 * 
	 * @方法名称: maxData
	 * @功能描述: 获取最大序列
	 * @作者:李巍
	 * @创建时间:2016年12月15日 下午1:07:46
	 * @return
	 * @throws Exception
	 *             String
	 */
	public String maxData() throws Exception;

	/**
	 * 
	 * @方法名称: maxBatchNo
	 * @功能描述: 最大的批次号
	 * @作者:李巍
	 * @创建时间:2017年2月7日 下午2:55:49
	 * @return
	 * @throws Exception
	 *             String
	 */
	public String maxNowBatchNo() throws Exception;

	/**
	 * 
	 * @方法名称: selectOneByName
	 * @功能描述: 根据同义词名 获取单条
	 * @作者:李巍
	 * @创建时间:2016年12月15日 下午1:44:23
	 * @param dataid
	 * @return
	 * @throws Exception
	 *             Object
	 */
	public Object selectOneByName(String dataid) throws Exception;

	/**
	 * 
	 * @param pd 
	 * @方法名称: listAllBatchNo
	 * @功能描述: 获取所有批次号
	 * @作者:李巍
	 * @创建时间:2017年2月9日 下午1:58:45
	 * @return
	 * @throws Exception
	 *             List<String>
	 */
	public List<PageData> listBatchWithNoDel(PageData pd) throws Exception;

	/**
	 * 
	 * @方法名称: delBatchData
	 * @功能描述: 按批次号删除数据
	 * @作者:李巍
	 * @创建时间:2017年2月13日 下午2:00:55
	 * @param string
	 * @return Object
	 */
	public Object delBatchData(String batchNo) throws Exception;


	/**
	 * 
	 * @方法名称: saveAreaBatch
	 * @功能描述: 保存区域批次号
	 * @作者:李巍
	 * @创建时间:2017年3月20日 上午10:44:13
	 * @param pd
	 * @return
	 * @throws Exception Object
	 */
	public Object saveAreaBatch(PageData pd) throws Exception;

	/**
	 * 
	 * @方法名称: maxAreaBatchId
	 * @功能描述: 最大区域批次号
	 * @作者:李巍
	 * @创建时间:2017年3月20日 上午10:52:11
	 * @return
	 * @throws Exception String
	 */
	public String maxAreaBatchId() throws Exception;


	public void editMtsData(MtsData md) throws Exception;


	public MtsData findMtsDataById(String dataid) throws Exception;


	public String maxBatchNo() throws Exception;


	public List<PageData> listBatchWithNoLoad(PageData pd) throws Exception;



	List<PageData> listAllBatch(PageData pd) throws Exception;


	public List<PageData> listTempMtsData() throws Exception;

}

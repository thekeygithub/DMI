package com.ts.service.mts.mtsdict;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDataType;
import com.ts.entity.mts.MtsDict;
import com.ts.util.PageData;

/**
 * 
 * @类名称: MtsDictManager
 * @类描述:
 * @作者:李巍
 * @创建时间:2016年11月10日 下午1:30:22
 */
public interface MtsDictManager {

	/**
	 * 加载规则列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listMtsDict(Page page) throws Exception;

	public List<MtsDataType> listAllMtsDataType(PageData pd) throws Exception;

	public void saveDict(PageData pd) throws Exception;

	public String maxRule() throws Exception;

	public PageData findDictById(PageData pd) throws Exception;

	public void editDict(PageData pd) throws Exception;

	public void deleteMtsDict(String did) throws Exception;
	
	public PageData selByName(String name) throws Exception;

}

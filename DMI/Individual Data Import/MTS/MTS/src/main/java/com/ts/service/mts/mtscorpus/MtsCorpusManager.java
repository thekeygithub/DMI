
package com.ts.service.mts.mtscorpus;


import java.io.File;
import java.util.List;

import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.util.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface MtsCorpusManager {

	/**
	 * 
	 * @方法名称: saveCorpusTag
	 * @功能描述: 保存标注实体
	 * @作者:李巍
	 * @创建时间:2017年1月16日 上午9:55:02
	 * @param ja
	 * @param myObj 
	 * @throws Exception void
	 */
	public void saveMtsCorpus(JSONObject myObj) throws Exception;

	public List<PageData> listMtsCorpus(PageData pd) throws Exception;

	public MtsCorpus findMtsCorpusById(String id) throws Exception;

	public String editMtsCorpus(JSONObject myObj, String id) throws Exception;

	public void importAllMtsCorpus(File dir) throws Exception;

	
}


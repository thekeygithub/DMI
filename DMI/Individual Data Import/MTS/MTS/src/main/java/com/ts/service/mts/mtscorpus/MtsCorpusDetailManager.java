
package com.ts.service.mts.mtscorpus;


import java.util.List;

import com.google.gson.JsonObject;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusDetail;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.util.PageData;

import net.sf.json.JSONObject;

public interface MtsCorpusDetailManager {

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
	public Integer saveMtsCorpusDetail(MtsCorpusDetail mtsCorpusDetail) throws Exception;
	
	public List<MtsCorpusDetail> listMtsCorpusDetail() throws Exception;
	
	public List<MtsCorpusDetail> listMtsCorpusDetailByCorpus(Integer mtsCorpusId) throws Exception;
	
	public int maxId() throws Exception ;
	
	public List<PageData> listMtsCorpusDetailView(Page page) throws Exception;
	
	public MtsCorpusDetail findMtsCorpusDetailById(String detailId) throws Exception;

	public String editAssertMtsCorpus(JSONObject myobj, String corpusDetailId) throws Exception;

	public Integer updateMtsCorpusDetail(MtsCorpusDetail mtsCorpusDetail) throws Exception;

	public List<PageData> listMtsCorpusEntityViewPage(Page page) throws Exception;

	public String saveAllMtsCorpus(JSONObject myObj) throws Exception;

	public List<PageData> listMtsCorpusView(Page page) throws Exception;

	public List<PageData> listMtsCorpusDetailViewForExcel(Page page) throws Exception;

	public List<PageData> listMtsCorpusEntityViewForExcel(Page page) throws Exception;

	public void importCorpusExcel(List<PageData> listPd, String filePath, String fileName) throws Exception;


}


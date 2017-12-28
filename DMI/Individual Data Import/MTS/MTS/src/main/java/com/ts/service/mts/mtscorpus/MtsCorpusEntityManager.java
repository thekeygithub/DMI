
package com.ts.service.mts.mtscorpus;


import java.util.List;

import com.ts.entity.mts.MtsCorpus;
import com.ts.entity.mts.MtsCorpusDetail;
import com.ts.entity.mts.MtsCorpusEntity;
import com.ts.util.PageData;

import net.sf.json.JSONObject;

public interface MtsCorpusEntityManager {

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
	public void saveMtsCorpusEntity(MtsCorpusEntity mtsCorpusEnttiy) throws Exception;
	
	public List<MtsCorpusEntity> listMtsCorpusEntity(Integer id) throws Exception ;

	public List<PageData> listMtsCorpusEntityByMtsCorpus(String mtsCorpustId) throws Exception;

	List<PageData> listMtsCorpusEntityByMtsCorpusDetail(String detailId) throws Exception;
	
}


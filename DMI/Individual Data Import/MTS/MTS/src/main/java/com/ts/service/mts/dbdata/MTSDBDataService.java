package com.ts.service.mts.dbdata;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDBData;
import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.entity.mts.MtsDBDataResult;
import com.ts.util.PageData;

public interface MTSDBDataService {

	public void addMTSDBData(MtsDBData mtsDBData) throws Exception;

	public void editMTSDBData(MtsDBData mtsDBData) throws Exception;

	public void deleteMTSDBData(MtsDBData mtsDBData) throws Exception;

	public List<MtsDBData> findMTSDBData(MtsDBData mtsDBData) throws Exception;
	
	public List<PageData> mtsDBDatalistPage(Page page)throws Exception;
	
	
	public void addMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception;

	public void editMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception;

	public void deleteMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception;

	public List<MtsDBDataDetail> findMTSDBDataDetail(MtsDBDataDetail mtsDBDataDetail) throws Exception;
	
	public List<PageData> mtsDBDataDetaillistPage(Page page)throws Exception;
	
	public int findMtsDBDataDetailCount(MtsDBDataDetail mtsDBDataDetail)throws Exception;
	
	public void addMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception;

	public void editMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception;

	public void deleteMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception;

	public List<MtsDBDataResult> findMtsDBDataResult(MtsDBDataResult mtsDBDataResult) throws Exception;
	
	public List<PageData> mtsDBDataResultlistPage(Page page) throws Exception;
	
	/*public List<PageData> mtsDBDataDetailslistPage(Page page) throws Exception;*/
}

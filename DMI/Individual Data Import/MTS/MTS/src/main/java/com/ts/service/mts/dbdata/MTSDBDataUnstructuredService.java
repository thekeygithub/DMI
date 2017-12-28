package com.ts.service.mts.dbdata;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsDBDataUnstructured;
import com.ts.entity.mts.MtsDBDataUnstructuredDetail;
import com.ts.entity.mts.MtsDBDataUnstructuredRelevance;
import com.ts.entity.mts.MtsDBDataUnstructuredResult;
import com.ts.util.PageData;

public interface MTSDBDataUnstructuredService {

	public void addMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception;

	public void editMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception;

	public void deleteMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception;

	public List<MtsDBDataUnstructured> findMtsDBDataUnstructured(MtsDBDataUnstructured mtsDBDataUnstructured) throws Exception;
	
	public List<PageData> mtsDBDataUnstructuredlistPage(Page page)throws Exception;
	
	
	public void addMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail) throws Exception;

	public void editMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail) throws Exception;

	public void deleteMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail) throws Exception;

	public List<MtsDBDataUnstructuredDetail> findMtsDBDataUnstructuredDetail(MtsDBDataUnstructuredDetail mtsDBDataUnstructuredDetail) throws Exception;
	
	public List<PageData> mtsDBDataUnstructuredDetaillistPage(Page page)throws Exception;
	
	
	public void addMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult) throws Exception;

	public void editMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult) throws Exception;

	public void deleteMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult) throws Exception;

	public List<MtsDBDataUnstructuredResult> findMtsDBDataUnstructuredResult(MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult) throws Exception;
	
	public List<PageData> mtsDBDataUnstructuredResultlistPage(Page page) throws Exception;
	
	
	public void addMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance) throws Exception;

	public void editMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance) throws Exception;

	public void deleteMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance) throws Exception;

	public List<MtsDBDataUnstructuredRelevance> findMtsDBDataUnstructuredRelevance(MtsDBDataUnstructuredRelevance mtsDBDataUnstructuredRelevance) throws Exception;
	
	public List<PageData> mtsDBDataUnstructuredRelevancelistPage(Page page) throws Exception;
	
}

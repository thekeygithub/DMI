package com.ts.service.mts.record;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsRecord;
import com.ts.entity.mts.MtsRecordDetail;
import com.ts.entity.mts.MtsRecordInfo;
import com.ts.entity.mts.MtsRecordParameters;
import com.ts.util.PageData;
import com.ts.vo.mts.RecordInfoVO;

public interface MTSRecordService {

	public void addMTSRecord(MtsRecord mtsRecord) throws Exception;

	public void editMTSRecord(MtsRecord mtsRecord) throws Exception;

	public void deleteMTSRecord(MtsRecord mtsRecord) throws Exception;

	public List<MtsRecord> findMTSRecord(MtsRecord mtsRecord) throws Exception;
	
	public List<PageData> mtsRecordlistPage(Page page) throws Exception;
	
	
	public void addMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception;

	public void editMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception;

	public void deleteMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception;

	public List<MtsRecordInfo> findMtsRecordInfo(MtsRecordInfo mtsRecordInfo) throws Exception;
	
	public int findMtsRecordInfoCount(MtsRecordInfo mtsRecordInfo) throws Exception;
	
	public List<PageData> mtsRecordInfolistPage(Page page) throws Exception;
	
	
	public void addMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception;

	public void editMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception;

	public void deleteMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception;

	public List<MtsRecordDetail> findMtsRecordDetail(MtsRecordDetail mtsRecordDetail) throws Exception;
	
	public List<PageData> mtsRecordDetaillistPage(Page page) throws Exception;
	
	
	public void addMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception;

	public void editMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception;

	public void deleteMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception;

	public List<MtsRecordParameters> findMtsRecordParameters(MtsRecordParameters mtsRecordParameters) throws Exception;
	
	public List<PageData> mtsRecordParameterslistPage(Page page) throws Exception;
	
	public List<MtsRecordInfo> findMtsRecordInfoByMark(RecordInfoVO recordInfoVO) throws Exception;
	
}

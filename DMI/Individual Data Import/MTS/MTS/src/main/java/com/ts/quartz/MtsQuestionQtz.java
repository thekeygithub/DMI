package com.ts.quartz;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ts.entity.mts.MtsDBData;
import com.ts.entity.mts.MtsDataSource;
import com.ts.entity.mts.MtsRecordDetail;
import com.ts.entity.mts.MtsRecordInfo;
import com.ts.entity.mts.MtsRecordParameters;
import com.ts.plugin.TsHttpClient;
import com.ts.service.mts.dataSource.MtsDataSourceService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.util.CommonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component("mtsQuestionQtz")
public class MtsQuestionQtz {

	@Resource(name = "MTSRecordService")
	private MTSRecordService mtsRecordService;

	@Resource(name = "MTSDBDataService")
	private MTSDBDataService mtsDBDataService;

	@Resource(name = "MtsDataSourceService")
	private MtsDataSourceService mtsDataSourceService;

	public void questionQtz() {
		
		System.out.println("questionQtz=====执行了===============>>>");
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*String oStr = "{\"O_TYPE\":\"1\",\"BATCH_NUMBER\":\"62730020170804123627694\",\"O_ID\":\"84082820170804123628305\",\"CLM_ID \":\"84082820170804123628305\","
				+ "\"oList\":[{\"NLP_RESULT\":\"咳嗽【症状】\",\"NLP_ORDER\":\"0\",\"XY_STANDARD_WORD\":\"咳嗽\",\"XY_STANDARD_MAIN_CODE\":\"R05.x00\",\"XY_STANDARD_ATTACH_CODE\":\"\","
				+ "\"ZY_STANDARD_WORD\":\"咳嗽病\",\"ZY_STANDARD_MAIN_CODE\":\"BNF010\",\"SS_STANDARD_WORD\":\"咳嗽病\",\"SS_STANDARD_MAIN_CODE\":\"BNF010\",\"TERMINOLOGY_TYPE\":\"1\",\"DEAL_STATUS\":\"4\"},{"
				+ "\"NLP_RESULT\":\"咳嗽1【症状】\",\"NLP_ORDER\":\"0\",\"XY_STANDARD_WORD\":\"咳嗽1\",\"XY_STANDARD_MAIN_CODE\":\"R05.x00\",\"XY_STANDARD_ATTACH_CODE\":\"\","
				+ "\"ZY_STANDARD_WORD\":\"咳嗽病1\",\"ZY_STANDARD_MAIN_CODE\":\"BNF010\",\"SS_STANDARD_WORD\":\"咳嗽病1\",\"SS_STANDARD_MAIN_CODE\":\"BNF010\",\"TERMINOLOGY_TYPE\":\"1\",\"DEAL_STATUS\":\"4\""
				+ "}]}";
		TsHttpClient.postMTSJson("http://10.10.40.140:8080/MTS/mtsRecord/syncResultFromAI.do", oStr);*/
		
		String AIUrl = CommonUtils.getPropValue("ai.properties", "ai.questionQtz.url");
		String AIType = CommonUtils.getPropValue("ai.properties", "ai.questionQtz.type");

		MtsRecordInfo mtsRecordInfo = new MtsRecordInfo();
		mtsRecordInfo.setSTANDARD_TYPE("0");
		mtsRecordInfo.setINFO_STATUS("1");

		List<MtsRecordInfo> recordInfoList = null;
		try {
			recordInfoList = mtsRecordService.findMtsRecordInfo(mtsRecordInfo);

			if (recordInfoList != null && recordInfoList.size() > 0) {
				for (int i = 0; i < recordInfoList.size(); i++) {
					mtsRecordInfo = recordInfoList.get(i);
					String DB_DATA_ID = mtsRecordInfo.getDB_DATA_ID();
					String RECORD_INFO_ID = mtsRecordInfo.getRECORD_INFO_ID();

					String BATCH_NUMBER = mtsRecordInfo.getBATCH_NUM();
					String O_TYPE = "1";
					String O_ID = mtsRecordInfo.getRECORD_INFO_ID();
					String ORIGIN_ID = "";
					String ORIGIN_VALUE = "";
					String CLM_ID = mtsRecordInfo.getDB_DATA_ID();

					if (null != DB_DATA_ID && !"".equals(DB_DATA_ID)) {
						MtsDBData mtsDBData = new MtsDBData();
						mtsDBData.setDB_DATA_ID(DB_DATA_ID);
						List<MtsDBData> DBDataList = mtsDBDataService.findMTSDBData(mtsDBData);
						if (null != DBDataList && !"".equals(DBDataList) && DBDataList.size() == 1) {
							MtsDBData DBData = DBDataList.get(0);
							ORIGIN_ID = DBData.getDATA_SOURCE_CODE();

							MtsDataSource mtsDataSource = new MtsDataSource();
							mtsDataSource.setFLAG(ORIGIN_ID);
							List<MtsDataSource> dataSourceList = mtsDataSourceService.findMtsDataSource(mtsDataSource);
							if (null != dataSourceList && !"".equals(dataSourceList) && dataSourceList.size() == 1) {
								mtsDataSource = dataSourceList.get(0);
								ORIGIN_VALUE = mtsDataSource.getDESCRIPTION();
							}
						}
					}

					String O_DIAG_NAME = "";
					MtsRecordParameters mtsRecordParameters = new MtsRecordParameters();
					mtsRecordParameters.setRECORD_INFO_ID(RECORD_INFO_ID);
					List<MtsRecordParameters> recordParaList = mtsRecordService
							.findMtsRecordParameters(mtsRecordParameters);
					if (recordParaList != null && recordParaList.size() > 0) {
						for (int j = 0; j < recordParaList.size(); j++) {
							MtsRecordParameters recordParameters = recordParaList.get(j);
							if (recordParameters != null) {
								if (null != O_DIAG_NAME && !"".equals(O_DIAG_NAME)) {
									O_DIAG_NAME = O_DIAG_NAME + " " + recordParameters.getPARAMETERS_CONTENT();
								} else {
									O_DIAG_NAME = recordParameters.getPARAMETERS_CONTENT();
								}
							}
						}
					}
					String O_DIAG_CODE = "";

					String NLP_DIAG_NAME = "";
					String MTS_DIAG_CODE = "";

					MtsRecordDetail mtsRecordDetail = new MtsRecordDetail();
					mtsRecordDetail.setRECORD_INFO_ID(RECORD_INFO_ID);
					List<MtsRecordDetail> recordDetailList = mtsRecordService.findMtsRecordDetail(mtsRecordDetail);
					if (recordDetailList != null && recordDetailList.size() > 0) {
						for (int k = 0; k < recordDetailList.size(); k++) {
							MtsRecordDetail recordDetail = recordDetailList.get(k);

							String NLP_RESULT = recordDetail.getNLP_RESULT();
							String XY_STANDARD_WORD = recordDetail.getXY_STANDARD_WORD();
							String XY_STANDARD_MAIN_CODE = recordDetail.getXY_STANDARD_MAIN_CODE();
							String XY_STANDARD_ATTACH_CODE = recordDetail.getXY_STANDARD_ATTACH_CODE();
							String ZY_STANDARD_WORD = recordDetail.getZY_STANDARD_WORD();
							String ZY_STANDARD_MAIN_CODE = recordDetail.getZY_STANDARD_MAIN_CODE();
							String SS_STANDARD_WORD = recordDetail.getSS_STANDARD_WORD();
							String SS_STANDARD_MAIN_CODE = recordDetail.getSS_STANDARD_MAIN_CODE();
							String all = XY_STANDARD_WORD + "@|&" + XY_STANDARD_MAIN_CODE + "@|&"
									+ XY_STANDARD_ATTACH_CODE + "@|&" + ZY_STANDARD_WORD + "@|&" + ZY_STANDARD_MAIN_CODE
									+ "@|&" + SS_STANDARD_WORD + "@|&" + SS_STANDARD_MAIN_CODE;
							if (recordDetail != null) {
								if ("".equals(NLP_DIAG_NAME)) {
									NLP_DIAG_NAME = NLP_RESULT;
								} else {
									NLP_DIAG_NAME = NLP_DIAG_NAME + "#q&q#" + NLP_RESULT;
								}

								if ("".equals(MTS_DIAG_CODE)) {
									MTS_DIAG_CODE = all;
								} else {
									MTS_DIAG_CODE = MTS_DIAG_CODE + "#q&q#" + all;
								}
							}
						}
					}
					JSONObject jsonAllStr = new JSONObject();
					jsonAllStr.put("O_TYPE", O_TYPE);
					jsonAllStr.put("BATCH_NUMBER", BATCH_NUMBER);

					JSONArray jsonArray = new JSONArray();
					JSONObject jsonStr = new JSONObject();
					jsonStr.put("O_ID", O_ID);
					jsonStr.put("ORIGIN_ID", ORIGIN_ID);
					jsonStr.put("ORIGIN_VALUE", ORIGIN_VALUE);
					jsonStr.put("CLM_ID", CLM_ID);
					jsonStr.put("O_DIAG_NAME", O_DIAG_NAME);
					jsonStr.put("O_DIAG_CODE", O_DIAG_CODE);
					jsonStr.put("NLP_DIAG_NAME", NLP_DIAG_NAME);
					jsonStr.put("MTS_DIAG_CODE", MTS_DIAG_CODE);
					jsonArray.add(jsonStr);
					jsonAllStr.element("oList", jsonArray);
					
					String postStr = jsonAllStr.toString();
					
					String result = "";
					
					if("http".equals(AIType)){
						result = TsHttpClient.postMTSJson(AIUrl, URLEncoder.encode(postStr));
					}else if("https".equals(AIType)){
						//result = TsHttpClient.postMTSJsonSSL(AIUrl, URLEncoder.encode(postStr));
					}
					
					if("1".equals(result)){
						mtsRecordInfo.setINFO_STATUS("2");
						mtsRecordService.editMtsRecordInfo(mtsRecordInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

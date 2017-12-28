package com.ts.threadPool;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.entity.mts.MtsDBDataResult;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.util.ApplicationUtil;
import com.ts.util.RandomUtil;

import net.sf.json.JSONObject;

public class ThreadPoolTaskForStandardizeData implements Callable<String>, Serializable {
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private String visitId;
	private String visitType;
	private String dataSource;
	private String dataType;
	private String parameters;
	private String batchNum;
	private String application;
	private String areaId;
	private String diagName;
	private MtsDBDataDetail mtsDBDataDetail;
	private CountDownLatch latch;  
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTaskForStandardizeData(CountDownLatch latch,MtsDBDataDetail mtsDBDataDetail,String visitId,String visitType,String dataSource,String dataType,String parameters,String batchNum,String application,String areaId,String diagName) {
		this.latch = latch;
		this.visitId = visitId;
		this.visitType = visitType;
		this.dataSource = dataSource;
		this.dataType = dataType;
		this.parameters = parameters;
		this.batchNum = batchNum;
		this.application = application;
		this.areaId = areaId;
		this.diagName = diagName;
		this.mtsDBDataDetail = mtsDBDataDetail;
	}

	public synchronized String call() throws Exception {
		latch.countDown();
		StandardizeService standardizeService = (StandardizeService)ApplicationUtil.getBean("StandardizeService");
		MTSDBDataService mtsDBDataService = (MTSDBDataService)ApplicationUtil.getBean("MTSDBDataService");
		String resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, parameters, batchNum,application,areaId);
		
		System.out.println("resultStr=====123======>>>"+resultStr);
		
		if(resultStr != null && !"".equals(resultStr)){
			JSONObject dealWithStrJSON = JSONObject.fromObject(resultStr);
			
			String status = "";
			String result = "";
			String nlpOrder = "";
			String doubtDiag = "";
			String inerverDiag = "";
			String[] nlpOrderList = null;
			String[] nlpResultList = null;
			String nlpResult = "";
			String STANDARD_RESULT = "";
			MtsDBDataResult mtsDBDataResult = null;
			String jsonarr = "";
			String NLPDrug = "";
			Map<String, String> inerverDiagMap = new HashMap<String, String>();
			
			if (dealWithStrJSON.containsKey("status")) {
				status = dealWithStrJSON.getString("status");
			}
			if (dealWithStrJSON.containsKey("result")) {
				result = dealWithStrJSON.getString("result");
			}
			if (dealWithStrJSON.containsKey("nlpOrder")) {
				nlpOrder = dealWithStrJSON.getString("nlpOrder");
			}
			if (dealWithStrJSON.containsKey("nlpResult")) {
				nlpResult = dealWithStrJSON.getString("nlpResult");
			}
			if (dealWithStrJSON.containsKey("doubtDiag")) {
				doubtDiag = dealWithStrJSON.getString("doubtDiag");
			}
			if (dealWithStrJSON.containsKey("inerverDiag")) {
				inerverDiag = dealWithStrJSON.getString("inerverDiag");
			}
			if (dealWithStrJSON.containsKey("jsonarr")) {
				jsonarr = dealWithStrJSON.getString("jsonarr");
				jsonarr = jsonarr.replace("{", "");
				jsonarr = jsonarr.replace("}", "");
			}
			if (dealWithStrJSON.containsKey("NLPDrug")) {
				NLPDrug = dealWithStrJSON.getString("NLPDrug");
				NLPDrug = NLPDrug.replace("{", "");
				NLPDrug = NLPDrug.replace("}", "");
			}
			if(nlpResult != null && !"".equals(nlpResult) && !"{}".equals(nlpResult)){
				nlpResult = nlpResult.replace("{", "");
				nlpResult = nlpResult.replace("}", "");
				nlpResultList = nlpResult.split("\",\"");
			}
			if(nlpOrder != null && !"".equals(nlpOrder)&& !"[]".equals(nlpOrder)){
				nlpOrder = nlpOrder.replace("[", "");
				nlpOrder = nlpOrder.replace("]", "");
				nlpOrderList = nlpOrder.split("\",\"");
			}
			if(inerverDiag != null && !"".equals(inerverDiag)){
				// 将json字符串转换成jsonObject
			    JSONObject jsonObject = JSONObject.fromObject(inerverDiag);
			    Iterator ite = jsonObject.keys();
			    // 遍历jsonObject数据,添加到Map对象
			    while (ite.hasNext()) {
			        String key = ite.next().toString();
			        String value = jsonObject.get(key).toString();
			        inerverDiagMap.put(key, value);
			    }
			}
			if(nlpResultList != null && nlpOrderList != null && nlpOrderList.length > 0 && nlpResultList.length > 0 && nlpOrderList.length == nlpResultList.length ){
				for(int k = 0;k < nlpOrderList.length;k ++){
					String nlpOrderStr = nlpOrderList[k];
					String nlpResultStr = nlpResultList[k];
					
					mtsDBDataResult = new MtsDBDataResult();
					String DB_DATA_RESULT_ID = RandomUtil.getRandomId();
					mtsDBDataResult.setDB_DATA_RESULT_ID(DB_DATA_RESULT_ID);
					mtsDBDataResult.setDB_DATA_DETAIL_ID(mtsDBDataDetail.getDB_DATA_DETAIL_ID());
					mtsDBDataResult.setDB_DATA_ID(mtsDBDataDetail.getDB_DATA_ID());
					mtsDBDataResult.setNLP_ORDER(k + "");
					nlpOrderStr = nlpOrderStr.replace("\"", "");
					nlpOrderStr = nlpOrderStr.replace("\"", "");
					
					mtsDBDataResult.setNLP_RESULT(nlpOrderStr);
					mtsDBDataResult.setJSONARR(jsonarr);
					
					if(nlpResultStr != null && !"".equals(nlpResultStr) && nlpResultStr.contains(":")){
						
						if(nlpResultStr.contains("none")){
							if(inerverDiagMap != null && inerverDiagMap.size() > 0){
								String nlpRes = nlpResultStr.substring(0,nlpResultStr.indexOf(":\""));
								nlpRes = nlpRes.replaceAll("\"", "");
								String doubtRes = inerverDiagMap.get(nlpRes);
								if("1".equals(doubtRes)){
									STANDARD_RESULT = "2";
								}else{
									STANDARD_RESULT = "0";
								}
							}else{
								STANDARD_RESULT = "0";
							}
						}else{
							STANDARD_RESULT = "1";
						}
						mtsDBDataResult.setSTANDARD_RESULT(STANDARD_RESULT);
						
						if(nlpResultStr.lastIndexOf("\"") == nlpResultStr.length() - 1){
							nlpResultStr = nlpResultStr.substring(nlpResultStr.indexOf(":\"") + ":\"".length(),nlpResultStr.length() - 1);
						}else{
							nlpResultStr = nlpResultStr.substring(nlpResultStr.indexOf(":\"") + ":\"".length(),nlpResultStr.length());
						}
						if(nlpResultStr.contains("@#&")){}
						String[] resLog = nlpResultStr.split("@#&");
						if(resLog != null && resLog.length > 0){
							if(resLog.length == 1){
								if(!resLog[0].equals("none")){
									mtsDBDataResult.setRESULT_1(resLog[0]);
								}
							}
							if(resLog.length == 2){
								if(!resLog[0].equals("none")){
									mtsDBDataResult.setRESULT_1(resLog[0]);
								}
								if(!resLog[1].equals("none")){
									mtsDBDataResult.setRESULT_2(resLog[1]);
								}
							}
							if(resLog.length == 3){
								if(!resLog[0].equals("none")){
									mtsDBDataResult.setRESULT_1(resLog[0]);
								}
								if(!resLog[1].equals("none")){
									mtsDBDataResult.setRESULT_2(resLog[1]);
								}
								if(!resLog[2].equals("none")){
									mtsDBDataResult.setRESULT_3(resLog[2]);
								}
							}
							if(resLog.length == 4){
								if(!resLog[0].equals("none")){
									mtsDBDataResult.setRESULT_1(resLog[0]);
								}
								if(!resLog[1].equals("none")){
									mtsDBDataResult.setRESULT_2(resLog[1]);
								}
								if(!resLog[2].equals("none")){
									mtsDBDataResult.setRESULT_3(resLog[2]);
								}
								if(!resLog[3].equals("none")){
									mtsDBDataResult.setRESULT_4(resLog[3]);
								}
							}
							if(resLog.length == 5){
								if(!resLog[0].equals("none")){
									mtsDBDataResult.setRESULT_1(resLog[0]);
								}
								if(!resLog[1].equals("none")){
									mtsDBDataResult.setRESULT_2(resLog[1]);
								}
								if(!resLog[2].equals("none")){
									mtsDBDataResult.setRESULT_3(resLog[2]);
								}
								if(!resLog[3].equals("none")){
									mtsDBDataResult.setRESULT_4(resLog[3]);
								}
								if(!resLog[4].equals("none")){
									mtsDBDataResult.setRESULT_5(resLog[4]);
								}
							}
							if(resLog.length == 6){
								if(!resLog[0].equals("none")){
									mtsDBDataResult.setRESULT_1(resLog[0]);
								}
								if(!resLog[1].equals("none")){
									mtsDBDataResult.setRESULT_2(resLog[1]);
								}
								if(!resLog[2].equals("none")){
									mtsDBDataResult.setRESULT_3(resLog[2]);
								}
								if(!resLog[3].equals("none")){
									mtsDBDataResult.setRESULT_4(resLog[3]);
								}
								if(!resLog[4].equals("none")){
									mtsDBDataResult.setRESULT_5(resLog[4]);
								}
								if(!resLog[5].equals("none")){
									mtsDBDataResult.setRESULT_6(resLog[5]);
								}
							}
						}
					}
					mtsDBDataService.addMtsDBDataResult(mtsDBDataResult);
				}
			}else{
				if("1".equals(status) && nlpResultList == null && nlpOrderList == null && !"".equals(result) && null != result){
					mtsDBDataResult = new MtsDBDataResult();
					String DB_DATA_RESULT_ID = RandomUtil.getRandomId();
					mtsDBDataResult.setDB_DATA_RESULT_ID(DB_DATA_RESULT_ID);
					mtsDBDataResult.setDB_DATA_DETAIL_ID(mtsDBDataDetail.getDB_DATA_DETAIL_ID());
					if("06".equals(dataType) || "07".equals(dataType)){
						mtsDBDataResult.setNLP_RESULT(NLPDrug);
					}else if("02".equals(dataType)){
						mtsDBDataResult.setNLP_RESULT(diagName);
					}
					mtsDBDataResult.setJSONARR(jsonarr);
					mtsDBDataResult.setNLP_ORDER("0");
					mtsDBDataResult.setDB_DATA_ID(mtsDBDataDetail.getDB_DATA_ID());
					
					String[] resLog = result.split("@#&");
					if(resLog != null && resLog.length > 0){
						if(resLog.length == 1){
							mtsDBDataResult.setRESULT_1(resLog[0]);
						}
						if(resLog.length == 2){
							mtsDBDataResult.setRESULT_1(resLog[0]);
							mtsDBDataResult.setRESULT_2(resLog[1]);
						}
						if(resLog.length == 3){
							mtsDBDataResult.setRESULT_1(resLog[0]);
							mtsDBDataResult.setRESULT_2(resLog[1]);
							mtsDBDataResult.setRESULT_3(resLog[2]);
						}
						if(resLog.length == 4){
							mtsDBDataResult.setRESULT_1(resLog[0]);
							mtsDBDataResult.setRESULT_2(resLog[1]);
							mtsDBDataResult.setRESULT_3(resLog[2]);
							mtsDBDataResult.setRESULT_4(resLog[3]);
						}
						if(resLog.length == 5){
							mtsDBDataResult.setRESULT_1(resLog[0]);
							mtsDBDataResult.setRESULT_2(resLog[1]);
							mtsDBDataResult.setRESULT_3(resLog[2]);
							mtsDBDataResult.setRESULT_4(resLog[3]);
							mtsDBDataResult.setRESULT_5(resLog[4]);
						}
						if(resLog.length == 6){
							mtsDBDataResult.setRESULT_1(resLog[0]);
							mtsDBDataResult.setRESULT_2(resLog[1]);
							mtsDBDataResult.setRESULT_3(resLog[2]);
							mtsDBDataResult.setRESULT_4(resLog[3]);
							mtsDBDataResult.setRESULT_5(resLog[4]);
							mtsDBDataResult.setRESULT_6(resLog[5]);
						}
						
						if(inerverDiagMap != null && inerverDiagMap.size() > 0){
							String doubtRes = inerverDiagMap.get(mtsDBDataResult.getRESULT_1());
							if("1".equals(doubtRes)){
								STANDARD_RESULT = "2";
							}else{
								STANDARD_RESULT = "1";
							}
						}else{
							STANDARD_RESULT = "1";
						}
						mtsDBDataResult.setSTANDARD_RESULT(STANDARD_RESULT);
					}
					mtsDBDataService.addMtsDBDataResult(mtsDBDataResult);
				}else if("0".equals(status)){
					STANDARD_RESULT = "0";
					mtsDBDataResult = new MtsDBDataResult();
					String DB_DATA_RESULT_ID = RandomUtil.getRandomId();
					mtsDBDataResult.setDB_DATA_RESULT_ID(DB_DATA_RESULT_ID);
					mtsDBDataResult.setDB_DATA_DETAIL_ID(mtsDBDataDetail.getDB_DATA_DETAIL_ID());
					mtsDBDataResult.setSTANDARD_RESULT(STANDARD_RESULT);
					if("06".equals(dataType) || "07".equals(dataType)){
						mtsDBDataResult.setNLP_RESULT(NLPDrug);
					}else if("02".equals(dataType)){
						mtsDBDataResult.setNLP_RESULT(diagName);
					}
					mtsDBDataResult.setJSONARR(jsonarr);
					mtsDBDataResult.setNLP_ORDER("0");
					mtsDBDataResult.setDB_DATA_ID(mtsDBDataDetail.getDB_DATA_ID());
					mtsDBDataService.addMtsDBDataResult(mtsDBDataResult);
				}
			}
			
			mtsDBDataDetail.setRESULT(result);
			mtsDBDataDetail.setSTATUS(status);
			mtsDBDataDetail.setIMPORT_TIME(new Date());
			mtsDBDataDetail.setOPERATE_TIME(new Date());
			mtsDBDataDetail.setBATCH_NUM(batchNum);
			mtsDBDataDetail.setMARK("1");
			mtsDBDataDetail.setNLP_RESULT(nlpOrder);
			mtsDBDataDetail.setDOUBT_DIAG(doubtDiag);
			mtsDBDataDetail.setDEAL_STATUS("1");
			mtsDBDataService.editMTSDBDataDetail(mtsDBDataDetail);
			mtsDBDataDetail = null;
		}
		return "true";
	}
}

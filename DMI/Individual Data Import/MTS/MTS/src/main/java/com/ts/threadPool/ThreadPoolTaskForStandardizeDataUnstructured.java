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
import com.ts.entity.mts.MtsDBDataUnstructured;
import com.ts.entity.mts.MtsDBDataUnstructuredResult;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.service.mts.dbdata.MTSDBDataUnstructuredService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.util.ApplicationUtil;
import com.ts.util.RandomUtil;

import net.sf.json.JSONObject;

public class ThreadPoolTaskForStandardizeDataUnstructured implements Callable<String>, Serializable {
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private String PROPERTY_CONTENT;
	private String AREA_CODE;
	private String DATA_UNSTRUCTURED_DETAIL_ID;
	private String DATA_UNSTRUCTURED_ID;
	private String PROPERTY_ID;
	private MtsDBDataUnstructured dataUnstructured;
	private CountDownLatch latch;  
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTaskForStandardizeDataUnstructured(CountDownLatch latch,MtsDBDataUnstructured dataUnstructured,String DATA_UNSTRUCTURED_DETAIL_ID,String DATA_UNSTRUCTURED_ID,String PROPERTY_ID,String PROPERTY_CONTENT,String AREA_CODE) {
		this.latch = latch;
		this.PROPERTY_CONTENT = PROPERTY_CONTENT;
		this.AREA_CODE = AREA_CODE;
		this.DATA_UNSTRUCTURED_DETAIL_ID = DATA_UNSTRUCTURED_DETAIL_ID;
		this.DATA_UNSTRUCTURED_ID = DATA_UNSTRUCTURED_ID;
		this.PROPERTY_ID = PROPERTY_ID;
		this.dataUnstructured = dataUnstructured;
	}

	public synchronized String call() throws Exception {
		latch.countDown();
		StandardizeService standardizeService = (StandardizeService)ApplicationUtil.getBean("StandardizeService");
		MTSDBDataService mtsDBDataService = (MTSDBDataService)ApplicationUtil.getBean("MTSDBDataService");
		MTSDBDataUnstructuredService mtsDBDataUnstructuredService = (MTSDBDataUnstructuredService) ApplicationUtil.getBean("MTSDBDataUnstructuredService");
		
		String resultStr = standardizeService.standardizeDataForZNWD(PROPERTY_CONTENT, AREA_CODE);
		System.out.println("resultStr====>>>"+resultStr);
		if(null != resultStr && !"".equals(resultStr)){
			JSONObject dealWithStrJSON = JSONObject.fromObject(resultStr);
			String result = "";
			if (dealWithStrJSON.containsKey("result")) {
				result = dealWithStrJSON.getString("result");
			}
			if(null != result && !"".equals(result)){
				JSONObject dealWithResultJSON = JSONObject.fromObject(result);
				String NlpResult = "";
				
				if (dealWithResultJSON.containsKey("NlpResult")) {
					NlpResult = dealWithResultJSON.getString("NlpResult");
				}
				if(null != NlpResult && !"".equals(NlpResult)){
					System.out.println("NlpResult===>>>"+NlpResult);
					JSONObject value = JSONObject.fromObject(NlpResult);
					int index = 0;
					for(Object map : value.keySet()){
						String NLP_RESULT = map.toString();
						String MTS_RESULT = (String) value.get(map);
						MtsDBDataUnstructuredResult mtsDBDataUnstructuredResult = new MtsDBDataUnstructuredResult();
						String DATA_UNSTRUCTURED_RESULT_ID = RandomUtil.getRandomId();
						mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_RESULT_ID(DATA_UNSTRUCTURED_RESULT_ID);
						mtsDBDataUnstructuredResult.setNLP_RESULT(NLP_RESULT);
						mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_DETAIL_ID(DATA_UNSTRUCTURED_DETAIL_ID);
						mtsDBDataUnstructuredResult.setDATA_UNSTRUCTURED_ID(DATA_UNSTRUCTURED_ID);
						mtsDBDataUnstructuredResult.setORDER_NUM(index + "");
						mtsDBDataUnstructuredResult.setPROPERTY_ID(PROPERTY_ID);
						String STANDARD_RESULT = "0";
						if(MTS_RESULT != null && !"".equals(MTS_RESULT)){
							if("none".equals(MTS_RESULT)){
								STANDARD_RESULT = "0";
							}else{
								STANDARD_RESULT = "1";
								String[] mtsResList = MTS_RESULT.split("@#&");
								if(mtsResList != null && mtsResList.length > 0){
									if(mtsResList.length == 1){
										mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
									}
									if(mtsResList.length == 2){
										mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
										mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
									}
									if(mtsResList.length == 3){
										mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
										mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
										mtsDBDataUnstructuredResult.setRESULT_3(mtsResList[2]);
									}
									if(mtsResList.length == 4){
										mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
										mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
										mtsDBDataUnstructuredResult.setRESULT_3(mtsResList[2]);
										mtsDBDataUnstructuredResult.setRESULT_4(mtsResList[3]);
									}
									if(mtsResList.length == 5){
										mtsDBDataUnstructuredResult.setRESULT_1(mtsResList[0]);
										mtsDBDataUnstructuredResult.setRESULT_2(mtsResList[1]);
										mtsDBDataUnstructuredResult.setRESULT_3(mtsResList[2]);
										mtsDBDataUnstructuredResult.setRESULT_4(mtsResList[3]);
										mtsDBDataUnstructuredResult.setRESULT_5(mtsResList[4]);
									}
								}
							}
							mtsDBDataUnstructuredResult.setSTANDARD_RESULT(STANDARD_RESULT);
							mtsDBDataUnstructuredService.addMtsDBDataUnstructuredResult(mtsDBDataUnstructuredResult);
							dataUnstructured.setSTATUS("1");
							mtsDBDataUnstructuredService.editMtsDBDataUnstructured(dataUnstructured);
						}
					}
				}
			}
		}
		return "true";
	}
}

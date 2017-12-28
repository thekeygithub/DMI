package com.ts.threadPool;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.util.ApplicationUtil;

public class ThreadPoolTaskForStandardizeDataNew implements Callable<String>, Serializable {
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
	private String RECORD_INFO_ID;
	private CountDownLatch latch;  
	private String DB_DATA_ID;
	
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTaskForStandardizeDataNew(CountDownLatch latch,String visitId,String visitType,String dataSource,String dataType,String parameters,String batchNum,String application,String areaId,String diagName,String RECORD_INFO_ID,String DB_DATA_ID) {
		this.latch = latch;
		this.visitId = visitId;
		this.visitType = visitType;
		this.dataSource = dataSource;
		this.dataType = dataType;
		this.parameters = parameters;
		this.batchNum = batchNum;
		this.application = application;
		this.areaId = areaId;
		this.RECORD_INFO_ID = RECORD_INFO_ID;
		this.DB_DATA_ID = DB_DATA_ID;
	}

	public synchronized String call() throws Exception {
		StandardizeService standardizeService = (StandardizeService)ApplicationUtil.getBean("StandardizeService");
		String result = standardizeService.standardizeDataNew(visitId, visitType, dataSource, dataType, parameters, batchNum,application,areaId,RECORD_INFO_ID,DB_DATA_ID);
		System.out.println("parametersresult=====123======>>>"+result);
		latch.countDown();
		return "true";
	}
}

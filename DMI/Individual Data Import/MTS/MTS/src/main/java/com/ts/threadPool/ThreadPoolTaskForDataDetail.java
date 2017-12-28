package com.ts.threadPool;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.util.ApplicationUtil;

public class ThreadPoolTaskForDataDetail implements Callable<String>, Serializable {
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private MtsDBDataDetail mtsDBDataDetail;
	private CountDownLatch latch;  
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTaskForDataDetail(CountDownLatch latch,MtsDBDataDetail mtsDBDataDetail) {
		this.mtsDBDataDetail = mtsDBDataDetail;
		this.latch = latch;
	}

	public synchronized String call() throws Exception {
		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
		/*if(mtsDBDataDetail != null){
			System.out.println("getDB_DATA_DETAIL_ID==========>>>"+mtsDBDataDetail.getDB_DATA_DETAIL_ID());
			System.out.println("getDIAG_NAM========E==========>>>"+mtsDBDataDetail.getDIAG_NAME());
		}*/
		latch.countDown();
		MTSDBDataService mtsDBDataService = (MTSDBDataService)ApplicationUtil.getBean("MTSDBDataService");
		mtsDBDataService.addMTSDBDataDetail(mtsDBDataDetail);
		mtsDBDataDetail = null;
		return "true";
	}
}

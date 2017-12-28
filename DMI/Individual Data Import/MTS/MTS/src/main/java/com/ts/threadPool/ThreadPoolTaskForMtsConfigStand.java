package com.ts.threadPool;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;


import com.ts.entity.mts.MtsConfigTest;
import com.ts.service.mts.MTSConfigPrks;
import com.ts.util.ApplicationUtil;

public class ThreadPoolTaskForMtsConfigStand implements Callable<String>, Serializable {
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private MtsConfigTest mtsConfigTest;
	private CountDownLatch latch;  	
	private Map<String, String> matchRuleCacheMap;
	private String AREA;
	private String classcode;
	private String memid;


	public ThreadPoolTaskForMtsConfigStand(CountDownLatch latch,String AREA,String classcode,MtsConfigTest mctdata,String memid,Map<String, String> matchRuleCacheMap) {
		this.mtsConfigTest = mctdata;
		this.latch = latch;
		this.AREA=AREA;
		this.classcode=classcode;
		this.memid=memid;
		this.matchRuleCacheMap=matchRuleCacheMap;
	}

	public synchronized String call() throws Exception {
		latch.countDown();
		MTSConfigPrks mtsConfigPrks = (MTSConfigPrks)ApplicationUtil.getBean("MTSConfigPrks");
		mtsConfigPrks.prksConfigInto(AREA, classcode, mtsConfigTest, memid, matchRuleCacheMap);	
		mtsConfigTest = null;
		return "true";
	}
}

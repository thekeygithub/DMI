package com.ts.threadPool;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import com.ts.entity.mts.MtsRecordInfo;
import com.ts.entity.mts.MtsRecordParameters;
import com.ts.service.mts.record.MTSRecordService;
import com.ts.util.ApplicationUtil;

public class ThreadPoolTaskForRecord implements Runnable, Serializable {
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private MtsRecordInfo mtsRecordInfo;
	private MtsRecordParameters mtsRecordParameters;
	private CountDownLatch latch;  
	
	public ThreadPoolTaskForRecord(CountDownLatch latch,MtsRecordInfo mtsRecordInfo,MtsRecordParameters mtsRecordParameters) {
		this.mtsRecordInfo = mtsRecordInfo;
		this.mtsRecordParameters = mtsRecordParameters;
		this.latch = latch;
	}

	/*public synchronized String call() throws Exception {
		MTSRecordService mtsRecordService = (MTSRecordService)ApplicationUtil.getBean("MTSRecordService");
		mtsRecordService.addMtsRecordInfo(mtsRecordInfo);
		mtsRecordService.addMtsRecordParameters(mtsRecordParameters);
		latch.countDown();
		return "true";
	}*/

	@Override
	public void run() {
		MTSRecordService mtsRecordService = (MTSRecordService)ApplicationUtil.getBean("MTSRecordService");
		try {
			mtsRecordService.addMtsRecordInfo(mtsRecordInfo);
			mtsRecordService.addMtsRecordParameters(mtsRecordParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		latch.countDown();
	}
}

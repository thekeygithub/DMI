package com.ts.threadPool;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import com.ts.entity.mts.MtsDBDataDetail;
import com.ts.entity.mts.MtsData;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.dbdata.MTSDBDataService;
import com.ts.service.mts.mtsdata.MtsDataManager;
import com.ts.service.mts.mtsdata.impl.MtsDataService;
import com.ts.util.ApplicationUtil;
import com.ts.util.PageData;

public class ThreadPoolTaskForMtsData implements Callable<String>, Serializable {
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private PageData pd;
	private CountDownLatch latch;  
	private String batchNo;
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTaskForMtsData(CountDownLatch latch,PageData pd,String batchNo) {
		this.latch = latch;
		this.pd = pd ;
		this.batchNo = batchNo;
	}

	public synchronized String call() throws Exception {
		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
		/*if(mtsDBDataDetail != null){
			System.out.println("getDB_DATA_DETAIL_ID==========>>>"+mtsDBDataDetail.getDB_DATA_DETAIL_ID());
			System.out.println("getDIAG_NAM========E==========>>>"+mtsDBDataDetail.getDIAG_NAME());
		}*/
		latch.countDown();
		
		MtsDataManager mtsDataService = (MtsDataManager)ApplicationUtil.getBean("mtsDataService");
		MtsData md = new MtsData(); 
		String ORIG_DATA_STR = pd.getString("ORIG_DATA_STR");
		String DATA_TYPE_ID = pd.getString("DATA_TYPE_ID");
		md.setDATA_TYPE_ID(DATA_TYPE_ID);
		String orig_data_id =  pd.getString("ORIG_DATA_ID");
		md.setORIG_DATA_ID(orig_data_id);
		String orig_data_name =  pd.getString("ORIG_DATA_NAME");
		md.setORIG_DATA_NAME(orig_data_name);
		md.setORIG_DATA_STR(ORIG_DATA_STR);
		// 诊断标识 1、科室 2、诊断本体 3、诊疗
		String zl_flag =  pd.getString("ZL_FLAG");
		md.setZL_FLAG(zl_flag);
		md.setAREA_ID("410201");
		// pd.put("AREA_ID", area);
		// 批次号
		md.setBATCH_NO(batchNo);
		// 删除标识 0 未删除 、 1 已删除
		// pd.put("DEL_FLAG", 0);
		md.setDEL_FLAG("0");
		// 加载标识 0、未加载 1 已加载
		// pd.put("LOAD_FLAG", 0);
		md.setLOAD_FLAG("0");
		// 导入时间
		md.setIMP_DATE(new Date());
		mtsDataService.saveMtsData(md);
		md = null;
		return "true";
	}
}

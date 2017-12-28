package com.ts.threadPool;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.vo.mts.StandardizeData;

public class ThreadPoolTask implements Callable<String>, Serializable {
	
	@Resource(name = "StandardizeService")
	private StandardizeService standardizeService;
	
	private static final long serialVersionUID = 0;
	// 保存任务所需要的数据
	private Object threadPoolTaskData;
	private static int consumeTaskSleepTime = 2000;
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;

	public ThreadPoolTask(Object tasks) {
		this.threadPoolTaskData = tasks;
	}

	public synchronized String call() throws Exception {
		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
		System.out.println("开始执行任务：" + threadPoolTaskData);
		StandardizeData data = (StandardizeData) threadPoolTaskData;
		
		String visitId = data.getVisitId();
		String visitType = data.getVisitType();
		String dataSource = data.getDataSource();
		String dataType = data.getDataType();
		String parameters = data.getParameters();
		String batchNum = data.getBatchNum();
		String application = data.getApplication();
		String areaId = data.getAreaId();
		
		String result = "";
		
		result = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, parameters, batchNum,application,areaId);
		
		Map<String, String> standardizeResMap = mcs.getStandardizeResMap();
		standardizeResMap.put(batchNum, result);
		
		// //便于观察，等待一段时间
		/*try {
			// long r = 5/0;
			for (int i = 0; i < 100000000; i++) {

			}
			result = "OK";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}*/
		threadPoolTaskData = null;
		return result;
	}
}

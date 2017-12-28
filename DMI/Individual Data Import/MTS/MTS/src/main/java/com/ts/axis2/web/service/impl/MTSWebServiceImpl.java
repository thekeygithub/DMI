package com.ts.axis2.web.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.ts.axis2.web.service.MTSWebService;
import com.ts.service.mts.MapCacheService;
import com.ts.service.mts.standardize.StandardizeService;
import com.ts.threadPool.StartTaskThread;
import com.ts.util.RandomUtil;
import com.ts.vo.mts.StandardizeData;

import net.sf.json.JSONObject;

@Service("MTSWebService")
public class MTSWebServiceImpl implements MTSWebService {

	@Resource(name = "StandardizeService")
	private StandardizeService standardizeService;

	@Override
	public String sayHello(String name) {
		return "hello" + name;
	}
	
	@Resource(name = "MapCacheService")
	private MapCacheService mcs;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	 
	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return threadPoolTaskExecutor;
	}


	public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	@Override
	public String standardizeData(String toBeStandardStr) {
		
		long startTime=System.currentTimeMillis();   //获取开始时间
		System.out.println("==============接口开始处理时间================"+startTime);
		
		String resultStr = "";
		String visitId = "";
		String visitType = "";
		String dataSource = "";
		String dataType = "";
		String parameters = "";
		String application = "";
		String areaId = "";
		
		if (toBeStandardStr == null || "".equals(toBeStandardStr)) {
			return null;
		}
		JSONObject dealWithStrJSON = JSONObject.fromObject(toBeStandardStr);
		if (dealWithStrJSON.containsKey("visitId")) {
			visitId = dealWithStrJSON.getString("visitId");
		}
		if (dealWithStrJSON.containsKey("visitType")) {
			visitType = dealWithStrJSON.getString("visitType");
		}
		if (dealWithStrJSON.containsKey("dataSource")) {
			dataSource = dealWithStrJSON.getString("dataSource");
		}
		if (dealWithStrJSON.containsKey("dataType")) {
			dataType = dealWithStrJSON.getString("dataType");
		}
		if (dealWithStrJSON.containsKey("parameters")) {
			parameters = dealWithStrJSON.getString("parameters");
		}
		if (dealWithStrJSON.containsKey("application")) {
			application = dealWithStrJSON.getString("application");
		}
		if (dealWithStrJSON.containsKey("areaId")) {
			areaId = dealWithStrJSON.getString("areaId");
		}
		String batchNum = RandomUtil.getRandomId();
		if(dealWithStrJSON != null){
			dealWithStrJSON = null;
		}
		StandardizeData data = new StandardizeData();
		data.setAreaId(areaId);
		data.setBatchNum(batchNum);
		data.setDataSource(dataSource);
		data.setDataType(dataType);
		data.setParameters(parameters);
		data.setVisitId(visitId);
		data.setVisitType(visitType);
		data.setApplication(application);
		
		try {
			resultStr = standardizeService.standardizeData(visitId, visitType, dataSource, dataType, parameters, batchNum,application,areaId);
			/*new Thread(new StartTaskThread(threadPoolTaskExecutor, batchNum ,data)).start();
			int waitTime = 0;
			boolean bool = true;
			while(bool){
	            try {
	            	Thread.sleep(1000);
	                //这里可以写你自己要运行的逻辑代码
	                System.out.println("一分钟运行一次");
	                
	                Map<String, String> standardizeResMap = mcs.getStandardizeResMap();
	                if(standardizeResMap.containsKey(batchNum)){
	                	resultStr = standardizeResMap.get(batchNum);
	                	standardizeResMap.remove(batchNum);
	                	bool = false;
	                }else{
	                	if(100000 < waitTime){
	                		if("".equals(resultStr)){
	                			JSONObject resultJSON = new JSONObject();
	                			resultJSON.put("status", "3");
	                			resultJSON.put("description", "链接超时");
	                			resultStr = resultJSON.toString();
	                		}
	 	                	bool = false;
	 	                }
	                }
	                waitTime += 1000;
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("==============接口处理完毕时间================"+endTime);
		System.out.println("==============接口处理总耗时： "+(endTime-startTime)+"ms");
		return resultStr;
	}
}

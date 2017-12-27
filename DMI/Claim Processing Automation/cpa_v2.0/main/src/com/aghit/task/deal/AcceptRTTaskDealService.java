package com.aghit.task.deal;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.aghit.scheduler.client.ServerHandler;
import com.aghit.scheduler.protocol.DealCodes;
import com.aghit.scheduler.protocol.Handler;
import com.aghit.scheduler.protocol.Protocol;
import com.aghit.scheduler.protocol.entities.CPARTTask;
import com.aghit.scheduler.protocol.impl.JsonDealService;


/**
 * 接受CPA任务处理类
 */
@Component
public class AcceptRTTaskDealService extends JsonDealService {
    
	//日志类
	private Logger log = Logger.getLogger(AcceptRTTaskDealService.class);
	
	@Resource(name="cpaTaskDeal")
	CPATaskDeal cpaTaskDeal;
	
	@Override
	public void dispose(Protocol p, Handler handler) throws Exception {
		CPARTTask cpaRTTask = decode(p.getBody(), CPARTTask.class);
		long taskId = cpaRTTask.getTaskId();
		log.info("接收到任务ID："+ taskId);
		cpaTaskDeal.deal(getDealCode().toString(), taskId);
	}

	@Override
	public Integer getDealCode() {
		return DealCodes.CPA_RT_TASK;
	}

}

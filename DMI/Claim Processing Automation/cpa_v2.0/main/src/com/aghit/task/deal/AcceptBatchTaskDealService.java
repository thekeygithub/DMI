package com.aghit.task.deal;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.aghit.scheduler.protocol.DealCodes;
import com.aghit.scheduler.protocol.Handler;
import com.aghit.scheduler.protocol.Protocol;
import com.aghit.scheduler.protocol.entities.CPATask;
import com.aghit.scheduler.protocol.impl.JsonDealService;

/**
 * 接受CPA批量任务
 *
 */
@Component
public class AcceptBatchTaskDealService extends JsonDealService {
	
	//日志类
	private Logger log = Logger.getLogger(AcceptBatchTaskDealService.class);
	
	@Resource(name="cpaTaskDeal")
	CPATaskDeal cpaTaskDeal;
	
	@Override
	public void dispose(Protocol p, Handler handler) throws Exception {
		CPATask cpaTask = decode(p.getBody(), CPATask.class);
		long taskId = cpaTask.getTaskId();
		log.info("接收到任务ID："+ taskId);
		cpaTaskDeal.deal(getDealCode().toString(), taskId);
	}

	@Override
	public Integer getDealCode() {
		return DealCodes.CPA_TASK;
	}
}

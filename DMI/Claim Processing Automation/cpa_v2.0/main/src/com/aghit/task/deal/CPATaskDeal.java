package com.aghit.task.deal;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.aghit.scheduler.client.ServerHandler;
import com.aghit.task.common.entity.CpaSubTask;
import com.aghit.task.deal.thread.CPATaskThread;
import com.aghit.task.manager.TaskMgr;

@Service("cpaTaskDeal")
public class CPATaskDeal {
	
	private Logger log = Logger.getLogger(CPATaskDeal.class);
	
	@Resource
	private ServerHandler shandler;
	
	@Resource(name="taskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 获得CPA任务处理
	 * @param dealId
	 * @param taskId
	 */
	public void deal(String dealId,long taskId){
		CPATaskThread thread=new CPATaskThread(dealId, taskId);
		thread.setServerHandler(shandler);
		this.initProject(taskId,thread);
		taskExecutor.execute(thread);
	}
	
	/**
	 * 初始化方案
	 * 
	 * @param taskNo
	 */
	private void initProject(long taskNo,CPATaskThread thread) {
		log.info("正在初始化方案,加载规则!");
		CpaSubTask task = null;
		try {
			task = TaskMgr.getInstance().getCpaTask(taskNo);
		} catch (Exception e1) {
			log.error("查询CPA任务失败," + e1.getMessage());
			return;
		}

		long logId = task.getLogId();
		try {
			thread.init(logId);
		} catch (Exception e) {
			log.error("初始化规则失败," + e.getMessage());
			return;
		}
	}
	
}

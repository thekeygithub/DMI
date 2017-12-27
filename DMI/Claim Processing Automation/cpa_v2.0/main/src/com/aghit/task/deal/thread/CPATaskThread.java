package com.aghit.task.deal.thread;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.aghit.scheduler.client.ServerHandler;
import com.aghit.scheduler.protocol.DealCodes;
import com.aghit.scheduler.protocol.entities.CPARTTaskDone;
import com.aghit.scheduler.protocol.entities.CPATaskDone;
import com.aghit.scheduler.protocol.entities.TaskDone;
import com.aghit.task.algorithm.AbstractRule;
import com.aghit.task.algorithm.domain.BreakDetail;
import com.aghit.task.algorithm.domain.Patient;
import com.aghit.task.common.entity.CpaSubTask;
import com.aghit.task.common.entity.CpaTask;
import com.aghit.task.manager.BreakDetailMgr;
import com.aghit.task.manager.CpaTaskMgr;
import com.aghit.task.manager.PatientMgr;
import com.aghit.task.manager.ProjectMgr;
import com.aghit.task.manager.TaskMgr;
import com.aghit.task.manager.rule.ProcedureRuleMgr;
import com.aghit.task.util.Constant;
import com.aghit.utils.ApplicationProperties;
import com.aghit.utils.SpringBeanUtil;

/**
 * CPA执行线程类
 * @author wenjie.wang
 *
 */
public class CPATaskThread implements Runnable {

	
	private ServerHandler shandler;
	
	private String dealId;
	private long taskNo;
	private static Logger log = Logger.getLogger(CPATaskThread.class);
	
	private CpaTask cpaTask = null;
	private List<AbstractRule> rules = null;
	
	public AtomicInteger count = new AtomicInteger();//已处理用户数
	public CpaSubTask task = null; //子任务类型
	
	public volatile boolean over = false;

	public CPATaskThread(String dealId, long taskNo) {
		this.dealId = dealId;
		this.taskNo = taskNo;
	}

	public void setServerHandler(ServerHandler shandler){
		this.shandler = shandler;
	}
	@Override
	public void run() {

		log.info("接收到交易:" + this.dealId + " ,msg=" + this.taskNo);
		if (!DealCodes.CPA_RT_TASK.toString().equals(this.dealId) && !DealCodes.CPA_TASK.toString().equals(this.dealId)) {
			log.error("交易暂未支持" + this.dealId);
			return;
		}

		Map<Long, Patient> patients = new LinkedHashMap<Long, Patient>();
		
		long start;
		long end;
		//默认为-1 返回调度时使用
		long logId=-1;
		
		try {
			task = TaskMgr.getInstance().getCpaTask(taskNo);
			// 组装患者数据
			start = new Long(task.getHandleStartNum()).intValue();
			end = new Long(task.getHandleEndNum()).intValue();
			logId = task.getLogId();
			
			for (long i = start; i <= end; i++) {
				Patient p =PatientMgr.getInstance().getPatient(i);
				patients.put(i, p);
			}
			// 初始化患者数据
			this.initPatients(patients, start, end, cpaTask);
		} catch (Exception e1) {
			String err = "初始化患者数据失败,taskNo:" + taskNo + ",startPatientId:"
					+ task.getHandleStartNum() + ",endPatientId:"
					+ task.getHandleEndNum() + ",错误描述:" +e1.getLocalizedMessage();
			e1.printStackTrace();
			this.over = true;
			this.sendMessage(logId,taskNo, false, err);
			return;
		}
	
		BreakDetailMgr mgr = new BreakDetailMgr();
		AbstractRule rule = null;
		Patient p = null;
		Iterator<Patient> it = patients.values().iterator();
		while (it.hasNext()) {
			p = it.next();
			boolean isErr = false;

			for (int i = 0; rules != null && i < rules.size(); i++) {
				rule = rules.get(i);
				try {
					List<BreakDetail> rs = rule.check(p);
					// 数据不能过规则后的处理办法
					if (rs != null && rs.size() > 0) {
						for (int k = 0; k < rs.size(); k++) {
							isErr = true;
							BreakDetail bd = rs.get(k);
							bd.setBreak_type(cpaTask.getExeType()); // 任务类型
							bd.setScenario_id(new Long(task.getScenarioId())
									.intValue()); // 方案ID
							bd.setBatch_code(this.cpaTask.getBatchCode()); // 任务名称
							bd.setCrt_user_id(9999); // 创建者ID
							bd.setRule_id(rule.ruleId);
							bd.setLog_id(this.cpaTask.getLogId());
//							bd.setBreak_detail(rule.category_name); //设置错误为用户指定的类型
							mgr.saveBreakDetail(bd);
//							log.debug("patient:" + p.getSeqId() + "  err: "
//									+ bd.getBreak_detail());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error(rule.getCkeckName() + "," + "运行出错,"
							+ e.getLocalizedMessage());
				}
			}
			
			count.addAndGet(1);
			
			if (!isErr) {
				// log.info("patient:" + p.getSeqId() + " no err!");
			}
		}
		
		try {
			// 所有的错误，在最后提交事务
			mgr.lastCommit();
			
			//判断是否执行存储过程
			String isExecuteProcedure = String.valueOf(ApplicationProperties.getPropertyValue("is_external_exe").trim());
			if ("true".equals(isExecuteProcedure)){
				ProcedureRuleMgr pmgr = ProcedureRuleMgr.getInstance();
				pmgr.updateClmByProcedure(start, end, cpaTask);
			}
			
			// 记录任务执行成功信息
			TaskMgr.getInstance().updateSubTaskState(taskNo,
					Constant.SUBTASK_STATE_OPERATE_OK, "任务正常运行完成");

			String rs = dealId +"result=OK|taskNo=" + taskNo + "|taskType=CPA";
			this.sendMessage(logId,taskNo, true, rs);		
		} catch (Exception e) {
			log.error(e.getMessage());
			String err = "执行审核过程出错,taskNo:" + taskNo + ",startPatientId:"
					+ task.getHandleStartNum() + ",endPatientId:"
					+ task.getHandleEndNum() + ",错误描述:" + e.getLocalizedMessage();
			this.sendMessage(logId,taskNo, false, err);
			e.printStackTrace();
		}finally{
			this.over=true;
		}
	}
	
	//返回信息处理
	private void sendMessage(long logId,long taskNo,boolean flag,String msg){
		//根据不同deal返回不同done任务
		TaskDone td =null;
		if (dealId.equals(DealCodes.CPA_RT_TASK.toString())){
			td = new CPARTTaskDone();
			((CPARTTaskDone)td).setLogId(logId);
		}else{
			td = new CPATaskDone();
			((CPATaskDone)td).setLogId(logId);
		}
		td.setTaskId(taskNo);
		if (false ==flag){
			log.error(msg);
			td.setSuccess(false);
			td.setMessage(msg);
		}else{
			log.info("返回报文:" + msg);
			td.setSuccess(true);
		}
		shandler.sendJsonMessage(td);
	}

	/**
	 * 患者数据初始化
	 * @param patients 患者对象
	 * @param start  开始编号
	 * @param end 结束编号
	 * @param task  任务
	 * @throws Exception
	 */
	public void initPatients(Map<Long, Patient> patients, long start,
			long end, CpaTask task) throws Exception {

		PatientMgr mgr = PatientMgr.getInstance();
		
		// 1.分表加载的方式加载处理数据
		mgr.loadPatiData(patients, start, end, task);
		
		// 2. 加载单纯住院信息，并排序
		mgr.loadHospitalData(patients, start, end, task);
	}

	/**
	 * 初始化方案
	 * 
	 * @param logId
	 * @throws Exception
	 */
	public void init(long logId) throws Exception {
		try {
			cpaTask = CpaTaskMgr.getInstance().getTaskById(logId);
			if (cpaTask == null) {
				throw new Exception("CPA_SCENARIO_EXELOG.id=" + logId + " is null");
			}
			//初始化规则集合
			rules = SpringBeanUtil.getSpringBean("projectMgr", ProjectMgr.class)
					.getRules(cpaTask.getScenarioId());
			if (null == rules){
				throw new Exception(" rules is null");
			}
		    //补充规则字段
			for (AbstractRule rule : rules){
				rule.calculateStartTime(cpaTask.getTaskStart());
			}
			
		} catch (Exception e1) {
			log.error("查询任务出错," + e1.getMessage());
			throw e1;
		}

		try {
			int maxDay =0;
			maxDay= SpringBeanUtil.getSpringBean("projectMgr", ProjectMgr.class)
					.getMaxRuleDate(cpaTask.getScenarioId());
			//设置数据的最长查询时间
			maxDay = maxDay <= 180 ? maxDay : 180;
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(cpaTask.getTaskStart());
			cal.add(cal.DAY_OF_MONTH, 0 - maxDay);
			
			cpaTask.setStartTimeSearch(cal.getTime());
			log.info("max searchDay:" + maxDay);
		} catch (Exception e1) {
			log.error("初始化方案出错," + e1.getMessage());
			e1.printStackTrace();
			throw e1;
		}
	}
	
	
}

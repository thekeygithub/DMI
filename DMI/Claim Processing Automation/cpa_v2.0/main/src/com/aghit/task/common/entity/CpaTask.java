package com.aghit.task.common.entity;

import java.util.Date;

import com.aghit.task.util.CPADateUtil;

public class CpaTask {
	
	//日志id，主键
	private long logId;
	
	//方案id
	private long scenarioId;
	
	//方案执行类型,1.定时执行， 2.手动执行
	private String exeType;
	
	//批次号
	private String batchCode;
	
	//用户处理数
	private long handleCount;
	
	//执行结果: 1。成功   0.失败
	private String exeResult;
	
	//操作用户，手动执行，记录登录者id，定时程序，直接写-1
	private long exeUserId;
	
	//创建人
	private long crtUserId;
	
	//创建日期时间
	private Date crtDTTM;
	
	//备注
	private String memo;
	
	//用户处理数
	private long patientCount;
	
	//数据处理数
	private long dataCount;
	
	//周期开始时间
	private Date taskStart;
	
	//周期结束时间
	private Date taskEnd;
	
	//数据周期开始时间(辅助计算的数据时间,这个时间由周期开始时间加上规则的最长周期计算而来)
	private Date startTimeSearch;
	
	//数据来源ID
	private long dataSrcId; 

	//总的结算单数
	private long clmCount;
	
	public Date getStartTimeSearch() {
		return startTimeSearch;
	}

	public void setStartTimeSearch(Date startTimeSearch) {
		this.startTimeSearch = startTimeSearch;
	}

	public Date getTaskStart() {
		return taskStart;
	}

	public void setTaskStart(Date taskStart) {
		this.taskStart = taskStart;
	}

	public Date getTaskEnd() {
		return taskEnd;
	}

	public void setTaskEnd(Date taskEnd) {
//		this.taskEnd = CPADateUtil.getDateLast(taskEnd);
		this.taskEnd = taskEnd;
	}

	public long getPatientCount() {
		return patientCount;
	}

	public void setPatientCount(long patientCount) {
		this.patientCount = patientCount;
	}

	public long getDataCount() {
		return dataCount;
	}

	public void setDataCount(long dataCount) {
		this.dataCount = dataCount;
	}

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public long getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(long scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getExeType() {
		return exeType;
	}

	public void setExeType(String exeType) {
		this.exeType = exeType;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public long getHandleCount() {
		return handleCount;
	}

	public void setHandleCount(long handleCount) {
		this.handleCount = handleCount;
	}

	public String getExeResult() {
		return exeResult;
	}

	public void setExeResult(String exeResult) {
		this.exeResult = exeResult;
	}

	public long getExeUserId() {
		return exeUserId;
	}

	public void setExeUserId(long exeUserId) {
		this.exeUserId = exeUserId;
	}

	public long getCrtUserId() {
		return crtUserId;
	}

	public void setCrtUserId(long crtUserId) {
		this.crtUserId = crtUserId;
	}

	public Date getCrtDTTM() {
		return crtDTTM;
	}

	public void setCrtDTTM(Date crtDTTM) {
		this.crtDTTM = crtDTTM;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public long getDataSrcId() {
		return dataSrcId;
	}

	public void setDataSrcId(long dataSrcId) {
		this.dataSrcId = dataSrcId;
	}

	public long getClmCount() {
		return clmCount;
	}
	
	public void setClmCount(long clmCount) {
		this.clmCount = clmCount;
	}

}

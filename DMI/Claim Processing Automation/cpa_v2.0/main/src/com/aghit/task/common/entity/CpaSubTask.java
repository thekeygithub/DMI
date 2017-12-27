package com.aghit.task.common.entity;

public class CpaSubTask extends Task {

	/**
	 * CPA_TASKQUEUE表的字段
	 */
	//主键
	private long cpaAtId;
	
	//审核方案id
	private long scenarioId;
	
	//审核开始日期
	private String exeStartDT;
	
	//审核结束日期
	private String exeEndDT;
	
	//处理患者开始序号
	private long handleStartNum;
	
	//处理患者结束序号
	private long handleEndNum;
	
	//任务类型
	private String taskType;
	
	//关联日志id
	private long logId;
	
	
	public long getCpaAtId() {
		return cpaAtId;
	}

	public void setCpaAtId(long cpaAtId) {
		this.cpaAtId = cpaAtId;
	}

	public long getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(long scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getExeStartDT() {
		return exeStartDT;
	}

	public void setExeStartDT(String exeStartDT) {
		this.exeStartDT = exeStartDT;
	}

	public String getExeEndDT() {
		return exeEndDT;
	}

	public void setExeEndDT(String exeEndDT) {
		this.exeEndDT = exeEndDT;
	}

	public long getHandleStartNum() {
		return handleStartNum;
	}

	public void setHandleStartNum(long handleStartNum) {
		this.handleStartNum = handleStartNum;
	}

	public long getHandleEndNum() {
		return handleEndNum;
	}

	public void setHandleEndNum(long handleEndNum) {
		this.handleEndNum = handleEndNum;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}
}

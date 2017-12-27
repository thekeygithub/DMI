package com.aghit.task.common.entity;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
	
	/**
	 * AG_TASKQUEUE表的字段
	 */
	
	//主键
	private long atId;
	
	//任务类型
	private int atType;
	
	//创建人
	private String cuser;
	
	//创建时间
	private Date cTime;
	
	//任务开始运行时间
	private Date atStime;
	
	//任务结束时间
	private Date atEtime;
	
	//任务运行状态
	private int atState;
	
	//运行日志
	private String atLog;
	
	//是否重复运行
	private int atRerun;
	
	//重复执行的用户
	private String atReuser;
	
	//终止运行
	private int atStop;
	
	//停止原因
	private String atScause;
	
	//设置任务终止的用户
	private String atSuser;
	
	//运行的server，ip地址 + 端口
	private String atRunserver;
	
	//本任务重复运行的次数
	public AtomicInteger runCount =new AtomicInteger(0);

	public long getAtId() {
		return atId;
	}

	public void setAtId(long atId) {
		this.atId = atId;
	}

	public int getAtType() {
		return atType;
	}

	public void setAtType(int atType) {
		this.atType = atType;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public Date getCTime() {
		return cTime;
	}

	public void setCTime(Date time) {
		cTime = time;
	}
	

	public Date getAtStime() {
		return atStime;
	}

	public void setAtStime(Date atStime) {
		this.atStime = atStime;
	}

	public Date getAtEtime() {
		return atEtime;
	}

	public void setAtEtime(Date atEtime) {
		this.atEtime = atEtime;
	}

	public int getAtState() {
		return atState;
	}

	public void setAtState(int atState) {
		this.atState = atState;
	}

	public String getAtLog() {
		return atLog;
	}

	public void setAtLog(String atLog) {
		this.atLog = atLog;
	}

	public int getAtRerun() {
		return atRerun;
	}

	public void setAtRerun(int atRerun) {
		this.atRerun = atRerun;
	}

	public String getAtReuser() {
		return atReuser;
	}

	public void setAtReuser(String atReuser) {
		this.atReuser = atReuser;
	}

	public int getAtStop() {
		return atStop;
	}

	public void setAtStop(int atStop) {
		this.atStop = atStop;
	}

	public String getAtScause() {
		return atScause;
	}

	public void setAtScause(String atScause) {
		this.atScause = atScause;
	}

	public String getAtSuser() {
		return atSuser;
	}

	public void setAtSuser(String atSuser) {
		this.atSuser = atSuser;
	}

	public String getAtRunserver() {
		return atRunserver;
	}

	public void setAtRunserver(String atRunserver) {
		this.atRunserver = atRunserver;
	}


}

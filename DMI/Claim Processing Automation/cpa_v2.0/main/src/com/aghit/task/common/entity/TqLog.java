package com.aghit.task.common.entity;

import java.util.Date;

public class TqLog {
	
	//AL_ID
	private long alId;
	
	//主键
	private long atId;
	
	//运行的SERVER，IP地址+端口，例如192.168.5.123:9001
	private String atRunserver;
	
	//创建人
	private String cuser;
	
	//创建时间
	private Date ctime;

	public long getAlId() {
		return alId;
	}

	public void setAlId(long alId) {
		this.alId = alId;
	}

	public long getAtId() {
		return atId;
	}

	public void setAtId(long atId) {
		this.atId = atId;
	}

	public String getAtRunserver() {
		return atRunserver;
	}

	public void setAtRunserver(String atRunserver) {
		this.atRunserver = atRunserver;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	
	
	
	
	

}

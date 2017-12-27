package com.aghit.task.algorithm.domain;

import java.util.Date;

/**
 * CPA审核结果头表实体映射类
 * 
 * @author Administrator
 * 
 */
public class BreakMaster {

	private long recId; // 违规记录id
	private String sysId;// 系统标识,1:合规性引擎;2:合理性引擎
	private String sendType;// 下发类型0 不可下发 1单一下发 2一对多下发
	private String breakType; // 方案执行类型，0:定时执行1:手动执行
	private long ruleId; // 违规规则id
	private long scenarioId; // 执行方案id
	private long logId;// 方案执行日志的id
	private String errMsg;// 错误原因
	private String objType;// 监控对象类型
	private String objName;// 监控对象名称
	private String objVal;// 监控对象值
	private String lgcyOrgCode;// 医院编号
	private double refTot;// 涉及总金额
	private String instParam;// 实例参数值
	private String timeRange;// 时间范围
	private String cycStart;// 时间段开始
	private String cycEnd;// 时间段结束
	private String indVal;// 指标值
	private String scopeVal1;// 计算范围1值
	private String scopeVal2;// 计算范围2值
	private String scopeVal3;// 计算范围3值
	private String scopeVal4;// 计算范围4值
	private String scopeVal5;// 计算范围5值
	private String crtUserId;// 操作者
	private Date crtTime;// 创建日期时间
	private String aplState;// 申诉状态
	private String updTime;// 更新时间
	private String updUser;// 更新人姓名
	private String updUid;// 更新人ID
	private long kId;  //知识id

	public long getRecId() {
		return recId;
	}

	public void setRecId(long recId) {
		this.recId = recId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getBreakType() {
		return breakType;
	}

	public void setBreakType(String breakType) {
		this.breakType = breakType;
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public long getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(long scenarioId) {
		this.scenarioId = scenarioId;
	}

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjVal() {
		return objVal;
	}

	public void setObjVal(String objVal) {
		this.objVal = objVal;
	}

	public String getLgcyOrgCode() {
		return lgcyOrgCode;
	}

	public void setLgcyOrgCode(String lgcyOrgCode) {
		this.lgcyOrgCode = lgcyOrgCode;
	}

	public double getRefTot() {
		return refTot;
	}

	public void setRefTot(double refTot) {
		this.refTot = refTot;
	}

	public String getInstParam() {
		return instParam;
	}

	public void setInstParam(String instParam) {
		this.instParam = instParam;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public String getCycStart() {
		return cycStart;
	}

	public void setCycStart(String cycStart) {
		this.cycStart = cycStart;
	}

	public String getCycEnd() {
		return cycEnd;
	}

	public void setCycEnd(String cycEnd) {
		this.cycEnd = cycEnd;
	}

	public String getIndVal() {
		return indVal;
	}

	public void setIndVal(String indVal) {
		this.indVal = indVal;
	}

	public String getScopeVal1() {
		return scopeVal1;
	}

	public void setScopeVal1(String scopeVal1) {
		this.scopeVal1 = scopeVal1;
	}

	public String getScopeVal2() {
		return scopeVal2;
	}

	public void setScopeVal2(String scopeVal2) {
		this.scopeVal2 = scopeVal2;
	}

	public String getScopeVal3() {
		return scopeVal3;
	}

	public void setScopeVal3(String scopeVal3) {
		this.scopeVal3 = scopeVal3;
	}

	public String getScopeVal4() {
		return scopeVal4;
	}

	public void setScopeVal4(String scopeVal4) {
		this.scopeVal4 = scopeVal4;
	}

	public String getScopeVal5() {
		return scopeVal5;
	}

	public void setScopeVal5(String scopeVal5) {
		this.scopeVal5 = scopeVal5;
	}

	public String getCrtUserId() {
		return crtUserId;
	}

	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}

	public Date getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	public String getAplState() {
		return aplState;
	}

	public void setAplState(String aplState) {
		this.aplState = aplState;
	}

	public String getUpdTime() {
		return updTime;
	}

	public void setUpdTime(String updTime) {
		this.updTime = updTime;
	}

	public String getUpdUser() {
		return updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public long getkId() {
		return kId;
	}

	public void setkId(long kId) {
		this.kId = kId;
	}

}

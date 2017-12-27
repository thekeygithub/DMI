package com.aghit.task.algorithm.domain;

/**
 * 药品的合理用药知识库实体
 * @author Administrator
 *
 */
public class ProperUsageKL{

	private long id;						// 主键ID
	private String properPersion; 			// 人群
	private long indication; 				// 适应症
	private String useWay; 					// 给药途径
	private int limitDays;					// 限制天数
	private double dayDose;					// 说明书日剂量
	private String logicId;					// 逻辑运算ID
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProperPersion() {
		return properPersion;
	}

	public void setProperPersion(String properPersion) {
		this.properPersion = properPersion;
	}

	public long getIndication() {
		return indication;
	}

	public void setIndication(long indication) {
		this.indication = indication;
	}

	public String getUseWay() {
		return useWay;
	}

	public void setUseWay(String useWay) {
		this.useWay = useWay;
	}

	public int getLimitDays() {
		return limitDays;
	}

	public void setLimitDays(int limitDays) {
		this.limitDays = limitDays;
	}

	public double getDayDose() {
		return dayDose;
	}

	public void setDayDose(double dayDose) {
		this.dayDose = dayDose;
	}

	public String getLogicId() {
		return logicId;
	}

	public void setLogicId(String logicId) {
		this.logicId = logicId;
	}

}

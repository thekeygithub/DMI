package com.aghit.task.common.entity.klbase;

import java.util.Date;
import java.util.Set;

public class KlRule {
	
	private long rule_id; // 规则ID
	private String rule_name; // 规则名称
	private int cycleStart; // 周期开始日
	private int cycleEnd; // 周期结束日
	private int cycleFlg; // 是否周期
	/**
	 * 就医类型
	 */
	private Set<Integer> medicalType; 

	/**
	 * 检查结算单数据的起始时间，包括参与计算的数据和辅助运算的数据,由任务开始时间再减去周期结束日而来 cycleStartTime =
	 * task.startTime - (0-cycleEnd)
	 */
	private Date cycleStartTime;

	//运行模型：0:药品ID，1:分类ID
	private String runModel;
	
	

}

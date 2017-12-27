/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.statistics.service;

import com.ebmi.seng.YearStatistics;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.statistics.dto.PYearMedical;

/**
 * 统计服务
 * 
 * @author xiangfeng.guan
 */
public interface StatisticsService {
	
	YearStatistics getPyearmedicalDatil(PMiBaseInfoCond cond) throws Exception;
	/**
	 * xuhai add 2016-5-25 15:55:36
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	YearStatistics getPYearMedicalNew(PMiBaseInfoCond cond) throws Exception;

	/**
	 * 获取个人年度统计信息
	 * 
	 * @param p_mi_id 社保ID
	 * @param year 年度
	 * @return 年度统计信息
	 * @throws Exception
	 */
	PYearMedical getPYearMedical(String p_mi_id, String year) throws Exception;

}

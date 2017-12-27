/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.statistics.dao;

import com.ebmi.std.statistics.dto.PYearMedical;

/**
 * @author xiangfeng.guan
 *
 */
public interface StatisticsDao {

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

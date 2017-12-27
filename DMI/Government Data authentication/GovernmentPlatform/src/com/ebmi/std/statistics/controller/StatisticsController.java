/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.statistics.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ebmi.seng.YearStatistics;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.statistics.dto.PYearMedical;
import com.ebmi.std.statistics.service.StatisticsService;

/**
 * 统计数据
 * 
 * @author xiangfeng.guan
 */
@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {

	private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

	@Resource
	private StatisticsService service;
	/**
	 * xuhai add 2016-5-25 15:21:04
	 * @param p_mi_id
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "/pyearmedicalNew", method = RequestMethod.POST)
	public @ResponseBody YearStatistics pyearmedicalNew(String p_mi_id, String year
			,String idcard,String username,String ss_num ,String table_stat
			,String sta_type) {
		try {
			PMiBaseInfoCond cond = new PMiBaseInfoCond();
			cond.setP_mi_id(p_mi_id);
			cond.setIdcard(idcard);
			cond.setUsername(username);
			cond.setSs_num(ss_num);
			cond.setTable_stat(table_stat);
			cond.setSta_type(sta_type);//年度费用统计类型（1支付构成、3就诊类型）
			cond.setYear(year);
			YearStatistics r = service.getPYearMedicalNew(cond);
			return r;
		} catch (Exception e) {
			logger.error("获取个人就诊年度统计信息错误", e);
		}
		return null;
	}
	
	/**
	 * 明细信息
	 * @param p_mi_id
	 * @param year
	 * @param idcard
	 * @param username
	 * @param ss_num
	 * @param table_stat
	 * @param sta_type
	 * @return
	 */
	@RequestMapping(value = "/pyearmedicalDatil", method = RequestMethod.POST)
	public @ResponseBody YearStatistics pyearmedicalDatil(String p_mi_id, String year, String idcard, String username, String ss_num, 
			String table_stat, String sta_type, String med_type_name, String dept_num_one, String dept_num_two, String item_id) {
		
		try {
			PMiBaseInfoCond cond = new PMiBaseInfoCond();
			cond.setP_mi_id(p_mi_id);
			cond.setIdcard(idcard);
			cond.setUsername(username);
			cond.setSs_num(ss_num);
			cond.setTable_stat(table_stat);
			cond.setSta_type(sta_type);//年度费用统计类型（1支付构成、3就诊类型）
			cond.setYear(year);
			cond.setMed_type_name(med_type_name);//类型名称 例如 普通住院 门诊 等
			cond.setDept_num_one(dept_num_one);//机构编号
			cond.setDept_num_two(dept_num_two);//定点医疗服务机构编号
			cond.setItem_id(item_id);//流水号
			YearStatistics r = service.getPyearmedicalDatil(cond);
			return r;
		} catch (Exception e) {
			logger.error("获取个人就诊年度统计明细信息错误", e);
		}
		return null;
	}
	

	/**
	 * 获取个人就诊年度统计信息
	 * 
	 * @param p_mi_id 社保ID
	 * @param year 年度
	 * @param recordStart 起始记录
	 * @param recordCount 获取条数
	 * @return 个人就诊年度统计信息
	 */
	@RequestMapping(value = "/pyearmedical", method = RequestMethod.POST)
	public @ResponseBody PYearMedical getPYearMedical(String p_mi_id, String year) {
		try {
			PYearMedical r = service.getPYearMedical(p_mi_id, year);
			return r;
		} catch (Exception e) {
			logger.error("获取个人就诊年度统计信息错误", e);
		}
		return null;
	}
}

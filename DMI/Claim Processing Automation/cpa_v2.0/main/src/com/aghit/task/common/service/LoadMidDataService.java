package com.aghit.task.common.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 获取中间表数据接口
 * @author Administrator
 *
 */
public interface LoadMidDataService {

	/**
	 * 获取中间表数据，包括结算单、医嘱、处方类型
	 * 返回结果Map，key为类型，value为类型对应的数据
	 * 
	 * @param sdt 开始时间
	 * @param edt 结束时间
	 * @param sseq 患者开始序号
	 * @param eseq 患者结束序号
	 * @param exeType 执行类型
	 * @return
	 * @throws Exception
	 */
	public Map<String, Collection<Map<String, Object>>> executeFetchMidData(Date sdt, Date edt,
			long sseq, long eseq, long src,String exeType) throws Exception;
	
}

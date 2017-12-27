package com.ebmi.std.pbase.dao;

import java.util.List;

import com.ebmi.jms.entity.ViewGrjbxxJm;
import com.ebmi.jms.entity.ViewGrjbxxYl;
import com.ebmi.std.pbase.cond.PSiStatCond;
import com.ebmi.std.pbase.dto.PSiStat;

/**
 * 参保人社保状态DAO
 * 
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午5:17:47
 */
public interface PersonInsDao {
	/**
	 * 参保人社保状态
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<PSiStat> query(PSiStatCond cond) throws Exception;

	public ViewGrjbxxJm queryGrjbxxJm(String grbh) throws Exception;

	public ViewGrjbxxYl queryGrjbxxYl(String grbh) throws Exception;

}

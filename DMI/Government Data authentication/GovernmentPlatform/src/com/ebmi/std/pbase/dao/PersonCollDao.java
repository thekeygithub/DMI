package com.ebmi.std.pbase.dao;

import java.util.List;

import com.ebmi.jms.entity.ViewGrjjkJm;
import com.ebmi.jms.entity.ViewGrjjkYl;
import com.ebmi.std.pbase.cond.PCollDetCond;
import com.ebmi.std.pbase.dto.PCollDet;

/**
 * 参保人缴费信息查询
 * 
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午5:37:39
 */
public interface PersonCollDao {

	/**
	 * 查询参保人社保缴费情况
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<PCollDet> query(PCollDetCond cond) throws Exception;

	/**
	 * 查询参保人参保险种列表
	 * 
	 * @param p_mi_id 参保人id
	 * @return 参保人参保险种列表
	 * @throws Exception
	 */
	public List<String> queryPSiCatList(String p_mi_id) throws Exception;

	public List<ViewGrjjkYl> queryGrjjkYl(String grbh, Integer year) throws Exception;

	public List<ViewGrjjkJm> queryGrjjkJm(String grbh, Integer year) throws Exception;

}

package com.ebmi.std.pbase.service;

import java.util.List;

import com.ebmi.jms.entity.ViewDwjbxx;
import com.ebmi.jms.entity.ViewGrjbxx;
import com.ebmi.jms.entity.ViewGrjbxxGs;
import com.ebmi.jms.entity.ViewGrjbxxJm;
import com.ebmi.jms.entity.ViewGrjbxxSy;
import com.ebmi.jms.entity.ViewGrjbxxYl;
import com.ebmi.jms.entity.ViewGrjjkJm;
import com.ebmi.jms.entity.ViewGrjjkYl;
import com.ebmi.seng.AccHisRecord;
import com.ebmi.seng.BalanceApp;
import com.ebmi.std.pbase.cond.PCollDetCond;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.pbase.cond.PSiEndowDetCond;
import com.ebmi.std.pbase.cond.PSiStatCond;
import com.ebmi.std.pbase.dto.PCollDet;
import com.ebmi.std.pbase.dto.PMiBaseInfo;
import com.ebmi.std.pbase.dto.PSiEndowDet;
import com.ebmi.std.pbase.dto.PSiStat;

/**
 * 参保人个人信息service
 * 
 * @author yanjie.ji
 * @date 2015-12-22
 * @time 下午3:56:51
 */
public interface PersonService {
	/**
	 * 查询变更记录
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	public List<AccHisRecord> queryBalanceRecords(PMiBaseInfoCond cond) throws Exception;
	/**
	 * 查询账户余额
	 * xuhai add 
	 * @param p_mi_id 参保人id
	 * @param year 年度
	 * @return
	 */
	public BalanceApp queryPAccBalance(PMiBaseInfoCond cond) throws Exception;
	
	
	
	/**
	 * 基本信息查询–用户参保信息
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public PMiBaseInfo queryInsuranceinfo(PMiBaseInfoCond cond) throws Exception;
	
	/**
	 * 查询参保人个人信息
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public PMiBaseInfo queryPersonBase(PMiBaseInfoCond cond) throws Exception;

	/**
	 * 参保人社保状态
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<PSiStat> queryPersonIns(PSiStatCond cond) throws Exception;

	/**
	 * 查询参保人社保缴费情况
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<PCollDet> queryPersonColl(PCollDetCond cond) throws Exception;

	/**
	 * 查询参保人养老金领取情况
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<PSiEndowDet> queryPersonPension(PSiEndowDetCond cond) throws Exception;

	/**
	 * 查询参保人参保险种列表
	 * 
	 * @param p_mi_id 参保人id
	 * @return 参保人参保险种列表
	 * @throws Exception
	 */
	public List<String> queryPSiCatList(String p_mi_id) throws Exception;

	public ViewGrjbxx queryGrjbxx(String grbh) throws Exception;

	public ViewGrjbxxJm queryGrjbxxJm(String grbh) throws Exception;

	public ViewGrjbxxYl queryGrjbxxYl(String grbh) throws Exception;

	public ViewDwjbxx queryDwjbxx(String dwbh) throws Exception;

	public ViewGrjbxxGs queryGrjbxxGs(String grbh) throws Exception;

	public ViewGrjbxxSy queryGrjbxxSy(String grbh) throws Exception;

	public List<ViewGrjjkYl> queryGrjjkYl(String grbh, Integer year) throws Exception;

	public List<ViewGrjjkJm> queryGrjjkJm(String grbh, Integer year) throws Exception;

}

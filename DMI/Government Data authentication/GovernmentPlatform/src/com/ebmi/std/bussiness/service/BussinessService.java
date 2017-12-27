package com.ebmi.std.bussiness.service;

import com.ebmi.std.bussiness.dto.Complaint;
import com.ebmi.std.bussiness.dto.SiCardInfo;
import com.ebmi.std.bussiness.dto.SiCardStateInfo;
import com.ebmi.std.bussiness.dto.SiCardStateQueryInfo;

public interface BussinessService {
	
	/**
	 * 
	 * @Description: 根据社保卡号获取社保卡信息
	 * @Title: getSiCardInfo 
	 * @param siCardNo
	 * @return SicardInfo
	 */
	public SiCardInfo getSiCardInfo(String siCardNo, String pMiId)throws Exception;
	
	/**
	 * 
	 * @Description: 获取社保卡状态
	 * @Title: getSiCardStateInfo 
	 * @param siCardNo
	 * @param pMiId
	 * @return
	 * @throws Exception SiCardStateInfo
	 */
	public SiCardStateInfo getSiCardStateInfo(String siCardNo, String pMiId) throws Exception;
	
	/**
	 * 
	 * @Description: 更新社保信息
	 * @Title: updateInfo 
	 * @param sicardInfo
	 * @return void
	 */
	public void updateInfo(SiCardInfo sicardInfo)throws Exception;
	
	/**
	 * 
	 * @Description: 挂失社保卡
	 * @Title: lostSiCard 
	 * @param siCardNo
	 * @param pMiId
	 * @return
	 * @throws Exception boolean
	 */
	public boolean lostSiCard(String siCardNo, String pMiId)throws Exception;
	
	/**
	 * 
	 * @Description: 提交投诉
	 * @Title: saveComplaint 
	 * @param complaint
	 * @return boolean
	 */
	public Complaint saveComplaint(Complaint complaint)throws Exception;
	
	/**
	 * 
	 * @Description:投诉详情
	 * @Title: getComplaint 
	 * @param recordId
	 * @return
	 * @throws Exception Complaint
	 */
	public Complaint getComplaint(String recordId) throws Exception;
	
	/**
	 * 
	 * @Description: 社保卡进度查询
	 * @Title: queryMakeCardState 
	 * @param siCardNo
	 * @return
	 * @throws Exception SiCardStateQueryInfo
	 */
	public SiCardStateQueryInfo queryMakeCardState(String siCardNo)throws Exception;
}

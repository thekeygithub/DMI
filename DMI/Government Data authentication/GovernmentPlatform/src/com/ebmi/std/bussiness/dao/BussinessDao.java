package com.ebmi.std.bussiness.dao;

import com.ebmi.std.bussiness.dto.Complaint;
import com.ebmi.std.bussiness.dto.SiCardInfo;
import com.ebmi.std.bussiness.dto.SiCardStateInfo;
import com.ebmi.std.bussiness.dto.SiCardStateQueryInfo;

public interface BussinessDao {
	/**
	 * 
	 * @Description: 根据社保卡号获取社保卡信息
	 * @Title: getSiCardInfo 
	 * @param siCardNo
	 * @param pMiId
	 * @return SicardInfo
	 */
	public SiCardInfo getSiCardInfo(String siCardNo, String pMiId) throws Exception;
	
	/**
	 * 
	 * @Description: 获取社保卡信息
	 * @Title: getSiCardStateInfo 
	 * @param pMiId
	 * @return
	 * @throws Exception SiCardStateInfo
	 */	
	public SiCardStateInfo getSiCardStateInfo(String siCardNo, String pMiId) throws Exception;
	
	/**
	 * 
	 * @Description:第三方 更新社保信息
	 * @Title: updateInfo 
	 * @param sicardInfo
	 * @return boolean
	 */
	public boolean updateInfo(SiCardInfo sicardInfo) throws Exception;
	
	
	/**
	 * 
	 * @Description: :本地更新社保信息
	 * @Title: updateLocalInfo 
	 * @param sicardInfo
	 * @throws Exception void
	 */
	public void updateLocalInfo(SiCardInfo sicardInfo) throws Exception;
	
	/**
	 * 
	 * @Description: 更新社保信息
	 * @Title: updateInfo 
	 * @param sicardInfo
	 * @return boolean
	 */
	public boolean lostSiCard(String siCardNo, String pMiId) throws Exception;
	
	/**
	 * 
	 * @Description: 提交投诉
	 * @Title: saveComplaint 
	 * @param complaint
	 * @return Complaint
	 */
	public Complaint saveComplaint(Complaint complaint) throws Exception;
	
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

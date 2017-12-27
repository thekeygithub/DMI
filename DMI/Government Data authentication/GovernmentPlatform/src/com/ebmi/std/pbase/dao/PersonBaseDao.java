package com.ebmi.std.pbase.dao;

import com.ebmi.jms.entity.ViewDwjbxx;
import com.ebmi.jms.entity.ViewGrjbxx;
import com.ebmi.jms.entity.ViewGrjbxxGs;
import com.ebmi.jms.entity.ViewGrjbxxSy;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.pbase.dto.PMiBaseInfo;

/**
 * 参保人个人信息DAO
 * 
 * @author yanjie.ji
 * @date 2015-12-21
 * @time 下午5:16:52
 */
public interface PersonBaseDao {
	public PMiBaseInfo queryInsuranceinfo(PMiBaseInfoCond cond) throws Exception;

	/**
	 * 查询参保人个人信息
	 * 
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public PMiBaseInfo query(PMiBaseInfoCond cond) throws Exception;

	public ViewGrjbxx queryGrjbxx(String grbh) throws Exception;

	public ViewDwjbxx queryDwjbxx(String dwbh) throws Exception;

	public ViewGrjbxxGs queryGrjbxxGs(String grbh) throws Exception;

	public ViewGrjbxxSy queryGrjbxxSy(String grbh) throws Exception;
}

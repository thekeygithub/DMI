package com.models.cloud.pay.escrow.mi.pangu.service;

import com.models.cloud.pay.escrow.mi.pangu.request.MzAccoutInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.MzQueryInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.MzTollReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TollsReq;
import com.models.cloud.pay.escrow.mi.pangu.response.MzBackResultRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzOrderResultRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzSfmxRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzXiaofxxRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzYSfmxRes;
/**
 * 门诊结算接口
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 上午11:10:54
 */
public interface OutpatientService {

	/**
	 * 门诊预结算
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public MzYSfmxRes executeMzYSfmx(String fixid,String cycid,TollsReq p)throws Exception;
	
	/**
	 * 门诊结算
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public MzSfmxRes executeMzSfmx(String fixid,String cycid,TollsReq p)throws Exception;
	
	/**
	 * 门诊回退
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public MzBackResultRes executeMzBack(String fixid,String cycid,MzTollReq p)throws Exception;
	
	/**
	 * 门诊交易记录查询
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public MzOrderResultRes queryMzOrder(String fixid,String cycid,MzQueryInfoReq p)throws Exception;
	
	/**
	 * 门诊月账单查询
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public MzXiaofxxRes queryMzYSfmx(String fixid,String cycid,MzAccoutInfoReq p)throws Exception;
	
}

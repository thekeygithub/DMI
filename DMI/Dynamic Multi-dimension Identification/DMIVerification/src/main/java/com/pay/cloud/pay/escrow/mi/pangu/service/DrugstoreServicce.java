package com.pay.cloud.pay.escrow.mi.pangu.service;

import com.pay.cloud.pay.escrow.mi.pangu.request.Toll2Req;
import com.pay.cloud.pay.escrow.mi.pangu.request.YdAccoutInfoReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.YdQueryInfoReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.YdTollReq;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdBackResultRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdOrderResultRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdSfmxRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdXiaofxxRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdYSfmxRes;

/**
 * 药店结算接口
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 下午1:07:22
 */
public interface DrugstoreServicce {
	
	/**
	 * 药店预结算
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public YdYSfmxRes executeYdYSfmx(String fixid,String cycid,Toll2Req p)throws Exception;
	
	/**
	 * 药店结算
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public YdSfmxRes executeYdSfmx(String fixid,String cycid,Toll2Req p)throws Exception;
	
	/**
	 * 药店回退
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public YdBackResultRes executeYdBack(String fixid,String cycid,YdTollReq p)throws Exception;
	
	/**
	 * 药店交易记录查询
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public YdOrderResultRes queryYdOrder(String fixid,String cycid,YdQueryInfoReq p)throws Exception;
	
	/**
	 * 药店月账单查询
	 * @param fixid  定点编号
	 * @param cycid  验证信息,由获取个人信息接口返回
	 * @param p      参数
	 * @return
	 * @throws Exception
	 */
	public YdXiaofxxRes queryYdYSfmx(String fixid,String cycid,YdAccoutInfoReq p)throws Exception;
}

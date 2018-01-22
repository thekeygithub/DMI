package com.pay.cloud.pay.escrow.mi.pangu.service;

import com.pay.cloud.pay.escrow.mi.pangu.request.MxbInfoReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.PersonReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.TollInfoReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.TreatmentStatusReq;
import com.pay.cloud.pay.escrow.mi.pangu.response.BzInfoRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.MxbRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.OrderStatusRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.SystemDateRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.TreatmentStatusRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YaopInfoRes;
/**
 * 获取公用信息的Service
 * @author yanjie.ji
 * @date 2016年12月3日 
 * @time 上午10:37:37
 */
public interface CommInfoService {
	/**
	 * 查询金保服务器时间
	 * @return
	 * @throws Exception
	 */
	public SystemDateRes querySysTime()throws Exception;
	
	/**
	 * 查询个人信息
	 * @param fixid
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public PersonInfoRes queryPerInfo(String fixid,PersonReq p)throws Exception;
	/**
	 * 查询医疗待遇封锁信息
	 * @param fixid
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public TreatmentStatusRes queryTreatStatus(String fixid,TreatmentStatusReq p)throws Exception;
	/**
	 * 查询慢性病
	 * @param fixid
	 * @param cycid
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public MxbRes queryChronicDisease(String fixid,String cycid,MxbInfoReq p)throws Exception;

	/**
	 * 查询病种信息
	 * @param fixid
	 * @return
	 * @throws Exception
	 */
	public BzInfoRes queryDiseaseInfo(String fixid)throws Exception;
	/**
	 * 查询药品目录
	 * @param fixid
	 * @return
	 * @throws Exception
	 */
	public YaopInfoRes queryDrugInfo(String fixid)throws Exception;
	/**
	 * 查询结算记录状态
	 * @param fixid
	 * @param p
	 * @return
	 * @throws Exception
	 */
	public OrderStatusRes queryOrderStatus(String fixid,TollInfoReq p)throws Exception;
}

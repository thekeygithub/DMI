package com.ts.controller.app.SearchAPI.PaymentAPI.InformationSynthesis;

import org.springframework.stereotype.Controller;

import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.app.SearchAPI.PaymentAPI.util.impl.CitizenCardQueryImpl;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_inte_info;
import com.ts.entity.P_user;
import com.ts.entity.CitizenCardQuery.V01InputCitizenCardQueryBean;
import com.ts.entity.CitizenCardQuery.V01OutCitizenCardQueryBean;
import com.ts.util.PayLinkedQueue;
import com.ts.util.Transformation;

import net.sf.json.JSONObject;

/**
 * 
 * ProjectName：API
 * ClassName：CitizenCardQueryPAY
 * Description：TODO(市民卡号查询接口)
 * @Copyright：
 * @Company：
 * @author：Lee 李世博
 * @version 
 * Create Date：2017年3月27日 下午6:00:21
 */
@Controller
public class CitizenCardQueryPAY extends PayRSHandler{

	@Override
	protected Object exceBusiness(JSONObject value) {
		Long l = System.currentTimeMillis();
		//调用接口
		Long ls = System.currentTimeMillis();
		CitizenCardQueryImpl cq = new CitizenCardQueryImpl();
		JSONObject jo = new JSONObject();
		V01InputCitizenCardQueryBean bean = new V01InputCitizenCardQueryBean();
		bean.setV01_INP_NO01(value.getString("V01_INP_NO01"));
		bean.setV01_INP_NO02(value.getString("V01_INP_NO02"));
		V01OutCitizenCardQueryBean out = cq.getCitizenCardQuery(bean);
		BaseController.logBefore(logger, "v01Service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		
		Long le = System.currentTimeMillis();
		jo.put("V01_OUT_NO01", out.getV01_OUT_NO01());
		jo.put("V01_OUT_NO02", out.getV01_OUT_NO02());
		jo.put("V01_OUT_NO03", out.getV01_OUT_NO03());
		P_inte_info pif = new P_inte_info();//接口数据信息
		P_user pu = new P_user();//用户信息		
		//用户信息
//		pu.setID(uID);//主键ID
		pu.setAPI_TYPE(value.getString("apitype")); //业务类型
		pu.setREQ_NO(value.getString("requestno")); //请求流水号
		pu.setTIME_STAMP(value.getString("timestamp")); //时间戳
		pu.setID_CARD(value.getString("V01_INP_NO01")); //身份证号
		pu.setCARD_NO(value.getString("V01_INP_NO02")); //市民卡号
		pu.setIC_CARD(out.getV01_OUT_NO03());//物理卡号
		pu.setNAME(out.getV01_OUT_NO02()); //姓名
		pu.setCARD_STAT(Integer.parseInt(Transformation.toCast(out.getV01_OUT_NO01(), Integer.class))); //卡状态
		pu.setCREATE_DATE(Transformation.getDate()); //创建日期
		pif.setPUser(pu);//用户信息子集
		PayLinkedQueue.setQueuePay(pif);
		BaseController.logBefore(logger, "v01---组装数据加入队列>>>执行时间:" +  (System.currentTimeMillis() - le ));
		BaseController.logBefore(logger, "v01--->>>执行时间:" +  (System.currentTimeMillis() - l ));
		return jo;
	}
}

package com.ts.controller.app.SearchAPI.PaymentAPI.TransactionIntegration;


import org.springframework.stereotype.Controller;

import com.ts.CXFClient.I49InputTradeConfirmBean;
import com.ts.CXFClient.I49OutputTradeConfirmBean;
import com.ts.CXFClient.MedicarePayment;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_insu_info;
import com.ts.entity.P_inte_info;
import com.ts.entity.P_user;
import com.ts.util.PayLinkedQueue;
import com.ts.util.Transformation;
import com.ts.util.CXFClientFactory.CXFClientFactory;

import net.sf.json.JSONObject;

@Controller
public class TradeConfirmPayBusiness extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		I49OutputTradeConfirmBean outModel = new I49OutputTradeConfirmBean();
		try{
			
			//MedicarePaymentService service = new MedicarePaymentService();
			//MedicarePayment function = service.getMedicarePaymentPort();
			Long ls = System.currentTimeMillis();
			I49InputTradeConfirmBean inModel = new I49InputTradeConfirmBean();
			inModel = (I49InputTradeConfirmBean) tf.setParam(inModel, value);
			MedicarePayment function = CXFClientFactory.FactoryMedicarePayment();
			outModel = function.getTradeConfirm(inModel);
			BaseController.logBefore(logger, "I49service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
			//===================================== start 将入参和出参放进实体bean中  start =========================================
			Long le = System.currentTimeMillis();
			P_inte_info pif = new P_inte_info();//接口数据信息
				
			//用户信息
			P_user pu = new P_user();
			pu.setAPI_TYPE(value.getString("apitype")); //业务类型
			pu.setREQ_NO(value.getString("requestno")); //请求流水号
			pu.setTIME_STAMP(value.getString("timestamp")); //时间戳
			pu.setID_CARD(value.getString("I49_INP_NO03")); //身份证号
			pu.setCARD_NO(value.getString("I49_INP_NO02")); //市民卡号
			pu.setIC_CARD(value.getString("I49_INP_NO10"));//物理卡号
			pu.setCREATE_DATE(Transformation.getDate()); //创建日期
			pif.setPUser(pu);//用户信息子集
			
			//参保人员信息
			P_insu_info pii = new P_insu_info();
			pii.setAPI_TYPE(value.getString("apitype"));//业务接口编号
			pii.setREQ_NO(value.getString("requestno"));//请求流水号
			pii.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
			pii.setBANK_NO(value.getString("I49_INP_NO05"));//银行卡信息
			pii.setHOS_CODE(value.getString("I49_INP_NO01"));//医院编码
			pii.setDEAL_STAT(Integer.parseInt(Transformation.toCast(outModel.getI49OUTNO01(), Integer.class)));//交易状态
			pii.setERROR(outModel.getI49OUTNO02());//错误信息
			pii.setCARD_RES(outModel.getI49OUTNO03());//写社会保障卡结果
			pii.setBANK_RES(outModel.getI49OUTNO04());//扣银行卡结果
			pii.setIC_DATA(outModel.getI49OUTNO05());//更新后IC卡数据
			pu.setPInsuInfo(pii);//参保人员子集
			
			//接口信息
			pif.setUSER_ID(pu.getID());//用户信息ID	
			pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
			pif.setREQ_NO(value.getString("requestno"));//请求流水号
			pif.setDATA_TYPE(9);//数据类型：1门诊预结算，2门诊结算，3门诊退费退号，4住院预结算，5出院结算，6住院明细查询，7交易结果查询，8参保人结果及享受状态查询，9确认交易，10获取参保人信息接口
			pif.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
			pif.setHOS_CODE(value.getString("I49_INP_NO01"));//医院编码
			pif.setPAY_TYPE(Integer.parseInt(Transformation.toCast(value.getString("I49_INP_NO04"),Integer.class)));//现金支付方式
			pif.setBANK_NO(value.getString("I49_INP_NO05"));//银行卡信息
			pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(outModel.getI49OUTNO01(), Integer.class)));//交易状态
			pif.setERROR(outModel.getI49OUTNO02());//错误信息
			pif.setCARD_RES(outModel.getI49OUTNO03());//写社会保障卡结果
			pif.setBANK_RES(outModel.getI49OUTNO04());//扣银行卡结果
			pif.setIC_DATA(outModel.getI49OUTNO05());//更新后IC卡数据
			pif.setDEAL_TYPE(value.getString("I49_INP_NO06"));//交易类型
			pif.setMED_DEAL_NO(value.getString("I49_INP_NO07"));//医保交易流水号
			pif.setDEAL_NO(outModel.getI49OUTNO06());//交易流水号
			pif.setHOS_RES(value.getString("I49_INP_NO08"));//掌医事务结果
			pif.setADD_INFO(value.getString("I49_INP_NO09"));//附加消息
			pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
			
			//实体放入队列
			PayLinkedQueue.setQueuePay(pif);
			BaseController.logBefore(logger, "I49---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		}catch(Exception e){
			BaseController.logBefore(logger, "错误信息:" +  e.getMessage());
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "I49--->>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson(outModel);
	}
	
}
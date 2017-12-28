package com.ts.controller.app.SearchAPI.PaymentAPI.Drugstore;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.DrugstorePayment.D02InputSettlementBean;
import com.ts.CXFClient.DrugstorePayment.D02OutResultInfoBean;
import com.ts.CXFClient.DrugstorePayment.D02OutputSettlementBean;
import com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_drugstore_inte_info;
import com.ts.util.PayLinkedQueue_Drugstore;
import com.ts.util.Transformation;
import com.ts.util.CXFClientFactory.CXFClientFactory_Drugstore;

import net.sf.json.JSONObject;

/**
 * D01药店结算接口
 * @ClassName:DrugstoreSettlementPAY
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月20日上午10:33:07
 */
@Controller
public class DrugstoreSettlementPAY extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		//调用接口
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		D02OutputSettlementBean out = new D02OutputSettlementBean();
		try {
			Long ls = System.currentTimeMillis();
		DrugstorePaymentServiceSoapBindingStub service = CXFClientFactory_Drugstore.FactoryDrugstorePayment();
		D02InputSettlementBean bean = new D02InputSettlementBean();
		bean = (D02InputSettlementBean) tf.setParam_arr(bean, value);
		out = service.getSettlement(bean);
		BaseController.logBefore(logger, "D02service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		Long le = System.currentTimeMillis();
		P_drugstore_inte_info pif = new P_drugstore_inte_info();//接口数据信息
		
		//单据信息
		D02OutResultInfoBean[] lrbs = out.getD02_OUT_SUB01();
		if(lrbs!=null){
			for (D02OutResultInfoBean obean : lrbs) {
				pif.setRECEIPT_NO(obean.getD02_OUT_SUB01_NO01());//药店收据号
			}
		}
		//接口信息
		
		pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
		pif.setREQ_NO(value.getString("requestno"));//请求流水号
		pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
		pif.setSERVICE_NAME(value.getString("D02_INP_NO00"));//服务机构名称
		
		pif.setDATA_TYPE(72);//数据类型 71 药店预结算 72药店结算73药店预退费74 药店退费75药店对账
		pif.setTIME_STAMP(value.getString("timestamp"));//时间戳
		pif.setSERVICE_NO(bean.getD02_INP_NO01());//服务机构编号
		pif.setIC_CARD(bean.getD02_INP_NO02());//IC卡卡号
		pif.setWORK_NO(bean.getD02_INP_NO03());//工号
		pif.setPERS_NO(bean.getD02_INP_NO04());//个人编号
		pif.setMEDICARE_NO(bean.getD02_INP_NO05());//医保流水号
		//药店收据号在返回参数列表中已经设置
		pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getD02_OUT_NO01(),Integer.class)));//调用状态：0成功，小于0失败
		pif.setDEAL_ERROR_CODE(out.getD02_OUT_NO02());//调用状态小于0，错误代码
		pif.setDEAL_ERROR_INFO(out.getD02_OUT_NO03());//调用状态小于0，错误信息
		pif.setSERVICE_ERROR(out.getD02_OUT_NO04());//调用服务返回的错误信息
		//实体放入队列
		PayLinkedQueue_Drugstore.setQueuePay(pif);
		BaseController.logBefore(logger, "D02---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "D02---->>>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson_arr(out);
	}

	
}
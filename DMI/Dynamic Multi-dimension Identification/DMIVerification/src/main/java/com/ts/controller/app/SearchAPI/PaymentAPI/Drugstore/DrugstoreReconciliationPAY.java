package com.ts.controller.app.SearchAPI.PaymentAPI.Drugstore;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.DrugstorePayment.D05InputReconciliationBean;
import com.ts.CXFClient.DrugstorePayment.D05OutResultInfoBean;
import com.ts.CXFClient.DrugstorePayment.D05OutputReconciliationBean;
import com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_drugstore_inte_info;
import com.ts.util.PayLinkedQueue_Drugstore;
import com.ts.util.Transformation;
import com.ts.util.CXFClientFactory.CXFClientFactory_Drugstore;

import net.sf.json.JSONObject;

/**
 * D05药店对账接口
 * @ClassName:DrugstoreReconciliationPAY
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月20日上午11:04:13
 */
@Controller
public class DrugstoreReconciliationPAY extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		//调用接口
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		D05OutputReconciliationBean out = new D05OutputReconciliationBean();
		try {
			Long ls = System.currentTimeMillis();
		DrugstorePaymentServiceSoapBindingStub service = CXFClientFactory_Drugstore.FactoryDrugstorePayment();
		D05InputReconciliationBean bean = new D05InputReconciliationBean();
		bean = (D05InputReconciliationBean) tf.setParam_arr(bean, value);
		out = service.getReconciliation(bean);
		BaseController.logBefore(logger, "D05service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		Long le = System.currentTimeMillis();
		P_drugstore_inte_info pif = new P_drugstore_inte_info();//接口数据信息
		
		D05OutResultInfoBean[] lrbs = out.getD05_OUT_SUB01();//对账信息
		if(lrbs!=null){
			for (D05OutResultInfoBean obean : lrbs) {
				String no01=obean.getD05_OUT_SUB01_NO01();//1预结算 2结算 3预退费 4退费
				//1预结算时为空 2结算时返回药店收据号 3预退费时为空 4退费时返回负单收据号
				String info="";
				switch (no01) {
				case "1":
					info="预结算";
					break;
				case "2":
					info="结算";
					pif.setRECEIPT_NO(obean.getD05_OUT_SUB01_NO02());			
					break;
				case "3":
					info="预退费";
					break;
				case "4":
					info="退费";
					pif.setREFUND_NO(obean.getD05_OUT_SUB01_NO02());
					break;
				default:
					info=no01+"状态有误";
					break;
				}
				pif.setADD_INFO("流水号:"+bean.getD05_INP_NO03()+",当前的状态:"+info);
			}
		}
		
		//接口信息
		pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
		pif.setREQ_NO(value.getString("requestno"));//请求流水号
		pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
		pif.setSERVICE_NAME(value.getString("D05_INP_NO00"));//服务机构名称
		
		pif.setDATA_TYPE(75);//数据类型 71 药店预结算 72药店结算73药店预退费74 药店退费75药店对账
		pif.setTIME_STAMP(value.getString("timestamp"));//时间戳
		pif.setSERVICE_NO(bean.getD05_INP_NO01());//服务机构编号
		pif.setPERS_NO(bean.getD05_INP_NO02());//个人编号
		pif.setMEDICARE_NO(bean.getD05_INP_NO03());//医保流水号
		pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getD05_OUT_NO01(),Integer.class)));//调用状态：0成功，小于0失败
		pif.setDEAL_ERROR_CODE(out.getD05_OUT_NO02());//调用状态小于0，错误代码
		pif.setDEAL_ERROR_INFO(out.getD05_OUT_NO03());//调用状态小于0，错误信息
		pif.setSERVICE_ERROR(out.getD05_OUT_NO04());//调用服务返回的错误信息
		//实体放入队列
		PayLinkedQueue_Drugstore.setQueuePay(pif);
		BaseController.logBefore(logger, "D05---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "D05---->>>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson_arr(out);
	}

	
}
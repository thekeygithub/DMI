package com.ts.controller.app.SearchAPI.PaymentAPI.Drugstore;


import org.springframework.stereotype.Controller;

import com.ts.CXFClient.DrugstorePayment.D06InListDrugInfoBean;
import com.ts.CXFClient.DrugstorePayment.D06InputPreRefundBean;
import com.ts.CXFClient.DrugstorePayment.D06OutResultInfoBean;
import com.ts.CXFClient.DrugstorePayment.D06OutputPreRefundBean;
import com.ts.CXFClient.DrugstorePayment.DrugstorePaymentServiceSoapBindingStub;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_drugstore_drug_item;
import com.ts.entity.P_drugstore_inte_info;
import com.ts.entity.P_drugstore_result;
import com.ts.util.PayLinkedQueue_Drugstore;
import com.ts.util.Transformation;
import com.ts.util.CXFClientFactory.CXFClientFactory_Drugstore;

import net.sf.json.JSONObject;

/**
 * D06药店预退费接口
 * @ClassName:DrugstorePreRefundPAY
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年10月25日上午10:49:44
 */
@Controller
public class DrugstorePreRefundPAY extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		//调用接口
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		D06OutputPreRefundBean out = new D06OutputPreRefundBean();
		try {
		Long ls = System.currentTimeMillis();
		DrugstorePaymentServiceSoapBindingStub service = CXFClientFactory_Drugstore.FactoryDrugstorePayment();
		D06InputPreRefundBean bean = new D06InputPreRefundBean();
		bean = (D06InputPreRefundBean) tf.setParam_arr(bean, value);
		out = service.getPreRefund(bean);
		BaseController.logBefore(logger, "D06service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		Long le = System.currentTimeMillis();
		P_drugstore_inte_info pif = new P_drugstore_inte_info();//接口数据信息
		
		//药品信息
		D06InListDrugInfoBean[] ldbs=bean.getD06_INP_LIST01();
		
		for (D06InListDrugInfoBean ibean : ldbs) {
			P_drugstore_drug_item d=new P_drugstore_drug_item();
			d.setCODE(ibean.getD06_INP_LIST01_NO01());//医疗服务机构端收费项目编码
			d.setUNIT_PRICE(Double.parseDouble(Transformation.toCast(ibean.getD06_INP_LIST01_NO02(), Double.class)));// 	服务机构药品单价
			d.setCOUNT(Double.parseDouble(Transformation.toCast(ibean.getD06_INP_LIST01_NO03(), Double.class)));//服务机构药品数量
			
			pif.addpDrug(d);//添加药品的信息
		}
		//计算结果信息
		D06OutResultInfoBean[] lrbs = out.getD06_OUT_SUB01();
		if(lrbs!=null){
			for (D06OutResultInfoBean obean : lrbs) {
				pif.setMEDICARE_NO(obean.getD06_OUT_SUB01_NO01());//负单医保流水号
				
				P_drugstore_result dr=new P_drugstore_result();
				dr.setRECEIPT_COUNT(Integer.parseInt(Transformation.toCast(obean.getD06_OUT_SUB01_NO02(),Integer.class)));//收据张数
				dr.setLAR_PAY(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO03() , Double.class)));//大额补助支付金额
				dr.setPLAN_PAY(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO04() , Double.class)));//统筹金支付金额
				dr.setSER_PAY(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO05() , Double.class)));//公务员补助支付金额
				dr.setSELF_ACCOUNT_PAY(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO06() , Double.class)));//个人帐户支付金额
				dr.setSELF_CASH_PAY(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO07() , Double.class)));//个人现金支付
				dr.setB_BALANCE(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO08() , Double.class)));//结算前帐户余额
				dr.setA_BALANCE(Double.parseDouble(Transformation.toCast(obean.getD06_OUT_SUB01_NO09() , Double.class)));//结算后帐户余额
				
				pif.addpReuslt(dr);
			}
		}
		
		//接口信息
		//医保流水号在返回的参数中已经设置
		pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
		pif.setREQ_NO(value.getString("requestno"));//请求流水号
		pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
		pif.setSERVICE_NAME(value.getString("D06_INP_NO00"));//服务机构名称
		
		pif.setDATA_TYPE(76);//数据类型 71 药店预结算 72药店结算73药店退费校验74 药店退费75药店对账76药店预退费
		pif.setTIME_STAMP(value.getString("timestamp"));//时间戳
		pif.setSERVICE_NO(bean.getD06_INP_NO01());//服务机构编号
		pif.setIC_CARD(bean.getD06_INP_NO02());//IC卡卡号
		pif.setWORK_NO(bean.getD06_INP_NO03());//工号
		pif.setPERS_NO(bean.getD06_INP_NO04());//个人编号
		pif.setRECEIPT_NO(bean.getD06_INP_NO05());//药店收据号
		pif.setBUSI_TYPE(Integer.parseInt(Transformation.toCast(bean.getD06_INP_NO06(),Integer.class)));//医疗类别
		pif.setCASHIER_NO(bean.getD06_INP_NO07());//收款员编号
		pif.setRECEIPT_TYPE(Integer.parseInt(Transformation.toCast(bean.getD06_INP_NO08(),Integer.class)));//药店收据类型
		pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getD06_OUT_NO01(),Integer.class)));//调用状态：0成功，小于0失败
		pif.setDEAL_ERROR_CODE(out.getD06_OUT_NO02());//调用状态小于0，错误代码
		pif.setDEAL_ERROR_INFO(out.getD06_OUT_NO03());//调用状态小于0，错误信息
		pif.setSERVICE_ERROR(out.getD06_OUT_NO04());//调用服务返回的错误信息
		//实体放入队列
		PayLinkedQueue_Drugstore.setQueuePay(pif);
		BaseController.logBefore(logger, "D06---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "D06---->>>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson_arr(out);
	}

	
}
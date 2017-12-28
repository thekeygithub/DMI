package com.ts.controller.app.SearchAPI.PaymentAPI.Drugstore;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.DrugstorePayment.D01InListDrugInfoBean;
import com.ts.CXFClient.DrugstorePayment.D01InputPreSettlementBean;
import com.ts.CXFClient.DrugstorePayment.D01OutResultInfoBean;
import com.ts.CXFClient.DrugstorePayment.D01OutputPreSettlementBean;
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
 * D01药店预结算接口
 * @ClassName:DrugstorePreSettlementPAY
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月15日上午9:55:13
 */
@Controller
public class DrugstorePreSettlementPAY extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		//调用接口
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		D01OutputPreSettlementBean out = new D01OutputPreSettlementBean();
		try {
			Long ls = System.currentTimeMillis();
		DrugstorePaymentServiceSoapBindingStub service = CXFClientFactory_Drugstore.FactoryDrugstorePayment();
		D01InputPreSettlementBean bean = new D01InputPreSettlementBean();
		bean = (D01InputPreSettlementBean) tf.setParam_arr(bean, value);
		out = service.getPreSettlement(bean);
		BaseController.logBefore(logger, "D01service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		Long le = System.currentTimeMillis();
		P_drugstore_inte_info pif = new P_drugstore_inte_info();//接口数据信息
		
		//药品信息
		D01InListDrugInfoBean[] ldbs=bean.getD01_INP_LIST01();
		
		for (D01InListDrugInfoBean ibean : ldbs) {
			P_drugstore_drug_item d=new P_drugstore_drug_item();
			d.setTYPE(Integer.parseInt(Transformation.toCast(ibean.getD01_INP_LIST01_NO01(), Integer.class)));//服务结构收费类别编码1西药费2中成药费用3中草药费
			d.setCODE(ibean.getD01_INP_LIST01_NO02());//服务结构药品编号
			d.setNAME(ibean.getD01_INP_LIST01_NO03());//服务机构药品名称
			d.setCENT_CODE(ibean.getD01_INP_LIST01_NO04());//服务机构药品中心端编号
			d.setCENT_NAME(ibean.getD01_INP_LIST01_NO05());// 	服务机构药品中心端名称
			d.setUNIT_PRICE(Double.parseDouble(Transformation.toCast(ibean.getD01_INP_LIST01_NO06(), Double.class)));// 	服务机构药品单价
			d.setCOUNT(Double.parseDouble(Transformation.toCast(ibean.getD01_INP_LIST01_NO07(), Double.class)));//服务机构药品数量
			d.setPRICE(Double.parseDouble(Transformation.toCast(ibean.getD01_INP_LIST01_NO08(), Double.class)));//服务机构药品金额
			d.setFORM_NO(ibean.getD01_INP_LIST01_NO09());// 	服务机构药品剂型编码（药品时填写）
			d.setDOSE(ibean.getD01_INP_LIST01_NO10());// 	服务机构药品每次用量
			d.setFREQUENCY(ibean.getD01_INP_LIST01_NO11());// 	服务机构药品使用频次
			d.setDAYS(Double.parseDouble(Transformation.toCast(ibean.getD01_INP_LIST01_NO12(), Double.class)));// 	服务机构药品执行天数
			
			pif.addpDrug(d);//添加药品的信息
		}
		//计算结果信息
		D01OutResultInfoBean[] lrbs = out.getD01_OUT_SUB01();
		if(lrbs!=null){
			for (D01OutResultInfoBean obean : lrbs) {
				pif.setMEDICARE_NO(obean.getD01_OUT_SUB01_NO01());//医保流水号
				
				P_drugstore_result dr=new P_drugstore_result();
				dr.setRECEIPT_COUNT(Integer.parseInt(Transformation.toCast(obean.getD01_OUT_SUB01_NO02(),Integer.class)));//收据张数
				dr.setLAR_PAY(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO03() , Double.class)));//大额补助支付金额
				dr.setPLAN_PAY(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO04() , Double.class)));//统筹金支付金额
				dr.setSER_PAY(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO05() , Double.class)));//公务员补助支付金额
				dr.setSELF_ACCOUNT_PAY(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO06() , Double.class)));//个人帐户支付金额
				dr.setSELF_CASH_PAY(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO07() , Double.class)));//个人现金支付
				dr.setB_BALANCE(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO08() , Double.class)));//结算前帐户余额
				dr.setA_BALANCE(Double.parseDouble(Transformation.toCast(obean.getD01_OUT_SUB01_NO09() , Double.class)));//结算后帐户余额
				dr.setINFO(obean.getD01_OUT_SUB01_NO10());//药店预结算提示
				
				pif.addpReuslt(dr);
			}
		}
		
		//接口信息
		//医保流水号在返回的参数中已经设置
		pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
		pif.setREQ_NO(value.getString("requestno"));//请求流水号
		pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
		pif.setSERVICE_NAME(value.getString("D01_INP_NO00"));//服务机构名称
		
		pif.setDATA_TYPE(71);//数据类型 71 药店预结算 72药店结算73药店预退费74 药店退费75药店对账
		pif.setTIME_STAMP(value.getString("timestamp"));//时间戳
		pif.setSERVICE_NO(bean.getD01_INP_NO01());//服务机构编号
		pif.setIC_CARD(bean.getD01_INP_NO02());//IC卡卡号
		pif.setWORK_NO(bean.getD01_INP_NO03());//工号
		pif.setPERS_NO(bean.getD01_INP_NO04());//个人编号
		pif.setBUSI_TYPE(Integer.parseInt(Transformation.toCast(bean.getD01_INP_NO05(),Integer.class)));//医疗类别
		pif.setCASHIER_NO(bean.getD01_INP_NO06());//收款员编号
		pif.setCASHIER_NAME(bean.getD01_INP_NO07());//收款员姓名
		pif.setBILL_DOC(bean.getD01_INP_NO08());//开单医生
		pif.setMEDICAL_DEPT(bean.getD01_INP_NO09());//就诊科室
		pif.setMEDICAL_NAME(bean.getD01_INP_NO10());//诊断疾病名称
		pif.setRECEIPT_TYPE(Integer.parseInt(Transformation.toCast(bean.getD01_INP_NO11(),Integer.class)));//药店收据类型
		pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getD01_OUT_NO01(),Integer.class)));//调用状态：0成功，小于0失败
		pif.setDEAL_ERROR_CODE(out.getD01_OUT_NO02());//调用状态小于0，错误代码
		pif.setDEAL_ERROR_INFO(out.getD01_OUT_NO03());//调用状态小于0，错误信息
		pif.setSERVICE_ERROR(out.getD01_OUT_NO04());//调用服务返回的错误信息
		//实体放入队列
		PayLinkedQueue_Drugstore.setQueuePay(pif);
		BaseController.logBefore(logger, "D01---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "D01---->>>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson_arr(out);
	}

	
}
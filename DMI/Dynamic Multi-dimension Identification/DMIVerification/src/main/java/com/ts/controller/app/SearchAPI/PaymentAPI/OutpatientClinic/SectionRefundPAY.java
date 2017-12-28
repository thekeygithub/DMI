package com.ts.controller.app.SearchAPI.PaymentAPI.OutpatientClinic;


import java.util.List;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.I31InputSectionRefundBean;
import com.ts.CXFClient.I31OutCalculationResultInfoBean;
import com.ts.CXFClient.I31OutSegmentedInfoStructureBean;
import com.ts.CXFClient.I31OutputSectionRefundBean;
import com.ts.CXFClient.MedicarePayment;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_fund;
import com.ts.entity.P_inte_info;
import com.ts.entity.P_result;
import com.ts.entity.P_user;
import com.ts.util.PayLinkedQueue;
import com.ts.util.Transformation;
import com.ts.util.CXFClientFactory.CXFClientFactory;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: SectionRefundPAY 
* @Description: TODO(I31----门诊挂号退费接口) 
* @author Lee 李世博
* @date 2017年3月22日 下午8:44:55 
*
 */
@Controller
public class SectionRefundPAY extends PayRSHandler{
	
	@Override
	protected Object exceBusiness(JSONObject value){
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		I31OutputSectionRefundBean out = new I31OutputSectionRefundBean();
		try{
			Long ls = System.currentTimeMillis();
//		MedicarePaymentService service = new MedicarePaymentService();
		MedicarePayment service = CXFClientFactory.FactoryMedicarePayment();
		I31InputSectionRefundBean bean = new I31InputSectionRefundBean();
		bean = (I31InputSectionRefundBean) tf.setParam(bean, value);
		out = service.getSectionRefund(bean);	
		BaseController.logBefore(logger, "I31service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		Long le = System.currentTimeMillis();
		P_inte_info pif = new P_inte_info();//接口数据信息
		P_user pu = new P_user();//用户信息
		
			//用户信息
			pu.setAPI_TYPE(value.getString("apitype")); //业务类型
			pu.setREQ_NO(value.getString("requestno")); //请求流水号
			pu.setTIME_STAMP(value.getString("timestamp")); //时间戳
			pu.setID_CARD(bean.getI31INPNO03()); //身份证号
			pu.setCARD_NO(bean.getI31INPNO02()); //市民卡号
			pu.setIC_CARD(bean.getI31INPNO08());//物理卡号
			pu.setCREATE_DATE(Transformation.getDate()); //创建日期
			pif.setPUser(pu);//用户信息子集
	
			//基金分段信息
			List<I31OutSegmentedInfoStructureBean> pfds = out.getI31OUTSUB02();
			for(I31OutSegmentedInfoStructureBean sis:pfds){
				P_fund pfd = new P_fund();//基金分段信息
				pfd.setCODE(sis.getI31OUTSUB02NO01());//分段编码
				pfd.setNAME(sis.getI31OUTSUB02NO02());//分段名称
				pfd.setAMOUNT(Double.parseDouble(Transformation.toCast(sis.getI31OUTSUB02NO03() , Double.class)));//进入额度	
				pfd.setPAY_AMOUNT(Double.parseDouble(Transformation.toCast(sis.getI31OUTSUB02NO04() , Double.class)));//分段基金支付金额
				pfd.setRATIO(Double.parseDouble(Transformation.toCast(sis.getI31OUTSUB02NO05() ,Double.class)));//报销比例
				pfd.setSELF_AMOUNT(Double.parseDouble(Transformation.toCast(sis.getI31OUTSUB02NO06() , Double.class)));//分段自负金额
				pfd.setNEG_AMOUNT(Double.parseDouble(Transformation.toCast(sis.getI31OUTSUB02NO07() , Double.class)));//分段自费金额
				pif.addpFund(pfd);//基金分段子集
				
			}
			
			List<I31OutCalculationResultInfoBean>  sub01= out.getI31OUTSUB01();
			for( I31OutCalculationResultInfoBean cris : sub01){
				P_result pr = new P_result();//计算结果信息
				//计算结果信息
				pr.setTOTAL_FEE(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO01() , Double.class)));//费用总额
				pr.setSELF_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO02() , Double.class)));//自费总额(非医保)
				pr.setSELF_NEG(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO03() , Double.class)));//药品乙类自负
				pr.setMED_FEE(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO04() , Double.class)));//医保费用
				pr.setTRAN_FEE(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO05() , Double.class)));//转院自费
				pr.setLEVEL_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO06() , Double.class)));//起付线
				pr.setSELF_CASH_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO07() , Double.class)));//个人自费现金支付
				pr.setNEG_CASH_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO08() , Double.class)));//个人自负现金支付
				pr.setCASH_TOTAL(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO09() , Double.class)));//合计现金支付
				pr.setRETURN_FEE(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO10() , Double.class)));//合计报销金额
				pr.setI_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO11() , Double.class)));//历年帐户支付
				pr.setO_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO12() , Double.class)));//当年帐户支付
				pr.setWHOLE_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO13() , Double.class)));//保统筹基金支付
				pr.setADD_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO14() , Double.class)));//补充保险支付
				pr.setSER_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO15() , Double.class)));//公务员补助支付
				pr.setCOM_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO16() , Double.class)));//单位补助支付
				pr.setSALV_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO17() , Double.class)));//医疗救助支付
				pr.setRETIRE_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO18() , Double.class)));//离休基金支付
				pr.setFUND_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO19() , Double.class)));//二乙基金支付
				pr.setMODEL_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO20() , Double.class)));//劳模医疗补助支付
				pr.setCIVIL_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO21() , Double.class)));//民政救助支付
				pr.setPRIV_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO22() , Double.class)));//优抚救助支付
				pr.setDPF_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO23() , Double.class)));//残联基金支付
				pr.setPLAN_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO24() , Double.class)));//计生基金支付
				pr.setHURT_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO25() , Double.class)));//工伤基金支付
				pr.setPROC_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO26() , Double.class)));//生育基金支付
				pr.setI_BALANCE(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO27() , Double.class)));//结算后当年个帐余额
				pr.setO_BALANCE(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO28() , Double.class)));//结算后历年个帐余额
				pr.setINSU_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO29() , Double.class)));//合保统筹基金支付
				pr.setINSU_SALV_PAY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO30() , Double.class)));//合保大病救助支付
				pr.setCHARITY(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO31() , Double.class)));//慈善基金
//				pr.setMUTUAL(Double.parseDouble(Transformation.toCast(cris.getI31OUTSUB01NO32() , Double.class)));//共济账户支出
	
				pif.addPReuslt(pr);//计算结果子集
			}

			
			//接口信息
//			pif.setUSER_ID(pu.getID());//用户信息ID
			pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
			pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
			pif.setREQ_NO(value.getString("requestno"));//请求流水号
			pif.setDATA_TYPE(3);//数据类型
			pif.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
			pif.setHOS_CODE(bean.getI31INPNO01());//医院编码
			pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getI31OUTNO01() , Integer.class)));//交易状态
			pif.setERROR(out.getI31OUTNO02());//错误信息
			pif.setCARD_RES(out.getI31OUTNO03());//写社会保障卡结果
			pif.setBANK_RES(out.getI31OUTNO04());//扣银行卡结果
			pif.setIC_DATA(out.getI31OUTNO05());//更新后IC卡数据
			pif.setIS_REPET(Integer.parseInt(Transformation.toCast(out.getI31OUTNO06() , Integer.class)));//是否重复退费
			pif.setRETURN_NO(out.getI31OUTNO07());//退费交易流水号
			pif.setRETURN_DATE(Transformation.toDate(out.getI31OUTNO08()));//退费结算日期  
			pif.setPAY_TYPE(Integer.parseInt(Transformation.toCast(bean.getI31INPNO04(),Integer.class)));//现金支付方式
			pif.setBANK_NO(bean.getI31INPNO05());//银行卡信息
			pif.setABO_DEAL_NO(bean.getI31INPNO06());//要作废的结算交易号
			pif.setOPERATOR(bean.getI31INPNO07());//经办人
			//实体放入队列
			PayLinkedQueue.setQueuePay(pif);
			BaseController.logBefore(logger, "I31---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "I31--->>>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson(out);
	}

}
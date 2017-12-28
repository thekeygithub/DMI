package com.ts.controller.app.SearchAPI.PaymentAPI.TransactionIntegration;

import java.util.List;



import org.springframework.stereotype.Controller;

import com.ts.CXFClient.I43InputTradeResultsQueryBean;
import com.ts.CXFClient.I43OutCalculationResultBean;
import com.ts.CXFClient.I43OutCostSummaryBean;
import com.ts.CXFClient.I43OutFundSegmentBean;
import com.ts.CXFClient.I43OutputTradeResultsQueryBean;
import com.ts.CXFClient.MedicarePayment;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_fee;
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
* @ClassName: TradeResultsQueryPAY 
* @Description: TODO(I43 --- 交易结果查询接口) 
* @author Lee 李世博
* @date 2017年3月22日 下午8:45:14 
*
 */
@Controller
public class TradeResultsQueryPAY extends PayRSHandler {
	
	@Override
	protected Object exceBusiness(JSONObject value) {
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		I43OutputTradeResultsQueryBean out = new I43OutputTradeResultsQueryBean();
		
		try{
			
			Long  ls = System.currentTimeMillis();
			MedicarePayment service = CXFClientFactory.FactoryMedicarePayment();
			I43InputTradeResultsQueryBean bean = new I43InputTradeResultsQueryBean();
			bean = (I43InputTradeResultsQueryBean) tf.setParam(bean, value);
			out = service.getTradeResultsQuery(bean);
			MedicarePayment function = CXFClientFactory.FactoryMedicarePayment();
			out = function.getTradeResultsQuery(bean);	
			BaseController.logBefore(logger, "I43service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
			//===================================== start 将入参和出参放进实体bean中  start =========================================
			Long le = System.currentTimeMillis();
			P_inte_info pif = new P_inte_info();//接口数据信息
			//用户信息
			P_user pu = new P_user();
			pu.setAPI_TYPE(value.getString("apitype")); //业务类型
			pu.setREQ_NO(value.getString("requestno")); //请求流水号
			pu.setTIME_STAMP(value.getString("timestamp")); //时间戳
			pu.setID_CARD(value.getString("I43_INP_NO03")); //身份证号
			pu.setCARD_NO(value.getString("I43_INP_NO02")); //市民卡号
			pu.setIC_CARD(value.getString("I43_INP_NO08"));//物理卡号
			pu.setCREATE_DATE(Transformation.getDate()); //创建日期
			pif.setPUser(pu);//用户信息子集
			//接口信息
			pif.setUSER_ID(pu.getID());//用户信息ID	
			pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
			pif.setREQ_NO(value.getString("requestno"));//请求流水号
			pif.setDATA_TYPE(7);//数据类型：1门诊预结算，2门诊结算，3门诊退费退号，4住院预结算，5出院结算，6住院明细查询，7交易结果查询，8参保人结果及享受状态查询，9确认交易，10获取参保人信息接口
			pif.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
			pif.setHOS_CODE(value.getString("I43_INP_NO01"));//医院编码
			pif.setPAY_TYPE(Integer.parseInt(Transformation.toCast(value.getString("I43_INP_NO04"),Integer.class)));//现金支付方式
			pif.setBANK_NO(bean.getI43INPNO05());//银行卡信息
			pif.setSEARCH_TYPE_NO(value.getString("I43_INP_NO06"));//待查询用户交易类型号
			pif.setSEARCH_DEAL_NO(value.getString("I43_INP_NO07"));//待查询的用户交易流水号
			pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getI43OUTNO01(), Integer.class)));//交易状态
			pif.setERROR(out.getI43OUTNO02());//错误信息
			pif.setCARD_RES(out.getI43OUTNO03());//写社会保障卡结果
			pif.setBANK_RES(out.getI43OUTNO04());//扣银行卡结果
			pif.setIC_DATA(out.getI43OUTNO05());//更新后IC卡数据
			pif.setIS_OK(Integer.parseInt(Transformation.toCast(out.getI43OUTNO06(), Integer.class)));//用户交易是否成功
			pif.setDEAL_DATE(Transformation.toDate(out.getI43OUTNO07()));//交易时间
			pif.setDEAL_FINAL_NO(out.getI43OUTNO08());//交易结算流水号
			pif.setDEAL_STEP(out.getI43OUTNO09());//交易处于阶段
			pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
			//计算结果信息
			List<I43OutCalculationResultBean> resList = out.getI43OUTSUB01();
			for(I43OutCalculationResultBean resObj : resList){
				P_result pr = new P_result();
				pr.setTOTAL_FEE(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO01(), Double.class)));//费用总额
				pr.setSELF_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO02(), Double.class)));//自费总额(非医保)
				pr.setSELF_NEG(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO03(), Double.class)));//药品乙类自负
				pr.setMED_FEE(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO04(), Double.class)));//医保费用
				pr.setTRAN_FEE(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO05(), Double.class)));//转院自费
				pr.setLEVEL_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO06(), Double.class)));//起付线
				pr.setSELF_CASH_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO07(), Double.class)));//个人自费现金支付
				pr.setNEG_CASH_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO08(), Double.class)));//个人自负现金支付
				pr.setCASH_TOTAL(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO09(), Double.class)));//合计现金支付
				pr.setRETURN_FEE(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO10(), Double.class)));//合计报销金额
				pr.setI_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO11(), Double.class)));//历年帐户支付
				pr.setO_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO12(), Double.class)));//当年帐户支付
				pr.setWHOLE_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO13(), Double.class)));//职保统筹基金支付
				pr.setADD_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO14(), Double.class)));//补充保险支付
				pr.setSER_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO15(), Double.class)));//公务员补助支付
				pr.setCOM_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO16(), Double.class)));//单位补助支付
				pr.setSALV_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO17(), Double.class)));//医疗救助支付
				pr.setRETIRE_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO18(), Double.class)));//离休基金支付
				pr.setFUND_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO19(), Double.class)));//二乙基金支付
				pr.setMODEL_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO20(), Double.class)));//劳模医疗补助支付
				pr.setCIVIL_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO21(), Double.class)));//民政救助支付
				pr.setPRIV_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO22(), Double.class)));//优抚救助支付
				pr.setDPF_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO23(), Double.class)));//残联基金支付
				pr.setPLAN_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO24(), Double.class)));//计生基金支付
				pr.setHURT_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO25(), Double.class)));//工伤基金支付
				pr.setPROC_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO26(), Double.class)));//生育基金支付
				pr.setI_BALANCE(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO27(), Double.class)));//结算后当年个帐余额
				pr.setO_BALANCE(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO28(), Double.class)));//结算后历年个帐余额
				pr.setINSU_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO29(), Double.class)));//合保统筹基金支付
				pr.setINSU_SALV_PAY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO30(), Double.class)));//合保大病救助支付
				pr.setCHARITY(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO31(), Double.class)));//慈善基金
				//pr.setMUTUAL(Double.parseDouble(Transformation.toCast(resObj.getI43OUTSUB01NO32(), Double.class)));//共济账户支出
				pif.addPReuslt(pr);//计算结果子集
			}
			//基金分段信息
			List<I43OutFundSegmentBean> fundList = out.getI43OUTSUB02();
			for(I43OutFundSegmentBean fundObj : fundList){
				P_fund pfd = new P_fund();
				pfd.setCODE(fundObj.getI43OUTSUB02NO01());//分段编码
				pfd.setNAME(fundObj.getI43OUTSUB02NO02());//分段名称
				pfd.setAMOUNT(Double.parseDouble(Transformation.toCast(fundObj.getI43OUTSUB02NO03(), Double.class)));//进入额度
				pfd.setPAY_AMOUNT(Double.parseDouble(Transformation.toCast(fundObj.getI43OUTSUB02NO04(), Double.class)));//分段基金支付金额
				pfd.setRATIO(Double.parseDouble(Transformation.toCast(fundObj.getI43OUTSUB02NO05(), Double.class)));//报销比例
				pfd.setSELF_AMOUNT(Double.parseDouble(Transformation.toCast(fundObj.getI43OUTSUB02NO06(), Double.class)));//分段自负金额
				pfd.setNEG_AMOUNT(Double.parseDouble(Transformation.toCast(fundObj.getI43OUTSUB02NO07(), Double.class)));//分段自费金额
				pif.addpFund(pfd);//基金分段子集
			}
			
			//费用汇总信息
			List<I43OutCostSummaryBean> costList = out.getI43OUTSUB03();
			for(I43OutCostSummaryBean costObj : costList){
				P_fee pf = new P_fee();
				pf.setSORT(costObj.getI43OUTSUB03NO01());//发票归类
				pf.setNAME(costObj.getI43OUTSUB03NO02());//归类名称
				pf.setTOTAL_FEE(Double.parseDouble(Transformation.toCast(costObj.getI43OUTSUB03NO03(), Double.class)));//总费用
				pf.setSELF_FEE(Double.parseDouble(Transformation.toCast(costObj.getI43OUTSUB03NO04(), Double.class)));//自费费用
				pf.setNEG_FEE(Double.parseDouble(Transformation.toCast(costObj.getI43OUTSUB03NO05(), Double.class)));//自负费用
				pif.addpFee(pf);//费用汇总子集
			} 
			//实体放入队列
			PayLinkedQueue.setQueuePay(pif);
			BaseController.logBefore(logger, "I43---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
//			if(out.getI43OUTNO06().equals("0")){
//				PayLinkedQueue.setQueuePayUP(pif);
//			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "I43接口总是--->>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson(out);
	}

}
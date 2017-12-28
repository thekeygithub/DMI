package com.ts.controller.app.SearchAPI.PaymentAPI.OutpatientClinic;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.I28InListChargesBean;
import com.ts.CXFClient.I28InListDocumentsBean;
import com.ts.CXFClient.I28InputSectionAccountsBean;
import com.ts.CXFClient.I28OutCalculationResultInfoBean;
import com.ts.CXFClient.I28OutSegmentedInfoStructureBean;
import com.ts.CXFClient.I28OutTransfiniteDetailListBean;
import com.ts.CXFClient.I28OutputSectionAccountsbean;
import com.ts.CXFClient.MedicarePayment;
import com.ts.controller.app.SearchAPI.PayResultHandler.impl.PayRSHandler;
import com.ts.controller.base.BaseController;
import com.ts.entity.P_bill;
import com.ts.entity.P_bill_item;
import com.ts.entity.P_fund;
import com.ts.entity.P_inte_info;
import com.ts.entity.P_over_detail;
import com.ts.entity.P_result;
import com.ts.entity.P_user;
import com.ts.util.PayLinkedQueue;
import com.ts.util.Transformation;
import com.ts.util.CXFClientFactory.CXFClientFactory;

import net.sf.json.JSONObject;
/**
 * 
* @ClassName: SectionAccountsPAY 
* @Description: TODO(I28---门诊挂号结算接口) 
* @author Lee 李世博
* @date 2017年3月22日 下午8:43:46 
*
 */
@Controller
public class SectionAccountsPAY extends PayRSHandler{

	@Override
	protected Object exceBusiness(JSONObject value) {
		//调用接口
		Long l = System.currentTimeMillis();
//		MedicarePaymentService service = new MedicarePaymentService();
		Transformation tf =new Transformation();
		I28OutputSectionAccountsbean out = new I28OutputSectionAccountsbean();
		try {
		Long ls = System.currentTimeMillis();
//		MedicarePayment service = CXFClientFactory.FactoryMedicarePayment();
//		MedicarePayment mp = service.getMedicarePaymentPort();
		I28InputSectionAccountsBean bean = new I28InputSectionAccountsBean();
		bean = (I28InputSectionAccountsBean)tf.setParam(bean, value);
//		out = service.getSectionAccounts(bean);
		BaseController.logBefore(logger, "I28service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
		Long le = System.currentTimeMillis();
		P_inte_info pif = new P_inte_info();//接口数据信息
		P_user pu = new P_user();//用户信息
			//用户信息
//			pu.setID(uID);//主键ID
			pu.setAPI_TYPE(value.getString("apitype")); //业务类型
			pu.setREQ_NO(value.getString("requestno")); //请求流水号
			pu.setTIME_STAMP(value.getString("timestamp")); //时间戳
			pu.setID_CARD(bean.getI28INPNO03()); //身份证号
			pu.setCARD_NO(bean.getI28INPNO02()); //市民卡号
			pu.setIC_CARD(bean.getI28INPNO14());//物理卡号
			pu.setCREATE_DATE(Transformation.getDate()); //创建日期
			pif.setPUser(pu);//用户信息子集
		//结算单据
		List<I28InListDocumentsBean> ldbs= bean.getI28INPLIST01();
		//收费项目
		List<I28InListChargesBean> lcbs = bean.getI28INPLIST02();
		//临时收费项目
		List<I28InListChargesBean> tmplcbs = new ArrayList<I28InListChargesBean>(); 
		for(I28InListDocumentsBean ldb :ldbs){
			P_bill pb = new P_bill();//结算单据
			pb.setBILL_NO(ldb.getI28INPLIST01NO01());//单据号码
			pb.setCODE(ldb.getI28INPLIST01NO02());//科室代码
			pb.setNAME(ldb.getI28INPLIST01NO03());//科室名称
			pb.setDOC_NO(ldb.getI28INPLIST01NO04());//医生编码
			pb.setNUM(Integer.parseInt(Transformation.toCast(ldb.getI28INPLIST01NO05() , Integer.class)));//收费明细条数
			for(I28InListChargesBean lcb : lcbs){
				String tmpBillNo =  lcb.getI28INPLIST02NO01();//收费项目中的单据号码
				if(pb.getBILL_NO() == null || !pb.getBILL_NO().equals(tmpBillNo)){
					continue;
				}
				P_bill_item pbi = getPBillItemByI28InListChargesBean(lcb);
				pb.addPbillItem(pbi);//收费项目子集
				tmplcbs.add(lcb);
			}
			pif.addpBill(pb);//结算单据子集
		}
		lcbs.removeAll(tmplcbs);
		if(lcbs != null && lcbs.size() > 0){//处理未匹配上的收费项目单据
			for(I28InListChargesBean lcb : lcbs){
				P_bill_item pbi = getPBillItemByI28InListChargesBean(lcb);
				pif.addpBillItem(pbi);
			}
		}
		//超限明细表
		List<I28OutTransfiniteDetailListBean> tfds = out.getI28OUTLIST01();
		for(I28OutTransfiniteDetailListBean tfd : tfds){
			P_over_detail pod = new P_over_detail();//超限明细表	
			pod.setTYPE(tfd.getI28OUTLIST01NO01());//药品诊疗类型
			pod.setCODE(tfd.getI28OUTLIST01NO02());//医院项目编码
			pod.setNUM(Double.parseDouble(Transformation.toCast(tfd.getI28OUTLIST01NO03() , Double.class)));//超限数量
			pod.setSELF_FEE(Double.parseDouble(Transformation.toCast(tfd.getI28OUTLIST01NO04() , Double.class)));//超限自费金额
			pod.setTOTAL_FEE(Double.parseDouble(Transformation.toCast(tfd.getI28OUTLIST01NO05() , Double.class)));//合计自费金额
			pod.setREA_CODE(Double.parseDouble(Transformation.toCast(tfd.getI28OUTLIST01NO06() , Double.class)));//超限原因代码
			pod.setREA_DESC(tfd.getI28OUTLIST01NO07());//超限原因说明
			pif.addpOverDetail(pod);//超限明细子集
		}
		//基金分段信息
		List<I28OutSegmentedInfoStructureBean> pfds = out.getI28OUTSUB02();
		for(I28OutSegmentedInfoStructureBean sis:pfds){
			P_fund pfd = new P_fund();//基金分段信息
			pfd.setCODE(sis.getI28OUTSUB02NO01());//分段编码
			pfd.setNAME(sis.getI28OUTSUB02NO02());//分段名称
			pfd.setAMOUNT(Double.parseDouble(Transformation.toCast(sis.getI28OUTSUB02NO03() , Double.class)));//进入额度	
			pfd.setPAY_AMOUNT(Double.parseDouble(Transformation.toCast(sis.getI28OUTSUB02NO04() , Double.class)));//分段基金支付金额
			pfd.setRATIO(Double.parseDouble(Transformation.toCast(sis.getI28OUTSUB02NO05(),Double.class)));//报销比例
			pfd.setSELF_AMOUNT(Double.parseDouble(Transformation.toCast(sis.getI28OUTSUB02NO06() , Double.class)));//分段自负金额
			pfd.setNEG_AMOUNT(Double.parseDouble(Transformation.toCast(sis.getI28OUTSUB02NO07() , Double.class)));//分段自费金额
			pif.addpFund(pfd);//基金分段子集
			
		}
		
		//计算结果信息
		List<I28OutCalculationResultInfoBean>  sub01= out.getI28OUTSUB01();
		for( I28OutCalculationResultInfoBean cris : sub01){
			P_result pr = new P_result();//计算结果信息
			pr.setTOTAL_FEE(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO01() , Double.class)));//费用总额
			pr.setSELF_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO02() , Double.class)));//自费总额(非医保)
			pr.setSELF_NEG(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO03() , Double.class)));//药品乙类自负
			pr.setMED_FEE(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO04() , Double.class)));//医保费用
			pr.setTRAN_FEE(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO05() , Double.class)));//转院自费
			pr.setLEVEL_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO06() , Double.class)));//起付线
			pr.setSELF_CASH_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO07() , Double.class)));//个人自费现金支付
			pr.setNEG_CASH_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO08() , Double.class)));//个人自负现金支付
			pr.setCASH_TOTAL(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO09() , Double.class)));//合计现金支付
			pr.setRETURN_FEE(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO10() , Double.class)));//合计报销金额
			pr.setI_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO11() , Double.class)));//历年帐户支付
			pr.setO_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO12() , Double.class)));//当年帐户支付
			pr.setWHOLE_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO13() , Double.class)));//保统筹基金支付
			pr.setADD_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO14() , Double.class)));//补充保险支付
			pr.setSER_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO15() , Double.class)));//公务员补助支付
			pr.setCOM_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO16() , Double.class)));//单位补助支付
			pr.setSALV_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO17() , Double.class)));//医疗救助支付
			pr.setRETIRE_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO18() , Double.class)));//离休基金支付
			pr.setFUND_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO19() , Double.class)));//二乙基金支付
			pr.setMODEL_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO20() , Double.class)));//劳模医疗补助支付
			pr.setCIVIL_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO21() , Double.class)));//民政救助支付
			pr.setPRIV_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO22() , Double.class)));//优抚救助支付
			pr.setDPF_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO23() , Double.class)));//残联基金支付
			pr.setPLAN_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO24() , Double.class)));//计生基金支付
			pr.setHURT_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO25() , Double.class)));//工伤基金支付
			pr.setPROC_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO26() , Double.class)));//生育基金支付
			pr.setI_BALANCE(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO27() , Double.class)));//结算后当年个帐余额
			pr.setO_BALANCE(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO28() , Double.class)));//结算后历年个帐余额
			pr.setINSU_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO29() , Double.class)));//合保统筹基金支付
			pr.setINSU_SALV_PAY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO30() , Double.class)));//合保大病救助支付
			pr.setCHARITY(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO31() , Double.class)));//慈善基金
//			pr.setMUTUAL(Double.parseDouble(Transformation.toCast(cris.getI28OUTSUB01NO32() , Double.class)));//共济账户支出
			pif.addPReuslt(pr);//计算结果子集
		}
		//接口信息
		pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
		pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
		pif.setREQ_NO(value.getString("requestno"));//请求流水号
		pif.setDATA_TYPE(2);//数据类型
		pif.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
		pif.setHOS_CODE(bean.getI28INPNO01());//医院编码
		pif.setPAY_TYPE(Integer.parseInt(Transformation.toCast(bean.getI28INPNO04(),Integer.class)));//现金支付方式
		pif.setBANK_NO(bean.getI28INPNO05());//银行卡信息
		pif.setBUSI_TYPE(bean.getI28INPNO06());//业务类型
		pif.setVISIT_NO(bean.getI28INPNO07());//医院交易流水号
		pif.setDIS_CODE(bean.getI28INPNO08());//疾病编号
		pif.setAPPR_NO(bean.getI28INPNO09());//病种审批号
		pif.setDIS_NAME(bean.getI28INPNO10());//疾病名称
		pif.setDIS_DESC(bean.getI28INPNO11());//疾病描述
		pif.setBILL_NUM(Integer.parseInt(Transformation.toCast( bean.getI28INPNO12(),Integer.class)));//本次结算单据张数
		pif.setOPERATOR(bean.getI28INPNO13());//经办人
		pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(out.getI28OUTNO01() , Integer.class)));//交易状态
		pif.setERROR(out.getI28OUTNO02());//错误信息
		pif.setCARD_RES(out.getI28OUTNO03());//写社会保障卡结果
		pif.setBANK_RES(out.getI28OUTNO04());//扣银行卡结果
		pif.setIC_DATA(out.getI28OUTNO05());//更新后IC卡数据
		pif.setOVER_FLAG(Integer.parseInt(Transformation.toCast(out.getI28OUTNO06(), Integer.class)));//超限提示标记
		pif.setSPEC_FLAG(Integer.parseInt(Transformation.toCast(out.getI28OUTNO07(), Integer.class)));//规定病种标志
		pif.setFINAL_DATE(Transformation.toDate(out.getI28OUTNO08()));//结算时间
		pif.setFINAL_NO(out.getI28OUTNO09());//结算流水号
		//实体放入队列
		PayLinkedQueue.setQueuePay(pif);
		BaseController.logBefore(logger, "I28---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "I28---->>>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson(out);
		
	}
	private P_bill_item getPBillItemByI28InListChargesBean(I28InListChargesBean lcb) {
		P_bill_item pbi = new P_bill_item();//收费项目 
		pbi.setTYPE(lcb.getI28INPLIST02NO02()); //药品诊疗类型
		pbi.setCODE(lcb.getI28INPLIST02NO03()); //医院编码
		pbi.setNAME(lcb.getI28INPLIST02NO04());//医院名称
		pbi.setUNIT(lcb.getI28INPLIST02NO05());//医院单位
		pbi.setSPEC(lcb.getI28INPLIST02NO06());//医院规格
		pbi.setFORM(lcb.getI28INPLIST02NO07());//医院剂型
		pbi.setRECIPE_NO(lcb.getI28INPLIST02NO08());//处方号
		pbi.setPRICE(Double.parseDouble(Transformation.toCast(lcb.getI28INPLIST02NO09() , Double.class)));//医院单价
		pbi.setP_NUM(Double.parseDouble(Transformation.toCast(lcb.getI28INPLIST02NO10() , Double.class)));//贴数
		pbi.setNUM(Double.parseDouble(Transformation.toCast(lcb.getI28INPLIST02NO11() , Double.class)));//数量
		pbi.setFEE(Double.parseDouble(Transformation.toCast(lcb.getI28INPLIST02NO12() , Double.class)));//总金额
		pbi.setUSE_DAY(Double.parseDouble(Transformation.toCast(lcb.getI28INPLIST02NO13() , Double.class)));//用药天数
		pbi.setOVER_FLAG(Integer.parseInt(Transformation.toCast(lcb.getI28INPLIST02NO14() , Integer.class)));//超限自费标志
		pbi.setCEN_CODE(lcb.getI28INPLIST02NO15());//中心编码（药品诊疗中心编码）
		pbi.setCOM(lcb.getI28INPLIST02NO16());//医院生产厂家（产地）
		pbi.setPACK(lcb.getI28INPLIST02NO17());//医院转换比（包装量）
		return pbi;
	}

}
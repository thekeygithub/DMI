package com.ts.controller.app.SearchAPI.PaymentAPI.InformationSynthesis;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.ts.CXFClient.I22InputGetInfoPersonBean;
import com.ts.CXFClient.I22OutPersonalInformationBean;
import com.ts.CXFClient.I22OutputGetInfoPersonBean;
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
public class PersonalInformationPayBusiness extends PayRSHandler {

	@Override
	protected Object exceBusiness(JSONObject value) {
		Long l = System.currentTimeMillis();
		Transformation tf = new Transformation();
		I22OutputGetInfoPersonBean outModel = new I22OutputGetInfoPersonBean();
		try{
			Long ls = System.currentTimeMillis();
			I22InputGetInfoPersonBean inModel = new I22InputGetInfoPersonBean();
			inModel = (I22InputGetInfoPersonBean) tf.setParam(inModel, value);
			MedicarePayment function = CXFClientFactory.FactoryMedicarePayment();
			outModel = function.getPersonalInformation(inModel);
			BaseController.logBefore(logger, "I22service--->>>执行时间:" +  (System.currentTimeMillis() - ls ));
			//===================================== start 将入参和出参放进实体bean中  start =========================================
			
			Long le = System.currentTimeMillis();
			P_inte_info pif = new P_inte_info();//接口数据信息
				
			//用户信息
			P_user pu = new P_user();
			pu.setAPI_TYPE(value.getString("apitype"));//业务类型
			pu.setREQ_NO(value.getString("requestno"));//请求流水号
			pu.setTIME_STAMP(value.getString("timestamp"));//时间戳
			pu.setID_CARD(value.getString("I22_INP_NO03"));//身份证号
			pu.setCARD_NO(value.getString("I22_INP_NO02"));//市民卡号
			pu.setIC_CARD(value.getString("I22_INP_NO07"));//物理卡号
			pu.setCREATE_DATE(Transformation.getDate());//创建日期
			pif.setPUser(pu);//用户信息子集
			
			//参保人员信息
			List<I22OutPersonalInformationBean> insuList = outModel.getI22OUTSUB01();
			for(I22OutPersonalInformationBean insuObj : insuList){
				P_insu_info pii = new P_insu_info();
				pii.setAPI_TYPE(value.getString("apitype"));//业务接口编号
				pii.setREQ_NO(value.getString("requestno"));//请求流水号
				pii.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
				pii.setBANK_NO(value.getString("I22_INP_NO05"));//银行卡信息
				pii.setREAD_TYPE(value.getString("I22_INP_NO06"));//读卡方式
				pii.setHOS_CODE(value.getString("I22_INP_NO01"));//医院编码
				pii.setMED_CARD(insuObj.getI22OUTSUB01NO01());//卡号
				pii.setINSU_NO(insuObj.getI22OUTSUB01NO02());//个人社保编号
				pii.setSEX(insuObj.getI22OUTSUB01NO04());//性别
				pii.setNAME(insuObj.getI22OUTSUB01NO03());//姓名
				//pii.setCREATE_DATE(Transformation.getDate());//创建日期
				//user表设置名字
				pu.setNAME(insuObj.getI22OUTSUB01NO03());
				pii.setNATION(insuObj.getI22OUTSUB01NO05());//民族
				pii.setBIRTH(insuObj.getI22OUTSUB01NO06());//出生日期
				pii.setCOM(insuObj.getI22OUTSUB01NO08());//单位性质
				pii.setNAME_ADDR(insuObj.getI22OUTSUB01NO09());//单位名称/家庭地址
				pii.setAREA_CODE(insuObj.getI22OUTSUB01NO10());//地区编码
				pii.setAREA_NAME(insuObj.getI22OUTSUB01NO11());//地区名称
				pii.setMED(insuObj.getI22OUTSUB01NO12());//医保待遇（政策）类别，结算依据
				pii.setHONOR(insuObj.getI22OUTSUB01NO13());//荣誉类别
				pii.setLOW(insuObj.getI22OUTSUB01NO14());//低保类别
				pii.setPRIV_RANK(insuObj.getI22OUTSUB01NO15());//优抚级别
				pii.setSPEC_FLAG(insuObj.getI22OUTSUB01NO16());//特殊病标志
				pii.setSPEC_CODE(insuObj.getI22OUTSUB01NO17());//特殊病编码
				pii.setI_BALANCE(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO18(), Double.class)));//当年帐户余额
				pii.setO_BALANCE(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO19(), Double.class)));//历年帐户余额
				pii.setOUT_TOTAL(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO20(), Double.class)));//当年住院医保累计
				pii.setIN_TOTAL(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO21(), Double.class)));//当年门诊医保累计
				pii.setDIS_TOTAL(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO22(), Double.class)));//当年规定病医保累计
				pii.setWHOLE(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO23(), Double.class)));//当年累计列入统筹基数
				pii.setWHOLE_PAY(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO24(), Double.class)));//当年统筹基金支付累计
				pii.setINSU_PAY(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO25(), Double.class)));//当年补充保险支付累计
				pii.setSER_PAY(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO26(), Double.class)));//当年公务员补助支付累计
				pii.setCOM_PAY(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO27(), Double.class)));//当年企事业补助支付累计
				pii.setSPEC_PAY(Double.parseDouble(Transformation.toCast(insuObj.getI22OUTSUB01NO28(), Double.class)));//当年专项基金支付累计
				pii.setIN_COUNT(Integer.parseInt(Transformation.toCast(insuObj.getI22OUTSUB01NO29(), Integer.class)));//当年住院次数
				pii.setPART(insuObj.getI22OUTSUB01NO30());//工伤认定部位
				pii.setLITTLE_INSU(insuObj.getI22OUTSUB01NO31());//医疗小险种
				pii.setDEAL_STAT(Integer.parseInt(Transformation.toCast(outModel.getI22OUTNO01(), Integer.class)));//交易状态
				pii.setERROR(outModel.getI22OUTNO02());//错误信息
				pii.setCARD_RES(outModel.getI22OUTNO03());//写社会保障卡结果
				pii.setBANK_RES(outModel.getI22OUTNO04());//扣银行卡结果
				pii.setIC_DATA(outModel.getI22OUTNO05());//更新后IC卡数据
				pii.setMED_RES(outModel.getI22OUTNO06());//医疗身份验证结果
				pii.setHURT_RES(outModel.getI22OUTNO07());//工伤身份验证结果
				pii.setPROC_RES(outModel.getI22OUTNO08());//生育身份验证结果
				pii.setCARD_NO(outModel.getI22OUTNO09());//市民卡社会保障卡号
				pu.setPInsuInfo(pii);//参保人员子集
			}
			
			//接口信息
			pif.setUSER_ID(pu.getID());//用户信息ID	
			pif.setAPI_TYPE(value.getString("apitype"));//业务接口编号
			pif.setREQ_NO(value.getString("requestno"));//请求流水号
			pif.setDATA_TYPE(10);//数据类型：1门诊预结算，2门诊结算，3门诊退费退号，4住院预结算，5出院结算，6住院明细查询，7交易结果查询，8参保人结果及享受状态查询，9确认交易，10获取参保人信息接口
			pif.setTIME_STAMP(pu.getTIME_STAMP());//时间戳
			pif.setHOS_CODE(value.getString("I22_INP_NO01"));//医院编码
			pif.setPAY_TYPE(Integer.parseInt(Transformation.toCast(value.getString("I22_INP_NO04"),Integer.class)));//现金支付方式
			pif.setBANK_NO(value.getString("I22_INP_NO05"));//银行卡信息
			pif.setDEAL_STAT(Integer.parseInt(Transformation.toCast(outModel.getI22OUTNO01(), Integer.class)));//交易状态
			pif.setERROR(outModel.getI22OUTNO02());//错误信息
			pif.setCARD_RES(outModel.getI22OUTNO03());//写社会保障卡结果
			pif.setBANK_RES(outModel.getI22OUTNO04());//扣银行卡结果
			pif.setIC_DATA(outModel.getI22OUTNO05());//更新后IC卡数据
			pif.setGROUP_ID(value.getString("GROUP_ID"));//机构CODE
			
			//实体放入队列
			PayLinkedQueue.setQueuePay(pif);
			BaseController.logBefore(logger, "I22---组装实体加入列队>>>执行时间:" +  (System.currentTimeMillis() - le ));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		BaseController.logBefore(logger, "I22--->>>执行时间:" +  (System.currentTimeMillis() - l ));
		return tf.toJson(outModel);
	}

}
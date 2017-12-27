package com.dhcc.ts.mzcfxq.dao;

import java.util.Arrays;
import java.util.List;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.mzcfxq.model.CfmxModel;
import com.dhcc.ts.mzcfxq.model.DoctorModel;
import com.dhcc.ts.mzcfxq.model.DrugModel;
import com.dhcc.ts.mzcfxq.model.MzcfModel;
import com.opensymphony.xwork2.ActionContext;

public class MzcfxqDao {
	/**
	 * @描述：
	 * @作者：SZ
	 * @时间：2017年2月22日 上午11:43:35
	 * @param mzh 门诊号
	 * @return
	 */
	public MzcfModel getMzcfModel(String cfh){
		DBManager_HIS dbm = new DBManager_HIS();
		String sql = "SELECT HR.CMZH,HR.CCFH,HR.CCFZD,HR.CSFZL,HR.CXM,HR.CXB,HR.CNL,HR.CKSMC,"
				+ "HR.CYSMC,HR.DXCSJ,HR.MSFJE,FH.HOS_NAME AS YYMC,HREG.CORP_ID AS CORP_ID,"
				+ "KND.DIS_ID AS DISEASE_ID,FACD.DEP_ID AS KESHI_ID "
				+ "FROM H_RECIPE HR "
				+ "LEFT JOIN H_REG HREG ON HREG.CMZH = HR.CMZH "
				+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HREG.CORP_ID "
				+ "LEFT JOIN KN_DISEASE KND ON KND.DIS_NAME = HR.CCFZD "
				+ "LEFT JOIN FAC_DEPARTMENT FACD ON FACD.DEP_NAME = HR.CKSMC AND FACD.HOS_ID = FH.HOS_ID "
				+ "WHERE HR.CCFH='"+cfh+"'";
		MzcfModel model = (MzcfModel)dbm.getObject(MzcfModel.class, sql);
		if(null!=model){
			if(!StringUtil.isNullOrEmpty(model.getCORP_ID())){
				model.setYYMC("<a href=\"hospitalDescription.html?corpid="+model.getCORP_ID()+"\">"+model.getYYMC()+"</a>");
			}
//			if(!StringUtil.isNullOrEmpty(model.getKESHI_ID())){
//				model.setCKSMC("<a href=\"departmentInfo.html?keshiid="+model.getKESHI_ID()+"\">"+model.getCKSMC()+"</a>");
//				//String keshisql = "";
//			}
			if(!StringUtil.isNullOrEmpty(model.getDISEASE_ID())){
				model.setCCFZD("<a href=\"disease.html?zhenduanid="+model.getDISEASE_ID()+"\">"+model.getCCFZD()+"</a>");
	//			String zheduansql = "";
			}
			String sqlmx = "SELECT * FROM H_RECIPE_DETAIL WHERE CCFH='"+model.getCCFH()+"'";//处方明细
			model.setCfmxmodel(dbm.getObjectList(CfmxModel.class, sqlmx));
			
			String 	userid = (String)ActionContext.getContext().getSession().get("userid");
			
			String yssql = "SELECT MED_ID AS YSID "
					+ "FROM FAC_MEDICAL FM "
					+ "LEFT JOIN FAC_DEPARTMENT FD ON FD.DEP_ID = FM.DEP_ID "
					+ "WHERE FM.STAFF_NAME = '"+model.getCYSMC()+"' AND FD.HOS_ID = '"+model.getCORP_ID()+"' ";
			
			MzcfModel ysmodel = (MzcfModel)dbm.getObject(MzcfModel.class, yssql);
			if(null!=ysmodel && !StringUtil.isNullOrEmpty(ysmodel.getYSID())){
//				if("a0babdd2c52c403483b44d8ae66238fc".equals(userid)){
//					userid = "56474";
//				}
				model.setCYSMC("<a href=\"doctor.html?doctor_id="+ysmodel.getYSID()+"\">"+model.getCYSMC()+"</a>");
			}
		}
		
		dbm.close();
		return model;
	}
	
	public List<MzcfModel> getMzcfListModel(String mzh){
		DBManager_HIS dbm = new DBManager_HIS();
		String sql = "SELECT CCFH,DXCSJ FROM H_RECIPE WHERE CMZH = '"+mzh+"'";
		List<MzcfModel> listModel = dbm.getObjectList(MzcfModel.class, sql);
		dbm.close();
		return listModel;
	}
	
	public DoctorModel queryDoctor(String doctor_id){
		DBManager_HIS dbm = new DBManager_HIS();
		String sql = "SELECT FM.STAFF_NAME AS doctor_name,FM.SKILL_TITLE AS zhiwu,FM.GOOD_SKILL AS sc,"
				+ " FM.INTRODUCE AS zyjl,FM.VISIT_DATE AS czsj,FD.DEP_TYPE AS keshi_type,FD.DEP_NAME AS keshi_name,"
				+ " FH.HOS_NAME AS yiyuan_name,FH.ADDRESS AS address "
				+ " FROM FAC_MEDICAL FM "
				+ " LEFT JOIN FAC_DEPARTMENT FD ON FD.DEP_ID = FM.DEP_ID "
				+ " LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = FD.HOS_ID "
				+ " WHERE FM.MED_ID = '"+doctor_id+"'";
		DoctorModel model = (DoctorModel) dbm.getObject(DoctorModel.class, sql);
		String a [] = model.getSc().split("<span>");
		model.setSc(a[0]);
		String b [] = model.getCzsj().split("@@@");
		String str = "<p>";
		for(int i=0;i<b.length;i++){
			if(i%3==0){
				str+="</p><p>";
			}
			str+=b[i];
		}
		str+="</p>";
		String c [] = str.split("</p>");
		Arrays.sort(c);
		String str1 = c [c.length-1] +"</p>";
		for(int i=0;i<c.length-1;i++){
			str1+= c[i] + "</p>";
		}
			
		model.setCzsj(str1.replace("1", "星期一   ").replace("2", "星期二   ")
				.replace("3", "星期三   ").replace("4", "星期四   ").replace("5", "星期五   ")
				.replace("6", "星期六   ").replace("7", "星期日   ").replace("上", "上午   ").replace("下", "下午   "));
		dbm.close();
		return model;
	}
	
	
	public DrugModel getPharmacyDrug(String ypid){
		DBManager_HIS dbm = new DBManager_HIS();
		DrugModel model = new DrugModel();
		String sql = "SELECT DRUG_NAME,MEDICAL_INSU,"
				+ "REPLACE(REPLACE(INDICATION, '<DIV>', ''), '</DIV>', '') AS INDICATION,"
				+ "REPLACE(REPLACE(CONTRA, '<DIV>', ''), '</DIV>', '') AS CONTRA,"
				+ "REPLACE(REPLACE(ADVERSE, '<DIV>', ''), '</DIV>', '') AS ADVERSE,"
				+ "REPLACE(REPLACE(USE_DOSAGE, '<DIV>', ''), '</DIV>', '') AS USE_DOSAGE,"
				+ "REPLACE(REPLACE(NOTES, '<DIV>', ''), '</DIV>', '') AS NOTES,"
				+ "REPLACE(REPLACE(INTERACTION, '<DIV>', ''), '</DIV>', '') AS INTERACTION,"
				+ "REPLACE(REPLACE(PHARMACO, '<DIV>', ''), '</DIV>', '') AS PHARMACO,"
				+ "REPLACE(REPLACE(DYNAMICS, '<DIV>', ''), '</DIV>', '') AS DYNAMICS "
				+ "FROM KN_DRUG_BASE "
				+ "WHERE DB_ID = '"+ypid+"'";
		model = (DrugModel) dbm.getObject(DrugModel.class, sql);
		dbm.close();
		return model;
	}
	
	public DrugModel queryDiseaseDetails(String diseaseid){
		DBManager_HIS dbm = new DBManager_HIS();
		DrugModel model = new DrugModel();
		String sql = "SELECT DIS_NAME AS DRUG_NAME,NAME_ENG AS MEDICAL_INSU,"
				+ "NAME_ENG_S AS INDICATION,"
				+ "SYNO AS CONTRA,"
				+ "DISCIP AS ADVERSE,"
				+ "REPLACE(REPLACE(OVERVIEW, '<DIV>概述：', ''), '</DIV>', '') AS USE_DOSAGE,"
				+ "REPLACE(REPLACE(EPIDEMIOLOGY, '<DIV>流行学病：', ''), '</DIV>', '') AS NOTES,"
				+ "REPLACE(REPLACE(ETIOLOGY, '<DIV>病因：', ''), '</DIV>', '') AS INTERACTION,"
				+ "REPLACE(REPLACE(NOSOGENESIS, '<DIV>发病机制：', ''), '</DIV>', '') AS PHARMACO,"
				
				+ "REPLACE(REPLACE(LAB_EXAMINE, '<DIV>其他辅助检查：', ''), '</DIV>', '') AS YP1,"
				+ "REPLACE(REPLACE(DIAGNOSE, '<DIV>诊断：', ''), '</DIV>', '') AS YP2,"
				+ "REPLACE(REPLACE(DIFF_DIAGNOSE, '<DIV>鉴别诊断：', ''), '</DIV>', '') AS YP3,"
				+ "REPLACE(REPLACE(CURE, '<DIV>治疗：', ''), '</DIV>', '') AS YP4,"
				+ "REPLACE(REPLACE(PROGNOSIS, '<DIV>预后：', ''), '</DIV>', '') AS YP5,"
				+ "REPLACE(REPLACE(PREVENT, '<DIV>预防：', ''), '</DIV>', '') AS YP6,"
				
				+ "REPLACE(REPLACE(COMPLICATION, '<DIV>并发症：', ''), '</DIV>', '') AS DYNAMICS "
				+ "FROM KN_DISEASE "
				+ "WHERE DIS_ID = '"+diseaseid+"'";
		model = (DrugModel) dbm.getObject(DrugModel.class, sql);
		dbm.close();
		return model;
	}

}

package com.dhcc.ts.mzcfxq.model;

import java.util.List;

/**
 * @描述：门诊处方
 * @作者：SZ
 * @时间：2017年2月21日 下午7:37:17
 */
public class MzcfModel {
	private String CMZH;//门诊号
	private String CCFH;//处方号
	private String CCFZD;//处方诊断==临床诊断
	private String CSFZL;//收费种类==费用类别
	private String CXM;//患者姓名
	private String CXB;//性别
	private String CNL;//年龄
	private String CKSMC;//科室名称
	private String CYSMC;//医生名称
	private String DXCSJ;//处方时间==开具时间
	private String MSFJE;//收费金额
	
	private String DDJGBM;//定点机构编码
	private String YYMC;//机构名称(医院名称)
	private String YBKH;//医保卡号
	private String SHDPYS;//审核/调配药师
	private String HDFYYS;//核对/发药药师
	private List<CfmxModel> cfmxmodel;//处方明细
	
	private String CORP_ID;//医院ID
	private String DISEASE_ID;//诊断ID   疾病ID
	private String KESHI_ID;//科室id
	private String YSID;
	
	
//	private String ddjgbm;//定点机构编码
//	private String yymc;//机构名称(医院名称)
//	private String mzh;//门诊号
//	private String ybkh;//医保卡号
//	private String ks;//科室
//	private String fylb;//费用类别
//	private String sex;//性别
//	private String age;//年龄
//	private String lczd;//临床诊断
//	private String kjsj;//开具时间
//	private String cfh;//处方号
//	private String ys;//医生
//	private String je;//金额
//	private String shys;//审核/调配药师
//	private String hdys;//核对/发药药师
//	private List<CfmxModel> cfmxmodel;
	public String getCMZH() {
		return CMZH;
	}
	public void setCMZH(String cMZH) {
		CMZH = cMZH;
	}
	public String getCCFH() {
		return CCFH;
	}
	public void setCCFH(String cCFH) {
		CCFH = cCFH;
	}
	public String getCCFZD() {
		return CCFZD;
	}
	public void setCCFZD(String cCFZD) {
		CCFZD = cCFZD;
	}
	public String getCSFZL() {
		return CSFZL;
	}
	public void setCSFZL(String cSFZL) {
		CSFZL = cSFZL;
	}
	public String getCXM() {
		return CXM;
	}
	public void setCXM(String cXM) {
		CXM = cXM;
	}
	public String getCXB() {
		return CXB;
	}
	public void setCXB(String cXB) {
		CXB = cXB;
	}
	public String getCNL() {
		return CNL;
	}
	public void setCNL(String cNL) {
		CNL = cNL;
	}
	public String getCKSMC() {
		return CKSMC;
	}
	public void setCKSMC(String cKSMC) {
		CKSMC = cKSMC;
	}
	public String getCYSMC() {
		return CYSMC;
	}
	public void setCYSMC(String cYSMC) {
		CYSMC = cYSMC;
	}
	public String getDXCSJ() {
		return DXCSJ;
	}
	public void setDXCSJ(String dXCSJ) {
		DXCSJ = dXCSJ;
	}
	public String getMSFJE() {
		return MSFJE;
	}
	public void setMSFJE(String mSFJE) {
		MSFJE = mSFJE;
	}
	public String getDDJGBM() {
		return DDJGBM;
	}
	public void setDDJGBM(String dDJGBM) {
		DDJGBM = dDJGBM;
	}
	public String getYYMC() {
		return YYMC;
	}
	public void setYYMC(String yYMC) {
		YYMC = yYMC;
	}
	public String getYBKH() {
		return YBKH;
	}
	public void setYBKH(String yBKH) {
		YBKH = yBKH;
	}
	public String getSHDPYS() {
		return SHDPYS;
	}
	public void setSHDPYS(String sHDPYS) {
		SHDPYS = sHDPYS;
	}
	public String getHDFYYS() {
		return HDFYYS;
	}
	public void setHDFYYS(String hDFYYS) {
		HDFYYS = hDFYYS;
	}
	public List<CfmxModel> getCfmxmodel() {
		return cfmxmodel;
	}
	public void setCfmxmodel(List<CfmxModel> cfmxmodel) {
		this.cfmxmodel = cfmxmodel;
	}
	public String getCORP_ID() {
		return CORP_ID;
	}
	public void setCORP_ID(String cORP_ID) {
		CORP_ID = cORP_ID;
	}
	public String getDISEASE_ID() {
		return DISEASE_ID;
	}
	public void setDISEASE_ID(String dISEASE_ID) {
		DISEASE_ID = dISEASE_ID;
	}
	public String getKESHI_ID() {
		return KESHI_ID;
	}
	public void setKESHI_ID(String kESHI_ID) {
		KESHI_ID = kESHI_ID;
	}
	public String getYSID() {
		return YSID;
	}
	public void setYSID(String ySID) {
		YSID = ySID;
	}
}

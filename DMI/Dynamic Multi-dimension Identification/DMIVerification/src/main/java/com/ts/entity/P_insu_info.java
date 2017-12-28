package com.ts.entity;

import java.io.Serializable;

/**
 * 
* @ClassName: P_insu_info 
* @Description: TODO(参保人员信息) 
* @author Lee 刘峰
* @date 2017年3月28日 上午10:30:00 
*
 */
public class P_insu_info implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID									
	private String USER_ID;//用户ID								
	private String API_TYPE;//业务接口编号							
	private String REQ_NO;//请求流水号							
	private String TIME_STAMP;//时间戳							
	private String BANK_NO;//银行卡信息							
	private String READ_TYPE;//读卡方式						
	private String HOS_CODE;//医院编码						
	private String MED_CARD;//卡号								
	private String INSU_NO;//个人社保编号					
	private String SEX;//参照字典
	private String NAME;//姓名
	private String NATION;//民族							
	private String BIRTH;//出生日期						
	private String COM;//单位性质							
	private String NAME_ADDR;//单位名称/家庭地址			
	private String AREA_CODE;//地区编码					
	private String AREA_NAME;//地区名称					
	private String MED;//参照字典								
	private String HONOR;//参照字典							
	private String LOW;//参照字典								
	private String PRIV_RANK;//参照字典						
	private String SPEC_FLAG;//1:中心有特殊病登记 0：没有登记	
	private String SPEC_CODE;//特殊病编码						
	private Double I_BALANCE;//当年帐户余额							
	private Double O_BALANCE;//历年帐户余额							
	private Double OUT_TOTAL;//当年住院医保累计						
	private Double IN_TOTAL;//当年门诊医保累计						
	private Double DIS_TOTAL;//当年规定病医保累计					
	private Double WHOLE;//当年累计列入统筹基数						
	private Double WHOLE_PAY;//当年统筹基金支付累计					
	private Double INSU_PAY;//当年补充保险支付累计					
	private Double SER_PAY;//当年公务员补助支付累计					
	private Double COM_PAY;//当年企事业补助支付累计					
	private Double SPEC_PAY;//当年专项基金支付累计					
	private Integer IN_COUNT;//当年住院次数							
	private String PART;//工伤认定部位							
	private String LITTLE_INSU;//参照字典						
	private Integer DEAL_STAT;//交易状态：0成功，小于0失败				
	private String ERROR;//错误信息								
	private String CARD_RES;//0表示不写或写卡成功，其他表示写卡错误信息		
	private String BANK_RES;//0表示不扣或扣成功						
	private String IC_DATA;//更新后IC卡数据							
	private String MED_RES;//医疗身份验证结果				
	private String HURT_RES;//工伤身份验证结果				
	private String PROC_RES;//生育身份验证结果				
	private String CARD_NO;//市民卡社会保障卡号			
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getAPI_TYPE() {
		return API_TYPE;
	}
	public void setAPI_TYPE(String aPI_TYPE) {
		API_TYPE = aPI_TYPE;
	}
	public String getREQ_NO() {
		return REQ_NO;
	}
	public void setREQ_NO(String rEQ_NO) {
		REQ_NO = rEQ_NO;
	}
	public String getTIME_STAMP() {
		return TIME_STAMP;
	}
	public void setTIME_STAMP(String tIME_STAMP) {
		TIME_STAMP = tIME_STAMP;
	}
	public String getBANK_NO() {
		return BANK_NO;
	}
	public void setBANK_NO(String bANK_NO) {
		BANK_NO = bANK_NO;
	}
	public String getREAD_TYPE() {
		return READ_TYPE;
	}
	public void setREAD_TYPE(String rEAD_TYPE) {
		READ_TYPE = rEAD_TYPE;
	}
	public String getHOS_CODE() {
		return HOS_CODE;
	}
	public void setHOS_CODE(String hOS_CODE) {
		HOS_CODE = hOS_CODE;
	}
	public String getMED_CARD() {
		return MED_CARD;
	}
	public void setMED_CARD(String mED_CARD) {
		MED_CARD = mED_CARD;
	}
	public String getINSU_NO() {
		return INSU_NO;
	}
	public void setINSU_NO(String iNSU_NO) {
		INSU_NO = iNSU_NO;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getNATION() {
		return NATION;
	}
	public void setNATION(String nATION) {
		NATION = nATION;
	}
	public String getBIRTH() {
		return BIRTH;
	}
	public void setBIRTH(String bIRTH) {
		BIRTH = bIRTH;
	}
	public String getCOM() {
		return COM;
	}
	public void setCOM(String cOM) {
		COM = cOM;
	}
	public String getNAME_ADDR() {
		return NAME_ADDR;
	}
	public void setNAME_ADDR(String nAME_ADDR) {
		NAME_ADDR = nAME_ADDR;
	}
	public String getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	public String getAREA_NAME() {
		return AREA_NAME;
	}
	public void setAREA_NAME(String aREA_NAME) {
		AREA_NAME = aREA_NAME;
	}
	public String getMED() {
		return MED;
	}
	public void setMED(String mED) {
		MED = mED;
	}
	public String getHONOR() {
		return HONOR;
	}
	public void setHONOR(String hONOR) {
		HONOR = hONOR;
	}
	public String getLOW() {
		return LOW;
	}
	public void setLOW(String lOW) {
		LOW = lOW;
	}
	public String getPRIV_RANK() {
		return PRIV_RANK;
	}
	public void setPRIV_RANK(String pRIV_RANK) {
		PRIV_RANK = pRIV_RANK;
	}
	public String getSPEC_FLAG() {
		return SPEC_FLAG;
	}
	public void setSPEC_FLAG(String sPEC_FLAG) {
		SPEC_FLAG = sPEC_FLAG;
	}
	public String getSPEC_CODE() {
		return SPEC_CODE;
	}
	public void setSPEC_CODE(String sPEC_CODE) {
		SPEC_CODE = sPEC_CODE;
	}
	public Double getI_BALANCE() {
		return I_BALANCE;
	}
	public void setI_BALANCE(Double i_BALANCE) {
		I_BALANCE = i_BALANCE;
	}
	public Double getO_BALANCE() {
		return O_BALANCE;
	}
	public void setO_BALANCE(Double o_BALANCE) {
		O_BALANCE = o_BALANCE;
	}
	public Double getOUT_TOTAL() {
		return OUT_TOTAL;
	}
	public void setOUT_TOTAL(Double oUT_TOTAL) {
		OUT_TOTAL = oUT_TOTAL;
	}
	public Double getIN_TOTAL() {
		return IN_TOTAL;
	}
	public void setIN_TOTAL(Double iN_TOTAL) {
		IN_TOTAL = iN_TOTAL;
	}
	public Double getDIS_TOTAL() {
		return DIS_TOTAL;
	}
	public void setDIS_TOTAL(Double dIS_TOTAL) {
		DIS_TOTAL = dIS_TOTAL;
	}
	public Double getWHOLE() {
		return WHOLE;
	}
	public void setWHOLE(Double wHOLE) {
		WHOLE = wHOLE;
	}
	public Double getWHOLE_PAY() {
		return WHOLE_PAY;
	}
	public void setWHOLE_PAY(Double wHOLE_PAY) {
		WHOLE_PAY = wHOLE_PAY;
	}
	public Double getINSU_PAY() {
		return INSU_PAY;
	}
	public void setINSU_PAY(Double iNSU_PAY) {
		INSU_PAY = iNSU_PAY;
	}
	public Double getSER_PAY() {
		return SER_PAY;
	}
	public void setSER_PAY(Double sER_PAY) {
		SER_PAY = sER_PAY;
	}
	public Double getCOM_PAY() {
		return COM_PAY;
	}
	public void setCOM_PAY(Double cOM_PAY) {
		COM_PAY = cOM_PAY;
	}
	public Double getSPEC_PAY() {
		return SPEC_PAY;
	}
	public void setSPEC_PAY(Double sPEC_PAY) {
		SPEC_PAY = sPEC_PAY;
	}
	public Integer getIN_COUNT() {
		return IN_COUNT;
	}
	public void setIN_COUNT(Integer iN_COUNT) {
		IN_COUNT = iN_COUNT;
	}
	public String getPART() {
		return PART;
	}
	public void setPART(String pART) {
		PART = pART;
	}
	public String getLITTLE_INSU() {
		return LITTLE_INSU;
	}
	public void setLITTLE_INSU(String lITTLE_INSU) {
		LITTLE_INSU = lITTLE_INSU;
	}
	public Integer getDEAL_STAT() {
		return DEAL_STAT;
	}
	public void setDEAL_STAT(Integer dEAL_STAT) {
		DEAL_STAT = dEAL_STAT;
	}
	public String getERROR() {
		return ERROR;
	}
	public void setERROR(String eRROR) {
		ERROR = eRROR;
	}
	public String getCARD_RES() {
		return CARD_RES;
	}
	public void setCARD_RES(String cARD_RES) {
		CARD_RES = cARD_RES;
	}
	public String getBANK_RES() {
		return BANK_RES;
	}
	public void setBANK_RES(String bANK_RES) {
		BANK_RES = bANK_RES;
	}
	public String getIC_DATA() {
		return IC_DATA;
	}
	public void setIC_DATA(String iC_DATA) {
		IC_DATA = iC_DATA;
	}
	public String getMED_RES() {
		return MED_RES;
	}
	public void setMED_RES(String mED_RES) {
		MED_RES = mED_RES;
	}
	public String getHURT_RES() {
		return HURT_RES;
	}
	public void setHURT_RES(String hURT_RES) {
		HURT_RES = hURT_RES;
	}
	public String getPROC_RES() {
		return PROC_RES;
	}
	public void setPROC_RES(String pROC_RES) {
		PROC_RES = pROC_RES;
	}
	public String getCARD_NO() {
		return CARD_NO;
	}
	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}

}
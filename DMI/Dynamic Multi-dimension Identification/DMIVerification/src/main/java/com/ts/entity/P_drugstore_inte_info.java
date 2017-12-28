package com.ts.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 药店支付接口数据信息
 * @ClassName:P_drugstore_inte_info
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年9月18日下午2:51:04
 */
public class P_drugstore_inte_info implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ID;//主键ID																					
	private String API_TYPE;//业务接口编号	
	private String GROUP_ID;//机构Code
	private Integer DATA_TYPE;//71 药店预结算 72药店结算73药店预退费74 药店退费75药店对账
	private String REQ_NO;//请求流水号																		
	private String TIME_STAMP;//时间戳	
	private String SERVICE_NAME;//服务机构名称
	private String SERVICE_NO;//服务机构编号
	private String IC_CARD;//IC卡卡号
	private String WORK_NO;//工号
	private String PERS_NO;//个人编号
	private Integer BUSI_TYPE;//医疗类别  参见字典	
	
	private String CASHIER_NO;//收款员编号
	private String CASHIER_NAME;//收款员名称
	private String BILL_DOC;//开单医生
	private String MEDICAL_DEPT;//就诊科室
	private String MEDICAL_NAME;//诊断疾病名称
	private Integer RECEIPT_TYPE;//药店收据类型
	private Integer DEAL_STAT;//调用状态
	private String DEAL_ERROR_CODE;//调用错误码
	private String DEAL_ERROR_INFO;//调用错误名称
	private String SERVICE_ERROR;//服务的错误信息
	private String MEDICARE_NO;//医保流水号
	private String RECEIPT_NO;//药店收据号
	private String REFUND_NO;//药店退费收据号
	private String NEW_NO;//药店部分退费后生成的新的收据号
	private String ADD_INFO;//附加信息
	private String CREATE_DATE;//记录的时间
	
	private List<P_drugstore_drug_item> pDrug=new ArrayList<P_drugstore_drug_item>();//药品信息
	private List<P_drugstore_result> pResult = new ArrayList<P_drugstore_result>() ;//计算结果
	
	/**
	 * 添加药品信息
	 * @param pr
	 */
	public void  addpDrug(P_drugstore_drug_item pd)
	{
		this.pDrug.add(pd);
	}
	/**
	 * 添加计算结果
	 * @param pr
	 */
	public void  addpReuslt(P_drugstore_result pr)
	{
		this.pResult.add(pr);
	}
	
	public List<P_drugstore_drug_item> getpDrug() {
		return pDrug;
	}
	public List<P_drugstore_result> getpResult() {
		return pResult;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getAPI_TYPE() {
		return API_TYPE;
	}

	public void setAPI_TYPE(String aPI_TYPE) {
		API_TYPE = aPI_TYPE;
	}

	public String getGROUP_ID() {
		return GROUP_ID;
	}
	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}
	public Integer getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(Integer dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
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

	public String getSERVICE_NO() {
		return SERVICE_NO;
	}

	public void setSERVICE_NO(String sERVICE_NO) {
		SERVICE_NO = sERVICE_NO;
	}

	public String getIC_CARD() {
		return IC_CARD;
	}

	public void setIC_CARD(String iC_CARD) {
		IC_CARD = iC_CARD;
	}

	public String getWORK_NO() {
		return WORK_NO;
	}

	public void setWORK_NO(String wORK_NO) {
		WORK_NO = wORK_NO;
	}

	public String getPERS_NO() {
		return PERS_NO;
	}

	public void setPERS_NO(String pERS_NO) {
		PERS_NO = pERS_NO;
	}

	public Integer getBUSI_TYPE() {
		return BUSI_TYPE;
	}

	public void setBUSI_TYPE(Integer bUSI_TYPE) {
		BUSI_TYPE = bUSI_TYPE;
	}

	public String getCASHIER_NO() {
		return CASHIER_NO;
	}

	public void setCASHIER_NO(String cASHIER_NO) {
		CASHIER_NO = cASHIER_NO;
	}

	public String getCASHIER_NAME() {
		return CASHIER_NAME;
	}

	public void setCASHIER_NAME(String cASHIER_NAME) {
		CASHIER_NAME = cASHIER_NAME;
	}

	public String getBILL_DOC() {
		return BILL_DOC;
	}

	public void setBILL_DOC(String bILL_DOC) {
		BILL_DOC = bILL_DOC;
	}

	public String getMEDICAL_DEPT() {
		return MEDICAL_DEPT;
	}

	public void setMEDICAL_DEPT(String mEDICAL_DEPT) {
		MEDICAL_DEPT = mEDICAL_DEPT;
	}

	public String getMEDICAL_NAME() {
		return MEDICAL_NAME;
	}

	public void setMEDICAL_NAME(String mEDICAL_NAME) {
		MEDICAL_NAME = mEDICAL_NAME;
	}

	public Integer getRECEIPT_TYPE() {
		return RECEIPT_TYPE;
	}

	public void setRECEIPT_TYPE(Integer rECEIPT_TYPE) {
		RECEIPT_TYPE = rECEIPT_TYPE;
	}

	public Integer getDEAL_STAT() {
		return DEAL_STAT;
	}

	public void setDEAL_STAT(Integer dEAL_STAT) {
		DEAL_STAT = dEAL_STAT;
	}

	public String getDEAL_ERROR_CODE() {
		return DEAL_ERROR_CODE;
	}

	public void setDEAL_ERROR_CODE(String dEAL_ERROR_CODE) {
		DEAL_ERROR_CODE = dEAL_ERROR_CODE;
	}

	public String getDEAL_ERROR_INFO() {
		return DEAL_ERROR_INFO;
	}

	public void setDEAL_ERROR_INFO(String dEAL_ERROR_INFO) {
		DEAL_ERROR_INFO = dEAL_ERROR_INFO;
	}

	public String getSERVICE_ERROR() {
		return SERVICE_ERROR;
	}

	public void setSERVICE_ERROR(String sERVICE_ERROR) {
		SERVICE_ERROR = sERVICE_ERROR;
	}

	public String getMEDICARE_NO() {
		return MEDICARE_NO;
	}

	public void setMEDICARE_NO(String mEDICARE_NO) {
		MEDICARE_NO = mEDICARE_NO;
	}

	public String getRECEIPT_NO() {
		return RECEIPT_NO;
	}

	public void setRECEIPT_NO(String rECEIPT_NO) {
		RECEIPT_NO = rECEIPT_NO;
	}

	public String getREFUND_NO() {
		return REFUND_NO;
	}

	public void setREFUND_NO(String rEFUND_NO) {
		REFUND_NO = rEFUND_NO;
	}


	public String getADD_INFO() {
		return ADD_INFO;
	}

	public void setADD_INFO(String aDD_INFO) {
		ADD_INFO = aDD_INFO;
	}
	public String getSERVICE_NAME() {
		return SERVICE_NAME;
	}
	public void setSERVICE_NAME(String sERVICE_NAME) {
		SERVICE_NAME = sERVICE_NAME;
	}
	public String getNEW_NO() {
		return NEW_NO;
	}
	public void setNEW_NO(String nEW_NO) {
		NEW_NO = nEW_NO;
	}
	public String getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(String cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}
}
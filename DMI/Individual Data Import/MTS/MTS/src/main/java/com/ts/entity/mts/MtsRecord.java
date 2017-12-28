package com.ts.entity.mts;

import java.util.Date;

public class MtsRecord {
	
	private String RECORD_ID;
	private String STATUS;
	private String RESULT;
	private String NLP_RESULT;
	private String VISIT_ID;
	private String VISIT_TYPE;
	private String DATA_SOURCE;
	private String BATCH_NUM;
	private String DATA_TYPE;
	private String DATA_CLASS;
	private String NLP_ORDER;
	private Date OPERATE_TIME;
	private String PARAMETERS;
	private String DOUBT_DIAG;
	
	
	public String getDOUBT_DIAG() {
		return DOUBT_DIAG;
	}
	public void setDOUBT_DIAG(String dOUBT_DIAG) {
		DOUBT_DIAG = dOUBT_DIAG;
	}
	public String getRECORD_ID() {
		return RECORD_ID;
	}
	public void setRECORD_ID(String rECORD_ID) {
		RECORD_ID = rECORD_ID;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getRESULT() {
		return RESULT;
	}
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}
	public String getNLP_RESULT() {
		return NLP_RESULT;
	}
	public void setNLP_RESULT(String nLP_RESULT) {
		NLP_RESULT = nLP_RESULT;
	}
	public String getVISIT_ID() {
		return VISIT_ID;
	}
	public void setVISIT_ID(String vISIT_ID) {
		VISIT_ID = vISIT_ID;
	}
	public String getVISIT_TYPE() {
		return VISIT_TYPE;
	}
	public void setVISIT_TYPE(String vISIT_TYPE) {
		VISIT_TYPE = vISIT_TYPE;
	}
	public String getDATA_SOURCE() {
		return DATA_SOURCE;
	}
	public void setDATA_SOURCE(String dATA_SOURCE) {
		DATA_SOURCE = dATA_SOURCE;
	}
	public String getBATCH_NUM() {
		return BATCH_NUM;
	}
	public void setBATCH_NUM(String bATCH_NUM) {
		BATCH_NUM = bATCH_NUM;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	public String getDATA_CLASS() {
		return DATA_CLASS;
	}
	public void setDATA_CLASS(String dATA_CLASS) {
		DATA_CLASS = dATA_CLASS;
	}
	public String getNLP_ORDER() {
		return NLP_ORDER;
	}
	public void setNLP_ORDER(String nLP_ORDER) {
		NLP_ORDER = nLP_ORDER;
	}
	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}
	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}
	public String getPARAMETERS() {
		return PARAMETERS;
	}
	public void setPARAMETERS(String pARAMETERS) {
		PARAMETERS = pARAMETERS;
	}
	
	
	
}

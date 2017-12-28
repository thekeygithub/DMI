package com.ts.entity.mts;

import java.util.Date;

public class MtsDBDataDetail {
	
	private String DB_DATA_DETAIL_ID;
	private String DB_DATA_ID;
	private String BATCH_NUM;
	private String DIAG_NAME;
	private String STATUS;
	private String RESULT;
	private Date IMPORT_TIME;
	private Date OPERATE_TIME;
	private String MARK;
	private String NLP_RESULT;
	private String DATA_CLASS_ID;
	private String DOUBT_DIAG;
	private String USELESS_DATA;
	private String DEAL_STATUS;
	
	public String getDEAL_STATUS() {
		return DEAL_STATUS;
	}

	public void setDEAL_STATUS(String dEAL_STATUS) {
		DEAL_STATUS = dEAL_STATUS;
	}

	public String getUSELESS_DATA() {
		return USELESS_DATA;
	}

	public void setUSELESS_DATA(String uSELESS_DATA) {
		USELESS_DATA = uSELESS_DATA;
	}

	public String getDOUBT_DIAG() {
		return DOUBT_DIAG;
	}

	public void setDOUBT_DIAG(String dOUBT_DIAG) {
		DOUBT_DIAG = dOUBT_DIAG;
	}

	public String getDB_DATA_DETAIL_ID() {
		return DB_DATA_DETAIL_ID;
	}

	public void setDB_DATA_DETAIL_ID(String dB_DATA_DETAIL_ID) {
		DB_DATA_DETAIL_ID = dB_DATA_DETAIL_ID;
	}
	public String getDATA_CLASS_ID() {
		return DATA_CLASS_ID;
	}

	public void setDATA_CLASS_ID(String dATA_CLASS_ID) {
		DATA_CLASS_ID = dATA_CLASS_ID;
	}

	public String getNLP_RESULT() {
		return NLP_RESULT;
	}

	public void setNLP_RESULT(String nLP_RESULT) {
		NLP_RESULT = nLP_RESULT;
	}

	public String getMARK() {
		return MARK;
	}

	public void setMARK(String mARK) {
		MARK = mARK;
	}

	public Date getIMPORT_TIME() {
		return IMPORT_TIME;
	}

	public void setIMPORT_TIME(Date iMPORT_TIME) {
		IMPORT_TIME = iMPORT_TIME;
	}

	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}

	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}

	public String getDB_DATA_ID() {
		return DB_DATA_ID;
	}

	public void setDB_DATA_ID(String dB_DATA_ID) {
		DB_DATA_ID = dB_DATA_ID;
	}

	public String getBATCH_NUM() {
		return BATCH_NUM;
	}

	public void setBATCH_NUM(String bATCH_NUM) {
		BATCH_NUM = bATCH_NUM;
	}

	public String getDIAG_NAME() {
		return DIAG_NAME;
	}

	public void setDIAG_NAME(String dIAG_NAME) {
		DIAG_NAME = dIAG_NAME;
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
}

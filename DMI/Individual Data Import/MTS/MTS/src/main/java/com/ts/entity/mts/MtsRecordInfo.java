package com.ts.entity.mts;

import java.util.Date;

public class MtsRecordInfo {
	
	private String RECORD_INFO_ID;
	private String INFO_TYPE;
	private String INFO_STATUS;
	private String DATA_CLASS;
	private String BATCH_NUM;
	private String DATA_SOURCE;
	private String BUSINESS_NUM;
	private String BUSINESS_TYPE;
	private Date OPERATE_TIME;
	private String STANDARD_TYPE;
	private String DB_DATA_ID;
	private String IMPORT_NAME;
	
	public String getIMPORT_NAME() {
		return IMPORT_NAME;
	}
	public void setIMPORT_NAME(String iMPORT_NAME) {
		IMPORT_NAME = iMPORT_NAME;
	}
	public String getDB_DATA_ID() {
		return DB_DATA_ID;
	}
	public void setDB_DATA_ID(String dB_DATA_ID) {
		DB_DATA_ID = dB_DATA_ID;
	}
	public String getSTANDARD_TYPE() {
		return STANDARD_TYPE;
	}
	public void setSTANDARD_TYPE(String sTANDARD_TYPE) {
		STANDARD_TYPE = sTANDARD_TYPE;
	}
	public String getRECORD_INFO_ID() {
		return RECORD_INFO_ID;
	}
	public void setRECORD_INFO_ID(String rECORD_INFO_ID) {
		RECORD_INFO_ID = rECORD_INFO_ID;
	}
	public String getINFO_TYPE() {
		return INFO_TYPE;
	}
	public void setINFO_TYPE(String iNFO_TYPE) {
		INFO_TYPE = iNFO_TYPE;
	}
	public String getINFO_STATUS() {
		return INFO_STATUS;
	}
	public void setINFO_STATUS(String iNFO_STATUS) {
		INFO_STATUS = iNFO_STATUS;
	}
	public String getDATA_CLASS() {
		return DATA_CLASS;
	}
	public void setDATA_CLASS(String dATA_CLASS) {
		DATA_CLASS = dATA_CLASS;
	}
	public String getBATCH_NUM() {
		return BATCH_NUM;
	}
	public void setBATCH_NUM(String bATCH_NUM) {
		BATCH_NUM = bATCH_NUM;
	}
	public String getDATA_SOURCE() {
		return DATA_SOURCE;
	}
	public void setDATA_SOURCE(String dATA_SOURCE) {
		DATA_SOURCE = dATA_SOURCE;
	}
	public String getBUSINESS_NUM() {
		return BUSINESS_NUM;
	}
	public void setBUSINESS_NUM(String bUSINESS_NUM) {
		BUSINESS_NUM = bUSINESS_NUM;
	}
	public String getBUSINESS_TYPE() {
		return BUSINESS_TYPE;
	}
	public void setBUSINESS_TYPE(String bUSINESS_TYPE) {
		BUSINESS_TYPE = bUSINESS_TYPE;
	}
	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}
	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}
	
}

package com.ts.entity.mts;

import java.util.Date;

public class MtsDBData {

	private String DB_DATA_ID;
	private String AREA_CODE;
	private String IMPORT_NAME;
	private Date OPERATE_TIME;
	private String STATUS;
	private String BATCH_NUM;
	private String DATA_CLASS_ID;
	private String DATA_TYPE_ID;
	private String DATA_SOURCE_CODE;
	private String DB_DATA_TYPE;
	

	public String getDB_DATA_TYPE() {
		return DB_DATA_TYPE;
	}

	public void setDB_DATA_TYPE(String dB_DATA_TYPE) {
		DB_DATA_TYPE = dB_DATA_TYPE;
	}

	public String getDATA_SOURCE_CODE() {
		return DATA_SOURCE_CODE;
	}

	public void setDATA_SOURCE_CODE(String dATA_SOURCE_CODE) {
		DATA_SOURCE_CODE = dATA_SOURCE_CODE;
	}

	public String getDATA_TYPE_ID() {
		return DATA_TYPE_ID;
	}

	public void setDATA_TYPE_ID(String dATA_TYPE_ID) {
		DATA_TYPE_ID = dATA_TYPE_ID;
	}

	public String getAREA_CODE() {
		return AREA_CODE;
	}

	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	
	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}

	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}
	
	public String getDATA_CLASS_ID() {
		return DATA_CLASS_ID;
	}

	public void setDATA_CLASS_ID(String dATA_CLASS_ID) {
		DATA_CLASS_ID = dATA_CLASS_ID;
	}

	public String getDB_DATA_ID() {
		return DB_DATA_ID;
	}

	public void setDB_DATA_ID(String dB_DATA_ID) {
		DB_DATA_ID = dB_DATA_ID;
	}

	public String getIMPORT_NAME() {
		return IMPORT_NAME;
	}

	public void setIMPORT_NAME(String iMPORT_NAME) {
		IMPORT_NAME = iMPORT_NAME;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getBATCH_NUM() {
		return BATCH_NUM;
	}

	public void setBATCH_NUM(String bATCH_NUM) {
		BATCH_NUM = bATCH_NUM;
	}

}

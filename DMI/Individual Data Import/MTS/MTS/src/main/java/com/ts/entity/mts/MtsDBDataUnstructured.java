package com.ts.entity.mts;

import java.util.Date;

public class MtsDBDataUnstructured {

	private String DATA_UNSTRUCTURED_ID;
	private String AREA_CODE;
	private String IMPORT_NAME;
	private String STATUS;
	private String BATCH_NUM;
	private String DATA_SOURCE_CODE;
	private Date OPERATE_TIME;
	
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}
	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}
	public String getDATA_UNSTRUCTURED_ID() {
		return DATA_UNSTRUCTURED_ID;
	}
	public void setDATA_UNSTRUCTURED_ID(String dATA_UNSTRUCTURED_ID) {
		DATA_UNSTRUCTURED_ID = dATA_UNSTRUCTURED_ID;
	}
	public String getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	public String getIMPORT_NAME() {
		return IMPORT_NAME;
	}
	public void setIMPORT_NAME(String iMPORT_NAME) {
		IMPORT_NAME = iMPORT_NAME;
	}
	public String getBATCH_NUM() {
		return BATCH_NUM;
	}
	public void setBATCH_NUM(String bATCH_NUM) {
		BATCH_NUM = bATCH_NUM;
	}
	public String getDATA_SOURCE_CODE() {
		return DATA_SOURCE_CODE;
	}
	public void setDATA_SOURCE_CODE(String dATA_SOURCE_CODE) {
		DATA_SOURCE_CODE = dATA_SOURCE_CODE;
	}
}

package com.ts.entity.mts;

import java.util.Date;

public class MtsData {

	private Integer DATA_ID; // 字典表主键
	private String DATA_TYPE_ID; // 字典表主键
	private String ORIG_DATA_ID; // 字典表主键
	private String ORIG_DATA_NAME; // 字典表主键
	private String ORIG_DATA_STR; // 字典表主键
	private String ORIG_DATA_MD5; // 字典表主键
	// 诊疗标识 1、科室 2、诊断 3、诊疗
	private String ZL_FLAG;
	private String BATCH_NO;
	private String AREA_ID;
	private Date IMP_DATE;
	// 加载标识 0、未加载 1 已加载
	private String LOAD_FLAG;
	private String DEL_FLAG;

	public String getDEL_FLAG() {

		return DEL_FLAG;
	}

	public void setDEL_FLAG(String dEL_FLAG) {

		DEL_FLAG = dEL_FLAG;
	}

	public String getLOAD_FLAG() {

		return LOAD_FLAG;
	}

	public void setLOAD_FLAG(String lOAD_FLAG) {

		LOAD_FLAG = lOAD_FLAG;
	}

	public Date getIMP_DATE() {

		return IMP_DATE;
	}

	public void setIMP_DATE(Date iMP_DATE) {

		IMP_DATE = iMP_DATE;
	}

	public String getBATCH_NO() {

		return BATCH_NO;
	}

	public void setBATCH_NO(String bATCH_NO) {

		BATCH_NO = bATCH_NO;
	}

	public String getAREA_ID() {

		return AREA_ID;
	}

	public void setAREA_ID(String aREA_ID) {

		AREA_ID = aREA_ID;
	}

	public String getZL_FLAG() {
		return ZL_FLAG;
	}

	public void setZL_FLAG(String zL_FLAG) {
		ZL_FLAG = zL_FLAG;
	}

	public Integer getDATA_ID() {

		return DATA_ID;
	}

	public void setDATA_ID(Integer dATA_ID) {

		DATA_ID = dATA_ID;
	}

	public String getDATA_TYPE_ID() {

		return DATA_TYPE_ID;
	}

	public void setDATA_TYPE_ID(String dATA_TYPE_ID) {

		DATA_TYPE_ID = dATA_TYPE_ID;
	}

	public String getORIG_DATA_ID() {

		return ORIG_DATA_ID;
	}

	public void setORIG_DATA_ID(String oRIG_DATA_ID) {

		ORIG_DATA_ID = oRIG_DATA_ID;
	}

	public String getORIG_DATA_NAME() {

		return ORIG_DATA_NAME;
	}

	public void setORIG_DATA_NAME(String oRIG_DATA_NAME) {

		ORIG_DATA_NAME = oRIG_DATA_NAME;
	}

	public String getORIG_DATA_STR() {

		return ORIG_DATA_STR;
	}

	public void setORIG_DATA_STR(String oRIG_DATA_STR) {

		ORIG_DATA_STR = oRIG_DATA_STR;
	}

	public String getORIG_DATA_MD5() {

		return ORIG_DATA_MD5;
	}

	public void setORIG_DATA_MD5(String oRIG_DATA_MD5) {

		ORIG_DATA_MD5 = oRIG_DATA_MD5;
	}

}

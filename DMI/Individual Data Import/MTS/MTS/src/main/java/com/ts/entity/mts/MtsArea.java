package com.ts.entity.mts;

import java.util.Date;

public class MtsArea {

	private String AREA_ID;
	private String AREA_NAME;
	private String AREA_CODE;
	private String REMARK;
	private Date OPERATE_TIME;

	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}

	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}

	public String getAREA_ID() {
		return AREA_ID;
	}

	public void setAREA_ID(String aREA_ID) {
		AREA_ID = aREA_ID;
	}

	public String getAREA_NAME() {
		return AREA_NAME;
	}

	public void setAREA_NAME(String aREA_NAME) {
		AREA_NAME = aREA_NAME;
	}

	public String getAREA_CODE() {
		return AREA_CODE;
	}

	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}

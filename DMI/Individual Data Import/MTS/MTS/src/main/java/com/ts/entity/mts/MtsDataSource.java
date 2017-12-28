package com.ts.entity.mts;

import java.util.Date;

public class MtsDataSource {

	private String DATA_SOURCE_ID;
	
	private String FLAG;
	
	private String DESCRIPTION;
	
	private String REMARK;

	private Date OPERATE_TIME;
	
	public Date getOPERATE_TIME() {
		return OPERATE_TIME;
	}

	public void setOPERATE_TIME(Date oPERATE_TIME) {
		OPERATE_TIME = oPERATE_TIME;
	}

	public String getDATA_SOURCE_ID() {
		return DATA_SOURCE_ID;
	}

	public void setDATA_SOURCE_ID(String dATA_SOURCE_ID) {
		DATA_SOURCE_ID = dATA_SOURCE_ID;
	}

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}

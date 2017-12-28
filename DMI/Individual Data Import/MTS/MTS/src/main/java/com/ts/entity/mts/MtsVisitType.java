package com.ts.entity.mts;

import java.util.Date;

public class MtsVisitType {
	
	private String VISIT_TYPE_ID;
	
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

	public String getVISIT_TYPE_ID() {
		return VISIT_TYPE_ID;
	}

	public void setVISIT_TYPE_ID(String vISIT_TYPE_ID) {
		VISIT_TYPE_ID = vISIT_TYPE_ID;
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

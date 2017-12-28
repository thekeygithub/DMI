package com.ts.entity.mts;

import java.util.Date;

public class LoadRule {
	private String LOAD_RULE_NAME;
	private String LOAD_RULE_ID;
	private String DATA_TYPE_ID;
	private String KEY_GEN_RULE;
	private String VALUE_GEN_RULE;
	private String ZL_FLAG;
	private String AREA_ID;
	private Date OP_DATE;

	public Date getOP_DATE() {

		return OP_DATE;
	}

	public void setOP_DATE(Date oP_DATE) {

		OP_DATE = oP_DATE;
	}

	public String getLOAD_RULE_NAME() {
		return LOAD_RULE_NAME;
	}

	public void setLOAD_RULE_NAME(String lOAD_RULE_NAME) {
		LOAD_RULE_NAME = lOAD_RULE_NAME;
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

	public String getLOAD_RULE_ID() {

		return LOAD_RULE_ID;
	}

	public void setLOAD_RULE_ID(String lOAD_RULE_ID) {

		LOAD_RULE_ID = lOAD_RULE_ID;
	}

	public String getDATA_TYPE_ID() {

		return DATA_TYPE_ID;
	}

	public void setDATA_TYPE_ID(String dATA_TYPE_ID) {

		DATA_TYPE_ID = dATA_TYPE_ID;
	}

	public String getKEY_GEN_RULE() {

		return KEY_GEN_RULE;
	}

	public void setKEY_GEN_RULE(String kEY_GEN_RULE) {

		KEY_GEN_RULE = kEY_GEN_RULE;
	}

	public String getVALUE_GEN_RULE() {

		return VALUE_GEN_RULE;
	}

	public void setVALUE_GEN_RULE(String vALUE_GEN_RULE) {

		VALUE_GEN_RULE = vALUE_GEN_RULE;
	}

}

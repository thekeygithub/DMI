package com.ts.entity.mts;

public class NlpTerm {

	private int TERM_ID;
	private String TERM_CN_NAME;
	private String TERM_EN_NAME;
	private String TERM_CLASSCODE;
	private String TERM_DATATYPE;
	private String COLOR;
	private String FLAG;

	public String getCOLOR() {

		return COLOR;
	}

	public void setCOLOR(String cOLOR) {

		COLOR = cOLOR;
	}

	public int getTERM_ID() {
		return TERM_ID;
	}

	public void setTERM_ID(int tERM_ID) {
		TERM_ID = tERM_ID;
	}

	public String getTERM_CN_NAME() {
		return TERM_CN_NAME;
	}

	public void setTERM_CN_NAME(String tERM_CN_NAME) {
		TERM_CN_NAME = tERM_CN_NAME;
	}

	public String getTERM_EN_NAME() {
		return TERM_EN_NAME;
	}

	public void setTERM_EN_NAME(String tERM_EN_NAME) {
		TERM_EN_NAME = tERM_EN_NAME;
	}

	public String getTERM_CLASSCODE() {
		return TERM_CLASSCODE;
	}

	public void setTERM_CLASSCODE(String tERM_CLASSCODE) {
		TERM_CLASSCODE = tERM_CLASSCODE;
	}

	public String getTERM_DATATYPE() {
		return TERM_DATATYPE;
	}

	public void setTERM_DATATYPE(String tERM_DATATYPE) {
		TERM_DATATYPE = tERM_DATATYPE;
	}

	public String getFLAG() {
		return FLAG;
	}

	public void setFLAG(String fLAG) {
		FLAG = fLAG;
	}

}

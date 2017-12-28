
package com.ts.entity.mts;

import java.util.Date;

public class MtsCorpusEntity {

	private int ID;
	private String ENTITY_NAME;
	private String ENTITY_TYPE_ID;
	private String IS_CONFRIM;
	private String ENTITY_TYPE_NAME;
	private int CORPUS_DETAIL_ID;
	private String START_TEXT_OFF;
	private String END_TEXT_OFF;
	private String START_HTML_OFF;
	private String END_HTML_OFF;
	private String SPAN_NUM;
	private Date EDIT_DATE;
	private String COLOR;

	public String getCOLOR() {
	
		return COLOR;
	}

	public void setCOLOR(String cOLOR) {
	
		COLOR = cOLOR;
	}

	public Date getEDIT_DATE() {
		return EDIT_DATE;
	}

	public void setEDIT_DATE(Date eDIT_DATE) {
		EDIT_DATE = eDIT_DATE;
	}

	public String getSPAN_NUM() {

		return SPAN_NUM;
	}

	public void setSPAN_NUM(String sPAN_NUM) {

		SPAN_NUM = sPAN_NUM;
	}

	public int getCORPUS_DETAIL_ID() {

		return CORPUS_DETAIL_ID;
	}

	public void setCORPUS_DETAIL_ID(int cORPUS_DETAIL_ID) {

		CORPUS_DETAIL_ID = cORPUS_DETAIL_ID;
	}

	public int getID() {

		return ID;
	}

	public void setID(int iD) {

		ID = iD;
	}

	public String getENTITY_NAME() {

		return ENTITY_NAME;
	}

	public void setENTITY_NAME(String eNTITY_NAME) {

		ENTITY_NAME = eNTITY_NAME;
	}

	public String getENTITY_TYPE_ID() {

		return ENTITY_TYPE_ID;
	}

	public void setENTITY_TYPE_ID(String eNTITY_TYPE_ID) {

		ENTITY_TYPE_ID = eNTITY_TYPE_ID;
	}

	public String getIS_CONFRIM() {

		return IS_CONFRIM;
	}

	public void setIS_CONFRIM(String iS_CONFRIM) {

		IS_CONFRIM = iS_CONFRIM;
	}

	public String getENTITY_TYPE_NAME() {

		return ENTITY_TYPE_NAME;
	}

	public void setENTITY_TYPE_NAME(String eNTITY_TYPE_NAME) {

		ENTITY_TYPE_NAME = eNTITY_TYPE_NAME;
	}

	public String getSTART_TEXT_OFF() {

		return START_TEXT_OFF;
	}

	public void setSTART_TEXT_OFF(String sTART_TEXT_OFF) {

		START_TEXT_OFF = sTART_TEXT_OFF;
	}

	public String getEND_TEXT_OFF() {

		return END_TEXT_OFF;
	}

	public void setEND_TEXT_OFF(String eND_TEXT_OFF) {

		END_TEXT_OFF = eND_TEXT_OFF;
	}

	public String getSTART_HTML_OFF() {

		return START_HTML_OFF;
	}

	public void setSTART_HTML_OFF(String sTART_HTML_OFF) {

		START_HTML_OFF = sTART_HTML_OFF;
	}

	public String getEND_HTML_OFF() {

		return END_HTML_OFF;
	}

	public void setEND_HTML_OFF(String eND_HTML_OFF) {

		END_HTML_OFF = eND_HTML_OFF;
	}
}

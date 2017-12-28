
package com.ts.entity.mts;

import java.util.Date;

public class MtsCorpusDetail {

	private int ID;
	private String ORIG_CORPUS;
	private String TYPE;
	private Date EDIT_DATE;
	private int CORPUS_ID;
	private String P_ID;
	private String P_HTML;

	public String getP_ID() {

		return P_ID;
	}

	public void setP_ID(String p_ID) {

		P_ID = p_ID;
	}

	public String getP_HTML() {

		return P_HTML;
	}

	public void setP_HTML(String p_HTML) {

		P_HTML = p_HTML;
	}

	public int getCORPUS_ID() {

		return CORPUS_ID;
	}

	public void setCORPUS_ID(int cORPUS_ID) {

		CORPUS_ID = cORPUS_ID;
	}

	public Date getEDIT_DATE() {
		return EDIT_DATE;
	}

	public void setEDIT_DATE(Date eDIT_DATE) {
		EDIT_DATE = eDIT_DATE;
	}

	public String getTYPE() {

		return TYPE;
	}

	public void setTYPE(String tYPE) {

		TYPE = tYPE;
	}

	public int getID() {

		return ID;
	}

	public void setID(int iD) {

		ID = iD;
	}

	public String getORIG_CORPUS() {

		return ORIG_CORPUS;
	}

	public void setORIG_CORPUS(String oRIG_CORPUS) {

		ORIG_CORPUS = oRIG_CORPUS;
	}

}

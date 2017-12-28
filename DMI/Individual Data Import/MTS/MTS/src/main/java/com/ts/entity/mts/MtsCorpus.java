
package com.ts.entity.mts;

import java.util.Date;

public class MtsCorpus {

	private int ID;
	private String ORIG_CONTENT;
	private String NEW_CONTENT;
	private String FILE_FULL_NAME;
	private String FILE_NAME;
	private Date EDIT_DATE;


	public int getID() {
	
		return ID;
	}

	public void setID(int iD) {
	
		ID = iD;
	}

	public Date getEDIT_DATE() {

		return EDIT_DATE;
	}

	public void setEDIT_DATE(Date eDIT_DATE) {

		EDIT_DATE = eDIT_DATE;
	}

	

	public String getORIG_CONTENT() {

		return ORIG_CONTENT;
	}

	public void setORIG_CONTENT(String oRIG_CONTENT) {

		ORIG_CONTENT = oRIG_CONTENT;
	}

	public String getNEW_CONTENT() {

		return NEW_CONTENT;
	}

	public void setNEW_CONTENT(String nEW_CONTENT) {

		NEW_CONTENT = nEW_CONTENT;
	}

	public String getFILE_FULL_NAME() {

		return FILE_FULL_NAME;
	}

	public void setFILE_FULL_NAME(String fILE_FULL_NAME) {

		FILE_FULL_NAME = fILE_FULL_NAME;
	}

	public String getFILE_NAME() {

		return FILE_NAME;
	}

	public void setFILE_NAME(String fILE_NAME) {

		FILE_NAME = fILE_NAME;
	}

}

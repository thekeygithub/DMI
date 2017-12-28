package com.ts.entity.mts;

public class MtsDBDataUnstructuredRelevance {

	private String DATA_UNSTRUCTURED_RELEVANCE_ID;
	private String DATA_UNSTRUCTURED_ID;
	private String ORDER_NUM;
	private String ORIG_DATA;
	
	public String getORIG_DATA() {
		return ORIG_DATA;
	}
	public void setORIG_DATA(String oRIG_DATA) {
		ORIG_DATA = oRIG_DATA;
	}
	public String getORDER_NUM() {
		return ORDER_NUM;
	}
	public void setORDER_NUM(String oRDER_NUM) {
		ORDER_NUM = oRDER_NUM;
	}
	public String getDATA_UNSTRUCTURED_RELEVANCE_ID() {
		return DATA_UNSTRUCTURED_RELEVANCE_ID;
	}
	public void setDATA_UNSTRUCTURED_RELEVANCE_ID(String dATA_UNSTRUCTURED_RELEVANCE_ID) {
		DATA_UNSTRUCTURED_RELEVANCE_ID = dATA_UNSTRUCTURED_RELEVANCE_ID;
	}
	public String getDATA_UNSTRUCTURED_ID() {
		return DATA_UNSTRUCTURED_ID;
	}
	public void setDATA_UNSTRUCTURED_ID(String dATA_UNSTRUCTURED_ID) {
		DATA_UNSTRUCTURED_ID = dATA_UNSTRUCTURED_ID;
	}
}

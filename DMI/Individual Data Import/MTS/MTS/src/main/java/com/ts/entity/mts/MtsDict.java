package com.ts.entity.mts;

public class MtsDict {

	private String DID; // 字典表主键
	private String DNAME; // 字段名
	private String LOADCODE; // 加载码
	private String MATCHCODE; // 匹配码
	private String DATACLASS; // 聚类
	private String TYPE;

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getDID() {
		return DID;
	}

	public void setDID(String dID) {
		DID = dID;
	}

	public String getDNAME() {
		return DNAME;
	}

	public void setDNAME(String dNAME) {
		DNAME = dNAME;
	}

	public String getLOADCODE() {
		return LOADCODE;
	}

	public void setLOADCODE(String lOADCODE) {
		LOADCODE = lOADCODE;
	}

	public String getMATCHCODE() {
		return MATCHCODE;
	}

	public void setMATCHCODE(String mATCHCODE) {
		MATCHCODE = mATCHCODE;
	}

	public String getDATACLASS() {
		return DATACLASS;
	}

	public void setDATACLASS(String dATACLASS) {
		DATACLASS = dATACLASS;
	}

}

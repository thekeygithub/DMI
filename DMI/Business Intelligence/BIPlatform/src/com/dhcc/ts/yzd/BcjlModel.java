package com.dhcc.ts.yzd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class BcjlModel implements Comparator<BcjlModel> {
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String CBCLXMC;
	private String CZYH;
	private String TJLNR;
	private String CJLRMC;
	private String DJLSJ;
	private String CXM;
	private String CRYKSMC;
	private String HOS_NAME;
	private String CBRXM;
	
	public int compare(BcjlModel obj1, BcjlModel obj2) {
        try {
			if (sf.parse(obj1.getDJLSJ()).after(sf.parse(obj2.getDJLSJ()))) {  
			    return 1;  
			} else {  
			    return -1;  
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return -1;  
    }  
	
	public String getCBCLXMC() {
		return CBCLXMC;
	}
	public void setCBCLXMC(String cBCLXMC) {
		CBCLXMC = cBCLXMC;
	}
	public String getCZYH() {
		return CZYH;
	}
	public void setCZYH(String cZYH) {
		CZYH = cZYH;
	}
	public String getTJLNR() {
		return TJLNR;
	}
	public void setTJLNR(String tJLNR) {
		TJLNR = tJLNR;
	}
	public String getCJLRMC() {
		return CJLRMC;
	}
	public void setCJLRMC(String cJLRMC) {
		CJLRMC = cJLRMC;
	}
	public String getDJLSJ() {
		return DJLSJ;
	}
	public void setDJLSJ(String dJLSJ) {
		DJLSJ = dJLSJ;
	}
	public String getCXM() {
		return CXM;
	}
	public void setCXM(String cXM) {
		CXM = cXM;
	}
	public String getCRYKSMC() {
		return CRYKSMC;
	}
	public void setCRYKSMC(String cRYKSMC) {
		CRYKSMC = cRYKSMC;
	}
	public String getHOS_NAME() {
		return HOS_NAME;
	}
	public void setHOS_NAME(String hOS_NAME) {
		HOS_NAME = hOS_NAME;
	}
	public String getCBRXM() {
		return CBRXM;
	}
	public void setCBRXM(String cBRXM) {
		CBRXM = cBRXM;
	}
}

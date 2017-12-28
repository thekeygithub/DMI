package com.ts.service.pay;

import com.ts.entity.P_drugstore_inte_info;
import com.ts.entity.P_inte_info;

public interface PayManager {

	public void insertPay(P_inte_info pif) throws Exception;
	
	public String findUserID() throws Exception;
	
	public String findInteID() throws Exception;
	
	public String findBillID() throws Exception;
	
	public void updatePrList(P_inte_info pif) throws Exception;

	public void insertPay_Drugstore(P_drugstore_inte_info pif) throws Exception ;
}
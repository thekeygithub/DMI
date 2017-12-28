package com.ts.controller.app.SearchAPI.PaymentAPI.util;

import com.ts.entity.CitizenCardQuery.V01InputCitizenCardQueryBean;
import com.ts.entity.CitizenCardQuery.V01OutCitizenCardQueryBean;

public interface CitizenCardQueryWsdl {
	
	public V01OutCitizenCardQueryBean getCitizenCardQuery(V01InputCitizenCardQueryBean bean);
}

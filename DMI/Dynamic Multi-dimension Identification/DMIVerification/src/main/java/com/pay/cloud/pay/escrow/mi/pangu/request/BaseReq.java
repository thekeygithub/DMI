package com.pay.cloud.pay.escrow.mi.pangu.request;

import com.pay.cloud.core.common.JsonStringUtils;

public class BaseReq {
	
	public String takeJson(){
		return JsonStringUtils.objectToJsonString(this);
	}

}

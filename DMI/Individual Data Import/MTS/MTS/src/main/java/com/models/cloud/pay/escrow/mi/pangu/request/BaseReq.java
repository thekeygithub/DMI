package com.models.cloud.pay.escrow.mi.pangu.request;

import com.models.cloud.core.common.JsonStringUtils;

public class BaseReq {
	
	public String takeJson(){
		return JsonStringUtils.objectToJsonString(this);
	}

}

package com.models.cloud.pay.escrow.mi.pangu.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.constants.PropertiesConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.pay.escrow.mi.pangu.utils.PanguAes;
import com.models.cloud.util.hint.Propertie;

public class RequestParam<T extends BaseReq> {

	private String channel;
	private int cmd;
	private BaseParam<T> req;
	
	public Map<String,String> takeProperties(){
		Map<String,String> map  = new HashMap<String,String>();
		if(req!=null) map.put("req", PanguAes.setEncrypt(JsonStringUtils.objectToJsonString(req), Propertie.APPLICATION.value(PropertiesConstant.TRANSPARENT_AESKEY)));
		map.put("cmd", String.valueOf(getCmd()));
		if(StringUtils.isNotEmpty(getChannel()))map.put("channel", String.valueOf(getChannel()));
		return map;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public BaseParam<T> getReq() {
		return req;
	}

	public void setReq(BaseParam<T> req) {
		this.req = req;
	}
	
}

package com.pay.cloud.pay.escrow.mi.pangu.service.impl;

import com.alipay.api.internal.util.WebUtils;
import com.pay.cloud.constants.PropertiesConstant;
import com.pay.cloud.pay.escrow.alipay.utils.AlipayConstants;
import com.pay.cloud.pay.escrow.mi.pangu.request.BaseParam;
import com.pay.cloud.pay.escrow.mi.pangu.request.BaseReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.RequestParam;
import com.pay.cloud.pay.escrow.mi.pangu.utils.PanguAes;
import com.pay.cloud.util.hint.Propertie;

public class PanguBaseServiceImpl {
	protected <T extends BaseReq> RequestParam<T> getRequest(String fixid,String cycid,int cmd,T p)throws Exception{
		BaseParam<T> param = new BaseParam<T>();
        param.setCmd(cmd);
        param.setFixid(fixid);
        param.setParam(p);
        param.setCycid(cycid);
        
        RequestParam<T> req = new RequestParam<T>();
        req.setChannel(Propertie.APPLICATION.value(PropertiesConstant.TRANSPARENT_SYSTEM));
        req.setCmd(param.getCmd());
        req.setReq(param);
        return req;
	}
	
	protected <T extends BaseReq> String getRestlt(RequestParam<T> req)throws Exception{
		String secResult = WebUtils.doPost(Propertie.APPLICATION.value(PropertiesConstant.TRANSPARENT_URL), req.takeProperties(),AlipayConstants.CHARSET_UTF8, AlipayConstants.CONNECT_TIMEOUT, AlipayConstants.READ_TIMEOUT);
//		String secResult = HttpUtils.send(Propertie.APPLICATION.value(PropertiesConstant.TRANSPARENT_URL), req.takeProperties(),HttpUtils.ENCODING);
        return PanguAes.getDecrypt(secResult, Propertie.APPLICATION.value(PropertiesConstant.TRANSPARENT_AESKEY));
	}
}

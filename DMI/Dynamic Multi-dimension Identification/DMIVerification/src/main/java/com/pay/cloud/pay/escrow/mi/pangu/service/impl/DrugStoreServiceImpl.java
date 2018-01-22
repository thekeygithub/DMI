package com.pay.cloud.pay.escrow.mi.pangu.service.impl;

import org.springframework.stereotype.Service;

import com.pay.cloud.core.common.JsonStringUtils;
import com.pay.cloud.pay.escrow.mi.pangu.request.Toll2Req;
import com.pay.cloud.pay.escrow.mi.pangu.request.YdAccoutInfoReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.YdQueryInfoReq;
import com.pay.cloud.pay.escrow.mi.pangu.request.YdTollReq;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdBackResultRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdOrderResultRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdSfmxRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdXiaofxxRes;
import com.pay.cloud.pay.escrow.mi.pangu.response.YdYSfmxRes;
import com.pay.cloud.pay.escrow.mi.pangu.service.DrugstoreServicce;
import com.pay.cloud.pay.escrow.mi.pangu.utils.PGConstants;

@Service
public class DrugStoreServiceImpl extends PanguBaseServiceImpl implements
		DrugstoreServicce {

	@Override
	public YdYSfmxRes executeYdYSfmx(String fixid, String cycid, Toll2Req p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_2001,p));
		YdYSfmxRes res = JsonStringUtils.jsonToObjectIgnorePro(result, YdYSfmxRes.class);
        return res;
	}

	@Override
	public YdSfmxRes executeYdSfmx(String fixid, String cycid, Toll2Req p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_2002,p));
		YdSfmxRes res = JsonStringUtils.jsonToObjectIgnorePro(result, YdSfmxRes.class);
        return res;
	}

	@Override
	public YdBackResultRes executeYdBack(String fixid, String cycid, YdTollReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_2003,p));
		YdBackResultRes res = JsonStringUtils.jsonToObjectIgnorePro(result, YdBackResultRes.class);
        return res;
	}

	@Override
	public YdOrderResultRes queryYdOrder(String fixid, String cycid, YdQueryInfoReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_2004,p));
		YdOrderResultRes res = JsonStringUtils.jsonToObjectIgnorePro(result, YdOrderResultRes.class);
        return res;
	}

	@Override
	public YdXiaofxxRes queryYdYSfmx(String fixid, String cycid,
			YdAccoutInfoReq p) throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_2005,p));
		YdXiaofxxRes res = JsonStringUtils.jsonToObjectIgnorePro(result, YdXiaofxxRes.class);
        return res;
	}
}

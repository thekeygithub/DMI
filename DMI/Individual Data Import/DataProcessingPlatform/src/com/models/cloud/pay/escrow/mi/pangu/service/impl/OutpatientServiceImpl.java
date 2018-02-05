package com.models.cloud.pay.escrow.mi.pangu.service.impl;

import org.springframework.stereotype.Service;

import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.pay.escrow.mi.pangu.request.MzAccoutInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.MzQueryInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.MzTollReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TollsReq;
import com.models.cloud.pay.escrow.mi.pangu.response.MzBackResultRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzOrderResultRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzSfmxRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzXiaofxxRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MzYSfmxRes;
import com.models.cloud.pay.escrow.mi.pangu.service.OutpatientService;
import com.models.cloud.pay.escrow.mi.pangu.utils.PGConstants;

@Service
public class OutpatientServiceImpl extends PanguBaseServiceImpl implements OutpatientService {

	@Override
	public MzYSfmxRes executeMzYSfmx(String fixid, String cycid, TollsReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_3001,p));
		MzYSfmxRes res = JsonStringUtils.jsonToObjectIgnorePro(result, MzYSfmxRes.class);
        return res;
	}

	@Override
	public MzSfmxRes executeMzSfmx(String fixid, String cycid, TollsReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_3002,p));
		MzSfmxRes res = JsonStringUtils.jsonToObjectIgnorePro(result, MzSfmxRes.class);
        return res;
	}

	@Override
	public MzBackResultRes executeMzBack(String fixid, String cycid, MzTollReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_3003,p));
		MzBackResultRes res = JsonStringUtils.jsonToObjectIgnorePro(result, MzBackResultRes.class);
        return res;
	}

	@Override
	public MzOrderResultRes queryMzOrder(String fixid, String cycid, MzQueryInfoReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_3004,p));
		MzOrderResultRes res = JsonStringUtils.jsonToObjectIgnorePro(result, MzOrderResultRes.class);
        return res;
	}

	@Override
	public MzXiaofxxRes queryMzYSfmx(String fixid, String cycid, MzAccoutInfoReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_3005,p));
		MzXiaofxxRes res = JsonStringUtils.jsonToObjectIgnorePro(result, MzXiaofxxRes.class);
        return res;
	}
}

package com.models.cloud.pay.escrow.mi.pangu.service.impl;

import org.springframework.stereotype.Service;

import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.pay.escrow.mi.pangu.request.MxbInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.PersonReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TollInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TreatmentStatusReq;
import com.models.cloud.pay.escrow.mi.pangu.response.BzInfoRes;
import com.models.cloud.pay.escrow.mi.pangu.response.MxbRes;
import com.models.cloud.pay.escrow.mi.pangu.response.OrderStatusRes;
import com.models.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.models.cloud.pay.escrow.mi.pangu.response.SystemDateRes;
import com.models.cloud.pay.escrow.mi.pangu.response.TreatmentStatusRes;
import com.models.cloud.pay.escrow.mi.pangu.response.YaopInfoRes;
import com.models.cloud.pay.escrow.mi.pangu.service.CommInfoService;
import com.models.cloud.pay.escrow.mi.pangu.utils.PGConstants;

@Service
public class CommInfoServiceImpl extends PanguBaseServiceImpl implements CommInfoService {

	@Override
	public SystemDateRes querySysTime() throws Exception {
		String result = getRestlt(getRequest(null,null,PGConstants.CMD_1001,null));
		SystemDateRes res = JsonStringUtils.jsonToObjectIgnorePro(result, SystemDateRes.class);
        return res;
	}
	
	@Override
	public PersonInfoRes queryPerInfo(String fixid, PersonReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,null,PGConstants.CMD_1002,p));
		PersonInfoRes res = JsonStringUtils.jsonToObjectIgnorePro(result, PersonInfoRes.class);
        return res;
	}

	@Override
	public TreatmentStatusRes queryTreatStatus(String fixid, TreatmentStatusReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,null,PGConstants.CMD_1015,p));
		TreatmentStatusRes res = JsonStringUtils.jsonToObjectIgnorePro(result, TreatmentStatusRes.class);
        return res;
	}

	@Override
	public MxbRes queryChronicDisease(String fixid, String cycid, MxbInfoReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,cycid,PGConstants.CMD_1003,p));
		MxbRes res = JsonStringUtils.jsonToObjectIgnorePro(result, MxbRes.class);
        return res;
	}

	@Override
	public BzInfoRes queryDiseaseInfo(String fixid) throws Exception {
		String result = getRestlt(getRequest(fixid,null,PGConstants.CMD_1004,null));
		BzInfoRes res = JsonStringUtils.jsonToObjectIgnorePro(result, BzInfoRes.class);
        return res;
	}

	@Override
	public YaopInfoRes queryDrugInfo(String fixid) throws Exception {
		String result = getRestlt(getRequest(fixid,null,PGConstants.CMD_1005,null));
		YaopInfoRes res = JsonStringUtils.jsonToObjectIgnorePro(result, YaopInfoRes.class);
        return res;
	}

	@Override
	public OrderStatusRes queryOrderStatus(String fixid, TollInfoReq p)
			throws Exception {
		String result = getRestlt(getRequest(fixid,null,PGConstants.CMD_1016,p));
		OrderStatusRes res = JsonStringUtils.jsonToObjectIgnorePro(result, OrderStatusRes.class);
        return res;
	}

}

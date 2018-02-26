package com.models.cloud.pay.escrow.mts.service.impl;

import org.springframework.stereotype.Service;

import com.models.cloud.constants.PropertiesConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.pay.common.http.utils.HttpUtils;
import com.models.cloud.pay.escrow.mts.request.MtsParam;
import com.models.cloud.pay.escrow.mts.request.MtsReqParam;
import com.models.cloud.pay.escrow.mts.request.MtsParam.DiagInfo;
import com.models.cloud.pay.escrow.mts.response.MtsResp;
import com.models.cloud.pay.escrow.mts.response.MtsRespResult;
import com.models.cloud.pay.escrow.mts.response.MtsRespResult.MtsResult;
import com.models.cloud.pay.escrow.mts.service.MTSService;
import com.models.cloud.util.hint.Propertie;

/**
 * MTS服务请求
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午11:33:15
 */
@Service("mTSServiceImpl")
public class MTSServiceImpl implements MTSService {

	@Override
	public MtsRespResult mts(MtsParam req) throws Exception {
		//构造请求参数
		MtsReqParam param = new MtsReqParam();
		param.setVisitId(req.getVisitId());
		param.setVisitType(req.getVisitType());
		param.setDataType(req.getDataType());
		param.setDataSource(req.getDataSource());
		param.setParameters(req.getDiagParams());
		//请求MTS服务
		String result = HttpUtils.httpPostWithJSON(Propertie.APPLICATION.value(PropertiesConstant.MTS_SERVICE_URL), 
				JsonStringUtils.objectToJsonString(param),HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		//结果转为对象
		MtsResp resp = JsonStringUtils.jsonToObjectIgnorePro(result, MtsResp.class);
		MtsRespResult respResult = new MtsRespResult();
		respResult.setBatchNum(resp.getBatchNum());
		respResult.setDataClass(resp.getDataClass());
		respResult.setOperTime(resp.getOperTime());
		respResult.setStatus(resp.getStatus());
		//设置原参数
		MtsParam p = new MtsParam();
		p.setDataSource(resp.getDataSource());
		p.setDataType(resp.getDataType());
		p.setVisitId(resp.getVisitId());
		p.setVisitType(resp.getVisitType());
		p.setDiag(new DiagInfo(resp.getParameters()));
		respResult.setReq(p);
		//设置匹配结果
		respResult.setResult(MtsResult.createMtsResult(resp.getResult()));
		return respResult;
	}
	
	public static void main(String[] args) {
		try {
			MtsParam req  = new MtsParam();
			req.setVisitId("123456789012");
			req.setVisitType("01");
			req.setDataType("02");
			req.setDataSource("10001");
			DiagInfo info = new DiagInfo();
			info.setCode("I63.903");
			info.setDiag("颈椎病,高血压,糖尿病");
			req.setDiag(info);
			
			MTSServiceImpl service = new MTSServiceImpl();
			MtsRespResult r = service.mts(req);
			System.out.println(r);
			
			
//			MtsReqParam param = new MtsReqParam();
//			param.setVisitId(req.getVisitId());
//			param.setVisitType(req.getVisitType());
//			param.setDataType(req.getDataType());
//			param.setDataSource(req.getDataSource());
//			param.setParameters(req.getDiagParams());
//			HttpUtils.httpPostWithJSON("http://113.6.246.23/MTS/mtsStandardize/standardizeData.do", 
//					JsonStringUtils.objectToJsonString(param),HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

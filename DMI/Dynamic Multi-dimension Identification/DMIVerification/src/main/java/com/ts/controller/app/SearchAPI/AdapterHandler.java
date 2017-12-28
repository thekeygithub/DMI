package com.ts.controller.app.SearchAPI;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ts.controller.app.SearchAPI.PayResultHandler.IPayRSHandler;
import com.ts.entity.app.AppOrganiKey;
import com.ts.util.Logger;
import com.ts.util.Enum.EnumStatus;

import net.sf.json.JSONObject;

@Component
public class AdapterHandler  {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "preSectionAccountsPAY")
	private IPayRSHandler PreSectionAccountsPAY; // 门诊/挂号预结算
	
	@Resource(name = "sectionAccountsPAY")
	private IPayRSHandler SectionAccountsPAY; // 门诊/挂号结算

	@Resource(name = "sectionRefundPAY")
	private IPayRSHandler SectionRefundPAY; // 门诊/挂号退费
	
	@Resource(name = "tradeResultsQueryPAY")
	private IPayRSHandler TradeResultsQueryPAY; // 交易结果查询接口
	
	@Resource(name = "preSectionStateQueryPAY")
	private IPayRSHandler PreSectionStateQueryPAY; // 参保人员预结算结果和享受状态查询接口
	
	@Resource(name = "citizenCardQueryPAY")
	private IPayRSHandler CitizenCardQueryPAY; // 市民卡号查询
	
	@Resource(name = "inHosptionSettlePayBusiness")
	private IPayRSHandler InHosptionSettlePayBusiness;//住院预结算
	
	@Resource(name = "outHosptionSettlePayBusiness")
	private IPayRSHandler OutHosptionSettlePayBusiness;//出院结算
	
	@Resource(name = "hospitalDetailQueryPayBusiness")
	private IPayRSHandler HospitalDetailQueryPayBusiness;//住院明细查询接口
	
	@Resource(name = "tradeConfirmPayBusiness")
	private IPayRSHandler TradeConfirmPayBusiness;//交易确认接口
	
	@Resource(name = "personalInformationPayBusiness")
	private IPayRSHandler PersonalInformationPayBusiness;//获取参保人信息接口
	
	@Resource(name = "totalReconciliation")
	private IPayRSHandler TotalReconciliation;//总额对账接口
	
	@Resource(name = "detailReconciliation")
	private IPayRSHandler DetailReconciliation;//明细对账接口
	
	@Resource(name = "refundTotalRecord")
	private IPayRSHandler RefundTotalRecord;//退费对账接口
	
	@Resource(name = "refundDetailRecord")
	private IPayRSHandler RefundDetailRecord;//退费明细对账接口
	
	@Resource(name = "totalRecordDown")
	private IPayRSHandler TotalRecordDown;//总额对账下载接口
	
	@Resource(name = "detailRecordDown")
	private IPayRSHandler DetailRecordDown;//明细对账下载接口
	
	@Resource(name = "reTotalRecordDown")
	private IPayRSHandler ReTotalRecordDown;//退费对账下载接口
	
	@Resource(name = "reDetailRecordDown")
	private IPayRSHandler ReDetailRecordDown;//退费明细对账下载接口
	
	@Resource(name = "faceComparisonPAY")
	private IPayRSHandler FaceComparisonPAY;//人脸比对接口
	
	@Resource(name = "drugstorePreSettlementPAY")
	private IPayRSHandler DrugstorePreSettlementPAY;//药店预结算接口
	
	@Resource(name = "drugstoreSettlementPAY")
	private IPayRSHandler DrugstoreSettlementPAY;//药店结算接口
	
	@Resource(name = "drugstoreCheckRefundPAY")
	private IPayRSHandler DrugstoreCheckRefundPAY;//药店退费校验接口
	
	@Resource(name = "drugstorePreRefundPAY")
	private IPayRSHandler DrugstorePreRefundPAY;//药店预退费接口
	
	@Resource(name = "drugstoreRefundPAY")
	private IPayRSHandler DrugstoreRefundPAY;//药店退费接口
	
	@Resource(name = "drugstoreReconciliationPAY")
	private IPayRSHandler DrugstoreReconciliationPAY;//药店对账接口
	/**
	 * 适配器，执行何种方法。
	 * @param jsonObject
	 * @param aok
	 * @return
	 */
	public Map<String, Object>  adapter(JSONObject jsonObject,AppOrganiKey aok)
	{
		Map<String, Object> rsMap = new HashMap<String ,Object>();
		Object ddbEntity = new Object() ;
		try 
		{
			JSONObject jo = jsonObject;
			String apitype = jo.getString("apitype");
			switch (apitype)
			{
				case "I22":
					ddbEntity = PersonalInformationPayBusiness.getPayRSHandler(jo); //获取参保人信息接口
					break;
				case "I27":
					ddbEntity = PreSectionAccountsPAY.getPayRSHandler(jo); // 门诊/挂号预结算业务
					break;
				case "I28":
					ddbEntity = SectionAccountsPAY.getPayRSHandler(jo); // 门诊/挂号结算业务
					break;
				case "I31":
					ddbEntity = SectionRefundPAY.getPayRSHandler(jo); // 门诊/挂号退费
					break;
				case "I34":
					ddbEntity = InHosptionSettlePayBusiness.getPayRSHandler(jo);//住院预结算
					break;
				case "I36":
					ddbEntity = OutHosptionSettlePayBusiness.getPayRSHandler(jo);//出院结算
					break;
				case "I41":
					ddbEntity = HospitalDetailQueryPayBusiness.getPayRSHandler(jo);//住院明细查询接口
					break;
				case "I43":
					ddbEntity = TradeResultsQueryPAY.getPayRSHandler(jo);//交易结果查询接口
					break;
				case "I46":
					ddbEntity = PreSectionStateQueryPAY.getPayRSHandler(jo);//参保人员预结算结果和享受状态查询接口
					break;
				case "I49":
					ddbEntity = TradeConfirmPayBusiness.getPayRSHandler(jo);//交易确认接口
					break;
				case "V01":
					ddbEntity = CitizenCardQueryPAY.getPayRSHandler(jo);//市民卡查询接口
					break;
				case "S01":
					ddbEntity = TotalReconciliation.getPayRSHandler(jo);//总额对账接口
					break;
				case "S02":
					ddbEntity = DetailReconciliation.getPayRSHandler(jo);//明细对账接口
					break;
				case "S03":
					ddbEntity = RefundTotalRecord.getPayRSHandler(jo);//退费对账接口
					break;
				case "S04":
					ddbEntity = RefundDetailRecord.getPayRSHandler(jo);//退费明细对账接口
					break;
				case "S05":
					ddbEntity = TotalRecordDown.getPayRSHandler(jo);//总额对账下载接口
					break;
				case "S06":
					ddbEntity = DetailRecordDown.getPayRSHandler(jo);//明细对账下载接口
					break;
				case "S07":
					ddbEntity = ReTotalRecordDown.getPayRSHandler(jo);//退费对账下载接口
					break;
				case "S08":
					ddbEntity = ReDetailRecordDown.getPayRSHandler(jo);//退费明细对账下载接口
					break;
				case "F01":
					ddbEntity = FaceComparisonPAY.getPayRSHandler(jo);//人脸比对接口
					break;
				case "D01":
					ddbEntity = DrugstorePreSettlementPAY.getPayRSHandler(jo);//药店预结算接口
					break;
				case "D02":
					ddbEntity = DrugstoreSettlementPAY.getPayRSHandler(jo);//药店结算接口
					break;
				case "D03":
					ddbEntity = DrugstoreCheckRefundPAY.getPayRSHandler(jo);//药店退费校验接口
					break;
				case "D06":
					ddbEntity = DrugstorePreRefundPAY.getPayRSHandler(jo);//药店预退费接口
					break;
				case "D04":
					ddbEntity = DrugstoreRefundPAY.getPayRSHandler(jo);//药店退费接口
					break;
				case "D05":
					ddbEntity = DrugstoreReconciliationPAY.getPayRSHandler(jo);//药店对账接口
					break;
				default:
					rsMap.put("status",EnumStatus.not_API_data_type.getEnumValue());
					break;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rsMap.put("status",EnumStatus.Parameter_error.getEnumValue());
		}
		if (JSONObject.fromObject(ddbEntity).has("status")) {
			String status = JSONObject.fromObject(ddbEntity).getString("status");
			rsMap.put("status",status);
		}
		rsMap.put("data", ddbEntity);
		return rsMap;
	}

}
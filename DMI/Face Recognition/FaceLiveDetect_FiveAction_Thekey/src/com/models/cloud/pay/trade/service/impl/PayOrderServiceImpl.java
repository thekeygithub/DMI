package com.models.cloud.pay.trade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.alibaba.fastjson.JSON;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.pay.escrow.mi.pangu.request.TollsReq;
import com.models.cloud.pay.escrow.mi.pangu.response.MzSfmxRes;
import com.models.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.models.cloud.pay.escrow.mi.pangu.service.OutpatientService;
import com.models.cloud.pay.payuser.entity.ActPBank;
import com.models.cloud.pay.payuser.service.ActPBankService;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.trade.dao.TdOrdExtPayMapper;
import com.models.cloud.pay.trade.dao.TdOrderMapper;
import com.models.cloud.pay.trade.dao.TdSearchTaskMapper;
import com.models.cloud.pay.trade.entity.TdMiPara;
import com.models.cloud.pay.trade.entity.TdOrdExtPay;
import com.models.cloud.pay.trade.entity.TdOrder;
import com.models.cloud.pay.trade.entity.TdSearchTask;
import com.models.cloud.pay.trade.service.PayOrderService;
import com.models.cloud.pay.trade.service.TdOrderService;
import com.models.cloud.util.DateUtils;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payOrderServiceImpl")
public class PayOrderServiceImpl implements PayOrderService {

    private static final Logger logger = Logger.getLogger(PayOrderServiceImpl.class);

    @Autowired
    private TdOrderMapper tdOrderMapper;
    @Autowired
    private TdOrdExtPayMapper tdOrdExtPayMapper;
    @Resource
    private ActPBankService actPBankServiceImpl;
    @Resource
    private RedisService redisService;
    @Autowired
    private TdSearchTaskMapper tdSearchTaskMapper;
	@Resource
	private TdOrderService tdOrderServiceImpl;
	@Resource
	private OutpatientService outpatientServiceImpl;
	@Resource
	private PayUserService payUserServiceImpl;
	@Resource
	private DoServiceInterface payOrderRefundInterfaceImpl;
	@Resource
	private DoServiceInterface payOrderFullRefundInterfaceImpl;

    /**
     * 获取错误提示语
     * @param errorCode
     * @param errorMessage
     * @return
     * @throws Exception
     */
    private String getErrorTips(String errorCode, String errorMessage) throws Exception {
        String errorMessageTemp;
        if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600095.equals(errorCode) ||
		   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_601036.equals(errorCode) ||
		   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600093.equals(errorCode)){
            errorMessageTemp = BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_MESSAGE_600095;
        }else if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600102.equals(errorCode)){
            errorMessageTemp = BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_MESSAGE_600102;
        }else if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600051.equals(errorCode)){
            errorMessageTemp = BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_MESSAGE_600051;
        }else {
            errorMessageTemp = errorMessage;
        }
        return errorMessageTemp;
    }

    /**
     * 支付结果通知时处理交易订单数据
     * @param payOrderId 交易单号
     * @param yeepayFlowId yeepay交易流水号
     * @param status 支付状态 1-成功 0-失败
     * @param closeTime 支付时间 到秒，如：1369973494
     * @param errorCode 错误码 成功为0即可
     * @param errorMsg 错误描述 成功为success即可
     * @param bindId 绑卡ID
     * @return 成功-SUCCESS 失败-FAILURE
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
    public String executeOrderTradeData_EBZF(String payOrderId, String yeepayFlowId, String status, String closeTime, String errorCode, String errorMsg, String bindId) throws Exception {
		TdOrder tdOrder = tdOrderMapper.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		TdOrdExtPay ordExtPay = tdOrdExtPayMapper.findTdOrdExtPay(Long.valueOf(payOrderId));
		if(null == tdOrder || null == ordExtPay){
			if(logger.isInfoEnabled()){
				logger.info("交易单or支付扩展信息不存在");
			}
			return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
		}
		if(logger.isInfoEnabled()){
			logger.info("当前主交易单状态 tdStatId=" + tdOrder.getTdStatId() + ",商业支付状态 tdBusiStatId=" + ordExtPay.getTdBusiStatId());
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || ordExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("当前交易单已支付成功或商业支付成功，本次请求无需处理");
			}
			return BaseDictConstant.YEEPAY_PAYMENT_RESULT_SUCCESS;
		}
		closeTime = String.valueOf(closeTime).trim();
		if(ValidateUtils.isEmpty(errorCode)){
			errorCode = String.valueOf(DimDictEnum.TD_TRANS_STAT_FAILED.getCode());
		}
		if(ValidateUtils.isEmpty(errorMsg)){
			errorMsg = "";
		}
		boolean paymentProcessResult = false;//支付处理结果 true-成功 false-失败
		String tdBusiStatId;//商业支付状态
		String busiResultCode = "0";//商业支付错误码
		String busiResultDesc = "success";//商业支付错误描述
		String tdSiStatId = "";//医保支付状态
		String siResultCode = "";//医保支付错误码
		String siResultDesc = "";//医保支付错误描述
		String siTdCode = "";//社保交易流水号
		String clmPara = "";
		String clmRet = null;
		if("1".equals(status)){
			if(logger.isInfoEnabled()){
				logger.info("易宝支付通知交易单(" + tdOrder.getTdId() + ")结果：支付成功");
			}
			paymentProcessResult = true;
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
		}else{
			if(logger.isInfoEnabled()){
				logger.info("易宝支付通知交易单(" + tdOrder.getTdId() + ")结果：支付失败");
			}
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultCode = errorCode.trim();
			busiResultDesc = this.getErrorTips(busiResultCode, errorMsg.trim());
		}

		Date nowDate = new Date();
		TdMiPara tdMiPara = null;
		if(paymentProcessResult){
			redisService.delete("paymentToken_".concat(tdOrder.getFromActId().toString()));
			redisService.delete("yeepayNeedSmsVerify_".concat(tdOrder.getTdId().toString()));
			redisService.delete("ebaoPaymentFlowId_".concat(tdOrder.getTdId().toString()));
			redisService.delete("bindCardParamsNeed_".concat(tdOrder.getTdId().toString()));

			TdSearchTask tdSearchTask = new TdSearchTask();
			tdSearchTask.setTdId(tdOrder.getTdId()); //交易单ID
			tdSearchTask.setFundId(tdOrder.getFundId());
			tdSearchTask.setTdTypeId(tdOrder.getTdTypeId()); //交易类型
			tdSearchTask.setTdStatId(Short.valueOf(tdBusiStatId)); //交易状态
			tdSearchTask.setSearchCnt(0); //查询次数
			tdSearchTask.setTdProcStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_PROC_STAT_ID_TREATMENT_PENDING.getCode()))); //交易任务处理状态
			tdSearchTask.setUpdTime(nowDate); //更新时间
			tdSearchTask.setCrtTime(nowDate); //创建时间
			if(logger.isInfoEnabled()){
				logger.info("开始保存交易查询任务信息-YeePay");
			}
			tdSearchTaskMapper.saveTdSearchTask(tdSearchTask);
			if(logger.isInfoEnabled()){
				logger.info("<<保存成功");
			}

			ActPBank selectActPBank = new ActPBank();
			selectActPBank.setActId(tdOrder.getFromActId());
			selectActPBank.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
			selectActPBank.setFromActTypeId(tdOrder.getFromActTypeId());//平台账户类型
			selectActPBank.setPayPropA(tdOrder.getTdId().toString());
			selectActPBank.setPayPropB(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			bindId = String.valueOf(bindId).trim();
			if(logger.isInfoEnabled()){
				logger.info("检查私人银行账号信息是否存在 bindId=" + bindId);
			}
			List<ActPBank> actPBankList = actPBankServiceImpl.findActPBankList(selectActPBank);
			if(null != actPBankList && actPBankList.size() > 0 && ValidateUtils.isNotEmpty(bindId) && !"0".equals(bindId)){
				ActPBank updActPBank = new ActPBank();
				updActPBank.setBankActId(actPBankList.get(0).getBankActId());
				updActPBank.setPayPropC(bindId);
				updActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
				if(logger.isInfoEnabled()){
					logger.info("<<存在且bindId不为0，开始更新状态为有效 actPBankList.size=" + actPBankList.size());
				}
				actPBankServiceImpl.updateActPBank(updActPBank);
				if(logger.isInfoEnabled()){
					logger.info("<<更新成功");
				}
			}else{
				if(logger.isInfoEnabled()){
					logger.info("<<不存在或bindId为0，无需处理");
				}
			}

			/*******************发起医保结算 begin*********************/
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
				if(null == tdMiPara){
					if(logger.isInfoEnabled()){
						logger.info("获取TdMiPara信息为空");
					}
					return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
				}
				String medicalClass = String.valueOf(ordExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
				String insuredPersonType = String.valueOf(ordExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
				if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
						Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
						if(null == preClmParaMap || preClmParaMap.size() == 0){
							if(logger.isInfoEnabled()){
								logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
							}
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), ordExtPay.getpSiCardNo(), preClmParaMap.get("fixedHospCode").toString());
						if(null == personInfoRes){
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						TollsReq tollsReq = (TollsReq) preClmParaMap.get("tollsReq");
						tollsReq.getMzToll().setOperDate(DateUtils.getNowDate("yyyyMMddHHmmss"));//经办日期yyyymmddhh24miss
						Map<String, Object> clmParaMap = new HashMap<>();
						clmParaMap.put("fixedHospCode", preClmParaMap.get("fixedHospCode"));
						clmParaMap.put("tollsReq", tollsReq);
						clmPara = JSON.toJSONString(clmParaMap);
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)，请求参数：fixedHospCode=" + preClmParaMap.get("fixedHospCode").toString() + ",cycId=" + personInfoRes.getCycid() + ",tollsReq=" + JSON.toJSONString(tollsReq));
						}
						MzSfmxRes mzSfmxRes = outpatientServiceImpl.executeMzSfmx(preClmParaMap.get("fixedHospCode").toString(), personInfoRes.getCycid(), tollsReq);
						if(null != mzSfmxRes){
							clmRet = JSON.toJSONString(mzSfmxRes);
						}
						if(logger.isInfoEnabled()){
							logger.info("响应报文：mzSfmxRes=" + clmRet);
						}
						if(null == mzSfmxRes || mzSfmxRes.getRTNCode() != 1){
							tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultDesc = "医院门诊医保结算(职工)失败";
							if(logger.isInfoEnabled()){
								logger.info("医院门诊医保结算(职工)失败，进行YeePay商业退款");
							}
							Map<String, Object> inputMap = new HashMap<>();
							inputMap.put("appId", tdOrder.getChanAppid());
							inputMap.put("oriTdId", tdOrder.getTdId().toString());
							inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
							inputMap.put("cause", "Settlement Failure YeePay Refund");
							inputMap.put("payRefundMoney", ordExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
							inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
							inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
							inputMap.put("systemId", tdOrder.getSpSubSysCode());
							inputMap.put("allowRefund", "true");
							Map<String, Object> returnMap = payOrderRefundInterfaceImpl.doService(inputMap);
							if(logger.isInfoEnabled()){
								logger.info("<<申请退款结果：" + returnMap);
							}
							if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
								throw new RuntimeException("<<申请退款出现异常");
							}
						}else {
							String zhpay = String.valueOf(mzSfmxRes.getZhpay()).trim();
							if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
								mzSfmxRes.setZhpay("0");
							}
							BigDecimal payPacct = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
							//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
							String tcpay = String.valueOf(mzSfmxRes.getTcpay()).trim();
							if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
								mzSfmxRes.setTcpay("0");
							}
							String bigpay = String.valueOf(mzSfmxRes.getBigpay()).trim();
							if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
								mzSfmxRes.setBigpay("0");
							}
							String gwybz = String.valueOf(mzSfmxRes.getGwybz()).trim();
							if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
								mzSfmxRes.setGwybz("0");
							}
							String qybcjjpay = String.valueOf(mzSfmxRes.getQybcpay()).trim();
							if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
								mzSfmxRes.setQybcpay("0");
							}
							BigDecimal paySi = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getTcpay()))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getBigpay())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getGwybz())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
							String cashpay = String.valueOf(mzSfmxRes.getCashpay()).trim();
							if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
								mzSfmxRes.setCashpay("0");
							}
							BigDecimal paySelf = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);

							if(logger.isInfoEnabled()){
								logger.info("[YeePay]预结算：payPacct=" + ordExtPay.getPayPacct() + ",paySi=" + ordExtPay.getPaySi() + ",paySelf=" + ordExtPay.getPaySelf() +
										" 结算：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
							}
							siTdCode = String.valueOf(mzSfmxRes.getDealID()).trim();
							if(ordExtPay.getPayPacct().compareTo(payPacct) == 0 &&
							   ordExtPay.getPaySi().compareTo(paySi) == 0 &&
							   ordExtPay.getPaySelf().compareTo(paySelf) == 0){
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)成功");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
								siResultCode = "0";
								siResultDesc = "医院门诊医保结算(职工)成功";
							}else {
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，预结算与结算结果不一致");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

								//进行结算撤销处理
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，进行[YeePay-医保]退款处理");
								}
								Map<String, Object> inputMap = new HashMap<>();
								inputMap.put("appId", tdOrder.getChanAppid());
								inputMap.put("oriTdId", tdOrder.getTdId().toString());
								inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
								inputMap.put("cause", "Settlement Failure Full Refund");
								inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
								inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
								inputMap.put("systemId", tdOrder.getSpSubSysCode());
								inputMap.put("allowRefund", "true");
								Map<String, Object> returnMap = payOrderFullRefundInterfaceImpl.doService(inputMap);
								if(logger.isInfoEnabled()){
									logger.info("<<申请退款结果：" + returnMap);
								}
								if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
									throw new RuntimeException("<<申请退款出现异常");
								}
							}
						}
					}else {
						//居民暂不考虑
					}
				}else {
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){

					}else {
						//居民暂不考虑
					}
				}
			}
			/*******************发起医保结算 end*********************/
		}else{
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		String tdStatId;
		if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdBusiStatId)){
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdSiStatId)){
					tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//商业支付成功&医保支付成功 成功
				}else {
					tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付成功&医保支付失败 失败
				}
			}else {
				tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//纯商业支付成功
			}
		}else {
			tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付失败
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
				siResultDesc = "YeePay支付失败未发起医保结算";
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		yeepayFlowId = String.valueOf(yeepayFlowId).trim();
		//更新交易单信息
		TdOrder updOrder = new TdOrder();
		updOrder.setTdId(tdOrder.getTdId());//交易单号
		updOrder.setTdStatId(Short.valueOf(tdStatId));//交易状态
		if(ValidateUtils.isNotEmpty(yeepayFlowId)){
			updOrder.setFundTdCode(yeepayFlowId);
		}
		if(ValidateUtils.isNotEmpty(closeTime)){
			updOrder.setTdEndTime(new Date(Long.valueOf(closeTime.concat("000"))));//交易结束时间
		}else{
			updOrder.setTdEndTime(nowDate);//交易结束时间
		}
		if(tdStatId.equals(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()) && "0".equals(busiResultCode)){
			updOrder.setErrCode("支付结果|".concat(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()));//交易失败编码
		}else{
			updOrder.setErrCode("支付结果|".concat(busiResultCode));//交易失败编码
		}

		updOrder.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("更新交易单信息");
		}
		tdOrderMapper.updateTdOrder(updOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		//更新支付信息
		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(tdOrder.getTdId());//交易单号
		updOrdExtPay.setTdBusiStatId(Short.valueOf(tdBusiStatId));//商业支付状态
		if(ValidateUtils.isNotEmpty(tdSiStatId)){
			updOrdExtPay.setTdSiStatId(Short.valueOf(tdSiStatId));//医保支付状态
		}
		if(ValidateUtils.isNotEmpty(yeepayFlowId)){
			updOrdExtPay.setFundTdCode(yeepayFlowId);
		}
		if(ValidateUtils.isNotEmpty(siTdCode)){
			updOrdExtPay.setSiTdCode(siTdCode);
		}
		updOrdExtPay.setChanRetCode(busiResultCode);//支付通道返回码
		updOrdExtPay.setChanRetDesc(busiResultDesc);//支付通道返回描述
		if(ValidateUtils.isNotEmpty(siResultCode)){
			updOrdExtPay.setSiRetCode(siResultCode);//社保通道返回码
		}
		if(ValidateUtils.isNotEmpty(siResultDesc)){
			updOrdExtPay.setSiRetDesc(siResultDesc);//社保通道返回描述
		}
		updOrdExtPay.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrdExtPayMapper.updateTdOrdExtPay(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		if(ValidateUtils.isNotEmpty(clmPara) && ValidateUtils.isNotEmpty(clmRet)){
			TdMiPara updTdMiPara = new TdMiPara();
			updTdMiPara.setTdId(tdOrder.getTdId());
			updTdMiPara.setClmPara(clmPara);//医保结算提交参数
			Map clmRetMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
			if(null != clmRetMap && clmRetMap.size() > 0){
				clmRetMap.putAll(JsonStringUtils.jsonStringToObject(clmRet, Map.class));
				clmRet = JsonStringUtils.objectToJsonString(clmRetMap);
			}
			updTdMiPara.setClmRet(clmRet);//医保结算返回
			updTdMiPara.setUpdTime(new Date());
			if(logger.isInfoEnabled()){
				logger.info("开始更新TdMiPara信息");
			}
			tdOrderServiceImpl.updateTdMiPara(updTdMiPara);
			if(logger.isInfoEnabled()){
				logger.info("<<更新成功");
			}
		}

		return BaseDictConstant.YEEPAY_PAYMENT_RESULT_SUCCESS;
    }

	@SuppressWarnings("unchecked")
	public String executeOrderTradeData_CCB(String payOrderId, String ccbPayFlowId, String status, String tranDate, String payAmount) throws Exception {
		TdOrder tdOrder = tdOrderMapper.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		TdOrdExtPay ordExtPay = tdOrdExtPayMapper.findTdOrdExtPay(Long.valueOf(payOrderId));
		if(null == tdOrder || null == ordExtPay){
			if(logger.isInfoEnabled()){
				logger.info("交易单or支付扩展信息不存在");
			}
			return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
		}
		if(logger.isInfoEnabled()){
			logger.info("当前主交易单状态 tdStatId=" + tdOrder.getTdStatId() + ",商业支付状态 tdBusiStatId=" + ordExtPay.getTdBusiStatId());
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || ordExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("当前交易单已支付成功或商业支付成功，本次请求无需处理");
			}
			return BaseDictConstant.CCB_PAYMENT_RESULT_SUCCESS;
		}
		payAmount = String.valueOf(payAmount).trim();
		if(ValidateUtils.isNotEmpty(payAmount) && ordExtPay.getPaySelf().compareTo(BigDecimal.valueOf(Double.parseDouble(payAmount))) != 0){
			if(logger.isInfoEnabled()){
				logger.info("建行支付结果通知金额不正确 paySelf=" + ordExtPay.getPaySelf() + ",payAmount=" + payAmount);
			}
			return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
		}

		boolean paymentProcessResult = false;//支付处理结果 true-成功 false-失败
		String tdBusiStatId;//商业支付状态
		String busiResultCode = "0";//商业支付错误码
		String busiResultDesc = "success";//商业支付错误描述
		String tdSiStatId = "";//医保支付状态
		String siResultCode = "";//医保支付错误码
		String siResultDesc = "";//医保支付错误描述
		String siTdCode = "";//社保交易流水号
		String clmPara = "";
		String clmRet = null;
		if("1".equals(status)){
			if(logger.isInfoEnabled()){
				logger.info("建行通知交易单(" + tdOrder.getTdId() + ")结果：支付成功");
			}
			paymentProcessResult = true;
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
		}else{
			if(logger.isInfoEnabled()){
				logger.info("建行通知交易单(" + tdOrder.getTdId() + ")结果：支付失败");
			}
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultDesc = "支付失败";
		}

		Date nowDate = new Date();
		TdMiPara tdMiPara = null;
		if(paymentProcessResult){
			redisService.delete("paymentToken_".concat(tdOrder.getFromActId().toString()));
			redisService.delete("ebaoPaymentFlowId_".concat(tdOrder.getTdId().toString()));

			TdSearchTask tdSearchTask = new TdSearchTask();
			tdSearchTask.setTdId(tdOrder.getTdId()); //交易单ID
			tdSearchTask.setFundId(tdOrder.getFundId());
			tdSearchTask.setTdTypeId(tdOrder.getTdTypeId()); //交易类型
			tdSearchTask.setTdStatId(Short.valueOf(tdBusiStatId)); //交易状态
			tdSearchTask.setSearchCnt(0); //查询次数
			tdSearchTask.setTdProcStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_PROC_STAT_ID_TREATMENT_PENDING.getCode()))); //交易任务处理状态
			tdSearchTask.setUpdTime(nowDate); //更新时间
			tdSearchTask.setCrtTime(nowDate); //创建时间
			if(logger.isInfoEnabled()){
				logger.info("开始保存交易查询任务信息-CCB");
			}
			tdSearchTaskMapper.saveTdSearchTask(tdSearchTask);
			if(logger.isInfoEnabled()){
				logger.info("<<保存成功");
			}

			/*******************发起医保结算 begin*********************/
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
				if(null == tdMiPara){
					if(logger.isInfoEnabled()){
						logger.info("获取TdMiPara信息为空");
					}
					return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
				}
				String medicalClass = String.valueOf(ordExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
				String insuredPersonType = String.valueOf(ordExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
				if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
						Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
						if(null == preClmParaMap || preClmParaMap.size() == 0){
							if(logger.isInfoEnabled()){
								logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
							}
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), ordExtPay.getpSiCardNo(), preClmParaMap.get("fixedHospCode").toString());
						if(null == personInfoRes){
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						TollsReq tollsReq = (TollsReq) preClmParaMap.get("tollsReq");
						tollsReq.getMzToll().setOperDate(DateUtils.getNowDate("yyyyMMddHHmmss"));//经办日期yyyymmddhh24miss
						Map<String, Object> clmParaMap = new HashMap<>();
						clmParaMap.put("fixedHospCode", preClmParaMap.get("fixedHospCode"));
						clmParaMap.put("tollsReq", tollsReq);
						clmPara = JSON.toJSONString(clmParaMap);
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)，请求参数：fixedHospCode=" + preClmParaMap.get("fixedHospCode").toString() + ",cycId=" + personInfoRes.getCycid() + ",tollsReq=" + JSON.toJSONString(tollsReq));
						}
						MzSfmxRes mzSfmxRes = outpatientServiceImpl.executeMzSfmx(preClmParaMap.get("fixedHospCode").toString(), personInfoRes.getCycid(), tollsReq);
						if(null != mzSfmxRes){
							clmRet = JSON.toJSONString(mzSfmxRes);
						}
						if(logger.isInfoEnabled()){
							logger.info("响应报文：mzSfmxRes=" + clmRet);
						}
						if(null == mzSfmxRes || mzSfmxRes.getRTNCode() != 1){
							tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultDesc = "医院门诊医保结算(职工)失败";
							if(logger.isInfoEnabled()){
								logger.info("医院门诊医保结算(职工)失败，进行CCB商业退款");
							}
							Map<String, Object> inputMap = new HashMap<>();
							inputMap.put("appId", tdOrder.getChanAppid());
							inputMap.put("oriTdId", tdOrder.getTdId().toString());
							inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
							inputMap.put("cause", "Settlement Failure CCB Refund");
							inputMap.put("payRefundMoney", ordExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
							inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
							inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
							inputMap.put("systemId", tdOrder.getSpSubSysCode());
							inputMap.put("allowRefund", "true");
							Map<String, Object> returnMap = payOrderRefundInterfaceImpl.doService(inputMap);
							if(logger.isInfoEnabled()){
								logger.info("<<申请退款结果：" + returnMap);
							}
							if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
								throw new RuntimeException("<<申请退款出现异常");
							}
						}else {
							String zhpay = String.valueOf(mzSfmxRes.getZhpay()).trim();
							if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
								mzSfmxRes.setZhpay("0");
							}
							BigDecimal payPacct = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
							//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
							String tcpay = String.valueOf(mzSfmxRes.getTcpay()).trim();
							if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
								mzSfmxRes.setTcpay("0");
							}
							String bigpay = String.valueOf(mzSfmxRes.getBigpay()).trim();
							if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
								mzSfmxRes.setBigpay("0");
							}
							String gwybz = String.valueOf(mzSfmxRes.getGwybz()).trim();
							if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
								mzSfmxRes.setGwybz("0");
							}
							String qybcjjpay = String.valueOf(mzSfmxRes.getQybcpay()).trim();
							if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
								mzSfmxRes.setQybcpay("0");
							}
							BigDecimal paySi = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getTcpay()))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getBigpay())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getGwybz())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
							String cashpay = String.valueOf(mzSfmxRes.getCashpay()).trim();
							if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
								mzSfmxRes.setCashpay("0");
							}
							BigDecimal paySelf = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);

							if(logger.isInfoEnabled()){
								logger.info("[CCB]预结算：payPacct=" + ordExtPay.getPayPacct() + ",paySi=" + ordExtPay.getPaySi() + ",paySelf=" + ordExtPay.getPaySelf() +
										" 结算：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
							}
							siTdCode = String.valueOf(mzSfmxRes.getDealID()).trim();
							if(ordExtPay.getPayPacct().compareTo(payPacct) == 0 &&
							   ordExtPay.getPaySi().compareTo(paySi) == 0 &&
							   ordExtPay.getPaySelf().compareTo(paySelf) == 0){
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)成功");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
								siResultCode = "0";
								siResultDesc = "医院门诊医保结算(职工)成功";
							}else {
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，预结算与结算结果不一致");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

								//进行结算撤销处理
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，进行[CCB-医保]退款处理");
								}
								Map<String, Object> inputMap = new HashMap<>();
								inputMap.put("appId", tdOrder.getChanAppid());
								inputMap.put("oriTdId", tdOrder.getTdId().toString());
								inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
								inputMap.put("cause", "Settlement Failure Full Refund");
								inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
								inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
								inputMap.put("systemId", tdOrder.getSpSubSysCode());
								inputMap.put("allowRefund", "true");
								Map<String, Object> returnMap = payOrderFullRefundInterfaceImpl.doService(inputMap);
								if(logger.isInfoEnabled()){
									logger.info("<<申请退款结果：" + returnMap);
								}
								if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
									throw new RuntimeException("<<申请退款出现异常");
								}
							}

						}
					}else {
						//居民暂不考虑
					}
				}else {
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){

					}else {
						//居民暂不考虑
					}
				}
			}
			/*******************发起医保结算 end*********************/
		}else {
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		String tdStatId;
		if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdBusiStatId)){
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdSiStatId)){
					tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//商业支付成功&医保支付成功 成功
				}else {
					tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付成功&医保支付失败 失败
				}
			}else {
				tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//纯商业支付成功
			}
		}else {
			tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付失败
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
				siResultDesc = "CCB支付失败未发起医保结算";
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		ccbPayFlowId = String.valueOf(ccbPayFlowId).trim();
		//更新交易单信息
		TdOrder updOrder = new TdOrder();
		updOrder.setTdId(tdOrder.getTdId());//交易单号
		updOrder.setTdStatId(Short.valueOf(tdStatId));//交易状态
		if(ValidateUtils.isNotEmpty(ccbPayFlowId)){
			updOrder.setFundTdCode(ccbPayFlowId);
		}
		tranDate = String.valueOf(tranDate).trim();
		if(ValidateUtils.isNotEmpty(tranDate)){
			updOrder.setTdEndTime(DateUtils.strToDate(tranDate, "yyyy-MM-dd HH:mm:ss"));//交易结束时间
		}else{
			updOrder.setTdEndTime(nowDate);//交易结束时间
		}
		if(tdStatId.equals(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()) && "0".equals(busiResultCode)){
			updOrder.setErrCode("支付结果|".concat(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()));//交易失败编码
		}else{
			updOrder.setErrCode("支付结果|".concat(busiResultCode));//交易失败编码
		}

		updOrder.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("更新交易单信息");
		}
		tdOrderMapper.updateTdOrder(updOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		//更新支付信息
		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(tdOrder.getTdId());//交易单号
		updOrdExtPay.setTdBusiStatId(Short.valueOf(tdBusiStatId));//商业支付状态
		if(ValidateUtils.isNotEmpty(tdSiStatId)){
			updOrdExtPay.setTdSiStatId(Short.valueOf(tdSiStatId));//医保支付状态
		}
		if(ValidateUtils.isNotEmpty(ccbPayFlowId)){
			updOrdExtPay.setFundTdCode(ccbPayFlowId);
		}
		if(ValidateUtils.isNotEmpty(siTdCode)){
			updOrdExtPay.setSiTdCode(siTdCode);
		}
		updOrdExtPay.setChanRetCode(busiResultCode);//支付通道返回码
		updOrdExtPay.setChanRetDesc(busiResultDesc);//支付通道返回描述
		if(ValidateUtils.isNotEmpty(siResultCode)){
			updOrdExtPay.setSiRetCode(siResultCode);//社保通道返回码
		}
		if(ValidateUtils.isNotEmpty(siResultDesc)){
			updOrdExtPay.setSiRetDesc(siResultDesc);//社保通道返回描述
		}
		updOrdExtPay.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrdExtPayMapper.updateTdOrdExtPay(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		if(ValidateUtils.isNotEmpty(clmPara) && ValidateUtils.isNotEmpty(clmRet)){
			TdMiPara updTdMiPara = new TdMiPara();
			updTdMiPara.setTdId(tdOrder.getTdId());
			updTdMiPara.setClmPara(clmPara);//医保结算提交参数
			Map clmRetMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
			if(null != clmRetMap && clmRetMap.size() > 0){
				clmRetMap.putAll(JsonStringUtils.jsonStringToObject(clmRet, Map.class));
				clmRet = JsonStringUtils.objectToJsonString(clmRetMap);
			}
			updTdMiPara.setClmRet(clmRet);//医保结算返回
			updTdMiPara.setUpdTime(new Date());
			if(logger.isInfoEnabled()){
				logger.info("开始更新TdMiPara医保信息");
			}
			tdOrderServiceImpl.updateTdMiPara(updTdMiPara);
			if(logger.isInfoEnabled()){
				logger.info("<<更新成功");
			}
		}

		return BaseDictConstant.CCB_PAYMENT_RESULT_SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String executeOrderTradeData_BKU(String payOrderId, String bkuPayFlowId, String status, String transTime, String orderAmt) throws Exception {
		TdOrder tdOrder = tdOrderMapper.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		TdOrdExtPay ordExtPay = tdOrdExtPayMapper.findTdOrdExtPay(Long.valueOf(payOrderId));
		if(null == tdOrder || null == ordExtPay){
			if(logger.isInfoEnabled()){
				logger.info("交易单or支付扩展信息不存在");
			}
			return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
		}
		if(logger.isInfoEnabled()){
			logger.info("当前主交易单状态 tdStatId=" + tdOrder.getTdStatId() + ",商业支付状态 tdBusiStatId=" + ordExtPay.getTdBusiStatId());
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || ordExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("当前交易单已支付成功或商业支付成功，本次请求无需处理");
			}
			return BaseDictConstant.BKU_PAYMENT_RESULT_SUCCESS;
		}
		orderAmt = String.valueOf(orderAmt).trim();
		if(ValidateUtils.isNotEmpty(orderAmt) && ordExtPay.getPaySelf().multiply(BigDecimal.valueOf(100)).compareTo(BigDecimal.valueOf(Double.parseDouble(orderAmt))) != 0){
			if(logger.isInfoEnabled()){
				logger.info("银联支付结果通知金额不正确 paySelf=" + ordExtPay.getPaySelf().multiply(BigDecimal.valueOf(100)) + ",orderAmt=" + orderAmt);
			}
			return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
		}

		boolean paymentProcessResult = false;//支付处理结果 true-成功 false-失败
		String tdBusiStatId;//商业支付状态
		String busiResultCode = "0";//商业支付错误码
		String busiResultDesc = "success";//商业支付错误描述
		String tdSiStatId = "";//医保支付状态
		String siResultCode = "";//医保支付错误码
		String siResultDesc = "";//医保支付错误描述
		String siTdCode = "";//社保交易流水号
		String clmPara = "";
		String clmRet = null;
		if("1".equals(status)){
			if(logger.isInfoEnabled()){
				logger.info("银联通知交易单(" + tdOrder.getTdId() + ")结果：支付成功");
			}
			paymentProcessResult = true;
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
		}else{
			if(logger.isInfoEnabled()){
				logger.info("银联通知交易单(" + tdOrder.getTdId() + ")结果：支付失败");
			}
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultDesc = "支付失败";
		}

		Date nowDate = new Date();
		TdMiPara tdMiPara = null;
		if(paymentProcessResult){
			redisService.delete("paymentToken_".concat(tdOrder.getFromActId().toString()));
			redisService.delete("ebaoPaymentFlowId_".concat(tdOrder.getTdId().toString()));
			redisService.delete("unionPayPostParams_".concat(tdOrder.getTdId().toString()));

			TdSearchTask tdSearchTask = new TdSearchTask();
			tdSearchTask.setTdId(tdOrder.getTdId()); //交易单ID
			tdSearchTask.setFundId(tdOrder.getFundId());
			tdSearchTask.setTdTypeId(tdOrder.getTdTypeId()); //交易类型
			tdSearchTask.setTdStatId(Short.valueOf(tdBusiStatId)); //交易状态
			tdSearchTask.setSearchCnt(0); //查询次数
			tdSearchTask.setTdProcStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_PROC_STAT_ID_TREATMENT_PENDING.getCode()))); //交易任务处理状态
			tdSearchTask.setUpdTime(nowDate); //更新时间
			tdSearchTask.setCrtTime(nowDate); //创建时间
			if(logger.isInfoEnabled()){
				logger.info("开始保存交易查询任务信息-BKU");
			}
			tdSearchTaskMapper.saveTdSearchTask(tdSearchTask);
			if(logger.isInfoEnabled()){
				logger.info("<<保存成功");
			}

			/*******************发起医保结算 begin*********************/
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
				if(null == tdMiPara){
					if(logger.isInfoEnabled()){
						logger.info("获取TdMiPara信息为空");
					}
					return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
				}
				String medicalClass = String.valueOf(ordExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
				String insuredPersonType = String.valueOf(ordExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
				if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
						Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
						if(null == preClmParaMap || preClmParaMap.size() == 0){
							if(logger.isInfoEnabled()){
								logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
							}
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), ordExtPay.getpSiCardNo(), preClmParaMap.get("fixedHospCode").toString());
						if(null == personInfoRes){
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						TollsReq tollsReq = (TollsReq) preClmParaMap.get("tollsReq");
						tollsReq.getMzToll().setOperDate(DateUtils.getNowDate("yyyyMMddHHmmss"));//经办日期yyyymmddhh24miss
						Map<String, Object> clmParaMap = new HashMap<>();
						clmParaMap.put("fixedHospCode", preClmParaMap.get("fixedHospCode"));
						clmParaMap.put("tollsReq", tollsReq);
						clmPara = JSON.toJSONString(clmParaMap);
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)，请求参数：fixedHospCode=" + preClmParaMap.get("fixedHospCode").toString() + ",cycId=" + personInfoRes.getCycid() + ",tollsReq=" + JSON.toJSONString(tollsReq));
						}
						MzSfmxRes mzSfmxRes = outpatientServiceImpl.executeMzSfmx(preClmParaMap.get("fixedHospCode").toString(), personInfoRes.getCycid(), tollsReq);
						if(null != mzSfmxRes){
							clmRet = JSON.toJSONString(mzSfmxRes);
						}
						if(logger.isInfoEnabled()){
							logger.info("响应报文：mzSfmxRes=" + clmRet);
						}
						if(null == mzSfmxRes || mzSfmxRes.getRTNCode() != 1){
							tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultDesc = "医院门诊医保结算(职工)失败";
							if(logger.isInfoEnabled()){
								logger.info("医院门诊医保结算(职工)失败，进行BKU商业退款");
							}
							Map<String, Object> inputMap = new HashMap<>();
							inputMap.put("appId", tdOrder.getChanAppid());
							inputMap.put("oriTdId", tdOrder.getTdId().toString());
							inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
							inputMap.put("cause", "Settlement Failure BKU Refund");
							inputMap.put("payRefundMoney", ordExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
							inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
							inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
							inputMap.put("systemId", tdOrder.getSpSubSysCode());
							inputMap.put("allowRefund", "true");
							Map<String, Object> returnMap = payOrderRefundInterfaceImpl.doService(inputMap);
							if(logger.isInfoEnabled()){
								logger.info("<<申请退款结果：" + returnMap);
							}
							if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
								throw new RuntimeException("<<申请退款出现异常");
							}
						}else {
							String zhpay = String.valueOf(mzSfmxRes.getZhpay()).trim();
							if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
								mzSfmxRes.setZhpay("0");
							}
							BigDecimal payPacct = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
							//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
							String tcpay = String.valueOf(mzSfmxRes.getTcpay()).trim();
							if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
								mzSfmxRes.setTcpay("0");
							}
							String bigpay = String.valueOf(mzSfmxRes.getBigpay()).trim();
							if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
								mzSfmxRes.setBigpay("0");
							}
							String gwybz = String.valueOf(mzSfmxRes.getGwybz()).trim();
							if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
								mzSfmxRes.setGwybz("0");
							}
							String qybcjjpay = String.valueOf(mzSfmxRes.getQybcpay()).trim();
							if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
								mzSfmxRes.setQybcpay("0");
							}
							BigDecimal paySi = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getTcpay()))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getBigpay())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getGwybz())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
							String cashpay = String.valueOf(mzSfmxRes.getCashpay()).trim();
							if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
								mzSfmxRes.setCashpay("0");
							}
							BigDecimal paySelf = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);

							if(logger.isInfoEnabled()){
								logger.info("[BKU]预结算：payPacct=" + ordExtPay.getPayPacct() + ",paySi=" + ordExtPay.getPaySi() + ",paySelf=" + ordExtPay.getPaySelf() +
										" 结算：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
							}
							siTdCode = String.valueOf(mzSfmxRes.getDealID()).trim();
							if(ordExtPay.getPayPacct().compareTo(payPacct) == 0 &&
							   ordExtPay.getPaySi().compareTo(paySi) == 0 &&
							   ordExtPay.getPaySelf().compareTo(paySelf) == 0){
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)成功");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
								siResultCode = "0";
								siResultDesc = "医院门诊医保结算(职工)成功";
							}else {
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，预结算与结算结果不一致");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

								//进行结算撤销处理
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，进行[BKU-医保]退款处理");
								}
								Map<String, Object> inputMap = new HashMap<>();
								inputMap.put("appId", tdOrder.getChanAppid());
								inputMap.put("oriTdId", tdOrder.getTdId().toString());
								inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
								inputMap.put("cause", "Settlement Failure Full Refund");
								inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
								inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
								inputMap.put("systemId", tdOrder.getSpSubSysCode());
								inputMap.put("allowRefund", "true");
								Map<String, Object> returnMap = payOrderFullRefundInterfaceImpl.doService(inputMap);
								if(logger.isInfoEnabled()){
									logger.info("<<申请退款结果：" + returnMap);
								}
								if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
									throw new RuntimeException("<<申请退款出现异常");
								}
							}
						}
					}else {
						//居民暂不考虑
					}
				}else {
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){

					}else {
						//居民暂不考虑
					}
				}
			}
			/*******************发起医保结算 end*********************/
		}else {
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		String tdStatId;
		if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdBusiStatId)){
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdSiStatId)){
					tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//商业支付成功&医保支付成功 成功
				}else {
					tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付成功&医保支付失败 失败
				}
			}else {
				tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//纯商业支付成功
			}
		}else {
			tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付失败
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
				siResultDesc = "BKU支付失败未发起医保结算";
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		bkuPayFlowId = String.valueOf(bkuPayFlowId).trim();
		//更新交易单信息
		TdOrder updOrder = new TdOrder();
		updOrder.setTdId(tdOrder.getTdId());//交易单号
		updOrder.setTdStatId(Short.valueOf(tdStatId));//交易状态
		if(ValidateUtils.isNotEmpty(bkuPayFlowId)){
			updOrder.setFundTdCode(bkuPayFlowId);
		}
		transTime = String.valueOf(transTime).trim();
		if(ValidateUtils.isNotEmpty(transTime)){
			updOrder.setTdEndTime(DateUtils.strToDate(DateUtils.dateToString(DateUtils.strToDate(transTime, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));//交易结束时间
		}else{
			updOrder.setTdEndTime(nowDate);//交易结束时间
		}
		if(tdStatId.equals(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()) && "0".equals(busiResultCode)){
			updOrder.setErrCode("支付结果|".concat(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()));//交易失败编码
		}else{
			updOrder.setErrCode("支付结果|".concat(busiResultCode));//交易失败编码
		}

		updOrder.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("更新交易单信息");
		}
		tdOrderMapper.updateTdOrder(updOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		//更新支付信息
		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(tdOrder.getTdId());//交易单号
		updOrdExtPay.setTdBusiStatId(Short.valueOf(tdBusiStatId));//商业支付状态
		if(ValidateUtils.isNotEmpty(tdSiStatId)){
			updOrdExtPay.setTdSiStatId(Short.valueOf(tdSiStatId));//医保支付状态
		}
		if(ValidateUtils.isNotEmpty(bkuPayFlowId)){
			updOrdExtPay.setFundTdCode(bkuPayFlowId);
		}
		if(ValidateUtils.isNotEmpty(siTdCode)){
			updOrdExtPay.setSiTdCode(siTdCode);
		}
		updOrdExtPay.setChanRetCode(busiResultCode);//支付通道返回码
		updOrdExtPay.setChanRetDesc(busiResultDesc);//支付通道返回描述
		if(ValidateUtils.isNotEmpty(siResultCode)){
			updOrdExtPay.setSiRetCode(siResultCode);//社保通道返回码
		}
		if(ValidateUtils.isNotEmpty(siResultDesc)){
			updOrdExtPay.setSiRetDesc(siResultDesc);//社保通道返回描述
		}
		updOrdExtPay.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrdExtPayMapper.updateTdOrdExtPay(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		if(ValidateUtils.isNotEmpty(clmPara) && ValidateUtils.isNotEmpty(clmRet)){
			TdMiPara updTdMiPara = new TdMiPara();
			updTdMiPara.setTdId(tdOrder.getTdId());
			updTdMiPara.setClmPara(clmPara);//医保结算提交参数
			Map clmRetMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
			if(null != clmRetMap && clmRetMap.size() > 0){
				clmRetMap.putAll(JsonStringUtils.jsonStringToObject(clmRet, Map.class));
				clmRet = JsonStringUtils.objectToJsonString(clmRetMap);
			}
			updTdMiPara.setClmRet(clmRet);//医保结算返回
			updTdMiPara.setUpdTime(new Date());
			if(logger.isInfoEnabled()){
				logger.info("开始更新TdMiPara医保结算信息");
			}
			tdOrderServiceImpl.updateTdMiPara(updTdMiPara);
			if(logger.isInfoEnabled()){
				logger.info("<<更新成功");
			}
		}

		return BaseDictConstant.BKU_PAYMENT_RESULT_SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String executeOrderTradeData_ALIPAY(String payOrderId, String alipayFlowId, String status, String closeTime, String totalFee) throws Exception {
		TdOrder tdOrder = tdOrderMapper.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		TdOrdExtPay ordExtPay = tdOrdExtPayMapper.findTdOrdExtPay(Long.valueOf(payOrderId));
		if(null == tdOrder || null == ordExtPay){
			if(logger.isInfoEnabled()){
				logger.info("交易单or支付扩展信息不存在");
			}
			return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
		}
		if(logger.isInfoEnabled()){
			logger.info("当前主交易单状态 tdStatId=" + tdOrder.getTdStatId() + ",商业支付状态 tdBusiStatId=" + ordExtPay.getTdBusiStatId());
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || ordExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("当前交易单已支付成功或商业支付成功，本次请求无需处理");
			}
			return BaseDictConstant.ALIPAY_PAYMENT_RESULT_SUCCESS;
		}
		totalFee = String.valueOf(totalFee).trim();
		if(ValidateUtils.isNotEmpty(totalFee) && ordExtPay.getPaySelf().compareTo(BigDecimal.valueOf(Double.parseDouble(totalFee))) != 0){
			if(logger.isInfoEnabled()){
				logger.info("支付宝支付结果通知金额不正确 paySelf=" + ordExtPay.getPaySelf() + ",total_fee=" + totalFee);
			}
			return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
		}

		boolean paymentProcessResult = false;//支付处理结果 true-成功 false-失败
		String tdBusiStatId;//商业支付状态
		String busiResultCode = "0";//商业支付错误码
		String busiResultDesc = "success";//商业支付错误描述
		String tdSiStatId = "";//医保支付状态
		String siResultCode = "";//医保支付错误码
		String siResultDesc = "";//医保支付错误描述
		String siTdCode = "";//社保交易流水号
		String clmPara = "";
		String clmRet = null;
		if("1".equals(status)){
			if(logger.isInfoEnabled()){
				logger.info("支付宝通知交易单(" + tdOrder.getTdId() + ")结果：支付成功");
			}
			paymentProcessResult = true;
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
		}else{
			if(logger.isInfoEnabled()){
				logger.info("支付宝通知交易单(" + tdOrder.getTdId() + ")结果：支付失败");
			}
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultDesc = "支付失败";
		}

		Date nowDate = new Date();
		TdMiPara tdMiPara = null;
		if(paymentProcessResult){
			redisService.delete("paymentToken_".concat(tdOrder.getFromActId().toString()));
			redisService.delete("ebaoPaymentFlowId_".concat(tdOrder.getTdId().toString()));

			TdSearchTask tdSearchTask = new TdSearchTask();
			tdSearchTask.setTdId(tdOrder.getTdId()); //交易单ID
			tdSearchTask.setFundId(tdOrder.getFundId());
			tdSearchTask.setTdTypeId(tdOrder.getTdTypeId()); //交易类型
			tdSearchTask.setTdStatId(Short.valueOf(tdBusiStatId)); //交易状态
			tdSearchTask.setSearchCnt(0); //查询次数
			tdSearchTask.setTdProcStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_PROC_STAT_ID_TREATMENT_PENDING.getCode()))); //交易任务处理状态
			tdSearchTask.setUpdTime(nowDate); //更新时间
			tdSearchTask.setCrtTime(nowDate); //创建时间
			if(logger.isInfoEnabled()){
				logger.info("开始保存交易查询任务信息-AliPay");
			}
			tdSearchTaskMapper.saveTdSearchTask(tdSearchTask);
			if(logger.isInfoEnabled()){
				logger.info("<<保存成功");
			}

			/*******************发起医保结算 begin*********************/
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
				if(null == tdMiPara){
					if(logger.isInfoEnabled()){
						logger.info("获取TdMiPara信息为空");
					}
					return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
				}
				String medicalClass = String.valueOf(ordExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
				String insuredPersonType = String.valueOf(ordExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
				if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
						Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
						if(null == preClmParaMap || preClmParaMap.size() == 0){
							if(logger.isInfoEnabled()){
								logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
							}
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), ordExtPay.getpSiCardNo(), preClmParaMap.get("fixedHospCode").toString());
						if(null == personInfoRes){
							return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
						}
						TollsReq tollsReq = (TollsReq) preClmParaMap.get("tollsReq");
						tollsReq.getMzToll().setOperDate(DateUtils.getNowDate("yyyyMMddHHmmss"));//经办日期yyyymmddhh24miss
						Map<String, Object> clmParaMap = new HashMap<>();
						clmParaMap.put("fixedHospCode", preClmParaMap.get("fixedHospCode"));
						clmParaMap.put("tollsReq", tollsReq);
						clmPara = JSON.toJSONString(clmParaMap);
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)，请求参数：fixedHospCode=" + preClmParaMap.get("fixedHospCode").toString() + ",cycId=" + personInfoRes.getCycid() + ",tollsReq=" + JSON.toJSONString(tollsReq));
						}
						MzSfmxRes mzSfmxRes = outpatientServiceImpl.executeMzSfmx(preClmParaMap.get("fixedHospCode").toString(), personInfoRes.getCycid(), tollsReq);
						if(null != mzSfmxRes){
							clmRet = JSON.toJSONString(mzSfmxRes);
						}
						if(logger.isInfoEnabled()){
							logger.info("响应报文：mzSfmxRes=" + clmRet);
						}
						if(null == mzSfmxRes || mzSfmxRes.getRTNCode() != 1){
							tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultDesc = "医院门诊医保结算(职工)失败";
							if(logger.isInfoEnabled()){
								logger.info("医院门诊医保结算(职工)失败，进行AliPay商业退款");
							}
							Map<String, Object> inputMap = new HashMap<>();
							inputMap.put("appId", tdOrder.getChanAppid());
							inputMap.put("oriTdId", tdOrder.getTdId().toString());
							inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
							inputMap.put("cause", "Settlement Failure AliPay Refund");
							inputMap.put("payRefundMoney", ordExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
							inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
							inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
							inputMap.put("systemId", tdOrder.getSpSubSysCode());
							inputMap.put("allowRefund", "true");
							Map<String, Object> returnMap = payOrderRefundInterfaceImpl.doService(inputMap);
							if(logger.isInfoEnabled()){
								logger.info("<<申请退款结果：" + returnMap);
							}
							if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
								throw new RuntimeException("<<申请退款出现异常");
							}
						}else {
							String zhpay = String.valueOf(mzSfmxRes.getZhpay()).trim();
							if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
								mzSfmxRes.setZhpay("0");
							}
							BigDecimal payPacct = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
							//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
							String tcpay = String.valueOf(mzSfmxRes.getTcpay()).trim();
							if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
								mzSfmxRes.setTcpay("0");
							}
							String bigpay = String.valueOf(mzSfmxRes.getBigpay()).trim();
							if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
								mzSfmxRes.setBigpay("0");
							}
							String gwybz = String.valueOf(mzSfmxRes.getGwybz()).trim();
							if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
								mzSfmxRes.setGwybz("0");
							}
							String qybcjjpay = String.valueOf(mzSfmxRes.getQybcpay()).trim();
							if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
								mzSfmxRes.setQybcpay("0");
							}
							BigDecimal paySi = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getTcpay()))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getBigpay())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getGwybz())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
							String cashpay = String.valueOf(mzSfmxRes.getCashpay()).trim();
							if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
								mzSfmxRes.setCashpay("0");
							}
							BigDecimal paySelf = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);

							if(logger.isInfoEnabled()){
								logger.info("[AliPay]预结算：payPacct=" + ordExtPay.getPayPacct() + ",paySi=" + ordExtPay.getPaySi() + ",paySelf=" + ordExtPay.getPaySelf() +
										" 结算：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
							}
							siTdCode = String.valueOf(mzSfmxRes.getDealID()).trim();
							if(ordExtPay.getPayPacct().compareTo(payPacct) == 0 &&
							   ordExtPay.getPaySi().compareTo(paySi) == 0 &&
							   ordExtPay.getPaySelf().compareTo(paySelf) == 0){
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)成功");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
								siResultCode = "0";
								siResultDesc = "医院门诊医保结算(职工)成功";
							}else {
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，预结算与结算结果不一致");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

								//进行结算撤销处理
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，进行[AliPay-医保]退款处理");
								}
								Map<String, Object> inputMap = new HashMap<>();
								inputMap.put("appId", tdOrder.getChanAppid());
								inputMap.put("oriTdId", tdOrder.getTdId().toString());
								inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
								inputMap.put("cause", "Settlement Failure Full Refund");
								inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
								inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
								inputMap.put("systemId", tdOrder.getSpSubSysCode());
								inputMap.put("allowRefund", "true");
								Map<String, Object> returnMap = payOrderFullRefundInterfaceImpl.doService(inputMap);
								if(logger.isInfoEnabled()){
									logger.info("<<申请退款结果：" + returnMap);
								}
								if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
									throw new RuntimeException("<<申请退款出现异常");
								}
							}
						}
					}else {
						//居民暂不考虑
					}
				}else {
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){

					}else {
						//居民暂不考虑
					}
				}
			}
			/*******************发起医保结算 end*********************/
		}else {
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		String tdStatId;
		if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdBusiStatId)){
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdSiStatId)){
					tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//商业支付成功&医保支付成功 成功
				}else {
					tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付成功&医保支付失败 失败
				}
			}else {
				tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//纯商业支付成功
			}
		}else {
			tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付失败
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
				siResultDesc = "AliPay支付失败未发起医保结算";
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		alipayFlowId = String.valueOf(alipayFlowId).trim();
		//更新交易单信息
		TdOrder updOrder = new TdOrder();
		updOrder.setTdId(tdOrder.getTdId());//交易单号
		updOrder.setTdStatId(Short.valueOf(tdStatId));//交易状态
		if(ValidateUtils.isNotEmpty(alipayFlowId)){
			updOrder.setFundTdCode(alipayFlowId);
		}
		closeTime = String.valueOf(closeTime).trim();
		if(ValidateUtils.isNotEmpty(closeTime)){
			updOrder.setTdEndTime(DateUtils.strToDate(closeTime, "yyyy-MM-dd HH:mm:ss"));//交易结束时间
		}else{
			updOrder.setTdEndTime(nowDate);//交易结束时间
		}
		if(tdStatId.equals(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()) && "0".equals(busiResultCode)){
			updOrder.setErrCode("支付结果|".concat(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()));//交易失败编码
		}else{
			updOrder.setErrCode("支付结果|".concat(busiResultCode));//交易失败编码
		}

		updOrder.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("更新交易单信息");
		}
		tdOrderMapper.updateTdOrder(updOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		//更新支付信息
		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(tdOrder.getTdId());//交易单号
		updOrdExtPay.setTdBusiStatId(Short.valueOf(tdBusiStatId));//商业支付状态
		if(ValidateUtils.isNotEmpty(tdSiStatId)){
			updOrdExtPay.setTdSiStatId(Short.valueOf(tdSiStatId));//医保支付状态
		}
		if(ValidateUtils.isNotEmpty(alipayFlowId)){
			updOrdExtPay.setFundTdCode(alipayFlowId);
		}
		if(ValidateUtils.isNotEmpty(siTdCode)){
			updOrdExtPay.setSiTdCode(siTdCode);
		}
		updOrdExtPay.setChanRetCode(busiResultCode);//支付通道返回码
		updOrdExtPay.setChanRetDesc(busiResultDesc);//支付通道返回描述
		if(ValidateUtils.isNotEmpty(siResultCode)){
			updOrdExtPay.setSiRetCode(siResultCode);//社保通道返回码
		}
		if(ValidateUtils.isNotEmpty(siResultDesc)){
			updOrdExtPay.setSiRetDesc(siResultDesc);//社保通道返回描述
		}
		updOrdExtPay.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrdExtPayMapper.updateTdOrdExtPay(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		if(ValidateUtils.isNotEmpty(clmPara) && ValidateUtils.isNotEmpty(clmRet)){
			TdMiPara updTdMiPara = new TdMiPara();
			updTdMiPara.setTdId(tdOrder.getTdId());
			updTdMiPara.setClmPara(clmPara);//医保结算提交参数
			Map clmRetMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
			if(null != clmRetMap && clmRetMap.size() > 0){
				clmRetMap.putAll(JsonStringUtils.jsonStringToObject(clmRet, Map.class));
				clmRet = JsonStringUtils.objectToJsonString(clmRetMap);
			}
			updTdMiPara.setClmRet(clmRet);//医保结算返回
			updTdMiPara.setUpdTime(new Date());
			if(logger.isInfoEnabled()){
				logger.info("[AliPay]开始更新TdMiPara医保订单结算信息");
			}
			tdOrderServiceImpl.updateTdMiPara(updTdMiPara);
			if(logger.isInfoEnabled()){
				logger.info("<<更新成功");
			}
		}

		return BaseDictConstant.ALIPAY_PAYMENT_RESULT_SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String executeOrderTradeData_WECHAT(String payOrderId, String weChatPayFlowId, String status, String payFinishTime, String totalFee) throws Exception {
		TdOrder tdOrder = tdOrderMapper.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		TdOrdExtPay ordExtPay = tdOrdExtPayMapper.findTdOrdExtPay(Long.valueOf(payOrderId));
		if(null == tdOrder || null == ordExtPay){
			if(logger.isInfoEnabled()){
				logger.info("交易单or支付扩展信息不存在");
			}
			return BaseDictConstant.WECHAT_PAYMENT_RESULT_FAILURE;
		}
		if(logger.isInfoEnabled()){
			logger.info("当前主交易单状态 tdStatId=" + tdOrder.getTdStatId() + ",商业支付状态 tdBusiStatId=" + ordExtPay.getTdBusiStatId());
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || ordExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("当前交易单已支付成功或商业支付成功，本次请求无需处理");
			}
			return BaseDictConstant.WECHAT_PAYMENT_RESULT_SUCCESS;
		}
		totalFee = String.valueOf(totalFee).trim();
		if(ValidateUtils.isNotEmpty(totalFee) && ordExtPay.getPaySelf().compareTo(BigDecimal.valueOf(Double.parseDouble(totalFee))) != 0){
			if(logger.isInfoEnabled()){
				logger.info("微信支付结果通知金额不正确 paySelf=" + ordExtPay.getPaySelf() + ",total_fee=" + totalFee);
			}
			return BaseDictConstant.WECHAT_PAYMENT_RESULT_FAILURE;
		}

		boolean paymentProcessResult = false;//支付处理结果 true-成功 false-失败
		String tdBusiStatId;//商业支付状态
		String busiResultCode = "0";//商业支付错误码
		String busiResultDesc = "success";//商业支付错误描述
		String tdSiStatId = "";//医保支付状态
		String siResultCode = "";//医保支付错误码
		String siResultDesc = "";//医保支付错误描述
		String siTdCode = "";//社保交易流水号
		String clmPara = "";
		String clmRet = null;
		if("1".equals(status)){
			if(logger.isInfoEnabled()){
				logger.info("微信通知交易单(" + tdOrder.getTdId() + ")结果：支付成功");
			}
			paymentProcessResult = true;
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
		}else{
			if(logger.isInfoEnabled()){
				logger.info("微信通知交易单(" + tdOrder.getTdId() + ")结果：支付失败");
			}
			tdBusiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			busiResultDesc = "支付失败";
		}

		Date nowDate = new Date();
		TdMiPara tdMiPara = null;
		if(paymentProcessResult){
			redisService.delete("paymentToken_".concat(tdOrder.getFromActId().toString()));
			redisService.delete("ebaoPaymentFlowId_".concat(tdOrder.getTdId().toString()));

			TdSearchTask tdSearchTask = new TdSearchTask();
			tdSearchTask.setTdId(tdOrder.getTdId()); //交易单ID
			tdSearchTask.setFundId(tdOrder.getFundId());
			tdSearchTask.setTdTypeId(tdOrder.getTdTypeId()); //交易类型
			tdSearchTask.setTdStatId(Short.valueOf(tdBusiStatId)); //交易状态
			tdSearchTask.setSearchCnt(0); //查询次数
			tdSearchTask.setTdProcStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_PROC_STAT_ID_TREATMENT_PENDING.getCode()))); //交易任务处理状态
			tdSearchTask.setUpdTime(nowDate); //更新时间
			tdSearchTask.setCrtTime(nowDate); //创建时间
			if(logger.isInfoEnabled()){
				logger.info("开始保存交易查询任务信息-WeChat");
			}
			tdSearchTaskMapper.saveTdSearchTask(tdSearchTask);
			if(logger.isInfoEnabled()){
				logger.info("<<保存成功");
			}

			/*******************发起医保结算 begin*********************/
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
				if(null == tdMiPara){
					if(logger.isInfoEnabled()){
						logger.info("获取TdMiPara信息为空");
					}
					return BaseDictConstant.WECHAT_PAYMENT_RESULT_FAILURE;
				}
				String medicalClass = String.valueOf(ordExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
				String insuredPersonType = String.valueOf(ordExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
				if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
						Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
						if(null == preClmParaMap || preClmParaMap.size() == 0){
							if(logger.isInfoEnabled()){
								logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
							}
							return BaseDictConstant.WECHAT_PAYMENT_RESULT_FAILURE;
						}
						PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), ordExtPay.getpSiCardNo(), preClmParaMap.get("fixedHospCode").toString());
						if(null == personInfoRes){
							return BaseDictConstant.WECHAT_PAYMENT_RESULT_FAILURE;
						}
						TollsReq tollsReq = (TollsReq) preClmParaMap.get("tollsReq");
						tollsReq.getMzToll().setOperDate(DateUtils.getNowDate("yyyyMMddHHmmss"));//经办日期yyyymmddhh24miss
						Map<String, Object> clmParaMap = new HashMap<>();
						clmParaMap.put("fixedHospCode", preClmParaMap.get("fixedHospCode"));
						clmParaMap.put("tollsReq", tollsReq);
						clmPara = JSON.toJSONString(clmParaMap);
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)，请求参数：fixedHospCode=" + preClmParaMap.get("fixedHospCode").toString() + ",cycId=" + personInfoRes.getCycid() + ",tollsReq=" + JSON.toJSONString(tollsReq));
						}
						MzSfmxRes mzSfmxRes = outpatientServiceImpl.executeMzSfmx(preClmParaMap.get("fixedHospCode").toString(), personInfoRes.getCycid(), tollsReq);
						if(null != mzSfmxRes){
							clmRet = JSON.toJSONString(mzSfmxRes);
						}
						if(logger.isInfoEnabled()){
							logger.info("响应报文：mzSfmxRes=" + clmRet);
						}
						if(null == mzSfmxRes || mzSfmxRes.getRTNCode() != 1){
							tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siResultDesc = "医院门诊医保结算(职工)失败";
							if(logger.isInfoEnabled()){
								logger.info("医院门诊医保结算(职工)失败，进行WeChat商业退款");
							}
							Map<String, Object> inputMap = new HashMap<>();
							inputMap.put("appId", tdOrder.getChanAppid());
							inputMap.put("oriTdId", tdOrder.getTdId().toString());
							inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
							inputMap.put("cause", "Settlement Failure WeChat Refund");
							inputMap.put("payRefundMoney", ordExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
							inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
							inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
							inputMap.put("systemId", tdOrder.getSpSubSysCode());
							inputMap.put("allowRefund", "true");
							Map<String, Object> returnMap = payOrderRefundInterfaceImpl.doService(inputMap);
							if(logger.isInfoEnabled()){
								logger.info("<<申请退款结果：" + returnMap);
							}
							if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
								throw new RuntimeException("<<申请退款出现异常");
							}
						}else {
							String zhpay = String.valueOf(mzSfmxRes.getZhpay()).trim();
							if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
								mzSfmxRes.setZhpay("0");
							}
							BigDecimal payPacct = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
							//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
							String tcpay = String.valueOf(mzSfmxRes.getTcpay()).trim();
							if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
								mzSfmxRes.setTcpay("0");
							}
							String bigpay = String.valueOf(mzSfmxRes.getBigpay()).trim();
							if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
								mzSfmxRes.setBigpay("0");
							}
							String gwybz = String.valueOf(mzSfmxRes.getGwybz()).trim();
							if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
								mzSfmxRes.setGwybz("0");
							}
							String qybcjjpay = String.valueOf(mzSfmxRes.getQybcpay()).trim();
							if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
								mzSfmxRes.setQybcpay("0");
							}
							BigDecimal paySi = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getTcpay()))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getBigpay())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getGwybz())))
									.add(BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
							String cashpay = String.valueOf(mzSfmxRes.getCashpay()).trim();
							if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
								mzSfmxRes.setCashpay("0");
							}
							BigDecimal paySelf = BigDecimal.valueOf(Double.parseDouble(mzSfmxRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);

							if(logger.isInfoEnabled()){
								logger.info("[WeChat]预结算：payPacct=" + ordExtPay.getPayPacct() + ",paySi=" + ordExtPay.getPaySi() + ",paySelf=" + ordExtPay.getPaySelf() +
										" 结算：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
							}
							siTdCode = String.valueOf(mzSfmxRes.getDealID()).trim();
							if(ordExtPay.getPayPacct().compareTo(payPacct) == 0 &&
									ordExtPay.getPaySi().compareTo(paySi) == 0 &&
									ordExtPay.getPaySelf().compareTo(paySelf) == 0){
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)成功");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
								siResultCode = "0";
								siResultDesc = "医院门诊医保结算(职工)成功";
							}else {
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，预结算与结算结果不一致");
								}
								tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
								siResultDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

								//进行结算撤销处理
								if(logger.isInfoEnabled()){
									logger.info("医院门诊医保结算(职工)失败，进行[WeChat-医保]退款处理");
								}
								Map<String, Object> inputMap = new HashMap<>();
								inputMap.put("appId", tdOrder.getChanAppid());
								inputMap.put("oriTdId", tdOrder.getTdId().toString());
								inputMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
								inputMap.put("cause", "Settlement Failure Full Refund");
								inputMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
								inputMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
								inputMap.put("systemId", tdOrder.getSpSubSysCode());
								inputMap.put("allowRefund", "true");
								Map<String, Object> returnMap = payOrderFullRefundInterfaceImpl.doService(inputMap);
								if(logger.isInfoEnabled()){
									logger.info("<<申请退款结果：" + returnMap);
								}
								if(!Hint.SYS_SUCCESS.getCodeString().equals(returnMap.get("resultCode").toString())){
									throw new RuntimeException("<<申请退款出现异常");
								}
							}
						}
					}else {
						//居民暂不考虑
					}
				}else {
					if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){

					}else {
						//居民暂不考虑
					}
				}
			}
			/*******************发起医保结算 end*********************/
		}else {
			if(ordExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		String tdStatId;
		if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdBusiStatId)){
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString().equals(tdSiStatId)){
					tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//商业支付成功&医保支付成功 成功
				}else {
					tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付成功&医保支付失败 失败
				}
			}else {
				tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();//纯商业支付成功
			}
		}else {
			tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();//商业支付失败
			if(ValidateUtils.isNotEmpty(tdSiStatId)){
				siResultCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
				siResultDesc = "WeChat支付失败未发起医保结算";
				tdSiStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
			}
		}

		weChatPayFlowId = String.valueOf(weChatPayFlowId).trim();
		//更新交易单信息
		TdOrder updOrder = new TdOrder();
		updOrder.setTdId(tdOrder.getTdId());//交易单号
		updOrder.setTdStatId(Short.valueOf(tdStatId));//交易状态
		if(ValidateUtils.isNotEmpty(weChatPayFlowId)){
			updOrder.setFundTdCode(weChatPayFlowId);
		}
		payFinishTime = String.valueOf(payFinishTime).trim();
		if(ValidateUtils.isNotEmpty(payFinishTime)){
			updOrder.setTdEndTime(DateUtils.strToDate(payFinishTime, "yyyy-MM-dd HH:mm:ss"));//交易结束时间
		}else{
			updOrder.setTdEndTime(nowDate);//交易结束时间
		}
		if(tdStatId.equals(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()) && "0".equals(busiResultCode)){
			updOrder.setErrCode("支付结果|".concat(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()));//交易失败编码
		}else{
			updOrder.setErrCode("支付结果|".concat(busiResultCode));//交易失败编码
		}

		updOrder.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("更新交易单信息");
		}
		tdOrderMapper.updateTdOrder(updOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		//更新支付信息
		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(tdOrder.getTdId());//交易单号
		updOrdExtPay.setTdBusiStatId(Short.valueOf(tdBusiStatId));//商业支付状态
		if(ValidateUtils.isNotEmpty(tdSiStatId)){
			updOrdExtPay.setTdSiStatId(Short.valueOf(tdSiStatId));//医保支付状态
		}
		if(ValidateUtils.isNotEmpty(weChatPayFlowId)){
			updOrdExtPay.setFundTdCode(weChatPayFlowId);
		}
		if(ValidateUtils.isNotEmpty(siTdCode)){
			updOrdExtPay.setSiTdCode(siTdCode);
		}
		updOrdExtPay.setChanRetCode(busiResultCode);//支付通道返回码
		updOrdExtPay.setChanRetDesc(busiResultDesc);//支付通道返回描述
		if(ValidateUtils.isNotEmpty(siResultCode)){
			updOrdExtPay.setSiRetCode(siResultCode);//社保通道返回码
		}
		if(ValidateUtils.isNotEmpty(siResultDesc)){
			updOrdExtPay.setSiRetDesc(siResultDesc);//社保通道返回描述
		}
		updOrdExtPay.setUpdTime(nowDate);
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrdExtPayMapper.updateTdOrdExtPay(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		if(ValidateUtils.isNotEmpty(clmPara) && ValidateUtils.isNotEmpty(clmRet)){
			TdMiPara updTdMiPara = new TdMiPara();
			updTdMiPara.setTdId(tdOrder.getTdId());
			updTdMiPara.setClmPara(clmPara);//医保结算提交参数
			Map clmRetMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
			if(null != clmRetMap && clmRetMap.size() > 0){
				clmRetMap.putAll(JsonStringUtils.jsonStringToObject(clmRet, Map.class));
				clmRet = JsonStringUtils.objectToJsonString(clmRetMap);
			}
			updTdMiPara.setClmRet(clmRet);//医保结算返回
			updTdMiPara.setUpdTime(new Date());
			if(logger.isInfoEnabled()){
				logger.info("[WeChat]开始更新TdMiPara医保订单结算信息");
			}
			tdOrderServiceImpl.updateTdMiPara(updTdMiPara);
			if(logger.isInfoEnabled()){
				logger.info("<<更新成功");
			}
		}

		return BaseDictConstant.WECHAT_PAYMENT_RESULT_SUCCESS;
	}
}

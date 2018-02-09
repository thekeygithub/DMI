package com.models.cloud.gw.service.trade.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.models.cloud.cert.utils.IdCardNoUtils;
import com.models.cloud.common.cache.account.entity.ActSpInfo;
import com.models.cloud.common.cache.dict.HospitalTreatmentLimitCache;
import com.models.cloud.common.cache.service.AccountCacheService;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.payuser.PayUserServiceGW;
import com.models.cloud.gw.service.trade.EbaoAsynchronousNotice;
import com.models.cloud.gw.service.trade.TdOrderServiceGW;
import com.models.cloud.pay.escrow.alipay.dto.BaseDTO;
import com.models.cloud.pay.escrow.alipay.param.AlipayImmediatePayParam;
import com.models.cloud.pay.escrow.alipay.param.AlipayMobileParam;
import com.models.cloud.pay.escrow.alipay.service.AlipayService;
import com.models.cloud.pay.escrow.alipay.utils.EBaoConstants;
import com.models.cloud.pay.escrow.ccb.service.NetPayService;
import com.models.cloud.pay.escrow.chinapay.service.CPNetPayService;
import com.models.cloud.pay.escrow.ebao.response.SocialCardInfo;
import com.models.cloud.pay.escrow.ebao.service.EbaoService;
import com.models.cloud.pay.escrow.mi.pangu.request.MxbInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TollsReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TreatmentStatusReq;
import com.models.cloud.pay.escrow.mi.pangu.response.*;
import com.models.cloud.pay.escrow.mi.pangu.service.CommInfoService;
import com.models.cloud.pay.escrow.mi.pangu.service.OutpatientService;
import com.models.cloud.pay.escrow.mts.request.MtsParam;
import com.models.cloud.pay.escrow.mts.response.MtsRespResult;
import com.models.cloud.pay.escrow.mts.service.MTSService;
import com.models.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.models.cloud.pay.escrow.wechat.service.WeChatAppPayService;
import com.models.cloud.pay.escrow.wechat.utils.WeChatUtil;
import com.models.cloud.pay.payment.service.PaymentService;
import com.models.cloud.pay.payuser.entity.*;
import com.models.cloud.pay.payuser.service.ActPBankService;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.proplat.entity.PpFeeRatioA;
import com.models.cloud.pay.proplat.entity.PpFundBankRel;
import com.models.cloud.pay.proplat.entity.PpFundPlatform;
import com.models.cloud.pay.proplat.entity.PpFundType;
import com.models.cloud.pay.proplat.service.*;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.pay.supplier.service.ActSpService;
import com.models.cloud.pay.trade.entity.*;
import com.models.cloud.pay.trade.service.ActSpBusiRelService;
import com.models.cloud.pay.trade.service.PayOrderService;
import com.models.cloud.pay.trade.service.TdOrderService;
import com.models.cloud.util.*;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;
import com.models.secrity.crypto.AesCipher;
import com.models.secrity.crypto.RSA;

@Service("tdOrderServiceGWImpl")
public class TdOrderServiceGWImpl implements TdOrderServiceGW {

	private static final Logger logger = Logger.getLogger(TdOrderServiceGWImpl.class);
	private static final Logger miPreSettleError = Logger.getLogger("miPreSettleError");

	@Resource
	private PayUserService payUserServiceImpl;
	@Resource
	private RedisService redisService;
	@Resource
	private ActSpService actSpServiceImpl;
	@Resource
	private PayUserServiceGW payUserServiceGWImpl;
	@Resource
	private PaymentService yeepayPaymentServiceImpl;
	@Resource
	private TdOrderService tdOrderServiceImpl;
	@Resource
	private PpFeeRatioAService ppFeeRatioAServiceImpl;
	@Resource
	private PpFundBankRelService ppFundBankRelServiceImpl;
	@Resource
	private ActPBankService actPBankServiceImpl;
	@Resource
	private AccountCacheService accountCacheServiceImpl;
	@Resource
	private BankImageService bankImageServiceImpl;
	@Resource
	private ActSpBusiRelService actSpBusiRelServiceImpl;
	@Resource
	private PayOrderService payOrderServiceImpl;
	@Resource
	private NetPayService netPayServiceImpl;
	@Resource
	private CPNetPayService cPNetPayServiceImpl;
	@Resource
	private AlipayService aliPayServiceImpl;
	@Resource
	private CommInfoService commInfoServiceImpl;
	@Resource
	private OutpatientService outpatientServiceImpl;
	@Resource
	private EbaoService ebaoServiceImpl;
	@Resource
	private MTSService mTSServiceImpl;
	@Resource
	private DoServiceInterface payOrderFullRefundInterfaceImpl;
	@Resource
	private WeChatAppPayService weChatAppPayServiceImpl;

	private static final ExecutorService executorService = Executors.newFixedThreadPool(50);
	private static final ExecutorService mailService = Executors.newFixedThreadPool(50);

	/**
	 * 查询交易单：支付完成返回商户
	 *    查询数据库中的支付订单信息，订单状态为成功或者失败直接返回结果；进行中或其他情况需要向易宝发起查询订单的请求
	 *    查询订单状态结果，失败--则修改数据库中订单状态为失败；成功--则修改数据库中订单状态为成功，并增加一条支付流水；
	 * 
	 * @param inputMap
	 * @return
	 * @throws Exception
     */
	public Map<String, Object> queryTdOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("请求查询交易单信息接口，请求参数："+inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String ebOrderId = String.valueOf(inputMap.get("ebOrderId")).trim();//易宝交易流水号
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付订单号
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID
		if(logger.isInfoEnabled()){
			logger.info("验证账户是否登录");
		}

		if(ValidateUtils.isNotEmpty(accountId)){
			Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
			if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
				return checkPayUserMap;
			}
		}

		if(ValidateUtils.isEmpty(payOrderId)){
			if(logger.isInfoEnabled()){
				logger.info("支付订单号不存在");
			}
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payOrderId"));
			return resultMap;
		}

		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		if(logger.isInfoEnabled()){
			logger.info("查询交易订单信息："+tdOrder);
		}
		if(null == tdOrder){
			resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
			return resultMap;
		}
		TdOrdExtPay tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(Long.valueOf(payOrderId));
		
		if(!"".equals(ebOrderId) && !ebOrderId.equals(tdOrder.getFundTdCode())){
			if(logger.isInfoEnabled()){
				logger.info("易宝交易流水号无效或不存在");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13061_FUND_ID_CODE_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13061_FUND_ID_CODE_INVALID.getMessage());
			return resultMap;
		}
		
		//1.返回结果为非成功或者失败则请求易宝支付查询支付结果
		if(logger.isInfoEnabled()){
			logger.info(payOrderId+" 的订单交易状态："+tdOrder.getTdStatId()+" 其中【初始化100】【进行中101】【成功110】【失败120】【异常130】");
		}
		payOrderId = payOrderId.trim();
		boolean orderIsTimeout = false;
		String payOrderExpire = tdOrder.getTdLimitSec().toString();
		Date nowDate = new Date();
		if(logger.isInfoEnabled()){
			logger.info("交易单创建时间：" + DateUtils.dateToString(tdOrder.getCrtTime(), "yyyy-MM-dd HH:mm:ss") +
					" 当前时间：" + DateUtils.dateToString(nowDate, "yyyy-MM-dd HH:mm:ss") + " 订单有效期：" + payOrderExpire);
		}
		if(nowDate.getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(payOrderExpire) * 1000)){
			orderIsTimeout = true;
		}
		if((tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode() && orderIsTimeout) ||
		   tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("请求易宝调用支付结果查询接口，请求参数：payOrderId:"+payOrderId+"-"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
			Map<String, String> responseMap = yeepayPaymentServiceImpl.payapiQueryByTdid(payOrderId+"-"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			if(logger.isInfoEnabled()){
				logger.info("请求易宝调用支付结果查询接口，返回结果："+responseMap);
			}
			if(responseMap.size() == 0 || responseMap.containsKey("customError")){
				resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
				return resultMap;
			}
			if(!responseMap.containsKey("error_code") || (responseMap.containsKey("error_code") && "600072".equals(responseMap.get("error_code")))){
				if(!responseMap.containsKey("error_code") && (ValidateUtils.isEmpty(responseMap.get("status")) || ValidateUtils.isEmpty(responseMap.get("closetime")))){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
				int status;
				String errorcode;
				String errormsg;
				if(responseMap.containsKey("error_code") && "600072".equals(responseMap.get("error_code"))){//订单不存在
					status = 0;
					errorcode = "600072";
					errormsg = "订单未支付";
				}else {
					status = Integer.valueOf(responseMap.get("status"));
					errorcode = responseMap.get("errorcode");
					errormsg = responseMap.get("errormsg");
				}

				if(logger.isInfoEnabled()){
					logger.info("易宝返回订单交易状态:"+responseMap.get("status"));
				}
				//0：失败 1：成功 2：未处理 3：处理中 4：已撤销
				if(status == 0 || status == 1 || status == 4){
					String orderStatus = "1";
					if(status == 0 || status == 4){
						orderStatus = "0";
					}
					if(logger.isInfoEnabled()){
						logger.info("易宝返回订单状态为【失败0】【成功1】，更新库中订单交易状态");
					}
					String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
					int expire = 60;
					Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
					if(logger.isInfoEnabled()){
						logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
					}
					if(null == redisValue || redisValue == 0){
						if(logger.isInfoEnabled()){
							logger.info("当前订单发生并发请求，已阻止");
						}
						resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
						resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
						return resultMap;
					}
					String result;
					try {
						result = payOrderServiceImpl.executeOrderTradeData_EBZF(payOrderId, responseMap.get("yborderid"), orderStatus,
								responseMap.get("closetime"), errorcode, errormsg, responseMap.get("bindid"));
					}catch (Exception e) {
						if(logger.isInfoEnabled()){
							logger.info("系统异常：" + e.getMessage());
						}
						result = BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
					}
					redisService.delete(redisKey);
					if(logger.isInfoEnabled()){
						logger.info("更新数据 -- 返回："+result);
					}
					if(!result.equals(BaseDictConstant.YEEPAY_PAYMENT_RESULT_SUCCESS)){
						//更新数据失败
						if(logger.isInfoEnabled()){
							logger.info("更新数据失败");
						}
						resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
						resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
						return resultMap;
					}
					tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
				}
			}else{
				resultMap.put("resultCode", responseMap.get("error_code"));
				resultMap.put("resultDesc", responseMap.get("error_msg"));
				return resultMap;
			}
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("payOrderid", payOrderId);//商户订单号
		String errorCodeDesc = String.valueOf(tdOrder.getErrCode()).trim();
		if(ValidateUtils.isEmpty(errorCodeDesc)){
			errorCodeDesc = "";
		}
		resultMap.put("errorCodeDesc", errorCodeDesc);

		String yeepayOrderId = String.valueOf(tdOrder.getFundTdCode()).trim();
		if(ValidateUtils.isEmpty(yeepayOrderId)){
			yeepayOrderId = "";
		}
		resultMap.put("ebOrderId", yeepayOrderId);//易保钱包生成的交易流水号
		BigDecimal amount = tdOrdExtPay.getPaySelf();
		if(null == amount || amount.doubleValue() < 0){
			amount = BigDecimal.ZERO;
		}
		resultMap.put("amount", amount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//订单金额(以"分"为单位的整型)
		resultMap.put("currency", BaseDictConstant.YEEPAY_TRADE_CURRENCY_RMB);//交易币种
		BigDecimal sourceFee = tdOrder.getServChrgAmt();
		if(null == sourceFee || sourceFee.doubleValue() < 0){
			sourceFee = BigDecimal.ZERO;
		}
		resultMap.put("sourceFee", sourceFee.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//付款方手续费
		resultMap.put("targetFee", new BigDecimal(0.00).toString());//收款方手续费
		BigDecimal tradeAmount = tdOrdExtPay.getPaySelf();
		if(null == tradeAmount || tradeAmount.doubleValue() < 0){
			tradeAmount = BigDecimal.ZERO;
		}
		resultMap.put("sourceAmount", tradeAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//付款方实付金额
		resultMap.put("targetAmount", tradeAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//收款方实收金额
		resultMap.put("orderTime", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyy-MM-dd HH:mm:ss"));//下单时间
		if(null != tdOrder.getTdEndTime()){
			resultMap.put("closeTime", DateUtils.dateToString(tdOrder.getTdEndTime(), "yyyy-MM-dd HH:mm:ss"));//交易时间
		}else{
			resultMap.put("closeTime", "");//交易时间
		}
		if(ValidateUtils.isNotEmpty(tdOrder.getDispSpName())){
			resultMap.put("targetName", tdOrder.getDispSpName());//收款方名称--合作商户
		}else{
			resultMap.put("targetName", "");//收款方名称--合作商户
		}
		if(ValidateUtils.isNotEmpty(tdOrder.getGoodsDesc())){
			resultMap.put("productDesc", tdOrder.getGoodsDesc());//商品描述
		}else{
			resultMap.put("productDesc", "");//商品描述
		}
		resultMap.put("shOrderId", tdOrder.getOrigOrdCode());//商户订单号
		int status = 0;
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
			status = 3;
		}else if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode()){
			status = 2;
		}else if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			status = 0;
		}else if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
			status = 1;
		}
		resultMap.put("status", String.valueOf(status));//支付状态
		if(logger.isInfoEnabled()){
			logger.info("查询支付扩展表信息：" + tdOrdExtPay);
		}
		if(tdOrdExtPay != null){
			BigDecimal refundTotal = tdOrdExtPay.getAccRetAmt();
			if(null == refundTotal || refundTotal.doubleValue() < 0){
				refundTotal = BigDecimal.ZERO;
			}
			resultMap.put("refundTotal", refundTotal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//累计退款金额
			if(ValidateUtils.isNotEmpty(tdOrdExtPay.getChanRetDesc())){
				resultMap.put("description", tdOrdExtPay.getChanRetDesc());//支付描述
			}else{
				resultMap.put("description", "");//支付描述
			}
			String flowFlag = "";
			if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600095.equals(tdOrdExtPay.getChanRetCode()) ||
			   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_601036.equals(tdOrdExtPay.getChanRetCode()) ||
			   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600093.equals(tdOrdExtPay.getChanRetCode())){
				flowFlag = "1";
			}else if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600051.equals(tdOrdExtPay.getChanRetCode())){
				flowFlag = "2";
			}
			resultMap.put("flowFlag", flowFlag);
		}else{
			resultMap.put("refundTotal", "");//累计退款金额
			resultMap.put("description", "");//支付描述
			resultMap.put("flowFlag", "");
		}
		
		if(tdOrder.getToActId() == null){
			if(logger.isInfoEnabled()){
				logger.info("查询平台商户表信息,toActId为空");
			}
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "toActId"));
			return resultMap;
		}
		
		ActSp actSp = actSpServiceImpl.selectByActId(tdOrder.getToActId());
		if(logger.isInfoEnabled()){
			logger.info("查询平台商户表信息：" + actSp);
		}
		if(null == actSp){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		resultMap.put("merchantName", actSp.getEntName());//商户名称--渠道商户
		
		return resultMap;
	}

	/**
	 * 根据订单号获取redis中支付流水号
	 * @param payOrderId
	 * @return
	 * @throws Exception
     */
	private String getEbaoPaymentFlowIdInRedis(String payOrderId) throws Exception {
		String redisKey = "ebaoPaymentFlowId_".concat(payOrderId);
		String redisValue = String.valueOf(redisService.get(redisKey)).trim();
		if(logger.isInfoEnabled()){
			logger.info("获取Redis中支付流水号 redisKey=" + redisKey + ",redisValue=" + redisValue);
		}
		if(ValidateUtils.isEmpty(redisValue)){
			redisValue = String.valueOf(BaseDictConstant.TRANSACTION_SERIAL_NUMBER);
		}
		return redisValue;
	}

	/**
	 * 更新redis中支付流水号
	 * @param tdOrder
	 * @throws Exception
     */
	private void updEbaoPaymentFlowIdInRedis(TdOrder tdOrder) throws Exception {
		String redisKey = "ebaoPaymentFlowId_".concat(tdOrder.getTdId().toString());
		String redisValue = this.getEbaoPaymentFlowIdInRedis(tdOrder.getTdId().toString());
		redisValue = BigDecimal.valueOf(Double.valueOf(redisValue)).add(BigDecimal.ONE).toString();
		if(logger.isInfoEnabled()){
			logger.info("更新Redis中支付流水号 redisKey=" + redisKey + ",redisValue=" + redisValue);
		}
		redisService.set(redisKey, redisValue);
		tdOrder.setTdOrder(BigDecimal.valueOf(Double.valueOf(redisValue)));
	}

	/**
	 * 保存易保支付流水号存入Redis
	 * @param payOrderId
	 * @param tradeNumber
	 * @param payOrderRedisExpire
	 * @throws Exception
     */
	private void setEbaoPaymentFlowIdInRedis(String payOrderId, String tradeNumber, String payOrderRedisExpire) throws Exception {
		String ebaoPaymentFlowIdRedisKey = "ebaoPaymentFlowId_".concat(payOrderId);
		if(logger.isInfoEnabled()){
			logger.info("支付流水号存入Redis中 ebaoPaymentFlowIdRedisKey=" + ebaoPaymentFlowIdRedisKey + ",ebaoPaymentFlowIdRedisValue=" + tradeNumber + ",ebaoPaymentFlowIdRedisExpire=" + payOrderRedisExpire);
		}
		redisService.set(ebaoPaymentFlowIdRedisKey, tradeNumber);
		redisService.expire(ebaoPaymentFlowIdRedisKey, Integer.parseInt(payOrderRedisExpire));
		if(logger.isInfoEnabled()){
			logger.info("<<保存成功");
		}
	}

	/**
	 * 支付-预下单
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> createPayOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> createPayOrder 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String appId = String.valueOf(inputMap.get("appId")).trim();//渠道商户ID
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户订单号
		String merOrderExpire = String.valueOf(inputMap.get("merOrderExpire")).trim();//商户订单有效期，单位：秒
		String goodsDesc = String.valueOf(inputMap.get("goodsDesc")).trim();//商品描述信息
		String workSpActId = String.valueOf(inputMap.get("workSpActId")).trim();//合作商户账号ID
		String transBusiType = String.valueOf(inputMap.get("transBusiType")).trim();//交易业务类型 201-缴费 202-挂号 203-购药
		BigDecimal payMoney = BigDecimal.valueOf(Double.parseDouble(String.valueOf(inputMap.get("payMoney")).trim())).setScale(2, BigDecimal.ROUND_HALF_UP);//支付金额
		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();
		String callbackUrl = String.valueOf(inputMap.get("callbackUrl")).trim();
		String systemId = String.valueOf(inputMap.get("systemId")).trim();
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();

		String fundId = String.valueOf(CacheUtils.getDimSysConfConfValue(BaseDictConstant.CUR_PP_FUND_ID)).trim();
		if(ValidateUtils.isEmpty(fundId)){
			if(logger.isInfoEnabled()){
				logger.info("fundId=" + fundId + " 资金平台ID为空");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}
		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(fundId);
		if(null == ppFundPlatform || ppFundPlatform.getValidFlag() != DimDictEnum.VALID_FLAG_VALID.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("fundId=" + fundId + " 资金平台无效或不存在");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}

		PayUser payUser = null;
		ActPerson actPerson = null;
		if(ValidateUtils.isNotEmpty(accountId)){
			Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
			if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
				return checkPayUserMap;
			}
			payUser = (PayUser) checkPayUserMap.get("payUser");
			actPerson = (ActPerson) checkPayUserMap.get("actPerson");
		}

		ActSp channelSp = actSpServiceImpl.findByChannelAppId(appId);
		if(null == channelSp){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		if(channelSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}
		ActSp workSp = actSpServiceImpl.findByWorkSpActId(Long.valueOf(workSpActId));
		if(null == workSp){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL.getMessage());
			return resultMap;
		}
		if(workSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}
		if(null == workSp.getChanActId() || channelSp.getActId().longValue() != workSp.getChanActId().longValue()){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID.getMessage());
			return resultMap;
		}
		TdOrder tdOrder = new TdOrder();
		tdOrder.setChanAppid(appId);
		tdOrder.setOrigOrdCode(merOrderId);
		long orderCount = 0;
		List<Long> orderCountList = tdOrderServiceImpl.findTdOrderCountByMerOrderId(tdOrder);
		for(Long count : orderCountList){
			orderCount = orderCount + count;
		}
		if(orderCount > 0){
			resultMap.put("resultCode", Hint.CHAN_USER_13003_MERORDERID_EXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_USER_13003_MERORDERID_EXIST.getMessage());
			return resultMap;
		}
		tdOrder = new TdOrder();
		tdOrder.setTdId(Long.parseLong(IdCreatorUtils.getPayOrderId()));//支付订单号
		tdOrder.setTdOrder(BigDecimal.valueOf(BaseDictConstant.TRANSACTION_SERIAL_NUMBER).setScale(0, BigDecimal.ROUND_HALF_UP));//交易序号
		tdOrder.setMicId(channelSp.getMicId());//医保项目ID
		tdOrder.setTdTypeId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_TYPE_PAYMENT.getCode())));//交易类型
		tdOrder.setTdBusiTypeId(Short.valueOf(transBusiType));//交易业务类型
		tdOrder.setChanActId(channelSp.getActId());//渠道账户ID
		tdOrder.setChanAppid(appId);//渠道账户APPID
		tdOrder.setTdOperChanTypeId(Short.valueOf(terminalType));//交易操作通道类别
		tdOrder.setFromActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_PRIVATE.getCode())));//付款账户类型
		if(ValidateUtils.isNotEmpty(accountId)){
			tdOrder.setFromActId(Long.valueOf(accountId));//付款账户ID
		}else {
			tdOrder.setFromActId(Long.valueOf(IdCreatorUtils.getAccountRecordId()));//为空时默认补充一个不存在的账户ID，主要为了兼容H5
		}
		if(null != payUser){
			tdOrder.setPayUserId(payUser.getPayUserId());//付款用户ID
		}
		tdOrder.setToActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_MERCHANT.getCode())));//收款账户类型
		tdOrder.setToActId(channelSp.getActId());//收款账户ID
		tdOrder.setDispSpActId(workSp.getActId());//显示商户账户ID(合作商户)
		tdOrder.setDispSpName(workSp.getEntName());//显示商户名称
		tdOrder.setGoodsDesc(goodsDesc);//商品描述
		tdOrder.setFundId(fundId);//资金平台ID
		tdOrder.setCbAddr(callbackUrl);//商户后台系统的回调地址
		tdOrder.setPayTot(payMoney);//交易总金额
		tdOrder.setRecvCur(payMoney);//本次实收金额
		tdOrder.setOrigOrdCode(merOrderId);//渠道商户订单号
		tdOrder.setSpSubSysCode(systemId);//商户子系统编号

		String payOrderExpire = Propertie.APPLICATION.value("payOrder.expire");
		if(ValidateUtils.isEmpty(merOrderExpire) || Integer.parseInt(merOrderExpire) > Integer.parseInt(payOrderExpire)){
			merOrderExpire = payOrderExpire;
		}
		tdOrder.setTdLimitSec(Integer.valueOf(merOrderExpire));

		tdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCode())));//交易状态
		tdOrder.setTdDate(DateUtils.getNowDate("yyyyMMdd"));//交易日期
		tdOrder.setTdStartTime(new Date());//申请交易时间
		tdOrder.setConfirmStatId(Short.valueOf(String.valueOf(DimDictEnum.CONFIRM_STAT_WAIT_CHECK.getCode())));//对账状态
		tdOrder.setCrtTime(new Date());//创建时间

		TdOrdExtPay tdOrdExtPay = new TdOrdExtPay();
		tdOrdExtPay.setTdId(tdOrder.getTdId());
		tdOrdExtPay.setPayTypeId(Short.valueOf(DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString()));//支付类型
		tdOrdExtPay.setTdBusiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCodeString()));//商业支付状态
		tdOrdExtPay.setTdSiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCodeString()));//社保支付状态
		if(null != actPerson){
			tdOrdExtPay.setpIdNo(actPerson.getpCertNo().toUpperCase());//身份证号
		}
		tdOrdExtPay.setPaySelf(payMoney);//个人支付金额
		tdOrdExtPay.setPaySi(BigDecimal.ZERO);//社保报销金额(统筹基金)
		tdOrdExtPay.setPayPacct(BigDecimal.ZERO);//个人账户支付金额(医保个账)
		tdOrdExtPay.setTdRetFlag(Short.valueOf("0"));//是否被退款 1-是 0-否
		tdOrdExtPay.setAccRetAmt(BigDecimal.ZERO);//累计退款金额

		if(logger.isInfoEnabled()){
			logger.info("保存交易单信息入库 payOrderId=" + tdOrder.getTdId() + ",merOrderExpire=" + merOrderExpire);
		}
		tdOrderServiceImpl.saveTdOrderAndExtPayInfo(tdOrder, tdOrdExtPay, null);
		if(logger.isInfoEnabled()){
			logger.info("<<保存成功");
		}

		this.setEbaoPaymentFlowIdInRedis(tdOrder.getTdId().toString(), tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString(), merOrderExpire);

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("accountId", accountId);//用户账户编号
		resultMap.put("payTimes", merOrderExpire);//剩余支付秒数
		resultMap.put("payOrderId", tdOrder.getTdId().toString());//支付平台订单号
		resultMap.put("merOrderId", merOrderId);//商户平台订单号
		resultMap.put("merchantName", channelSp.getEntName());
		resultMap.put("goodsDesc", goodsDesc);
		resultMap.put("skDesc", workSp.getEntName());
		resultMap.put("workSpActId", workSpActId);
		resultMap.put("transBusiType", transBusiType);
		resultMap.put("payMoney", payMoney.toString());
		resultMap.put("createOrderTime", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyy-MM-dd HH:mm:ss"));

		return resultMap;
	}

	/**
	 * 支付-预支付
	 * @param inputMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> prePaymentOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> prePaymentOrder 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户编号
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//支付通行证
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户订单号
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付平台订单号
		String userIp = String.valueOf(inputMap.get("userIp")).trim();//用户终端IP
		String bindId = String.valueOf(inputMap.get("bindId")).trim();//绑卡ID
		String cardNo = String.valueOf(inputMap.get("cardNo")).trim();//借记卡/信用卡的卡号

		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		Map<String, Object> checkOrderMap = tdOrderServiceImpl.checkOrderIsValid(tdOrder, accountId);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
			return checkOrderMap;
		}
		if(!tdOrder.getFundId().startsWith(BaseDictConstant.PP_FUND_TYPE_ID_EBZF)){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
			return resultMap;
		}
		if(!merOrderId.equals(tdOrder.getOrigOrdCode())){
			resultMap.put("resultCode", Hint.PAY_ORDER_13005_MERORDERID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13005_MERORDERID_INVALID.getMessage());
			return resultMap;
		}
		Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
		if(null == redisPayToken || redisPayToken.size() == 0){
			resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
			resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
			return resultMap;
		}
		if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
			resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}
		PayUser payUser = (PayUser) checkPayUserMap.get("payUser");
		ActPerson actPerson = (ActPerson) checkPayUserMap.get("actPerson");
		String paymentType = String.valueOf(inputMap.get("paymentType")).trim();//支付类型 1：绑卡支付 2：首次或绑卡过期储蓄卡 3：首次或绑卡过期信用卡

		TdOrdExtPay checkTdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(checkTdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13092.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13092.getMessage());
			return resultMap;
		}
		if(checkTdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
			if(ValidateUtils.isEmpty(String.valueOf(checkTdOrdExtPay.getpSiCardNo()).trim())){
				resultMap.put("resultCode", Hint.PAY_ORDER_13091.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13091.getMessage());
				return resultMap;
			}
		}

		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(tdOrder.getFundId());
		if(null == ppFundPlatform || ppFundPlatform.getValidFlag() != DimDictEnum.VALID_FLAG_VALID.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("FundId=" + tdOrder.getFundId() + " 资金平台不存在或无效");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}
		PpFeeRatioA selectPpFeeRatioA = new PpFeeRatioA();
		selectPpFeeRatioA.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
		selectPpFeeRatioA.setBeginDate(DateUtils.getNowDate("yyyyMMdd"));
		selectPpFeeRatioA.setEndDate(DateUtils.getNowDate("yyyyMMdd"));
		List<PpFeeRatioA> ppFeeRatioAList = ppFeeRatioAServiceImpl.findPpFeeRatioAInfoList(selectPpFeeRatioA);
		if(null == ppFeeRatioAList || ppFeeRatioAList.size() == 0){
			if(logger.isInfoEnabled()){
				logger.info("FundId=" + tdOrder.getFundId() + " 资金平台费率配置信息不存在");
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}

		if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType) ||
		   BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD.equals(paymentType)) {
			ActPBank selectActPBank = new ActPBank();
			selectActPBank.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
			selectActPBank.setFromActTypeId(tdOrder.getFromActTypeId());//平台账户类型
			selectActPBank.setBankAccount(cardNo);
			selectActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
			if(logger.isInfoEnabled()){
				logger.info("检查私人银行账号信息是否存在");
			}
			List<ActPBank> actPBankList = actPBankServiceImpl.findActPBankList(selectActPBank);
			Map<String, String> bindCardAccountIdMap = new HashMap<String, String>();
			if(null != actPBankList && actPBankList.size() > 0){
				for(ActPBank actPBank : actPBankList){
					if("0".equals(String.valueOf(actPBank.getPayPropC()).trim())){
						continue;
					}
					if(!bindCardAccountIdMap.containsKey(actPBank.getActId().toString())){
						bindCardAccountIdMap.put(actPBank.getActId().toString(), actPBank.getActId().toString());
					}
				}
				if(bindCardAccountIdMap.size() >= BaseDictConstant.YEEPAY_MANY_PEOPLE_BIND_SAME_CARD_MAX){
					if(logger.isInfoEnabled()){
						logger.info("该银行卡(" + cardNo + ")已被超过" + bindCardAccountIdMap.size() + "人绑定过，请换卡支付");
						resultMap.put("resultCode", Hint.BANK_CARD_CHECK_13078.getCodeString());
						resultMap.put("resultDesc", Hint.BANK_CARD_CHECK_13078.getMessage().replace("{X}", String.valueOf(BaseDictConstant.YEEPAY_MANY_PEOPLE_BIND_SAME_CARD_MAX)));
						return resultMap;
					}
				}
			}
		}

		if(logger.isInfoEnabled()){
			logger.info("获取用户绑卡信息列表 accountId=" + accountId);
		}
		Map<String, String> bankCardListMap = yeepayPaymentServiceImpl.bankCardBindQuery(accountId, BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID);
		if(logger.isInfoEnabled()){
			logger.info("<<响应报文：" + bankCardListMap);
		}
		if(null == bankCardListMap || bankCardListMap.size() == 0 ||
		   bankCardListMap.containsKey("customError") || bankCardListMap.containsKey("error_code")){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		List<Map<String, Object>> bankCardList = JSON.parseObject(bankCardListMap.get("cardlist"), List.class);
		Map<String, Map<String, Object>> cardNoMap = new HashMap<String, Map<String, Object>>();
		if(null != bankCardList && bankCardList.size() > 0){
			String bindBankMaxConfig = String.valueOf(CacheUtils.getDimSysConfConfValue(BaseDictConstant.P_BIND_BANK_MAX)).trim();
			if(ValidateUtils.isEmpty(bindBankMaxConfig)){
				bindBankMaxConfig = BaseDictConstant.P_BIND_BANK_MAX_DEFAULT;
			}
			int pBindBankMax = bankCardList.size();
			boolean cardNumRepeat = false;
			String cardNoTemp = EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY);
			for(Map<String, Object> cardMap : bankCardList){
				cardNoMap.put(String.valueOf(cardMap.get("bindid")).trim(), cardMap);
				if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType) ||
				   BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD.equals(paymentType)) {
					if(cardNoTemp.substring(0, 6).equals(cardMap.get("card_top")) && cardNoTemp.substring(cardNoTemp.length() - 4).equals(cardMap.get("card_last"))){
						cardNumRepeat = true;
					}
				}else{
					if(bindId.equals(cardMap.get("bindid"))){
						cardNumRepeat = true;
					}
				}
			}
			if(!cardNumRepeat){//本次卡号与之前绑定卡号都不相同
				if(BaseDictConstant.PAYMENT_TYPE_BIND_CARD.equals(paymentType)) {
					resultMap.put("resultCode", Hint.PAY_ORDER_13040_INVALID_CARD_BIND_ID.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13040_INVALID_CARD_BIND_ID.getMessage());
					return resultMap;
				}
				pBindBankMax ++;
			}
			if(pBindBankMax > Integer.parseInt(bindBankMaxConfig)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13048_PERSONAL_BIND_BANK_CARD_MAX.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13048_PERSONAL_BIND_BANK_CARD_MAX.getMessage());
				return resultMap;
			}
		}

		this.updEbaoPaymentFlowIdInRedis(tdOrder);

		Map<String, String> yeepayResultMap;
		Map<String, String> reqDataMap = new HashMap<String, String>();
		Integer payOrderValidTime = this.getPayOrderSurplusValidTime(tdOrder.getCrtTime(), tdOrder.getTdLimitSec());
		payOrderValidTime = payOrderValidTime / 60;//支付订单有效期（单位：分钟）
		if(payOrderValidTime < BaseDictConstant.YEEPAY_MIN_ORDER_EXP_DATE){
			payOrderValidTime = BaseDictConstant.YEEPAY_MIN_ORDER_EXP_DATE;
		}
		if(logger.isInfoEnabled()){
			logger.info("请求yeepay预支付，设置订单有效期(分钟)：" + payOrderValidTime);
		}
		String idCardType = String.valueOf(inputMap.get("idCardType")).trim();//证件类型 01：身份证
		String idCard18 = String.valueOf(inputMap.get("idCard18")).trim();//证件号
		String owner = String.valueOf(inputMap.get("owner")).trim();//持卡人姓名
		String merchantProductCatalogCode = Propertie.APPLICATION.value("payOrder.merchantProductCatalogCode");//商户商品类别码
		if(BaseDictConstant.PAYMENT_TYPE_BIND_CARD.equals(paymentType)) {//绑卡支付
			reqDataMap.put("bindid", bindId);
			reqDataMap.put("orderid", payOrderId.concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));
			reqDataMap.put("productcatalog", merchantProductCatalogCode);
			reqDataMap.put("productname", tdOrder.getGoodsDesc());
			reqDataMap.put("identityid", accountId);
			reqDataMap.put("terminalid", hardwareId);
			reqDataMap.put("userip", userIp);
			reqDataMap.put("transtime", String.valueOf(System.currentTimeMillis() / 1000));
			reqDataMap.put("amount", checkTdOrdExtPay.getPaySelf().multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			reqDataMap.put("identitytype", BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID);
			reqDataMap.put("terminaltype", BaseDictConstant.YEEPAY_TERMINAL_TYPE_OTHER);
			reqDataMap.put("productdesc", tdOrder.getGoodsDesc());
			reqDataMap.put("currency", BaseDictConstant.YEEPAY_TRADE_CURRENCY_RMB);
			reqDataMap.put("orderexpdate", payOrderValidTime.toString());
			reqDataMap.put("other", "");
			if(logger.isInfoEnabled()){
				logger.info("绑卡支付，请求参数：" + reqDataMap);
			}
			yeepayResultMap = yeepayPaymentServiceImpl.bindBankCardRequest(reqDataMap);
			if(logger.isInfoEnabled()){
				logger.info("<<响应报文：" + yeepayResultMap);
			}
		}else if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType)) {//首次或绑卡过期储蓄卡
			String phone = String.valueOf(inputMap.get("phone")).trim();//手机号
			reqDataMap.put("phone", phone);
			reqDataMap.put("orderid", payOrderId.concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));
			reqDataMap.put("productcatalog", merchantProductCatalogCode);
			reqDataMap.put("productname", tdOrder.getGoodsDesc());
			reqDataMap.put("identityid", accountId);
			reqDataMap.put("userip", userIp);
			reqDataMap.put("terminalid", hardwareId);
			reqDataMap.put("idcardtype", idCardType);
			reqDataMap.put("idcard", idCard18);
			reqDataMap.put("owner", owner);
			reqDataMap.put("productdesc", tdOrder.getGoodsDesc());
			reqDataMap.put("terminaltype", BaseDictConstant.YEEPAY_TERMINAL_TYPE_OTHER);
			reqDataMap.put("transtime", String.valueOf(System.currentTimeMillis() / 1000));
			reqDataMap.put("amount", checkTdOrdExtPay.getPaySelf().multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			reqDataMap.put("identitytype", BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID);
			reqDataMap.put("currency", BaseDictConstant.YEEPAY_TRADE_CURRENCY_RMB);
			reqDataMap.put("orderexpdate", payOrderValidTime.toString());
			reqDataMap.put("other", "");
			if(logger.isInfoEnabled()){
				logger.info("储蓄卡支付，请求参数：" + reqDataMap);
			}
			reqDataMap.put("cardno", EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY));
			yeepayResultMap = yeepayPaymentServiceImpl.debitCardRequest(reqDataMap);
			if(logger.isInfoEnabled()){
				logger.info("<<响应报文：" + yeepayResultMap);
			}
		}else{//首次或绑卡过期信用卡
			String phone = String.valueOf(inputMap.get("phone")).trim();//手机号
			String validThru = String.valueOf(inputMap.get("validThru")).trim();//有效期
			String cvv2 = String.valueOf(inputMap.get("cvv2")).trim();//信用卡背后的 3 位数字
			reqDataMap.put("orderid", payOrderId.concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));
			reqDataMap.put("amount", checkTdOrdExtPay.getPaySelf().multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			reqDataMap.put("transtime", String.valueOf(System.currentTimeMillis() / 1000));
			reqDataMap.put("phone", phone);
			reqDataMap.put("idcardtype", idCardType);
			reqDataMap.put("idcard", idCard18);
			reqDataMap.put("owner", owner);
			reqDataMap.put("productcatalog", merchantProductCatalogCode);
			reqDataMap.put("productname", tdOrder.getGoodsDesc());
			reqDataMap.put("productdesc", tdOrder.getGoodsDesc());
			reqDataMap.put("identitytype", BaseDictConstant.YEEPAY_IDENTITY_TYPE_USER_ID);
			reqDataMap.put("identityid", accountId);
			reqDataMap.put("userip", userIp);
			reqDataMap.put("terminaltype", BaseDictConstant.YEEPAY_TERMINAL_TYPE_OTHER);
			reqDataMap.put("terminalid", hardwareId);
			reqDataMap.put("currency", BaseDictConstant.YEEPAY_TRADE_CURRENCY_RMB);
			reqDataMap.put("orderexpdate", payOrderValidTime.toString());
			reqDataMap.put("other", "");
			if(logger.isInfoEnabled()){
				logger.info("信用卡支付，请求参数：" + reqDataMap);
			}
			reqDataMap.put("cardno", EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY));
			reqDataMap.put("validthru", EncryptUtils.aesDecrypt(validThru, CipherAesEnum.CARDNOKEY));
			reqDataMap.put("cvv2", EncryptUtils.aesDecrypt(cvv2, CipherAesEnum.CARDNOKEY));
			yeepayResultMap = yeepayPaymentServiceImpl.creditCardRequest(reqDataMap);
			if(logger.isInfoEnabled()){
				logger.info("<<响应报文：" + yeepayResultMap);
			}
		}

		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		if(tdOrder.getTdOperChanTypeId().intValue() == DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCode() ||
		   tdOrder.getTdOperChanTypeId().intValue() == DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCode()){
			updTdOrder.setFromActId(Long.valueOf(accountId));
			updTdOrder.setPayUserId(payUser.getPayUserId());
		}
		updTdOrder.setUpdTime(new Date());
		updTdOrder.setTdOrder(tdOrder.getTdOrder());
		String interfaceErrorCode = this.getInterfaceErrorCode(yeepayResultMap);
		updTdOrder.setErrCode("预支付结果|".concat(interfaceErrorCode));
		updTdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode())));

		String bankcardtype = "";
		if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType)){
			bankcardtype = "1";
		}else if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD.equals(paymentType)){
			bankcardtype = "2";
		}else{
			if(cardNoMap.containsKey(bindId)){
				Map<String, Object> bankCardMap = cardNoMap.get(bindId);
				bankcardtype = String.valueOf(bankCardMap.get("cardtype")).trim();
			}
		}
		BigDecimal feeRatio;
		BigDecimal feeMin;
		if("1".equals(bankcardtype)){//储蓄卡
			feeRatio = ppFeeRatioAList.get(0).getDebitFeeRatio();
			feeMin = ppFeeRatioAList.get(0).getDebitFeeMin();
		}else {//信用卡
			feeRatio = ppFeeRatioAList.get(0).getCreditFeeRatio();
			feeMin = ppFeeRatioAList.get(0).getCreditFeeMin();
		}
		if(null == feeRatio || feeRatio.doubleValue() < 0){
			feeRatio = BigDecimal.ZERO;
		}
		updTdOrder.setServChrgRatio(feeRatio);//本次交易费率
		if(null == feeMin || feeMin.doubleValue() < 0){
			feeMin = BigDecimal.ZERO;
		}
		BigDecimal servChrgAmt = checkTdOrdExtPay.getPaySelf().multiply(updTdOrder.getServChrgRatio()).setScale(2, BigDecimal.ROUND_HALF_UP);
		if(logger.isInfoEnabled()){
			logger.info("本次交易收取手续费：" + servChrgAmt + ",费用下限：feeMin=" + feeMin);
		}
		if(feeMin.doubleValue() > 0 && servChrgAmt.compareTo(feeMin) < 0){
			servChrgAmt = feeMin;
		}
		updTdOrder.setServChrgAmt(servChrgAmt);//本次交易手续费

		this.updTdOrderInfo(updTdOrder);

		TdOrdExtPay tdOrdExtPay;
		if(!Hint.SYS_SUCCESS.getCodeString().equals(interfaceErrorCode)){
			tdOrdExtPay = new TdOrdExtPay();
			tdOrdExtPay.setTdId(tdOrder.getTdId());
			tdOrdExtPay.setTdBusiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCodeString()));
			tdOrdExtPay.setUpdTime(new Date());
			tdOrderServiceImpl.updateTdOrdExtPayInfo(tdOrdExtPay);
		}

		if(null == yeepayResultMap || yeepayResultMap.size() == 0 || yeepayResultMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		if(yeepayResultMap.containsKey("error_code")){
			String error_code = String.valueOf(yeepayResultMap.get("error_code")).trim();
			//请求频次过高，请过几秒重试
			if("601101".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13049_601101.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13049_601101.getMessage());
				return resultMap;
			}
			//CVV2格式有误，请输入信用卡背面的3位数字
			if("600023".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13058_600023.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13058_600023.getMessage());
				return resultMap;
			}
			//有效期格式有误，请以月年的形式填写
			if("600024".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13059_600024.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13059_600024.getMessage());
				return resultMap;
			}
			//请确认身份证号是否正确
			if("600119".equals(error_code) || "600021".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID.getMessage());
				return resultMap;
			}
			//订单金额过低受限
			if("600113".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13050_600113.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13050_600113.getMessage());
				return resultMap;
			}
			//单卡超过当日累积支付限额
			if("600043".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13051_600043.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13051_600043.getMessage());
				return resultMap;
			}
			//单卡超过单笔支付限额
			if("600045".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13052_600045.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13052_600045.getMessage());
				return resultMap;
			}
			//单卡超过单月累积支付限额
			if("600046".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13053_600046.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13053_600046.getMessage());
				return resultMap;
			}
			//单卡超过单日累积支付次数上限
			if("600047".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13054_600047.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13054_600047.getMessage());
				return resultMap;
			}
			//单卡超过单月累积支付次数上限
			if("600048".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13055_600048.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13055_600048.getMessage());
				return resultMap;
			}
			//消费金额超限，请联系发卡银行
			if("600098".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13056_600098.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13056_600098.getMessage());
				return resultMap;
			}
			//商户状态冻结不能进行交易
			if("611110".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13057_611110.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13057_611110.getMessage());
				return resultMap;
			}
			//无效的绑卡ID
			if("600076".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13040_INVALID_CARD_BIND_ID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13040_INVALID_CARD_BIND_ID.getMessage());
				return resultMap;
			}
			//卡信息或银行预留手机号有误
			if("600095".equals(error_code) || "600120".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID.getMessage());
				return resultMap;
			}
			//无效或不支持的卡号，请换卡支付
			if("600051".equals(error_code) || "600053".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED.getMessage());
				return resultMap;
			}
			//卡已过期，请换卡重新支付
			if("600118".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13043_BANK_CARD_OVERDUE.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13043_BANK_CARD_OVERDUE.getMessage());
				return resultMap;
			}
			//该卡为储蓄卡，请用信用卡支付
			if("600126".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13044_MUST_USE_CREDIT_CARD.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13044_MUST_USE_CREDIT_CARD.getMessage());
				return resultMap;
			}
			//该卡为信用卡，请用储蓄卡支付
			if("600127".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13045_MUST_USE_DEBIT_CARD.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13045_MUST_USE_DEBIT_CARD.getMessage());
				return resultMap;
			}
			//本卡被发卡方没收，请联系发卡银行
			if("600094".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13046_CARD_BE_CONFISCATED.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13046_CARD_BE_CONFISCATED.getMessage());
				return resultMap;
			}
			//本卡未激活或睡眠卡，请联系发卡银行
			if("600099".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP.getMessage());
				return resultMap;
			}
			//订单重复提交
			if(BaseDictConstant.YEEPAY_ORDER_REPEAT_SUBMIT.equals(error_code) ||
			   BaseDictConstant.YEEPAY_ORDER_ALREADY_PAYMENT_SUCCESS.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13008_ORDER_REPEAT_SUBMIT.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13008_ORDER_REPEAT_SUBMIT.getMessage());
				return resultMap;
			}
			//订单已成功
			if(BaseDictConstant.YEEPAY_ORDER_ALREADY_TRADE_SUCCESS.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getMessage());
				return resultMap;
			}
			//订单已过期或已撤销
			if(BaseDictConstant.YEEPAY_ORDER_TIMEOUT_OR_CANCEL.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
				return resultMap;
			}
			//订单金额过低受限
			if(BaseDictConstant.YEEPAY_ORDER_AMOUNT_TOO_LOW.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getMessage());
				return resultMap;
			}
			//可用余额不足
			if(BaseDictConstant.YEEPAY_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
		String phone = String.valueOf(yeepayResultMap.get("phone")).trim();
		if(ValidateUtils.isEmpty(phone)){
			phone = "";
		}
		String smsConfirm = String.valueOf(yeepayResultMap.get("smsconfirm")).trim();//0：建议不需要进行短信校验 1：建议需要进行短信校验
		if(ValidateUtils.isEmpty(smsConfirm)){
			smsConfirm = "";
		}
		if(BaseDictConstant.YEEPAY_SUGGEST_NEED_SMS_VERIFICATION.equals(smsConfirm)){
			String redisKey = "yeepayNeedSmsVerify_".concat(payOrderId);
			if(!redisService.exists(redisKey)){
				int expire = tdOrder.getTdLimitSec();
				Map<String, String> redisInputMap = new HashMap<String, String>();
				redisInputMap.put("sendCount", "0");
				redisInputMap.put("sendTime", String.valueOf(System.currentTimeMillis()));
				if(logger.isInfoEnabled()){
					logger.info("订单确认支付时需要短信验证码校验 redisKey=" + redisKey + ",redisValue=" + redisInputMap + ",expire=" + expire);
				}
				redisService.setMap(redisKey, redisInputMap);
				redisService.expire(redisKey, expire);
			}
		}

		String cardType = ""; //1：储蓄卡 2：信用卡 -1：未知银行
		String cardTypeName = ""; //银行卡类型名称
		String bankName = "";
		String bankCode = "";
		String payActTypeId = "";
		String bankAccount = "";
		String actName = "";
		String responseCardNo = "";
		if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType) ||
		   BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_CREDIT_CARD.equals(paymentType)) {
			bankAccount = cardNo;
			responseCardNo = EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY);
			actName = owner;
			if(BaseDictConstant.PAYMENT_TYPE_FIRST_OR_BIND_EXPIRE_DEBIT_CARD.equals(paymentType)){
				payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_DEBIT_CARD.getCode());
			}else{
				payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_CREDIT_CARD.getCode());
			}

			/*********************调用接口判断银行卡类型 begin***********************/
			if(logger.isInfoEnabled()){
				logger.info("请求易宝接口查询银行卡类型，请求业务参数：" + cardNo);
			}
			yeepayResultMap = yeepayPaymentServiceImpl.bankCardCheck(EncryptUtils.aesDecrypt(cardNo, CipherAesEnum.CARDNOKEY));
			if(null != yeepayResultMap){
				yeepayResultMap.remove("cardno");
			}
			if(logger.isInfoEnabled()){
				logger.info("接口响应报文：" + yeepayResultMap);
			}
			if(null == yeepayResultMap || yeepayResultMap.size() == 0 ||
			   yeepayResultMap.containsKey("error_code") || yeepayResultMap.containsKey("customError") ||
			   "-1".equals(String.valueOf(yeepayResultMap.get("cardtype")).trim())){
				if(logger.isInfoEnabled()){
					logger.info("<<查询失败");
				}
			}else{
				bankName = yeepayResultMap.get("bankname");
				if(ValidateUtils.isEmpty(bankName)){
					bankName = "";
				}
				bankCode = yeepayResultMap.get("bankcode");
				if(ValidateUtils.isEmpty(bankCode)){
					bankCode = "";
				}
				cardType = String.valueOf(yeepayResultMap.get("cardtype")).trim();
				if(ValidateUtils.isEmpty(cardType)){
					cardType = "-1";
				}
				if(String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_DEBIT_CARD_INT).equals(cardType)){
					cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_DEBIT_CARD_INT;
				}else if(String.valueOf(BaseDictConstant.PAYMENT_TYPE_FOB_EXPIRE_CREDIT_CARD_INT).equals(cardType)){
					cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_CREDIT_CARD_INT;
				}else{
					cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_UNKNOWN;
				}
				PpFundBankRel ppFundBankRel = new PpFundBankRel();
				ppFundBankRel.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
				ppFundBankRel.setPpBankCode(bankCode);
				ppFundBankRel.setPayActTypeId(Short.valueOf(payActTypeId));
				ppFundBankRel.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
				List<PpFundBankRel> ppFundBankRelList = ppFundBankRelServiceImpl.findPpFundBankRelList(ppFundBankRel);
				if(null == ppFundBankRelList || ppFundBankRelList.size() == 0){
					if(logger.isInfoEnabled()){
						logger.info("资金平台支持银行卡类型不存在或无效");
					}
				}else{
					Map<String, String> otherParamsNeedMap = new HashMap<>();
					otherParamsNeedMap.put("idcard", idCard18);
					otherParamsNeedMap.put("owner", owner);
					ActPBank actPBank = new ActPBank();
					actPBank.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);//资金平台ID
					actPBank.setFromActTypeId(tdOrder.getFromActTypeId());//平台账户类型
					actPBank.setActId(Long.valueOf(accountId));//账户ID
					actPBank.setPayActTypeId(Short.valueOf(payActTypeId));//支付账号类别
					actPBank.setBankId(ppFundBankRelList.get(0).getBankId());//银行ID
					actPBank.setBankAccount(bankAccount);//银行账号
					actPBank.setActName(owner);//户名
					actPBank.setAuthMobile(phone);//验证手机号
					List<ActPBank> actPBankList = actPBankServiceImpl.findActPBankList(actPBank);
					if(null == actPBankList || actPBankList.size() == 0){
						actPBank.setpIdNo(idCard18.toUpperCase());
						actPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_INVALID.getCode())));
						actPBank.setBankActId(Long.valueOf(IdCreatorUtils.getPersonBankAccountId()));//银行账号ID
						actPBank.setBankName(bankName);//开户银行
						actPBank.setPayPropA(tdOrder.getTdId().toString());//借用字段：支付订单号
						actPBank.setPayPropB(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());//借用字段：交易序号
						actPBank.setRemark(EncryptUtils.aesEncrypt(JSON.toJSONString(otherParamsNeedMap), CipherAesEnum.CARDNOKEY));
						if(logger.isInfoEnabled()){
							logger.info("私人银行账号信息不存在，开始保存");
						}
						actPBankServiceImpl.saveActPBank(actPBank);
						if(logger.isInfoEnabled()){
							logger.info("<<保存成功");
						}
					}else{
						actPBank = new ActPBank();
						actPBank.setBankActId(actPBankList.get(0).getBankActId());
						actPBank.setpIdNo(idCard18.toUpperCase());
						actPBank.setUpdTime(new Date());
						if(actPBankList.get(0).getValidFlag().intValue() == DimDictEnum.VALID_FLAG_VALID.getCode()){
							if(logger.isInfoEnabled()){
								logger.info("私人银行账号信息已存在且状态为有效，更新操作时间");
							}
						}else{
							actPBank.setPayPropA(tdOrder.getTdId().toString());//借用字段：支付订单号
							actPBank.setPayPropB(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());//借用字段：交易序号
							actPBank.setRemark(EncryptUtils.aesEncrypt(JSON.toJSONString(otherParamsNeedMap), CipherAesEnum.CARDNOKEY));
							if(logger.isInfoEnabled()){
								logger.info("私人银行账号信息已存在且状态为无效，开始更新 原交易单号：" + actPBankList.get(0).getPayPropA() +
										" 原交易序号：" + actPBankList.get(0).getPayPropB() + " 现交易单号：" + actPBank.getPayPropA() +
										" 现交易序号：" + actPBank.getPayPropB());
							}
						}
						actPBankServiceImpl.updateActPBank(actPBank);
						if(logger.isInfoEnabled()){
							logger.info("<<更新成功");
						}
					}
				}
			}
			/*********************调用接口判断银行卡类型 end***********************/
		}else {
			if(cardNoMap.containsKey(bindId)){
				Map<String, Object> bankCardMap = cardNoMap.get(bindId);
				responseCardNo = String.valueOf(bankCardMap.get("card_last")).trim();
				cardType = String.valueOf(bankCardMap.get("cardtype")).trim();
				if("1".equals(cardType)){
					cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_DEBIT_CARD_INT;
					payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_DEBIT_CARD.getCode());
				}else{
					cardTypeName = BaseDictConstant.PAYMENT_TYPE_CARD_NAME_FOB_EXPIRE_CREDIT_CARD_INT;
					payActTypeId = String.valueOf(DimDictEnum.PAY_ACT_TYPE_CREDIT_CARD.getCode());
				}
				bankName = String.valueOf(bankCardMap.get("card_name")).trim();
				if(bankName.endsWith("卡")){
					bankName = bankName.substring(0, bankName.length() - 3);
				}
				bankCode = String.valueOf(bankCardMap.get("bankcode")).trim();
			}
			ActPBank selectActPBank = new ActPBank();
			selectActPBank.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
			selectActPBank.setFromActTypeId(tdOrder.getFromActTypeId());
			selectActPBank.setActId(Long.valueOf(accountId));
			selectActPBank.setPayActTypeId(Short.valueOf(payActTypeId));
			selectActPBank.setPayPropC(bindId);//借用字段：绑卡ID
			selectActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
			List<ActPBank> actPBankList = actPBankServiceImpl.findActPBankList(selectActPBank);
			if(null != actPBankList && actPBankList.size() > 0){
				bankAccount = actPBankList.get(0).getBankAccount();
				actName = actPBankList.get(0).getActName();
				ActPBank updActPBank = new ActPBank();
				updActPBank.setBankActId(actPBankList.get(0).getBankActId());
				updActPBank.setUpdTime(new Date());
				actPBankServiceImpl.updateActPBank(updActPBank);
			}

			String otherParamsNeed = String.valueOf(yeepayResultMap.get("otherparamsneed")).trim();
			if(logger.isInfoEnabled()){
				logger.info("当前订单(" + payOrderId + ")绑卡预支付成功 bindid=" + bindId + ",otherparamsneed=" + otherParamsNeed);
			}
			if(ValidateUtils.isNotEmpty(otherParamsNeed)){
				String redisKey = "bindCardParamsNeed_".concat(payOrderId);
				int expire = Integer.parseInt(Propertie.APPLICATION.value("payOrder.expire"));
				Map<String, String> redisValueMap = new HashMap<>();
				redisValueMap.put("otherParamsNeed", otherParamsNeed);
				redisValueMap.put("bindId", bindId);
				redisService.set(redisKey, JSON.toJSONString(redisValueMap));
				redisService.expire(redisKey, expire);
			}
		}

		String bankId = "";
		DimBank dimBank = bankImageServiceImpl.getDimBankInfo(BaseDictConstant.PP_FUND_TYPE_ID_EBZF, bankCode, payActTypeId);
		if(null != dimBank){
			bankId = dimBank.getBankId().toString();
		}

		tdOrdExtPay = new TdOrdExtPay();
		tdOrdExtPay.setTdId(tdOrder.getTdId());//交易单ID
		tdOrdExtPay.setTdBusiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCodeString()));
		tdOrdExtPay.setpIdNo(actPerson.getpCertNo().toUpperCase());
		if(ValidateUtils.isNotEmpty(payActTypeId)){
			tdOrdExtPay.setPayActTypeId(Short.valueOf(payActTypeId));
		}
		if(ValidateUtils.isNotEmpty(bankId)){
			tdOrdExtPay.setBankId(Long.valueOf(bankId));
		}
		if(ValidateUtils.isNotEmpty(bankAccount)){
			tdOrdExtPay.setBankAccount(bankAccount);
		}
		if(ValidateUtils.isNotEmpty(actName)){
			tdOrdExtPay.setActName(actName);
		}
		tdOrdExtPay.setUpdTime(new Date());
		if(logger.isInfoEnabled()){
			logger.info("开始更新支付扩展信息");
		}
		tdOrderServiceImpl.updateTdOrdExtPayInfo(tdOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("merOrderId", merOrderId);
		resultMap.put("payOrderId", payOrderId);
		if(!phone.contains("****")){
			String before = phone.substring(0, 3);
			String after = phone.substring(phone.length() - 4);
			phone = before.concat("****").concat(after);
		}
		resultMap.put("phone", phone);
		if(responseCardNo.length() > 4){
			responseCardNo = responseCardNo.substring(responseCardNo.length() - 4);
		}
		resultMap.put("cardNo", responseCardNo);
		resultMap.put("cardType", cardType);
		resultMap.put("cardTypeName", cardTypeName);
		resultMap.put("bankName", bankName);
		resultMap.put("bankCode", bankCode);
		String bankIconUrl;
		if(null == dimBank){
			bankIconUrl = bankImageServiceImpl.getBankImageUrl(BaseDictConstant.PP_FUND_TYPE_ID_EBZF, bankCode, payActTypeId);
		}else {
			bankIconUrl = bankImageServiceImpl.getBankImageUrl(dimBank.getBankImageId());
		}
		resultMap.put("bankIconUrl", bankIconUrl);
		resultMap.put("smsConfirm", smsConfirm);
		return resultMap;
	}

	/**
	 * 支付-预支付验证
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> prePaymentVerify(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> prePaymentVerify 输入参数：" + inputMap);
		}
		Map<String, Object> prePaymentOrderMap = this.prePaymentOrder(inputMap);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(prePaymentOrderMap.get("resultCode")))){
			return prePaymentOrderMap;
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("payOrderId", inputMap.get("payOrderId"));
		dataMap.put("accountId", inputMap.get("accountId"));
		dataMap.put("payToken", inputMap.get("payToken"));
		dataMap.put("hardwareId", inputMap.get("hardwareId"));
		Map<String, Object> sendPayVerifyCodeMap = this.sendPayVerifyCode(dataMap);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(sendPayVerifyCodeMap.get("resultCode")))){
			return sendPayVerifyCodeMap;
		}
		prePaymentOrderMap.remove("smsConfirm");//已下发短信验证码，无需此字段返回
		return prePaymentOrderMap;
	}

	/**
	 * 获取订单剩余有效时间
	 * @param payOrderCreateTime 订单创建时间
	 * @param validLimitTime 订单有效期限（单位：秒）
	 * @return 订单剩余有效期（单位：秒）
	 * @throws Exception
     */
	private Integer getPayOrderSurplusValidTime(Date payOrderCreateTime, Integer validLimitTime) throws Exception {
		Date nowDate = new Date();
		if(logger.isInfoEnabled()){
			logger.info("当前系统时间：" + DateUtils.dateToString(nowDate, "yyyy-MM-dd HH:mm:ss") +
					" 交易单创建时间：" + DateUtils.dateToString(payOrderCreateTime, "yyyy-MM-dd HH:mm:ss") +
					" 订单有效期限(秒)：" + validLimitTime);
		}
		long payOrderSurplusValidTime = nowDate.getTime() - payOrderCreateTime.getTime();
		payOrderSurplusValidTime = (validLimitTime * 1000 - payOrderSurplusValidTime) / 1000;//毫秒转换为秒
		if(payOrderSurplusValidTime < 0){
			payOrderSurplusValidTime = 0;
		}
		if(logger.isInfoEnabled()){
			logger.info("交易单剩余有效期时间(秒)：" + payOrderSurplusValidTime);
		}
		return Integer.parseInt(String.valueOf(payOrderSurplusValidTime));
	}

	/**
	 * 判断交易单是否有效，并对成功和失败的订单不做处理
	 * @param tdOrder
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> checkOrderIsValid2(TdOrder tdOrder, String accountId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null == tdOrder){
			resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
			return resultMap;
		}
		if(tdOrder.getTdOperChanTypeId().intValue() != DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCode() &&
		   tdOrder.getTdOperChanTypeId().intValue() != DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCode() &&
		   tdOrder.getFromActId().longValue() != Long.valueOf(accountId).longValue()){
			if(logger.isInfoEnabled()){
				logger.info("下单账户fromActId=" + tdOrder.getFromActId() + ",本次请求账户accountId=" + accountId);
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
			return resultMap;
		}
		String payOrderExpire = tdOrder.getTdLimitSec().toString();
		Date nowDate = new Date();
		if(logger.isInfoEnabled()){
			logger.info("交易单创建时间：" + DateUtils.dateToString(tdOrder.getCrtTime(), "yyyy-MM-dd HH:mm:ss") +
					" 当前时间：" + DateUtils.dateToString(nowDate, "yyyy-MM-dd HH:mm:ss") + " 订单有效期：" + payOrderExpire);
		}
		if(nowDate.getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(payOrderExpire) * 1000)){
			resultMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
			resultMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
			return resultMap;
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
			//订单成功或者失败，直接返回成功
			if(logger.isInfoEnabled()){
				logger.info("订单状态："+tdOrder.getTdStatId()+" 其中【初始化100】【进行中101】【成功110】【失败120】【异常130】，订单状态不处理，直接返回成功");
			}
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			return resultMap;
		}
		resultMap.put("resultCode", Hint.PAY_ORDER_13075_ORDER_NOT_TOP.getCodeString());
		resultMap.put("resultDesc", Hint.PAY_ORDER_13075_ORDER_NOT_TOP.getMessage());
		return resultMap;
	}

	/**
	 * 判断交易单扩展信息是否有效，并对成功和失败的订单不做处理
	 * @param tdOrdExtPay
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> checkOrderExtIsValid2(TdOrdExtPay tdOrdExtPay, String accountId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null == tdOrdExtPay){//支付订单扩展信息不存在或已失效
			resultMap.put("resultCode", Hint.PAY_ORDER_13019_ORDERINFO_EXT_NOT_EXIST_OR_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13019_ORDERINFO_EXT_NOT_EXIST_OR_INVALID.getMessage());
			return resultMap;
		}
		if(tdOrdExtPay.getPayTypeId() != null){
			if(tdOrdExtPay.getPayTypeId().intValue() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode() ){//商业支付
				if(tdOrdExtPay.getTdBusiStatId() != null){
					if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
						//订单成功或者失败，直接返回成功
						if(logger.isInfoEnabled()){
							logger.info("订单扩展状态："+tdOrdExtPay.getTdBusiStatId()+" 其中【初始化100】【进行中101】【成功110】【失败120】【异常130】，订单状态不处理，直接返回成功");
						}
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
						return resultMap;
					}
				}
			}else if(tdOrdExtPay.getPayTypeId().intValue() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode() ){//社保支付
				if(tdOrdExtPay.getTdSiStatId() != null){
					if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
						//订单成功或者失败，直接返回成功
						if(logger.isInfoEnabled()){
							logger.info("订单扩展状态："+tdOrdExtPay.getTdSiStatId()+" 其中【初始化100】【进行中101】【成功110】【失败120】【异常130】，订单状态不处理，直接返回成功");
						}
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
						return resultMap;
					}
				}
			}else{//混合支付
				if(tdOrdExtPay.getTdSiStatId() != null && tdOrdExtPay.getTdBusiStatId() != null){
					if((tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()) && 
							(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() || tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode())){
						//订单成功或者失败，直接返回成功
						if(logger.isInfoEnabled()){
							logger.info("订单扩展状态："+tdOrdExtPay.getTdSiStatId()+" 其中【初始化100】【进行中101】【成功110】【失败120】【异常130】，订单状态不处理，直接返回成功");
						}
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
						return resultMap;
					}
				}
			}
		}
		resultMap.put("resultCode", Hint.PAY_ORDER_13075_ORDER_NOT_TOP.getCodeString());
		resultMap.put("resultDesc", Hint.PAY_ORDER_13075_ORDER_NOT_TOP.getMessage());
		return resultMap;
	}
	
	/**
	 * 更新交易单信息
	 * @param tdOrder
	 * @throws Exception
     */
	private void updTdOrderInfo(TdOrder tdOrder) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易单信息");
		}
		tdOrderServiceImpl.updateTdOrderInfo(tdOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}
	}

	/**
	 * 获取接口返回错误码
	 * @param resultMap
	 * @return
	 * @throws Exception
     */
	private String getInterfaceErrorCode(Map<String, String> resultMap) throws Exception {
		String interfaceErrorCode;
		if(null == resultMap || resultMap.size() == 0 || resultMap.containsKey("customError")){
			interfaceErrorCode = Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString();
		}else if(resultMap.containsKey("error_code")) {
			interfaceErrorCode = String.valueOf(resultMap.get("error_code")).trim();
		}else {
			interfaceErrorCode = Hint.SYS_SUCCESS.getCodeString();
		}
		return interfaceErrorCode;
	}

	/**
	 * 确认支付前下发短信验证码
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> sendPayVerifyCode(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> sendPayVerifyCode 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<>();
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付平台订单号
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//支付通行证
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID

		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		Map<String, Object> checkOrderMap = tdOrderServiceImpl.checkOrderIsValid(tdOrder, accountId);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
			return checkOrderMap;
		}
		if(!tdOrder.getFundId().startsWith(BaseDictConstant.PP_FUND_TYPE_ID_EBZF)){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
			return resultMap;
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getMessage());
			return resultMap;
		}
		Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
		if(null == redisPayToken || redisPayToken.size() == 0){
			resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
			resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
			return resultMap;
		}
		if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
			resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		Map<String, Object> smsCodeVerifyMap = this.isNeedSmsCodeVerify(payOrderId, true);
		if("1".equals(String.valueOf(smsCodeVerifyMap.get("resultCode")))){
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", "该订单无需进行短信验证");
			return resultMap;
		}
		if(Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getCodeString().equals(String.valueOf(smsCodeVerifyMap.get("resultCode"))) ||
		   Hint.YEEPAY_13007_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH.getCodeString().equals(String.valueOf(smsCodeVerifyMap.get("resultCode")))){
			return smsCodeVerifyMap;
		}
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}

		TdOrdExtPay checkTdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(checkTdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13092.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13092.getMessage());
			return resultMap;
		}

		/**********************调用易宝发送短信验证码 begin**************************/
		String ebaoPaymentFlowId = payOrderId.concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		if(logger.isInfoEnabled()){
			logger.info("易宝支付发送短信验证码，请求参数：ebaoPaymentFlowId=" + ebaoPaymentFlowId);
		}
		Map<String, String> yeepayResultMap = yeepayPaymentServiceImpl.sendShortMessage(ebaoPaymentFlowId);
		if(logger.isInfoEnabled()){
			logger.info("<<响应报文：" + yeepayResultMap);
		}
		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setUpdTime(new Date());
		updTdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode())));
		updTdOrder.setErrCode("下发短信验证码结果|".concat(this.getInterfaceErrorCode(yeepayResultMap)));
		this.updTdOrderInfo(updTdOrder);
		if(null == yeepayResultMap || yeepayResultMap.size() == 0 || yeepayResultMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		if(yeepayResultMap.containsKey("error_code")){
			String error_code = String.valueOf(yeepayResultMap.get("error_code")).trim();
			//请求频次过高，请过几秒重试
			if("601101".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13049_601101.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13049_601101.getMessage());
				return resultMap;
			}
			//银行预留手机号有误
			if("600095".equals(error_code) || "600120".equals(error_code) || "601037".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID.getMessage());
				return resultMap;
			}
			//输入姓名有误
			if("601036".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13071_INPUT_USERNAME_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13071_INPUT_USERNAME_INVALID.getMessage());
				return resultMap;
			}
			//请确认身份证号是否正确
			if("600119".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID.getMessage());
				return resultMap;
			}
			//无效或不支持的卡号，请换卡支付
			if("600051".equals(error_code) || "600053".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED.getMessage());
				return resultMap;
			}
			//卡已过期，请换卡重新支付
			if("600118".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13043_BANK_CARD_OVERDUE.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13043_BANK_CARD_OVERDUE.getMessage());
				return resultMap;
			}
			//该卡为储蓄卡，请用信用卡支付
			if("600126".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13044_MUST_USE_CREDIT_CARD.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13044_MUST_USE_CREDIT_CARD.getMessage());
				return resultMap;
			}
			//该卡为信用卡，请用储蓄卡支付
			if("600127".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13045_MUST_USE_DEBIT_CARD.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13045_MUST_USE_DEBIT_CARD.getMessage());
				return resultMap;
			}
			if(BaseDictConstant.YEEPAY_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getMessage());
				return resultMap;
			}
			if(BaseDictConstant.YEEPAY_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13007_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13007_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH.getMessage());
				return resultMap;
			}
			//订单金额过低受限
			if("600113".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13050_600113.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13050_600113.getMessage());
				return resultMap;
			}
			//单卡超过当日累积支付限额
			if("600043".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13051_600043.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13051_600043.getMessage());
				return resultMap;
			}
			//单卡超过单笔支付限额
			if("600045".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13052_600045.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13052_600045.getMessage());
				return resultMap;
			}
			//单卡超过单月累积支付限额
			if("600046".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13053_600046.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13053_600046.getMessage());
				return resultMap;
			}
			//单卡超过单日累积支付次数上限
			if("600047".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13054_600047.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13054_600047.getMessage());
				return resultMap;
			}
			//单卡超过单月累积支付次数上限
			if("600048".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13055_600048.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13055_600048.getMessage());
				return resultMap;
			}
			//消费金额超限，请联系发卡银行
			if("600098".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13056_600098.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13056_600098.getMessage());
				return resultMap;
			}
			//商户状态冻结不能进行交易
			if("611110".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13057_611110.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13057_611110.getMessage());
				return resultMap;
			}
			//本卡被发卡方没收，请联系发卡银行
			if("600094".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13046_CARD_BE_CONFISCATED.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13046_CARD_BE_CONFISCATED.getMessage());
				return resultMap;
			}
			//本卡未激活或睡眠卡，请联系发卡银行
			if("600099".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP.getMessage());
				return resultMap;
			}
			//订单重复提交
			if(BaseDictConstant.YEEPAY_ORDER_REPEAT_SUBMIT.equals(error_code) ||
			   BaseDictConstant.YEEPAY_ORDER_ALREADY_PAYMENT_SUCCESS.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13008_ORDER_REPEAT_SUBMIT.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13008_ORDER_REPEAT_SUBMIT.getMessage());
				return resultMap;
			}
			//订单已成功
			if(BaseDictConstant.YEEPAY_ORDER_ALREADY_TRADE_SUCCESS.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getMessage());
				return resultMap;
			}
			//订单已过期或已撤销
			if(BaseDictConstant.YEEPAY_ORDER_TIMEOUT_OR_CANCEL.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
				return resultMap;
			}
			//订单金额过低受限
			if(BaseDictConstant.YEEPAY_ORDER_AMOUNT_TOO_LOW.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getMessage());
				return resultMap;
			}
			//可用余额不足
			if(BaseDictConstant.YEEPAY_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
		/**********************调用易宝发送短信验证码 end**************************/

		this.setYeepaySmsVerifyCodeCountInRedis(payOrderId);

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	/**
	 * 设置确认支付发送短信验证码次数
	 * @param payOrderId
	 * @throws Exception
     */
	private void setYeepaySmsVerifyCodeCountInRedis(String payOrderId) throws Exception {
		String redisKey = "yeepayNeedSmsVerify_".concat(payOrderId);
		Map<String, String> redisResultMap = redisService.getMap(redisKey);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单(" + payOrderId + ")确认支付时发送短信验证码次数 redisKey=" + redisKey + ",redisValue=" + redisResultMap);
		}
		String sendCount;
		if(null == redisResultMap || redisResultMap.size() == 0){
			sendCount = "1";
		}else{
			sendCount = String.valueOf(Integer.parseInt(redisResultMap.get("sendCount")) + 1);
		}
		Map<String, String> redisInputMap = new HashMap<String, String>();
		redisInputMap.put("sendCount", sendCount);
		redisInputMap.put("sendTime", String.valueOf(System.currentTimeMillis()));
		redisService.setMap(redisKey, redisInputMap);
		if(logger.isInfoEnabled()){
			logger.info("短信验证码发送成功，更新发送次数 redisKey=" + redisKey + ",redisValue=" + redisInputMap);
		}
	}

	/**
	 * 订单确认支付时是否需要短信验证码校验
	 * @param payOrderId 支付订单号
	 * @param isSendPayCode 是否发送验证码功能 true-是 false-否
	 * @return
	 * @throws Exception
     */
	private Map<String, Object> isNeedSmsCodeVerify(String payOrderId, boolean isSendPayCode) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String redisKey = "yeepayNeedSmsVerify_".concat(payOrderId);
		Map<String, String> redisResultMap = redisService.getMap(redisKey);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单(" + payOrderId + ")确认支付时是否需要短信验证码校验 redisKey=" + redisKey + ",redisValue=" + redisResultMap);
		}
		if(null == redisResultMap || redisResultMap.size() == 0){
			if(logger.isInfoEnabled()){
				logger.info("<<不需要校验");
			}
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "不需要短信验证码");
			return resultMap;
		}
		if(logger.isInfoEnabled()){
			logger.info("<<需要校验");
		}
		String sendCount = redisResultMap.get("sendCount");
		String smsPayMaxPerOrd = CacheUtils.getDimSysConfConfValue(BaseDictConstant.SMS_PAY_MAX_PER_ORD);
		if(Integer.parseInt(sendCount) > Integer.parseInt(smsPayMaxPerOrd)){
			resultMap.put("resultCode", Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getCodeString());
			resultMap.put("resultDesc", Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getMessage());
			return resultMap;
		}
		if(isSendPayCode){
			if(Integer.parseInt(sendCount) == Integer.parseInt(smsPayMaxPerOrd)){
				resultMap.put("resultCode", Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getMessage());
				return resultMap;
			}
			String sendTime = redisResultMap.get("sendTime");
			String smsPayBetweSec = CacheUtils.getDimSysConfConfValue(BaseDictConstant.SMS_PAY_BETWE_SEC);
			long currentTime = System.currentTimeMillis();
			long timeConsuming = currentTime - Long.parseLong(sendTime);
			if(logger.isInfoEnabled()){
				logger.info("currentTime=" + currentTime + ",sendTime=" + sendTime + ",timeConsuming=" + timeConsuming + ",smsPayBetweSec=" + smsPayBetweSec);
			}
			if(Integer.parseInt(sendCount) > 0 && timeConsuming < (Long.parseLong(smsPayBetweSec) * 1000)){
				resultMap.put("resultCode", Hint.YEEPAY_13007_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13007_SEND_VALIDATE_CODE_FREQUENCY_TOO_HIGH.getMessage());
				return resultMap;
			}
		}
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	/**
	 * 确认支付
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> confirmPaymentOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> confirmPaymentOrder 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付平台订单号
		String validateCode = String.valueOf(inputMap.get("validateCode")).trim();//短信校验码
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//支付通行证
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID
		String validThru = String.valueOf(inputMap.get("validThru")).trim();//有效期
		String cvv2 = String.valueOf(inputMap.get("cvv2")).trim();//信用卡背后的 3 位数字

		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		Map<String, Object> checkOrderMap = tdOrderServiceImpl.checkOrderIsValid(tdOrder, accountId);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
			return checkOrderMap;
		}
		if(!tdOrder.getFundId().startsWith(BaseDictConstant.PP_FUND_TYPE_ID_EBZF)){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
			return resultMap;
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getMessage());
			return resultMap;
		}
		Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
		if(null == redisPayToken || redisPayToken.size() == 0){
			resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
			resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
			return resultMap;
		}
		if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
			resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		Map<String, Object> smsCodeVerifyMap = this.isNeedSmsCodeVerify(payOrderId, false);
		if(!"1".equals(String.valueOf(smsCodeVerifyMap.get("resultCode")))){
			if(ValidateUtils.isEmpty(validateCode)){
				resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "validateCode"));
				return resultMap;
			}
			if(Hint.YEEPAY_13006_SEND_VALIDATE_CODE_EXCEEDS_THE_MAXIMUM.getCodeString().equals(String.valueOf(smsCodeVerifyMap.get("resultCode")))){
				return smsCodeVerifyMap;
			}
		}
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}

		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(tdOrder.getFundId());
		if(null == ppFundPlatform || ppFundPlatform.getValidFlag() != DimDictEnum.VALID_FLAG_VALID.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("fundId=" + tdOrder.getFundId() + " 资金平台不存在或无效");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}
		PpFeeRatioA selectPpFeeRatioA = new PpFeeRatioA();
		selectPpFeeRatioA.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
		selectPpFeeRatioA.setBeginDate(DateUtils.getNowDate("yyyyMMdd"));
		selectPpFeeRatioA.setEndDate(DateUtils.getNowDate("yyyyMMdd"));
		List<PpFeeRatioA> ppFeeRatioAList = ppFeeRatioAServiceImpl.findPpFeeRatioAInfoList(selectPpFeeRatioA);
		if(null == ppFeeRatioAList || ppFeeRatioAList.size() == 0){
			if(logger.isInfoEnabled()){
				logger.info("fundId=" + tdOrder.getFundId() + " 资金平台费率配置信息不存在");
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}

		TdOrdExtPay tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(tdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13092.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13092.getMessage());
			return resultMap;
		}

		Map<String, String> otherParamNeedMap = new HashMap<>();
		String redisKey = "bindCardParamsNeed_".concat(payOrderId);
		String redisValue = String.valueOf(redisService.get(redisKey)).trim();
		if(logger.isInfoEnabled()){
			logger.info("redisKey=" + redisKey + ",redisValue=" + redisValue);
		}
		if(ValidateUtils.isNotEmpty(redisValue)){
			Map<String, String> bindCardParamsNeedMap = JSON.parseObject(redisValue, Map.class);
			ActPBank selectActPBank = new ActPBank();
			selectActPBank.setFundId(BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
			selectActPBank.setFromActTypeId(tdOrder.getFromActTypeId());
			selectActPBank.setActId(Long.valueOf(accountId));
			selectActPBank.setPayPropC(bindCardParamsNeedMap.get("bindId"));//借用字段：绑卡ID
			selectActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
			List<ActPBank> actPBankList = actPBankServiceImpl.findActPBankList(selectActPBank);
			if(null != actPBankList && actPBankList.size() > 0){
				String authItems = String.valueOf(actPBankList.get(0).getRemark()).trim();
				if(logger.isInfoEnabled()){
					logger.info("调用YeePay确认支付时，需要的鉴权要素为：" + authItems);
				}
				if(ValidateUtils.isNotEmpty(authItems)){
					Map<String, String> authItemsMap = JSON.parseObject(EncryptUtils.aesDecrypt(authItems, CipherAesEnum.CARDNOKEY), Map.class);
					String[] fieldArray = bindCardParamsNeedMap.get("otherParamsNeed").split(",");
					for(String field : fieldArray){
						field = String.valueOf(field).trim();
						otherParamNeedMap.put(field, authItemsMap.get(field));
					}
					if(ValidateUtils.isNotEmpty(cvv2)){
						otherParamNeedMap.put("cvv2", EncryptUtils.aesDecrypt(cvv2, CipherAesEnum.CARDNOKEY));
					}else {
						otherParamNeedMap.put("cvv2", "");
					}
					if(ValidateUtils.isNotEmpty(validThru)){
						otherParamNeedMap.put("validthru", EncryptUtils.aesDecrypt(validThru, CipherAesEnum.CARDNOKEY));
					}else {
						otherParamNeedMap.put("validthru", "");
					}
				}
			}
		}

		/**********************************调用易宝确认支付 begin*************************************/
		Map<String, String> reqDataMap = new HashMap<String, String>();
		reqDataMap.put("orderid", payOrderId.concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));
		reqDataMap.put("validatecode", validateCode);
		if(logger.isInfoEnabled()){
			logger.info("订单确认支付，请求参数：" + reqDataMap);
		}
		if(otherParamNeedMap.size() > 0){
			reqDataMap.putAll(otherParamNeedMap);
		}
		Map<String, String> yeepayResultMap = yeepayPaymentServiceImpl.paymentConfirm(reqDataMap);
		if(logger.isInfoEnabled()){
			logger.info("<<响应报文：" + yeepayResultMap);
		}
		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setUpdTime(new Date());
		String interfaceErrorCode = this.getInterfaceErrorCode(yeepayResultMap);
		updTdOrder.setErrCode("确认支付结果|".concat(interfaceErrorCode));
		if(!interfaceErrorCode.equals(Hint.SYS_SUCCESS.getCodeString())){
			this.updTdOrderInfo(updTdOrder);
		}
		if(null == yeepayResultMap || yeepayResultMap.size() == 0 || yeepayResultMap.containsKey("customError")){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		if(yeepayResultMap.containsKey("error_code")){
			String error_code = String.valueOf(yeepayResultMap.get("error_code")).trim();
			//请求频次过高，请过几秒重试
			if("601101".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13049_601101.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13049_601101.getMessage());
				return resultMap;
			}
			//订单重复提交
			if(BaseDictConstant.YEEPAY_ORDER_REPEAT_SUBMIT.equals(error_code) ||
			   BaseDictConstant.YEEPAY_ORDER_ALREADY_PAYMENT_SUCCESS.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13008_ORDER_REPEAT_SUBMIT.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13008_ORDER_REPEAT_SUBMIT.getMessage());
				return resultMap;
			}
			//订单已成功
			if(BaseDictConstant.YEEPAY_ORDER_ALREADY_TRADE_SUCCESS.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getMessage());
				return resultMap;
			}
			//订单已过期或已撤销
			if(BaseDictConstant.YEEPAY_ORDER_TIMEOUT_OR_CANCEL.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13011_ORDER_TIMEOUT_OR_CANCEL.getMessage());
				return resultMap;
			}
			//订单金额过低受限
			if(BaseDictConstant.YEEPAY_ORDER_AMOUNT_TOO_LOW.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getMessage());
				return resultMap;
			}
			//短信验证码错误
			if(BaseDictConstant.YEEPAY_SMS_VERIFY_CODE_ERROR.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13013_SMS_VERIFY_CODE_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13013_SMS_VERIFY_CODE_ERROR.getMessage());
				return resultMap;
			}
			//短信验证码无效或已过期
			if(BaseDictConstant.YEEPAY_SMS_VERIFY_CODE_INVALID_OR_TIMEOUT.equals(error_code) || "600136".equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13014_SMS_VERIFY_CODE_INVALID_OR_TIMEOUT.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13014_SMS_VERIFY_CODE_INVALID_OR_TIMEOUT.getMessage());
				return resultMap;
			}
			//短信验证码错误次数太多
			if(BaseDictConstant.YEEPAY_SMS_VERIFY_CODE_ERROR_COUNT_TOO_MUCH.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13015_SMS_VERIFY_CODE_ERROR_COUNT_TOO_MUCH.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13015_SMS_VERIFY_CODE_ERROR_COUNT_TOO_MUCH.getMessage());
				return resultMap;
			}
			//请确认身份证号是否正确
			if("600119".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13072_INPUT_USER_IDCARD_INVALID.getMessage());
				return resultMap;
			}
			//订单金额过低受限
			if("600113".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13050_600113.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13050_600113.getMessage());
				return resultMap;
			}
			//单卡超过当日累积支付限额
			if("600043".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13051_600043.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13051_600043.getMessage());
				return resultMap;
			}
			//单卡超过单笔支付限额
			if("600045".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13052_600045.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13052_600045.getMessage());
				return resultMap;
			}
			//单卡超过单月累积支付限额
			if("600046".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13053_600046.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13053_600046.getMessage());
				return resultMap;
			}
			//单卡超过单日累积支付次数上限
			if("600047".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13054_600047.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13054_600047.getMessage());
				return resultMap;
			}
			//单卡超过单月累积支付次数上限
			if("600048".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13055_600048.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13055_600048.getMessage());
				return resultMap;
			}
			//消费金额超限，请联系发卡银行
			if("600098".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13056_600098.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13056_600098.getMessage());
				return resultMap;
			}
			//商户状态冻结不能进行交易
			if("611110".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13057_611110.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13057_611110.getMessage());
				return resultMap;
			}
			//卡信息或银行预留手机号有误
			if("600095".equals(error_code) || "600120".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13041_CARDINFO_OR_RESERVE_PHONE_INVALID.getMessage());
				return resultMap;
			}
			//无效或不支持的卡号，请换卡支付
			if("600051".equals(error_code) || "600053".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13042_CARD_NO_INVALID_OR_NOT_SUPPORTED.getMessage());
				return resultMap;
			}
			//卡已过期，请换卡重新支付
			if("600118".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13043_BANK_CARD_OVERDUE.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13043_BANK_CARD_OVERDUE.getMessage());
				return resultMap;
			}
			//该卡为储蓄卡，请用信用卡支付
			if("600126".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13044_MUST_USE_CREDIT_CARD.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13044_MUST_USE_CREDIT_CARD.getMessage());
				return resultMap;
			}
			//该卡为信用卡，请用储蓄卡支付
			if("600127".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13045_MUST_USE_DEBIT_CARD.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13045_MUST_USE_DEBIT_CARD.getMessage());
				return resultMap;
			}
			//本卡被发卡方没收，请联系发卡银行
			if("600094".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13046_CARD_BE_CONFISCATED.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13046_CARD_BE_CONFISCATED.getMessage());
				return resultMap;
			}
			//本卡未激活或睡眠卡，请联系发卡银行
			if("600099".equals(error_code)){
				resultMap.put("resultCode", Hint.PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13047_CARD_NOT_ACTIVE_OR_SLEEP.getMessage());
				return resultMap;
			}
			//可用余额不足
			if(BaseDictConstant.YEEPAY_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getMessage());
				return resultMap;
			}
			//银行内部系统间调用超时或日切造成付款失败，稍候可能付款成功
			if(BaseDictConstant.YEEPAY_PAYMENT_FAILURE_600097.equals(error_code)){
				if(logger.isInfoEnabled()){
					logger.info("预支付结果：600097=支付失败，请稍候重试");
				}
				resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
				return resultMap;
			}
			//支付失败
			if(BaseDictConstant.YEEPAY_PAYMENT_FAILURE_600096.equals(error_code) ||
			   BaseDictConstant.YEEPAY_PAYMENT_FAILURE_600106.equals(error_code) ||
			   BaseDictConstant.YEEPAY_PAYMENT_FAILURE_600044.equals(error_code)){
				resultMap.put("resultCode", Hint.YEEPAY_13017_PAYMENT_FAILURE.getCodeString());
				resultMap.put("resultDesc", Hint.YEEPAY_13017_PAYMENT_FAILURE.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
		/**********************************调用易宝确认支付 end*************************************/

		String bankcardtype = String.valueOf(yeepayResultMap.get("bankcardtype")).trim();//银行卡类型 1：储蓄卡 2：信用卡
		if(ValidateUtils.isEmpty(bankcardtype)){
			if(logger.isInfoEnabled()){
				logger.info("易宝支付-确认支付接口返回银行卡类型为空 bankcardtype=" + bankcardtype);
			}
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
		String yborderid = String.valueOf(yeepayResultMap.get("yborderid")).trim();//易宝交易流水号
		if(ValidateUtils.isEmpty(yborderid)){
			yborderid = "";
		}
		String bankname = String.valueOf(yeepayResultMap.get("bankname")).trim();//银行卡名称
		if(ValidateUtils.isEmpty(bankname)){
			bankname = "";
		}
		String cardlast = String.valueOf(yeepayResultMap.get("cardlast")).trim();//卡号后4位
		if(ValidateUtils.isEmpty(cardlast)){
			cardlast = "";
		}
		String phone = String.valueOf(yeepayResultMap.get("phone")).trim();//手机号
		if(ValidateUtils.isEmpty(phone)){
			phone = "";
		}
		String bankcode = String.valueOf(yeepayResultMap.get("bankcode")).trim();//银行缩写，如ICBC
		if(ValidateUtils.isEmpty(bankcode)){
			bankcode = "";
		}

		/****************************更新交易单&交易扩展表信息 begin**********************************/
		updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setFundTdCode(yborderid);//资金平台交易流水号
		BigDecimal feeRatio;
		BigDecimal feeMin;
		if("2".equals(bankcardtype)){//信用卡
			feeRatio = ppFeeRatioAList.get(0).getCreditFeeRatio();
			feeMin = ppFeeRatioAList.get(0).getCreditFeeMin();
		}else {//储蓄卡
			feeRatio = ppFeeRatioAList.get(0).getDebitFeeRatio();
			feeMin = ppFeeRatioAList.get(0).getDebitFeeMin();
		}
		if(null == feeRatio || feeRatio.doubleValue() < 0){
			feeRatio = BigDecimal.ZERO;
		}
		updTdOrder.setServChrgRatio(feeRatio);//本次交易费率
		if(null == feeMin || feeMin.doubleValue() < 0){
			feeMin = BigDecimal.ZERO;
		}
		TdOrdExtPay getTdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		BigDecimal servChrgAmt = getTdOrdExtPay.getPaySelf().multiply(updTdOrder.getServChrgRatio()).setScale(2, BigDecimal.ROUND_HALF_UP);
		if(logger.isInfoEnabled()){
			logger.info("本次交易收取手续费：" + servChrgAmt + ",费用下限：feeMin=" + feeMin);
		}
		if(feeMin.doubleValue() > 0 && servChrgAmt.compareTo(feeMin) < 0){
			servChrgAmt = feeMin;
		}
		updTdOrder.setServChrgAmt(servChrgAmt);//本次交易手续费
		updTdOrder.setErrCode("确认支付结果|".concat(interfaceErrorCode));
		updTdOrder.setUpdTime(new Date());
		this.updTdOrderInfo(updTdOrder);

		TdOrdExtPay updTdOrdExtPay = new TdOrdExtPay();
		updTdOrdExtPay.setTdId(tdOrder.getTdId());//交易单ID
		updTdOrdExtPay.setFundTdCode(yborderid);//资金平台交易流水号
		updTdOrdExtPay.setUpdTime(new Date());
		if("1".equals(bankcardtype)){
			updTdOrdExtPay.setPayActTypeId(Short.valueOf(String.valueOf(DimDictEnum.PAY_ACT_TYPE_DEBIT_CARD.getCode())));//支付账号类别-储蓄卡
		}else{
			updTdOrdExtPay.setPayActTypeId(Short.valueOf(String.valueOf(DimDictEnum.PAY_ACT_TYPE_CREDIT_CARD.getCode())));//支付账号类别-信用卡
		}
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展信息");
		}
		tdOrderServiceImpl.updateTdOrdExtPayInfo(updTdOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}
		/****************************更新交易单&交易扩展表信息 end**********************************/

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("payOrderId", tdOrder.getTdId().toString());
		resultMap.put("merOrderId", tdOrder.getOrigOrdCode());
		resultMap.put("ebOrderId", yborderid);
		resultMap.put("bankCardType", bankcardtype);
		resultMap.put("bankName", bankname);
		resultMap.put("cardNoAfterFour", cardlast);
		resultMap.put("phone", phone);
		resultMap.put("bankCode", bankcode);
		return resultMap;
	}

	/**
	 * 易宝支付结果通知
	 * @param data
	 * @param encryptKey
	 * @return
	 * @throws Exception
     */
	public String yeepayPaymentResultNotice(String data, String encryptKey) throws Exception {
		Map<String, String> decryptDataMap = yeepayPaymentServiceImpl.decryptCallbackData(data, encryptKey);
		String orderid = String.valueOf(decryptDataMap.get("orderid")).trim();//请求易宝支付交易流水号
		String status = String.valueOf(decryptDataMap.get("status")).trim();//交易状态 0：失败 1：成功 2：撤销
		if(ValidateUtils.isEmpty(orderid)){
			if(logger.isInfoEnabled()){
				logger.info("参数orderid为空");
			}
			return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
		}
		if(ValidateUtils.isEmpty(status)){
			if(logger.isInfoEnabled()){
				logger.info("参数status为空");
			}
			return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
		}
		String payOrderId = orderid.split("-")[0];
		decryptDataMap.put("orderid", payOrderId);
		String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
		int expire = 60;
		Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
		}
		if(null == redisValue || redisValue == 0){
			if(logger.isInfoEnabled()){
				logger.info("当前订单发生并发请求，已阻止");
			}
			return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
		}
		String result;
		try{
			result = tdOrderServiceImpl.executeYeepayPaymentResultNotice(decryptDataMap);
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("系统异常：" + e.getMessage());
			}
			result = BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
		}
		redisService.delete(redisKey);
		if(BaseDictConstant.YEEPAY_PAYMENT_RESULT_SUCCESS.equals(result)){
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("payOrderId", payOrderId);
			dataMap.put("bank", String.valueOf(decryptDataMap.get("bank")));
			dataMap.put("lastNo", String.valueOf(decryptDataMap.get("lastno")));
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
		}
		return result;
	}

	/**
	 * 支付宝支付结果通知
	 * @param param
	 * @param payOrderId
	 * @return
	 * @throws Exception
	 */
	public String aliPaymentResultNotice(Map<String, Object> param, String payOrderId) throws Exception {
		String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
		int expire = 60;
		Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
		}
		if(null == redisValue || redisValue == 0){
			if(logger.isInfoEnabled()){
				logger.info("当前订单发生并发请求，已阻止");
			}
			return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
		}
		String result;
		try {
			result = tdOrderServiceImpl.executeAliPaymentResultNotice(param, payOrderId);
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("系统异常：" + e.getMessage());
			}
			result = BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
		}
		redisService.delete(redisKey);
		if(BaseDictConstant.ALIPAY_PAYMENT_RESULT_SUCCESS.equals(result)){
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("payOrderId", payOrderId);
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
		}
		return result;
	}

	/**
	 * 微信支付结果通知
	 * @param param
	 * @param payOrderId
	 * @return
	 * @throws Exception
	 */
	public String weChatPaymentResultNotice(Map<String, Object> param, String payOrderId) throws Exception {
		String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
		int expire = 60;
		Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
		}
		if(null == redisValue || redisValue == 0){
			if(logger.isInfoEnabled()){
				logger.info("当前订单发生并发请求，已阻止");
			}
			return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "系统异常");
		}
		String result;
		try {
			result = tdOrderServiceImpl.executeWeChatPaymentResultNotice(param, payOrderId);
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("系统异常：" + e.getMessage());
			}
			return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "系统异常");
		}
		redisService.delete(redisKey);
		String returnCode = WeChatConstants.RETURN_CODE_FAIL;
		String returnMsg = "处理失败";
		if(BaseDictConstant.WECHAT_PAYMENT_RESULT_SUCCESS.equals(result)){
			returnCode = WeChatConstants.RETURN_CODE_SUCCESS;
			returnMsg = WeChatConstants.RETURN_MSG_OK;
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("payOrderId", payOrderId);
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
		}
		return WeChatUtil.returnXmlResult(returnCode, returnMsg);
	}

	/**
	 * 建行支付结果通知
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String ccbPaymentResultNotice(Map<String, Object> param) throws Exception {
		String payOrderId = String.valueOf(param.get("ORDERID")).trim();
		if(ValidateUtils.isEmpty(payOrderId)){
			if(logger.isInfoEnabled()){
				logger.info("payOrderId=" + payOrderId + " 交易单号为空");
				return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
			}
		}
		payOrderId = payOrderId.split("_")[0];
		String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
		int expire = 60;
		Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
		}
		if(null == redisValue || redisValue == 0){
			if(logger.isInfoEnabled()){
				logger.info("当前订单发生并发请求，已阻止");
			}
			return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
		}
		String result;
		try{
			result = tdOrderServiceImpl.executeCcbPaymentResultNotice(param, payOrderId);
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("系统异常：" + e.getMessage());
			}
			result = BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
		}
		redisService.delete(redisKey);
		if(BaseDictConstant.CCB_PAYMENT_RESULT_SUCCESS.equals(result)){
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("payOrderId", payOrderId);
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
		}
		return result;
	}

	/**
	 * 银联支付结果通知
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String bkuPaymentResultNotice(Map<String, Object> param) throws Exception {
		String payOrderId = String.valueOf(param.get("MerOrderNo")).trim();
		if(ValidateUtils.isEmpty(payOrderId)){
			if(logger.isInfoEnabled()){
				logger.info("payOrderId=" + payOrderId + " 交易单号为空");
				return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
			}
		}
		payOrderId = payOrderId.split("_")[0];
		String redisKey = BaseDictConstant.PAYMENT_RESULT_REDIS_KEY.replace("{payOrderId}", payOrderId);
		int expire = 60;
		Long redisValue = redisService.setNx(redisKey, payOrderId, expire);
		if(logger.isInfoEnabled()){
			logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
		}
		if(null == redisValue || redisValue == 0){
			if(logger.isInfoEnabled()){
				logger.info("当前订单发生并发请求，已阻止");
			}
			return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
		}
		String result;
		try {
			result = tdOrderServiceImpl.executeBkuPaymentResultNotice(param, payOrderId);
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("系统异常：" + e.getMessage());
			}
			result = BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
		}
		redisService.delete(redisKey);
		if(BaseDictConstant.BKU_PAYMENT_RESULT_SUCCESS.equals(result)){
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("payOrderId", payOrderId);
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
		}
		return result;
	}

	/**
	 * ebao支付平台通知商户订单支付结果
	 * @param inputMap
	 * @throws Exception
     */
	public void noticeMerchantPaymentResult(Map<String, String> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("[" + inputMap.get("payOrderId") + "]TdOrderServiceGWImpl --> noticeMerchantPaymentResult 输入参数：" + inputMap);
		}
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();
		String bank = String.valueOf(inputMap.get("bank")).trim();
		if(ValidateUtils.isEmpty(bank)){
			bank = "";
		}
		String lastNo = String.valueOf(inputMap.get("lastNo")).trim();
		if(ValidateUtils.isEmpty(lastNo)){
			lastNo = "";
		}
		if(ValidateUtils.isEmpty(payOrderId)){
			if(logger.isInfoEnabled()){
				logger.info("[" + inputMap.get("payOrderId") + "]支付订单号为空");
			}
			return;
		}
		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		if(null == tdOrder){
			if(logger.isInfoEnabled()){
				logger.info("[" + inputMap.get("payOrderId") + "]支付订单号不存在");
			}
			return;
		}
		String escrowTradeNo = String.valueOf(tdOrder.getFundTdCode()).trim();
		if(ValidateUtils.isEmpty(escrowTradeNo)){
			escrowTradeNo = "";
		}
		TdOrdExtPay tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(null == tdOrdExtPay){
			if(logger.isInfoEnabled()){
				logger.info("[" + inputMap.get("payOrderId") + "]支付扩展信息不存在");
			}
			return;
		}
		String siTradeCode = String.valueOf(tdOrdExtPay.getSiTdCode()).trim();//医保交易流水号
		if(ValidateUtils.isEmpty(siTradeCode)){
			siTradeCode = "";
		}
		String serialId = String.valueOf(tdOrdExtPay.getHospSeqCode()).trim();//门诊流水号
		if(ValidateUtils.isEmpty(serialId)){
			serialId = "";
		}
		TdMiPara tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
		String settleDetailInfo = "";
		String miSettleFlag = "0";//0-纯自费 1-预结算分账后纯自费 2-预结算分账后使用医保
		if(null != tdMiPara){
			settleDetailInfo = String.valueOf(tdMiPara.getClmRet()).trim();//结算明细信息
			if(ValidateUtils.isEmpty(settleDetailInfo)){
				settleDetailInfo = "";
			}else {
				Map settleDetailInfoMap = JsonStringUtils.jsonStringToObject(settleDetailInfo, Map.class);
				if(null != settleDetailInfoMap && settleDetailInfoMap.size() > 0){
					miSettleFlag = String.valueOf(settleDetailInfoMap.get("miSettleFlag")).trim();
					if(ValidateUtils.isEmpty(miSettleFlag)){
						miSettleFlag = "0";
					}
				}
			}
		}
		String orderPayStatus;
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			orderPayStatus = "0";//成功
		}else{
			orderPayStatus = "1";//失败
		}
		String busiPayStatus;
		if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			busiPayStatus = "0";//成功
		}else if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
			busiPayStatus = "1";//失败
		}else {
			busiPayStatus = "2";//初始化
		}
		String siPayStatus;
		if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
			siPayStatus = "0";//成功
		}else if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
			siPayStatus = "1";//失败
		}else {
			siPayStatus = "2";//初始化
		}
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("payorderid", payOrderId);
		resultMap.put("merorderid", tdOrder.getOrigOrdCode());
		resultMap.put("amount", tdOrder.getRecvCur().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		resultMap.put("medicarePersonPayMoney", tdOrdExtPay.getPayPacct().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//医保个人账户支付金额
		resultMap.put("insuranceFundPayMoney", tdOrdExtPay.getPaySi().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//统筹基金支付金额
		resultMap.put("personalPayMoney", tdOrdExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP).toString());//个人支付金额
		resultMap.put("usePayChannelType", tdOrdExtPay.getPayTypeId().toString());//使用支付通道方式
		resultMap.put("orderExtendInfo", "");//订单扩展信息
		resultMap.put("bank", bank);
		resultMap.put("lastno", lastNo);
		resultMap.put("paystatus", orderPayStatus);
		resultMap.put("busipaystatus", busiPayStatus);
		resultMap.put("sipaystatus", siPayStatus);
		resultMap.put("payfinishtime", DateUtils.dateToString(tdOrder.getTdEndTime(), "yyyy-MM-dd HH:mm:ss"));
		resultMap.put("escrowTradeNo", escrowTradeNo);
		resultMap.put("siTradeCode", siTradeCode);
		resultMap.put("serialId", serialId);
		resultMap.put("settleDetailInfo", settleDetailInfo);
		resultMap.put("miSettleFlag", miSettleFlag);
		String fundCode = "";
		if(tdOrdExtPay.getPayTypeId() != DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
			PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(tdOrder.getFundId());
			if(null != ppFundPlatform){
				PpFundType ppFundType = CacheUtils.getPpFundType(ppFundPlatform.getPpFundTypeId());
				if(null != ppFundType){
					fundCode = ppFundType.getDispOrder().toString();
				}
			}
		}
		resultMap.put("fundCode", fundCode);
		Map<String, String> dataMap = dataEncrypt(resultMap, tdOrder.getChanAppid());
		if(logger.isInfoEnabled()){
			logger.info("[" + inputMap.get("payOrderId") + "]异步通知商户订单支付结果 原始报文：" + resultMap + ",加密后报文：" + dataMap);
		}
		String callBackUrl = String.valueOf(tdOrder.getCbAddr()).trim();
		String result;
		if(callBackUrl.startsWith("https")){
			result = HttpsPost.doPost(callBackUrl, JsonStringUtils.objectToJsonString(dataMap), HttpsPost.CONTENT_TYPE_JSON, HttpsPost.DEFAULT_CHARSET, HttpsPost.CONNECT_TIMEOUT, HttpsPost.READ_TIMEOUT);
		}else {
			result = HttpRequest.doPostJson(callBackUrl, JSON.toJSONString(dataMap));
		}
		if(logger.isInfoEnabled()){
			logger.info("<<[" + inputMap.get("payOrderId") + "]接口响应报文：" + result);
		}
	}

	/**
	 * 数据加密
	 * @param resultMap
	 * @return
	 * @throws Exception
     */
	private Map<String, String> dataEncrypt(Map<String, String> resultMap, String appId) throws Exception {

		Map<String, String> encryptMap = new HashMap<String, String>();

		ActSpInfo actSpInfo = accountCacheServiceImpl.findActSpInfoByAppId(appId);
		if(null == actSpInfo){
			encryptMap.put("resultCode", Hint.SYS_10012_APPID_ERROR.getCodeString());
			encryptMap.put("resultDesc", Hint.SYS_10012_APPID_ERROR.getMessage());
			return encryptMap;
		}
		String ybPrivateKey = String.valueOf(actSpInfo.getServerKeyPri()).trim();
		String shPublicKey = String.valueOf(actSpInfo.getSpKeyPub()).trim();
		if(actSpInfo.getValidFlag() != BaseDictConstant.VALID_FLAG_YES || ValidateUtils.isEmpty(ybPrivateKey) || ValidateUtils.isEmpty(shPublicKey)){
			encryptMap.put("resultCode", Hint.SYS_10013_VALID_FLAG_NO.getCodeString());
			encryptMap.put("resultDesc", Hint.SYS_10013_VALID_FLAG_NO.getMessage());
			return encryptMap;
		}

		/**
		 * 生成AESkey和AESIv秘钥
		 */
		AesCipher aes = new AesCipher();
		String aesKey = aes.generateKeyToBase64(128);
		String aesIv = aes.generateIvBytesToBase64();//生成aeskey 和 aesiv
		Map<String, String> keysMap = new HashMap<String, String>();
		keysMap.put("aesKey", aesKey);
		keysMap.put("aesIv", aesIv);
		String keysJson = JSON.toJSONString(keysMap);

		/**
		 * RSA易保私密解密
		 * 输入：动态AES KEY,动态 AES Iv
		 * 输出 ：keys(RSA加密 AES密钥)，ybPrivateKEY(RSA易保PrivateKEY)
		 */
		String keysEncrypt = RSA.encrypt(keysJson, shPublicKey);
		/**
		 * 签名数据
		 * 输入：原文数据，易保RSA privateKey
		 * 输出：密文
		 */
		String dataJson = JSON.toJSONString(resultMap);
		String signEncrypt = RSA.sign(dataJson, ybPrivateKey);
		/**
		 * 数据加密
		 * 输入：原文数据，动态AES KEY,动态 AES Iv
		 */
		String dataEncrypt = aes.encrypt(dataJson, aesKey, aesIv);

		encryptMap.put("sign", signEncrypt);
		encryptMap.put("keys", keysEncrypt);
		encryptMap.put("data", dataEncrypt);
		return encryptMap;
	}

	@Override
	public Map<String, Object> quitOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("请求放弃支付接口，请求参数：" + inputMap);
		}
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//订单号
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//硬件id
		String quitFlag = String.valueOf(inputMap.get("quitFlag")).trim();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("payOrderId", payOrderId);
		resultMap.put("accountId", accountId);
		
		//验证用户是否是登录状态
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}
		
		//验证订单是否超时
		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		if(logger.isInfoEnabled()){
			logger.info("查询订单信息，输入参数：" + payOrderId);
		}
		Map<String, Object> checkOrderMap = this.checkOrderIsValid2(tdOrder, accountId);
		if(logger.isInfoEnabled()){
			logger.info("验证订单是否有效：" + checkOrderMap);
		}
		if(!Hint.PAY_ORDER_13075_ORDER_NOT_TOP.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
			if(logger.isInfoEnabled()){
				logger.info("订单状态未做更新，返回结果：" + checkOrderMap);
			}
			return checkOrderMap;
		}
		
		//验证扩展支付表订单是否超时
		TdOrdExtPay tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(Long.valueOf(payOrderId));
		if(logger.isInfoEnabled()){
			logger.info("查询订单交易扩展信息，输入参数：" + payOrderId);
		}
		Map<String, Object> checkOrderExtMap = this.checkOrderExtIsValid2(tdOrdExtPay, accountId);
		if(logger.isInfoEnabled()){
			logger.info("验证订单扩展信息是否有效：" + checkOrderExtMap);
		}
		if(!Hint.PAY_ORDER_13075_ORDER_NOT_TOP.getCodeString().equals(String.valueOf(checkOrderExtMap.get("resultCode")))){
			if(logger.isInfoEnabled()){
				logger.info("订单扩展状态未做更新，返回结果：" + checkOrderExtMap);
			}
			return checkOrderExtMap;
		}
		
		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_FAILED.getCode())));
		updTdOrder.setErrCode(Hint.PAY_ORDER_13074_QUIT_ORDER.getMessage()+"|"+Hint.PAY_ORDER_13074_QUIT_ORDER.getCodeString());
		Date date = new Date();
		updTdOrder.setTdEndTime(date);
		updTdOrder.setUpdTime(date);
		if(logger.isInfoEnabled()){
			logger.info("开始更新订单信息");
		}
		tdOrderServiceImpl.updateTdOrderInfo(updTdOrder);
		
		//更新扩展表状态
		TdOrdExtPay updTdOrdExtPay = new TdOrdExtPay();
		updTdOrdExtPay.setTdId(tdOrder.getTdId());
		if(tdOrdExtPay.getPayTypeId().intValue() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode()){//商业支付
			updTdOrdExtPay.setTdBusiStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_FAILED.getCode())));
			updTdOrdExtPay.setTdSiStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCode())));
			updTdOrdExtPay.setChanRetCode(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString());
			updTdOrdExtPay.setChanRetDesc(Hint.PAY_ORDER_13074_QUIT_ORDER.getMessage());
		}else if(tdOrdExtPay.getPayTypeId().intValue() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){//社保支付
			updTdOrdExtPay.setTdBusiStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCode())));
			updTdOrdExtPay.setTdSiStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_FAILED.getCode())));
			updTdOrdExtPay.setSiRetCode(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString());
			updTdOrdExtPay.setSiRetDesc(Hint.PAY_ORDER_13074_QUIT_ORDER.getMessage());
		}else{//混合支付
			updTdOrdExtPay.setTdBusiStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_FAILED.getCode())));
			updTdOrdExtPay.setTdSiStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_FAILED.getCode())));
			updTdOrdExtPay.setChanRetCode(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString());
			updTdOrdExtPay.setChanRetDesc(Hint.PAY_ORDER_13074_QUIT_ORDER.getMessage());
			updTdOrdExtPay.setSiRetCode(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString());
			updTdOrdExtPay.setSiRetDesc(Hint.PAY_ORDER_13074_QUIT_ORDER.getMessage());
		}
		updTdOrdExtPay.setUpdTime(date);
		if(logger.isInfoEnabled()){
			logger.info("开始更新订单扩展信息");
		}
		tdOrderServiceImpl.updateTdOrdExtPayInfo(updTdOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新完成");
		}

		if("1".equals(quitFlag)){
			if(logger.isInfoEnabled()){
				logger.info("本次不通知业务线支付结果");
			}
		}else {
			/************异步通知业务线 begin**************/
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("payOrderId", payOrderId);
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
			/************异步通知业务线 end**************/
		}

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	/**
	 * 交易单处理
	 * @param inputMap
	 * @return
	 */
	public Map<String, Object> createTradeOrderProcess(Map<String, Object> inputMap) throws Exception {
		String origOrderId = String.valueOf(inputMap.get("origOrderId")).trim();//原交易订单号
		if(ValidateUtils.isNotEmpty(origOrderId)){
			Map<String, Object> params = new HashMap<>();
			params.put("accountId", String.valueOf(inputMap.get("accountId")).trim());
			params.put("payOrderId", origOrderId);
			params.put("hardwareId", String.valueOf(inputMap.get("hardwareId")).trim());
			params.put("quitFlag", "1");
			Map<String, Object> resultMap = quitOrder(params);
			if(logger.isInfoEnabled()){
				logger.info("设置订单失效，处理结果：" + resultMap);
			}
		}
		return createTradeOrder(inputMap);
	}

	/**
	 * 创建交易订单
	 * @param inputMap 输入Map
	 * @return 输出Map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> createTradeOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> createTradeOrder 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户订单号
		String merOrderExpire = String.valueOf(inputMap.get("merOrderExpire")).trim();//商户订单有效期，单位：秒
		String goodsDesc = String.valueOf(inputMap.get("goodsDesc")).trim();//商品描述信息
		String workSpActId = String.valueOf(inputMap.get("workSpActId")).trim();//合作商户账号ID
		String transBusiType = String.valueOf(inputMap.get("transBusiType")).trim();//交易一级业务类型 201-缴费 202-挂号 203-购药
		String transSecBusiType = String.valueOf(inputMap.get("transSecBusiType")).trim();//交易二级业务类型
		BigDecimal totalPayMoney = BigDecimal.valueOf(Double.parseDouble(String.valueOf(inputMap.get("totalPayMoney")).trim())).setScale(2, BigDecimal.ROUND_HALF_UP);//支付总额
		String callbackUrl = String.valueOf(inputMap.get("callbackUrl")).trim();
		String appId = String.valueOf(inputMap.get("appId")).trim();//渠道商户ID
		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();
		String systemId = String.valueOf(inputMap.get("systemId")).trim();
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();
		String socialSecurityBindId = String.valueOf(inputMap.get("socialSecurityBindId")).trim();//社保卡绑卡ID
		String buttonShowName = String.valueOf(inputMap.get("buttonShowName")).trim();//付款结果页返回按钮显示文案
		String webTerminalOrderId = String.valueOf(inputMap.get("webTerminalOrderId")).trim();//Web终端订单号
		String orderExtendInfo = String.valueOf(inputMap.get("orderExtendInfo")).trim();//订单扩展信息
		String buySelf = String.valueOf(inputMap.get("buySelf")).trim();//自费标识 0-全部自费 1-部分自费或全部医保

		DimBusiDetType dimBusiDetType = CacheUtils.getDimBusiDetType(transSecBusiType);
		if(null == dimBusiDetType){
			resultMap.put("resultCode", Hint.PAY_ORDER_13081.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13081.getMessage());
			return resultMap;
		}
		if(!transBusiType.equals(dimBusiDetType.getTdBusiTypeId().toString())){
			resultMap.put("resultCode", Hint.PAY_ORDER_13084.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13084.getMessage());
			return resultMap;
		}

		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}
		PayUser payUser = (PayUser) checkPayUserMap.get("payUser");
		ActPerson actPerson = (ActPerson) checkPayUserMap.get("actPerson");

		ActSp channelSp = actSpServiceImpl.findByChannelAppId(appId);
		if(null == channelSp){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		if(channelSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}
		ActSp workSp = actSpServiceImpl.findByWorkSpActId(Long.valueOf(workSpActId));
		if(null == workSp){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL.getMessage());
			return resultMap;
		}
		if(workSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}
		if(null == workSp.getChanActId() || channelSp.getActId().longValue() != workSp.getChanActId().longValue()){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID.getMessage());
			return resultMap;
		}
		if(!(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_1.getCode() ||
				channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_2.getCode() ||
				channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_3.getCode())){
			resultMap.put("resultCode", Hint.PAY_ORDER_13095.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13095.getMessage());
			return resultMap;
		}
		Long toActId;
		if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_1.getCode()){//大账户集中收单
			toActId = channelSp.getActId();
		}else{
			toActId = workSp.getActId();
		}
		String workFundId = String.valueOf(workSp.getFundId()).trim();
		if(ValidateUtils.isEmpty(workFundId)){
			if(logger.isInfoEnabled()){
				logger.info("FundId=" + workFundId + " 资金平台不能为空");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}
		if(BaseDictConstant.MERCHANT_FUND_ID_MULTIPLE.equalsIgnoreCase(workFundId)){
			//当有多个资金平台ID，默认获取排序第一个
			workFundId = CacheUtils.getPpFundPlatformByWorkActId(workSp.getActId().toString()).getFundId();
		}
		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(workFundId);
		if(null == ppFundPlatform || ppFundPlatform.getValidFlag() != DimDictEnum.VALID_FLAG_VALID.getCode()){
			if(logger.isInfoEnabled()){
				logger.info("FundId=" + workFundId + " 资金平台无效或不存在");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}
		PpFundType ppFundType = CacheUtils.getPpFundType(ppFundPlatform.getPpFundTypeId());
		if(null == ppFundType){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
			return resultMap;
		}
		String micId = String.valueOf(channelSp.getMicId()).trim();
		if(ValidateUtils.isEmpty(micId)){
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "micId"));
			return resultMap;
		}
		String spFundCode = "";
		if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_2.getCode() ||
		   channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_3.getCode()){
			if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_2.getCode()){
				if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundType.getPpFundTypeId()) ||
					BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundType.getPpFundTypeId())){
					spFundCode = CacheUtils.getPpIntfPara(workFundId.concat("_").concat(EBaoConstants.PARA_TYPE_SELLER_ID));
				}else if(BaseDictConstant.PP_FUND_TYPE_ID_WECHAT.equals(ppFundType.getPpFundTypeId())){
					spFundCode = WeChatUtil.getMchId(workFundId);
				}
			}else {
				spFundCode = String.valueOf(workSp.getSpFundCode()).trim();
			}
		}

		ActSpBusiRel selectActSpBusiRel = new ActSpBusiRel();
		selectActSpBusiRel.setActId(channelSp.getActId());
		selectActSpBusiRel.setBusiDetTypeId(transSecBusiType);
		ActSpBusiRel channelActSpBusiRel = actSpBusiRelServiceImpl.findActSpBusiRel(selectActSpBusiRel);
		if(null == channelActSpBusiRel){
			resultMap.put("resultCode", Hint.PAY_ORDER_13082.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13082.getMessage());
			return resultMap;
		}

		TdOrder tdOrder = new TdOrder();
		tdOrder.setChanAppid(appId);
		tdOrder.setOrigOrdCode(merOrderId);
		long orderCount = 0;
		List<Long> orderCountList = tdOrderServiceImpl.findTdOrderCountByMerOrderId(tdOrder);
		for(Long count : orderCountList){
			orderCount = orderCount + count;
		}
		if(orderCount > 0){
			resultMap.put("resultCode", Hint.CHAN_USER_13003_MERORDERID_EXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_USER_13003_MERORDERID_EXIST.getMessage());
			return resultMap;
		}

		String serviceLineType = String.valueOf(dimBusiDetType.getBusiInFlag()).trim();//业务线类型 1-内部业务 0-外部业务
		if(ValidateUtils.isEmpty(serviceLineType)){
			serviceLineType = "1";
		}
		if(logger.isInfoEnabled()){
			logger.info("当前订单业务线类型 serviceLineType=" + serviceLineType);
		}
		BigDecimal payPacct = BigDecimal.ZERO;//个人账户支付金额
		BigDecimal paySi = BigDecimal.ZERO;//社保报销金额(统筹基金报销)
		BigDecimal paySelf = BigDecimal.ZERO;//个人支付金额
		String usePayChannelType = "";//使用支付通道方式 1-商业支付 2-社保支付 3-混合支付
		String goodsExpenseDetail = "";//商品报销详情
		List<Map<String, String>> businessPayTypes = new ArrayList<>();//商业支付方式
		List<Map<String, String>> medicarePayTypes = new ArrayList<>();//医保支付方式
		String medicalClass = "";//1-医院门诊 2-药店购药
		String insuredPersonType = "";//1-职工 2-居民
		String cycId = "";//验证信息，后续与个人相关接口都需要此参数
		String siCardNo = "";//社保卡号
		String siPCode = "";//个人编号
		TdMiPara tdMiPara = null;
		String doctorFlowId = "";
		String interactiveCode = "";//交互编码
		String interactiveDesc = "";//交互描述
		String serialNumber = IdCreatorUtils.getAccountRecordId();//流水号

		if(buySelf.equals(DimDictEnum.BUY_SELF_YES.getCodeString())){
			if(logger.isInfoEnabled()){
				logger.info("当前业务要求订单全部自费 buySelf=" + buySelf);
			}
			paySelf = totalPayMoney;
			usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
			this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
		}else {
			if(logger.isInfoEnabled()){
				logger.info("检测当前业务是否允许订单医保结算 workSpMiPayFlag=" + workSp.getMiPayFlag() + ",channelActSpBusiRelMiPayFlag=" + channelActSpBusiRel.getMiPayFlag());
			}
			String medicareUseFlag = String.valueOf(workSp.getMiPayFlag()).trim();//合作商户是否使用医保支付，1是0否
			if(DimDictEnum.BUY_SELF_NO.getCodeString().equals(medicareUseFlag)){
				medicareUseFlag = String.valueOf(channelActSpBusiRel.getMiPayFlag()).trim();//渠道商户与业务子类型关系是否使用医保支付，1是0否
				if(DimDictEnum.BUY_SELF_NO.getCodeString().equals(medicareUseFlag)){
					medicareUseFlag = DimDictEnum.BUY_SELF_NO.getCodeString();
				}else {
					medicareUseFlag = DimDictEnum.BUY_SELF_YES.getCodeString();
				}
			}else {
				medicareUseFlag = DimDictEnum.BUY_SELF_YES.getCodeString();
			}
			if(DimDictEnum.BUY_SELF_YES.getCodeString().equals(medicareUseFlag)){//纯自费
				if(logger.isInfoEnabled()){
					logger.info("medicareUseFlag=" + medicareUseFlag + ",fundId=" + workFundId + " 纯自费订单");
				}
				paySelf = totalPayMoney;
				usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
				this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
			}else{//可能混合
				if(logger.isInfoEnabled()){
					logger.info("medicareUseFlag=" + medicareUseFlag + ",fundId=" + workFundId + " 可能混合支付订单");
				}
				String nowTime = DateUtils.getNowDate("yyyy-MM-dd HH:mm:ss");
				String endMiSettleTime = String.valueOf(Propertie.APPLICATION.value("endMiSettleTime")).trim();
				String endMiSettleTimeTemp = endMiSettleTime;
				String lastDayOfMonth = DateUtils.getLastDayOfMonth("yyyy-MM-dd");
				endMiSettleTime = lastDayOfMonth.concat(" ").concat(endMiSettleTime);
				if(logger.isInfoEnabled()){
					logger.info("nowTime=" + nowTime + ",endMiSettleTime=" + endMiSettleTime);
				}
				if(DateUtils.strToDate(nowTime, "yyyy-MM-dd HH:mm:ss").compareTo(DateUtils.strToDate(endMiSettleTime, "yyyy-MM-dd HH:mm:ss")) >= 0){
					if(logger.isInfoEnabled()){
						logger.info("医保结算已超过当月截止日期，转为自费订单");
					}
					paySelf = totalPayMoney;
					usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
					this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
					interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_SETTLE_CLOSING_DATE.getCodeString();
					interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_SETTLE_CLOSING_DATE.getName().replace("{X}", endMiSettleTimeTemp.substring(0, 5));
				}else {
					Map<String, Object> miSettleOrderExtendInfoMap = this.checkMediSettleData(orderExtendInfo, totalPayMoney);
					if(!Hint.SYS_SUCCESS.getCodeString().equals(miSettleOrderExtendInfoMap.get("resultCode").toString())){
						return miSettleOrderExtendInfoMap;
					}
					boolean isMiSettle = true;//是否使用医保结算 true-是 false-否
					List<Map<String, String>> mediSettleDetailList = (List<Map<String, String>>) miSettleOrderExtendInfoMap.get("mediSettleDetailList");
					String treatmentCostType = String.valueOf(miSettleOrderExtendInfoMap.get("treatmentCostType")).trim();//类型 1-药品 2-非药品
					if("1".equals(String.valueOf(CacheUtils.getDimSysConfConfValue("HOSP_TREATMENT_LIMIT_FLAG")).trim())){
						//1-限制性别 2-限制年龄(18周岁) 0-合法
						String checkFlag = this.checkTreatmentLimit(actPerson, mediSettleDetailList, treatmentCostType);
						if("1".equals(checkFlag)){
							isMiSettle = false;
							if(logger.isInfoEnabled()){
								logger.info("此次包含限制性别项目[药品]，转为自费订单");
							}
							paySelf = totalPayMoney;
							usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
							this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
							interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_YP_SEX_LIMIT.getCodeString();
							interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_YP_SEX_LIMIT.getName();
						}else if("2".equals(checkFlag)){
							isMiSettle = false;
							if(logger.isInfoEnabled()){
								logger.info("此次包含限制性别项目[非药品]，转为自费订单");
							}
							paySelf = totalPayMoney;
							usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
							this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
							interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_FYP_SEX_LIMIT.getCodeString();
							interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_FYP_SEX_LIMIT.getName();
						}else if("3".equals(checkFlag)){
							isMiSettle = false;
							if(logger.isInfoEnabled()){
								logger.info("此次包含限制年龄项目[药品]，转为自费订单");
							}
							paySelf = totalPayMoney;
							usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
							this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
							interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_YP_AGE_LIMIT.getCodeString();
							interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_YP_AGE_LIMIT.getName();
						}else if("4".equals(checkFlag)){
							isMiSettle = false;
							if(logger.isInfoEnabled()){
								logger.info("此次包含限制年龄项目[非药品]，转为自费订单");
							}
							paySelf = totalPayMoney;
							usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
							this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
							interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_FYP_AGE_LIMIT.getCodeString();
							interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_FYP_AGE_LIMIT.getName();
						}
					}
					if(isMiSettle){
						doctorFlowId = String.valueOf(miSettleOrderExtendInfoMap.get("doctorFlowId")).trim();//门诊流水号
						String fixedHospCode = String.valueOf(miSettleOrderExtendInfoMap.get("fixedHospCode")).trim();//定点医院编码
						String visDate = String.valueOf(miSettleOrderExtendInfoMap.get("visDate")).trim();//就诊日期
						String ylType = String.valueOf(miSettleOrderExtendInfoMap.get("ylType")).trim();//医疗类别 11-普通门诊 14-药店购药 16-门诊规定病种(慢性病) 45-计划生育手术(门诊)
						String mxbID = String.valueOf(miSettleOrderExtendInfoMap.get("mxbID")).trim();//慢性病编码
						String jhsyssType = String.valueOf(miSettleOrderExtendInfoMap.get("jhsyssType")).trim();//计生手术类别
						String jdjbID = String.valueOf(miSettleOrderExtendInfoMap.get("jdjbID")).trim();//诊断疾病编号
						String jdjbName = String.valueOf(miSettleOrderExtendInfoMap.get("jdjbName")).trim();//诊断疾病名称
						String officeName = String.valueOf(miSettleOrderExtendInfoMap.get("officeName")).trim();//科室名称
						String doctorID = String.valueOf(miSettleOrderExtendInfoMap.get("doctorID")).trim();//医生编号
						String doctorName = String.valueOf(miSettleOrderExtendInfoMap.get("doctorName")).trim();//医生姓名
						List<String> cfIds = new ArrayList<>();
						if("1".equals(serviceLineType)){//内部业务
							if(!(DimDictEnum.MEDICAL_CLASS_YAODIANGOUYAO.getCodeString().equals(ylType) ||
									DimDictEnum.MEDICAL_CLASS_PUTONGMENZHEN.getCodeString().equals(ylType) ||
									DimDictEnum.MEDICAL_CLASS_MANXINGBING.getCodeString().equals(ylType))){
								resultMap.put("resultCode", Hint.PAY_ORDER_13103.getCodeString());
								resultMap.put("resultDesc", Hint.PAY_ORDER_13103.getMessage());
								return resultMap;
							}
							//检测用户是否已绑社保卡，获取社保卡号
							if(logger.isInfoEnabled()){
								logger.info("查询用户社保绑卡信息，请求参数：payUserCode=" + payUser.getPayUserCode());
							}
							SocialCardInfo socialCardInfo = ebaoServiceImpl.querySocialCard(payUser.getPayUserCode());
							if(logger.isInfoEnabled()){
								logger.info("响应报文：socialCardInfo=" + (socialCardInfo == null ? null : JSON.toJSONString(socialCardInfo)));
							}
							if(null == socialCardInfo || !"0".equals(socialCardInfo.getCode()) || !"1".equals(socialCardInfo.getCardStatus())){//社保卡无效或未绑卡
								isMiSettle = false;
								if(logger.isInfoEnabled()){
									logger.info("当前用户社保卡无效或未绑定，转为自费订单");
								}
								paySelf = totalPayMoney;
								usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
								this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
								interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_UNBIND_SI_CARD.getCodeString();
								interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_UNBIND_SI_CARD.getName();
							}
							if(isMiSettle){
								//1-城镇职工;2-城乡居民;3-灵活就业人员
								if(ValidateUtils.isEmpty(socialCardInfo.getpSiCatId()) ||
										!("1".equals(String.valueOf(socialCardInfo.getpSiCatId()).trim()) ||
												"2".equals(String.valueOf(socialCardInfo.getpSiCatId()).trim()) ||
												"3".equals(String.valueOf(socialCardInfo.getpSiCatId()).trim()))){
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("当前用户人员类型异常，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_INSURED_PERSONNEL_TYPE.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_INSURED_PERSONNEL_TYPE.getName();
								}
							}
							if(isMiSettle){
								if("1".equals(String.valueOf(socialCardInfo.getpSiCatId()).trim()) || "3".equals(String.valueOf(socialCardInfo.getpSiCatId()).trim())){
									insuredPersonType = BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE;
								}else {
									insuredPersonType = BaseDictConstant.INSURED_PERSON_TYPE_RESIDENT;
								}
								if(ValidateUtils.isEmpty(socialCardInfo.getMedicarePersonNo())){//医保个人编号
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("当前用户医保个人编号为空，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_PERSON_IS_NULL.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_PERSON_IS_NULL.getName();
								}
							}
							if(isMiSettle){
								//检测实名认证与绑卡是否为同一人
								if(!(String.valueOf(actPerson.getpName()).trim().equals(String.valueOf(socialCardInfo.getCardName()).trim()) &&
										String.valueOf(actPerson.getpCertNo()).trim().equalsIgnoreCase(String.valueOf(socialCardInfo.getCardNo()).trim()))){
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("当前用户实名认证与绑定社保卡非同一人，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_NOT_THE_SAME_PERSON.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_NOT_THE_SAME_PERSON.getName();
								}
							}
							if(isMiSettle){
								if(ValidateUtils.isEmpty(socialCardInfo.getSocialSecurityNo())){
									if(logger.isInfoEnabled()){
										logger.info("用户社保卡号为空 socialSecurityNo=" + socialCardInfo.getSocialSecurityNo());
									}
									//调用卡信息查询接口
									if(logger.isInfoEnabled()){
										logger.info("查询卡管中心用户社保卡信息，请求参数：fullName=" + actPerson.getpName() + ",idNumber=" + actPerson.getpCertNo());
									}
									Map<String, Object> cardDataMap = ebaoServiceImpl.cardDataQuery(actPerson.getpName(), actPerson.getpCertNo().toUpperCase());
									if(logger.isInfoEnabled()){
										logger.info("响应报文：" + cardDataMap);
									}
									if(!"0".equals(String.valueOf(cardDataMap.get("code")))){
										isMiSettle = false;
										if(logger.isInfoEnabled()){
											logger.info("获取用户社保卡信息出现异常(卡管中心)，转为自费订单");
										}
										paySelf = totalPayMoney;
										usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
										this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
										interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_GET_SOCIAL_CARD_INFO_ERROR.getCodeString();
										interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_GET_SOCIAL_CARD_INFO_ERROR.getName();
									}else if(!"OK".equals(String.valueOf(cardDataMap.get("ERR")).trim())){
										isMiSettle = false;
										if(logger.isInfoEnabled()){
											logger.info("用户社保卡信息不存在(卡管中心)，转为自费订单");
										}
										paySelf = totalPayMoney;
										usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
										this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
										interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_SOCIAL_CARD_INFO_NOT_EXIST.getCodeString();
										interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_SOCIAL_CARD_INFO_NOT_EXIST.getName();
									}else {
										siCardNo = String.valueOf(cardDataMap.get("AAZ500")).trim();//社保卡号
									}
								}else {
									siCardNo = String.valueOf(socialCardInfo.getSocialSecurityNo()).trim();//社保卡号
								}
							}
							TreatmentStatusRes treatmentStatusRes = null;
							if(isMiSettle){
								//查询人员是否存在封锁
								TreatmentStatusReq treatmentStatusReq = new TreatmentStatusReq();
								treatmentStatusReq.setCardID(siCardNo);//社保卡号
								treatmentStatusReq.setPerCardID(actPerson.getpCertNo().toUpperCase());//身份证号
								treatmentStatusReq.setPerName(actPerson.getpName());//姓名
								if(logger.isInfoEnabled()){
									logger.info("查询用户医疗待遇封锁信息，请求参数：fixedHospCode=" + fixedHospCode + ",treatmentStatusReq=" + JSON.toJSONString(treatmentStatusReq));
								}
								treatmentStatusRes = commInfoServiceImpl.queryTreatStatus(fixedHospCode, treatmentStatusReq);
								if(logger.isInfoEnabled()){
									logger.info("响应报文：treatmentStatusRes=" + (treatmentStatusRes == null ? null : JSON.toJSONString(treatmentStatusRes)));
								}
								if(null == treatmentStatusRes){
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("获取用户医疗待遇封锁信息出现异常，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_GET_MI_TREATMENT_BLOCKADE_INFO_ERROR.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_GET_MI_TREATMENT_BLOCKADE_INFO_ERROR.getName();
								}
								if(isMiSettle && treatmentStatusRes.getRTNCode() != 1){
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("您的社保卡可能在办理中或未激活，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_SOCIAL_CARD_NOT_ACTIVE.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_SOCIAL_CARD_NOT_ACTIVE.getName();
								}
							}
							if(isMiSettle){
								if(!"1".equals(treatmentStatusRes.getShebkzt())){//1-正常 2-挂失 3-注销
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("您的社保卡可能已封锁或已挂失，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MEDICAL_TREATMENT_BLOCK.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MEDICAL_TREATMENT_BLOCK.getName();
								}
							}
							PersonInfoRes personInfoRes = null;
							if(isMiSettle){
								//查询个人信息获取个人编号等信息
								personInfoRes = payUserServiceImpl.getMiPersonalInfo(actPerson.getActId(), siCardNo, fixedHospCode);
								if(null == personInfoRes){
									isMiSettle = false;
									if(logger.isInfoEnabled()){
										logger.info("获取医保个人信息失败(盘古)，转为自费订单");
									}
									paySelf = totalPayMoney;
									usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
									this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_GET_SOCIAL_CARD_INFO_ERROR.getCodeString();
									interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_GET_SOCIAL_CARD_INFO_ERROR.getName();
								}else {
									siPCode = String.valueOf(personInfoRes.getPersonID()).trim();
									cycId = String.valueOf(personInfoRes.getCycid()).trim();
								}
							}
							if(isMiSettle){
								if(DimDictEnum.MEDICAL_CLASS_PUTONGMENZHEN.getCodeString().equals(ylType) ||
										DimDictEnum.MEDICAL_CLASS_MANXINGBING.getCodeString().equals(ylType)){//普通门诊or门诊规定病种(慢性病)，需调用MTS标化
									if(logger.isInfoEnabled()){
										logger.info("jdjbID=" + jdjbID + ",jdjbName=" + jdjbName);
									}
									if(ValidateUtils.isNotEmpty(jdjbID) && ValidateUtils.isNotEmpty(jdjbName)){
										//查询慢性病信息
										MxbInfoReq mxbInfoReq = new MxbInfoReq();
										mxbInfoReq.setPersonID(siPCode);//个人编号
										if(logger.isInfoEnabled()){
											logger.info("查询用户慢性病信息，请求参数：fixedHospCode=" + fixedHospCode + ",cycId=" + cycId + ",mxbInfoReq=" + JSON.toJSONString(mxbInfoReq));
										}
										MxbRes mxbRes = commInfoServiceImpl.queryChronicDisease(fixedHospCode, cycId, mxbInfoReq);
										if(logger.isInfoEnabled()){
											logger.info("响应报文：mxbRes=" + (mxbRes == null ? null : JSON.toJSONString(mxbRes)));
										}
										if(null == mxbRes || mxbRes.getRTNCode() != 1){
											resultMap.put("resultCode", Hint.PAY_ORDER_13106.getCodeString());
											resultMap.put("resultDesc", Hint.PAY_ORDER_13106.getMessage());
											return resultMap;
										}
										Map<String, String> mxbMap = new HashMap<>();
										List<MxbRes.Mxb> mxbList = mxbRes.getMxblist();//慢性病信息
										if(null != mxbList && mxbList.size() > 0){
											for(MxbRes.Mxb mxb : mxbList){
												mxbMap.put(mxb.getBingzbm(), mxb.getBingzmc());
											}
										}
										//MTS进行匹配诊断信息
										MtsParam mtsParam = new MtsParam();
										mtsParam.setVisitId(serialNumber);//就诊ID
										mtsParam.setVisitType("01");//就诊类型
										mtsParam.setDataSource(String.valueOf(workSp.getEntPrivCode()).trim());//数据来源 10002-开封中心医院
										mtsParam.setDataType("02");//聚类 02-诊断
										MtsParam.DiagInfo diagInfo = new MtsParam.DiagInfo();
										diagInfo.setCode(jdjbID);
										diagInfo.setDiag(jdjbName);
										mtsParam.setDiag(diagInfo);
										if(logger.isInfoEnabled()){
											logger.info("请求MTS标化诊断信息，请求参数：" + JSON.toJSONString(mtsParam));
										}
										MtsRespResult mtsRespResult = mTSServiceImpl.mts(mtsParam);
										if(logger.isInfoEnabled()){
											logger.info("响应报文：mtsRespResult=" + (mtsRespResult == null ? null : JSON.toJSONString(mtsRespResult)));
										}
										if(null == mtsRespResult || !"1".equals(mtsRespResult.getStatus())){//MTS诊断未全部匹配
											isMiSettle = false;
											if(logger.isInfoEnabled()){
												logger.info("MTS诊断未全部匹配，转为自费订单");
											}
											paySelf = totalPayMoney;
											usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
											this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
											interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MTS_NOT_MATCH.getCodeString();
											interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MTS_NOT_MATCH.getName();
										}else {
											List<MtsRespResult.MtsResult> mtsResultList = mtsRespResult.getResult();
											if(null == mtsResultList || mtsResultList.size() == 0){
												isMiSettle = false;
												if(logger.isInfoEnabled()){
													logger.info("MTS返回诊断结果信息为空，转为自费订单");
												}
												paySelf = totalPayMoney;
												usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
												this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
												interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MTS_NOT_MATCH.getCodeString();
												interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MTS_NOT_MATCH.getName();
											}
											if(isMiSettle){
												mxbID = "";
												int matchCount = 0;
												if(mxbMap.size() > 0){
													for(MtsRespResult.MtsResult mtsResult : mtsResultList){
														if(mxbMap.containsKey(mtsResult.getCode())){
															mxbID = mtsResult.getCode();
															jdjbID = mtsResult.getCode();
															jdjbName = mtsResult.getName();
															matchCount ++;
														}
													}
													if(matchCount > 1){
														if(logger.isInfoEnabled()){
															logger.info("MTS诊断匹配多个慢性病信息，拒绝医保结算");
														}
														resultMap.put("resultCode", Hint.PAY_ORDER_13110.getCodeString());
														resultMap.put("resultDesc", Hint.PAY_ORDER_13110.getMessage());
														return resultMap;
													}
												}
											}
										}
									}else {
										mxbID = "";
									}
									medicalClass = BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC;
								}else {//药店购药
									medicalClass = BaseDictConstant.MEDICAL_CLASS_BUYING_MEDICINE;
								}
							}
							if(isMiSettle){
								//调用医保预结算
								if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){//医院门诊
									if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){//职工
										TollsReq.MzToll mzToll = new TollsReq.MzToll();
										if(ValidateUtils.isEmpty(mxbID)){
											if(logger.isInfoEnabled()){
												logger.info("不存在慢性病 mxbID=" + mxbID + ",医疗类别：" + DimDictEnum.MEDICAL_CLASS_PUTONGMENZHEN.getName());
											}
											ylType = DimDictEnum.MEDICAL_CLASS_PUTONGMENZHEN.getCodeString();
										}else {
											ylType = DimDictEnum.MEDICAL_CLASS_MANXINGBING.getCodeString();
											if(logger.isInfoEnabled()){
												logger.info("存在慢性病 mxbID=" + mxbID + ",医疗类别：" + DimDictEnum.MEDICAL_CLASS_MANXINGBING.getName());
											}
										}
										mzToll.setYlType(ylType);//医疗类别
										mzToll.setPersonID(siPCode);//个人编号
										mzToll.setPerCardID(actPerson.getpCertNo().toUpperCase());//身份证号
										mzToll.setPerName(actPerson.getpName());//姓名
										mzToll.setSerialID(serialNumber);//门诊流水号
										mzToll.setVisDate(visDate.substring(0, 8));//就诊日期yyyymmdd
										mzToll.setJbr(Propertie.APPLICATION.value("miPayOperator"));//经办人
										mzToll.setOperDate(DateUtils.getNowDate("yyyyMMddHHmmss"));//经办日期yyyymmddhh24miss
										mzToll.setJhsyssType(jhsyssType);//计生手术类别
										mzToll.setMxbID(mxbID);//慢性病编码
										mzToll.setJdjbID(jdjbID);//诊断疾病编号
										mzToll.setJdjbName(jdjbName);//诊断疾病名称
										mzToll.setOfficeName(officeName);//科室名称
										mzToll.setDoctorID(doctorID);//医生编号
										mzToll.setDoctorName(doctorName);//医生姓名
										mzToll.setZhsybz(medicareUseFlag);//账户使用标志 0-不使用 1-使用
										mzToll.setTelephone(payUser.getPayUserCode());//持卡人手机号

										List<TollsReq.MzSfmx> sfmxs = new ArrayList<>();
										TollsReq.MzSfmx mzSfmx;
										for(Map<String, String> mediSettleDetailMap : mediSettleDetailList){
											mzSfmx = new TollsReq.MzSfmx();
											mzSfmx.setCfID(mediSettleDetailMap.get("cfID"));//处方ID
											if(!cfIds.contains(mzSfmx.getCfID())){
												cfIds.add(mzSfmx.getCfID());
											}
											mzSfmx.setSfType(mediSettleDetailMap.get("mediSfType"));//收费类别
											mzSfmx.setCenterID(mediSettleDetailMap.get("mediProjCode"));//医保中心项目ID
											mzSfmx.setYyCode(mediSettleDetailMap.get("hospProjCode"));//医院项目ID
											mzSfmx.setProjectName(mediSettleDetailMap.get("projectName"));//项目名称
											mzSfmx.setPrice(BigDecimal.valueOf(Double.valueOf(mediSettleDetailMap.get("price"))));//单价
											mzSfmx.setNum(BigDecimal.valueOf(Double.valueOf(mediSettleDetailMap.get("num"))));//数量
											mzSfmx.setSum(BigDecimal.valueOf(Double.valueOf(mediSettleDetailMap.get("sum"))));//金额
											mzSfmx.setUnit(mediSettleDetailMap.get("unit"));//单位
											mzSfmx.setNorms(mediSettleDetailMap.get("norms"));//规格
											mzSfmx.setDosage(mediSettleDetailMap.get("dosage"));//剂型
											mzSfmx.setDose(mediSettleDetailMap.get("dose"));//每次用量
											mzSfmx.setFrequency(mediSettleDetailMap.get("frequency"));//执行频次
											mzSfmx.setDays("");//执行天数
											mzSfmx.setFrom("");//产地
											sfmxs.add(mzSfmx);
										}

										TollsReq tollsReq = new TollsReq();
										tollsReq.setMzToll(mzToll);
										tollsReq.setSfmxs(sfmxs);
										String requestStr = "";
										String responseStr = "";
										if(logger.isInfoEnabled()){
											requestStr = "医院门诊医保预结算(职工)，请求参数：fixedHospCode=" + fixedHospCode + ",cycId=" + cycId + ",tollsReq=" + JSON.toJSONString(tollsReq);
											logger.info(requestStr);
										}
										MzYSfmxRes mzYSfmxRes = outpatientServiceImpl.executeMzYSfmx(fixedHospCode, cycId, tollsReq);
										String preClmRet = null;
										if(null != mzYSfmxRes){
											preClmRet = JSON.toJSONString(mzYSfmxRes);
										}
										if(logger.isInfoEnabled()){
											responseStr = "响应报文：mzYSfmxRes=" + preClmRet;
											logger.info(responseStr);
										}
										if(null == mzYSfmxRes || mzYSfmxRes.getRTNCode() != 1){
											if(logger.isInfoEnabled()){
												logger.info("医院门诊医保预结算(职工)出现异常，转为自费订单");
												//记录门诊预结算异常日志
												miPreSettleError.info(requestStr);
												miPreSettleError.info(responseStr);
												if("1".equals(String.valueOf(CacheUtils.getDimSysConfConfValue("EBAO_MAIL_SEND_FLAG")).trim())){
													final String requestStrTemp = requestStr;
													final String responseStrTemp = responseStr;
													mailService.execute(new Runnable() {
														public void run() {
															try{
																MailUtil.sendMail("开封中心医院医保预结算异常", requestStrTemp.concat("\n").concat(responseStrTemp));
															}catch (Exception e) {
																logger.info("发送邮件出现异常:" + e.getMessage());
															}
														}
													});
												}
											}
											isMiSettle = false;
											paySelf = totalPayMoney;
											usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
											this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
											interactiveCode = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_PRE_SETTLE_EXCEPTION.getCodeString();
											interactiveDesc = DimDictEnum.MEDICAL_SETTLE_INTERACTIVE_MI_PRE_SETTLE_EXCEPTION.getName();
										}
										if(isMiSettle){
											String zhpay = String.valueOf(mzYSfmxRes.getZhpay()).trim();
											if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
												mzYSfmxRes.setZhpay("0");
											}
											payPacct = BigDecimal.valueOf(Double.parseDouble(mzYSfmxRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
											//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
											String tcpay = String.valueOf(mzYSfmxRes.getTcpay()).trim();
											if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
												mzYSfmxRes.setTcpay("0");
											}
											String bigpay = String.valueOf(mzYSfmxRes.getBigpay()).trim();
											if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
												mzYSfmxRes.setBigpay("0");
											}
											String gwybz = String.valueOf(mzYSfmxRes.getGwybz()).trim();
											if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
												mzYSfmxRes.setGwybz("0");
											}
											String qybcjjpay = String.valueOf(mzYSfmxRes.getQybcpay()).trim();
											if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
												mzYSfmxRes.setQybcpay("0");
											}
											paySi = BigDecimal.valueOf(Double.parseDouble(mzYSfmxRes.getTcpay()))
													.add(BigDecimal.valueOf(Double.parseDouble(mzYSfmxRes.getBigpay())))
													.add(BigDecimal.valueOf(Double.parseDouble(mzYSfmxRes.getGwybz())))
													.add(BigDecimal.valueOf(Double.parseDouble(mzYSfmxRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
											String cashpay = String.valueOf(mzYSfmxRes.getCashpay()).trim();
											if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
												mzYSfmxRes.setCashpay("0");
											}
											paySelf = BigDecimal.valueOf(Double.parseDouble(mzYSfmxRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
											BigDecimal totalSettleMoney = payPacct.add(paySi).add(paySelf);
											if(logger.isInfoEnabled()){
												logger.info("totalPayMoney=" + totalPayMoney + ",totalSettleMoney=" + totalSettleMoney);
											}
											if(totalPayMoney.compareTo(totalSettleMoney) != 0){
												resultMap.put("resultCode", Hint.PAY_ORDER_13111.getCodeString());
												resultMap.put("resultDesc", Hint.PAY_ORDER_13111.getMessage());
												return resultMap;
											}

											Map<String, Object> settleDetailMap = new HashMap<>();
											settleDetailMap.put("siCardNo", siCardNo);//社保卡号
											settleDetailMap.put("hisTreatmentId", doctorFlowId);//HIS就诊流水号
											settleDetailMap.put("paymentTreatmentId", serialNumber);//支付系统生成的就诊流水号
											StringBuilder prescriptionIds = new StringBuilder(128);
											for(String cfId : cfIds){
												if(prescriptionIds.length() == 0){
													prescriptionIds.append(cfId);
												}else {
													prescriptionIds.append(",").append(cfId);
												}
											}
											settleDetailMap.put("prescriptionId", prescriptionIds.toString());//处方ID
											settleDetailMap.put("treatmentCostType", treatmentCostType);//类型 1-药品、2-非药品
											settleDetailMap.put("overallAreaCode", personInfoRes.getSbjgCode());//统筹区号
											settleDetailMap.put("medicalType", ylType);//医疗类别
											settleDetailMap.put("medicalTreatType", personInfoRes.getYldyType());//医疗人员类别
											settleDetailMap.put("personId", personInfoRes.getPersonID());//个人编号
											settleDetailMap.put("unitName", personInfoRes.getUnitName());//单位名称
											settleDetailMap.put("statementStandId", mxbID);//结算单标化记录编码(慢性病编码)

											tdMiPara = new TdMiPara();
											Map<String, Object> preClmParaMap = new HashMap<>();
											preClmParaMap.put("fixedHospCode", fixedHospCode);
											preClmParaMap.put("tollsReq", tollsReq);
											tdMiPara.setPreClmPara(JSON.toJSONString(preClmParaMap));//医保预结算提交参数
											tdMiPara.setPreClmRet(preClmRet);//医保预结算返回
											tdMiPara.setClmRet(JsonStringUtils.objectToJsonString(settleDetailMap));
											tdMiPara.setUpdTime(new Date());
											tdMiPara.setCrtTime(new Date());
										}
									}else {//居民
										//暂不考虑
									}
								}else {//药店购药
									if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){//职工

									}else {//居民
										//暂不考虑
									}
								}

								if(isMiSettle){
									this.buildMedicarePayTypes(medicarePayTypes, actPerson.getpName(), siCardNo);
									if(paySelf.compareTo(BigDecimal.ZERO) != 0){
										this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
									}

									String miSettleFlag;//0-纯自费(不使用医保) 1-预结算分账后纯自费 2-预结算分账后使用医保
									if(paySelf.compareTo(BigDecimal.ZERO) > 0 && (payPacct.compareTo(BigDecimal.ZERO) > 0 || paySi.compareTo(BigDecimal.ZERO) > 0)){
										usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCodeString();
										miSettleFlag = "2";
									}else{
										if(payPacct.compareTo(BigDecimal.ZERO) <= 0 && paySi.compareTo(BigDecimal.ZERO) <= 0 && paySelf.compareTo(BigDecimal.ZERO) > 0){
											usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();
											miSettleFlag = "1";
										}else{
											usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCodeString();
											miSettleFlag = "2";
										}
									}
									if(null != tdMiPara && ValidateUtils.isNotEmpty(tdMiPara.getClmRet())){
										Map settleDetailMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
										settleDetailMap.put("miSettleFlag", miSettleFlag);
										tdMiPara.setClmRet(JsonStringUtils.objectToJsonString(settleDetailMap));
									}
								}
							}
						}else {//外部业务(目前暂未考虑具体业务，暂定纯商业支付)
							if(logger.isInfoEnabled()){
								logger.info("当前为外部业务，目前仅支持纯商业支付");
							}
							paySelf = totalPayMoney;
							usePayChannelType = DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString();//商业支付
							this.buildBusinessPayTypes(businessPayTypes, ppFundType, workFundId, workSpActId, workSp.getFundId());
						}
					}
				}
			}
		}

		tdOrder = new TdOrder();
		if(terminalType.equals(DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCodeString())){
			tdOrder.setTdId(Long.valueOf(webTerminalOrderId));//支付订单号
		}else {
			tdOrder.setTdId(Long.valueOf(IdCreatorUtils.getPayOrderId()));//支付订单号
		}
		tdOrder.setTdOrder(BigDecimal.valueOf(BaseDictConstant.TRANSACTION_SERIAL_NUMBER).setScale(0, BigDecimal.ROUND_HALF_UP));//交易序号
		tdOrder.setMicId(micId);//医保项目ID
		tdOrder.setTdTypeId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_TYPE_PAYMENT.getCode())));//交易类型
		tdOrder.setTdBusiTypeId(Short.valueOf(transBusiType));//交易业务类型
		tdOrder.setBusiDetTypeId(transSecBusiType);
		tdOrder.setChanActId(channelSp.getActId());//渠道账户ID
		tdOrder.setChanAppid(appId);//渠道账户APPID
		tdOrder.setTdOperChanTypeId(Short.valueOf(terminalType));//交易操作通道类别
		tdOrder.setFromActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_PRIVATE.getCode())));//付款账户类型
		tdOrder.setFromActId(Long.valueOf(accountId));//付款账户ID
		if(null != payUser){
			tdOrder.setPayUserId(payUser.getPayUserId());//付款用户ID
		}
		tdOrder.setToActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_MERCHANT.getCode())));//收款账户类型
		tdOrder.setToActId(toActId);//收款账户ID
		tdOrder.setDispSpActId(workSp.getActId());//显示商户账户ID(合作商户)
		tdOrder.setDispSpName(workSp.getEntName());//显示商户名称
		tdOrder.setGoodsDesc(goodsDesc);//商品描述
		tdOrder.setFundId(workFundId);//资金平台ID
		tdOrder.setCbAddr(callbackUrl);//商户后台系统的回调地址
		tdOrder.setServChrgRatio(BigDecimal.ZERO);//本次交易费率
		tdOrder.setServChrgAmt(BigDecimal.ZERO);//本次交易手续费
		tdOrder.setPayTot(totalPayMoney);//交易总金额
		tdOrder.setRecvCur(totalPayMoney);//本次实收金额
		tdOrder.setOrigOrdCode(merOrderId);//渠道商户订单号
		tdOrder.setSpSubSysCode(systemId);//商户子系统编号

		String payOrderExpire = Propertie.APPLICATION.value("payOrder.expire");
		if(ValidateUtils.isEmpty(merOrderExpire) || Integer.parseInt(merOrderExpire) > Integer.parseInt(payOrderExpire)){
			merOrderExpire = payOrderExpire;
		}
		tdOrder.setTdLimitSec(Integer.valueOf(merOrderExpire));

		tdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode())));//交易状态
		tdOrder.setTdDate(DateUtils.getNowDate("yyyyMMdd"));//交易日期
		tdOrder.setTdStartTime(new Date());//申请交易时间
		tdOrder.setConfirmStatId(Short.valueOf(String.valueOf(DimDictEnum.CONFIRM_STAT_WAIT_CHECK.getCode())));//对账状态
		tdOrder.setCrtTime(new Date());//创建时间

		TdOrdExtPay tdOrdExtPay = new TdOrdExtPay();
		tdOrdExtPay.setTdId(tdOrder.getTdId());
		tdOrdExtPay.setPayTypeId(Short.valueOf(usePayChannelType));//支付类型
		if(DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCodeString().equals(usePayChannelType)){
			tdOrdExtPay.setTdBusiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCodeString()));//商业支付状态
			tdOrdExtPay.setTdSiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCodeString()));//社保支付状态
		}else if(DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString().equals(usePayChannelType)){
			tdOrdExtPay.setTdBusiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCodeString()));//商业支付状态
			tdOrdExtPay.setTdSiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCodeString()));//社保支付状态
		}else{
			tdOrdExtPay.setTdBusiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCodeString()));//商业支付状态
			tdOrdExtPay.setTdSiStatId(Short.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCodeString()));//社保支付状态
		}
		if(null != actPerson){
			tdOrdExtPay.setpIdNo(actPerson.getpCertNo().toUpperCase());//身份证号
		}
		if(ValidateUtils.isNotEmpty(siCardNo)){
			tdOrdExtPay.setpSiCardNo(siCardNo);//社保卡号
		}
		if(ValidateUtils.isNotEmpty(siPCode)){
			tdOrdExtPay.setSiPCode(siPCode);//社保个人编号
		}
		tdOrdExtPay.setPaySelf(paySelf);//个人支付金额
		tdOrdExtPay.setPaySi(paySi);//社保报销金额(统筹基金)
		tdOrdExtPay.setPayPacct(payPacct);//个人账户支付金额(医保个账)
		tdOrdExtPay.setTdRetFlag(Short.valueOf("0"));//是否被退款 1-是 0-否
		tdOrdExtPay.setAccRetAmt(BigDecimal.ZERO);//累计退款金额
		tdOrdExtPay.setPayPropA(spFundCode);//借用字段，保存合作商户资金平台编码
		tdOrdExtPay.setPayPropB(medicalClass);//借用字段，保存医疗类别 1-医院门诊 2-药店购药
		tdOrdExtPay.setPayPropC(insuredPersonType);//借用字段，保存参保人员类别 1-职工 2-居民
		if(ValidateUtils.isNotEmpty(doctorFlowId)){
			tdOrdExtPay.setHospSeqCode(serialNumber);//支付平台生成就诊流水号
			tdOrdExtPay.setRemark(doctorFlowId);//医院门诊流水号
		}
		if(logger.isInfoEnabled()){
			logger.info("保存交易单&支付扩展信息入库 payOrderId=" + tdOrder.getTdId() + ",merOrderExpire=" + merOrderExpire);
		}
		if(null != tdMiPara){
			tdMiPara.setTdId(tdOrder.getTdId());
		}
		tdOrderServiceImpl.saveTdOrderAndExtPayInfo(tdOrder, tdOrdExtPay, tdMiPara);
		if(logger.isInfoEnabled()){
			logger.info("<<保存成功");
		}

		this.setEbaoPaymentFlowIdInRedis(tdOrder.getTdId().toString(), tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString(), merOrderExpire);

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("accountId", accountId);//用户账户编号
		resultMap.put("payTimes", merOrderExpire);//剩余支付秒数
		resultMap.put("payOrderId", tdOrder.getTdId().toString());//支付平台订单号
		resultMap.put("merOrderId", merOrderId);//商户平台订单号
		resultMap.put("merchantName", channelSp.getEntName());
		resultMap.put("serviceLineType", serviceLineType);
		resultMap.put("goodsDesc", goodsDesc);
		resultMap.put("skDesc", workSp.getEntName());
		resultMap.put("workSpActId", workSpActId);
		resultMap.put("transBusiType", transBusiType);
		resultMap.put("transSecBusiType", transSecBusiType);//交易二级业务类型
		resultMap.put("totalPayMoney", totalPayMoney.toString());//支付总额
		resultMap.put("medicarePersonPayMoney", payPacct.toString());//医保个人账户支付金额
		resultMap.put("insuranceFundPayMoney", paySi.toString());//统筹基金支付金额
		resultMap.put("personalPayMoney", paySelf.toString());//个人支付金额
		resultMap.put("usePayChannelType", usePayChannelType);//1-商业支付 2-社保支付 3-混合支付
		resultMap.put("businessPayTypes", JSON.toJSONString(businessPayTypes));//商业支付方式
		resultMap.put("medicarePayTypes", JSON.toJSONString(medicarePayTypes));//医保支付方式
		resultMap.put("buttonShowName", buttonShowName);
		resultMap.put("goodsExpenseDetail", goodsExpenseDetail);//商品报销详情
		resultMap.put("socialSecurityBindId", "");
		resultMap.put("createOrderTime", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyy-MM-dd HH:mm:ss"));
		resultMap.put("interactiveCode", interactiveCode);
		resultMap.put("interactiveDesc", interactiveDesc);
		String miPayGetGpsFlag = "0";//1-强制获取 0-非强制获取 默认0
		if(!usePayChannelType.equals(DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCodeString())){
			miPayGetGpsFlag = String.valueOf(CacheUtils.getDimSysConfConfValue("MI_PAY_GET_GPS_FLAG")).trim();
			if(!"1".equals(miPayGetGpsFlag)){
				miPayGetGpsFlag = "0";
			}
		}
		resultMap.put("getGpsFlag", miPayGetGpsFlag);
		return resultMap;
	}

	/**
	 * 变更商业支付通道
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> changeFundId(Map<String, Object> inputMap) throws Exception {
		return tdOrderServiceImpl.changeFundId(inputMap);
	}

	private void buildBusinessPayTypes(List<Map<String, String>> businessPayTypes, PpFundType ppFundType, String workFundId, String workActId, String workSpOrigFundId) throws Exception {
		if(BaseDictConstant.MERCHANT_FUND_ID_MULTIPLE.equalsIgnoreCase(workSpOrigFundId)){
			SortedMap<String, PpFundPlatform> sortedMap = CacheUtils.getPpFundPlatformMapByWorkActId(workActId);
			if(null == sortedMap || sortedMap.size() == 0){
				return;
			}
			for(PpFundPlatform ppFundPlatform : sortedMap.values()){
				businessPayTypes.add(this.getBusiPayTypesMap(CacheUtils.getPpFundType(ppFundPlatform.getPpFundTypeId()), ppFundPlatform.getFundId()));
			}
		}else {
			businessPayTypes.add(this.getBusiPayTypesMap(ppFundType, workFundId));
		}
	}

	private Map<String, String> getBusiPayTypesMap(PpFundType ppFundType, String workFundId){
		Map<String, String> businessPayTypesMap = new HashMap<>();
		String channelCode = ppFundType.getPpFundTypeId();
		if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(channelCode)){
			channelCode = BaseDictConstant.PP_FUND_TYPE_ID_ALI;
		}
		businessPayTypesMap.put("channelCode", channelCode);
		businessPayTypesMap.put("fundId", workFundId);
		businessPayTypesMap.put("channelName", ppFundType.getDispName());
		businessPayTypesMap.put("channelUrl", "");
		String imageId = String.valueOf(ppFundType.getImageId()).trim();
		String iconUrl = "";
		if(ValidateUtils.isNotEmpty(imageId)){
			iconUrl = Propertie.APPLICATION.value("ebaoPaySystem.bankIconUrl").replace("{bankImageId}", imageId);
		}
		businessPayTypesMap.put("iconUrl", iconUrl);
		return businessPayTypesMap;
	}

	private void buildMedicarePayTypes(List<Map<String, String>> medicarePayTypes, String userName, String siCardNo) throws Exception {
		String iconUrl = Propertie.APPLICATION.value("ebaoPaySystem.bankIconUrl").replace("{bankImageId}", "sipay");
		Map<String, String> medicarePayTypesMap = new HashMap<>();
		medicarePayTypesMap.put("bindCardId", String.valueOf(System.currentTimeMillis()));
		medicarePayTypesMap.put("bindCardName", "社保卡(".concat("*".concat(userName.substring(1))).concat(" 尾号").concat(siCardNo.substring(siCardNo.length() - 4)).concat(")"));
		medicarePayTypesMap.put("iconUrl", iconUrl);
		medicarePayTypesMap.put("isMain", "1");
		String miIdentityValidateMode = String.valueOf(CacheUtils.getDimSysConfConfValue("MI_IDENTITY_VALIDATE_MODE")).trim();//医保身份验证方式(0-关闭 1-人脸识别 2-短信验证)
		if(ValidateUtils.isEmpty(miIdentityValidateMode)){
			miIdentityValidateMode = "1";
		}
		medicarePayTypesMap.put("miIdentityValidateMode", miIdentityValidateMode);
		medicarePayTypes.add(medicarePayTypesMap);
	}

	/**
	 * 医保结算数据校验
	 * @param orderExtendInfo 订单扩展信息
	 * @param totalPayMoney 订单总额
	 * @return 解析结果
     */
	@SuppressWarnings("unchecked")
	private Map<String, Object> checkMediSettleData(String orderExtendInfo, BigDecimal totalPayMoney){
		Map<String, Object> miSettleOrderExtendInfoMap = new HashMap<>();
		if(ValidateUtils.isEmpty(orderExtendInfo)){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "orderExtendInfo"));
			return miSettleOrderExtendInfoMap;
		}
		Map orderExtendInfoMap = JSON.parseObject(orderExtendInfo, Map.class);
		if(null == orderExtendInfoMap || orderExtendInfoMap.size() == 0){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "orderExtendInfo"));
			return miSettleOrderExtendInfoMap;
		}

		String doctorFlowId = String.valueOf(orderExtendInfoMap.get("doctorFlowId")).trim();//门诊流水号
		if(ValidateUtils.isEmpty(doctorFlowId)){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "doctorFlowId"));
			return miSettleOrderExtendInfoMap;
		}

		String fixedHospCode = String.valueOf(orderExtendInfoMap.get("fixedHospCode")).trim();//定点医院编码
		if(ValidateUtils.isEmpty(fixedHospCode)){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "fixedHospCode"));
			return miSettleOrderExtendInfoMap;
		}

		String visDate = String.valueOf(orderExtendInfoMap.get("visDate")).trim();//就诊日期
		if(ValidateUtils.isEmpty(visDate)){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "visDate"));
			return miSettleOrderExtendInfoMap;
		}

		String ylType = String.valueOf(orderExtendInfoMap.get("ylType")).trim();//医疗类别 11-普通门诊 14-药店购药 16-门诊规定病种(慢性病) 45-计划生育手术(门诊)
		if(ValidateUtils.isEmpty(ylType)){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "ylType"));
			return miSettleOrderExtendInfoMap;
		}
		if(!(DimDictEnum.MEDICAL_CLASS_PUTONGMENZHEN.getCodeString().equals(ylType) ||
				DimDictEnum.MEDICAL_CLASS_MANXINGBING.getCodeString().equals(ylType))) {//普通门诊or门诊规定病种(慢性病)
			miSettleOrderExtendInfoMap.put("resultCode", Hint.PAY_ORDER_13108.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.PAY_ORDER_13108.getMessage());
			return miSettleOrderExtendInfoMap;
		}

		String mxbID = String.valueOf(orderExtendInfoMap.get("mxbID")).trim();//慢性病编码
		if(ValidateUtils.isEmpty(mxbID)){
			mxbID = "";
		}

		String jhsyssType = String.valueOf(orderExtendInfoMap.get("jhsyssType")).trim();//计生手术类别
		if(ValidateUtils.isEmpty(jhsyssType)){
			jhsyssType = "";
		}

		String jdjbID = String.valueOf(orderExtendInfoMap.get("jdjbID")).trim();//诊断疾病编号
		if(DimDictEnum.MEDICAL_CLASS_MANXINGBING.getCodeString().equals(ylType)) {//门诊规定病种(慢性病)
			if(ValidateUtils.isEmpty(jdjbID)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "jdjbID"));
				return miSettleOrderExtendInfoMap;
			}
		}else {
			if(ValidateUtils.isEmpty(jdjbID)){
				jdjbID = "";
			}
		}

		String jdjbName = String.valueOf(orderExtendInfoMap.get("jdjbName")).trim();//诊断疾病名称
		if(DimDictEnum.MEDICAL_CLASS_MANXINGBING.getCodeString().equals(ylType)) {//门诊规定病种(慢性病)
			if(ValidateUtils.isEmpty(jdjbName)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "jdjbName"));
				return miSettleOrderExtendInfoMap;
			}
		}else {
			if(ValidateUtils.isEmpty(jdjbName)){
				jdjbName = "";
			}
		}

		String officeName = String.valueOf(orderExtendInfoMap.get("officeName")).trim();//科室名称
		if(ValidateUtils.isEmpty(officeName)){
			officeName = "";
		}

		String doctorID = String.valueOf(orderExtendInfoMap.get("doctorID")).trim();//医生编号
		if(ValidateUtils.isEmpty(doctorID)){
			doctorID = "";
		}

		String doctorName = String.valueOf(orderExtendInfoMap.get("doctorName")).trim();//医生姓名
		if(ValidateUtils.isEmpty(doctorName)){
			doctorName = "";
		}

		String treatmentCostType = String.valueOf(orderExtendInfoMap.get("treatmentCostType")).trim();//类型
		if(ValidateUtils.isEmpty(treatmentCostType)){
			treatmentCostType = "";
		}

		String busiOrderDetailInfo = String.valueOf(orderExtendInfoMap.get("busiOrderDetailInfo")).trim();//业务订单收费明细
		if(ValidateUtils.isEmpty(busiOrderDetailInfo)){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "busiOrderDetailInfo"));
			return miSettleOrderExtendInfoMap;
		}
		List detailList = JSON.parseObject(busiOrderDetailInfo, List.class);
		if(null == detailList || detailList.size() == 0){
			if(logger.isInfoEnabled()){
				logger.info("busiOrderDetailInfo转换为List出现异常");
			}
			miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "busiOrderDetailInfo"));
			return miSettleOrderExtendInfoMap;
		}
		String cfID;
		String cfDate;
		String hospSfType;
		String mediSfType;
		String hospProjCode;
		String mediProjCode;
		String projectName;
		String price;
		String num;
		String sum;
		String unit;
		String norms;
		String dosage;
		String dose;
		String frequency;
		Map<String, String> mediSettleDetailMap;
		List<Map<String, String>> mediSettleDetailList = new ArrayList<>();
		Map<String, Object> detailMap;
		BigDecimal totalCfMoney = BigDecimal.ZERO;//处方累计总额
		for(Object object : detailList){
			detailMap = (Map<String, Object>) object;
			cfID = String.valueOf(detailMap.get("cfID")).trim();//处方ID
			if(ValidateUtils.isEmpty(cfID)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cfID"));
				return miSettleOrderExtendInfoMap;
			}

			cfDate = String.valueOf(detailMap.get("cfDate")).trim();//处方日期
			if(ValidateUtils.isEmpty(cfDate)){
				cfDate = "";
			}

			hospSfType = String.valueOf(detailMap.get("hospSfType")).trim();//医院收费类别
			if(ValidateUtils.isEmpty(hospSfType)){
				hospSfType = "";
			}

			mediSfType = String.valueOf(detailMap.get("mediSfType")).trim();//医保收费类别(药店购药需要通过医保目录对应)
			if(ValidateUtils.isEmpty(mediSfType)){
				mediSfType = "";
			}

			hospProjCode = String.valueOf(detailMap.get("hospProjCode")).trim();//医院项目编码
			if(ValidateUtils.isEmpty(hospProjCode)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "hospProjCode"));
				return miSettleOrderExtendInfoMap;
			}

			mediProjCode = String.valueOf(detailMap.get("mediProjCode")).trim();//医保项目编码
			if(ValidateUtils.isEmpty(mediProjCode)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "mediProjCode"));
				return miSettleOrderExtendInfoMap;
			}

			projectName = String.valueOf(detailMap.get("projectName")).trim();//项目名称
			if(ValidateUtils.isEmpty(projectName)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "projectName"));
				return miSettleOrderExtendInfoMap;
			}

			price = String.valueOf(detailMap.get("price")).trim();//单价
			if(ValidateUtils.isEmpty(price)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "price"));
				return miSettleOrderExtendInfoMap;
			}
			if(!ValidateUtils.isMoney(price, false)){//单价必须大于0
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "price"));
				return miSettleOrderExtendInfoMap;
			}

			num = String.valueOf(detailMap.get("num")).trim();//数量
			if(ValidateUtils.isEmpty(num)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "num"));
				return miSettleOrderExtendInfoMap;
			}
			if(!ValidateUtils.isPositiveInteger(num) || Long.parseLong(num) == 0){//数量必须大于0
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "num"));
				return miSettleOrderExtendInfoMap;
			}

			sum = String.valueOf(detailMap.get("sum")).trim();//金额=单价*数量
			if(ValidateUtils.isEmpty(sum)){
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "sum"));
				return miSettleOrderExtendInfoMap;
			}
			if(!ValidateUtils.isMoney(sum, false)){//金额必须大于0
				miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "sum"));
				return miSettleOrderExtendInfoMap;
			}
			totalCfMoney = totalCfMoney.add(BigDecimal.valueOf(Double.valueOf(sum)));

			unit = String.valueOf(detailMap.get("unit")).trim();//单位
			if(ValidateUtils.isEmpty(unit)){
				unit = "";
			}

			norms = String.valueOf(detailMap.get("norms")).trim();//规格
			if(ValidateUtils.isEmpty(norms)){
				norms = "";
			}

			dosage = String.valueOf(detailMap.get("dosage")).trim();//剂型
			if(ValidateUtils.isEmpty(dosage)){
				dosage = "";
			}

			dose = String.valueOf(detailMap.get("dose")).trim();//每次用量
			if(ValidateUtils.isEmpty(dose)){
				dose = "";
			}

			frequency = String.valueOf(detailMap.get("frequency")).trim();//执行频次
			if(ValidateUtils.isEmpty(frequency)){
				frequency = "";
			}

			mediSettleDetailMap = new HashMap<>();
			mediSettleDetailMap.put("cfID", cfID);
			mediSettleDetailMap.put("cfDate", cfDate);
			mediSettleDetailMap.put("hospSfType", hospSfType);
			mediSettleDetailMap.put("mediSfType", mediSfType);
			mediSettleDetailMap.put("hospProjCode", hospProjCode);
			mediSettleDetailMap.put("mediProjCode", mediProjCode);
			mediSettleDetailMap.put("projectName", projectName);
			mediSettleDetailMap.put("price", price);
			mediSettleDetailMap.put("num", num);
			mediSettleDetailMap.put("sum", sum);
			mediSettleDetailMap.put("unit", unit);
			mediSettleDetailMap.put("norms", norms);
			mediSettleDetailMap.put("dosage", dosage);
			mediSettleDetailMap.put("dose", dose);
			mediSettleDetailMap.put("frequency", frequency);
			mediSettleDetailList.add(mediSettleDetailMap);
		}
		if(logger.isInfoEnabled()){
			logger.info("totalPayMoney=" + totalPayMoney + ",totalCfMoney=" + totalCfMoney);
		}
		if(totalPayMoney.compareTo(totalCfMoney) != 0){
			miSettleOrderExtendInfoMap.put("resultCode", Hint.PAY_ORDER_13111.getCodeString());
			miSettleOrderExtendInfoMap.put("resultDesc", Hint.PAY_ORDER_13111.getMessage());
			return miSettleOrderExtendInfoMap;
		}
		miSettleOrderExtendInfoMap.put("doctorFlowId", doctorFlowId);
		miSettleOrderExtendInfoMap.put("fixedHospCode", fixedHospCode);
		miSettleOrderExtendInfoMap.put("visDate", visDate);
		miSettleOrderExtendInfoMap.put("ylType", ylType);
		miSettleOrderExtendInfoMap.put("mxbID", mxbID);
		miSettleOrderExtendInfoMap.put("jhsyssType", jhsyssType);
		miSettleOrderExtendInfoMap.put("jdjbID", jdjbID);
		miSettleOrderExtendInfoMap.put("jdjbName", jdjbName);
		miSettleOrderExtendInfoMap.put("officeName", officeName);
		miSettleOrderExtendInfoMap.put("doctorID", doctorID);
		miSettleOrderExtendInfoMap.put("doctorName", doctorName);
		miSettleOrderExtendInfoMap.put("treatmentCostType", treatmentCostType);
		miSettleOrderExtendInfoMap.put("mediSettleDetailList", mediSettleDetailList);

		miSettleOrderExtendInfoMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		miSettleOrderExtendInfoMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return miSettleOrderExtendInfoMap;
	}

	/**
	 * 纯确认支付--医保支付
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> confirmPayment(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("纯医保确认支付TdOrderServiceGWImpl --> confirmPayment 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<>();
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付平台订单号
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//支付通行证
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID

		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		Map<String, Object> checkOrderMap = tdOrderServiceImpl.checkOrderIsValid(tdOrder, accountId);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
			return checkOrderMap;
		}
		TdOrdExtPay tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(null == tdOrdExtPay){
			resultMap.put("resultCode", Hint.PAY_ORDER_13085_ORDEREXT_NOT_EXIST.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13085_ORDEREXT_NOT_EXIST.getMessage());
			return resultMap;
		}
		TdMiPara tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
		if(null == tdMiPara){
			if(logger.isInfoEnabled()){
				logger.info("tdId=" + tdOrder.getTdId() + " TdMiPara相关信息为空");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13085_ORDEREXT_NOT_EXIST.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13085_ORDEREXT_NOT_EXIST.getMessage());
			return resultMap;
		}
		//判断支付方式:1.商业支付；2.社保支付；3.混合支付
		if(tdOrdExtPay.getPayTypeId() != DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13086_PAY_TYPE_ID_NOT_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13086_PAY_TYPE_ID_NOT_INVALID.getMessage());
			return resultMap;
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getMessage());
			return resultMap;
		}
		Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
		if(null == redisPayToken || redisPayToken.size() == 0){
			resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
			resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
			return resultMap;
		}
		if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
			resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}

		/**********************************调用医保确认支付 begin*************************************/
		String tdStatId = "";
		String siRetCode = "";
		String siRetDesc = "";
		String siTdCode = "";
		String medicalClass = String.valueOf(tdOrdExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
		String insuredPersonType = String.valueOf(tdOrdExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
		String clmPara = "";
		String clmRet = null;
		if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
			if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
				Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
				if(null == preClmParaMap || preClmParaMap.size() == 0){
					if(logger.isInfoEnabled()){
						logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
					}
					resultMap.put("resultCode", Hint.PAY_ORDER_13109.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13109.getMessage());
					return resultMap;
				}
				PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(Long.valueOf(accountId), tdOrdExtPay.getpSiCardNo(), preClmParaMap.get("fixedHospCode").toString());
				if(null == personInfoRes){
					resultMap.put("resultCode", Hint.PAY_ORDER_13101.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13101.getMessage());
					return resultMap;
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
					if(logger.isInfoEnabled()){
						logger.info("医院门诊医保结算(职工)失败");
					}
					tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
					siRetCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
					siRetDesc = "医院门诊医保结算(职工)失败";
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
						logger.info("预结算：payPacct=" + tdOrdExtPay.getPayPacct() + ",paySi=" + tdOrdExtPay.getPaySi() + ",paySelf=" + tdOrdExtPay.getPaySelf() +
						" 结算：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
					}
					siTdCode = String.valueOf(mzSfmxRes.getDealID()).trim();
					if(tdOrdExtPay.getPayPacct().compareTo(payPacct) == 0 &&
					   tdOrdExtPay.getPaySi().compareTo(paySi) == 0 &&
					   tdOrdExtPay.getPaySelf().compareTo(paySelf) == 0){
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)成功");
						}
						tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
						siRetCode = "0";
						siRetDesc = "医院门诊医保结算(职工)成功";
					}else {
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)失败，预结算与结算结果不一致");
						}
						tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
						siRetCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
						siRetDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

						//进行结算撤销处理
						if(logger.isInfoEnabled()){
							logger.info("医院门诊医保结算(职工)失败，进行结算撤销处理");
						}
						Map<String, Object> refundMap = new HashMap<>();
						refundMap.put("appId", tdOrder.getChanAppid());
						refundMap.put("oriTdId", tdOrder.getTdId().toString());
						refundMap.put("merOrderId", tdOrder.getFundId().concat(String.valueOf(System.currentTimeMillis())));
						refundMap.put("cause", "Settlement Failure Medical Refund");
						refundMap.put("transBusiType", tdOrder.getTdBusiTypeId().toString());
						refundMap.put("terminalType", tdOrder.getTdOperChanTypeId().toString());
						refundMap.put("systemId", tdOrder.getSpSubSysCode());
						refundMap.put("allowRefund", "true");
						Map<String, Object> returnMap = payOrderFullRefundInterfaceImpl.doService(refundMap);
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
		/**********************************调用医保确认支付 end*************************************/

		/****************************更新交易单&交易扩展表信息 begin**********************************/
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易订单&支付扩展信息");
		}
		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setTdEndTime(new Date());
		updTdOrder.setErrCode("支付结果|".concat(siRetCode));
		updTdOrder.setUpdTime(new Date());
		updTdOrder.setTdStatId(Short.valueOf(tdStatId));

		TdOrdExtPay updTdOrdExtPay = new TdOrdExtPay();
		updTdOrdExtPay.setTdId(tdOrder.getTdId());//交易单ID
		updTdOrdExtPay.setTdSiStatId(Short.valueOf(tdStatId));
		updTdOrdExtPay.setSiTdCode(siTdCode);//社保交易流水号
		updTdOrdExtPay.setSiRetCode(siRetCode);
		updTdOrdExtPay.setSiRetDesc(siRetDesc);
		updTdOrdExtPay.setUpdTime(new Date());
		tdOrderServiceImpl.updateTdOrderAndExtPayInfo(updTdOrder, updTdOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

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
		/****************************更新交易单&交易扩展表信息 end**********************************/

		/****************************医保支付成功通知业务线 begin**********************************/
		if(tdStatId.equals(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString()) || tdStatId.equals(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString())){
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("payOrderId", payOrderId);
			dataMap.put("bank", "");
			dataMap.put("lastNo", "");
			executorService.execute(new EbaoAsynchronousNotice(this, dataMap));
		}
		/****************************医保支付成功通知业务线 end**********************************/

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("payOrderId", tdOrder.getTdId().toString());
		resultMap.put("accountId", tdOrder.getFromActId());
		return resultMap;
	}

	/**
	 * 预支付（非YeePay方式）
	 * @param inputMap 输入Map
	 * @return 响应Map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> preparePay(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("TdOrderServiceGWImpl --> preparePay 输入参数：" + inputMap);
		}
		Map<String, Object> resultMap = new HashMap<>();
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付平台订单号
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String payToken = String.valueOf(inputMap.get("payToken")).trim();//支付通行证
		String ccbFlag = String.valueOf(inputMap.get("ccbFlag")).trim();//建行通道标识 可空 1-建行 2-其他行
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID
		String userIp = String.valueOf(inputMap.get("userIp")).trim();//终端用户IP
		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();//终端类型 11-Android 12-IOS 21-微信 31-WEB

		TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
		Map<String, Object> checkOrderMap = tdOrderServiceImpl.checkOrderIsValid(tdOrder, accountId);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
			return checkOrderMap;
		}
		if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13066_TRADE_STATUS_INVALID.getMessage());
			return resultMap;
		}
		Map<String, String> redisPayToken = payUserServiceGWImpl.getPayTokenInRedis(accountId);
		if(null == redisPayToken || redisPayToken.size() == 0){
			resultMap.put("resultCode", Hint.USER_11006_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11006_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		if(!hardwareId.equals(String.valueOf(redisPayToken.get("hardwareId")))){
			resultMap.put("resultCode", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11007_CURR_HARDID_NOTEQUALS_PAYTOKEN_HARDID.getMessage());
			return resultMap;
		}
		if(!payToken.equals(String.valueOf(redisPayToken.get("payToken")))){
			resultMap.put("resultCode", Hint.USER_11008_PAYTOKEN_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11008_PAYTOKEN_INVALID.getMessage());
			return resultMap;
		}
		Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
		if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
			return checkPayUserMap;
		}
		TdOrdExtPay tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(tdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13092.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13092.getMessage());
			return resultMap;
		}
		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(tdOrder.getFundId());
		if(null == ppFundPlatform){
			if(logger.isInfoEnabled()){
				logger.info("FundId=" + tdOrder.getFundId() + " 资金平台无效或不存在");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
			return resultMap;
		}
		if(BaseDictConstant.PP_FUND_TYPE_ID_EBZF.equals(ppFundPlatform.getPpFundTypeId())){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
			return resultMap;
		}
		ActSp channelSp = actSpServiceImpl.selectByActId(tdOrder.getChanActId());
		if(null == channelSp){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		if(channelSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}
		ActSp workSp = actSpServiceImpl.findByWorkSpActId(tdOrder.getDispSpActId());
		if(null == workSp){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13063_ACCOUNT_ID_NOT_NULL.getMessage());
			return resultMap;
		}
		if(workSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13064_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}
		if(null == workSp.getChanActId() || channelSp.getActId().longValue() != workSp.getChanActId().longValue()){
			resultMap.put("resultCode", Hint.WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.WORK_MERCHANT_13065_BELONGTO_CHANNEL_ACCOUNT_INVALID.getMessage());
			return resultMap;
		}
		if(logger.isInfoEnabled()){
			logger.info("资金平台类型：" + ppFundPlatform.getPpFundTypeId() + ",渠道商户收单模式：" + channelSp.getFundModelId());
		}
		if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_1.getCode()){
			resultMap.put("resultCode", Hint.PAY_ORDER_13097.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13097.getMessage());
			return resultMap;
		}

		/*********************调用第三方支付平台 begin************************/
		this.updEbaoPaymentFlowIdInRedis(tdOrder);
		String channelParams = "";
		boolean reqSuccess = false;
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> responseMap;
		BigDecimal payAmount = tdOrdExtPay.getPaySelf().setScale(2, BigDecimal.ROUND_HALF_UP);
		if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundPlatform.getPpFundTypeId()) ||
			BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundPlatform.getPpFundTypeId())){

			if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundPlatform.getPpFundTypeId())){
				if(channelSp.getFundModelId() != DimDictEnum.FUND_MODEL_ID_2.getCode()){
					resultMap.put("resultCode", Hint.PAY_ORDER_13097.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13097.getMessage());
					return resultMap;
				}
			}
			if(BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundPlatform.getPpFundTypeId())){
				if(channelSp.getFundModelId() != DimDictEnum.FUND_MODEL_ID_3.getCode()){
					resultMap.put("resultCode", Hint.PAY_ORDER_13097.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13097.getMessage());
					return resultMap;
				}
			}

			String spFundCode = "";
			if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_3.getCode()){
				spFundCode = String.valueOf(workSp.getSpFundCode()).trim();
				if(ValidateUtils.isEmpty(spFundCode)){
					resultMap.put("resultCode", Hint.PAY_ORDER_13098.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13098.getMessage());
					return resultMap;
				}
			}

			Integer payOrderSurplusValidTime = this.getPayOrderSurplusValidTime(tdOrder.getCrtTime(), tdOrder.getTdLimitSec());
			payOrderSurplusValidTime = payOrderSurplusValidTime / 60;//支付订单有效期（单位：分钟）
			if(payOrderSurplusValidTime < BaseDictConstant.YEEPAY_MIN_ORDER_EXP_DATE){
				payOrderSurplusValidTime = BaseDictConstant.YEEPAY_MIN_ORDER_EXP_DATE;
			}
			if(logger.isInfoEnabled()){
				logger.info("请求支付宝预支付，设置订单有效期(分钟)：" + payOrderSurplusValidTime);
			}
			BaseDTO baseDTO;
			if(terminalType.equals(DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCodeString())){
				AlipayImmediatePayParam alipayImmediatePayParam = new AlipayImmediatePayParam();
				alipayImmediatePayParam.setNotify_url(CacheUtils.getDimSysConfConfValue("ALIPAY_BG_CALLBACK_URL"));//服务器异步通知页面路径
				alipayImmediatePayParam.setReturn_url(CacheUtils.getDimSysConfConfValue("ALIPAY_FT_CALLBACK_URL"));//前台通知地址
				alipayImmediatePayParam.setOut_trade_no(tdOrder.getTdId().toString().concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));//商户网站唯一订单号
				alipayImmediatePayParam.setSubject(tdOrder.getGoodsDesc());//商品名称
				alipayImmediatePayParam.setTotal_fee(payAmount.doubleValue());//总金额
				alipayImmediatePayParam.setBody(tdOrder.getDispSpName().concat("-").concat(tdOrder.getGoodsDesc()));//商品详情
				alipayImmediatePayParam.setNeed_buyer_realnamed("T");//是否需要买家实名认证
				alipayImmediatePayParam.setIt_b_pay(payOrderSurplusValidTime.toString().concat("m"));//未付款交易的超时时间，取值范围：1m～15d
				alipayImmediatePayParam.setSeller_id(spFundCode);//卖家支付宝账号
				if(logger.isInfoEnabled()){
					logger.info("请求支付宝预支付接口：terminalType=" + terminalType + ",requestParams=" + JSON.toJSONString(alipayImmediatePayParam));
				}
				baseDTO = aliPayServiceImpl.getImmediatePay(alipayImmediatePayParam, tdOrder.getFundId());
			}else {
				AlipayMobileParam alipayMobileParam = new AlipayMobileParam();
				alipayMobileParam.setNotify_url(CacheUtils.getDimSysConfConfValue("ALIPAY_BG_CALLBACK_URL"));//服务器异步通知页面路径
				alipayMobileParam.setApp_id(hardwareId);//标识客户端
				alipayMobileParam.setOut_trade_no(tdOrder.getTdId().toString().concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));//商户网站唯一订单号
				alipayMobileParam.setSubject(tdOrder.getGoodsDesc());//商品名称
				alipayMobileParam.setPayment_type("1");//支付类型。默认值为：1（商品购买）
				alipayMobileParam.setTotal_fee(payAmount.doubleValue());//总金额
				alipayMobileParam.setBody(tdOrder.getDispSpName().concat("-").concat(tdOrder.getGoodsDesc()));//商品详情
				alipayMobileParam.setRn_check("T");//是否发起实名校验
				alipayMobileParam.setIt_b_pay(payOrderSurplusValidTime.toString().concat("m"));//未付款交易的超时时间，取值范围：1m～15d
				alipayMobileParam.setSeller_id(spFundCode);//卖家支付宝账号
				if(logger.isInfoEnabled()){
					logger.info("请求支付宝预支付接口：terminalType=" + terminalType + ",requestParams=" + JSON.toJSONString(alipayMobileParam));
				}
				baseDTO = aliPayServiceImpl.getMobilePayUrl(alipayMobileParam, tdOrder.getFundId());
			}

			if(logger.isInfoEnabled()){
				logger.info("<<接口响应报文：resultCode=" + baseDTO.getResultCode() + ",resultDesc=" + baseDTO.getResultDesc() + ",result=" + baseDTO.getResult());
			}
			if(Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(baseDTO.getResultCode()).trim())){
				reqSuccess = true;
				channelParams = baseDTO.getResult().toString();
			}
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_CCB.equals(ppFundPlatform.getPpFundTypeId())){
			if(channelSp.getFundModelId() != DimDictEnum.FUND_MODEL_ID_2.getCode()){//多商户直接收单，目前建行仅支持此种方式
				resultMap.put("resultCode", Hint.PAY_ORDER_13097.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13097.getMessage());
				return resultMap;
			}
			if(ValidateUtils.isEmpty(userIp) || "127.0.0.1".equals(userIp)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "终端用户IP"));
				return resultMap;
			}
			params.put("FundId", tdOrder.getFundId());//资金平台ID
			params.put("orderId", tdOrder.getTdId().toString().concat("_").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));//订单号
			params.put("payment", payAmount.toString());//付款金额
			params.put("clientIp", userIp);//客户端IP
			params.put("regInfo", accountId);//客户注册信息
			params.put("proInfo", tdOrder.getGoodsDesc());//商品信息
			params.put("remark1", tdOrder.getFundId());
			params.put("remark2", tdOrder.getTdId().toString());
			if(ValidateUtils.isEmpty(ccbFlag) || "1".equals(ccbFlag)){
				params.put("gateway", "");//网关类型
			}else {
				params.put("gateway", "UnionPay");//网关类型
			}
			if(logger.isInfoEnabled()){
				logger.info("请求建行防钓鱼支付接口：" + params);
			}
			responseMap = netPayServiceImpl.antiPhishingPayment(params);
			if(logger.isInfoEnabled()){
				logger.info("<<接口响应报文：" + responseMap);
			}
			if(Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(responseMap.get("resultCode")).trim())){
				reqSuccess = true;
				channelParams = responseMap.get("url").toString().concat("?").concat(responseMap.get("params").toString());
			}
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_BKU.equals(ppFundPlatform.getPpFundTypeId())){
			params.put("FundId", tdOrder.getFundId());//资金平台ID
			params.put("MerOrderNo", tdOrder.getTdId().toString().concat("_").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));//订单号
			String orderCreateTime = DateUtils.dateToString(tdOrder.getCrtTime(), "yyyyMMddHHmmss");
			params.put("TranDate", orderCreateTime.substring(0, 8));//商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			params.put("TranTime", orderCreateTime.substring(8));//商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			params.put("Amount", payAmount.toString());//付款金额
			params.put("CommodityMsg", tdOrder.getGoodsDesc());//商品信息
			params.put("MerResv", accountId);//商户自定义，ChinaPay原样返回
			if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_2.getCode()){
				if(logger.isInfoEnabled()){
					logger.info("请求银联防钓鱼支付接口(多商户直接收单)：" + params);
				}
				responseMap = cPNetPayServiceImpl.consumePayment(params);
				if(logger.isInfoEnabled()){
					logger.info("<<接口响应报文：" + responseMap);
				}
			}else{
				String spFundCode = String.valueOf(workSp.getSpFundCode()).trim();
				if(ValidateUtils.isEmpty(spFundCode)){
					resultMap.put("resultCode", Hint.PAY_ORDER_13098.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13098.getMessage());
					return resultMap;
				}
				params.put("MerSplitMsg", spFundCode);//商户订单分账商户号
				if(logger.isInfoEnabled()){
					logger.info("请求银联防钓鱼支付接口(通道直转子账户)：" + params);
				}
				responseMap = cPNetPayServiceImpl.consumeSplitPayment(params);
				if(logger.isInfoEnabled()){
					logger.info("<<接口响应报文：" + responseMap);
				}
			}
			if(Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(responseMap.get("resultCode")).trim())){
				reqSuccess = true;
				Map dataMap = JSON.parseObject(responseMap.get("params").toString(), Map.class);
				dataMap.put("unionPayUrl", responseMap.get("url").toString());
				String redisKey = "unionPayPostParams_".concat(payOrderId);
				int expire = Integer.parseInt(Propertie.APPLICATION.value("payOrder.expire"));
				if(logger.isInfoEnabled()){
					logger.info("redisKey=" + redisKey + ",redisValue=" + dataMap + ",expire=" + expire);
				}
				redisService.setMap(redisKey, dataMap);
				redisService.expire(redisKey, expire);
				channelParams = Propertie.APPLICATION.value("unionPayMiddlePageUrl").concat("?data=").concat(payOrderId);
			}
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_WECHAT.equals(ppFundPlatform.getPpFundTypeId())){
			Integer payOrderSurplusValidTime = this.getPayOrderSurplusValidTime(tdOrder.getCrtTime(), tdOrder.getTdLimitSec());
			payOrderSurplusValidTime = payOrderSurplusValidTime / 60;//支付订单有效期（单位：分钟）
			if(payOrderSurplusValidTime < BaseDictConstant.YEEPAY_MIN_ORDER_EXP_DATE){
				payOrderSurplusValidTime = BaseDictConstant.YEEPAY_MIN_ORDER_EXP_DATE;
			}
			if(logger.isInfoEnabled()){
				logger.info("请求微信预支付，设置订单有效期(分钟)：" + payOrderSurplusValidTime);
			}
			Date nowDate = new Date();
			params.put("device_info", hardwareId);//设备号
			params.put("body", tdOrder.getDispSpName().concat("-").concat(tdOrder.getGoodsDesc()));//商品描述
			params.put("attach", tdOrder.getFundId());
			params.put("out_trade_no", tdOrder.getTdId().toString());//商户订单号
			params.put("total_fee", payAmount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP));//总金额，单位：分
			params.put("spbill_create_ip", userIp);//终端IP
			params.put("time_start", DateUtils.dateToString(nowDate, "yyyyMMddHHmmss"));//交易起始时间，格式：yyyyMMddHHmmss
			params.put("time_expire", DateUtils.dateToString(DateUtils.dateAddMinutess(nowDate, payOrderSurplusValidTime), "yyyyMMddHHmmss"));//交易结束时间，格式：yyyyMMddHHmmss
			params.put("notify_url", CacheUtils.getDimSysConfConfValue("WECHAT_BG_CALLBACK_URL"));//通知地址
			if(logger.isInfoEnabled()){
				logger.info("请求微信预支付接口：" + params);
			}
			Map<String, Object> unifiedOrderMap = weChatAppPayServiceImpl.unifiedOrder(params, tdOrder.getFundId());
			if(logger.isInfoEnabled()){
				logger.info("<<接口响应报文：" + unifiedOrderMap);
			}
			String returnCode = String.valueOf(unifiedOrderMap.get("return_code")).trim();
			String resultCode = String.valueOf(unifiedOrderMap.get("result_code")).trim();
			if(WeChatConstants.RETURN_CODE_SUCCESS.equals(returnCode) && WeChatConstants.RESULT_CODE_SUCCESS.equals(resultCode)){
				String prepayId = String.valueOf(unifiedOrderMap.get("prepay_id")).trim();
				if(ValidateUtils.isEmpty(prepayId)){
					if(logger.isInfoEnabled()){
						logger.info("微信返回prepayId为空");
					}
				}else {
					reqSuccess = true;
					channelParams = weChatAppPayServiceImpl.transferPayment(prepayId, tdOrder.getFundId());
				}
			}
		}else {
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
			return resultMap;
		}
		/*********************调用第三方支付平台 end************************/

		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setTdOrder(tdOrder.getTdOrder());
		updTdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode())));
		if(reqSuccess){
			updTdOrder.setErrCode("预支付结果|0");
		}else{
			updTdOrder.setErrCode("预支付结果|".concat(DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString()));
		}
		updTdOrder.setUpdTime(new Date());
		this.updTdOrderInfo(updTdOrder);

		if(!reqSuccess){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("payOrderId", tdOrder.getTdId().toString());
		resultMap.put("accountId", accountId);
		String channelCode = ppFundPlatform.getPpFundTypeId();
		if(BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(channelCode)){
			channelCode = BaseDictConstant.PP_FUND_TYPE_ID_ALI;
		}
		resultMap.put("channelCode", channelCode);
		resultMap.put("channelParams", channelParams);
		return resultMap;
	}

	/**
	 * 检测诊疗限制
	 * @param actPerson 用户账户信息
	 * @param mediSettleDetailList 门诊结算明细数据
	 * @param treatmentCostType 类型 1-药品 2-非药品
	 * @return 1-限制性别 2-限制年龄(18周岁) 0-合法
	 * @throws Exception
     */
	private String checkTreatmentLimit(ActPerson actPerson, List<Map<String, String>> mediSettleDetailList, String treatmentCostType) throws Exception {
		if(null == actPerson.getpGendId()){
			actPerson.setpGendId(IdCardNoUtils.getUserSexByIdNum(actPerson.getpCertNo()));
		}
		short sex = actPerson.getpGendId();
		if(null == actPerson.getpBirth()){
			actPerson.setpBirth(IdCardNoUtils.getUserBirthByIdNum(actPerson.getpCertNo()));
		}
		String birthday = DateUtils.dateToString(actPerson.getpBirth(), "yyyy-MM-dd");
		int age = UserUtil.calcUserAge(birthday);
		for(Map<String, String> mediSettleDetailMap : mediSettleDetailList){
			String centerId = mediSettleDetailMap.get("mediProjCode");
			if(logger.isInfoEnabled()){
				logger.info("检测诊疗限制 centerId=" + centerId + ",sex=" + sex + ",age=" + age);
			}
			if(HospitalTreatmentLimitCache.getSexLimitDataMap().containsKey(centerId.concat("_").concat(String.valueOf(sex)).toUpperCase())){
				if("1".equals(treatmentCostType)){
					if(logger.isInfoEnabled()){
						logger.info("违反规则[药品]-限制性别");
					}
					return "1";
				}
				if(logger.isInfoEnabled()){
					logger.info("违反规则[非药品]-限制性别");
				}
				return "2";
			}
			if(age > 18 && HospitalTreatmentLimitCache.getChildLimitDataMap().containsKey(centerId.toUpperCase())){
				if("1".equals(treatmentCostType)){
					if(logger.isInfoEnabled()){
						logger.info("违反规则[药品]-限制年龄");
					}
					return "3";
				}
				if(logger.isInfoEnabled()){
					logger.info("违反规则[非药品]-限制年龄");
				}
				return "4";
			}
		}
		if(logger.isInfoEnabled()){
			logger.info("<<检测通过");
		}
		return "0";
	}
}

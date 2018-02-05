package com.models.cloud.gw.service.trade.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.trade.PayOrderServiceGW;
import com.models.cloud.pay.escrow.alipay.dto.BaseDTO;
import com.models.cloud.pay.escrow.alipay.param.AlipaySingleQueryParam;
import com.models.cloud.pay.escrow.alipay.response.AlipaySingleQueryResponse.SingleQueryResponseTrade;
import com.models.cloud.pay.escrow.alipay.service.AlipayService;
import com.models.cloud.pay.escrow.chinapay.service.CPNetPayService;
import com.models.cloud.pay.escrow.mi.pangu.request.MzQueryInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.response.MzOrderResultRes;
import com.models.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.models.cloud.pay.escrow.mi.pangu.service.OutpatientService;
import com.models.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.models.cloud.pay.escrow.wechat.service.WeChatAppPayService;
import com.models.cloud.pay.escrow.wechat.utils.WeChatErrorCodeEnum;
import com.models.cloud.pay.payment.service.PaymentService;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.proplat.entity.PpFundPlatform;
import com.models.cloud.pay.proplat.entity.PpFundType;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.pay.supplier.service.ActSpService;
import com.models.cloud.pay.trade.entity.TdMiPara;
import com.models.cloud.pay.trade.entity.TdOrdExtPay;
import com.models.cloud.pay.trade.entity.TdOrder;
import com.models.cloud.pay.trade.service.PayOrderService;
import com.models.cloud.pay.trade.service.TdOrderService;
import com.models.cloud.util.*;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;

@Service("payOrderServiceGWImpl")
public class PayOrderServiceGWImpl implements PayOrderServiceGW {

	private static final Logger logger = Logger.getLogger(PayOrderServiceGWImpl.class);

	@Resource
	private PayUserService payUserServiceImpl;
	@Resource
	private RedisService redisService;
	@Resource
	private ActSpService actSpServiceImpl;
	@Resource
	private PaymentService yeepayPaymentServiceImpl;
	@Resource
	private TdOrderService tdOrderServiceImpl;
	@Resource
	private PayOrderService payOrderServiceImpl;
	//银联
	@Resource(name="cPNetPayServiceImpl")
	private CPNetPayService cPNetPayService;
	//支付宝
	@Resource(name="aliPayServiceImpl")
	private AlipayService alipayService;
	@Resource
	private OutpatientService outpatientServiceImpl;
	@Resource
	private DoServiceInterface payOrderFullRefundInterfaceImpl;
	@Resource
	private WeChatAppPayService weChatAppPayServiceImpl;

	/**
	 * 查询交易单：支付完成返回商户
	 *    查询数据库中的支付订单信息，订单状态为成功或者失败直接返回结果；进行中或其他情况需要向易宝发起查询订单的请求
	 *    查询订单状态结果，失败--则修改数据库中订单状态为失败；成功--则修改数据库中订单状态为成功，并增加一条支付流水；
	 * 
	 * 	1.根据订单号查询库中主支付订单信息，
	 *    状态为：进行中或初始化且已超时的订单；判断扩展表中支付方式为哪种：（1.商业支付；2.社保支付；3.混合支付）
	 * 2.纯商业支付：判断订单表中的资金平台fundId为哪种（EBZF  ALIPAY  JSYH），并调用不同的查询支付渠道结果接口
	 * 3.社保支付：调用社保支付结果接口
	 * 4.混合支付：判断子状态： 商业支付状态   和   社保支付状态
	 *    (1)商业支付状态为进行中或初始化且已超时，判断订单表中的资金平台fundId为哪种（EBZF  ALIPAY  JSYH），并调用不同的查询支付渠道结果接口,
	 *        返回结果成功  ；0：失败 1：成功 2：未处理 3：处理中 4：已撤销
	 *        a.0 1 4 修改订单状态
	 *        b.其他不做处理
	 * 	   (2)商业支付状态为成功，判断社保支付状态
	 * 	          社保支付状态为进行中或初始化且已超时，调用社保支付结果接口，根据返回结果修改订单
	 *         成功或者失败修改订单，其他不做处理
	 *    (2)商业支付状态为失败，修改订单状态
	 * 
	 * 5.订单状态为成功或者失败直接返回结果；
	 *    
	 * @param inputMap
	 * @return
	 * @throws Exception
     */
	public Map<String, Object> queryOrder(Map<String, Object> inputMap) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("请求查询交易单信息接口，请求参数："+inputMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户交易流水号
		String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付订单号
		String hardwareId = String.valueOf(inputMap.get("hardwareId")).trim();//设备硬件ID
		if(logger.isInfoEnabled()){
			logger.info("验证账户是否登录");
		}

		if(ValidateUtils.isNotEmpty(accountId)){
			Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, hardwareId, true);
			if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
				if(logger.isInfoEnabled()){
					logger.info("验证账户是否登录，返回失败");
				}
				return checkPayUserMap;
			}
		}

		List<TdOrder> tdOrderList = new ArrayList<TdOrder>();
		if(ValidateUtils.isNotEmpty(payOrderId)){
			TdOrder tdOrderInfo = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
			if(null == tdOrderInfo){
				if(logger.isInfoEnabled()){
					logger.info("支付订单信息不存在");
				}
				resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
				return resultMap;
			}	
			
			if(!"".equals(merOrderId) && !merOrderId.equals(tdOrderInfo.getOrigOrdCode())){
				if(logger.isInfoEnabled()){
					logger.info("商户订单号无效或不存在");
				}
				resultMap.put("resultCode", Hint.PAY_ORDER_13061_FUND_ID_CODE_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13061_FUND_ID_CODE_INVALID.getMessage());
				return resultMap;
			}

			tdOrderList.add(tdOrderInfo);
		}else if(ValidateUtils.isNotEmpty(merOrderId)){
			tdOrderList = tdOrderServiceImpl.findTdOrderListByOrigOrdCode(merOrderId);
		}else{
			if(logger.isInfoEnabled()){
				logger.info("支付订单号&商户订单号不存在");
			}
			resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payOrderId"));
			return resultMap;
		}
		
		if(tdOrderList == null || tdOrderList.size()<=0){
			if(logger.isInfoEnabled()){
				logger.info("支付订单信息不存在");
			}
			resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage().replace("{param}", "payOrderId"));
			return resultMap;
		}
		
		List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();
		TdMiPara tdMiPara;
		TdOrdExtPay tdOrdExtPay;
		PpFundPlatform ppFundPlatform;
		PpFundType ppFundType;
		for(TdOrder tdOrder : tdOrderList){
			Map<String, Object> map = new HashMap<String, Object>();
			if(logger.isInfoEnabled()){
				logger.info("查询交易订单信息："+tdOrder);
				logger.info("订单号 "+tdOrder.getTdId()+" 的订单交易状态："+tdOrder.getTdStatId()+" 其中【初始化100】【进行中101】【成功110】【失败120】【异常130】");
			}
			
			tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
			if(logger.isInfoEnabled()){
				logger.info("查询支付扩展表信息：" + tdOrdExtPay);
			}
			if(null == tdOrder || null == tdOrdExtPay){
				if(logger.isInfoEnabled()){
					logger.info("支付订单信息or支付扩展信息不存在");
				}
				resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
				return resultMap;
			}
			
			//订单是否超时（true为超时，false不超时）
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
			
			Map<String,Object> result = new HashMap<>();
			//1.支付状态为：进行中或初始化且已超时的订单
			if((tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode() && orderIsTimeout) ||
					tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode()){
				
				//判断支付方式:1.商业支付；2.社保支付；3.混合支付
				
				//-----------------------------------纯商业支付 begin----------------------------------------------------------
				if(tdOrdExtPay.getPayTypeId()==DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode()){
					if(logger.isInfoEnabled()){
						logger.info("查询纯商业支付结果");
					}
					result = toSYZF(tdOrder);
					if(!Hint.SYS_SUCCESS.getCodeString().equals(result.get("resultCode"))){
						return result;
					}
				}else
				//-----------------------------------纯商业支付 end----------------------------------------------------------
				
				//-----------------------------------纯医保支付 begin----------------------------------------------------------
				if(tdOrdExtPay.getPayTypeId()==DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
					if(logger.isInfoEnabled()){
						logger.info("查询纯医保支付结果");
					}
					result = toYB(tdOrder, tdOrdExtPay, orderIsTimeout);
					if(!Hint.SYS_SUCCESS.getCodeString().equals(result.get("resultCode"))){
						return result;
					}
				}else
				//-----------------------------------纯医保支付 end  ----------------------------------------------------------
				
				//-----------------------------------混合支付 begin----------------------------------------------------------
				if(tdOrdExtPay.getPayTypeId()==DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
					if(logger.isInfoEnabled()){
						logger.info("查询混合支付结果");
					}
					if(null != tdOrdExtPay.getTdBusiStatId() && 
							!(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() 
							|| tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode())){
						//商业支付
						if(logger.isInfoEnabled()){
							logger.info("查询商业支付结果");
						}
						result = toSYZF(tdOrder);
						if(!Hint.SYS_SUCCESS.getCodeString().equals(result.get("resultCode"))){
							return result;
						}
					}else if(null != tdOrdExtPay.getTdSiStatId() &&
							!(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() 
							|| tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode())){
						//社保支付
						if(logger.isInfoEnabled()){
							logger.info("查询医保支付结果");
						}
						result = toYB(tdOrder, tdOrdExtPay, orderIsTimeout);
						if(!Hint.SYS_SUCCESS.getCodeString().equals(result.get("resultCode"))){
							return result;
						}
					}
				}
				//-----------------------------------混合支付 end  ----------------------------------------------------------
				else{
					if(logger.isInfoEnabled()){
						logger.info("支付方式不存在:"+tdOrdExtPay.getPayTypeId());
					}
					resultMap.put("resultCode", Hint.PAY_ORDER_13090_PAY_TYPE_IS_NOT_INVALID.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13090_PAY_TYPE_IS_NOT_INVALID.getMessage());
					return resultMap;
				}
			}
			
			tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(tdOrder.getTdId());
			tdOrdExtPay = tdOrderServiceImpl.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
			String errorCodeDesc = String.valueOf(tdOrder.getErrCode()).trim();
			if(ValidateUtils.isEmpty(errorCodeDesc)){
				errorCodeDesc = "";
			}
			map.put("errorCodeDesc", errorCodeDesc);
			
			BigDecimal amount = tdOrder.getRecvCur();
			if(null == amount || amount.doubleValue() < 0){
				amount = BigDecimal.ZERO;
			}
			map.put("amount", amount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//订单金额(以"分"为单位的整型)
			map.put("currency", BaseDictConstant.YEEPAY_TRADE_CURRENCY_RMB);//交易币种
			BigDecimal sourceFee = tdOrder.getServChrgAmt();
			if(null == sourceFee || sourceFee.doubleValue() < 0){
				sourceFee = BigDecimal.ZERO;
			}
			map.put("sourceFee", sourceFee.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//付款方手续费
			map.put("targetFee", new BigDecimal(0.00).toString());//收款方手续费
			BigDecimal tradeAmount = tdOrder.getRecvCur();
			if(null == tradeAmount || tradeAmount.doubleValue() < 0){
				tradeAmount = BigDecimal.ZERO;
			}
			map.put("sourceAmount", tradeAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//付款方实付金额
			map.put("targetAmount", tradeAmount.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//收款方实收金额
			map.put("orderTime", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyy-MM-dd HH:mm:ss"));//下单时间
			if(null != tdOrder.getTdEndTime()){
				map.put("closeTime", DateUtils.dateToString(tdOrder.getTdEndTime(), "yyyy-MM-dd HH:mm:ss"));//交易时间
			}else{
				map.put("closeTime", "");//交易时间
			}
			if(ValidateUtils.isNotEmpty(tdOrder.getDispSpName())){
				map.put("targetName", tdOrder.getDispSpName());//收款方名称--合作商户
			}else{
				map.put("targetName", "");//收款方名称--合作商户
			}
			if(ValidateUtils.isNotEmpty(tdOrder.getGoodsDesc())){
				map.put("productDesc", tdOrder.getGoodsDesc());//商品描述
			}else{
				map.put("productDesc", "");//商品描述
			}
			int status = 0;
			if(null != result && ValidateUtils.isNotEmpty(String.valueOf(result.get("status")).trim())){
				status = 3;
			}else{
				if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
					status = 3;
				}else if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode()){
					status = 2;
				}else if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
					status = 0;
				}else if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
					status = 1;
				}
			}
			
			int siStatus = 0;
			if(null != result && ValidateUtils.isNotEmpty(String.valueOf(result.get("status")).trim())){
				siStatus = 3;
			}else{
				if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
					siStatus = 3;
				}else if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode()){
					siStatus = 2;
				}else if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
					siStatus = 0;
				}else if(tdOrdExtPay.getTdSiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
					siStatus = 1;
				}
			}
			
			int busiStatus = 0;
			if(null != result && ValidateUtils.isNotEmpty(String.valueOf(result.get("status")).trim())){
				busiStatus = 3;
			}else{
				if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_INIT.getCode()){
					busiStatus = 3;
				}else if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_PROCESSING.getCode()){
					busiStatus = 2;
				}else if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
					busiStatus = 0;
				}else if(tdOrdExtPay.getTdBusiStatId() == DimDictEnum.TD_TRANS_STAT_FAILED.getCode()){
					busiStatus = 1;
				}
			}
			
			map.put("status", String.valueOf(status));//支付状态
			map.put("payTypeId", tdOrdExtPay.getPayTypeId());
			map.put("busiStatus", String.valueOf(busiStatus));
			map.put("siStatus", String.valueOf(siStatus));
			
			BigDecimal refundTotal = tdOrdExtPay.getAccRetAmt();
			if(null == refundTotal || refundTotal.doubleValue() < 0){
				refundTotal = BigDecimal.ZERO;
			}
			map.put("refundTotal", refundTotal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());//累计退款金额

			if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600102.equals(tdOrdExtPay.getChanRetCode())){
				map.put("payErrorCode", Hint.YEEPAY_13016_ACCOUNT_AVAILABLE_BALANCE_NOT_ENOUGH.getCodeString());//支付错误码
			}else{
				map.put("payErrorCode", tdOrdExtPay.getChanRetCode() == null ? "" : tdOrdExtPay.getChanRetCode().trim());//支付错误码
			}
			if(ValidateUtils.isNotEmpty(tdOrdExtPay.getChanRetDesc())){
				map.put("description", tdOrdExtPay.getChanRetDesc());//支付描述
			}else{
				map.put("description", "");//支付描述
			}
			
			String flowFlag = "";
			if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600095.equals(tdOrdExtPay.getChanRetCode()) ||
			   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_601036.equals(tdOrdExtPay.getChanRetCode()) ||
			   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600102.equals(tdOrdExtPay.getChanRetCode()) ||
			   BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600093.equals(tdOrdExtPay.getChanRetCode())){
				flowFlag = "1";
			}else if(BaseDictConstant.YEEPAY_PAY_RESULT_ERROR_CODE_600051.equals(tdOrdExtPay.getChanRetCode())){
				flowFlag = "2";
			}
			map.put("flowFlag", flowFlag);
			
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
			map.put("merchantName", actSp.getEntName());//商户名称--渠道商户
			map.put("medicarePersonPayMoney", tdOrdExtPay.getPayPacct().setScale(2,BigDecimal.ROUND_HALF_UP).toString());//医保个人账户金额
			map.put("insuranceFundPayMoney", tdOrdExtPay.getPaySi().setScale(2,BigDecimal.ROUND_HALF_UP).toString());//医保统筹基金金额
			map.put("personalPayMoney", tdOrdExtPay.getPaySelf().setScale(2,BigDecimal.ROUND_HALF_UP).toString());//个人支付金额
			map.put("payOrderId", tdOrder.getTdId());//支付订单号
			map.put("merOrderId", tdOrder.getOrigOrdCode());//商户订单号
			String escrowTradeNo = String.valueOf(tdOrder.getFundTdCode()).trim();
			if(ValidateUtils.isEmpty(escrowTradeNo)){
				escrowTradeNo = "";
			}
			tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
			String serialId = String.valueOf(tdOrdExtPay.getHospSeqCode()).trim();//门诊流水号
			if(ValidateUtils.isEmpty(serialId)){
				serialId = "";
			}
			String siTradeCode = String.valueOf(tdOrdExtPay.getSiTdCode()).trim();//医保交易流水号
			if(ValidateUtils.isEmpty(siTradeCode)){
				siTradeCode = "";
			}
			String settleDetailInfo = "";
			String miSettleFlag = "0";//0-纯自费(不使用医保) 1-预结算分账后纯自费 2-预结算分账后使用医保
			if(null != tdMiPara){
				settleDetailInfo = String.valueOf(tdMiPara.getClmRet()).trim();//结算明细信息
				if(ValidateUtils.isNotEmpty(settleDetailInfo)){
					Map settleDetailInfoMap = JsonStringUtils.jsonStringToObject(settleDetailInfo, Map.class);
					if(null != settleDetailInfoMap && settleDetailInfoMap.size() > 0){
						miSettleFlag = String.valueOf(settleDetailInfoMap.get("miSettleFlag")).trim();
						if(ValidateUtils.isEmpty(miSettleFlag)){
							miSettleFlag = "0";
						}
					}
				}else {
					settleDetailInfo = "";
				}
			}
			map.put("escrowTradeNo", escrowTradeNo);
			map.put("siTradeCode", siTradeCode);
			map.put("serialId", serialId);
			map.put("settleDetailInfo", settleDetailInfo);
			map.put("miSettleFlag", miSettleFlag);
            String fundCode = "";
            if(tdOrdExtPay.getPayTypeId() != DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode()){
				ppFundPlatform = CacheUtils.getPpFundPlatform(tdOrder.getFundId());
				if(null != ppFundPlatform){
					ppFundType = CacheUtils.getPpFundType(ppFundPlatform.getPpFundTypeId());
					if(null != ppFundType){
						fundCode = ppFundType.getDispOrder().toString();
					}
				}
            }
            map.put("fundCode", fundCode);

			orderList.add(map);
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("orderList", orderList);//订单列表
		return resultMap;
	}
	
	/**
	 * 商业支付
	 * @Description
	 * @encoding UTF-8 
	 * @author haiyan.zhang
	 * @date 2016年8月16日 
	 * @time 上午10:34:44
	 * @version V1.0
	 * @param @param tdOrder
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	public Map<String, Object> toSYZF(TdOrder tdOrder) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(tdOrder.getFundId());
		if(null == ppFundPlatform){
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}

		//-----------------------------------纯商业支付+易pay ----------------------------------------------------------
		if(BaseDictConstant.PP_FUND_TYPE_ID_EBZF.equals(ppFundPlatform.getPpFundTypeId())){
			if(logger.isInfoEnabled()){
				logger.info("商业支付--易宝支付");
			}
			resultMap = toRequestEBZFQueryOrderResult(tdOrder);
		}
		
		//-----------------------------------纯商业支付+建行 ----------------------------------------------------------
		if(BaseDictConstant.PP_FUND_TYPE_ID_CCB.equals(ppFundPlatform.getPpFundTypeId())){
			if(logger.isInfoEnabled()){
				logger.info("商业支付--建设银行支付");
			}
			resultMap = toRequestCCBQueryOrderResult(tdOrder);
		}
		
		//-----------------------------------纯商业支付+银联 ----------------------------------------------------------
		if(BaseDictConstant.PP_FUND_TYPE_ID_BKU.equals(ppFundPlatform.getPpFundTypeId())){
			if(logger.isInfoEnabled()){
				logger.info("商业支付--银联支付");
			}
			resultMap = toRequestBKUQueryOrderResult(tdOrder);
		}
		
		//-----------------------------------纯商业支付+支付宝 --------------------------------------------------------
		if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundPlatform.getPpFundTypeId()) ||
			BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundPlatform.getPpFundTypeId())){
			if(logger.isInfoEnabled()){
				logger.info("商业支付--支付宝支付");
			}
			resultMap = toRequestAliPayQueryOrderResult(tdOrder);
		}

		//-----------------------------------纯商业支付+微信 --------------------------------------------------------
		if(BaseDictConstant.PP_FUND_TYPE_ID_WECHAT.equals(ppFundPlatform.getPpFundTypeId())){
			if(logger.isInfoEnabled()){
				logger.info("商业支付--微信支付");
			}
			resultMap = toRequestWeChatPayQueryOrderResult(tdOrder);
		}

		return resultMap;
	}

	/**
	 * 医保结算交易单查询
	 * @param tdOrder
	 * @param tdOrdExtPay
	 * @param orderIsTimeout
	 * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	private Map<String, Object> toYB(TdOrder tdOrder, TdOrdExtPay tdOrdExtPay, boolean orderIsTimeout) throws Exception{

		Map<String, Object> resultMap = new HashMap<>();
		TdMiPara tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
		if(null == tdMiPara){
			if(logger.isInfoEnabled()){
				logger.info("TdMiPara信息为空");
			}
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
			return resultMap;
		}
		Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
		if(null == preClmParaMap || preClmParaMap.size() == 0){
			if(logger.isInfoEnabled()){
				logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
			}
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13109.getMessage());
			return resultMap;
		}
		String fixedHospCode = preClmParaMap.get("fixedHospCode").toString();
		PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), tdOrdExtPay.getpSiCardNo(), fixedHospCode);
		if(null == personInfoRes){
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13101.getMessage());
			return resultMap;
		}
		/**********************************调用医保结算查询 begin*************************************/
		String tdStatId = "";
		String siRetCode = "";
		String siRetDesc = "";
		String siTdCode = "";
		String clmRet = null;
		String medicalClass = String.valueOf(tdOrdExtPay.getPayPropB()).trim();//医疗类别 1-医院门诊 2-药店购药
		String insuredPersonType = String.valueOf(tdOrdExtPay.getPayPropC()).trim();//参保人员类别 1-职工 2-居民
		if(BaseDictConstant.MEDICAL_CLASS_HOSPITAL_CLINIC.equals(medicalClass)){
			if(BaseDictConstant.INSURED_PERSON_TYPE_EMPLOYEE.equals(insuredPersonType)){
				MzQueryInfoReq mzQueryInfoReq = new MzQueryInfoReq();
				mzQueryInfoReq.setSerialID(tdOrdExtPay.getHospSeqCode());//门诊流水号
				mzQueryInfoReq.setPersonID(tdOrdExtPay.getSiPCode());//个人编号
				if(logger.isInfoEnabled()){
					logger.info("医院门诊医保结算(职工)记录查询，请求参数：fixedHospCode=" + fixedHospCode + ",cycId=" + personInfoRes.getCycid() + ",mzQueryInfoReq=" + JSON.toJSONString(mzQueryInfoReq));
				}
				MzOrderResultRes mzOrderResultRes = outpatientServiceImpl.queryMzOrder(fixedHospCode, personInfoRes.getCycid(), mzQueryInfoReq);
				if(null != mzOrderResultRes){
					clmRet = JSON.toJSONString(mzOrderResultRes);
				}
				if(logger.isInfoEnabled()){
					logger.info("响应报文：mzOrderResultRes=" + clmRet);
				}
				if(null == mzOrderResultRes || mzOrderResultRes.getRTNCode() != 1){
					if(orderIsTimeout){//订单超时，需修改订单状态
						if(logger.isInfoEnabled()){
							logger.info("查询-医院门诊医保结算(职工)失败且当前订单已超时，修改状态");
						}
						tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
						siRetCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
						siRetDesc = "查询-医院门诊医保结算(职工)失败且当前订单已超时";
					}else {
						if(logger.isInfoEnabled()){
							logger.info("查询医院门诊医保结算(职工)失败");
						}
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", "查询医院门诊医保结算(职工)记录失败");
						return resultMap;
					}
				}else {
					String jbr = Propertie.APPLICATION.value("miPayOperator");
					if(logger.isInfoEnabled()){
						logger.info("申请结算经办人：" + jbr + ",门诊交易记录查询返回经办人：" + mzOrderResultRes.getJingbr());
					}
					if(ValidateUtils.isNotEmpty(mzOrderResultRes.getJingbr()) && !jbr.equals(mzOrderResultRes.getJingbr())){
						tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
						siRetCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
						siRetDesc = "申请结算经办人与门诊交易记录查询返回经办人不一致";
					}else {
						String zhpay = String.valueOf(mzOrderResultRes.getZhpay()).trim();
						if(ValidateUtils.isEmpty(zhpay) || Double.parseDouble(zhpay) < 0){
							mzOrderResultRes.setZhpay("0");
						}
						BigDecimal payPacct = BigDecimal.valueOf(Double.parseDouble(mzOrderResultRes.getZhpay())).setScale(2, BigDecimal.ROUND_HALF_UP);
						//统筹基金=统筹支付+大额支付+公务员补助+企业补充基金支付
						String tcpay = String.valueOf(mzOrderResultRes.getTcpay()).trim();
						if(ValidateUtils.isEmpty(tcpay) || Double.parseDouble(tcpay) < 0){
							mzOrderResultRes.setTcpay("0");
						}
						String bigpay = String.valueOf(mzOrderResultRes.getBigpay()).trim();
						if(ValidateUtils.isEmpty(bigpay) || Double.parseDouble(bigpay) < 0){
							mzOrderResultRes.setBigpay("0");
						}
						String gwybz = String.valueOf(mzOrderResultRes.getGwybz()).trim();
						if(ValidateUtils.isEmpty(gwybz) || Double.parseDouble(gwybz) < 0){
							mzOrderResultRes.setGwybz("0");
						}
						String qybcjjpay = String.valueOf(mzOrderResultRes.getQybcpay()).trim();
						if(ValidateUtils.isEmpty(qybcjjpay) || Double.parseDouble(qybcjjpay) < 0){
							mzOrderResultRes.setQybcpay("0");
						}
						BigDecimal paySi = BigDecimal.valueOf(Double.parseDouble(mzOrderResultRes.getTcpay()))
								.add(BigDecimal.valueOf(Double.parseDouble(mzOrderResultRes.getBigpay())))
								.add(BigDecimal.valueOf(Double.parseDouble(mzOrderResultRes.getGwybz())))
								.add(BigDecimal.valueOf(Double.parseDouble(mzOrderResultRes.getQybcpay()))).setScale(2, BigDecimal.ROUND_HALF_UP);
						String cashpay = String.valueOf(mzOrderResultRes.getCashpay()).trim();
						if(ValidateUtils.isEmpty(cashpay) || Double.parseDouble(cashpay) < 0){
							mzOrderResultRes.setCashpay("0");
						}
						BigDecimal paySelf = BigDecimal.valueOf(Double.parseDouble(mzOrderResultRes.getCashpay())).setScale(2, BigDecimal.ROUND_HALF_UP);

						if(logger.isInfoEnabled()){
							logger.info("预结算：payPacct=" + tdOrdExtPay.getPayPacct() + ",paySi=" + tdOrdExtPay.getPaySi() + ",paySelf=" + tdOrdExtPay.getPaySelf() +
									" 查询结算记录：payPacct=" + payPacct + ",paySi=" + paySi + ",paySelf=" + paySelf);
						}
						siTdCode = String.valueOf(mzOrderResultRes.getDealID()).trim();
						if(tdOrdExtPay.getPayPacct().compareTo(payPacct) == 0 &&
								tdOrdExtPay.getPaySi().compareTo(paySi) == 0 &&
								tdOrdExtPay.getPaySelf().compareTo(paySelf) == 0){
							if(logger.isInfoEnabled()){
								logger.info("查询-医院门诊医保结算(职工)成功");
							}
							tdStatId = DimDictEnum.TD_TRANS_STAT_SUCCESS.getCodeString();
							siRetCode = "0";
							siRetDesc = "医院门诊医保结算(职工)成功";
						}else {
							if(logger.isInfoEnabled()){
								logger.info("查询-医院门诊医保结算(职工)失败，预结算与结算结果不一致");
							}
							tdStatId = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siRetCode = DimDictEnum.TD_TRANS_STAT_FAILED.getCodeString();
							siRetDesc = "医院门诊医保结算(职工)失败，预结算与结算结果不一致";

							//进行结算撤销处理
							if(logger.isInfoEnabled()){
								logger.info("医院门诊医保结算(职工)失败，进行退款处理");
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
		/**********************************调用医保结算查询 end*************************************/

		/****************************更新交易单&交易扩展表信息 begin**********************************/
		TdOrder updTdOrder = new TdOrder();
		updTdOrder.setTdId(tdOrder.getTdId());
		updTdOrder.setTdEndTime(new Date());
		updTdOrder.setErrCode("支付结果|".concat(siRetCode));
		updTdOrder.setUpdTime(new Date());
		updTdOrder.setTdStatId(Short.valueOf(tdStatId));
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易订单信息");
		}
		tdOrderServiceImpl.updateTdOrderInfo(updTdOrder);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		TdOrdExtPay updTdOrdExtPay = new TdOrdExtPay();
		updTdOrdExtPay.setTdId(tdOrder.getTdId());//交易单ID
		updTdOrdExtPay.setTdSiStatId(Short.valueOf(tdStatId));
		if(ValidateUtils.isNotEmpty(siTdCode)){
			updTdOrdExtPay.setSiTdCode(siTdCode);//社保交易流水号
		}
		updTdOrdExtPay.setSiRetCode(siRetCode);
		updTdOrdExtPay.setSiRetDesc(siRetDesc);
		updTdOrdExtPay.setUpdTime(new Date());
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展信息");
		}
		tdOrderServiceImpl.updateTdOrdExtPayInfo(updTdOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		if(ValidateUtils.isNotEmpty(clmRet)){
			TdMiPara updTdMiPara = new TdMiPara();
			updTdMiPara.setTdId(tdOrder.getTdId());
			Map clmRetMap = JsonStringUtils.jsonStringToObject(tdMiPara.getClmRet(), Map.class);
			if(null != clmRetMap && clmRetMap.size() > 0){
				clmRetMap.putAll(JsonStringUtils.jsonStringToObject(clmRet, Map.class));
				clmRet = JsonStringUtils.objectToJsonString(clmRetMap);
			}
			updTdMiPara.setClmRet(clmRet);
			updTdMiPara.setUpdTime(new Date());
			if(logger.isInfoEnabled()){
				logger.info("开始更新TdMiPara信息");
			}
			tdOrderServiceImpl.updateTdMiPara(updTdMiPara);
			if(logger.isInfoEnabled()){
				logger.info("<<更新成功");
			}
		}
		/****************************更新交易单&交易扩展表信息 end**********************************/

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getCodeString());
		return resultMap;
	}
	
	/**
	 * 请求易pay支付查询结果
	 * @Description
	 * @encoding UTF-8 
	 * @author haiyan.zhang
	 * @date 2016年8月15日 
	 * @time 下午2:37:25
	 * @version V1.0
	 * @param @param tdOrder
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	private Map<String, Object> toRequestEBZFQueryOrderResult(TdOrder tdOrder) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String payOrderId = tdOrder.getTdId().toString();
		
		if(logger.isInfoEnabled()){
			logger.info("请求【易宝】调用支付结果查询接口，请求参数：payOrderId:"+payOrderId+"-"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		}
		Map<String, String> responseMap = yeepayPaymentServiceImpl.payapiQueryByTdid(payOrderId+"-"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		if(logger.isInfoEnabled()){
			logger.info("请求【易宝】调用支付结果查询接口，返回结果："+responseMap);
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
				resultMap.put("status", "3");
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
				if(!BaseDictConstant.YEEPAY_PAYMENT_RESULT_SUCCESS.equals(result)){
					//更新数据失败
					if(logger.isInfoEnabled()){
						logger.info("更新数据失败");
					}
					resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
					resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
					return resultMap;
				}
				
			}
		}else{
			resultMap.put("resultCode", responseMap.get("error_code"));
			resultMap.put("resultDesc", responseMap.get("error_msg"));
			return resultMap;
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
		
	}

	/**
	 * 请求建设银行查询支付结果
	 * @Description
	 * @encoding UTF-8 
	 * @author haiyan.zhang
	 * @date 2016年8月15日 
	 * @time 下午2:42:09
	 * @version V1.0
	 * @param @param tdOrder
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	private Map<String, Object> toRequestCCBQueryOrderResult(TdOrder tdOrder) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Map<String, String> responseMap = new HashMap<String, String>();
//		String payOrderId = tdOrder.getTdId().toString()+"-"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString();
		String payOrderId = tdOrder.getTdId().toString();

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("interfaceCode", "merchantPaymentQuery");
		Random random = new Random();
		//请求序列号
		map.put("REQUEST_SN", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+random.nextInt(2));
		//流水类型: 0:未结流水,1:已结流水
		map.put("KIND", "0");
		//资金平台ID
		map.put("FundId", tdOrder.getFundId());
		//订单号
		map.put("ORDER", tdOrder.getTdId()+"_"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		//排序 : 1:交易日期,2:订单号
		map.put("NORDERBY", "2");
		//当前页次
		map.put("PAGE", "1");
		//流水状态：0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部
		map.put("STATUS", "3");
		if(logger.isInfoEnabled()){
			logger.info("查询【建行】订单支付结果，请求参数map："+map);
		}
		String json = JSON.toJSONString(map);
		String key = CipherAesEnum.HTTPINVOKE.getAesKey();
		String iv = CipherAesEnum.HTTPINVOKE.getAesIvbytes();
		String sign = Utils.aesData(json,key, iv);
		Map<String,String> data = new HashMap<String,String>();
		
		data.put("sign", sign);
		data.put("data", json);
		String url = CacheUtils.getDimSysConfConfValue("CCB_FRONT_MACHINE_URL");
		if(logger.isInfoEnabled()){
			logger.info("查询【建行】订单支付结果，请求参数："+url+data);
		}
		String resultStr = HttpRequest.doPostJson(url, JSON.toJSONString(data));
		Map<String,Object>  responseMap= JSON.parseObject(resultStr);
		if(logger.isInfoEnabled()){
			logger.info("查询【建行】订单支付结果，返回结果："+responseMap);
		}
		
		//返回结果为成功或者查询的订单不存在
		if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.get("resultCode"))
				|| "0250E0200001".equals(responseMap.get("resultCode"))){
			
			String orderStatus = "1";
			//是否超时并且订单不存在
			boolean isTimeOutAndExist = false;
			//操作支付数据
			boolean toDoUpdate = false;
			Map<String,Object> listMap = new HashMap<String,Object>();
			if("0250E0200001".equals(responseMap.get("resultCode"))){
				isTimeOutAndExist = true;
				resultMap.put("status", "3");
				if(new Date().getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(tdOrder.getTdLimitSec().toString()) * 1000)){
					//订单不存在且订单已超时，则修改订单为失败
					orderStatus = "0";
					toDoUpdate = true;
					listMap.put("TRAN_DATE", "");
					listMap.put("PAYMENT_MONEY", "");
				}
			}
			
			if(!isTimeOutAndExist){
				Map<String,Object> dataMap = new HashMap<String,Object>();
				dataMap = JSON.parseObject((String) responseMap.get("data"));
				logger.info("dataMap:"+dataMap);
				if(!dataMap.containsKey("TX_INFO")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}

				Map<String,Object> infoMap = (Map<String, Object>) dataMap.get("TX_INFO");
				if(!infoMap.containsKey("LIST")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
				
				listMap =  (Map<String, Object>) infoMap.get("LIST");
				if(!listMap.containsKey("ORDER_STATUS")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
				
				//订单状态（0：失败，1：成功，2：待银行确认，3：已部分退款，4：已全额退款，5：待银行确认）
				String status = (String) listMap.get("ORDER_STATUS");
				if(logger.isInfoEnabled()){
					logger.info("查询【建行】订单支付结果，返回状态："+status);
				}
				if("0".equals(status)){
					//失败
					orderStatus = "0";
					toDoUpdate = true;
				}else if("1".equals(status) || "3".equals(status) || "4".equals(status)){
					//成功
					toDoUpdate = true;
				}
			}
			
			if(toDoUpdate){
				if(logger.isInfoEnabled()){
					logger.info("更新库中订单交易状态");
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
					result = payOrderServiceImpl.executeOrderTradeData_CCB(payOrderId, "", orderStatus,
							(String) listMap.get("TRAN_DATE"), (String) listMap.get("PAYMENT_MONEY"));
				}catch (Exception e) {
					if(logger.isInfoEnabled()){
						logger.info("系统异常：" + e.getMessage());
					}
					result = BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
				}
				redisService.delete(redisKey);
				if(logger.isInfoEnabled()){
					logger.info("更新数据 -- 返回："+result);
				}
				if(!BaseDictConstant.CCB_PAYMENT_RESULT_SUCCESS.equals(result)){
					//更新数据失败
					if(logger.isInfoEnabled()){
						logger.info("更新数据失败");
					}
					resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
					resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
					return resultMap;
				}
			}
			
		}else{
			resultMap.put("resultCode", responseMap.get("resultCode"));
			resultMap.put("resultDesc", responseMap.get("resultDesc"));
			return resultMap;
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}
	
	/**
	 * 调用银联查询交易单
	 * @param @param tdOrder
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	private Map<String, Object> toRequestBKUQueryOrderResult(TdOrder tdOrder) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		Map<String,Object> map = new HashMap<>();
		String payOrderId = String.valueOf(tdOrder.getTdId());
		//资金平台ID
		map.put("FundId", tdOrder.getFundId());
		//订单号
		map.put("MerOrderNo", payOrderId+"_"+tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		//商户交易日期
		map.put("TranDate", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyyMMdd"));
		
		if(logger.isInfoEnabled()){
			logger.info("查询【银联】订单支付结果，请求参数："+map);
		}
		Map<String,Object>  responseMap= cPNetPayService.consumePaymentQuery(map);
		if(logger.isInfoEnabled()){
			logger.info("查询【银联】订单支付结果，返回结果："+responseMap);
		}
		
		//查询结果返回成功或者订单不存在  3441-订单不存在
		if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.get("resultCode"))
				|| "3441".equals(responseMap.get("resultCode"))){
			
			//支付状态：1-支付成功 0-支付失败
			String orderStatus = "1";
			//是否超时并且订单不存在
			boolean isTimeOutAndExist = false;
			//操作支付数据
			boolean toDoUpdate = false;
			String closeTime = "";
			String orderPrice = "";
			if("3441".equals(responseMap.get("resultCode"))){
				isTimeOutAndExist = true;
				resultMap.put("status", "3");
				if(new Date().getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(tdOrder.getTdLimitSec().toString()) * 1000)){
					//订单不存在且订单已超时，则修改订单为失败
					orderStatus = "0";
					toDoUpdate = true;
					responseMap.put("AcqSeqId", "");
				}
			}
			
			if(!isTimeOutAndExist){
				String CompleteDate = (String) responseMap.get("CompleteDate");
				String CompleteTime = (String) responseMap.get("CompleteTime");
				if(ValidateUtils.isNotEmpty(CompleteDate) && ValidateUtils.isNotEmpty(CompleteTime)){
					closeTime = DateUtils.dateToString(DateUtils.strToDate(CompleteDate.concat(" ").concat(CompleteTime), "yyyyMMdd hhmmss"), "yyyy-MM-dd hh:mm:ss");
				}
				//单位：分
				orderPrice = (String) responseMap.get("OrderAmt");
				String status = String.valueOf(responseMap.get("OrderStatus"));
				if(logger.isInfoEnabled()){
					logger.info("查询【银联】订单支付结果，返回状态："+status);
				}
				//0000-成功 0001-初始化
				if("0000".equals(status)){
					toDoUpdate = true;
				}else if("0001".equals(status)){
					if(new Date().getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(tdOrder.getTdLimitSec().toString()) * 1000)){
						//返回结果中的订单为初始化且订单已超时，则修改订单为失败
						orderStatus = "0";
						toDoUpdate = true;
					}
				}else{
					//支付失败
					orderStatus = "0";
					toDoUpdate = true;
				}
			}
			
			if(toDoUpdate){
				if(logger.isInfoEnabled()){
					logger.info("更新库中订单交易状态");
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
					result = payOrderServiceImpl.executeOrderTradeData_BKU(payOrderId,
							(String) responseMap.get("AcqSeqId"),orderStatus,closeTime, orderPrice);
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
				if(!BaseDictConstant.YEEPAY_PAYMENT_RESULT_SUCCESS.equals(result)){
					//更新数据失败
					if(logger.isInfoEnabled()){
						logger.info("更新数据失败");
					}
					resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
					resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
					return resultMap;
				}
			}
			
		}else{
			resultMap.put("resultCode", responseMap.get("resultCode"));
			resultMap.put("resultDesc", responseMap.get("resultDesc"));
			return resultMap;
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}
	
	/**
	 * 支付宝支付
	 * @Description
	 * @encoding UTF-8 
	 * @author haiyan.zhang
	 * @date 2016年8月16日 
	 * @time 上午11:54:29
	 * @version V1.0
	 * @param @param tdOrder
	 * @param @return
	 * @param @throws Exception
	 *
	 */
	private Map<String, Object> toRequestAliPayQueryOrderResult(TdOrder tdOrder) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		String payOrderId = String.valueOf(tdOrder.getTdId());
		String fundTdCode = ValidateUtils.isNotEmpty(tdOrder.getFundTdCode())? tdOrder.getFundTdCode() : "";
		String fundId = tdOrder.getFundId();
		
		AlipaySingleQueryParam alipayParam = new AlipaySingleQueryParam();
		//支付宝交易号
		alipayParam.setTrade_no(fundTdCode);
		//商户唯一订单号
		alipayParam.setOut_trade_no(payOrderId.concat("-").concat(tdOrder.getTdOrder().setScale(0, BigDecimal.ROUND_HALF_UP).toString()));
		
		if(logger.isInfoEnabled()){
			logger.info("查询【支付宝】订单支付结果，请求参数：orderId:"+alipayParam.getOut_trade_no()+" ,fundTdCode:"+fundTdCode);
		}
		BaseDTO<SingleQueryResponseTrade> responseMap = alipayService.querySingleTrade(alipayParam, fundId);
		if(null == responseMap){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		if(logger.isInfoEnabled()){
			logger.info("查询【支付宝】订单支付结果，返回结果："+JSON.toJSONString(responseMap));
		}
		if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.getResultCode()) 
				|| "TRADE_NOT_EXIST".equals(responseMap.getResultCode()) ){
			
			//支付状态 0-支付失败 1-支付成功
			String orderStatus = "1";
			//是否超时并且订单不存在
			boolean isTimeOutAndExist = false;
			//操作支付数据
			boolean toDoUpdate = false;
			SingleQueryResponseTrade singleTrade = new SingleQueryResponseTrade();
			if("TRADE_NOT_EXIST".equals(responseMap.getResultCode())){
				isTimeOutAndExist = true;
				resultMap.put("status", "3");
				if(new Date().getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(tdOrder.getTdLimitSec().toString()) * 1000)){
					//订单不存在且订单已超时，则修改订单为失败
					orderStatus = "0";
					toDoUpdate = true;
					singleTrade.setTrade_no("");
					singleTrade.setGmt_payment("");
					singleTrade.setTotal_fee("");
				}
			}
			
			if(!isTimeOutAndExist){
				singleTrade = responseMap.getResult();
				if(singleTrade == null){
					resultMap.put("resultCode", responseMap.getResultCode());
					resultMap.put("resultDesc", responseMap.getResultDesc());
					return resultMap;
				}
				String status = singleTrade.getTrade_status();
				if(logger.isInfoEnabled()){
					logger.info("查询【支付宝】订单支付结果，返回状态："+status);
				}
				if("TRADE_CLOSED".equals(status) || "TRADE_REFUSE".equals(status) 
						|| "TRADE_CANCEL".equals(status)){
					//支付失败
					orderStatus = "0";
					toDoUpdate = true;
				}else if("WAIT_SELLER_SEND_GOODS".equals(status) || "WAIT_BUYER_CONFIRM_GOODS".equals(status) 
						|| "TRADE_FINISHED".equals(status) || "WAIT_SYS_PAY_SELLER".equals(status) 
						|| "TRADE_PENDING".equals(status) || "TRADE_SUCCESS".equals(status)
						|| "BUYER_PRE_AUTH".equals(status) || "COD_WAIT_SELLER_SEND_GOODS".equals(status)){
					//支付成功
					toDoUpdate = true;
				}
			}
			
			if(toDoUpdate){
				if(logger.isInfoEnabled()){
					logger.info("更新库中订单交易状态");
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
					resultMap.put("resultCode", responseMap.getResultCode());
					resultMap.put("resultDesc", responseMap.getResultDesc());
					return resultMap;
				}
				String result;
				try {
					result = payOrderServiceImpl.executeOrderTradeData_ALIPAY(payOrderId, singleTrade.getTrade_no(),
							orderStatus, singleTrade.getGmt_payment(), singleTrade.getTotal_fee());
				}catch (Exception e) {
					if(logger.isInfoEnabled()){
						logger.info("系统异常：" + e.getMessage());
					}
					result = BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
				}
				redisService.delete(redisKey);
				if(logger.isInfoEnabled()){
					logger.info("更新数据 -- 返回："+result);
				}
				if(!BaseDictConstant.ALIPAY_PAYMENT_RESULT_SUCCESS.equals(result)){
					//更新数据失败
					if(logger.isInfoEnabled()){
						logger.info("更新数据失败");
					}
					resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
					resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
					return resultMap;
				}
			}
			
		}else{
			resultMap.put("resultCode", responseMap.getResultCode());
			resultMap.put("resultDesc", responseMap.getResultDesc());
			return resultMap;
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	/**
	 * 微信支付
	 * @param tdOrder
	 * @return
	 * @throws Exception
     */
	private Map<String, Object> toRequestWeChatPayQueryOrderResult(TdOrder tdOrder) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String payOrderId = tdOrder.getTdId().toString();
		String fundTdCode = String.valueOf(tdOrder.getFundTdCode()).trim();
		if(ValidateUtils.isEmpty(fundTdCode)){
			fundTdCode = "";
		}
		if(logger.isInfoEnabled()){
			logger.info("查询【微信】订单支付结果，请求报文：payOrderId=" + payOrderId + ",fundTdCode=" + fundTdCode + ",fundId=" + tdOrder.getFundId());
		}
		Map<String, Object> orderMap = weChatAppPayServiceImpl.orderQuery(fundTdCode, payOrderId, tdOrder.getFundId());
		if(logger.isInfoEnabled()){
			logger.info("查询【微信】订单支付结果，响应报文：" + orderMap);
		}
		if(null == orderMap || orderMap.size() == 0){
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}
		String returnCode = String.valueOf(orderMap.get("return_code")).trim();
		String resultCode = String.valueOf(orderMap.get("result_code")).trim();
		String errCode = String.valueOf(orderMap.get("err_code")).trim();
		String weChatPayFlowId = "";
		String payFinishTime = "";
		String totalFee = "";
		if((WeChatConstants.RETURN_CODE_SUCCESS.equals(returnCode) && WeChatConstants.RESULT_CODE_SUCCESS.equals(resultCode)) ||
			WeChatErrorCodeEnum.ORDERNOTEXIST.getErrorCode().equals(errCode)){
			//支付状态 0-支付失败 1-支付成功
			String orderStatus = "1";
			//是否超时并且订单不存在
			boolean isTimeOutAndExist = false;
			//操作支付数据
			boolean toDoUpdate = false;
			String status = String.valueOf(orderMap.get("trade_state")).trim();
			if(WeChatErrorCodeEnum.ORDERNOTEXIST.getErrorCode().equals(errCode) || "NOTPAY".equals(status)){
				isTimeOutAndExist = true;
				resultMap.put("status", "3");
				if(new Date().getTime() - tdOrder.getCrtTime().getTime() > (Long.valueOf(tdOrder.getTdLimitSec().toString()) * 1000)){
					//订单不存在且订单已超时，则修改订单为失败
					orderStatus = "0";
					toDoUpdate = true;
				}
			}
			if(!isTimeOutAndExist){
				if(logger.isInfoEnabled()){
					logger.info("查询【微信】订单支付结果，返回状态：" + status);
				}
				if("CLOSED".equals(status) || "REVOKED".equals(status) || "PAYERROR".equals(status)){//已关闭or已撤销or支付失败
					//支付失败
					orderStatus = "0";
					toDoUpdate = true;
				}else if("SUCCESS".equals(status) || "REFUND".equals(status)){//支付成功or转入退款
					//支付成功
					toDoUpdate = true;
					weChatPayFlowId = String.valueOf(orderMap.get("transaction_id")).trim();
					payFinishTime = String.valueOf(orderMap.get("time_end")).trim();
					totalFee = String.valueOf(orderMap.get("total_fee")).trim();
					if(ValidateUtils.isNotEmpty(totalFee)){
                        totalFee = BigDecimal.valueOf(Double.parseDouble(totalFee)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).toString();
					}
				}
			}
			if(toDoUpdate){
				if(logger.isInfoEnabled()){
					logger.info("更新库中订单交易状态");
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
					resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
					return resultMap;
				}
				String result;
				try {
					result = payOrderServiceImpl.executeOrderTradeData_WECHAT(payOrderId, weChatPayFlowId, orderStatus, payFinishTime, totalFee);
				}catch (Exception e) {
					if(logger.isInfoEnabled()){
						logger.info("系统异常：" + e.getMessage());
					}
					result = BaseDictConstant.WECHAT_PAYMENT_RESULT_FAILURE;
				}
				redisService.delete(redisKey);
				if(logger.isInfoEnabled()){
					logger.info("更新数据--返回：" + result);
				}
				if(!BaseDictConstant.WECHAT_PAYMENT_RESULT_SUCCESS.equals(result)){
					//更新数据失败
					if(logger.isInfoEnabled()){
						logger.info("更新数据失败");
					}
					resultMap.put("resultCode", Hint.TD_13033_WITH_SEARCH_FAIL.getCodeString());
					resultMap.put("resultDesc", Hint.TD_13033_WITH_SEARCH_FAIL.getMessage());
					return resultMap;
				}
			}
		}else {
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
			return resultMap;
		}

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}
}

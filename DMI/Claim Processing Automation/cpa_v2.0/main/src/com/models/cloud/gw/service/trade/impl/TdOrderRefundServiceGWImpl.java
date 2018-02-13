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
import com.models.cloud.gw.service.trade.TdOrderRefundServiceGW;
import com.models.cloud.pay.escrow.alipay.dto.BaseDTO;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundParam;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundQueryParam;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundDetailParam.TradeRefundData;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundParam.RefundTradeDetailParam;
import com.models.cloud.pay.escrow.alipay.response.AlipayRefundQueryResponse;
import com.models.cloud.pay.escrow.alipay.response.RefundTradeDetailResponse;
import com.models.cloud.pay.escrow.alipay.response.RefundTradeDetailResponse.AlipayRefundDetailResponse.TradeRefundResponse;
import com.models.cloud.pay.escrow.alipay.service.AlipayService;
import com.models.cloud.pay.escrow.chinapay.service.CPNetPayService;
import com.models.cloud.pay.escrow.mi.pangu.request.MzTollReq;
import com.models.cloud.pay.escrow.mi.pangu.request.TollInfoReq;
import com.models.cloud.pay.escrow.mi.pangu.response.MzBackResultRes;
import com.models.cloud.pay.escrow.mi.pangu.response.OrderStatusRes;
import com.models.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.models.cloud.pay.escrow.mi.pangu.service.CommInfoService;
import com.models.cloud.pay.escrow.mi.pangu.service.OutpatientService;
import com.models.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.models.cloud.pay.escrow.wechat.service.WeChatAppPayService;
import com.models.cloud.pay.payment.service.ErrorCodeService;
import com.models.cloud.pay.payment.service.PaymentService;
import com.models.cloud.pay.payuser.entity.ActPerson;
import com.models.cloud.pay.payuser.entity.PayUser;
import com.models.cloud.pay.payuser.service.ActPersonService;
import com.models.cloud.pay.payuser.service.PayUserService;
import com.models.cloud.pay.proplat.entity.PpFundPlatform;
import com.models.cloud.pay.proplat.service.PpFundPlatformService;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.pay.supplier.service.ActSpService;
import com.models.cloud.pay.trade.entity.TdMiPara;
import com.models.cloud.pay.trade.entity.TdOrdExtPay;
import com.models.cloud.pay.trade.entity.TdOrdExtRet;
import com.models.cloud.pay.trade.entity.TdOrder;
import com.models.cloud.pay.trade.service.TdOrderRefundService;
import com.models.cloud.pay.trade.service.TdOrderService;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.DateUtils;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.HttpRequest;
import com.models.cloud.util.IdCreatorUtils;
import com.models.cloud.util.Utils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;

/**
 * 商户通用接口处理 退款部分
 */
@Service("tdOrderRefundServiceGWImpl")
public class TdOrderRefundServiceGWImpl implements TdOrderRefundServiceGW {

	/**
	 * 日志
	 */
	private static final Logger logger = Logger.getLogger(TdOrderRefundServiceGWImpl.class);
	
	//平台商户商户服务
	@Resource(name = "actSpServiceImpl")
	private ActSpService actSpService;
	//订单服务
	@Resource(name = "tdOrderServiceImpl")
	private TdOrderService tdOrderService;
	//一键支付服务接口
	@Resource(name = "yeepayPaymentServiceImpl")
	private PaymentService yeepayPaymentService;
	//调用支付返回码处理服务
	@Resource(name = "yeepayErrorCodeServiceImpl")
	private ErrorCodeService errorCodeService;
	//退款服务
	@Resource(name="tdOrderRefundServiceImpl")
	private TdOrderRefundService tdOrderRefundService;
	//银联服务
	@Resource(name="cPNetPayServiceImpl")
	private CPNetPayService cPNetPayService;
	//支付宝服务
	@Resource(name="aliPayServiceImpl")
	private AlipayService alipayService;	
	@Resource
	private PpFundPlatformService ppFundPlatformServiceImpl;
	//社保撤消服务
	@Resource(name="outpatientServiceImpl")
	private OutpatientService outpatientService;
	//
	@Resource(name="payUserServiceImpl")
	private PayUserService payUserService;
	//私人帐户服务
	@Resource(name="actPersonServiceImpl")
	private ActPersonService actPersonServie;
	@Resource
	private TdOrderService tdOrderServiceImpl;
	@Resource
	private PayUserService payUserServiceImpl;
	@Resource
	private CommInfoService commInfoServiceImpl;
	@Resource
	private WeChatAppPayService weChatAppPayServiceImpl;

	public Map<String, Object> prePayOrderRefund(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> prePayOrderRefund 输入参数：" + inputMap);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String appId = String.valueOf(inputMap.get("appId")).trim();//渠道商户ID
		String oriTdId = String.valueOf(inputMap.get("oriTdId")).trim();//原始支付交易单ID
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户退款订单号
		String cause = String.valueOf(inputMap.get("cause")).trim(); //退款说明
		Short transBusiType = Short.valueOf(String.valueOf(inputMap.get("transBusiType")).trim());//交易业务类型 201-缴费 202-挂号 203-购药
		BigDecimal payRefundMoney = BigDecimal.valueOf(Double.parseDouble(String.valueOf(inputMap.get("payRefundMoney")).trim())).setScale(2, BigDecimal.ROUND_HALF_UP);//退款金额
		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();  //终端类型
		String systemId = String.valueOf(inputMap.get("systemId")).trim();         //系统ID
		String allowRefund = String.valueOf(inputMap.get("allowRefund")).trim();
        //判断退款金额是否大于0
		if (payRefundMoney.compareTo(BigDecimal.ZERO)<=0){
			logger.info("payRefundMoney=" + payRefundMoney + " 退款金额无效");
			resultMap.put("resultCode", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getCodeString());
			resultMap.put("resultDesc", Hint.YEEPAY_13012_ORDER_AMOUNT_TOO_LOW.getMessage());
			return resultMap;
		}
        //获取渠道商户
		ActSp channelSp = actSpService.findByChannelAppId(appId);
		if(null == channelSp){
			logger.info("CHAN_APPID=" + appId + " 渠道商户不存在");
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		//校验渠道商户状态是否正常状态
		if(channelSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			logger.info("CHAN_APPID=" + appId + " 渠道商户状态不正常");
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}	
		
		//判断当前订单号是否已成功退款
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", merOrderId);
		params.put("chanAppId", appId);
		TdOrdExtRet successOrdExtRet = tdOrderRefundService.findTdOrdExtRetByMerOrderId(params);
		if(null != successOrdExtRet){
			logger.info("orderCode=" + merOrderId + " 该订单已成功退款");
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCode());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			resultMap.put("payRefundOrderId", successOrdExtRet.getTdId().toString());//退款交易单号
			resultMap.put("OriPayOrderId", successOrdExtRet.getRetTdId());//原支付交易单号
			resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
			resultMap.put("transBusiType", transBusiType);
			resultMap.put("payRefundMoney", payRefundMoney.toString());
			return resultMap;
		}
        //判断是否已存在商户退款订单号
		Long orderCount = tdOrderRefundService.findTdOrdCountByMerOrderId(params);
		if(orderCount>0){
			logger.info("oriTdId=" + oriTdId + "商户退款订单号已存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_14016_APP_ID_INCONFORMITY.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14016_APP_ID_INCONFORMITY.getMessage());
			return resultMap;
		}
		//判断当前是否存在退款未完成
		List<TdOrdExtRet> underwayOrdExtRetList = tdOrderRefundService.findTdOrdExtRetByRetTdId(Long.parseLong(oriTdId));
		if(null!= underwayOrdExtRetList && underwayOrdExtRetList.size() > 0){
			logger.info("oriTdId=" + oriTdId + "原有订单存在正在退款业务");
			resultMap.put("resultCode", Hint.PAY_ORDER_14009_REFUND_UNDERWAY.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14009_REFUND_UNDERWAY.getMessage());
			return resultMap;
		}
		//获取原交易单数据
		TdOrder oriTdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf(oriTdId));
		if(null == oriTdOrder){
			logger.info("oriTdOrder=" + oriTdId + "退款原有交易单不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getMessage());
			return resultMap;
		}
		//校验支付和退款的渠道商户是否一致
		if(!channelSp.getActId().equals(oriTdOrder.getChanActId())){
			logger.info("退款渠道商户不是原有支付渠道商户");
			resultMap.put("resultCode", Hint.PAY_ORDER_14015_APP_ID_INCONFORMITY.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14015_APP_ID_INCONFORMITY.getMessage());
			return resultMap;
		}
		
		//模式三：判断合作商户资金平台编码是否存在
		if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_3.getCode()){
			ActSp workSp = actSpService.findByWorkSpActId(oriTdOrder.getDispSpActId());
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
			if(ValidateUtils.isBlank(workSp.getSpFundCode())){
				resultMap.put("resultCode", Hint.PAY_ORDER_13098.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13098.getMessage());
				return resultMap;
			}
			
		}
		
		//获取原交易单交易扩展表-支付
		TdOrdExtPay oriTdOrdExtPay = tdOrderService.findTdOrdExtPayByPayOrderId(Long.valueOf(oriTdId));
		if(null == oriTdOrdExtPay){
			logger.info("oriTdOrder=" + oriTdOrder + "退款原有交易单支付扩展信息不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getMessage());
			return resultMap;
		}
		//判断交易状态(商业支付状态)是否为成功，交易类型是否与当前退款交易相同
		if(!"true".equals(allowRefund) && (oriTdOrdExtPay.getTdBusiStatId() != BaseDictConstant.TD_STAT_ID_SUCCESS ||
		   oriTdOrdExtPay.getPayTypeId() != DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode())){
			logger.info("oriTdOrder=" + oriTdOrder + "原交易订单非支付成功或非纯商业支付");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14002_TDSTATID_NON_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14002_TDSTATID_NON_SUCCESS.getMessage());
			return resultMap;
		}
		BigDecimal accRetAmt = oriTdOrdExtPay.getAccRetAmt();
		if(null == accRetAmt || accRetAmt.doubleValue() <= 0){
			accRetAmt = BigDecimal.ZERO;
		}
		//判断交易单类型是否相同
		if(!oriTdOrder.getTdBusiTypeId().equals(transBusiType)){
			logger.info("oriTdOrder=" + oriTdOrder + "退款交易单类型与原交易单类型不一致");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14003_TDTYPEID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14003_TDTYPEID_INVALID.getMessage());
			return resultMap;
		}
		//判断原有交易单累计退款金额+当前退款金额是否小于交易金额
		if((accRetAmt.add(payRefundMoney)).compareTo(oriTdOrdExtPay.getPaySelf()) > 0){
			logger.info("oriTdOrder=" + oriTdOrder + "退款金额大于原交易单剩余可退金额");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14005_INSUFFICIENT_BALANCE.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14005_INSUFFICIENT_BALANCE.getMessage());
			return resultMap;
		}
		//当前日期
		Date curDate = new Date();
		//生成退款交易单
		TdOrder tdOrder = new TdOrder();
		tdOrder.setTdId(Long.parseLong(IdCreatorUtils.getRefundOrderId()));//退款订单号
		tdOrder.setTdOrder(BigDecimal.valueOf(BaseDictConstant.TRANSACTION_SERIAL_NUMBER));//交易序号
		tdOrder.setMicId(channelSp.getMicId());//医保项目ID
		tdOrder.setTdTypeId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_TYPE_REFUND.getCode())));//交易类型退款
		tdOrder.setTdBusiTypeId(transBusiType);//交易业务类型
		tdOrder.setChanActId(channelSp.getActId());//渠道账户ID
		tdOrder.setChanAppid(appId);//渠道账户APPID
		tdOrder.setTdOperChanTypeId(Short.valueOf(terminalType));//交易操作通道类别
		tdOrder.setFromActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_MERCHANT.getCode())));//付款账户类型
		tdOrder.setFromActId(oriTdOrder.getToActId());//付款账户ID
		tdOrder.setToActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_PRIVATE.getCode())));//收款账户类型
		tdOrder.setToActId(oriTdOrder.getFromActId());//收款账户ID
		tdOrder.setDispSpActId(oriTdOrder.getDispSpActId());//显示商户账户ID(合作商户)
		tdOrder.setDispSpName(oriTdOrder.getDispSpName());//显示商户名称
		tdOrder.setFundTdCode(oriTdOrder.getFundTdCode());
		tdOrder.setGoodsDesc(cause);//退款原因
		tdOrder.setFundId(oriTdOrder.getFundId());//资金平台ID
		tdOrder.setPayTot(payRefundMoney);//交易总金额
		tdOrder.setRecvCur(payRefundMoney);//本次实退金额
		tdOrder.setOrigOrdCode(merOrderId);//渠道商户订单号
		tdOrder.setSpSubSysCode(systemId);//商户子系统编号
		tdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCode())));//交易状态
		tdOrder.setTdDate(DateUtils.getNowDate("yyyyMMdd"));//交易日期
		tdOrder.setTdStartTime(curDate);//申请交易时间
		tdOrder.setConfirmStatId(Short.valueOf(String.valueOf(DimDictEnum.CONFIRM_STAT_WAIT_CHECK.getCode())));//对账状态
		tdOrder.setCrtTime(curDate);//创建时间

		logger.info("保存退款交易单信息入库 payRefundOrderId=" + tdOrder.getTdId());
		tdOrderService.saveTdOrderInfo(tdOrder);
		logger.info("<<保存成功");
		
		//增加退款交易单退款扩展表
		TdOrdExtRet tdOrdExtRet = new TdOrdExtRet();
		tdOrdExtRet.setTdId(tdOrder.getTdId());//交易单ID
		tdOrdExtRet.setRetTdId(Long.parseLong(oriTdId));
		tdOrdExtRet.setSearchCnt(0); //默认查询次数为0
		tdOrdExtRet.setTdRetStatId(BaseDictConstant.TD_RET_STAT_ID_INI );//状态运行中
		tdOrdExtRet.setCrtTime(curDate);
		tdOrdExtRet.setUpdTime(curDate);
		logger.info("保存退款交易单扩展信息入库 payRefundOrderId=" + tdOrder.getTdId());
		tdOrderRefundService.saveTdOrdExtRetInfo(tdOrdExtRet);
		logger.info("<<保存成功");

		//更新支付信息
		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(oriTdOrder.getTdId());//交易单号
		updOrdExtPay.setTdRetFlag(Short.valueOf("1"));//是否被退款，1是0否
		updOrdExtPay.setAccRetAmt(accRetAmt.add(payRefundMoney));//累计退款金额
		updOrdExtPay.setUpdTime(new Date());
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrderService.updateTdOrdExtPayInfo(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}
		
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("payRefundOrderId", tdOrder.getTdId());//退款交易单号
		resultMap.put("oriPayOrderId", oriTdId);//原支付交易单号
		resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
		resultMap.put("cause", cause);
		resultMap.put("transBusiType", transBusiType);
		resultMap.put("payRefundMoney", payRefundMoney);
		
		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.gw.service.trade.TdOrderRefundServiceGW#PayOrderRefund(java.util.Map)
	 */
	@Override
	public Map<String, Object> PayOrderRefund(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> PayOrderRefund 输入参数：" + inputMap);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		String amount = String.valueOf(inputMap.get("amount"));
		String orderid = String.valueOf(inputMap.get("orderId"));
		String fundid = String.valueOf(inputMap.get("fundId"));
		String ybOrderId = String.valueOf(inputMap.get("ybOrderId"));
		String cause = String.valueOf(inputMap.get("cause"));
		
		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(fundid);
		if(null == ppFundPlatform){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
	  		resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
	  		return resultMap;
		}
		if(BaseDictConstant.PP_FUND_TYPE_ID_EBZF.equals(ppFundPlatform.getPpFundTypeId())){

			/**********************************调用第三方易pay退款 begin*************************************/
			Map<String, String> reqDataMap = new HashMap<String, String>();
			//单位：分
			reqDataMap.put("amount", new BigDecimal(amount).multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			reqDataMap.put("orderid", orderid);
			reqDataMap.put("currency", BaseDictConstant.YEEPAY_TRADE_CURRENCY_RMB);
			reqDataMap.put("cause", cause);
			reqDataMap.put("origyborderid", ybOrderId);
			reqDataMap.put("fundid", fundid);
			logger.info("易宝退款，请求参数：" + reqDataMap);
			Map<String, String> yeepayResultMap = yeepayPaymentService.refund(reqDataMap);
			logger.info("<<响应报文：" + yeepayResultMap);
			 //校验返回结果
		    Map<String,String> supplyResultMap = errorCodeService.getSupplyResultMap(yeepayResultMap);
		    if(supplyResultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
		    	//退款提交成功
		    	resultMap.put("fundTdCode",yeepayResultMap.get("yborderid"));
		    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		  		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		    }else if(supplyResultMap.get("resultCode").equals(Hint.SP_14112_REPEAT_REFUND_ORDER.getCodeString())){
		    	//订单重复提交认为提交成功
		    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		  		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		    }else{
		    	//订单失败
		    	resultMap.putAll(supplyResultMap);
		    }
		    return resultMap;
			/**********************************调用第三方易pay退款 end*************************************/

		}else if(BaseDictConstant.PP_FUND_TYPE_ID_CCB.equals(ppFundPlatform.getPpFundTypeId())){

			/**********************************调用第三方建行退款 begin*************************************/
			resultMap = CCBRefund(inputMap, 1);
			return resultMap;
			/**********************************调用第三方建行退款 end*************************************/

		}else if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundPlatform.getPpFundTypeId())
				|| BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundPlatform.getPpFundTypeId())){

			/**********************************调用第三方支付宝退款 begin*************************************/
			resultMap = ALIPAYRefund(inputMap, 1);
			return resultMap;
			/**********************************调用第三方支付宝退款 end*************************************/

		}else if(BaseDictConstant.PP_FUND_TYPE_ID_BKU.equals(ppFundPlatform.getPpFundTypeId())){

			/**********************************调用银联退款 begin*************************************/
			resultMap = BKURefund(inputMap, 1);
			return resultMap;
			/**********************************调用银联退款 end*************************************/

		}else if(BaseDictConstant.PP_FUND_TYPE_ID_WECHAT.equals(ppFundPlatform.getPpFundTypeId())){

			/**********************************调用微信退款 begin*************************************/
			resultMap = weChatRefund(inputMap, 1);
			return resultMap;
			/**********************************调用微信退款 end*************************************/
		}else{
			resultMap.put("resultCode", Hint.PAY_ORDER_13082.getCodeString());
	  		resultMap.put("resultDesc", Hint.PAY_ORDER_13082.getMessage());
			return resultMap;
		}
	    
	}
	
	@Override
	public Map<String, Object> payOrderRefundQuery(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> PayOrderRefundQuery 输入参数：" + inputMap);
		//返回结果定义
		Map<String, Object> resultMap = new HashMap<>();
		//入口参数
		String appId = String.valueOf(inputMap.get("appId")).trim();//渠道商户ID
		String payRefundOrderId = String.valueOf(inputMap.get("payRefundOrderId")).trim();//退款交易单ID
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户退款订单号
		 //获取渠道商户
		ActSp channelSp = actSpService.findByChannelAppId(appId);
		if(null == channelSp){
			logger.info("渠道商户不存在 appId=" + appId);
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		//获取交易单
		TdOrder tdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf(payRefundOrderId));
		if(null == tdOrder){
			logger.info("payRefundOrderId=" + payRefundOrderId + " 退款交易单号不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_14010_REFUND_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14010_REFUND_TDID_INVALID.getMessage());
			return resultMap;
		}
		//校验交易单所对应的订单号是否与参数订单号一致
		if(!tdOrder.getOrigOrdCode().equals(merOrderId)){
			logger.info("退款交易单号=" + payRefundOrderId + "与退款定单号=" + merOrderId + "不一致");
			resultMap.put("resultCode", Hint.PAY_ORDER_14011_ORDERCODE_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14011_ORDERCODE_TDID_INVALID.getMessage());
			return resultMap;
		}
		//校验退款订单状态
		if(DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode() == tdOrder.getTdStatId()){
			//退款成功
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			resultMap.put("payRefundMoney", tdOrder.getPayTot());//退款金额
			resultMap.put("cause", tdOrder.getGoodsDesc());//退款原因
			resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
			resultMap.put("status",BaseDictConstant.TD_STAT_ID_SUCCESS);//状态 成功
			resultMap.put("payRefundOrderId", payRefundOrderId);//退款交易单号
		}else {
			//退款进行中
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			resultMap.put("payRefundMoney", tdOrder.getPayTot());//退款金额
			resultMap.put("cause", tdOrder.getGoodsDesc());//退款原因
			resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
			resultMap.put("status",BaseDictConstant.TD_STAT_ID_DOING);//状态 进行中
			resultMap.put("payRefundOrderId", payRefundOrderId);//退款交易单号
		}
	    return resultMap;  
	}

	@Override
	public Map<String, Object> payRefundQuery(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> payRefundQuery 输入参数：" + inputMap);
		//返回结果定义
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String orderId = String.valueOf(inputMap.get("orderId")).trim();//客户退货订单号
		String ybOrderId = String.valueOf(inputMap.get("ybOrderId")).trim();//易宝退货流水
		String fundId = String.valueOf(inputMap.get("fundId")).trim();//渠道类型
		
		PpFundPlatform ppFundPlatform = CacheUtils.getPpFundPlatform(fundId);
		if(null == ppFundPlatform){
			resultMap.put("resultCode", Hint.PAY_ORDER_13096.getCodeString());
	  		resultMap.put("resultDesc", Hint.PAY_ORDER_13096.getMessage());
	  		return resultMap;
		}
		if(BaseDictConstant.PP_FUND_TYPE_ID_EBZF.equals(ppFundPlatform.getPpFundTypeId())){
			logger.info("易宝退货退款记录查询，请求参数：" + orderId + ";" + ybOrderId);
	    	Map<String, String> yeepayResultMap = yeepayPaymentService.refundQuery(orderId, ybOrderId);
		    logger.info("<<响应报文：" + yeepayResultMap);
		  //校验返回结果
		    Map<String,String> supplyResultMap = errorCodeService.getSupplyResultMap(yeepayResultMap);
		    if(!supplyResultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
		    	//有错误
		    	resultMap.putAll(supplyResultMap);
		    }else{
		    	resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				//退款金额（单位：分）转换成元
				resultMap.put("payRefundMoney", yeepayResultMap.get("amount"));
				resultMap.put("fundTdCode", yeepayResultMap.get("yborderid"));//易宝退货流水号
//				resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
//				resultMap.put("payRefundOrderId", payRefundOrderId);//退款交易单号
				String status = String.valueOf(yeepayResultMap.get("status"));//退款状态
				if(status.equals("4")){
					status = BaseDictConstant.TD_STAT_ID_DOING + "";
				}else if (status.equals("5")){
					status = BaseDictConstant.TD_STAT_ID_SUCCESS + "";
				}
				resultMap.put("status",status);//状态
		    }
		    return resultMap;  
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_CCB.equals(ppFundPlatform.getPpFundTypeId())){
			
			//**************************第三方建行确认退款*********************************************
			resultMap = CCBRefund(inputMap, 2);
			return resultMap;  
			
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundPlatform.getPpFundTypeId())
				|| BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundPlatform.getPpFundTypeId())){
			
			/**********************************调用支付宝退款查询 begin*************************************/
			resultMap = ALIPAYRefund(inputMap,2);
			return resultMap; 
			/**********************************调用支付宝退款查询 end*************************************/
			
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_BKU.equals(ppFundPlatform.getPpFundTypeId())){

			/**********************************调用银联退款查询 begin*************************************/
			resultMap = BKURefund(inputMap, 2);
			return resultMap;
			/**********************************调用银联退款查询 end*************************************/
			
		}else if(BaseDictConstant.PP_FUND_TYPE_ID_WECHAT.equals(ppFundPlatform.getPpFundTypeId())){
			/**********************************调用微信退款查询 begin*************************************/
			resultMap = weChatRefund(inputMap, 2);
			return resultMap;
			/**********************************调用微信退款查询 end*************************************/
		}else{
			resultMap.put("resultCode", Hint.PAY_ORDER_13082.getCodeString());
	  		resultMap.put("resultDesc", Hint.PAY_ORDER_13082.getMessage());
			return resultMap;
		}
	}
	
	public Map<String,Object> CCBRefund(Map<String,Object> inputMap, int doType){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//1-申请退款 2-查询退款结果
		if(doType == 1){
			map.put("interfaceCode", "merchantSingleRefund");
			Random random = new Random();
			//请求序列号
			map.put("REQUEST_SN", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+random.nextInt(2));
			//订单号
			map.put("ORDER", inputMap.get("oriTdId")+"_"+inputMap.get("orderIdSign"));
//			map.put("ORDER", "1000000003");
			//退款金额(单位：元）
		    map.put("MONEY",  inputMap.get("amount"));
//			map.put("MONEY",  "0.01");
			map.put("FundId",  inputMap.get("fundId"));
			if(logger.isInfoEnabled()){
				logger.info("向【建行】申请退款接口，请求参数map："+map);
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
				logger.info("向【建行】申请退款接口，请求参数："+url+data);
			}
			String resultStr = HttpRequest.doPostJson(url, JSON.toJSONString(data));
			Map<String,Object>  responseMap= JSON.parseObject(resultStr);
			if(logger.isInfoEnabled()){
				logger.info("向【建行】申请退款接口，返回结果："+responseMap);
			}
			if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.get("resultCode"))){
				//退款申请成功
				Map<String,Object> dataMap = new HashMap<String,Object>();
				dataMap = JSON.parseObject((String) responseMap.get("data"));
				logger.info("dataMap:"+dataMap);
				if(!dataMap.containsKey("TX_INFO")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
				
				resultMap.put("fundTdCode","");
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				return resultMap;
			}else{
				//订单失败
				resultMap.put("resultCode", (String)responseMap.get("resultCode"));
				resultMap.put("resultDesc", (String)responseMap.get("resultDesc"));
				return resultMap;
			}
			
		}else if(doType == 2){
			map.put("interfaceCode", "merchantRefundQuery");
			Random random = new Random();
			map.put("REQUEST_SN", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+random.nextInt(2));
			//流水类型: 0:未结流水,1:已结流水
			map.put("KIND", "0");
			//订单号
			map.put("ORDER", inputMap.get("oriTdId")+"_"+inputMap.get("orderIdSign"));
			//排序 : 1:交易日期,2:订单号
			map.put("NORDERBY", "2");
			//当前页次
			map.put("PAGE", "1");
			//流水状态：0:交易失败,1:交易成功,2:待银行确认(针对未结流水查询);3:全部
			map.put("STATUS", "3");
			map.put("FundId",  inputMap.get("fundId"));
			if(logger.isInfoEnabled()){
				logger.info("查询【建行】退款结果，请求参数map："+map);
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
				logger.info("查询【建行】退款结果，请求参数："+url+data);
			}
			String resultStr = HttpRequest.doPostJson(url, JSON.toJSONString(data));
			Map<String,Object>  responseMap= JSON.parseObject(resultStr);
			if(logger.isInfoEnabled()){
				logger.info("查询【建行】退款结果，返回结果："+responseMap);
			}
			if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.get("resultCode"))){
				//退款申请成功
				Map<String,Object> dataMap = new HashMap<String,Object>();
				dataMap = JSON.parseObject((String) responseMap.get("data"));
				logger.info("dataMap:"+dataMap);
				if(!dataMap.containsKey("TX_INFO")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
					
				Map<String,Object> infoMap = new HashMap<String,Object>();
				infoMap = (Map<String, Object>) dataMap.get("TX_INFO");
				if(!infoMap.containsKey("LIST")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
				Map<String,Object> listMap = new HashMap<String,Object>();
				listMap =  (Map<String, Object>) infoMap.get("LIST");
				if(!listMap.containsKey("STATUS")){
					resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
					return resultMap;
				}
				String status = (String) listMap.get("STATUS");
				if(logger.isInfoEnabled()){
					logger.info("查询【建行】退款结果，状态："+status);
				}
				//订单状态（0：失败，1：成功，2：待银行确认，5：待银行确认）
				if("1".equals(status)){
					status = BaseDictConstant.TD_STAT_ID_SUCCESS + "";
				}else if("2".equals(status) || "5".equals(status)){
					status = BaseDictConstant.TD_STAT_ID_DOING + "";
				}else if("0".equals(infoMap.get("STATUS"))){
					status = BaseDictConstant.TD_STAT_ID_FAIL + "";
				}
				
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				//退款金额（单位：元）
				resultMap.put("payRefundMoney", new BigDecimal(String.valueOf(listMap.get("REFUNDEMENT_AMOUNT")))
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				resultMap.put("fundTdCode", "");//易宝退货流水号
				resultMap.put("status",status);//状态
				return resultMap;
			}else{
				//订单失败
				resultMap.put("resultCode", (String)responseMap.get("resultCode"));
		  		resultMap.put("resultDesc", (String)responseMap.get("resultDesc"));
				return resultMap;
			}
		}
		
		return resultMap;
	}
	
	public Map<String,Object> BKURefund(Map<String,Object> inputMap, int doType){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//查询原始订单
		try {
			TdOrder oriOrdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf( String.valueOf(inputMap.get("oriTdId"))));
			TdOrder tdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf(String.valueOf(inputMap.get("orderId"))));
			if(logger.isInfoEnabled()){
				logger.info("查询交易订单信息："+tdOrder);
				logger.info("查询交易原始订单信息："+oriOrdOrder);
			}
			if(null == tdOrder || null == oriOrdOrder){
				if(logger.isInfoEnabled()){
					logger.info("支付订单或原始订单信息不存在");
				}
				resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
				return resultMap;
			}
			//1-申请退款 2-查询退款结果
			if(doType == 1){
				//查询商户信息
				ActSp channelSp = actSpService.selectByActId(tdOrder.getChanActId());
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
				if(logger.isInfoEnabled()){
					logger.info( "订单号："+inputMap.get("orderId")+"，渠道商户收单模式：" + channelSp.getFundModelId());
				}
				
				//请求参数
				//资金平台ID
				map.put("FundId", tdOrder.getFundId());
				//订单号
				map.put("MerOrderNo", tdOrder.getTdId()+"_"+inputMap.get("orderIdSign"));
				//商户交易日期yyyyMMdd
				map.put("TranDate", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyyMMdd"));
				//商户交易时间HHmmss
				map.put("TranTime", DateUtils.dateToString(tdOrder.getCrtTime(), "hhmmss"));
				//原始交易订单号
				map.put("OriOrderNo", oriOrdOrder.getTdId()+"_"+inputMap.get("orderIdSign"));
				//原始商户交易日期
				map.put("OriTranDate", DateUtils.dateToString(oriOrdOrder.getCrtTime(), "yyyyMMdd"));
				//退款订单金额（单位：元）
				map.put("RefundAmt", inputMap.get("amount"));
				//MerResv
				map.put("MerResv", tdOrder.getFromActId());
				
				Map<String,Object> responseMap = new HashMap<String,Object>();
				if(DimDictEnum.FUND_MODEL_ID_2.getCode() == channelSp.getFundModelId().intValue()){
					//多商户直接收单-------后续类交易-退款
					if(logger.isInfoEnabled()){
						logger.info("多商户直接收单，向【银联】发起退款申请，请求参数："+map);
					}
					responseMap= cPNetPayService.consumePaymentRefund(map);
					if(logger.isInfoEnabled()){
						logger.info("多商户直接收单，向【银联】发起退款申请，返回结果："+responseMap);
					}
				}else if(DimDictEnum.FUND_MODEL_ID_3.getCode() == channelSp.getFundModelId().intValue()){
					//通道直转子账户(内部户)-------后续类分账交易-退款
					//分账商户号--资金平台商户编码
					map.put("MerSplitMsg", channelSp.getSpFundCode());
					if(logger.isInfoEnabled()){
						logger.info("通道直转子账户，向【银联】发起退款申请，请求参数："+map);
					}
					responseMap = cPNetPayService.consumeSplitPaymentRefund(map);
					if(logger.isInfoEnabled()){
						logger.info("通道直转子账户，向【银联】发起退款申请，返回结果："+responseMap);
					}
				}else{
					if(logger.isInfoEnabled()){
						logger.info("渠道商户不支持此支付模式");
					}
					resultMap.put("resultCode", Hint.PAY_ORDER_13099.getCodeString());
					resultMap.put("resultDesc", Hint.PAY_ORDER_13099.getMessage());
					return resultMap;
				}
				
				if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.get("resultCode"))){
					resultMap.put("fundTdCode",responseMap.get("AcqSeqId"));//银联退货流水号
					resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					return resultMap;
				}else{
					resultMap.put("resultCode", responseMap.get("resultCode").toString());
					resultMap.put("resultDesc", responseMap.get("resultDesc").toString());
					return resultMap;
				}
			}else if(doType == 2){
				//请求参数
				//资金平台ID
				map.put("FundId", tdOrder.getFundId());
				//订单号
				map.put("MerOrderNo", tdOrder.getTdId()+"_"+inputMap.get("orderIdSign"));
				//原始商户交易日期
				map.put("TranDate", DateUtils.dateToString(tdOrder.getCrtTime(), "yyyyMMdd"));
				
				Map<String,Object> responseMap = new HashMap<String,Object>();
				if(logger.isInfoEnabled()){
					logger.info("查询【银联】退款结果，请求参数："+map);
				}
				responseMap= cPNetPayService.consumePaymentQuery(map);
				if(logger.isInfoEnabled()){
					logger.info("查询【银联】退款结果，返回结果："+responseMap);
				}
				if(Hint.SYS_SUCCESS.getCodeString().equals(responseMap.get("resultCode"))){
					String status = (String)responseMap.get("OrderStatus");
					if(logger.isInfoEnabled()){
						logger.info("查询【银联】退款结果，状态："+status);
					}
					//0008-退款交易成功 0009-退款交易失败 1004-交易退款中2034-重复退款 1003-商户已审核
					if("0008".equals(status)){
						status = BaseDictConstant.TD_STAT_ID_SUCCESS + "";
					}else if("0009".equals(status) || "2034".equals(status)){
						status = BaseDictConstant.TD_STAT_ID_FAIL + "";
					}else if("1004".equals(status) || "1003".equals(status)){
						status = BaseDictConstant.TD_STAT_ID_DOING + "";
					}
					resultMap.put("fundTdCode",responseMap.get("AcqSeqId"));//银联退货流水号
					resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					//退款金额（单位：分）转换成元
					resultMap.put("payRefundMoney", new BigDecimal((String)responseMap.get("RefundSumAmount")).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					resultMap.put("status",status);//状态
					return resultMap;
				}else{
					resultMap.put("resultCode", responseMap.get("resultCode").toString());
					resultMap.put("resultDesc", responseMap.get("resultDesc").toString());
					return resultMap;
				}
			}
			
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("【银联】退款操作抛出异常--"+e);
			}
			resultMap.put("resultCode", Hint.SP_14101_SYSTEM_ABNORMAL.getCodeString());
			resultMap.put("resultDesc", Hint.SP_14101_SYSTEM_ABNORMAL.getMessage());
			return resultMap;
		}
		
		return resultMap;
	}
	
	/**
	 * 支付宝退款 ：doType=1 申请退款  =2退款结果查询
	 * @param @param inputMap
	 * @param @param doType
	 * @param @return
	 *
	 */
	private Map<String,Object> ALIPAYRefund(Map<String,Object> inputMap, int doType){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//查询原始订单
		try {
			//1-申请退款 2-查询退款结果
			if(doType == 1){
				AlipayRefundParam alipay = new AlipayRefundParam();
				List<RefundTradeDetailParam> refundDetailDataList = new ArrayList<RefundTradeDetailParam>();
				RefundTradeDetailParam refundDetailData = new RefundTradeDetailParam();
				TradeRefundData tradeRefund = new TradeRefundData();
				//原付款支付宝交易号
				tradeRefund.setTrade_no((String) inputMap.get("fundTdCode"));
				//退款总金额(单位：元)
				tradeRefund.setRefund_amount((String) inputMap.get("amount"));
				//退款理由
				tradeRefund.setRefund_reason((String) inputMap.get("cause"));
				refundDetailData.setTradeRefund(tradeRefund);
				refundDetailDataList.add(refundDetailData);
				//退款请求的明细数据
				alipay.setRefund_detail_data(refundDetailDataList);
				//退款批次号
				Random random = new Random();
				alipay.setBatch_no(DateUtils.dateToString(new Date(), "yyyyMMddHHmmss")+random.nextInt(10));
				//退款请求时间
				String refundTime = DateUtils.dateToString(new Date(), "yyyy-MM-dd hh:mm:ss");
				alipay.setRefund_date(refundTime);
				//退款总笔数
				alipay.setBatch_num("1");
				if(logger.isInfoEnabled()){
					logger.info("向【支付宝】发起退款申请，请求参数：退款批次号："+alipay.getBatch_no()
					+" ,退款时间："+refundTime+" ,fundId："+String.valueOf(inputMap.get("fundId"))
					+" ,支付宝交易号："+tradeRefund.getTrade_no()+" ,退款金额："+tradeRefund.getRefund_amount());
				}
				BaseDTO<Boolean> responseDTO = alipayService.refundTrade(alipay,  String.valueOf(inputMap.get("fundId")));
				if(responseDTO == null){
					resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
					return resultMap;
				}
				if(logger.isInfoEnabled()){
					logger.info("向【支付宝】发起退款申请，返回结果："+responseDTO.getResultCode());
				}
				if(Hint.SYS_SUCCESS.getCodeString().equals(responseDTO.getResultCode())){
//					resultMap.put("fundTdCode",);//银联退货流水号
					resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					return resultMap;
				}else{
					resultMap.put("resultCode", responseDTO.getResultCode());
					resultMap.put("resultDesc", responseDTO.getResultDesc());
					return resultMap;
				}
			}else if(doType == 2){
				AlipayRefundQueryParam alipay = new AlipayRefundQueryParam();
				//支付宝交易号
				alipay.setTrade_no((String)inputMap.get("fundTdCode"));
				//批次号
//				alipay.setBatch_no("");
				if(logger.isInfoEnabled()){
					logger.info("查询【支付宝】退款结果，请求参数：支付宝交易号："+alipay.getTrade_no()
					+" ,fundId："+String.valueOf(inputMap.get("fundId")));
				}
				BaseDTO<AlipayRefundQueryResponse> responseDTO = alipayService.refundQuery(alipay, String.valueOf(inputMap.get("fundId")));
				if(null == responseDTO){
					resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
					return resultMap;
				}
				if(logger.isInfoEnabled()){
					logger.info("查询【支付宝】退款结果，返回结果："+responseDTO.getResultCode());
				}
				
				if(Hint.SYS_SUCCESS.getCodeString().equals(responseDTO.getResultCode())){
					AlipayRefundQueryResponse response = responseDTO.getResult();
					if(null == response){
						resultMap.put("resultCode", Hint.SP_14103_NO_DATA_RETURN.getCodeString());
						resultMap.put("resultDesc", Hint.SP_14103_NO_DATA_RETURN.getMessage());
						return resultMap;
					}
					List<RefundTradeDetailResponse> refundTradeDetailResponseList = RefundTradeDetailResponse.getDetails(response.getResult_details());
					if(logger.isInfoEnabled()){
						logger.info("【支付宝】退款结果，结果详情："+refundTradeDetailResponseList);
					}
					if(refundTradeDetailResponseList.isEmpty() || refundTradeDetailResponseList.size()<=0){
						resultMap.put("resultCode", Hint.SP_14103_NO_DATA_RETURN.getCodeString());
						resultMap.put("resultDesc", Hint.SP_14103_NO_DATA_RETURN.getMessage());
						return resultMap;
					}
					
					//交易退款数据
					TradeRefundResponse tradeRefund = refundTradeDetailResponseList.get(0).getTradeRefund();
					if(logger.isInfoEnabled()){
						logger.info("【支付宝】退款结果，结果详情--交易退款数据："+tradeRefund);
					}
					if(null == tradeRefund){
						resultMap.put("resultCode", Hint.SP_14103_NO_DATA_RETURN.getCodeString());
						resultMap.put("resultDesc", Hint.SP_14103_NO_DATA_RETURN.getMessage());
						return resultMap;
					}
					
					String status = tradeRefund.getResult_code();
					if(logger.isInfoEnabled()){
						logger.info("查询【支付宝】退款结果，状态："+status);
					}
					if("SUCCESS".equals(status)){
						//退款结果：成功
						status = BaseDictConstant.TD_STAT_ID_SUCCESS + "";
					}
					
					resultMap.put("fundTdCode", tradeRefund.getTrade_no());//支付宝退货流水号
					resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					//退款金额（单位：元）支付宝返回结果为元
					resultMap.put("payRefundMoney", tradeRefund.getRefund_amount());
					resultMap.put("status", status);//状态
					return resultMap;
				}else{
					resultMap.put("resultCode", responseDTO.getResultCode());
					resultMap.put("resultDesc", responseDTO.getResultDesc());
					return resultMap;
				}
				
			}
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("【支付宝】退款操作抛出异常--"+e);
			}
			resultMap.put("resultCode", Hint.SP_14101_SYSTEM_ABNORMAL.getCodeString());
			resultMap.put("resultDesc", Hint.SP_14101_SYSTEM_ABNORMAL.getMessage());
			return resultMap;
		}
		
		
		return resultMap;
	}

	/**
	 * 微信退款 ：doType=1 申请退款  doType=2退款结果查询
	 * @param inputMap
	 * @param doType
     * @return
     */
	private Map<String, Object> weChatRefund(Map<String,Object> inputMap, int doType) throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
		//1-申请退款 2-查询退款结果
		if(doType == 1){
			String oriTdId = String.valueOf(inputMap.get("oriTdId")).trim();//支付订单号
			//获取原交易单数据
			TdOrdExtPay oriTdOrdExtPay = tdOrderService.findTdOrdExtPayByPayOrderId(Long.valueOf(oriTdId));
			if(null == oriTdOrdExtPay){
				logger.info("原支付订单号不存在 oriTdId=" + oriTdId);
				resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getMessage());
				return resultMap;
			}
			String fundTdCode = String.valueOf(inputMap.get("fundTdCode")).trim();//微信订单号
			String orderId = String.valueOf(inputMap.get("orderId")).trim();//退款订单号
			Map<String, String> requestMap = new HashMap<>();
			requestMap.put("transaction_id", fundTdCode);
			requestMap.put("out_refund_no", orderId);
			requestMap.put("total_fee", oriTdOrdExtPay.getPaySelf().multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			double refundFee = Double.parseDouble(inputMap.get("amount").toString());
			requestMap.put("refund_fee", BigDecimal.valueOf(refundFee).multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			if(logger.isInfoEnabled()){
				logger.info("微信申请退款，请求报文：" + requestMap);
			}
			Map<String, Object> refundResultMap = weChatAppPayServiceImpl.refundApply(requestMap, String.valueOf(inputMap.get("fundId")).trim());
			if(logger.isInfoEnabled()){
				logger.info("响应报文：" + refundResultMap);
			}
			if(refundResultMap == null || refundResultMap.size() == 0){
				resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
				return resultMap;
			}
			String returnCode = String.valueOf(refundResultMap.get("return_code")).trim();
			String resultCode = String.valueOf(refundResultMap.get("result_code")).trim();
			if(WeChatConstants.RETURN_CODE_SUCCESS.equals(returnCode) && WeChatConstants.RESULT_CODE_SUCCESS.equals(resultCode)){
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", WeChatConstants.RETURN_CODE_FAIL);
			resultMap.put("resultDesc", "退款申请失败");
			return resultMap;
		}else if(doType == 2){
			Map<String, String> requestMap = new HashMap<>();
			requestMap.put("out_refund_no", String.valueOf(inputMap.get("orderId")));
			if(logger.isInfoEnabled()){
				logger.info("微信退款结果查询，请求报文：" + requestMap);
			}
			Map<String, Object> refundResultMap = weChatAppPayServiceImpl.refundQuery(requestMap, String.valueOf(inputMap.get("fundId")).trim());
			if(logger.isInfoEnabled()){
				logger.info("响应报文：" + refundResultMap);
			}
			if(null == refundResultMap || refundResultMap.size() == 0){
				resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getMessage());
				return resultMap;
			}
			String returnCode = String.valueOf(refundResultMap.get("return_code")).trim();
			String resultCode = String.valueOf(refundResultMap.get("result_code")).trim();
			if(WeChatConstants.RETURN_CODE_SUCCESS.equals(returnCode) && WeChatConstants.RESULT_CODE_SUCCESS.equals(resultCode)){
                int refundCount = Integer.parseInt(String.valueOf(refundResultMap.get("refund_count")).trim());
                String num = "";
                String refundStatus = "";
                for(int idx = 0; idx < refundCount; idx++){
                    num = String.valueOf(idx);
                    if(refundResultMap.containsKey("refund_status_".concat(num))){
                        refundStatus = String.valueOf(refundResultMap.get("refund_status_".concat(num))).trim();
                        break;
                    }
                }
                if(WeChatConstants.REFUND_STATUS_SUCCESS.equals(refundStatus)){
                    resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
                    resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
                    resultMap.put("status", String.valueOf(BaseDictConstant.TD_STAT_ID_SUCCESS));
                    resultMap.put("fundTdCode", refundResultMap.get("refund_id_".concat(num)));
                    return resultMap;
                }
			}
			resultMap.put("resultCode", WeChatConstants.RETURN_CODE_FAIL);
			resultMap.put("resultDesc", "退款未完成");
			return resultMap;
		}
		return resultMap;
	}

	//混合支付
	public Map<String, Object> prePayOrderRefundMix(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> prePayOrderRefundMix 输入参数：" + inputMap);
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		String appId = String.valueOf(inputMap.get("appId")).trim();//渠道商户ID
		String oriTdId = String.valueOf(inputMap.get("oriTdId")).trim();//原始支付交易单ID
		String merOrderId = String.valueOf(inputMap.get("merOrderId")).trim();//商户退款订单号
		String cause = String.valueOf(inputMap.get("cause")).trim(); //退款说明
		Short transBusiType = Short.valueOf(String.valueOf(inputMap.get("transBusiType")).trim());//交易业务类型 201-缴费 202-挂号 203-购药
		String terminalType = String.valueOf(inputMap.get("terminalType")).trim();  //终端类型
		String systemId = String.valueOf(inputMap.get("systemId")).trim();         //系统ID
		String allowRefund = String.valueOf(inputMap.get("allowRefund")).trim();

        //获取渠道商户
		ActSp channelSp = actSpService.findByChannelAppId(appId);
		if(null == channelSp){
			logger.info("CHAN_APPID=" + appId + Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13001_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		//校验渠道商户状态是否正常状态
		if(channelSp.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
			logger.info("CHAN_APPID=" + appId + Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getMessage());
			resultMap.put("resultCode", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.CHAN_MERCHANT_13002_ACCOUNT_STATUS_INVALID.getMessage());
			return resultMap;
		}

		Map<String,Object> params = new HashMap<>();
		params.put("orderCode", merOrderId);
		params.put("chanAppId", appId);
		//判断是否已存在商户退款订单号
		Long orderCount = tdOrderRefundService.findTdOrdCountByMerOrderId(params);
		if(orderCount > 0){
			logger.info("oriTdId=" + oriTdId + Hint.PAY_ORDER_14016_APP_ID_INCONFORMITY.getMessage());
			resultMap.put("resultCode", Hint.PAY_ORDER_14016_APP_ID_INCONFORMITY.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14016_APP_ID_INCONFORMITY.getMessage());
			return resultMap;
		}

		//获取原交易单数据
		TdOrder oriTdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf(oriTdId));
		if(null == oriTdOrder){
			logger.info("oriTdOrder=" + oriTdId + Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getMessage());
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getMessage());
			return resultMap;
		}
		//判断交易状态是否为成功
		if(!"true".equals(allowRefund) && oriTdOrder.getTdStatId() != BaseDictConstant.TD_STAT_ID_SUCCESS){
			logger.info("oriTdOrder=" + oriTdOrder + "退款原有交易单状态非成功");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14002_TDSTATID_NON_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14002_TDSTATID_NON_SUCCESS.getMessage());
			return resultMap;
		}
		//校验支付和退款的渠道商户是否一致
		if(!channelSp.getActId().equals(oriTdOrder.getChanActId())){
			logger.info(Hint.PAY_ORDER_14015_APP_ID_INCONFORMITY.getMessage());
			resultMap.put("resultCode", Hint.PAY_ORDER_14015_APP_ID_INCONFORMITY.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_14015_APP_ID_INCONFORMITY.getMessage());
			return resultMap;
		}
		
		//模式三：判断合作商户资金平台编码是否存在
		if(channelSp.getFundModelId() == DimDictEnum.FUND_MODEL_ID_3.getCode()){
			ActSp workSp = actSpService.findByWorkSpActId(oriTdOrder.getDispSpActId());
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
			if(ValidateUtils.isEmpty(workSp.getSpFundCode())){
				resultMap.put("resultCode", Hint.PAY_ORDER_13098.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_13098.getMessage());
				return resultMap;
			}
		}
		
		//获取原交易单交易扩展表-支付
		TdOrdExtPay oriTdOrdExtPay = tdOrderService.findTdOrdExtPayByPayOrderId(Long.valueOf(oriTdId));
		if(null == oriTdOrdExtPay){
			logger.info("oriTdOrder=" + oriTdOrder + Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getMessage());
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getMessage());
			return resultMap;
		}
		if(oriTdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode() || oriTdOrdExtPay.getPayTypeId() == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
			Date nowDate = new Date();
			Long tradeCrtDate = Long.parseLong(DateUtils.dateToString(oriTdOrder.getCrtTime(), "yyyyMM"));
			Long tradeNowDate = Long.parseLong(DateUtils.dateToString(nowDate, "yyyyMM"));
			logger.info("tradeCrtDate=" + tradeCrtDate + ",tradeNowDate=" + tradeNowDate);
			if(tradeCrtDate < tradeNowDate){
				resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14018.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14018.getMessage());
				return resultMap;
			}
			String endMiSettleCancelTime = String.valueOf(Propertie.APPLICATION.value("endMiSettleCancelTime")).trim();
			String lastDayOfMonth = DateUtils.getLastDayOfMonth("yyyy-MM-dd");
			endMiSettleCancelTime = lastDayOfMonth.concat(" ").concat(endMiSettleCancelTime);
			logger.info("nowDate=" + DateUtils.dateToString(nowDate, "yyyy-MM-dd HH:mm:ss") + ",endMiSettleCancelTime=" + endMiSettleCancelTime);
			if(nowDate.compareTo(DateUtils.strToDate(endMiSettleCancelTime, "yyyy-MM-dd HH:mm:ss")) >= 0){
				resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14017.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14017.getMessage());
				return resultMap;
			}
		}
		BigDecimal accRetAmt = oriTdOrdExtPay.getAccRetAmt();
		if(null == accRetAmt || accRetAmt.doubleValue() <= 0){
			accRetAmt = BigDecimal.ZERO;
		}

		//判断原有交易单累计退款金额是否小于交易金额
		if(accRetAmt.compareTo(oriTdOrder.getRecvCur()) >= 0){
			logger.info("oriTdOrder=" + oriTdOrder + "该笔订单已无可退金额");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14005_INSUFFICIENT_BALANCE.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14005_INSUFFICIENT_BALANCE.getMessage());
			return resultMap;
		}

		//判断当前是否存在退款未完成
		List<TdOrdExtRet> ordExtRetList = tdOrderRefundService.selectByRetList(Long.parseLong(oriTdId));
		if(null!= ordExtRetList && ordExtRetList.size() > 0){
			int unfinishedRefundCount = 0;
			for(TdOrdExtRet tdOrdExtRet : ordExtRetList){
				if(tdOrdExtRet.getTdRetStatId() != BaseDictConstant.TD_RET_STAT_ID_SUCESS){
					unfinishedRefundCount ++;
				}
			}
			if(unfinishedRefundCount > 0){
				logger.info("oriTdId=" + oriTdId + "原有订单存在退款中业务");
				resultMap.put("resultCode", Hint.PAY_ORDER_14009_REFUND_UNDERWAY.getCodeString());
				resultMap.put("resultDesc", Hint.PAY_ORDER_14009_REFUND_UNDERWAY.getMessage());
			}else {
				logger.info("oriTdId=" + oriTdId + " 该订单已成功退款");
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCode());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				String payRefundOrderId = ordExtRetList.get(0).getTdId().toString();
				if(payRefundOrderId.startsWith(BaseDictConstant.MEDICAL_REFUND_PREFIX)){
					payRefundOrderId = payRefundOrderId.substring(1);
				}
				resultMap.put("payRefundOrderId", payRefundOrderId);//退款交易单号
				resultMap.put("OriPayOrderId", ordExtRetList.get(0).getRetTdId());//原支付交易单号
				resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
				resultMap.put("transBusiType", transBusiType);
				resultMap.put("payRefundMoney", oriTdOrdExtPay.getAccRetAmt().toString());
			}
			return resultMap;
		}
		Long tdId = Long.parseLong(IdCreatorUtils.getRefundOrderId());
		//当前日期
		Date curDate = new Date();
		//生成退款交易单
		TdOrder tdOrder = new TdOrder();
		tdOrder.setTdId(tdId);//退款订单号
		tdOrder.setTdOrder(BigDecimal.valueOf(BaseDictConstant.TRANSACTION_SERIAL_NUMBER));//交易序号
		tdOrder.setMicId(channelSp.getMicId());//医保项目ID
		tdOrder.setTdTypeId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_TYPE_REFUND.getCode())));//交易类型退款
		tdOrder.setTdBusiTypeId(transBusiType);//交易业务类型
		tdOrder.setChanActId(channelSp.getActId());//渠道账户ID
		tdOrder.setChanAppid(appId);//渠道账户APPID
		tdOrder.setTdOperChanTypeId(Short.valueOf(terminalType));//交易操作通道类别
		tdOrder.setFromActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_MERCHANT.getCode())));//付款账户类型
		tdOrder.setFromActId(oriTdOrder.getToActId());//付款账户ID
		tdOrder.setToActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_PRIVATE.getCode())));//收款账户类型
		tdOrder.setToActId(oriTdOrder.getFromActId());//收款账户ID
		tdOrder.setDispSpActId(oriTdOrder.getDispSpActId());//显示商户账户ID(合作商户)
		tdOrder.setDispSpName(oriTdOrder.getDispSpName());//显示商户名称
		tdOrder.setFundTdCode(oriTdOrder.getFundTdCode());
		tdOrder.setGoodsDesc(cause);//退款原因
		tdOrder.setFundId(oriTdOrder.getFundId());//资金平台ID
		tdOrder.setPayTot(oriTdOrder.getPayTot());//交易总金额
		tdOrder.setRecvCur(oriTdOrder.getRecvCur());//本次实退金额
		tdOrder.setOrigOrdCode(merOrderId);//渠道商户订单号
		tdOrder.setSpSubSysCode(systemId);//商户子系统编号
		tdOrder.setTdStatId(Short.valueOf(String.valueOf(DimDictEnum.TD_TRANS_STAT_INIT.getCode())));//交易状态
		tdOrder.setTdDate(DateUtils.getNowDate("yyyyMMdd"));//交易日期
		tdOrder.setTdStartTime(curDate);//申请交易时间
		tdOrder.setConfirmStatId(Short.valueOf(String.valueOf(DimDictEnum.CONFIRM_STAT_WAIT_CHECK.getCode())));//对账状态
		tdOrder.setCrtTime(curDate);//创建时间
		tdOrder.setRemark("1");

		logger.info("保存退款交易单信息入库 payRefundOrderId=" + tdOrder.getTdId());
		tdOrderService.saveTdOrderInfo(tdOrder);
		logger.info("<<保存成功");

		//获取当前订单号的支付方式（1商业、2社保、3混合）
		short payTypeId = oriTdOrdExtPay.getPayTypeId();
		TdOrdExtRet tdOrdExtRet;
		if(payTypeId == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode() || payTypeId == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
			//增加商业退款扩展表
			tdOrdExtRet = new TdOrdExtRet();
			tdOrdExtRet.setTdId(tdId);//交易单ID
			tdOrdExtRet.setRetTdId(oriTdOrder.getTdId());
			tdOrdExtRet.setSearchCnt(0); //默认查询次数为0
			tdOrdExtRet.setTdRetStatId(BaseDictConstant.TD_RET_STAT_ID_INI);//状态运行中
			tdOrdExtRet.setCrtTime(curDate);
			tdOrdExtRet.setUpdTime(curDate);
			tdOrdExtRet.setRemark("1");
			logger.info("保存商业退款任务信息入库 taskId=" + tdOrdExtRet.getTdId());
			tdOrderRefundService.saveTdOrdExtRetInfo(tdOrdExtRet);
			logger.info("<<保存成功");
		}
		if(payTypeId == DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode() || payTypeId == DimDictEnum.USE_PAY_CHANNEL_TYPE_HUNHE.getCode()){
			//增加医保退款扩展表
			tdOrdExtRet = new TdOrdExtRet();
			tdOrdExtRet.setTdId(Long.parseLong(BaseDictConstant.MEDICAL_REFUND_PREFIX.concat(tdId.toString())));//交易单ID
			tdOrdExtRet.setRetTdId(oriTdOrder.getTdId());
			tdOrdExtRet.setSearchCnt(0); //默认查询次数为0
			tdOrdExtRet.setTdRetStatId(BaseDictConstant.TD_RET_STAT_ID_INI);//状态运行中
			tdOrdExtRet.setCrtTime(curDate);
			tdOrdExtRet.setUpdTime(curDate);
			tdOrdExtRet.setRemark("2");
			logger.info("保存医保退款任务信息入库 taskId=" + tdOrdExtRet.getTdId());
			tdOrderRefundService.saveTdOrdExtRetInfo(tdOrdExtRet);
			logger.info("<<保存成功");
		}

		TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
		updOrdExtPay.setTdId(oriTdOrder.getTdId());//交易单号
		updOrdExtPay.setTdRetFlag(Short.valueOf("1"));//是否被退款，1是0否
		updOrdExtPay.setAccRetAmt(oriTdOrder.getRecvCur());//累计退款金额
		updOrdExtPay.setUpdTime(new Date());
		if(logger.isInfoEnabled()){
			logger.info("开始更新交易扩展-支付信息");
		}
		tdOrderService.updateTdOrdExtPayInfo(updOrdExtPay);
		if(logger.isInfoEnabled()){
			logger.info("<<更新成功");
		}

		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("payRefundOrderId", tdOrder.getTdId().toString());//退款交易单号
		resultMap.put("oriPayOrderId", oriTdId);//原支付交易单号
		resultMap.put("merOrderId", merOrderId);//商户平台退款订单号
		resultMap.put("cause", cause);
		resultMap.put("transBusiType", transBusiType);
		resultMap.put("payRefundMoney", oriTdOrder.getRecvCur().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		return resultMap;
	}

	public Map<String, Object> payOrderRefundSi(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> payOrderRefundSi 输入参数：" + inputMap);
		Map<String, Object> resultMap = new HashMap<>();
		String oriTdId = String.valueOf(inputMap.get("oriTdId")).trim();//原支付交易单号

		//查询原支付交易单
		TdOrder tdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf(oriTdId));
		if(null == tdOrder){
			logger.info("oriTdId=" + oriTdId + " 支付订单不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getMessage());
			return resultMap;
		}
		//查询原支付交易单扩展信息
		TdOrdExtPay tdOrdExtPay = tdOrderService.findTdOrdExtPayByPayOrderId(Long.valueOf(oriTdId));
		if(null == tdOrdExtPay){
			logger.info("oriTdId=" + oriTdId + " 支付扩展信息不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getMessage());
			return resultMap;
		}
		//获取支付用户信息
		PayUser payUser = payUserService.findByPayUserActId(tdOrder.getFromActId().toString());
		if(null == payUser){
			logger.info("actId=" + tdOrder.getFromActId() + "支付用户信息不存在");
			resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
			return resultMap;
		}
		//获取私人帐户信息
		ActPerson actPerson = actPersonServie.findActPersonById(tdOrder.getFromActId());
		if(null == actPerson){
			logger.info("actId=" + tdOrder.getFromActId() + "私人账户信息不存在");
			resultMap.put("resultCode", Hint.USER_11003_ACCOUNTINFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11003_ACCOUNTINFO_NOTEXIST.getMessage());
			return resultMap;
		}
		TdMiPara tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
		if(null == tdMiPara){
			logger.info("TdMiPara信息为空");
			resultMap.put("resultCode", Hint.PAY_ORDER_13109.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13109.getMessage());
			return resultMap;
		}
		Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
		if(null == preClmParaMap || preClmParaMap.size() == 0){
			resultMap.put("resultCode", Hint.PAY_ORDER_13109.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13109.getMessage());
			return resultMap;
		}
		String fixedHospCode = preClmParaMap.get("fixedHospCode").toString();
		PersonInfoRes personInfoRes = payUserServiceImpl.getMiPersonalInfo(tdOrder.getFromActId(), tdOrdExtPay.getpSiCardNo(), fixedHospCode);
		if(null == personInfoRes){
			resultMap.put("resultCode", Hint.PAY_ORDER_13101.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13101.getMessage());
			return resultMap;
		}

		MzTollReq mzTollReq = new MzTollReq();
		mzTollReq.setSerialID(tdOrdExtPay.getHospSeqCode());// 门诊流水号
		mzTollReq.setDealID(tdOrdExtPay.getSiTdCode());//交易流水号
		mzTollReq.setPersonID(tdOrdExtPay.getSiPCode());//个人编号
		mzTollReq.setPerCardID(actPerson.getpCertNo());//身份证号
		mzTollReq.setPerName(actPerson.getpName());//姓名
		mzTollReq.setJbr(Propertie.APPLICATION.value("miPayOperator"));//经办人
		mzTollReq.setTelephone(payUser.getPayUserCode());//手机号码
		if(logger.isInfoEnabled()){
			logger.info("医院门诊医保结算(职工)撤销，请求参数：fixedHospCode=" + fixedHospCode + ",cycId=" + personInfoRes.getCycid() + ",mzTollReq=" + JSON.toJSONString(mzTollReq));
		}
		MzBackResultRes mzBackResultRes = outpatientService.executeMzBack(fixedHospCode, personInfoRes.getCycid(), mzTollReq);
		if(logger.isInfoEnabled()){
			logger.info("响应报文：mzBackResultRes=" + (mzBackResultRes == null ? null : JSON.toJSONString(mzBackResultRes)));
		}
		if(null == mzBackResultRes || mzBackResultRes.getRTNCode() != 1){
			logger.info("医院门诊医保结算(职工)撤销失败");
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", "医院门诊医保结算(职工)撤销失败");
			return resultMap;
		}
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	public Map<String, Object> payRefundQuerySi(Map<String, Object> inputMap) throws Exception {
		logger.info("TdOrderRefundServiceGWImpl --> payRefundQuerySi 输入参数：" + inputMap);
		//返回结果定义
		Map<String, Object> resultMap = new HashMap<>();
		String oriTdId = String.valueOf(inputMap.get("oriTdId")).trim();//原支付交易单号
		//查询原支付交易单
		TdOrder tdOrder = tdOrderService.findTdOrderByPayOrderId(Long.valueOf(oriTdId));
		if(null == tdOrder){
			logger.info("oriTdId=" + oriTdId + " 支付订单信息不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14001_TDID_INVALID.getMessage());
			return resultMap;
		}
		//查询原支付交易单扩展信息
		TdOrdExtPay tdOrdExtPay = tdOrderService.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
		if(null == tdOrdExtPay){
			logger.info("oriTdOrder=" + oriTdId + "交易单支付扩展信息不存在");
			resultMap.put("resultCode", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_REFUND_14004_EXT_TDID_INVALID.getMessage());
			return resultMap;
		}
		TdMiPara tdMiPara = tdOrderServiceImpl.queryTdMiPara(tdOrder.getTdId());
		if(null == tdMiPara){
			logger.info("TdMiPara信息为空");
			resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
			return resultMap;
		}
		Map<String, Object> preClmParaMap = tdOrderServiceImpl.getPreClmPara(tdMiPara);
		if(null == preClmParaMap || preClmParaMap.size() == 0){
			logger.info(Hint.PAY_ORDER_13109.getMessage() + " preClmParaMap=" + preClmParaMap);
			resultMap.put("resultCode", Hint.PAY_ORDER_13109.getCodeString());
			resultMap.put("resultDesc", Hint.PAY_ORDER_13109.getMessage());
			return resultMap;
		}
		String fixedHospCode = preClmParaMap.get("fixedHospCode").toString();
		TollInfoReq tollInfoReq = new TollInfoReq();
		tollInfoReq.setSerialID(tdOrdExtPay.getHospSeqCode());//门诊流水号
		tollInfoReq.setPersonID(tdOrdExtPay.getSiPCode());//个人编号
		if(logger.isInfoEnabled()){
			logger.info("医院门诊医保结算(职工)撤销结果查询，请求参数：fixedHospCode=" + fixedHospCode + ",tollInfoReq=" + JSON.toJSONString(tollInfoReq));
		}
		OrderStatusRes orderStatusRes = commInfoServiceImpl.queryOrderStatus(fixedHospCode, tollInfoReq);
		if(logger.isInfoEnabled()){
			logger.info("响应报文：orderStatusRes=" + (orderStatusRes == null ? null : JSON.toJSONString(orderStatusRes)));
		}
		if(null == orderStatusRes || orderStatusRes.getRTNCode() != 1 || !"0".equals(orderStatusRes.getYouxbz())){//0:无效(已撤销) 1:有效(未撤销) -1:无数据(未结算)
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", "查询医院门诊医保结算(职工)撤销结果失败");
			return resultMap;
		}
		String jbr = Propertie.APPLICATION.value("miPayOperator");
		if(ValidateUtils.isNotEmpty(orderStatusRes.getJingbr()) && !jbr.equals(orderStatusRes.getJingbr())){
			if(logger.isInfoEnabled()){
				logger.info("申请结算撤销经办人：" + jbr + ",结算撤销查询返回经办人：" + orderStatusRes.getJingbr());
			}
			resultMap.put("resultCode", Hint.SYS_10011_REQUEST_DSF_INTERFACE_EXCEPTION.getCodeString());
			resultMap.put("resultDesc", "申请结算撤销经办人与结算撤销查询返回经办人不一致");
			return resultMap;
		}
		resultMap.put("status", String.valueOf(BaseDictConstant.TD_STAT_ID_SUCCESS));//状态
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}
}

package com.pay.cloud.pay.trade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.core.common.JsonStringUtils;
import com.pay.cloud.pay.escrow.alipay.utils.EBaoConstants;
import com.pay.cloud.pay.escrow.mi.pangu.request.TollsReq;
import com.pay.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.pay.cloud.pay.escrow.wechat.utils.WeChatUtil;
import com.pay.cloud.pay.payuser.service.PayUserService;
import com.pay.cloud.pay.proplat.entity.PpFundPlatform;
import com.pay.cloud.pay.supplier.entity.ActSp;
import com.pay.cloud.pay.supplier.service.ActSpService;
import com.pay.cloud.pay.trade.dao.TdMiParaMapper;
import com.pay.cloud.pay.trade.dao.TdOrdExtPayMapper;
import com.pay.cloud.pay.trade.dao.TdOrderMapper;
import com.pay.cloud.pay.trade.entity.TdMiPara;
import com.pay.cloud.pay.trade.entity.TdOrdExtPay;
import com.pay.cloud.pay.trade.entity.TdOrder;
import com.pay.cloud.pay.trade.service.PayOrderService;
import com.pay.cloud.pay.trade.service.TdOrderService;
import com.pay.cloud.util.*;
import com.pay.cloud.util.hint.Hint;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("tdOrderServiceImpl")
public class TdOrderServiceImpl implements TdOrderService {

    private static final Logger logger = Logger.getLogger(TdOrderServiceImpl.class);

    @Autowired
    private TdOrderMapper tdOrderMapper;
    @Autowired
    private TdOrdExtPayMapper tdOrdExtPayMapper;
    @Autowired
    private TdMiParaMapper tdMiParaMapper;
    @Resource
    private PayOrderService payOrderServiceImpl;
    @Resource
    private PayUserService payUserServiceImpl;
    @Resource
    private ActSpService actSpServiceImpl;

    /**
     * 获取商户订单记录条数
     * @param record
     * @return
     * @throws Exception
     */
    public List<Long> findTdOrderCountByMerOrderId(TdOrder record) throws Exception {
        return tdOrderMapper.findTdOrderCountByMerOrderId(record);
    }

    /**
     * 保存交易单信息
     * @param tdOrder 交易单信息
     * @throws Exception
     */
    public void saveTdOrderInfo(TdOrder tdOrder) throws Exception {
        tdOrderMapper.saveTdOrder(tdOrder);
    }

    /**
     * 保存交易单&支付扩展信息
     * @param tdOrder
     * @throws Exception
     */
    public void saveTdOrderAndExtPayInfo(TdOrder tdOrder, TdOrdExtPay tdOrdExtPay, TdMiPara tdMiPara) throws Exception {
        tdOrderMapper.saveTdOrder(tdOrder);
        tdOrdExtPayMapper.saveTdOrdExtPay(tdOrdExtPay);
        if(null != tdMiPara){
            tdMiParaMapper.saveTdMiPara(tdMiPara);
        }
    }

    /**
     * 更新交易单&支付扩展信息
     * @param tdOrder
     * @throws Exception
     */
    public void updateTdOrderAndExtPayInfo(TdOrder tdOrder, TdOrdExtPay tdOrdExtPay) throws Exception {
        tdOrderMapper.updateTdOrder(tdOrder);
        tdOrdExtPayMapper.updateTdOrdExtPay(tdOrdExtPay);
    }

    /**
     * 更新交易单信息
     * @param tdOrder
     * @return
     * @throws Exception
     */
    public int updateTdOrderInfo(TdOrder tdOrder) throws Exception {
        return tdOrderMapper.updateTdOrder(tdOrder);
    }

    /**
     * 保存交易扩展表-支付信息
     * @param tdOrdExtPay 交易扩展信息
     * @throws Exception
     */
    public void saveTdOrdExtPayInfo(TdOrdExtPay tdOrdExtPay) throws Exception {
        tdOrdExtPayMapper.saveTdOrdExtPay(tdOrdExtPay);
    }

    /**
     * 获取交易单信息
     * @param tdId
     * @return
     * @throws Exception
     */
    public TdOrder findTdOrderByPayOrderId(Long tdId) throws Exception {
        return tdOrderMapper.findTdOrderByPayOrderId(tdId);
    }

    /**
     * 获取交易扩展表信息
     * @param tdId
     * @return
     * @throws Exception
     */
    public TdOrdExtPay findTdOrdExtPayByPayOrderId(Long tdId) throws Exception {
        return tdOrdExtPayMapper.findTdOrdExtPay(tdId);
    }

    /**
     * 更新交易扩展表信息
     * @param tdOrdExtPay
     * @return
     * @throws Exception
     */
    public int updateTdOrdExtPayInfo(TdOrdExtPay tdOrdExtPay) throws Exception {
        return tdOrdExtPayMapper.updateTdOrdExtPay(tdOrdExtPay);
    }

    /**
     * 处理易宝支付结果通知
     * @param inputMap 输入map
     * @return 处理结果 SUCCESS or FAILURE
     * @throws Exception
     */
    public String executeYeepayPaymentResultNotice(Map<String, String> inputMap) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info("TdOrderServiceImpl --> executeYeepayPaymentResultNotice 输入参数：" + inputMap);
        }
        String orderid = String.valueOf(inputMap.get("orderid")).trim();//交易单号
        String status = String.valueOf(inputMap.get("status")).trim();//交易状态 0：失败 1：成功 2：撤销
        String closetime = String.valueOf(inputMap.get("closetime")).trim();//订单支付成功之后订单状态发生变化时间
        String errorcode = String.valueOf(inputMap.get("errorcode")).trim();
        String yborderid = String.valueOf(inputMap.get("yborderid")).trim();//易宝交易流水号
        String bindid = String.valueOf(inputMap.get("bindid")).trim();//绑卡ID
        if(ValidateUtils.isEmpty(errorcode)){
            errorcode = "";
        }
        String errormsg = String.valueOf(inputMap.get("errormsg")).trim();
        if(ValidateUtils.isEmpty(errormsg)){
            errormsg = "";
        }
        return payOrderServiceImpl.executeOrderTradeData_EBZF(orderid, yborderid, status, closetime, errorcode, errormsg, bindid);
    }

    /**
     * 处理支付宝支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    public String executeAliPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception {
        String status = String.valueOf(inputMap.get("trade_status")).trim();
        if("TRADE_SUCCESS".equals(status) || "TRADE_FINISHED".equals(status)){
            status = "1";
        }else {
            status = "0";
        }
        return payOrderServiceImpl.executeOrderTradeData_ALIPAY(payOrderId, String.valueOf(inputMap.get("trade_no")), status, String.valueOf(inputMap.get("gmt_payment")), String.valueOf(inputMap.get("total_fee")));
    }

    /**
     * 处理微信支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    public String executeWeChatPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception {
        String status = String.valueOf(inputMap.get("result_code")).trim();
        if(WeChatConstants.RESULT_CODE_SUCCESS.equals(status)){
            status = "1";
        }else {
            status = "0";
        }
        String totalFee = String.valueOf(inputMap.get("total_fee")).trim();
        if(ValidateUtils.isNotEmpty(totalFee)){
            totalFee = BigDecimal.valueOf(Double.parseDouble(totalFee)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return payOrderServiceImpl.executeOrderTradeData_WECHAT(payOrderId, String.valueOf(inputMap.get("transaction_id")), status, String.valueOf(inputMap.get("time_end")), totalFee);
    }

    /**
     * 处理建行支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    public String executeCcbPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception {
        String status = String.valueOf(inputMap.get("SUCCESS")).trim();
        if("Y".equals(status)){
            status = "1";
        }else {
            status = "0";
        }
        return payOrderServiceImpl.executeOrderTradeData_CCB(payOrderId, "", status, "", String.valueOf(inputMap.get("PAYMENT")));
    }

    /**
     * 处理支付宝支付结果通知
     * @param inputMap
     * @param payOrderId
     * @return
     * @throws Exception
     */
    public String executeBkuPaymentResultNotice(Map<String, Object> inputMap, String payOrderId) throws Exception {
        String completeDate = String.valueOf(inputMap.get("CompleteDate")).trim();
        String completeTime = String.valueOf(inputMap.get("CompleteTime")).trim();
        String transTime = "";
        if(ValidateUtils.isNotEmpty(completeDate) && ValidateUtils.isNotEmpty(completeTime)){
            transTime = completeDate.concat(completeTime);
        }
        String status = String.valueOf(inputMap.get("OrderStatus")).trim();
        if("0000".equals(status)){
            status = "1";
        }else {
            status = "0";
        }
        return payOrderServiceImpl.executeOrderTradeData_BKU(payOrderId, String.valueOf(inputMap.get("AcqSeqId")), status, transTime, String.valueOf(inputMap.get("OrderAmt")));
    }

	public List<TdOrder> findTdOrderListByOrigOrdCode(String merOrderId) throws Exception {
		return tdOrderMapper.findTdOrderListByOrigOrdCode(merOrderId);
	}

    public int saveTdMiPara(TdMiPara tdMiPara) throws Exception {
        return tdMiParaMapper.saveTdMiPara(tdMiPara);
    }

    public TdMiPara queryTdMiPara(Long tdId) throws Exception {
        return tdMiParaMapper.queryTdMiPara(tdId);
    }

    public int updateTdMiPara(TdMiPara tdMiPara) throws Exception {
        return tdMiParaMapper.updateTdMiPara(tdMiPara);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getPreClmPara(TdMiPara tdMiPara) throws Exception {
        if(null == tdMiPara){
            if(logger.isInfoEnabled()){
                logger.info("TdOrderServiceImpl --> getPreClmPara：tdMiPara为空");
            }
            return null;
        }
        String preClmPara = String.valueOf(tdMiPara.getPreClmPara()).trim();
        if(ValidateUtils.isEmpty(preClmPara)){
            if(logger.isInfoEnabled()){
                logger.info("TdOrderServiceImpl --> getPreClmPara：preClmPara为空");
            }
            return null;
        }
        Map convertMap = JSON.parseObject(preClmPara, Map.class);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("fixedHospCode", convertMap.get("fixedHospCode"));
        returnMap.put("tollsReq", JsonStringUtils.jsonStringToObject(convertMap.get("tollsReq").toString(), TollsReq.class));
        return returnMap;
    }

    /**
     * 变更商业支付通道
     * @param inputMap
     * @return
     * @throws Exception
     */
    public Map<String, Object> changeFundId(Map<String, Object> inputMap) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info("TdOrderServiceImpl --> changeFundId 输入参数：" + inputMap);
        }
        Map<String, Object> resultMap = new HashMap<>();
        String payOrderId = String.valueOf(inputMap.get("payOrderId")).trim();//支付平台订单号
        String accountId = String.valueOf(inputMap.get("accountId")).trim();//用户账户
        String fundId = String.valueOf(inputMap.get("fundId")).trim();//资金平台编码
        String longitude = String.valueOf(inputMap.get("longitude")).trim();//经度
        String latitude = String.valueOf(inputMap.get("latitude")).trim();//纬度

        Map<String, Object> checkPayUserMap = payUserServiceImpl.checkPayUserValidity(accountId, "", true);
        if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkPayUserMap.get("resultCode")))){
            return checkPayUserMap;
        }
        TdOrder tdOrder = this.findTdOrderByPayOrderId(Long.valueOf(payOrderId));
        Map<String, Object> checkOrderMap = this.checkOrderIsValid(tdOrder, accountId);
        if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(checkOrderMap.get("resultCode")))){
            return checkOrderMap;
        }
        TdOrdExtPay tdOrdExtPay = this.findTdOrdExtPayByPayOrderId(tdOrder.getTdId());
        if(logger.isInfoEnabled()){
            logger.info("当前订单支付方式 PayTypeId=" + tdOrdExtPay.getPayTypeId());
        }
        PpFundPlatform ppFundPlatform = null;
        if(DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode() != tdOrdExtPay.getPayTypeId()){
            if(ValidateUtils.isEmpty(fundId)){
                resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
                resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "fundId"));
                return resultMap;
            }
            ppFundPlatform = CacheUtils.getPpFundPlatform(fundId);
            if(null == ppFundPlatform){
                if(logger.isInfoEnabled()){
                    logger.info("FundId=" + fundId + " 资金平台无效或不存在");
                }
                resultMap.put("resultCode", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13018_FUND_ID_INVALID_OR_NOT_FUND.getMessage());
                return resultMap;
            }
        }
        ActSp workSp = actSpServiceImpl.findByWorkSpActId(tdOrder.getDispSpActId());
        if(logger.isInfoEnabled()){
            logger.info("用户当前位置：longitude=" + longitude + ",latitude=" + latitude +
                    " 商户(" + workSp.getActId() + ")所在位置：longitude=" + workSp.getLongitude() + ",latitude=" + workSp.getLatitude());
        }
        String miPayGetGpsFlag = String.valueOf(CacheUtils.getDimSysConfConfValue("MI_PAY_GET_GPS_FLAG")).trim();
        if(DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode() != tdOrdExtPay.getPayTypeId() && "1".equals(miPayGetGpsFlag)){
            if(ValidateUtils.isEmpty(longitude) || Double.parseDouble(longitude) <= 0 ||
               ValidateUtils.isEmpty(latitude) || Double.parseDouble(latitude) <= 0 ||
               ValidateUtils.isEmpty(workSp.getLongitude()) || Double.parseDouble(workSp.getLongitude()) <= 0 ||
               ValidateUtils.isEmpty(workSp.getLatitude()) || Double.parseDouble(workSp.getLatitude()) <= 0){
                resultMap.put("resultCode", Hint.PAY_ORDER_13113.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13113.getMessage());
                return resultMap;
            }
        }
        int safeDist = 0;
        double distance = 0;
        if(ValidateUtils.isNotEmpty(longitude) && Double.parseDouble(longitude) > 0 &&
           ValidateUtils.isNotEmpty(latitude) && Double.parseDouble(latitude) > 0 &&
           ValidateUtils.isNotEmpty(workSp.getLongitude()) && Double.parseDouble(workSp.getLongitude()) > 0 &&
           ValidateUtils.isNotEmpty(workSp.getLatitude()) && Double.parseDouble(workSp.getLatitude()) > 0){
            //安全距离校验标志(1-开启 0-关闭)
            String safeDistanceCheckFlag = String.valueOf(CacheUtils.getDimSysConfConfValue("SAFE_DISTANCE_CHECK_FLAG")).trim();
            if(logger.isInfoEnabled()){
                logger.info("安全距离校验标志 safeDistanceCheckFlag=" + safeDistanceCheckFlag);
            }
            safeDist = workSp.getSafeDist();
            if(logger.isInfoEnabled()){
                logger.info("当前商户设置的医保支付安全距离(米)为：" + safeDist);
            }
            if(safeDist <= 0){
                String merchantDefaultSafeDistance = String.valueOf(CacheUtils.getDimSysConfConfValue("MERCHANT_DEFAULT_SAFE_DISTANCE")).trim();//商户默认安全距离(米)
                if(logger.isInfoEnabled()){
                    logger.info("商户未设置安全距离，读取默认配置(米)：" + merchantDefaultSafeDistance);
                }
                safeDist = Integer.parseInt(merchantDefaultSafeDistance);
            }
            distance = DistanceUtil.GetDistance2(Double.parseDouble(longitude), Double.parseDouble(latitude), Double.parseDouble(workSp.getLongitude().trim()), Double.parseDouble(workSp.getLatitude().trim()));
            if(logger.isInfoEnabled()){
                logger.info("计算两点之间距离为：" + distance);
            }
            if("1".equals(safeDistanceCheckFlag) && DimDictEnum.USE_PAY_CHANNEL_TYPE_SHANGYE.getCode() != tdOrdExtPay.getPayTypeId()){
                if(distance > safeDist){
                    tdOrder = new TdOrder();
                    tdOrder.setTdId(Long.valueOf(payOrderId));
                    tdOrder.setLongitude(longitude);
                    tdOrder.setLatitude(latitude);
                    tdOrder.setUpdTime(new Date());
                    tdOrder.setPaySafeFlag(BaseDictConstant.PAY_SAFE_FLAG_NO);
                    tdOrder.setSafeDist(safeDist);
                    tdOrder.setRealDist(BigDecimal.valueOf(distance).setScale(4, BigDecimal.ROUND_HALF_UP));
                    this.updateTdOrderInfo(tdOrder);
                    resultMap.put("resultCode", Hint.PAY_ORDER_13112.getCodeString());
                    resultMap.put("resultDesc", Hint.PAY_ORDER_13112.getMessage());
                    return resultMap;
                }else {
                    if(logger.isInfoEnabled()){
                        logger.info("安全距离范围内，允许医保支付");
                    }
                }
            }
        }
        if(logger.isInfoEnabled()){
            logger.info("订单当前fundId=" + tdOrder.getFundId() + ",申请变更fundId=" + fundId);
        }
        if(tdOrder.getFundId().equals(fundId) || DimDictEnum.USE_PAY_CHANNEL_TYPE_SHEBAO.getCode() == tdOrdExtPay.getPayTypeId()){
            if(ValidateUtils.isNotEmpty(longitude) && ValidateUtils.isNotEmpty(latitude)){
                tdOrder = new TdOrder();
                tdOrder.setTdId(Long.valueOf(payOrderId));
                tdOrder.setLongitude(longitude);
                tdOrder.setLatitude(latitude);
                tdOrder.setUpdTime(new Date());
                if(distance > safeDist){
                    tdOrder.setPaySafeFlag(BaseDictConstant.PAY_SAFE_FLAG_NO);
                }else {
                    tdOrder.setPaySafeFlag(BaseDictConstant.PAY_SAFE_FLAG_YES);
                }
                tdOrder.setSafeDist(safeDist);
                tdOrder.setRealDist(BigDecimal.valueOf(distance).setScale(4, BigDecimal.ROUND_HALF_UP));
                this.updateTdOrderInfo(tdOrder);
            }
            resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
            return resultMap;
        }
        ActSp channelSp = actSpServiceImpl.selectByActId(tdOrder.getChanActId());
        String spFundCode = "";
        Short fundModelId = channelSp.getFundModelId();
        if(null != ppFundPlatform){
            if(fundModelId == DimDictEnum.FUND_MODEL_ID_2.getCode() || fundModelId == DimDictEnum.FUND_MODEL_ID_3.getCode()){
                if(fundModelId == DimDictEnum.FUND_MODEL_ID_2.getCode()){
                    if(BaseDictConstant.PP_FUND_TYPE_ID_ALI.equals(ppFundPlatform.getPpFundTypeId()) ||
                            BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN.equals(ppFundPlatform.getPpFundTypeId())){
                        spFundCode = CacheUtils.getPpIntfPara(fundId.concat("_").concat(EBaoConstants.PARA_TYPE_SELLER_ID));
                    }else if(BaseDictConstant.PP_FUND_TYPE_ID_WECHAT.equals(ppFundPlatform.getPpFundTypeId())){
                        spFundCode = WeChatUtil.getMchId(fundId);
                    }
                }else {
                    spFundCode = String.valueOf(workSp.getSpFundCode()).trim();
                }
            }
        }
        tdOrder = new TdOrder();
        tdOrder.setTdId(Long.valueOf(payOrderId));
        tdOrder.setFundId(fundId);
        tdOrder.setLongitude(longitude);
        tdOrder.setLatitude(latitude);
        tdOrder.setUpdTime(new Date());
        if(distance > safeDist){
            tdOrder.setPaySafeFlag(BaseDictConstant.PAY_SAFE_FLAG_NO);
        }else {
            tdOrder.setPaySafeFlag(BaseDictConstant.PAY_SAFE_FLAG_YES);
        }
        tdOrder.setSafeDist(safeDist);
        tdOrder.setRealDist(BigDecimal.valueOf(distance).setScale(4, BigDecimal.ROUND_HALF_UP));
        TdOrdExtPay updOrdExtPay = new TdOrdExtPay();
        updOrdExtPay.setTdId(tdOrder.getTdId());
        updOrdExtPay.setPayPropA(spFundCode);
        updOrdExtPay.setUpdTime(new Date());
        this.updateTdOrderAndExtPayInfo(tdOrder, updOrdExtPay);

        resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
        return resultMap;
    }

    /**
     * 检测交易单是否有效
     * @param tdOrder
     * @param accountId
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkOrderIsValid(TdOrder tdOrder, String accountId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if(null == tdOrder){
            resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
            resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
            return resultMap;
        }
        if(tdOrder.getTdOperChanTypeId().intValue() != DimDictEnum.TD_OPER_CHAN_TYPE_WECHAT.getCode() &&
                tdOrder.getTdOperChanTypeId().intValue() != DimDictEnum.TD_OPER_CHAN_TYPE_WEB.getCode()){
            if(tdOrder.getFromActId().longValue() != Long.valueOf(accountId).longValue()){
                if(logger.isInfoEnabled()){
                    logger.info("下单账户fromActId=" + tdOrder.getFromActId() + ",本次请求账户accountId=" + accountId);
                }
                resultMap.put("resultCode", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getCodeString());
                resultMap.put("resultDesc", Hint.PAY_ORDER_13004_ORDERINFO_NOT_EXIST_OR_INVALID.getMessage());
                return resultMap;
            }
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
        if(tdOrder.getTdStatId() == DimDictEnum.TD_TRANS_STAT_SUCCESS.getCode()){
            //订单已成功
            resultMap.put("resultCode", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getCodeString());
            resultMap.put("resultDesc", Hint.YEEPAY_13010_ORDER_ALREADY_TRADE_SUCCESS.getMessage());
            return resultMap;
        }
        resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
        return resultMap;
    }
}

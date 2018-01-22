package com.pay.cloud.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.gw.service.trade.SupplierTradeServiceGW;
import com.pay.cloud.gw.service.trade.TdOrderServiceGW;
import com.pay.cloud.pay.escrow.alipay.dto.BaseDTO;
import com.pay.cloud.pay.escrow.alipay.service.AlipayService;
import com.pay.cloud.pay.escrow.ccb.service.NetPayService;
import com.pay.cloud.pay.escrow.chinapay.service.CPNetPayService;
import com.pay.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.pay.cloud.pay.escrow.wechat.utils.WeChatUtil;
import com.pay.cloud.pay.payment.service.PaymentService;
import com.pay.cloud.pay.trade.entity.TdOrder;
import com.pay.cloud.pay.trade.service.TdOrderService;
import com.pay.cloud.util.GetRemoteIpAddr;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 第三方支付平台回调通知入口
 * Created by yacheng.ji on 2016/4/19.
 */
@Controller
@RequestMapping(value = "/ebaonet")
public class EscrowNoticeControler {

	private static final Logger logger = Logger.getLogger(EscrowNoticeControler.class);

	@Resource(name="supplierTradeServiceGWImpl")
	private SupplierTradeServiceGW supplierTradeServiceGW;
	@Resource(name = "yeepayPaymentServiceImpl")
	private PaymentService paymentService;
    @Resource(name = "aliPayServiceImpl")
    private AlipayService aliPayServiceImpl;
    @Resource(name = "tdOrderServiceImpl")
    private TdOrderService tdOrderServiceImpl;
    @Resource(name = "tdOrderServiceGWImpl")
    private TdOrderServiceGW tdOrderServiceGWImpl;
    @Resource(name = "netPayServiceImpl")
    private NetPayService netPayServiceImpl;
    @Resource(name = "cPNetPayServiceImpl")
    private CPNetPayService cPNetPayServiceImpl;
    @Resource
    private RedisService redisService;

    /**
     * yeepay支付结果通知
     * @param request
     * @return
     */
    @RequestMapping(value = "yeebaoPayNotice", method = RequestMethod.POST)
    @ResponseBody
    public String yeebaoPayNotice(HttpServletRequest request) {
        try {
            String data = String.valueOf(request.getParameter("data")).trim();
            String encryptKey = String.valueOf(request.getParameter("encryptkey")).trim();
            logger.info("接收YeePay支付结果通知 ipAddress=" + GetRemoteIpAddr.getIpAddr(request) + ",data=" + data + ",encryptKey=" + encryptKey);
            if (ValidateUtils.isEmpty(data)) {
                logger.info("data数据为空");
                return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
            }
            if (ValidateUtils.isEmpty(encryptKey)) {
                logger.info("encryptkey数据为空");
                return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
            }
            String result = tdOrderServiceGWImpl.yeepayPaymentResultNotice(data, encryptKey);
            logger.info("接口处理结果：" + result);
            return result;
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
        }
    }

    /**
     * 支付宝支付结果通知
     * @param request
     * @return
     */
    @RequestMapping(value = "aliPayNotice", method = RequestMethod.POST)
    @ResponseBody
    public String aliPayNotice(HttpServletRequest request) {
        try {
            Map<String, Object> params = new HashMap<>();
            Enumeration enumeration = request.getParameterNames();
            String paramName;
            while(enumeration.hasMoreElements()){
                paramName = String.valueOf(enumeration.nextElement()).trim();
                params.put(paramName, request.getParameter(paramName));
            }
            if(logger.isInfoEnabled()){
                logger.info("支付宝支付结果异步通知，请求参数：ipAddress=" + GetRemoteIpAddr.getIpAddr(request) + "," + params);
            }
            if(params.size() == 0){
                return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
            }
            String outTradeNo = String.valueOf(params.get("out_trade_no")).trim();
            if(ValidateUtils.isEmpty(outTradeNo)){
                if(logger.isInfoEnabled()){
                    logger.info("out_trade_no=" + outTradeNo + ",支付平台交易流水号为空");
                }
                return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
            }
            outTradeNo = outTradeNo.split("-")[0];
            TdOrder tdOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.valueOf(outTradeNo));
            if(null == tdOrder){
                if(logger.isInfoEnabled()){
                    logger.info("payOrderId=" + outTradeNo + " 交易单信息不存在");
                }
                return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
            }
            BaseDTO<Boolean> baseDTO = aliPayServiceImpl.verify(params, tdOrder.getFundId());
            if(!baseDTO.getResult()){
                if(logger.isInfoEnabled()){
                    logger.info("验签失败");
                }
                return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
            }
            if("WAIT_BUYER_PAY".equals(String.valueOf(params.get("trade_status")).trim())){
                logger.info("支付宝通知当前交易单状态为：交易创建，等待买家付款，本次请求无需处理");
                return BaseDictConstant.ALIPAY_PAYMENT_RESULT_SUCCESS;
            }
            String result = tdOrderServiceGWImpl.aliPaymentResultNotice(params, outTradeNo);
            logger.info("接口处理结果：" + result);
            return result;
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            return BaseDictConstant.ALIPAY_PAYMENT_RESULT_FAILURE;
        }
    }

    /**
     * 建行支付结果通知
     * @param request
     * @return
     */
    @RequestMapping(value = "ccbPayNotice", method = RequestMethod.POST)
    @ResponseBody
    public String ccbPayNotice(HttpServletRequest request) {
        try {
            String ccbNoticeParams = String.valueOf(request.getQueryString()).trim();
            if(logger.isInfoEnabled()){
                logger.info("建行支付结果异步通知，请求参数：ipAddress=" + GetRemoteIpAddr.getIpAddr(request) + "," + ccbNoticeParams);
            }
            if(ValidateUtils.isEmpty(ccbNoticeParams)){
                return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
            }
            Map<String, Object> ccbValidateMap = netPayServiceImpl.getNotifyData(ccbNoticeParams);
            if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(ccbValidateMap.get("resultCode")).trim())){
                if(logger.isInfoEnabled()){
                    logger.info("<<验签失败");
                }
                return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
            }
            String result = tdOrderServiceGWImpl.ccbPaymentResultNotice(ccbValidateMap);
            logger.info("接口处理结果：" + result);
            return result;
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            return BaseDictConstant.CCB_PAYMENT_RESULT_FAILURE;
        }
    }

    /**
     * 银联支付结果通知
     * @param request
     * @return
     */
    @RequestMapping(value = "bkuPayNotice", method = RequestMethod.POST)
    @ResponseBody
    public String bkuPayNotice(HttpServletRequest request) {
        try {
            Enumeration<String> params = request.getParameterNames();
            Map<String, Object> bkuNoticeParams = new HashMap<>();
            String paramName;
            while(params.hasMoreElements()){
                paramName = String.valueOf(params.nextElement()).trim();
                bkuNoticeParams.put(paramName, request.getParameter(paramName));
            }
            if(logger.isInfoEnabled()){
                logger.info("银联支付结果异步通知，请求参数：ipAddress=" + GetRemoteIpAddr.getIpAddr(request) + "," + bkuNoticeParams);
            }
            if(bkuNoticeParams.size() == 0){
                return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
            }
            Map<String, Object> bkuValidateMap = cPNetPayServiceImpl.getNotifyData(bkuNoticeParams);
            if(!Hint.SYS_SUCCESS.getCodeString().equals(String.valueOf(bkuValidateMap.get("resultCode")).trim())){
                if(logger.isInfoEnabled()){
                    logger.info("<<验签失败");
                }
                return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
            }
            String result = tdOrderServiceGWImpl.bkuPaymentResultNotice(bkuValidateMap);
            logger.info("接口处理结果：" + result);
            return result;
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            return BaseDictConstant.BKU_PAYMENT_RESULT_FAILURE;
        }
    }

    @RequestMapping(value = "yeebaoTransferNotice", method = RequestMethod.POST)
    @ResponseBody
    public String yeebaoTransferNotice(HttpServletRequest request) throws IOException {
        InputStream is = request.getInputStream();
        byte bytes[] = new byte[request.getContentLength()];
        is.read(bytes);
        String notifyXml = new String(bytes, request.getCharacterEncoding());
        logger.info("xml data：" + notifyXml);
        try {
        	logger.info("yeebaoTransferNotice易宝支付异步通知开始接受报文："+notifyXml );
            // 解密 
        	Map<String,Object> receiveMap = paymentService.transferNotify(notifyXml);
        	//  批次号
    		String batch_No = String.valueOf(receiveMap.get("batch_No"));
    		// order_Id 订单号是最长50 位，不能重复C(1,50)
    		String order_Id = String.valueOf(receiveMap.get("order_Id"));
    		// status 打款状态是S：已到帐F 失败C(1)
    		String status = String.valueOf(receiveMap.get("status"));
    		// message
    		String message = String.valueOf(receiveMap.get("message"));

            String redisKey = "withdrawOrderResultExecute_".concat(order_Id);
            int expire = 60;
            Long redisValue = redisService.setNx(redisKey, order_Id, expire);
            if(logger.isInfoEnabled()){
                logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
            }
            if(null == redisValue || redisValue == 0){
                if(logger.isInfoEnabled()){
                    logger.info("当前订单发生并发请求，已阻止");
                }
                return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
            }
            supplierTradeServiceGW.WithdrawActiveNotify(batch_No, order_Id, status, message);
            redisService.delete(redisKey);

            Map<String,String> params = new HashMap<String,String>();
            params.put("batch_No", batch_No);
            params.put("order_Id", order_Id);
            String result = paymentService.transferNotifyBack(params);
            logger.info("接口处理结果：" + result);
            is.close();
            return result;
        } catch (Exception ex) {
            is.close();
            logger.error("访问接口yeebaoTransferNotice出现异常：" + ex.getMessage(), ex);
            return BaseDictConstant.YEEPAY_PAYMENT_RESULT_FAILURE;
        }
    }

    @RequestMapping(value = "wechatPayNotice", method = RequestMethod.POST)
    @ResponseBody
    public String wechatPayNotice(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        byte bytes[] = new byte[request.getContentLength()];
        inputStream.read(bytes);
        String notifyXml = new String(bytes, request.getCharacterEncoding());
        try {
            if(logger.isInfoEnabled()){
                logger.info("微信支付结果异步通知，请求参数：ipAddress=" + GetRemoteIpAddr.getIpAddr(request) + "," + notifyXml);
            }
            if(ValidateUtils.isEmpty(notifyXml)){
                return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "通知内容为空");
            }
            Map<String, Object> notifyMap = WeChatUtil.analysisNotifyData(notifyXml);
            if(null == notifyMap || notifyMap.size() == 0){
                return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "解析报文出错");
            }
            String returnCode = String.valueOf(notifyMap.get("return_code")).trim();
            if(!WeChatConstants.RETURN_CODE_SUCCESS.equals(returnCode)){
                return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "微信通知状态码异常");
            }
            String fundId = String.valueOf(notifyMap.get("attach")).trim();
            if(ValidateUtils.isEmpty(fundId)){
                return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "附加数据为空");
            }
            String weChatSign = String.valueOf(notifyMap.get("sign")).trim();
            String paySign = WeChatUtil.getSign(notifyMap, fundId);
            if(logger.isInfoEnabled()){
                logger.info("weChatSign=" + weChatSign + ",paySign=" + paySign);
            }
            if(!weChatSign.equals(paySign)){
                return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "签名失败");
            }
            String result = tdOrderServiceGWImpl.weChatPaymentResultNotice(notifyMap, String.valueOf(notifyMap.get("out_trade_no")).trim());
            logger.info("接口处理结果：" + result);
            inputStream.close();
            return result;
        } catch (Exception ex) {
            inputStream.close();
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            return WeChatUtil.returnXmlResult(WeChatConstants.RETURN_CODE_FAIL, "系统异常");
        }
    }
}
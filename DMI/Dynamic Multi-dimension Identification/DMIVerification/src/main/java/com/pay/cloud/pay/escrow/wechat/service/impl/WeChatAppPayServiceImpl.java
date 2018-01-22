package com.pay.cloud.pay.escrow.wechat.service.impl;

import com.pay.cloud.core.common.JsonStringUtils;
import com.pay.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.pay.cloud.pay.escrow.wechat.service.WeChatAppPayService;
import com.pay.cloud.pay.escrow.wechat.utils.WeChatUtil;
import com.pay.cloud.util.HttpsPost;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.XmlToMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信App支付服务
 * Created by yacheng.ji on 2017/2/16.
 */
@Service("weChatAppPayServiceImpl")
public class WeChatAppPayServiceImpl implements WeChatAppPayService {

    private static final Logger logger = Logger.getLogger(WeChatAppPayServiceImpl.class);

    /**
     * 请求微信支付接口
     * @param requestUrl 接口地址
     * @param requestMap 请求参数
     * @return 响应报文
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> requestWechatPay(String requestUrl, Map<String, Object> requestMap) throws Exception {
        String result = HttpsPost.doPost(requestUrl, WeChatUtil.map2Xml(requestMap), HttpsPost.CONTENT_TYPE_XML, HttpsPost.DEFAULT_CHARSET, HttpsPost.CONNECT_TIMEOUT, HttpsPost.READ_TIMEOUT);
        if(logger.isInfoEnabled()){
            logger.info("result=" + result);
        }
        Map<String, Object> responseMap = new HashMap<>();
        if(ValidateUtils.isEmpty(result)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "访问接口异常");
            return responseMap;
        }
        responseMap = XmlToMap.xml2mapWithAttr(result, false);
        if(null == responseMap || responseMap.size() == 0){
            responseMap = new HashMap<>();
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "解析XML结果异常");
            return responseMap;
        }
        return responseMap;
    }

    /**
     * 统一下单
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return 下单结果
     * @throws Exception
     */
    public Map<String, Object> unifiedOrder(Map<String, Object> params, String fundId) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        String device_info = String.valueOf(params.get("device_info")).trim();
        if(ValidateUtils.isEmpty(device_info)){
            device_info = "";
        }
        String body = String.valueOf(params.get("body")).trim();
        if(ValidateUtils.isEmpty(body)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "body不能为空");
            return responseMap;
        }
        String detail = String.valueOf(params.get("detail")).trim();
        if(ValidateUtils.isEmpty(detail)){
            detail = "";
        }
        String attach = String.valueOf(params.get("attach")).trim();
        if(ValidateUtils.isEmpty(attach)){
            attach = "";
        }
        String out_trade_no = String.valueOf(params.get("out_trade_no")).trim();
        if(ValidateUtils.isEmpty(out_trade_no)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "out_trade_no不能为空");
            return responseMap;
        }
        String total_fee = String.valueOf(params.get("total_fee")).trim();
        if(ValidateUtils.isEmpty(total_fee) || Integer.parseInt(total_fee) <= 0){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "total_fee不是有效整数");
            return responseMap;
        }
        String spbill_create_ip = String.valueOf(params.get("spbill_create_ip")).trim();
        if(ValidateUtils.isEmpty(spbill_create_ip)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "spbill_create_ip不能为空");
            return responseMap;
        }
        String time_start = String.valueOf(params.get("time_start")).trim();
        if(ValidateUtils.isEmpty(time_start)){
            time_start = "";
        }
        String time_expire = String.valueOf(params.get("time_expire")).trim();
        if(ValidateUtils.isEmpty(time_expire)){
            time_expire = "";
        }
        String goods_tag = String.valueOf(params.get("goods_tag")).trim();
        if(ValidateUtils.isEmpty(goods_tag)){
            goods_tag = "";
        }
        String notify_url = String.valueOf(params.get("notify_url")).trim();
        if(ValidateUtils.isEmpty(notify_url)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "notify_url不能为空");
            return responseMap;
        }
        String limit_pay = String.valueOf(params.get("limit_pay")).trim();
        if(ValidateUtils.isEmpty(limit_pay)){
            limit_pay = "";
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("appid", WeChatUtil.getAppId(fundId));
        requestMap.put("mch_id", WeChatUtil.getMchId(fundId));
        requestMap.put("device_info", device_info);
        requestMap.put("nonce_str", WeChatUtil.getNonceStr());
        requestMap.put("sign_type", WeChatConstants.SIGN_TYPE);
        requestMap.put("body", body);
        requestMap.put("detail", detail);
        requestMap.put("attach", attach);
        requestMap.put("out_trade_no", out_trade_no);
        requestMap.put("fee_type", WeChatConstants.FEE_TYPE);
        requestMap.put("total_fee", Integer.valueOf(total_fee));
        requestMap.put("spbill_create_ip", spbill_create_ip);
        requestMap.put("time_start", time_start);
        requestMap.put("time_expire", time_expire);
        requestMap.put("goods_tag", goods_tag);
        requestMap.put("notify_url", notify_url);
        requestMap.put("trade_type", WeChatConstants.TRADE_TYPE);
        requestMap.put("limit_pay", limit_pay);
        requestMap.put("sign", WeChatUtil.getSign(requestMap, fundId));
        return this.requestWechatPay(WeChatUtil.getUnifiedOrderUrl(fundId), requestMap);
    }

    /**
     * 调起支付
     * @param prepayId 预支付交易会话ID
     * @param fundId 资金平台ID
     * @return 请求串
     * @throws Exception
     */
    public String transferPayment(String prepayId, String fundId) throws Exception {
        if(ValidateUtils.isEmpty(prepayId)){
            return null;
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("appid", WeChatUtil.getAppId(fundId));
        requestMap.put("partnerid", WeChatUtil.getMchId(fundId));
        requestMap.put("prepayid", prepayId.trim());
        requestMap.put("package", WeChatConstants.PACKAGE);
        requestMap.put("noncestr", WeChatUtil.getNonceStr());
        requestMap.put("timestamp", WeChatUtil.getTimeStamp());
        requestMap.put("sign", WeChatUtil.getSign(requestMap, fundId));
        return JsonStringUtils.objectToJsonString(requestMap);
    }

    /**
     * 查询订单(“微信订单号”和“商户订单号”可二选一)
     * @param transactionId 微信订单号
     * @param outTradeNo 商户订单号
     * @param fundId 资金平台ID
     * @return 订单信息
     * @throws Exception
     */
    public Map<String, Object> orderQuery(String transactionId, String outTradeNo, String fundId) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        if(ValidateUtils.isEmpty(transactionId) && ValidateUtils.isEmpty(outTradeNo)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "transactionId与outTradeNo不能同时为空");
            return responseMap;
        }
        if(ValidateUtils.isEmpty(transactionId)){
            transactionId = "";
        }
        if(ValidateUtils.isEmpty(outTradeNo)){
            outTradeNo = "";
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("appid", WeChatUtil.getAppId(fundId));
        requestMap.put("mch_id", WeChatUtil.getMchId(fundId));
        requestMap.put("transaction_id", transactionId.trim());
        requestMap.put("out_trade_no", outTradeNo.trim());
        requestMap.put("nonce_str", WeChatUtil.getNonceStr());
        requestMap.put("sign", WeChatUtil.getSign(requestMap, fundId));
        return this.requestWechatPay(WeChatUtil.getOrderQueryUrl(fundId), requestMap);
    }

    /**
     * 关闭订单
     * @param outTradeNo 商户订单号[订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟]
     * @param fundId 资金平台ID
     * @return 订单关闭结果
     * @throws Exception
     */
    public Map<String, Object> closeOrder(String outTradeNo, String fundId) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        if(ValidateUtils.isEmpty(outTradeNo)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "outTradeNo不能为空");
            return responseMap;
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("appid", WeChatUtil.getAppId(fundId));
        requestMap.put("mch_id", WeChatUtil.getMchId(fundId));
        requestMap.put("out_trade_no", outTradeNo.trim());
        requestMap.put("nonce_str", WeChatUtil.getNonceStr());
        requestMap.put("sign", WeChatUtil.getSign(requestMap, fundId));
        return this.requestWechatPay(WeChatUtil.getCloseOrderUrl(fundId), requestMap);
    }

    /**
     * 申请退款
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return 申请结果
     * @throws Exception
     */
    public Map<String, Object> refundApply(Map<String, String> params, String fundId) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        String device_info = String.valueOf(params.get("device_info")).trim();
        if(ValidateUtils.isEmpty(device_info)){
            device_info = "";
        }
        String transaction_id = String.valueOf(params.get("transaction_id")).trim();
        String out_trade_no = String.valueOf(params.get("out_trade_no")).trim();
        if(ValidateUtils.isEmpty(transaction_id) && ValidateUtils.isEmpty(out_trade_no)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "transaction_id与out_trade_no不能同时为空");
            return responseMap;
        }
        if(ValidateUtils.isEmpty(transaction_id)){
            transaction_id = "";
        }
        if(ValidateUtils.isEmpty(out_trade_no)){
            out_trade_no = "";
        }
        String out_refund_no = String.valueOf(params.get("out_refund_no")).trim();
        if(ValidateUtils.isEmpty(out_refund_no)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "out_refund_no不能为空");
            return responseMap;
        }
        String total_fee = String.valueOf(params.get("total_fee")).trim();
        if(ValidateUtils.isEmpty(total_fee) || Integer.parseInt(total_fee) <= 0){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "total_fee不是有效整数");
            return responseMap;
        }
        String refund_fee = String.valueOf(params.get("refund_fee")).trim();
        if(ValidateUtils.isEmpty(refund_fee) || Integer.parseInt(refund_fee) <= 0){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "refund_fee不是有效整数");
            return responseMap;
        }
        String refund_account = String.valueOf(params.get("device_info")).trim();
        if(ValidateUtils.isEmpty(refund_account)){
            refund_account = "";
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("appid", WeChatUtil.getAppId(fundId));
        requestMap.put("mch_id", WeChatUtil.getMchId(fundId));
        requestMap.put("device_info", device_info);
        requestMap.put("transaction_id", transaction_id);
        requestMap.put("out_trade_no", out_trade_no);
        requestMap.put("out_refund_no", out_refund_no);
        requestMap.put("total_fee", Integer.valueOf(total_fee));
        requestMap.put("refund_fee", Integer.valueOf(refund_fee));
        requestMap.put("refund_fee_type", WeChatConstants.FEE_TYPE);
        requestMap.put("op_user_id", WeChatUtil.getMchId(fundId));
        requestMap.put("refund_account", refund_account);
        requestMap.put("nonce_str", WeChatUtil.getNonceStr());
        requestMap.put("sign", WeChatUtil.getSign(requestMap, fundId));
        return WeChatUtil.sendHttpsByCert(WeChatUtil.map2Xml(requestMap), WeChatUtil.getApplyRefundCertFilePath(fundId), WeChatUtil.getRefundApplyUrl(fundId), fundId);
    }

    /**
     * 查询退款
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return 退款结果
     * @throws Exception
     */
    public Map<String, Object> refundQuery(Map<String, String> params, String fundId) throws Exception {
        String device_info = String.valueOf(params.get("device_info")).trim();
        String transaction_id = String.valueOf(params.get("transaction_id")).trim();
        String out_trade_no = String.valueOf(params.get("out_trade_no")).trim();
        String out_refund_no = String.valueOf(params.get("out_refund_no")).trim();
        String refund_id = String.valueOf(params.get("refund_id")).trim();
        Map<String, Object> responseMap = new HashMap<>();
        if(ValidateUtils.isEmpty(transaction_id) && ValidateUtils.isEmpty(out_trade_no) &&
                ValidateUtils.isEmpty(out_refund_no) && ValidateUtils.isEmpty(refund_id)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "transaction_id,out_trade_no,out_refund_no,refund_id不能同时为空");
            return responseMap;
        }
        if(ValidateUtils.isEmpty(device_info)){
            device_info = "";
        }
        if(ValidateUtils.isEmpty(transaction_id)){
            transaction_id = "";
        }
        if(ValidateUtils.isEmpty(out_trade_no)){
            out_trade_no = "";
        }
        if(ValidateUtils.isEmpty(out_refund_no)){
            out_refund_no = "";
        }
        if(ValidateUtils.isEmpty(refund_id)){
            refund_id = "";
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("appid", WeChatUtil.getAppId(fundId));
        requestMap.put("mch_id", WeChatUtil.getMchId(fundId));
        requestMap.put("device_info", device_info);
        requestMap.put("transaction_id", transaction_id);
        requestMap.put("out_trade_no", out_trade_no);
        requestMap.put("out_refund_no", out_refund_no);
        requestMap.put("refund_id", refund_id);
        requestMap.put("nonce_str", WeChatUtil.getNonceStr());
        requestMap.put("sign", WeChatUtil.getSign(requestMap, fundId));
        return this.requestWechatPay(WeChatUtil.getRefundQueryUrl(fundId), requestMap);
    }
}

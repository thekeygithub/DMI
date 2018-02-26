package com.models.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.models.cloud.gw.protocolfactory.DoServiceInterface;
import com.models.cloud.gw.service.supplier.SupplierServiceGW;
import com.models.cloud.gw.service.trade.TdOrderRefundServiceGW;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.EncryptUtils;
import com.models.cloud.util.hint.Hint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部App通知入口（不对外，仅内部使用）
 * Created by yacheng.ji on 2016/6/1.
 */
@Controller
@RequestMapping(value = "/private")
public class PrivateNoticeControler {

	private static final Logger logger = Logger.getLogger(PrivateNoticeControler.class);
	
	//商户退款接口
	@Resource(name = "tdOrderRefundServiceGWImpl")
    private TdOrderRefundServiceGW tdOrderRefundServiceGW;

    @Resource(name = "supplierServiceGWImpl")
    private SupplierServiceGW supplierServiceGW;

    @RequestMapping(value = "queryOrder", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String queryOrder(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> respMap = new HashMap<String, Object>();
        try {
            if(logger.isInfoEnabled()){
                logger.info("[内部接口] 查询交易单信息，输入参数：" + map);
            }
            String sign = String.valueOf(map.get("sign")).trim();
            String signString = "payOrderId=".concat(String.valueOf(map.get("payOrderId"))).concat("&merOrderId=")
                    .concat(String.valueOf(map.get("merOrderId"))).concat("&accountId=").concat(String.valueOf(map.get("accountId"))).
                            concat("&timestamp=").concat(String.valueOf(map.get("timestamp")));
            String ebSign = EncryptUtils.aesEncrypt(signString, CipherAesEnum.PRIVATECALL);
            if(logger.isInfoEnabled()){
                logger.info("sign=" + sign + ",ebSign=" + ebSign);
            }
            if(!sign.equals(ebSign)){
                respMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
                respMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
                return JSON.toJSONString(respMap);
            }
            WebApplicationContext webApp = RequestContextUtils.getWebApplicationContext(request, request.getSession().getServletContext());
            DoServiceInterface doServiceInterface = (DoServiceInterface) webApp.getBean("queryTradeInfoInterfaceImpl");
            respMap = doServiceInterface.doService(map);
            if(logger.isInfoEnabled()){
                logger.info("接口响应报文：" + respMap);
            }
            return JSON.toJSONString(respMap);
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            respMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            respMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return JSON.toJSONString(respMap);
        }
    }
    /**
     * 退款订单提交
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "payRefundSubmit", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String payRefundSubmit(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> respMap = new HashMap<String, Object>();
        try {
            logger.info("[内部接口] 退款提交，输入参数：" + map);
            String sign = String.valueOf(map.get("sign")).trim();
            String signString = "amount="
            		.concat(String.valueOf(map.get("amount")))
            		.concat("&orderId=").concat(String.valueOf(map.get("orderId")))
            		.concat("&fundTdCode=").concat(String.valueOf(map.get("fundTdCode")))
                    .concat("&cause=").concat(String.valueOf(map.get("cause")))
                    .concat("&ybOrderId=").concat(String.valueOf(map.get("ybOrderId")))
                    .concat("&orderIdSign=").concat(String.valueOf(map.get("orderIdSign")))
                    .concat("&timeStamp=").concat(String.valueOf(map.get("timeStamp")))
                    .concat("&oriTdId=").concat(String.valueOf(map.get("oriTdId")))
                    .concat("&fundId=").concat(String.valueOf(map.get("fundId")));
            String ebSign = EncryptUtils.aesEncrypt(signString, CipherAesEnum.PRIVATECALL);
            logger.info("sign=" + sign + ",ebSign=" + ebSign);
            if(!sign.equals(ebSign)){
                respMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
                respMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
                return JSON.toJSONString(respMap);
            }
            respMap = tdOrderRefundServiceGW.PayOrderRefund(map);
            logger.info("退款提交接口响应报文：" + respMap);
            return JSON.toJSONString(respMap);
        } catch (Exception ex) {
            logger.error("退款提交访问接口出现异常：" + ex.getMessage(), ex);
            respMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            respMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return JSON.toJSONString(respMap);
        }
    }
    
    /**
     * 退款订单确认
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "payRefundConfirm", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String payRefundConfirm(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> respMap = new HashMap<String, Object>();
        try {
            logger.info("[内部接口] 退款确认，输入参数：" + map);
            String sign = String.valueOf(map.get("sign")).trim();
            String signString = "orderId="
            		.concat(String.valueOf(map.get("orderId")))
            		.concat("&fundTdCode=").concat(String.valueOf(map.get("fundTdCode")))
            		.concat("&ybOrderId=").concat(String.valueOf(map.get("ybOrderId")))
            		.concat("&fundId=").concat(String.valueOf(map.get("fundId")))
                    .concat("&orderIdSign=").concat(String.valueOf(map.get("orderIdSign")))
                    .concat("&oriTdId=").concat(String.valueOf(map.get("oriTdId")))
                    .concat("&timeStamp=").concat(String.valueOf(map.get("timeStamp")));
            String ebSign = EncryptUtils.aesEncrypt(signString, CipherAesEnum.PRIVATECALL);
            logger.info("sign=" + sign + ",ebSign=" + ebSign);
            if(!sign.equals(ebSign)){
                respMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
                respMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
                return JSON.toJSONString(respMap);
            }
            respMap = tdOrderRefundServiceGW.payRefundQuery(map);
            logger.info("退款确认接口响应报文：" + respMap);
            return JSON.toJSONString(respMap);
        } catch (Exception ex) {
            logger.error("退款确认访问接口出现异常：" + ex.getMessage(), ex);
            respMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            respMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return JSON.toJSONString(respMap);
        }
    }

    /**
     * 社保撤消退款订单提交
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "medicalSettleCancel", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String medicalSettleCancel(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> respMap = new HashMap<>();
        try {
            logger.info("[内部接口] 医保退款提交，输入参数：" + map);
            String sign = String.valueOf(map.get("sign")).trim();
            String signString = "oriTdId=".concat(String.valueOf(map.get("oriTdId")))
                    .concat("&timeStamp=").concat(String.valueOf(map.get("timeStamp")));
            String ebSign = EncryptUtils.aesEncrypt(signString, CipherAesEnum.PRIVATECALL);
            logger.info("sign=" + sign + ",ebSign=" + ebSign);
            if(!sign.equals(ebSign)){
                respMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
                respMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
                return JSON.toJSONString(respMap);
            }
            respMap = tdOrderRefundServiceGW.payOrderRefundSi(map);
            logger.info("医保退款提交接口响应报文：" + respMap);
            return JSON.toJSONString(respMap);
        } catch (Exception ex) {
            logger.error("医保退款提交访问接口出现异常：" + ex.getMessage(), ex);
            respMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            respMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return JSON.toJSONString(respMap);
        }
    }

    /**
     * 医保结算单撤销确认
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "medicalSettleCancelConfirm", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String medicalSettleCancelConfirm(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> respMap = new HashMap<>();
        try {
            logger.info("[内部接口] 医保退款确认，输入参数：" + map);
            String sign = String.valueOf(map.get("sign")).trim();
            String signString = "oriTdId=".concat(String.valueOf(map.get("oriTdId")))
                    .concat("&timeStamp=").concat(String.valueOf(map.get("timeStamp")));
            String ebSign = EncryptUtils.aesEncrypt(signString, CipherAesEnum.PRIVATECALL);
            logger.info("sign=" + sign + ",ebSign=" + ebSign);
            if(!sign.equals(ebSign)){
                respMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
                respMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
                return JSON.toJSONString(respMap);
            }
            respMap = tdOrderRefundServiceGW.payRefundQuerySi(map);
            logger.info("退款确认接口响应报文：" + respMap);
            return JSON.toJSONString(respMap);
        } catch (Exception ex) {
            logger.error("退款确认访问接口出现异常：" + ex.getMessage(), ex);
            respMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            respMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return JSON.toJSONString(respMap);
        }
    }

    /**
     * YeePay提现结果确认
     * @param map
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "withdrawResultConfirm", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String withdrawResultConfirm(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> respMap = new HashMap<>();
        try {
            logger.info("[内部接口] YeePay提现结果确认，输入参数：" + map);
            String sign = String.valueOf(map.get("sign")).trim();
            String signString = "withdrawOrderId=".concat(String.valueOf(map.get("withdrawOrderId")))
                    .concat("&timeStamp=").concat(String.valueOf(map.get("timeStamp")));
            String ebSign = EncryptUtils.aesEncrypt(signString, CipherAesEnum.PRIVATECALL);
            logger.info("sign=" + sign + ",ebSign=" + ebSign);
            if(!sign.equals(ebSign)){
                respMap.put("resultCode", Hint.SYS_10003_SIGN_ERROR.getCodeString());
                respMap.put("resultDesc", Hint.SYS_10003_SIGN_ERROR.getMessage());
                return JSON.toJSONString(respMap);
            }
            respMap = supplierServiceGW.withdrawResultConfirm(map);
            logger.info("接口响应报文：" + respMap);
            return JSON.toJSONString(respMap);
        } catch (Exception ex) {
            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            respMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            respMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return JSON.toJSONString(respMap);
        }
    }
}

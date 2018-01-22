package com.pay.cloud.gw.service.trade;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * 易保支付平台异步通知
 * Created by yacheng.ji on 2016/4/20.
 */
public class EbaoAsynchronousNotice implements Runnable {

    private static final Logger logger = Logger.getLogger(EbaoAsynchronousNotice.class);

    private TdOrderServiceGW tdOrderServiceGWImpl;
    private Map<String, String> dataMap = new HashMap<String, String>();

    public EbaoAsynchronousNotice(TdOrderServiceGW tdOrderServiceGWImpl, Map<String, String> dataMap) {
        this.tdOrderServiceGWImpl = tdOrderServiceGWImpl;
        this.dataMap = dataMap;
    }

    public void run() {
        try {
            tdOrderServiceGWImpl.noticeMerchantPaymentResult(dataMap);
        } catch (Exception ex) {
            logger.error("[" + dataMap.get("payOrderId") + "]异步通知商户订单支付结果出现异常：" + ex.getMessage(), ex);
        }
    }
}

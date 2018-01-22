package com.pay.cloud.gw.protocolfactory.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.cloud.gw.protocolfactory.DoServiceInterface;
import com.pay.cloud.gw.service.trade.TdOrderServiceGW;
import com.pay.cloud.util.MapUtil;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 变更商业支付通道
 * Created by yacheng.ji on 2017/2/24.
 */
@Service("changeFundIdInterfaceImpl")
public class ChangeFundIdInterfaceImpl implements DoServiceInterface {

    private static final Logger logger = Logger.getLogger(ChangeFundIdInterfaceImpl.class);

    @Resource
    private TdOrderServiceGW tdOrderServiceGWImpl;

    public Map<String, Object> doService(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<>();
        String payOrderId = String.valueOf(receiveMap.get("payOrderId")).trim();//支付平台订单号
        if(ValidateUtils.isEmpty(payOrderId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payOrderId"));
            return resultMap;
        }
        String accountId = String.valueOf(receiveMap.get("accountId")).trim();//用户账户
        if(ValidateUtils.isEmpty(accountId)){
            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "accountId"));
            return resultMap;
        }
        if(!ValidateUtils.isPositiveInteger(accountId)){
            resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "accountId"));
            return resultMap;
        }
        String fundId = String.valueOf(receiveMap.get("fundId")).trim();//资金平台编码
        if(ValidateUtils.isEmpty(fundId)){
            fundId = "";
        }
        String longitude = String.valueOf(receiveMap.get("longitude")).trim();//经度
        if(ValidateUtils.isEmpty(longitude)){
            longitude = "";
        }
        String latitude = String.valueOf(receiveMap.get("latitude")).trim();//纬度
        if(ValidateUtils.isEmpty(latitude)){
            latitude = "";
        }
        if(ValidateUtils.isNotEmpty(longitude) && ValidateUtils.isNotEmpty(latitude)){
            MapUtil mapUtil = MapUtil.gpsToBaidu(Double.parseDouble(latitude), Double.parseDouble(longitude), "");
            if(logger.isInfoEnabled()){
                logger.info("GPS原始定位：longitude=" + longitude + ",latitude=" + latitude +
                        " 转换为百度坐标：longitude=" + mapUtil.getBdLng() + ",latitude=" + mapUtil.getBdLat());
            }
            longitude = String.valueOf(mapUtil.getBdLng());
            latitude = String.valueOf(mapUtil.getBdLat());
        }
        try {
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("payOrderId", payOrderId);
            inputMap.put("accountId", accountId);
            inputMap.put("fundId", fundId);
            inputMap.put("longitude", longitude);
            inputMap.put("latitude", latitude);
            resultMap = tdOrderServiceGWImpl.changeFundId(inputMap);
        } catch (Exception e) {
            logger.error("系统错误：" + e.getMessage(), e);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
        }
        return resultMap;
    }
}

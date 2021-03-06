package com.pay.cloud.web.controller.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.web.controller.DoWebPageService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yacheng.ji on 2016/6/29.
 */
@Service("pcToConfirmPayPageServiceImpl")
public class ToConfirmPayPageServiceImpl implements DoWebPageService {

    private static final Logger logger = Logger.getLogger(ToConfirmPayPageServiceImpl.class);

    public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());

        resultMap.put("cardNo", request.getParameter("cardNo").replaceAll(" ", ""));
        resultMap.put("cardTypeName", request.getParameter("cardTypeName"));
        resultMap.put("bankName", request.getParameter("bankName"));
        resultMap.put("bankIconUrl", request.getParameter("bankIconUrl"));
        resultMap.put("phone", request.getParameter("phone"));
//        if(logger.isInfoEnabled()){
//        	logger.info("进入绑卡确认支付页面 传入参数：" + resultMap.toString());
//        }
        return resultMap;
    }

    public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
        return null;
    }
}

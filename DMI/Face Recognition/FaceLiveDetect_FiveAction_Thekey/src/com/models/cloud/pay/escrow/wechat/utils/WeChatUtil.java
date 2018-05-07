package com.models.cloud.pay.escrow.wechat.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.models.cloud.pay.escrow.wechat.constant.WeChatConstants;
import com.models.cloud.pay.escrow.yeepay.utils.RandomUtil;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.Md5Util;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.XmlToMap;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.*;

/**
 * 微信支付相关工具类
 * Created by yacheng.ji on 2017/2/20.
 */
public class WeChatUtil {

    private static final Logger logger = Logger.getLogger(WeChatUtil.class);

    /**
     * 获取随机字符串
     * @return 长度32位
     * @throws Exception
     */
    public static String getNonceStr() throws Exception {
        return RandomUtil.getRandom(32);
    }

    /**
     * 获取时间戳
     * @return 长度10位
     * @throws Exception
     */
    public static String getTimeStamp() throws Exception {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取应用ID
     * @param fundId 资金平台ID
     * @return appId
     * @throws Exception
     */
    public static String getAppId(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.APP_ID));
    }

    /**
     * 获取商户号
     * @param fundId 资金平台ID
     * @return mchId
     * @throws Exception
     */
    public static String getMchId(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.MCH_ID));
    }

    /**
     * 获取统一下单接口地址
     * @param fundId 资金平台ID
     * @return unifiedOrderUrl
     * @throws Exception
     */
    public static String getUnifiedOrderUrl(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.UNIFIED_ORDER_URL));
    }

    /**
     * 获取查询订单接口地址
     * @param fundId 资金平台ID
     * @return orderQueryUrl
     * @throws Exception
     */
    public static String getOrderQueryUrl(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.ORDER_QUERY_URL));
    }

    /**
     * 获取关闭订单接口地址
     * @param fundId 资金平台ID
     * @return closeOrderUrl
     * @throws Exception
     */
    public static String getCloseOrderUrl(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.CLOSE_ORDER_URL));
    }

    /**
     * 获取申请退款接口地址
     * @param fundId 资金平台ID
     * @return refundApplyUrl
     * @throws Exception
     */
    public static String getRefundApplyUrl(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.REFUND_APPLY_URL));
    }

    /**
     * 获取查询退款接口地址
     * @param fundId 资金平台ID
     * @return refundQueryUrl
     * @throws Exception
     */
    public static String getRefundQueryUrl(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.REFUND_QUERY_URL));
    }

    /**
     * 获取申请退款证书文件路径
     * @param fundId 资金平台ID
     * @return 相对路径
     * @throws Exception
     */
    public static String getApplyRefundCertFilePath(String fundId) throws Exception {
        return CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.APPLY_REFUND_CERT_FILE_PATH));
    }

    /**
     * 获取Sign
     * @param params 输入Map
     * @param fundId 资金平台ID
     * @return sign串
     * @throws Exception
     */
    public static String getSign(Map<String, Object> params, String fundId) throws Exception {
        if(null == params || params.size() == 0){
            if(logger.isInfoEnabled()){
                logger.info("传入参数为空 params=" + params);
            }
            return null;
        }
        SortedMap<String, Object> sortedMap = new TreeMap<>(params);
        StringBuilder stringBuilder = new StringBuilder(128);
        Set entrySet = sortedMap.entrySet();
        Map.Entry entry;
        String key;
        String value;
        for (Object object : entrySet) {
            entry = (Map.Entry) object;
            key = String.valueOf(entry.getKey()).trim();
            value = String.valueOf(entry.getValue()).trim();
            if (ValidateUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                stringBuilder.append(key).append("=").append(value).append("&");
            }
        }
        stringBuilder.append("key=").append(CacheUtils.getPpIntfPara(fundId.concat("_").concat(WeChatConstants.WECHAT_API_KEY)));
        String sign = String.valueOf(Md5Util.getKeyedDigest(stringBuilder.toString(), "MD5", "", WeChatConstants.CHARSET_UTF8)).trim().toUpperCase();
        if(logger.isInfoEnabled()){
            logger.info("生成Sign结果：sign=" + sign);
        }
        if(ValidateUtils.isEmpty(sign)){
            if(logger.isInfoEnabled()){
                logger.info("生成Sign失败");
            }
            return null;
        }
        return sign;
    }

    /**
     * 解析微信支付后台通知结果
     * @param xml XML格式数据
     * @return MAP
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> analysisNotifyData(String xml) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        if(ValidateUtils.isEmpty(xml)){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "待解析XML数据为空");
            return responseMap;
        }
        responseMap = XmlToMap.xml2mapWithAttr(xml, false);
        if(null == responseMap || responseMap.size() == 0){
            responseMap = new HashMap<>();
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "XML转成MAP出现异常");
            return responseMap;
        }
        return responseMap;
    }

    /**
     * https请求（需要证书）
     * @param data xml数据
     * @param wechatCertFilePath 证书路径
     * @param interfaceUrl 访问接口地址
     * @param fundId 资金平台ID
     * @return 结果Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> sendHttpsByCert(String data, String wechatCertFilePath, String interfaceUrl, String fundId) {
        FileInputStream instream = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        StringBuilder result = new StringBuilder();
        Map<String, Object> responseMap = new HashMap<>();
        try{
            instream = new FileInputStream(new File(wechatCertFilePath));
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            String mchId = WeChatUtil.getMchId(fundId);
            keyStore.load(instream, mchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            HttpPost httppost = new HttpPost(interfaceUrl);
            httppost.addHeader("Connection", "keep-alive");
            httppost.addHeader("Accept", "*/*");
            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("Host", "api.mch.weixin.qq.com");
            httppost.addHeader("X-Requested-With", "XMLHttpRequest");
            httppost.addHeader("Cache-Control", "max-age=0");
            httppost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)");
            if(logger.isInfoEnabled()){
                logger.info("executing request:" + httppost.getRequestLine());
            }
            httppost.setEntity(new StringEntity(data, WeChatConstants.CHARSET_UTF8));
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if(logger.isInfoEnabled()){
                logger.info("response statusLine:" + response.getStatusLine());
            }
            if (entity != null) {
                if(logger.isInfoEnabled()){
                    logger.info("Response content length: " + entity.getContentLength());
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    result.append(text);
                }
            }
            EntityUtils.consume(entity);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }finally {
            try {
                if(null != instream){
                    instream.close();
                }
                if(null != response){
                    response.close();
                }
                if(null != httpclient){
                    httpclient.close();
                }
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
        if(ValidateUtils.isEmpty(result.toString())){
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "访问接口异常");
            return responseMap;
        }
        try {
            responseMap = XmlToMap.xml2mapWithAttr(result.toString(), false);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            responseMap = new HashMap<>();
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "XML转换MAP出现异常");
            return responseMap;
        }
        if(null == responseMap || responseMap.size() == 0){
            responseMap = new HashMap<>();
            responseMap.put("return_code", WeChatConstants.RETURN_CODE_FAIL);
            responseMap.put("return_msg", "XML转换MAP出现异常");
            return responseMap;
        }
        return responseMap;
    }

    /**
     * 响应微信支付回调通知结果
     * @param errCode 错误代码
     * @param errMsg 错误描述
     * @return xml
     */
    public static String returnXmlResult(String errCode, String errMsg) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("return_code", errCode);
        responseMap.put("return_msg", errMsg);
        return map2Xml(responseMap);
    }

    /**
     * Map转换为XML(目前只针对微信支付相关接口)
     * @param inputMap
     * @return
     */
    public static String map2Xml(Map inputMap) {
        StringBuilder xml = new StringBuilder();
        Object value;
        xml.append("<xml>");
        for(Object key : inputMap.keySet()){
            value = inputMap.get(key);
            xml.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }
}

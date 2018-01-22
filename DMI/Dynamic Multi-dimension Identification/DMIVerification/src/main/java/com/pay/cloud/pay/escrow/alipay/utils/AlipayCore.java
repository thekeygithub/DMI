package com.pay.cloud.pay.escrow.alipay.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayCore {

    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static TreeMap<String, String> paraFilter(Map<String, Object> sArray) {

    	TreeMap<String, String> result = new TreeMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
        	Object value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value.toString());
        }

        return result;
    }
    
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(TreeMap<String, String> params) {
		if(params==null||params.isEmpty()) return "";
		StringBuffer paramStr = new StringBuffer();
		boolean first = true;
		for (Entry<String, String> entry : params.entrySet()) {
			if(StringUtils.isEmpty(entry.getKey())
					||"sign".equalsIgnoreCase(entry.getKey())
					||"sign_type".equalsIgnoreCase(entry.getKey())
					||StringUtils.isEmpty(entry.getValue())) continue;
			if(first){
				paramStr.append(entry.getKey()).append("=").append(entry.getValue()).append("");
				first=false;
			}else{
				paramStr.append("&").append(entry.getKey()).append("=").append(entry.getValue()).append("");
			}
		}
		return paramStr.toString();
    }
    
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串,参数值加双引号
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkStringWithQuotes(TreeMap<String, String> params) {
		if(params==null||params.isEmpty()) return "";
		StringBuffer paramStr = new StringBuffer();
		boolean first = true;
		for (Entry<String, String> entry : params.entrySet()) {
			if(StringUtils.isEmpty(entry.getKey())
					||"sign".equalsIgnoreCase(entry.getKey())
					||"sign_type".equalsIgnoreCase(entry.getKey())
					||StringUtils.isEmpty(entry.getValue())) continue;
			if(first){
				paramStr.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
				first=false;
			}else{
				paramStr.append("&").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
			}
		}
		return paramStr.toString();
    }
}

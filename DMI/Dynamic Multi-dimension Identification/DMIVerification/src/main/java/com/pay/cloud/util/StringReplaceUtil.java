package com.pay.cloud.util;

import org.apache.commons.lang3.StringUtils;

/**
 * String的替换
 */
public class StringReplaceUtil {
	
	///转化方法
	private static String replaceAction(String str, String regular) {
        return str.replaceAll(regular, "*");
    }
	
	 /**
     * 身份证号替换，保留前四位和后四位
     *
     * 如果身份证号为空 或者 null ,返回"" ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     * @return
     */
    public static String idCardReplaceWithStar(Object objIdCard) {
    	//转换为String
    	String strIdCard = ConvertUtils.getString(objIdCard).trim();
        if (StringUtils.isBlank(strIdCard)) {
            return "";
        } else {
            return replaceAction(strIdCard, "(?<=\\w{4})\\w(?=\\w{4})");
        }
    }
    
    /**
     * 银行卡替换，保留前四位
     *
     * 如果银行卡号为空 或者 null ,返回"" ；否则，返回替换后的字符串；
     *
     * @param bankCard 银行卡号
     * @return
     */
    public static String bankCardReplaceWithStar(Object objBankCard) {
        //转换为String
    	String strBankCard = ConvertUtils.getString(objBankCard).trim();
        if (StringUtils.isBlank(strBankCard)) {
            return "";
        } else {
            return replaceAction(strBankCard, "(?<=\\w{4})\\w(?=\\w{0})");
        }
    }

    /**
     * 字符串转换
     * @param str
     * @return
     */
    public static String StrReplaceWithStar(Object obj) {
    	//转换为String
    	String str = ConvertUtils.getString(obj).trim();
    	//返回结果
        String strAfterReplaced = "";
        if (StringUtils.isBlank(str)){
        	str = "";
        }
        int len = str.length();

        if (len == 1) {
            strAfterReplaced = "*";
        }else if (len ==2 ){
        	strAfterReplaced = "**";
        }else if (len == 3 ){
        	strAfterReplaced = "***";
        }else if (len == 4){
        	strAfterReplaced = "****";
        } else if (len > 3 && len <= 6) {
            strAfterReplaced = replaceAction(str, "(?<=\\w{1})\\w(?=\\w{1})");
        } else if (len == 7) {
            strAfterReplaced = replaceAction(str, "(?<=\\w{1})\\w(?=\\w{2})");
        } else if (len == 8) {
            strAfterReplaced = replaceAction(str, "(?<=\\w{2})\\w(?=\\w{2})");
        } else if (len == 9) {
            strAfterReplaced = replaceAction(str, "(?<=\\w{2})\\w(?=\\w{3})");
        } else if (len == 10) {
            strAfterReplaced = replaceAction(str, "(?<=\\w{3})\\w(?=\\w{3})");
        } else if (len >= 11) {
            strAfterReplaced = replaceAction(str, "(?<=\\w{3})\\w(?=\\w{4})");
        }

        return strAfterReplaced;
    }

}

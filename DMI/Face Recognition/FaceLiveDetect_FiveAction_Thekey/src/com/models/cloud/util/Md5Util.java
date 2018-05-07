package com.models.cloud.util;

import java.security.MessageDigest;

import org.apache.log4j.Logger;


/**
 * MD5工具类
 */
public class Md5Util {

    private static final Logger logger = Logger.getLogger(Md5Util.class);

    /**
     * MD5加密
     *
     * @param strSrc   原始串
     * @param signType 加密类型MD5
     * @param key      密钥
     * @param charset  字符集
     * @return 加密串
     */
    public static String getKeyedDigest(String strSrc, String signType, String key, String charset) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(signType);
            md5.update(strSrc.getBytes(charset));
            if (null == key) {
                key = "";
            }
            String result = "";
            byte[] temp;
            temp = md5.digest(key.getBytes(charset));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
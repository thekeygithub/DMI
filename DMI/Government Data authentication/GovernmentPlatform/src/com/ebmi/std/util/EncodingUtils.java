package com.ebmi.std.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import com.core.exception.FrameworkException;

/**
 * 字符编码工具
 * 
 * @author xiangfeng.guan
 */
public class EncodingUtils {
	public static String trans(String originalString, String originalCharset, String targetCharset) {
		if (StringUtils.isNotBlank(originalString)) {
			try {
				return new String(originalString.trim().getBytes(originalCharset), targetCharset);
			} catch (UnsupportedEncodingException e) {
				throw new FrameworkException(e);
			}
		} else {
			return originalString;
		}
	}

	public static String transGBKToCP1252(String originalString) {
		return trans(originalString, "GBK", "CP1252");
	}

	public static String transCP1252ToGBK(String originalString) {
		return trans(originalString, "CP1252", "GBK");
	}
}

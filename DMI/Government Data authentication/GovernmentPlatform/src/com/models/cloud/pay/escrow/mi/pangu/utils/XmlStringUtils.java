/**
 * Copyright 2012-2015 the original author or authors.
 */
package com.models.cloud.pay.escrow.mi.pangu.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author wangxiao
 * xml 工具类，利用  xStream 开源工具类实现
 */
public class XmlStringUtils {

	public static final String DEFAULT_ENCODING = "UTF-8";

	private static XStream xStream;

	static {
		xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
	}
	
	public static void alias(String name, Class<?> cls) {
		XmlStringUtils.xStream.alias(name, cls);
	}

	/**
	 * 简单对象与XML 转换
	 * @param t
	 * @return t
	 */
	public static <T> String object2Xml(T t) {
		return XmlStringUtils.xStream.toXML(t);
	}

	/**
	 * 简单 xml 字符串转换BEAN
	 * @param xmlString
	 * @return Object
	 */
	public static Object xml2Object(String xmlString) {
		return XmlStringUtils.xStream.fromXML(xmlString);
	}
}
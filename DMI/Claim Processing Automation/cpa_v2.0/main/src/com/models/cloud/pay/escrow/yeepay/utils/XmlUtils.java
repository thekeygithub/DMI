package com.models.cloud.pay.escrow.yeepay.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML工具类
 */
public class XmlUtils {
	
	//日志
	private static Logger log = Logger.getLogger(XmlUtils.class);
	
	/**
	 * xml 转化为 map
	 * @param xml
	 * @return
	 * @throws DocumentException 
	 */
	@SuppressWarnings("rawtypes")
	public static Map xml2Map(String xml) {
		Document document;
		try {
			document = DocumentHelper.parseText(xml);
			Element rootElm = document.getRootElement();
			List elementList = rootElm.elements();
			HashMap<String, Object> outmap = new HashMap<String, Object>();
			if (elementList != null) {
				for (int i = 0; i < elementList.size(); i++) {
					Element item = (Element) elementList.get(i);
					List ishaschild = item.elements();
					if ((ishaschild != null) && (ishaschild.size() > 0)) {
						List sublist = item.elements();
						if (sublist != null) {
							ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
							for (int j = 0; j < sublist.size(); j++) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								Element subitem = (Element) sublist.get(j);
								List finalitemlist = subitem.elements();
								for (int k = 0; k < finalitemlist.size(); k++) {
									Element finalitem = (Element) finalitemlist
											.get(k);
									if (finalitem != null) {
										map.put(finalitem.getName(),
												finalitem.getStringValue());
									}
								}
								arrayList.add(map);
							}
							outmap.put(item.getName(), arrayList);
						}
					} else {
						outmap.put(item.getName(), item.getStringValue());
					}
				}
			}

			return outmap;
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
            //存在错误，返回空对象
			return null;
		}
	}
	
	
	/**
	 * map 转换为 XML 
	 * @param inputmap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String map2Xml(Map inputmap,String decodeCharset){
	    Set keSet = inputmap.keySet();
	    Iterator iterator = keSet.iterator();
	    StringBuffer outxml = new StringBuffer();
	    outxml.append("<?xml version=\"1.0\" encoding=\"" + decodeCharset+ "\"?>\n");
	    outxml.append("<data>\n");
	    while ((iterator != null) && (iterator.hasNext()))
	    {
	      String key = String.valueOf(iterator.next());
	      Object object = inputmap.get(key);
	      if ((object instanceof String)) {
	    		outxml.append("<" + key + ">" + String.valueOf(object) + "</" + key + ">\n");
	      } else if ((object instanceof List)) {
	        outxml.append("<list>\n");
	        ArrayList<HashMap<String, Object>> list = (ArrayList)object;
	        if (list != null)
	        {
	          for (int i = 0; i < list.size(); i++) {
	            outxml.append("<item>\n");
	            HashMap<String, Object> map = (HashMap)list.get(i);
	            Set subkey = map.keySet();
	            Iterator subkeyiterator = subkey.iterator();
	            while ((subkeyiterator != null) && (subkeyiterator.hasNext())) {
	              String keysub = String.valueOf(subkeyiterator.next());
	              outxml.append("<" + keysub + ">" + map.get(keysub) + "</" + keysub + ">\n");
	            }
	            outxml.append("</item>\n");
	          }
	        }
	        outxml.append("</list>\n");
	      }
	    }
	    outxml.append("</data>\n");
	    
	    return String.valueOf(outxml);
	}

}

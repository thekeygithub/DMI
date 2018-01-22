package com.pay.cloud.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.pay.cloud.util.hint.Hint;

/**
 * 读取xml配置文件
 *
 * @author qingsong.li
 */
public class ReadWebPageXmlUtil {

    private final static String h5pagePath = "webpage.xml";
    private static final Logger logger = Logger.getLogger(ReadWebPageXmlUtil.class);
    
    private static Map<String, Map<String, String>> map = null;

    public static Map<String, Object> beansMap(String pageName) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

       // Map<String, Map<String, String>> map = readInterfaceXml();
        if(map == null){
        	map = readH5PageXml();
        }

        if (map == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "读取配置文件为空！");
            return returnMap;
        }
        Map<String, String> pageMap = map.get(pageName);
        if (pageMap == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "未知页面！");
            return returnMap;
        }
        String pageBeanName = pageMap.get("beanName");
        String pagePath = pageMap.get("pagePath");
        if (pagePath == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "配置页面有误！");
            return returnMap;
        }
        String doService = pageMap.get("doService");
        if (doService == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "处理业务参数有误！");
            return returnMap;
        }
        
        if(doService.equals("1")){
	        if (pageBeanName == null || pageBeanName.equals("")) {
	            returnMap.put("resultCode", "1");
	            returnMap.put("resultDesc", "配置接口bean有误！");
	            return returnMap;
	        }
        	
        }
        
        String auth = pageMap.get("auth");
        
        returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        returnMap.put("resultDesc", "读取成功");
        returnMap.put("beanName", pageBeanName);
        returnMap.put("pagePath", pagePath);
        returnMap.put("doService", doService);
        returnMap.put("auth", auth);
        return returnMap;
    }

    private static Map<String, Map<String, String>> readH5PageXml() {
        SAXReader reader = new SAXReader();
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        try {
            String path = ReadWebPageXmlUtil.class.getClassLoader().getResource("").toURI().getPath();
            if(logger.isInfoEnabled()){
                logger.info("读取配置文件：" + h5pagePath);
            }
            Document document = reader.read(new File(path + h5pagePath));
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();
            for (Element child : childElements) {
                String pageName = child.elementText("pageName");
                String beanName = child.elementText("beanName");
                String classPath = child.elementText("classPath");
                String pagePath = child.elementText("pagePath");
                String doService = child.elementText("doService");
                String auth = child.elementText("auth");
                Map<String, String> interfaceMap = new HashMap<String, String>();
                interfaceMap.put("pageName", pageName);
                interfaceMap.put("beanName", beanName);
                interfaceMap.put("classPath", classPath);
                interfaceMap.put("pagePath", pagePath);
                interfaceMap.put("doService", doService);
                interfaceMap.put("auth", auth);
                map.put(pageName, interfaceMap);
            }
            if(logger.isInfoEnabled()){
                logger.info("读取h5page配置文件结果：" + map);
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return map;
        }
    }
}

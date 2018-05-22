package com.models.cloud.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.models.cloud.util.hint.Hint;

/**
 * 读取xml配置文件
 *
 * @author qingsong.li
 */
public class ReadInterfaceXmlUtil {

    private final static String interfacelPath = "interfacebean.xml";
    private static final Logger logger = Logger.getLogger(ReadInterfaceXmlUtil.class);
    
    private static Map<String, Map<String, String>> map = null;

    public static Map<String, Object> beansMap(String interfaceName) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

       // Map<String, Map<String, String>> map = readInterfaceXml();
        if(map == null){
        	map = readInterfaceXml();
        }

        if (map == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "读取配置文件为空！");
            return returnMap;
        }
        Map<String, String> interfaceMap = map.get(interfaceName);
        if (interfaceMap == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "未知接口！");
            return returnMap;
        }
        String interfaceBeanName = interfaceMap.get("beanName");
        if (interfaceBeanName == null) {
            returnMap.put("resultCode", "1");
            returnMap.put("resultDesc", "配置接口bean有误！");
            return returnMap;
        }
        String auth = interfaceMap.get("auth");
        returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        returnMap.put("resultDesc", "读取成功");
        returnMap.put("beanName", interfaceBeanName);
        returnMap.put("auth", auth);
        return returnMap;
    }

    private static Map<String, Map<String, String>> readInterfaceXml() {
        SAXReader reader = new SAXReader();
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        try {
            String path = ReadInterfaceXmlUtil.class.getClassLoader().getResource("").toURI().getPath();
            if(logger.isInfoEnabled()){
                logger.info("读取配置文件：" + interfacelPath);
            }
            Document document = reader.read(new File(path + interfacelPath));
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();
            for (Element child : childElements) {
                String interfaceCode = child.elementText("interfaceCode");
                String beanName = child.elementText("beanName");
                String classPath = child.elementText("classPath");
                String auth = child.elementText("auth");
                Map<String, String> interfaceMap = new HashMap<String, String>();
                interfaceMap.put("interfaceCode", interfaceCode);
                interfaceMap.put("beanName", beanName);
                interfaceMap.put("classPath", classPath);
                interfaceMap.put("auth", auth);
                map.put(interfaceCode, interfaceMap);
            }
            if(logger.isInfoEnabled()){
                logger.info("读取interfacebean配置文件结果：" + map);
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return map;
        }
    }
}

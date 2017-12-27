package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class MyXmlUtil {
	
	  /** 
     * xml转map 不带属性 
     * @param xmlStr 
     * @param needRootKey 是否需要在返回的map里加根节点键 
     * @return 
     * @throws DocumentException 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map xml2map(String xmlStr, boolean needRootKey) throws DocumentException {  
    	if (xmlStr == null) return null;
        Document doc = DocumentHelper.parseText(xmlStr);  
        Element root = doc.getRootElement();  
        Map<String, Object> map = (Map<String, Object>) xml2map(root);  
        if(root.elements().size()==0 && root.attributes().size()==0){  
            return map;  
        }  
        if(needRootKey){  
            //在返回的map里加根节点键（如果需要）  
            Map<String, Object> rootMap = new HashMap<String, Object>();  
            rootMap.put(root.getName(), map);  
            return rootMap;  
        }  
        return map;  
    }  
  
    /** 
     * xml转map 带属性 
     * @param xmlStr 
     * @param needRootKey 是否需要在返回的map里加根节点键 
     * @return 
     * @throws DocumentException 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map xml2mapWithAttr(String xmlStr, boolean needRootKey) throws DocumentException {  
        if (xmlStr == null) return null;
    	Document doc = DocumentHelper.parseText(xmlStr);  
        Element root = doc.getRootElement();  
        Map<String, Object> map = (Map<String, Object>) xml2mapWithAttr(root);  
        if(root.elements().size()==0 && root.attributes().size()==0){  
            return map; //根节点只有一个文本内容  
        }  
        if(needRootKey){  
            //在返回的map里加根节点键（如果需要）  
            Map<String, Object> rootMap = new HashMap<String, Object>();  
            rootMap.put(root.getName(), map);  
            return rootMap;  
        }  
        return map;  
    }  
  
    /** 
     * xml转map 不带属性 
     * @param e 
     * @return 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map xml2map(Element e) {  
        Map map = new LinkedHashMap();  
        List list = e.elements();  
        if (list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                Element iter = (Element) list.get(i);  
                List mapList = new ArrayList();  
  
                if (iter.elements().size() > 0) {  
                    Map m = xml2map(iter);  
                    if (map.get(iter.getName()) != null) {  
                        Object obj = map.get(iter.getName());  
                        if (!(obj instanceof List)) {  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(m);  
                        }  
                        if (obj instanceof List) {  
                            mapList = (List) obj;  
                            mapList.add(m);  
                        }  
                        map.put(iter.getName(), mapList);  
                    } else  
                        map.put(iter.getName(), m);  
                } else {  
                    if (map.get(iter.getName()) != null) {  
                        Object obj = map.get(iter.getName());  
                        if (!(obj instanceof List)) {  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(iter.getText());  
                        }  
                        if (obj instanceof List) {  
                            mapList = (List) obj;  
                            mapList.add(iter.getText());  
                        }  
                        map.put(iter.getName(), mapList);  
                    } else  
                        map.put(iter.getName(), iter.getText());  
                }  
            }  
        } else  
            map.put(e.getName(), e.getText());  
        return map;  
    }  
  
    /** 
     * xml转map 带属性 
     * @param e 
     * @return 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map xml2mapWithAttr(Element element) {  
        Map<String, Object> map = new LinkedHashMap<String, Object>();  
  
        List<Element> list = element.elements();  
        List<Attribute> listAttr0 = element.attributes(); // 当前节点的所有属性的list  
        for (Attribute attr : listAttr0) {  
            map.put("@" + attr.getName(), attr.getValue());  
        }  
        if (list.size() > 0) {  
  
            for (int i = 0; i < list.size(); i++) {  
                Element iter = list.get(i);  
                List mapList = new ArrayList();  
  
                if (iter.elements().size() > 0) {  
                    Map m = xml2mapWithAttr(iter);  
                    if (map.get(iter.getName()) != null) {  
                        Object obj = map.get(iter.getName());  
                        if (!(obj instanceof List)) {  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            mapList.add(m);  
                        }  
                        if (obj instanceof List) {  
                            mapList = (List) obj;  
                            mapList.add(m);  
                        }  
                        map.put(iter.getName(), mapList);  
                    } else  
                        map.put(iter.getName(), m);  
                } else {  
  
                    List<Attribute> listAttr = iter.attributes(); // 当前节点的所有属性的list  
                    Map<String, Object> attrMap = null;  
                    boolean hasAttributes = false;  
                    if (listAttr.size() > 0) {  
                        hasAttributes = true;  
                        attrMap = new LinkedHashMap<String, Object>();  
                        for (Attribute attr : listAttr) {  
                            attrMap.put("@" + attr.getName(), attr.getValue());  
                        }  
                    }  
  
                    if (map.get(iter.getName()) != null) {  
                        Object obj = map.get(iter.getName());  
                        if (!(obj instanceof List)) {  
                            mapList = new ArrayList();  
                            mapList.add(obj);  
                            // mapList.add(iter.getText());  
                            if (hasAttributes) {  
                                attrMap.put("#text", iter.getText());  
                                mapList.add(attrMap);  
                            } else {  
                                mapList.add(iter.getText());  
                            }  
                        }  
                        if (obj instanceof List) {  
                            mapList = (List) obj;  
                            // mapList.add(iter.getText());  
                            if (hasAttributes) {  
                                attrMap.put("#text", iter.getText());  
                                mapList.add(attrMap);  
                            } else {  
                                mapList.add(iter.getText());  
                            }  
                        }  
                        map.put(iter.getName(), mapList);  
                    } else {  
                        // map.put(iter.getName(), iter.getText());  
                        if (hasAttributes) {  
                            attrMap.put("#text", iter.getText());  
                            map.put(iter.getName(), attrMap);  
                        } else {  
                            map.put(iter.getName(), iter.getText());  
                        }  
                    }  
                }  
            }  
        } else {  
            // 根节点的  
            if (listAttr0.size() > 0) {  
                map.put("#text", element.getText());  
            } else {  
                map.put(element.getName(), element.getText());  
            }  
        }  
        return map;  
    }  
      
    /** 
     * map转xml map中没有根节点的键 
     * @param map 
     * @param rootName 
     * @throws DocumentException 
     * @throws IOException 
     */  
    public static Document map2xml(Map<String, Object> map, String rootName) throws DocumentException, IOException  {  
        Document doc = DocumentHelper.createDocument();  
        Element root = DocumentHelper.createElement(rootName);  
        doc.add(root);  
        map2xml(map, root);  
        //System.out.println(doc.asXML());  
        //System.out.println(formatXml(doc));  
        return doc;  
    }  
      
    /** 
     * map转xml map中含有根节点的键 
     * @param map 
     * @throws DocumentException 
     * @throws IOException 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Document map2xml(Map<String, Object> map) throws DocumentException, IOException  {  
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();  
        if(entries.hasNext()){ //获取第一个键创建根节点  
            Map.Entry<String, Object> entry = entries.next();  
            Document doc = DocumentHelper.createDocument();  
            Element root = DocumentHelper.createElement(entry.getKey());  
            doc.add(root);  
            map2xml((Map)entry.getValue(), root);  
            //System.out.println(doc.asXML());  
            //System.out.println(formatXml(doc));  
            return doc;  
        }  
        return null;  
    }  
      
    /** 
     * map转xml 
     * @param map 
     * @param body xml元素 
     * @return 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Element map2xml(Map<String, Object> map, Element body) {  
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();  
        while (entries.hasNext()) {  
            Map.Entry<String, Object> entry = entries.next();  
            String key = entry.getKey();  
            Object value = entry.getValue();  
            if(key.startsWith("@")){    //属性  
                body.addAttribute(key.substring(1, key.length()), value.toString());  
            } else if(key.equals("#text")){ //有属性时的文本  
                body.setText(value.toString());  
            } else {  
                if(value instanceof java.util.List ){  
                    List list = (List)value;  
                    Object obj;  
                    for(int i=0; i<list.size(); i++){  
                        obj = list.get(i);  
                        //list里是map或String，不会存在list里直接是list的，  
                        if(obj instanceof java.util.Map){  
                            Element subElement = body.addElement(key);  
                            map2xml((Map)list.get(i), subElement);  
                        } else {  
                            body.addElement(key).setText((String)list.get(i));  
                        }  
                    }  
                } else if(value instanceof java.util.Map ){  
                    Element subElement = body.addElement(key);  
                    map2xml((Map)value, subElement);  
                } else {  
                    body.addElement(key).setText(value.toString());  
                }  
            }  
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
        }  
        return body;  
    }  
      
    /** 
     * 格式化输出xml 
     * @param xmlStr 
     * @return 
     * @throws DocumentException 
     * @throws IOException 
     */  
    public static String formatXml(String xmlStr) throws DocumentException, IOException  {  
        Document document = DocumentHelper.parseText(xmlStr);  
        return formatXml(document);  
    }  
      
    /** 
     * 格式化输出xml 
     * @param document 
     * @return 
     * @throws DocumentException 
     * @throws IOException 
     */  
    public static String formatXml(Document document) throws DocumentException, IOException  {  
        // 格式化输出格式  
        OutputFormat format = OutputFormat.createPrettyPrint();  
        //format.setEncoding("UTF-8");  
        StringWriter writer = new StringWriter();  
        // 格式化输出流  
        XMLWriter xmlWriter = new XMLWriter(writer, format);  
        // 将document写入到输出流  
        xmlWriter.write(document);  
        xmlWriter.close();  
        return writer.toString();  
    }
    
    public static void main(String[] args) throws DocumentException, IOException {  
//        String textFromFile = FileUtils.readFileToString(new File("D:/workspace/workspace_3.7/xml2map/src/xml2json/sample.xml"),"UTF-8");  
//        Map<String, Object> map = xml2map(textFromFile, false);  
        // long begin = System.currentTimeMillis();  
        // for(int i=0; i<1000; i++){  
        // map = (Map<String, Object>) xml2mapWithAttr(doc.getRootElement());  
        // }  
        // System.out.println("耗时:"+(System.currentTimeMillis()-begin));  
//        JSON json = JSONObject.fromObject(map);  
//        System.out.println(json.toString(1)); // 格式化输出  
//        Document doc = map2xml(map, "root");  
        //Document doc = map2xml(map); //map中含有根节点的键  
//        System.out.println(formatXml(doc));  
    	
    	
    	String xml = "<CYJ><JBX><JBX.1>0000707445</JBX.1><JBX.7>已婚</JBX.7><JBX.16>河回族区仪北街6号楼</JBX.16></JBX><MZX><MZX.1>53069921</MZX.1></MZX><CYJ.1>0000603137</CYJ.1><CYJ.12>104012</CYJ.12><CYJ.13>繆东</CYJ.13><CYJ.14>2014-02-15 09:50:48</CYJ.14><CYJ.15>2014-02-15 09:50:48</CYJ.15><CYJ.35>1</CYJ.35><CYJ.37>2014-02-15 09:45:23</CYJ.37><CYJ.40>2014-02-15 09:45:23</CYJ.40><ZYX><ZYX.1>53069921</ZYX.1><ZYX.7>赵兰秋</ZYX.7><ZYX.8>女</ZYX.8><ZYX.19>退休</ZYX.19><ZYX.42>2014-02-11 09:08:04</ZYX.42></ZYX><NUL><NUL.6 Serch=\"\"/><NUL.0 Serch=\"住院后的治疗及检查情况\">患者此次入院经查患者右上臂部位为肿大的结节，考虑为弥漫大B淋巴瘤进展阶段，患者体表面积 1.54，患者于2014年2月15日开始行GDP化疗方案，吉西他滨的D1。8 1.5（7*0.2），顺铂38毫克*3，地塞米松40毫*4天。患者化疗后出现骨髓抑制，给予B型RHD阳性成分血，患者无恶心及呕吐，一般状况稳定，患者好转出院</NUL.0><NUL.0 Serch=\"出院时情况及医嘱\">监测血常规及肝功，加强个人防护，1周复诊</NUL.0><NUL.7 Serch=\"空元素(整数)\">28</NUL.7><NUL.0 Serch=\"入院时情况\">淋巴结肿大1年余，左前臂肿物1周为主诉，1年前出现全身浅表淋巴结无痛性肿大，双侧颈部淋巴结肿大(左侧明显)，约1cm×0.5cm大小，体温37-37.5°，伴乏力消瘦。未正规检查即治疗，自服抗生素服用散利痛和螺旋霉素口服。乏力症状进行性加重，体温达到38°以上,为弛张热，伴全身疼痛，双侧颈部及锁骨上窝可以触及多发2cm×1cm大小质韧淋巴结，触痛。2012年10月前在我院淋巴结活检提示为弥漫大B淋巴瘤CD20+mum+CD23-,CD10-CD3-cd5-,KI-67+60%，弥漫大B淋巴瘤Ⅲ期SE,IPI高危4分患者行CHOP方案化疗6周期，骨髓抑制Ⅳ度，胃肠反应Ⅱ度，重度骨髓抑制时曾给予成分血输入。2013年2月-5月因亲人病故等原因未正规随诊，2013年5月13日，患者后背部肩胛下角线出现无痛性肿物3*4厘米，经活检提示“非霍奇金淋巴瘤DLBCL复发”经2周期的CTOP方案后评估无浅表及深部淋巴结肿大，末次化疗2013年9月9日-9月12日开始给予DICE化疗方案，异环磷酰胺15毫克*4天，顺铂30mg*3天，VP16100mg*4天，地塞米松20毫克*4天，患者化疗后出现Ⅳ度骨髓抑制，此次以弥漫大B淋巴瘤Ⅲ期SE高危组进一步住院治疗。1周前，患者发现左前臂出现肿物。近1月来，患者无纳差及乏力，饮食睡眠为正常。</NUL.0><NUL.4 Serch=\"\">2014-03-11 09:45:24</NUL.4></NUL><CYJ.50>1.弥漫大B淋巴瘤Ⅲ期SE,IPI高危4分2.浅表性胃炎</CYJ.50><CYJ.54>1.弥漫大B淋巴瘤Ⅲ期SE,IPI高危4分2.浅表性胃炎</CYJ.54><CYJ.33/><CYJ.28/><NUL Serch=\"空元素\">61岁</NUL></CYJ>";
    	Map map = xml2mapWithAttr(xml, false);
    	System.out.println(map);
    }  
  
}  
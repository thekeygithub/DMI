package com.dhcc.common.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class XmlUtil {
	private static List<String> listType = new ArrayList<String>();
	private static List<String> listNUL = new ArrayList<String>();
	private static List<String> listNULType = new ArrayList<String>();
	static{
		//入院记录
		//TGJ体格检查，GRS个人史，JWS既往史，JZS家族史，ZSX主诉，XBS现病史，<NUL.0 Serch="辅助检查">
		listType.add("TGJ");
		listType.add("GRS");
		listType.add("JWS");
		listType.add("JZS");
		listType.add("ZSX");
		listType.add("XBS");
		//出院记录
		//<NUL.0 Serch="住院后的治疗及检查情况">，<NUL.0 Serch="出院时情况及医嘱">，<NUL.0 Serch="入院时情况">
		//手术记录
		//<NUL.0 Serch="术前准备">，<NUL.0 Serch="病历报告">，<NUL.0 Serch="手术指征">，<NUL.0 Serch="手术方案">
		//<NUL.0 Serch="意外及防范措施">，<NUL.0 Serch="讨论意见">，<NUL.0 Serch="总结意见">，<NUL.0 Serch="标本检查及送检物">
		//<NUL.0 Serch="手术前诊断">，<NUL.0 Serch="手术后诊断">，<NUL.0 Serch="麻醉式">，<NUL.0 Serch="手术所见">，<NUL.0 Serch="手术经过">
		listNUL.add("NUL.0");
		
		
		listNULType.add("住院后的治疗及检查情况");
		listNULType.add("出院时情况及医嘱");
		listNULType.add("入院时情况");
		listNULType.add("术前准备");
		listNULType.add("病历报告");
		listNULType.add("手术指征");
		listNULType.add("手术方案");
		listNULType.add("意外及防范措施");
		listNULType.add("讨论意见");
		listNULType.add("总结意见");
		listNULType.add("标本检查及送检物");
		listNULType.add("手术前诊断");
		listNULType.add("手术后诊断");
		listNULType.add("麻醉式");
		listNULType.add("手术所见");
		listNULType.add("联系人电话");
	}
	
	
	public static Map<String ,Object> readerXML(String xml){
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
//			SAXReader saxreader = new SAXReader();
//			Document document = saxreader.read(new File(xml));
			
			Document document = DocumentHelper.parseText(xml); 
            Element bookstore = document.getRootElement();  
            Iterator storeit = bookstore.elementIterator(); 
			
			while(storeit.hasNext()){
				Element bookElement = (Element) storeit.next();
				doSomething(map, bookElement);
//				
//				List<Attribute> attributes = bookElement.attributes();
//				for(Attribute attribute : attributes){  
//					System.out.println(attribute.getName()+"-----111111111");
//				}
				
				Iterator bookit = bookElement.elementIterator();  
	            while(bookit.hasNext()){
	            	Element child = (Element) bookit.next();  
                    String nodeName = child.getName();
                   // System.out.println(nodeName);
                    doSomething(map, child);
	            }
			}
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return map;
	}
	
	private static void doSomething(Map<String ,Object> map,Element element){
		if(element.attributeValue("Serch")==null){
			map.put(element.getName().replace(".", "_"), element.getData());
		}else if("".equals(element.attributeValue("Serch"))){
			List<String> list=null;
			list=(List<String>) map.get("");
			if(list==null){
				list=new ArrayList<String>();   
			}
			list.add(element.getData().toString());
			map.put("",list);
		}else{
			map.put(element.attributeValue("Serch").replace(".", "_"), element.getData());
			
		}
		/*if(listType.contains(element.getName())){
//			System.out.println(element.getStringValue()+"==========");
			map.put(element.getName(), element.getStringValue());
		}else if(listNUL.contains(element.getName())){
//			System.out.println(element.attributeValue("Serch")+"[[[[[[[[[["+element.getStringValue());
			if(listNULType.contains(element.attributeValue("Serch"))){
				map.put(element.attributeValue("Serch"), element.getStringValue());
				
			}
		}*/
	}
	
	

	public static void main(String[] args) {	
		
		String str="<RYJ><JBX><JBX.1>0000728530</JBX.1><JBX.17>-</JBX.17><JBX.7>已婚</JBX.7><JBX.5><JBX.51>河南</JBX.51><JBX.53/><JBX.52>开封</JBX.52></JBX.5><JBX.15>410204194702192010</JBX.15><JBX.8>汉族</JBX.8><JBX.16>鼓楼区勤农封16号附2号</JBX.16><JBX.23>卢雯</JBX.23><JBX.22>-</JBX.22></JBX><ZYX><ZYX.1>53070961</ZYX.1><ZYX.7>卢世臣</ZYX.7><ZYX.28>15</ZYX.28><ZYX.1>53070961</ZYX.1><ZYX.8>男</ZYX.8><ZYX.8>男</ZYX.8><ZYX.1>53070961</ZYX.1><ZYX.42>2014-03-08 08:45:08</ZYX.42><ZYX.19>退休</ZYX.19><ZYX.7>卢世臣</ZYX.7><ZYX.17>内一西病区</ZYX.17><ZYX.28>15</ZYX.28></ZYX><RYJ.1>0000172756</RYJ.1><RYJ.2>2014-03-08 22:02:14</RYJ.2><RYJ.16>2014-03-08 22:02:14</RYJ.16><RYJ.17>101004</RYJ.17><RYJ.18>常娜</RYJ.18><RYJ.19>1</RYJ.19><RYJ.20>2014-03-08 21:34:52</RYJ.20><RYJ.23>2014-03-08 21:34:52</RYJ.23><NUL Serch=\"空元素\">神经内<NUL.0 Serch=\"\">1</NUL.0><NUL.0 Serch=\"\">一般</NUL.0><NUL.0 Serch=\"联系人电话\">13781190281</NUL.0><NUL.0 Serch=\"\">1.短暂性脑缺血发作"
				+"2.2型糖尿病？"
				+"3.高同型半胱氨酸血症</NUL.0><NUL.0 Serch=\"空元素(字符串)\"/><NUL.6 Serch=\"\"/><NUL.0 Serch=\"婚姻史\">婚育史：29岁结婚，配偶体健，夫妻关系和睦。育有1女，体健。</NUL.0><NUL.0 Serch=\"辅助检查\">头颅MRI及MRA（我院 2014.03.06）示：右侧大脑后动脉狭窄。</NUL.0><NUL.0 Serch=\"联系人地址\">同上</NUL.0><NUL.0 Serch=\"\">住院医师：周淑芳"
				+"副主任医师：常娜</NUL.0></NUL><TGJ>T：36.5℃        P：80次/分      R：18次/分     BP:140/80 mmHg"
				+"发育正常，营养好，体型中等。神志清，精神欠佳。步入病房，自主体位，查体合作。全身皮肤粘膜无黄染及出血点，浅表淋巴结未触及肿大，无肝掌及蜘蛛痣。头颅无畸形，口腔、鼻腔、外耳道无血性、脓性等异常分泌物。眼睑无水肿，结膜无充血，巩膜无黄染。口唇无发绀，咽腔无充血。颈软，无抵抗，气管居中，双侧颈动脉搏动对称，双侧颈静脉无怒张，听诊两侧颈动脉未闻及杂音。甲状腺无肿大。胸廓无畸形，双侧呼吸运动度对称，叩诊清音，听诊两肺呼吸音清，未闻及干湿啰音。心前区无隆起，触诊无震颤，无异常心尖搏动。叩诊心界无明显扩大，心率80次/分钟，律齐，心脏各瓣膜听诊区未闻及病理性杂音。腹平软，无压痛、反跳痛，肝脾肋下未触及, Murphy征（-），双肾区无压痛、叩击痛，移动性浊音（-）。听诊肠鸣音约3～5次/分。双下肢无水肿。四肢、脊柱、大关节未见畸形，双侧桡、足背动脉搏动正常对称。肛门、外生殖器未查。神经系统检查详见专科检查。"

				                       +"   神经系统检查"
				+"1.神志、精神状态：神志清，精神一般，无失语及构音障碍，粗测定向力、记忆力、计算力无明显异常。"
				+"2.颅神经"
				+"①嗅神经：粗测嗅觉正常。"
				+"②视神经：粗测近视力可，远视力未测。粗侧视野无缺损。眼底未查。瞬目反射存在。"
				+"③动眼神经、滑车神经、展神经：眼裂对称，无上睑下垂，眼球向各方向运动正常，无复视，眼震，双侧瞳孔等大等圆，直径约3.0mm，直接、间接光反应均存在，调节反射好。"
				+"④三叉神经：两侧面部痛觉对称存在；颞颊部无肌肉萎缩，颞肌、咬肌肌力好，张口下颌无偏斜，角膜反射存在，下颌反射正常。"
				+"⑤面神经：两侧面部对称，无面肌痉挛，双侧鼻唇沟对称，示齿充分，口角无歪斜，额纹两侧对称，双眼睑闭合好。"
			+"	⑥位听神经：粗测两侧听力正常。"
			+"	⑦舌咽神经、迷走神经：声音无嘶哑，饮水无呛咳，双侧软腭上提可，悬雍垂居中，咽反射存在。"
				+"⑧副神经、舌下神经：转头及耸肩运动正常,胸锁乳突肌无萎缩，伸舌居中，舌肌无萎缩、震颤。"
				+"3.运动系统：躯干及四肢肌肉无萎缩，无不自主运动。四肢肌张力正常。双侧肢体轻瘫试验阴性，左侧上下肢肌力5级，右侧肢体肌力5级。臀部躯干联合屈曲等未查。"
				+"4.感觉系统：双侧浅感觉基本正常，关节觉、位置觉，音叉震动觉正常。深部压痛感正常。复合感觉：双侧图形觉、实体觉、皮肤定位觉、两点辨别觉正常。"
				+"5.反射：两侧肱二头肌腱、肱三头肌腱、挠骨膜反射、膝腱、踝腱反射（++）。两侧腹壁反射、跖反射均存在。肛门反射未查。病理反射：右侧Babinski征（-），左侧（-）。双侧Chaddock征（-）；双侧Oppenheim征（-）；双侧Gordon征（-） 。"
				+"6.脑膜刺激征：颈软，Kernig征（-），Brudzinski征（-）。"
				+"7.植物神经系统：皮肤色泽好，无汗液分泌障碍，双下肢皮温基本相同，无膀胱括约肌功能障碍。</TGJ><GRS>个人史：生长于原籍，无外地长期居住史，无工业毒物、放射性物质、粉尘等接触史，未到过疟疾、血吸虫、肺吸虫等疾病流行区。否认吸烟嗜酒等不良嗜好。无冶游史。</GRS><JWS>既往史：去年6月行“胆囊息肉切除术”；否认“高血压病、2型糖尿病、冠心病”等慢性病史；否认“慢性支气管炎、肺气肿病”等慢性病史，否认“肝炎”“结核”等慢性传染病史；否认输血、献血史；否认食物、药物过敏史；无手术、外伤史；预防接种随当地进行。"
				+"</JWS><JZS>家族史：父母去世，死因不详，有一姐，体健；否认家族性遗传疾病史及传染病史。</JZS><ZSX>主  诉：头晕、发作性右眼视物模糊1月余</ZSX><RYJ.4>父女</RYJ.4><RYJ.3>患者本人</RYJ.3><XBS>现病史:1月前患者无明显诱因出现头晕，呈持续性，伴恶心，伴右侧眼前视物模糊，视物模糊持续3-5分钟自行缓解，不伴头痛、呕吐，不伴视物旋转、复视、视力下降；不伴耳鸣、听力下降；不伴心慌、心悸、心前区不适；不伴肢体抽搐、行走不稳；不伴意识丧失、言语不清；不伴饮水呛咳、吞咽困难。未加治疗，休息后头晕症状稍缓解，半月前再次出现上述症状，视物模糊加重，今日为进一步诊治来我院就诊，查头颅MRI及MRA示右侧大脑后动脉狭窄；为求进一步诊治来我院，门诊以“头晕待查”收入我科，发病来，患者神志清，精神一般，饮食一般，睡眠、大小便正常，体重无明显变化。</XBS></RYJ>";
				Map<String ,Object> map = readerXML(str);
		 
		 for (Map.Entry<String, Object> entry : map.entrySet()) {
			 System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		 }
		
	}
}

package com.ts.controller.mts.matchrule;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ts.service.mts.MtsConfig;


@Controller
@RequestMapping(value="/config")
public class ConfigController {
	
	@Resource(name="MtsConfig")
	private MtsConfig mc;

	
	
	@RequestMapping(value="/matchL")
	public void matchL() throws Exception{
		
		
		//智能问答测试
	   	String str="病程记录:患者入院第2天，患者一般情况尚可，双手近端指间关节、腕关节、踝关节疼痛不适，晨僵明显，活动后减轻。纳眠可，二便调。舌质红，苔薄白，脉弦细。 张健君副主任医师查看病人后意见：患者主因“确诊类风湿性关节炎6年”入院。入院后已行血常规、肝肾功等相关检查，评估病情，指导治疗。暂给予慢作用药物、活血化瘀药物等对症治疗；中医以驱寒除湿、活血通络止痛为治则辨证治疗，方可选羌活胜湿汤加减。待检查回示后，讨论下步治疗。";
	    String result2=mc.ZnwdNlp(str,"410202");	    
		System.out.println("result2"+result2);
		List<String> list=new ArrayList<String>();
//		list.add("慢乙肝恢复期");
		list.add("右肺腺癌伴全身多处骨转移放化疗后脑转移");
//		list.add("普通二维超声心动图");
//		list.add("眼泪小点");
//		list.add("青霉素，便血");
//		list.add("异位妊娠治");
//		list.add("异位妊娠治疗后随诊检查");
//		for(int i=0;i<list.size();i++){
//			String json5= "{\"dataType\":\"02\"}";	   
//			String json6= "{\"data\":\"05@#&"+list.get(i)+"\"}";		
//		    String result1=mc.matchPRKS(json5, json6,"410204");	    
//			System.out.println("result2"+result1);
//		}
		//药品
//	    String json33= "{\"dataType\":\"06\"}";	   
//		String json44= "{\"data\":\"01@#&(TT)复方苦参水杨酸散 （足光散）@|&02@#&散剂 \"}";		
//	    String result2=mc.match(json33, json44,"410200");	    
//		System.out.println("result2"+result2);
		
		//需要切词的诊断
//	    String json5= "{\"dataType\":\"02\"}";	   
//		String json6= "{\"data\":\"05@#&糖尿病啊呀\"}";		
//	    String result1=mc.inerver(json5, json6,"410200");	    
//		System.out.println("result2"+result1);
//		
		
		//不需要切词的诊断
		//String json5= "{\"dataType\":\"02\"}";
		//String json6= "{\"data\":\"05@#&经皮交通动脉血管成形术\"}";		
		//String result3=mc.match(json5, json6,"410200");
		//System.out.println("result3"+result3);	
		//{"result":"209330","status":"1"}
		
		//药品匹配
		// String json1= "{\"dataType\":\"06\"}";
		 //String json2= "{\"data\":\"01@#&二磷酸果糖@|&02@#&针剂\"}";
		// String result=mc.match(json1, json2,"410200");
		// System.out.println("result"+result);
		 
		//药品匹配
		 //String json3= "{\"dataType\":\"01\"}";
		 //String json4= "{\"data\":\"01@#&补中益气丸（浓缩丸）\"}";
		 //String result3=mc.match(json3, json4,"410202");
		 //System.out.println("result3"+result3);
		 
		 
		//药品匹配
//		 String json5= "{\"dataType\":\"07\"}";
//		 String json6= "{\"data\":\"19@#&硝酸异山梨酯注射液@|&21@#&硝酸异山梨酯注射液@|&20@#&@|&21@#&@|&23@#&注射液@|&03@#&5ml:5mg@|&17@#&齐鲁制药有限公司\"}";
//		 String result2=mc.match(json5, json6,"410203");
//		 System.out.println("result2"+result2);
//		 String json7= "{\"data\":\"19@#&复方板蓝根颗粒 (特格尔）@|&21@#&复方板蓝根颗粒 (特格尔）@|&20@#&复方板蓝根颗粒 (特格尔）@|&23@#&颗粒剂@|&03@#&15g@|&16@#&21袋@|&18@#&空@|&17@#&四川禾邦阳光制药股份有限公司\"}";
//		 String result7=mc.match(json5, json7,"410200");
//		 System.out.println("result7"+result7);
		 
		//药品匹配
		 String json5= "{\"dataType\":\"07\"}";
		 String json6= "{\"data\":\"34@#&复方硫酸新霉素滴眼液（五景）@|&34@#&复方硫酸新霉素滴眼液（五景）@|&34@#&null@|&23@#&滴眼液@|&03@#&6ml@|&16@#&null@|&18@#&空@|&17@#&武汉五景药业有限公司\"}";
		 String result22=mc.match(json5, json6,"410200");
		 System.out.println("result22"+result22);
		//
		//特殊处理流程
		//String json1= "{\"dataType\":\"02\"}";
		//String json2= "{\"data\":\"05@#&咯血，急性发作\"}";
		//String result=mc.special(json1, json2,"410200");
		//System.out.println("result"+result);
		
		//慢性病
		//String json7= "{\"dataType\":\"02\"}";
		//String json8= "{\"data\":\"11#肾衰竭\"}";
		//String result4=mc.matchNlp(json7, json8);
		//System.out.println("result4"+result4);
		//{"result":"251919","status":"1"}
		
		//慢性病
		//String json9= "{\"dataType\":\"02\"}";
		//String json0= "{\"data\":\"11#糖并眼|11#糖并肾|11#糖并脑\"}";
		//String result5=mc.matchNlp(json9, json0);
		//System.out.println("result4"+result5);
		
		//String ss="(K52.903)胃肠炎,(E11.901)2型糖尿病";
		//String sa="1,(I60,301)后交通动脉瘤破裂,2,(J98,402)肺部感染";
		//System.out.println(mc.standarde(ss));
		//System.out.println(mc.standarde(sa));
	}

}

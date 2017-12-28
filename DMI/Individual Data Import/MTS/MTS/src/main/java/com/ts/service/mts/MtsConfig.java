package com.ts.service.mts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ts.bean.HttpResult;
import com.ts.entity.mts.OInterver;
import com.ts.entity.mts.OTerm;
import com.ts.service.httpClient.HttpClientService;
import com.ts.service.mts.matchrule.impl.DataOInverter;
import com.ts.service.mts.matchrule.impl.DataOTerm;
import com.ts.service.mts.matchrule.impl.DataPrksTs;
import com.ts.util.PageData;
import com.ts.util.StringUtil;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

import net.sf.json.JSONObject;


@Service("MtsConfig")
public class MtsConfig {
	
	@Resource(name = "redisUtil")
	private IRedisUtil redisDao;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mca;	
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;	
	@Resource(name = "httpClientService")
	private HttpClientService httpClientService;
	
	private static Logger logger = Logger.getLogger(MtsConfig.class);	
	private  String termsStrs = "";  //无用术语串
	private  String doubtsStrs = ""; //怀疑诊断串
	private  String specialStrs= ""; //诊断限定词串
	private  String inverStrs= ""; //无法干预词串
	private  String jpStrs = ""; //解剖部位串
	private  String opStrs= ""; //术后词串
	private  String zlStrs= ""; //肿瘤转移词串
	private  String	arr="";//字符集
	private  Map<String, String> matchRuleCacheMap ;// 匹配规则缓存容器	
	private String nlpBS="0";
	
	String nlpUrl =""; 

	

	
	/**
	 * 从radis中匹配数据
	 * @param bhlx:标化类型
	 * @param css ：聚类
	 * @param inkey：待匹配数据
	 * @return json格式的字符串
	 * @throws Exception
	 */
	public String config(String bhlx,String css,String inkey,String areaid) {
		
		JSONObject json = new JSONObject();
		
		String reval="";
		
		//1.根据标化类型，查询返回值名	
			 
		
		String bhType=matchRuleCacheMap.get("DATACODE"+bhlx);
		String valueGen =matchRuleCacheMap.get("VALUE"+areaid+bhType);
		
		if(bhType==null||valueGen==null){
			System.out.println("根据标化类型，查询返回值时为空");			
			json.put("status", "0");
			reval=json.toString();
			return reval;
		}
		 
		//2.将入参拼接成key		
		String mdkey="";
		String[] inkeys=inkey.split("@#&");
		for(int j=0;j<inkeys.length;j++){
			try {
				if(mdkey==""){				
					
					mdkey =mca.trimUnicode(StringUtil.full2Half(inkeys[j]).toUpperCase().trim()) ;
				
				}else{
					
					mdkey =mdkey+"#"+mca.trimUnicode(StringUtil.full2Half(inkeys[j]).toUpperCase().trim());
				}
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		
		
		 String key=areaid+"#"+css+"#"+bhlx+"#"+mdkey;		
		 
		
		 		 
		 
		 
		//3.从redis中获取结果	
		 String value=redisDao.get(RedisKeys.SYSMTS, key);
		 
		 
		
			
		if(value==null){
			json.put("status", "0");
			json.put(valueGen, "none");
		}else{ //4.保存成json格式	
			
			json.put(valueGen, value);
			json.put("status", "1");
		}	
		
		 reval=json.toString();
		
		json=null;
		return reval;		
		
		
	}




	
	

	
	/**
	 * 分类匹配（带循环）
	 * @param dataClass ：聚类  "02"
	 * @param str ：数据串     "01#阿司匹林|02#制药六厂|03#片|……"
	 * @return {"status"："1","jsonarr":"药品属性详细结果","返回值名":"返回值"}
	 * @throws Exception
	 */
	
	public String match(String dataClass,String str,String areaid) {
		
		JSONObject jsonarr = new JSONObject();
		JSONObject jsondrug = new JSONObject();
		JSONObject jsonnlp = new JSONObject();
		
		JSONObject jsontemp=new JSONObject();		
		String bhlx="";                               //标化类型
		String inkey="";                              //标化后的术语		
		String valg="none";                           //返回匹配内容
		String stt="0";                               //成功失败标识
		String value="";
		 
		//获取json中的数据
		if(this.nlpUrl==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
		} 		
		JSONObject dc=JSONObject.fromObject(dataClass);
		JSONObject st=JSONObject.fromObject(str);
		String dataType =(String) dc.get("dataType");
		String data =(String) st.get("data");
		
		
		//对data中的数据进行分词
		String[] datas=data.split("@\\|&");	
		String ifnext="1";
		Set<String>  drugSet=new HashSet<String>();
		
			for(int j=0;j<datas.length;j++){
				String[] instr=datas[j].split("@#&");				
				if(instr.length==1){
					jsontemp.put("result", "none");		
					jsontemp.put("status", 0);
					value=jsontemp.toString();
					
					jsonarr =null;
					jsondrug =null;
					jsontemp=null;
					jsonnlp=null;					
					return value;
				}
				bhlx=instr[0];
				inkey=instr[1];
				
				String bT =matchRuleCacheMap.get("DATACODE"+bhlx);
				String val =matchRuleCacheMap.get("VALUE"+areaid+bT);
				String stand=matchRuleCacheMap.get("STAND"+areaid+bT);
				String nlpbs=matchRuleCacheMap.get("NLP"+areaid+bT);
				
				if(bT==null||val==null||stand==null){
					
//					System.out.println("获取DATACODE、VALUE、STAND时为空");
					jsontemp.put("result", "none");		
					jsontemp.put("status", 0);
					value=jsontemp.toString();
					return value;
				}
				if(stand == "1"|| "1".equals(stand)&& nlpBS=="0"){
					//inkey=mca.standarde(inkey);
				}
				
				
				if(inkey==null||inkey==""||"".equals(inkey)){
					jsontemp.put("result", valg);		
					jsontemp.put("status", stt);
					value=jsontemp.toString();
					
					jsonarr =null;
					jsondrug =null;
					jsontemp=null;
					jsonnlp=null;					
					return value;
				}
				String jvalue=config(bhlx,dataType,inkey,areaid);
				JSONObject json=JSONObject.fromObject(jvalue);
				
				//药品内部循环带NLP切词--begin--加
				List<String> drug=new ArrayList<String>();
				
				String jsonstatus=(String) json.get("status");
				if("0".equals(jsonstatus)&&"1".equals(nlpbs)&&!"none".equals(inkey)){
					
					String rs=this.nlpCtrl(nlpUrl, instr[1]);					
					JSONObject jsout=JSONObject.fromObject(rs);				
					@SuppressWarnings("unchecked")
					List<String> terms=(List<String>) jsout.get("terms");
					@SuppressWarnings("unchecked")
					List<String> nlptype=(List<String>) jsout.get("nlptype");
					jsonnlp.put(instr[1], nlptype);					
					
					JSONObject jsls=new JSONObject();										
						
					for(int g=0;g<terms.size();g++){
						
						String dvalue=config(bhlx,dataType,terms.get(g),areaid);
						
						JSONObject dson=JSONObject.fromObject(dvalue);
						String dstatus=(String) dson.get("status");
						if("1".equals(dstatus)){
							drug.add((String)dson.get(val));
							jsls.put(terms.get(g), (String)dson.get(val));
						}else{
							jsls.put(terms.get(g), "none");
						}
					}
					
					
					
					//NLP抽词后，药品名返回多个按drugName规则取，其他类型如果返回多个视为匹配失败none
					
					if(drug.size()>0){
						
						//jsondrug.put(instr[1], jsls.toString());						
						String nameand="";
						if(drug.size()>1&"01".equals(bhlx)){
							nameand=mca.drugName(drug);	
							if(nameand==""||"".equals(nameand)){
								
								nameand="none";
								stt="0";
							}else{
								stt="1";
							}
							
						}else if(drug.size()==1&&!"34".equals(bhlx)){
							nameand=drug.get(0);	
							stt="1";
						}else if(drug.size()>1&&!"01".equals(bhlx)&&!"34".equals(bhlx)){//nlp返回多个匹配成功，但不是药品通用名类，则视为错误数据无法匹配返回none
							nameand="none";
							stt="0";
						}
						
							
						if("34".equals(bhlx)){
							
							//将drug药品名按类型赋值到json中
							for(int a=0;a<drug.size();a++){
								String[] kvs=drug.get(a).split("@#&");
								String k=kvs[2];
								String v=kvs[0];
								String jv=(String) json.get(k);
								if(jv!=v){
									json.put(k, v);
								}else{
									drugSet.add(k);
									json.put(k, v);
								}
							}
							valg=drug.toString();
						}else{	
							valg=nameand;
							json.put(val,nameand);
						}
						
					}else{						
						valg="none";
						stt="0";
						json.put(val,"none");
					}
					
					
						
					
					jsondrug.put(instr[1], jsls.toString());				
					json.remove("status");
					if("34".equals(bhlx)){						
						//2、判断jsonarr中与json中key相同，value不同，则存入set集合，循环执行完毕后将其删除。
						Iterator itarr = jsonarr.keys();  				
						while(itarr.hasNext()){  
							String ky = (String) itarr.next();
							String vr=(String) jsonarr.get(ky);
							String va=(String) json.get(ky);
							if(va!=null&&vr!=va){
								drugSet.add(ky);
							}
						 }  
					}
					
					//药品内部循环带NLP切词--end--加
					
				}else{
					JSONObject jsonnlpValue = new JSONObject();
					jsonnlpValue.put(instr[1], json.get(val));
					//将每个词和匹配结果保存到jsonarr中返回						
					jsondrug.put(instr[1], jsonnlpValue.toString());
							
					valg=(String) json.get(val);   //每一次匹配后都赋值给返回串
					stt=(String) json.get("status");
					json.remove("status");
					
					if("34".equals(bhlx)){
						
						//1、将drug药品名按类型重新赋值到json中，删除原有的
						String[] drugvl=valg.split("@#&");
						json.put(drugvl[2], drugvl[0]);
						json.remove(val);
						//2、判断jsonarr中与json中key相同，value不同，则存入set集合，循环执行完毕后将其删除。
						Iterator itarr = jsonarr.keys();  				
						while(itarr.hasNext()){  
							String ky = (String) itarr.next();
							String vr=(String) jsonarr.get(ky);
							String va=(String) json.get(ky);
							if(va!=null&&vr!=va){
								drugSet.add(ky);
							}
						 }  
					}else{
						//诊断概念转换，keys相同则加上序号进行区别
						Iterator itarr = jsonarr.keys();  				
						while(itarr.hasNext()){  
							String ky = (String) itarr.next();
							String va=(String) json.get(ky);
							if(va!=null){
								json.put(ky+j, va);
								json.remove(ky);
							}
						 }  
					}
										
					jsonnlpValue=null;
				}
				//药品制剂的匹配规则中，药品类型为中药或饮片，或药品标准名为none的，不进入联合匹配
				
				if("01".equals(bhlx)){					
					String vv=(String) json.get(val);
					if(vv=="none"||"none".equals(vv)){//药品通用名为none，则不进入联合匹配
						ifnext="0";						
					}else if(vv.split("@#&").length>1){//药品通用名的中西分类为非西药，则不进入联合匹配，并且返回相应标准通用名
						String zx=mca.ifZX(vv);
						if("0".equals(zx)){
							ifnext="0";
							valg=vv.split("@#&")[2];
							stt="1";
						}else{
							json.put(val.split("@#&")[2], vv.split("@#&")[2]);//药品进入联合匹配，参数列表中只需要标准通用名
							json.remove(val);
						}
					}
					
				}
				jsonarr.putAll(json);
				if("0".equals(ifnext)&&!"none".equals(valg)&&"01".equals(bhlx)){//非西药类已返回标准通用名，则退出循环，返回结果
					break;
				}else{                                       
					continue;
				}
				
			}
			
			jsontemp.put("jsonarr", jsondrug.toString());
			jsontemp.put("NLPDrug", jsonnlp.toString());
			
			//drug药品名，同一类型不同值的要删除
			for(String vlue : drugSet){  	           
	            jsonarr.remove(vlue);
	        }
			
			String bhlxx=matchRuleCacheMap.get("DETAIL"+areaid+dataType+bhlx); 
			String pinkey=matchRuleCacheMap.get("DETAILPIN"+areaid+dataType+bhlx); 
			String memid=matchRuleCacheMap.get("MEMID"+areaid+dataType+bhlx); 
			
			//内部循环判断入口
			if(bhlxx!=""&&bhlxx!=null&&"1".equals(ifnext)){
		
					
					
					Map<String,String> map=nLMatch(areaid,dataType,bhlxx,jsonarr.toString(),pinkey,inkey,memid);
					String status=map.get("stt");
					if(status=="1"||"1".equals(status)){						
						valg=map.get("valg");
						stt=map.get("stt");
						
						
					}else if("1".equals(pinkey)&&"1".equals(stt)) {//药品类联合匹配失败，返回none；诊断大概念匹配失败，则保留上一级别的返回值
						
						valg="none";
						stt="0";
						
					}
					
					
			}else{
				
            	Iterator e_iterator = jsonarr.keys();
    	        while(e_iterator.hasNext()){
    	        	String e = (String) e_iterator.next();
    	        	String vale =jsonarr.getString(e) ;
    	        	if("none".equals(vale)){
    	        		valg="none";
    	        		stt="0";
    	        		break;
    	        	}
				
    	        }
			}
			
	
		jsontemp.put("result", valg);		
		jsontemp.put("status", stt);		
		value=jsontemp.toString();
		
		jsonarr =null;
		jsondrug =null;
		jsontemp=null;
		jsonnlp=null;
		
		System.out.println("==value=="+value);
		return value;
		
	}
	
	/***
	 * DETAIL内部流程
	 * @param areaid  区域
	 * @param classcode  聚类
	 * @param bhlxx   新标化类型
	 * @param jsonarr  新key串
	 * @param jsonarr  拼key标识
	 * @param jsonarr  原始key
	 * @param jsonarr  流程ID
	 * @return
	 */
	public Map<String,String> nLMatch(String areaid,String classcode,String bhlxx,String jsonarr,String pinkey,String inkey,String memid){

		String bhType=matchRuleCacheMap.get("DATACODE"+bhlxx);			
		String keyGen=matchRuleCacheMap.get("KEY"+areaid+bhType);
		String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);
		Map<String,String> value=new HashMap<String,String>();
		
		if(bhType==null||keyGen==null||valueGen==null){
			
			System.out.println("获取DATACODE、VALUE、KEY时为空");
			value.put("valg", "none")	;
			value.put("stt", "0")	;
			return value;
		}
		
		String inkeyLI="";
		String[] keys=keyGen.split("#");
		JSONObject jtem=JSONObject.fromObject(jsonarr);
		String valg="none";                          
		String stt="0"; 
		
			
		
		if(pinkey=="0"||"0".equals(pinkey)){
			
			inkeyLI=inkey;
			String outJson=config(bhlxx,classcode,inkeyLI,areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);			
			
			String sttpin=(String) jtempin.get("status");	
			if(sttpin=="1"||"1".equals(sttpin)){
				
				valg=(String) jtempin.get(valueGen);
				stt=sttpin;
			}
			value.put("valg", valg)	;
			value.put("stt", stt)	;
			
		}else{
				for(int d=3;d<keys.length;d++){
					String vals=(String) jtem.get(keys[d]);
					if(vals=="none"||"none".equals(vals)){
						inkeyLI="";
						break;
					}
					if(vals!=null){
						if(inkeyLI==""){
							inkeyLI=(String) jtem.get(keys[d]);
						}else{
							inkeyLI=inkeyLI+"@#&"+jtem.get(keys[d]);
						}
						
					}
					
				}
				
				if(inkeyLI==""){
					valg="none";                          
					stt="0";  
				}else{
					String outJson=config(bhlxx,classcode,inkeyLI,areaid);
					JSONObject jtemp=JSONObject.fromObject(outJson);
					valg=(String) jtemp.get(valueGen);
					stt=(String) jtemp.get("status");	
				}						
				
				value.put("valg", valg)	;
				value.put("stt", stt)	;
			
		}
		
		
		if(stt=="0"||"0".equals(stt)){
			String fallto=matchRuleCacheMap.get("FAILTO"+memid);
			if(fallto!=null&&!"".equals(fallto)){
				String xbhlx=matchRuleCacheMap.get("XBHLX"+fallto);
				String mempin=matchRuleCacheMap.get("MEMPIN"+fallto);
				value=nLMatch(areaid,classcode,xbhlx,jsonarr,mempin,inkey,fallto);
			}
		}
		
		
		return value;
	}
	
	/**
	 * 王燕药品临时
	 * @param areaid
	 * @param classcode
	 * @param bhlxx
	 * @param jsonarr
	 * @param pinkey
	 * @param inkey
	 * @param memid
	 * @return
	 */
	public Map<String,String> nLMatch1(String areaid,String classcode,String bhlxx,String jsonarr,String pinkey,String inkey,String memid){

		String bhType=matchRuleCacheMap.get("DATACODE"+bhlxx);			
		String keyGen=matchRuleCacheMap.get("KEY"+areaid+bhType);
		String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);
		Map<String,String> value=new HashMap<String,String>();
		
		if(bhType==null||keyGen==null||valueGen==null){
			
			System.out.println("获取DATACODE、VALUE、KEY时为空");
			value.put("valg", "none")	;
			value.put("stt", "0")	;
			return value;
		}
		
		String inkeyLI="";
		String[] keys=keyGen.split("#");
		JSONObject jtem=JSONObject.fromObject(jsonarr);
		
			
		
		
				for(int d=3;d<keys.length;d++){									
						if(inkeyLI==""){
							inkeyLI=(String) jtem.get(keys[d]);
						}else{
							inkeyLI=inkeyLI+"@#&"+jtem.get(keys[d]);
						}
						
					}					
				
					
				value.put("valg", inkeyLI);
				value.put("stt", "1");		
		
		return value;
	}
	
	
	
	/**
	 * NLP匹配程序入口（带循环）
	 *
	 * @param dataClass
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String matchNlp(String dataClass,String str,String areaid) {
		
		if(this.nlpUrl==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
		} 
		//aresKey = matchRuleCacheMap.get("aresKey");
		nlpBS="1";
		
		JSONObject jsondata=JSONObject.fromObject(str);
		String datastr=(String) jsondata.get("data");
		
		
		String bhlxz=datastr.split("@#&")[0];
		String datavalue=datastr.split("@#&")[1];
		
			
		//工大NLP
		String rs=this.nlpCtrl(nlpUrl, datavalue);
		
		JSONObject jsout=JSONObject.fromObject(rs);
	
		//NLP调用结束，下面对切词后的数据进行处理 
	
		@SuppressWarnings("unchecked")
		List<String> terms=(List<String>) jsout.get("terms");
		@SuppressWarnings("unchecked")
		List<String> nlptype=(List<String>) jsout.get("nlptype");
		//jsopnlp.put(datavalue, nlptype);
		JSONObject jsop = new JSONObject();//存储NlpValue
		
		JSONObject jsonstrb = new JSONObject(); 
		String stat="1";	
		if(terms.size()==0){
			stat="0";
		}
		for(int a=0;a<terms.size();a++){//切词后的数据进行循环匹配
		  
		  
		  JSONObject jskey = new JSONObject(); 
		 
		  jskey.put("data",bhlxz+"@#&"+terms.get(a));
		  String jsonnlp= match(dataClass,jskey.toString(),areaid);
		  
		  JSONObject joo=JSONObject.fromObject(jsonnlp);
		  String value=(String) joo.get("result");
		  jsop.put(terms.get(a), value);
		  
//		  if("none".equals(value)){
//			  String inver=inerver(dataClass,jskey.toString(),areaid);
//			  JSONObject inveroo=JSONObject.fromObject(inver);
//			  if("1".equals(inveroo.get("status"))){
//				  jsonstrb.put("inver", inveroo.get("inver"));
//			  }
//		  }
		  
		  if(stat=="1"&&"none".equals(value)){			  
			  stat="0"; 
		  }
		  jskey=null;			  
		 }				
		
		
		
		
			  
		 jsonstrb.put("status", stat);
		 //jsonstrb.put("NLP", jsopnlp.toString());
		 jsonstrb.put("NLP", nlptype);
		 jsonstrb.put("NlpValue", jsop);
		 jsonstrb.put("result", "none");
		 
		 String value=jsonstrb.toString();
		 
		 
		 jsop=null;
		 jsonstrb=null;
		
		 nlpBS="0";
		 logger.info(str+"匹配结果为："+value);
		 return value;
		
		
		
	}
	
	
	/**
	 * 非结构化入口
	 *
	 * @param dataClass
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String ZnwdNlp(String str,final String areaid) {
		
		if(this.nlpUrl==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
		}
		//aresKey = matchRuleCacheMap.get("aresKey");
		
		JSONObject jsopZ = new JSONObject();
		JSONObject jsop1 = new JSONObject();//药品json
		JSONObject jsop2 = new JSONObject();//诊断json
		JSONObject jsop3 = new JSONObject();//诊疗json	
		String stat="1";
		
		
		//工大NLP
		String rs=this.nlpCtrl(nlpUrl, str);
		
		if(null == rs || "".equals(rs)){
			JSONObject statusJSON = null;
			JSONObject resultJSON = null;
			resultJSON = new JSONObject();
			statusJSON = new JSONObject();
			statusJSON.put("status", "2");
			statusJSON.put("description", "调用实体识别服务失败");
			resultJSON.put("result", statusJSON);
			return resultJSON.toString();
		}
		
		JSONObject jsout=JSONObject.fromObject(rs);
	
		//NLP调用结束，下面对切词后的数据进行处理 
	
		@SuppressWarnings("unchecked")
		final List<String> nlptype=(List<String>) jsout.get("nlptype");
		JSONObject jsop = new JSONObject();			
		
		
		
		for(int a=0;a<nlptype.size();a++){//切词后的数据进行循环匹配		
		 
		  String nlpvalue=nlptype.get(a);
		  String cnName=nlpvalue.substring(0,nlpvalue.lastIndexOf("【"));
		  String termName=nlpvalue.substring(nlpvalue.lastIndexOf("【")+1,nlpvalue.lastIndexOf("】"));
		  
		  String clacode=matchRuleCacheMap.get("TERMCLASS"+termName);
		  String tpyecode=matchRuleCacheMap.get("TERMTYPE"+termName);
		  String bhType=matchRuleCacheMap.get("DATACODE"+tpyecode);
		  String valueGen =matchRuleCacheMap.get("VALUE"+areaid+bhType);
		  
		  
		  if(clacode==null||tpyecode==null||"".equals(clacode)||"".equals(tpyecode)){
			 
			  jsopZ.put(nlpvalue, "none");
			  stat="0";
			 
		  }else{
			  
			  String jsonnlp = config(tpyecode,clacode,cnName,areaid);
			  
			  JSONObject joo=JSONObject.fromObject(jsonnlp);
			  String value=(String) joo.get(valueGen);
			  jsopZ.put(nlpvalue, value);
			  if("01".equals(clacode)){
				  jsop1.put(cnName, value);
			  }else if("02".equals(clacode)){
				  jsop2.put(cnName, value);
			  }else if("04".equals(clacode)){
				  jsop3.put(cnName, value);
			  }
			  
			 
			  if(stat=="1"&&(value=="none"||"none".equals(value))){
				  stat="0";
			  }
			  
			  
		  }		  
		  
		 
		 }				
		
		
		if(!jsop1.isEmpty()){
			jsop.put("药品", jsop1);
			
		}
		if(!jsop2.isEmpty()){
			jsop.put("诊断", jsop2);
		}
        if(!jsop3.isEmpty()){
			
			jsop.put("诊疗", jsop3);
		}
		
		
		
		JSONObject jsonstrb = new JSONObject(); 		
			  
		 jsonstrb.put("status", stat);
		 jsonstrb.put("NLP", nlptype);
		 jsonstrb.put("NlpValue", jsop);
		 jsonstrb.put("NlpResult", jsopZ);
		 String value=jsonstrb.toString();
		 
		 
		 jsop=null;
		 jsop1=null;
		 jsop2=null;
		 jsop3=null;
		 jsopZ=null;
		 jsonstrb=null;
		 logger.info(str+"匹配结果为："+value);
		 return value;
		
		
		
	}
	
	
	
		
	/**
	 * 怀疑诊断流程
	 * @param str
	 * @return
	 */
	public String doubt(String dataClass,String str,String areaid){
		
		JSONObject jsonob=JSONObject.fromObject(str);
		String data=(String) jsonob.get("data");		
		String datavalue=data.split("@#&")[1];		
		JSONObject jsonstrb = new JSONObject(); 
		
		String val="0";		
		
		if(this.doubtsStrs==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			doubtsStrs = matchRuleCacheMap.get("doubtsStrs"); 
		}
		Pattern p = Pattern.compile(this.doubtsStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {			
			val="1";	
			break;		
		}
			  
		 jsonstrb.put("status", "1");
		 jsonstrb.put("doubt", val);	
		 
		 String value=jsonstrb.toString();
		 
		 
		 jsonstrb=null;
		 logger.info(str+"的怀疑匹配结果为："+value);
		 return value;
	
		
	}
	

	/**
	 * 诊断限定词处理流程
	 * @param str
	 * @return
	 */
	public String special(String dataClass,String str,String areaid){
		
		JSONObject jsonob=JSONObject.fromObject(str);
		String data=(String) jsonob.get("data");		
		String datavalue=data.split("@#&")[1];
		String datavaluef=data.split("@#&")[0];	
		JSONObject jsonstra = new JSONObject(); 
		String value="";
		
				
		if(this.specialStrs==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			specialStrs = matchRuleCacheMap.get("specialStrs"); 
		}
		Pattern p = Pattern.compile(this.specialStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {	
			String term = m.group(1);
			datavalue=datavalue.replace(term, "");		
		}
		datavalue=mca.standarde(datavalue);
		String datan=datavaluef+"@#&"+datavalue;
		jsonstra.put("data", datan);
		String nstr=jsonstra.toString();
		String jsonstr= match(dataClass,nstr,areaid);
		JSONObject jsonstrb=JSONObject.fromObject(jsonstr);
		if(jsonstrb.get("status")=="0"||"0".equals(jsonstrb.get("status"))){
			jsonstrb.put("SPEC", "0");
		}else{
			jsonstrb.put("SPEC", "1");
		}
		 value= jsonstrb.toString();
		 jsonstra=null;
		 jsonstrb=null;
		 logger.info(str+"的诊断限定词匹配结果为："+value);
		 
		 return value;
	
		
	}
	
	/**
	 * 无法干预处理流程
	 * @param dataClass
	 * @param str
	 * @param areaid
	 * @return doubt 为1 是无法干预类型，直接返回  ；为0 继续向下走流程
	 */
	public String inerver(String dataClass,String str,String areaid){
		
		JSONObject jsonob=JSONObject.fromObject(str);
		JSONObject jsonstr=new JSONObject();
		String data=(String) jsonob.get("data");		
		String datavalue=data.split("@#&")[1];	
		JSONObject jsonstrb = new JSONObject(); 
		String status="0";
	
		
		
		if(this.inverStrs==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			inverStrs = matchRuleCacheMap.get("inverStrs"); 
		}
		Pattern p = Pattern.compile(this.inverStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {	
			String termi = m.group(1);
			datavalue = datavalue.replace(termi, "");		
				
		}
		if(datavalue==null||datavalue==""||"".equals(datavalue)){
			jsonstr.put(data.split("@#&")[1], "1");
			status="1";
			
		}else{
			datavalue=mca.standarde(datavalue);
			
			if(datavalue==null||datavalue==""||"".equals(datavalue)){
				
				jsonstr.put(data.split("@#&")[1], "1");
				status="1";	
			}else{
				jsonstr.put(data.split("@#&")[1], "0");
			}
		}
		
		jsonstrb.put("status", status);
		jsonstrb.put("inver", jsonstr.toString());	
		
		String value=jsonstrb.toString();
		 
		 
		jsonstrb=null;
		jsonstr=null;
		logger.info(str+"的无法干预匹配结果为："+value);
		return value;
	}
	
	
	/**
 * 开封切词用  暂停不用	
 * @param str
 * @return
 */
public String simnlp(String str){
		
		String bzstr=str;		
		String[] arrs={"\"",".","。","'","‘","!","！","“","|",":","："};
		String term="";
		
		 //1.去空格、tab、制表符等
		bzstr=bzstr.replaceAll("\\s*", ""); 
		
		//2、去无用术语
		if(this.termsStrs==""){
			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
			termsStrs = matchRuleCacheMap.get("termsStrs"); 
		}
		Pattern p = Pattern.compile(this.termsStrs);
		Matcher m = p.matcher(bzstr);
		while (m.find()) {
			if(term!=""){
				term="";
			}
			term = m.group(1);
			bzstr = bzstr.replace(term, "");			
		}
		//3、去（）中无中文的串。例如(C01.053)、(8)
		
		if(bzstr.indexOf("(")!=-1||bzstr.indexOf("（")!=-1||bzstr.indexOf("<")!=-1){
			
			
			for(int i=0;i<bzstr.length();i++){	
				
				int inc=-1;		    
				int ind=-1;
				char bac=bzstr.charAt(i);
				if(bac=='('||bac=='（'||bac=='<'){
					inc=i;
					for(int j=i;j<bzstr.length();j++){
						char bad=bzstr.charAt(j);
						if(bad==')'||bad=='）'||bad=='>'){
							ind=j;
							
							if(inc!=-1&&ind!=-1){							
								String hl=bzstr.substring(inc , ind+1);							
								Pattern p1=Pattern.compile("[\u4e00-\u9fa5]"); 
						        Matcher m1=p1.matcher(hl); 
						        if(!m1.find()){        	
						        	bzstr=bzstr.replace(hl, "");
						        }
							}
							break;
						}
					}
				}
				
			}
		}
		
        
		//4.替换数字＋字符组合
		String regex="[0-9]+?";
		Pattern pat=Pattern.compile(regex);
		Matcher mat=pat.matcher(bzstr);
		if(mat.find()){
			
			for(int i=0;i<bzstr.length();i++){
				 char at=bzstr.charAt(i);
				  String ats=String.valueOf(at);				 
				  Matcher matcher=pat.matcher(ats);
				  while (matcher.find()) {					    				  
					  if(i!=bzstr.length()-1){
						  if(i!=0){							  
							  String af=bzstr.substring(i-1, i);						
							  Matcher matcher2=pat.matcher(af);
							  if(matcher2.find()){
								 break; 
							  } 							  
						   }	
						   String zf=bzstr.substring(i+1, i+2);
							if(".".equals(zf)||",".equals(zf)||"、".equals(zf)||"，".equals(zf)||"。".equals(zf)||":".equals(zf)||"：".equals(zf)){
								bzstr = bzstr.replace(ats+zf, "@#&");	
							}
					    	
					    }							
					}
			}
		}
		
		//5.替换逗号,分号,问号,顿号,斜杠	
		bzstr=bzstr.replaceAll("(,|，|;|；|？|、|/)", "@#&");  
		bzstr=bzstr.replace("?", "@#&"); 
		bzstr=bzstr.replace("\\", "@#&");  
       
		
		//5.去无用符号
		for(int j=0;j<arrs.length;j++){
			if(bzstr.contains(arrs[j])){
				bzstr=bzstr.replace(arrs[j], "");
			}
		}
		String[] nlpstr=bzstr.split("@#&");
		
		List<String> ss=new ArrayList<String>();
		for(int i=0;i<nlpstr.length;i++){
			if(!(nlpstr[i]==""||"".equals(nlpstr[i]))){
				ss.add(nlpstr[i]);
			}
			
		}
		JSONObject jsop = new JSONObject();
		jsop.put("terms", ss);
		String value=jsop.toString();
		
		
		ss=null;
		jsop=null;
		logger.info("切分后词语："+value);
		return value;
		
	}



/**
 * 获取NLP切词结果
 * @Title: nlpCtrl 
 * @Description: TODO(这里用一句话描述这个方法的作用) 
 * @param @param url
 * @param @param o_name
 * @param @return
 * @param @throws UnsupportedEncodingException    设定文件 
 * @return String    返回类型 
 * @throws
 */

public  String nlpCtrl(String url,String o_name) {
    String res="";	   
    JSONObject jsop = new JSONObject();
    List<String> nnlp=new ArrayList<String>();
    List<String> ss=new ArrayList<String>();
	try {
        Map<String, String> m=new HashMap<>();
        m.put("s", o_name);
        
        HttpResult httpResult = httpClientService.doPost(url, m);
        Integer status = httpResult.getStatus();
        String str = httpResult.getData();
		//结果为json格式的数据
		if(null == str || "".equals(str)){
					return "";
		}

		JSONObject j=JSONObject.fromObject(str);		
        Iterator iterator = j.keys();
        while(iterator.hasNext()){
            String key = (String) iterator.next();//术语类型  疾病
              if(key.equals("entity")){
            	  //"entity" : "急性支气管炎【疾病】<br/>咳嗽【症状】<br/>气道高反应【症状】<br/>【转换】性障碍【症状】",

            	  String entity=j.getString(key);  
            	  String[] nlpstrs=entity.split("<br/>");
            	  for(int k=0;k<nlpstrs.length;k++){
 		        	  
 		        	  if(!(nlpstrs[k]==""||"".equals(nlpstrs[k]))){
 		        		 nnlp.add(nlpstrs[k]);
 		        		 ss.add(nlpstrs[k].substring(0,nlpstrs[k].lastIndexOf("【"))); 		        		
 						}	 		        	  
 		           }	 

		    }
        }
        
   
		
		jsop.put("terms", ss);
		jsop.put("nlptype", nnlp);
		logger.info("切分后词语："+jsop.toString());
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		return jsop.toString();
	}



public String matchPRKS(String dataClass,String str,String areaid) {
	
	String jsonarr = "";	
	JSONObject jsontemp=new JSONObject();		
	String bhlx="";                               //标化类型
	String inkey="";                              //标化后的术语		
	String valg="none";                           //返回匹配内容
	String stt="0";                               //成功失败标识
	String value="";
	 
	//获取json中的数据
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	JSONObject dc=JSONObject.fromObject(dataClass);
	JSONObject st=JSONObject.fromObject(str);
	String dataType =(String) dc.get("dataType");
	String data =(String) st.get("data");
	
	
	//对data中的数据进行分词
	String[] datas=data.split("@\\|&");		
	
		for(int j=0;j<datas.length;j++){
			String[] instr=datas[j].split("@#&");			
			if(instr.length==1){
				jsontemp.put("result", "none");		
				jsontemp.put("status", "0");
				value=jsontemp.toString();
				
				jsonarr =null;
				jsontemp=null;								
				return value;
			}
			bhlx=instr[0];
			inkey=instr[1];
						
		    inkey=mca.firstInto(inkey);	//数据预处理
		    			
		
		String bhlxx=matchRuleCacheMap.get("DETAIL"+areaid+dataType+bhlx); 
		String pinkey=matchRuleCacheMap.get("DETAILPIN"+areaid+dataType+bhlx); 
		String memid=matchRuleCacheMap.get("MEMID"+areaid+dataType+bhlx); 
		
		//内部循环判断入口
		
		Map<String,String> map=nLMatchPRKS(areaid,dataType,bhlxx,jsonarr,pinkey,inkey,memid);
						
		valg=map.get("valg");
		stt=map.get("stt");		
				
				
	}	
		

	jsontemp.put("result", valg);		
	jsontemp.put("status", stt);		
	value=jsontemp.toString();
	
	jsonarr =null;	
	jsontemp=null;
	
	System.out.println("==value=="+value);
	return value;
	
}


public Map<String,String> nLMatchPRKS(String areaid,String classcode,String bhlxx,String jsonarr,String pinkey,String inkey,String memid){

	String bhType=matchRuleCacheMap.get("DATACODE"+bhlxx);			
	String keyGen=matchRuleCacheMap.get("KEY"+areaid+bhType);
	String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);
	String stand=matchRuleCacheMap.get("STAND"+areaid+bhType);
	Map<String,String> value=new HashMap<String,String>();
	
	if(bhType==null||keyGen==null||valueGen==null){
		
		System.out.println("获取DATACODE、VALUE、KEY时为空");
		value.put("valg", "none")	;
		value.put("stt", "0")	;
		return value;
	}
	
	String inkeyLI="";	
	String valg="none";                          
	String stt="0"; 
	String oldValue=jsonarr;
			
	
	
	if(stand == "1"|| "1".equals(stand)&& nlpBS=="0"){
		inkeyLI=mca.standarde(inkey);
	}else{
		inkeyLI=inkey;
	}
	
	if(inkey==null||inkey==""||"".equals(inkey)){
		value.put("valg", valg)	;
		value.put("stt", stt)	;
		
		return value;
	}
	
	String outJson=config(bhlxx,classcode,inkeyLI,areaid);
	JSONObject jtempin=JSONObject.fromObject(outJson);			
	
	String sttpin=(String) jtempin.get("status");	
	if(sttpin=="1"||"1".equals(sttpin)){
		jsonarr="";
		jsonarr=(String) jtempin.get(valueGen);
		
	}	
	
	
	String fallto=matchRuleCacheMap.get("FAILTO"+memid);
	String succto=matchRuleCacheMap.get("SUCCTO"+memid);
	String mark=matchRuleCacheMap.get("RESUL"+memid);
	if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){
		
		String xbhlx=matchRuleCacheMap.get("XBHLX"+fallto);
		String mempin=matchRuleCacheMap.get("MEMPIN"+fallto);
		value=nLMatchPRKS(areaid,classcode,xbhlx,jsonarr,mempin,inkey,fallto);
		
	}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
		
		String xbhlx=matchRuleCacheMap.get("XBHLX"+succto);
		String mempin=matchRuleCacheMap.get("MEMPIN"+succto);
		value=nLMatchPRKS(areaid,classcode,xbhlx,jsonarr,mempin,inkey,fallto);
	}else {
		if("0".equals(mark) && "1".equals(sttpin)){			
			
			valg=(String) jtempin.get(valueGen);
			stt=sttpin;
			
		}else if("1".equals(mark) && "1".equals(sttpin)){
			String wfbh="";
			if(oldValue.contains("@#&")){
				 wfbh=oldValue.substring(oldValue.lastIndexOf("@#&")+3);
			}
			
			if("".equals(oldValue)||"none".equals(oldValue)||oldValue==null||"无法标化".equals(wfbh)){
				valg=(String) jtempin.get(valueGen);
			}else{
				valg=oldValue+","+(String) jtempin.get(valueGen);
			}
			stt=sttpin;
		}else if("0".equals(sttpin)){
			if("".equals(oldValue)||"none".equals(oldValue)||oldValue==null){
				valg="none";
				stt="0";
			}else{
				valg=jsonarr;
				stt="1";
			}			
			
		}
		value.put("valg", valg)	;
		value.put("stt", stt)	;
	}	
	
	return value;
}

public String specialPRKS(String dataClass,String str,String areaid){
	
	JSONObject jsonob=JSONObject.fromObject(str);
	JSONObject jsonstrb=new JSONObject();
	String value="";
	
	String data=(String) jsonob.get("data");
	String[] instr=data.split("@#&");			
	if(instr.length==1){
		jsonstrb.put("result", "none");		
		jsonstrb.put("status", "0");
		jsonstrb.put("SPEC", "0");
		value=jsonstrb.toString();
		
		jsonstrb =null;								
		return value;
	}
	String inkey=mca.firstInto(instr[1]);	//数据预处理
	String datavalue=inkey;
	String datavaluef=instr[0];	
	JSONObject jsonstra = new JSONObject(); 
	
	
			
	//datavalue=mca.spec11(datavalue);
	datavalue=mca.spec14(datavalue);
	String datan=datavaluef+"@#&"+datavalue;
	jsonstra.put("data", datan);
	String nstr=jsonstra.toString();
	String jsonstr= matchPRKS(dataClass,nstr,areaid);
	jsonstrb=JSONObject.fromObject(jsonstr);
	if(jsonstrb.get("status")=="0"||"0".equals(jsonstrb.get("status"))){
		String dvalue=inkey;
		//dvalue=mca.spec15(dvalue);
		dvalue=mca.spec14(dvalue);
		datan=datavaluef+"@#&"+dvalue;
		jsonstra.put("data", datan);
		nstr=jsonstra.toString();
		jsonstr= matchPRKS(dataClass,nstr,areaid);
		jsonstrb=JSONObject.fromObject(jsonstr);
		if(jsonstrb.get("status")=="0"||"0".equals(jsonstrb.get("status"))){
			String ddvalue=inkey;
			//ddvalue=mca.spec11(ddvalue);
			//ddvalue=mca.spec15(ddvalue);
			ddvalue=mca.spec14(ddvalue);
			datan=datavaluef+"@#&"+ddvalue;
			jsonstra.put("data", datan);
			nstr=jsonstra.toString();
			jsonstr= matchPRKS(dataClass,nstr,areaid);
		}
		jsonstrb.put("SPEC", "0");
	}else{
		jsonstrb.put("SPEC", "1");
	}
	 value= jsonstrb.toString();
	 jsonstra=null;
	 jsonstrb=null;
	 logger.info(str+"的诊断限定词匹配结果为："+value);
	 
	 return value;

	
}

public String matchNlpPRKS(String dataClass,String str,String areaid) {
	
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	//aresKey = matchRuleCacheMap.get("aresKey");
	nlpBS="1";
	
	JSONObject jsondata=JSONObject.fromObject(str);
	String datastr=(String) jsondata.get("data");
	
	
	String bhlxz=datastr.split("@#&")[0];
	String datavalue=datastr.split("@#&")[1];
	
		
	//工大NLP
	String rs=this.nlpCtrl(nlpUrl, datavalue);
	
	JSONObject jsout=JSONObject.fromObject(rs);

	//NLP调用结束，下面对切词后的数据进行处理 

	@SuppressWarnings("unchecked")
	List<String> terms=(List<String>) jsout.get("terms");
	@SuppressWarnings("unchecked")
	List<String> nlptype=(List<String>) jsout.get("nlptype");	
	JSONObject jsop = new JSONObject();//存储NlpValue
	
	JSONObject jsonstrb = new JSONObject(); 
	String stat="1";	
	if(terms.size()==0){
		stat="0";
	}
	for(int a=0;a<terms.size();a++){//切词后的数据进行循环匹配
		
	  String keyv=mca.firstInto(terms.get(a));//数据预处理
	  JSONObject jskey = new JSONObject(); 
	 
	  jskey.put("data",bhlxz+"@#&"+keyv);
	  String jsonnlp= matchPRKS(dataClass,jskey.toString(),areaid);
	  
	  JSONObject joo=JSONObject.fromObject(jsonnlp);
	  String value=(String) joo.get("result");
	  jsop.put(terms.get(a), value);
	  
	  if("none".equals(value)){
		  String spec=specialPRKS(dataClass,jskey.toString(),areaid);
		  JSONObject specoo=JSONObject.fromObject(spec);		  
		  if("1".equals(specoo.get("status"))){
			  jsonstrb.put("SPEC", specoo.get("SPEC"));
			  value=(String) specoo.get("result");
			  jsop.put(terms.get(a), value);
		  }
	  }
	  
	  if(stat=="1"&&"none".equals(value)){			  
		  stat="0"; 
	  }
	  jskey=null;			  
	 }				
	
	
	
	
		  
	 jsonstrb.put("status", stat);
	 //jsonstrb.put("NLP", jsopnlp.toString());
	 jsonstrb.put("NLP", nlptype);
	 jsonstrb.put("NlpValue", jsop);
	 jsonstrb.put("result", "none");
	 
	 String value=jsonstrb.toString();
	 
	 
	 jsop=null;
	 jsonstrb=null;
	
	 nlpBS="0";
	 logger.info(str+"匹配结果为："+value);
	 return value;
	}



/**
 * 普瑞快思2.3版限定词处理流程
 * @param str
 * @return
 */

public String specialPRKS23(String dataClass,String str,String areaid){
	
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();		
	}
	JSONObject jsonob=JSONObject.fromObject(str);
	JSONObject jsonstrb=new JSONObject();
	String data=(String) jsonob.get("data");
	String value="";
	JSONObject dc=JSONObject.fromObject(dataClass);	
	String classcode =(String) dc.get("dataType");
	
	String specList=matchRuleCacheMap.get("SPECSOFT"+areaid+classcode);
	String[] cnt=specList.split("@");
	
	String[] instr=data.split("@#&");			
	if(instr.length==1){
		jsonstrb.put("result", "none");		
		jsonstrb.put("status", "0");
		jsonstrb.put("SPEC", "0");
		value=jsonstrb.toString();
		
		jsonstrb =null;								
		return value;
	}
	
	String inkey=data.split("@#&")[1];
	inkey=mca.firstInto(inkey);	
	String datavaluef=data.split("@#&")[0];	
	
	
	//特定区域下特殊处理步骤总数和每个步骤的内容
	for(int i=0;i<cnt.length;i++){
		jsonstrb=new JSONObject();		
		String datavalue=inkey;	
		//获取第i步内容
		String softi=cnt[i];
		String[] listSoft=softi.split(",");
		
		List<String> datav=new ArrayList<String>();//拆分后循环处理源数据		
		
		for(int j=0;j<listSoft.length;j++){
			//得到内容分量j
			String cx=listSoft[j];
			//处理数据
			if("spec11".equals(cx)){
				String dv_temp=datavalue;//每次循环去限定词，要使用原始词
				List<String> specv=mca.spec11(datavalue);
				for(int k=0;k<specv.size();k++){
					datavalue=dv_temp;
					String spec=specv.get(k);
					//System.out.println("==dv_temp=="+dv_temp+"==spec===="+spec);
					if(!("".equals(spec)||spec==null)){
						String demosub = spec.substring(1,spec.length()-1);
						String demoArray[] = demosub.split(",");
						for(int d=0;d<demoArray.length;d++){
							datavalue=datavalue.replace(demoArray[d].trim(), "");	
						}
					}
					
					
					for(int a=j+1;a<listSoft.length;a++){
						
						if("spec12".equals(cx)){
							datavalue=mca.spec12(datavalue);
						}else if("spec13".equals(cx)){
							datav=mca.spec13(datavalue);
						}else if("spec15".equals(cx)){
							List<String> noTermv=mca.spec15(datavalue);
							for(int k1=0;k1<noTermv.size();k1++){
								String noterm=noTermv.get(k1);
								if(!("".equals(noterm)||noterm==null)){
									String sub = noterm.substring(1,noterm.length()-1);
									String Array[] = sub.split(",");
									for(int d1=0;d1<Array.length;d1++){
										datavalue=datavalue.replace(Array[d1].trim(), "");	
									}
								}								
								
								for(int a1=j+1;a1<listSoft.length;a1++){
									
									if("spec12".equals(cx)){
										datavalue=mca.spec12(datavalue);
									}else if("spec13".equals(cx)){
										datav=mca.spec13(datavalue);
									}else if("spec16".equals(cx)){
										datav=mca.spec16(datavalue);
									}
								}
								
								jsonstrb=specValue(datavalue,datav,datavaluef,dataClass , areaid, softi);
								if("1".equals(jsonstrb.get("status"))){
									break;
								}
							}
						}else if("spec16".equals(cx)){
							datav=mca.spec16(datavalue);
						}
					}
					
					jsonstrb=specValue(datavalue,datav,datavaluef,dataClass , areaid, softi);
					if("1".equals(jsonstrb.get("status"))){
						break;
					}
				}
				
			}else if("spec12".equals(cx)){
				datavalue=mca.spec12(datavalue);
			}else if("spec13".equals(cx)){
				datav=mca.spec13(datavalue);
			}else if("spec15".equals(cx)){	
				String dv_temp=datavalue;//每次循环去无用术语，要使用原始词
				List<String> noTermv=mca.spec15(datavalue);
				for(int k1=0;k1<noTermv.size();k1++){
					datavalue=dv_temp;
					String noterm=noTermv.get(k1);
					if(!("".equals(noterm)||noterm==null)){
						String sub = noterm.substring(1,noterm.length()-1);
						String Array[] = sub.split(",");
						for(int d1=0;d1<Array.length;d1++){
							datavalue=datavalue.replace(Array[d1].trim(), "");	
						}
					}
					
					
					for(int a1=j+1;a1<listSoft.length;a1++){
						
						if("spec12".equals(cx)){
							datavalue=mca.spec12(datavalue);
						}else if("spec13".equals(cx)){
							datav=mca.spec13(datavalue);
						}else if("spec16".equals(cx)){
							datav=mca.spec16(datavalue);
						}
					}
					
					jsonstrb=specValue(datavalue,datav,datavaluef,dataClass , areaid, softi);
					if("1".equals(jsonstrb.get("status"))){
						break;
					}
				}
				
			}else if("spec16".equals(cx)){
				datav=mca.spec16(datavalue);
			}
		}
		
		
		jsonstrb=specValue(datavalue,datav,datavaluef,dataClass , areaid, softi);

		if("1".equals(jsonstrb.get("status"))){
			break;
		}
		
	}			
	
	
	if(jsonstrb.get("status")=="0"||"0".equals(jsonstrb.get("status"))){
		jsonstrb.put("SPEC", "0");
	}else{
		jsonstrb.put("SPEC", "1");
	}

	 value= jsonstrb.toString();	 
	 jsonstrb=null;
	 logger.info(str+"普瑞快思2.3的诊断限定词匹配结果为："+value);
	 
	 return value;

	
}

/**
 * 特殊处理匹配返回结果
 * @param datavalue  处理后单条数据
 * @param datav      处理后多条数据-括号和斜杠
 * @param datavaluef 标化类型
 * @param dataClass  聚类
 * @param areaid     区域
 * @param softi      特殊处理步骤内容 例如;spec11,spec12
 * @return
 */
public JSONObject specValue(String datavalue,List<String> datav,String datavaluef,String dataClass ,String areaid,String softi){
	
	JSONObject jsonstrb=new JSONObject();
	JSONObject jsonstra = new JSONObject(); 
	String datan="";
	String nstr="";
	if(datav.size()<=1){
		datavalue=mca.spec14(datavalue);
		//特殊字符处理后的剩余字符串如果为空的话，则输出无法标化20171122
		if("".equals(datavalue)||datavalue==null){
			jsonstrb.put("result", "其他-@#&无法标化");		
			jsonstrb.put("status", "1");
			jsonstrb.put("SPEC", "1");
			//value=jsonstrb.toString();
			
			//jsonstrb =null;								
			return jsonstrb;
		}

		datan=datavaluef+"@#&"+datavalue;
		jsonstra.put("data", datan);
		nstr=jsonstra.toString();
		String jsonstr= matchPRKS23(dataClass,nstr,areaid);
		jsonstrb=JSONObject.fromObject(jsonstr);
	}else{
		
		if(softi.contains("spec13")){
			JSONObject jspec=new JSONObject();
			
				String zknum=datav.get(datav.size()-1);
				int num=Integer.parseInt(zknum);
				String zkh=datav.get(num);
				datavalue=mca.spec14(zkh);
				datan=datavaluef+"@#&"+datavalue;
				jsonstra.put("data", datan);
				nstr=jsonstra.toString();
				String jsonstr= matchPRKS23(dataClass,nstr,areaid);
				jsonstrb=JSONObject.fromObject(jsonstr);
				String res="";
				String spNlp="";
				JSONObject spNlpValue= new JSONObject();
				jspec.put(zkh, (String)jsonstrb.get("result"));
				if("1".equals(jsonstrb.get("status"))){
					
					
					for(int n=0;n<datav.size()-1;n++){
						if(n!=num){
							datavalue=mca.spec14(datav.get(n));
							datan=datavaluef+"@#&"+datavalue;
							jsonstra.put("data", datan);
							nstr=jsonstra.toString();
							jsonstr= matchPRKS23(dataClass,nstr,areaid);
							jsonstrb=JSONObject.fromObject(jsonstr);
							if("1".equals(jsonstrb.get("status"))){
														
								String vav=(String) jsonstrb.get("result");
								jspec.put(datav.get(n), vav);
		
							}
						}

					}
					for(int n=0;n<datav.size()-1;n++){
						String spName=datav.get(n);
						if(jspec.containsKey(spName)){
							if("".equals(res)){
								res=jspec.getString(spName);
							}else{
								res=res+","+jspec.getString(spName);
							}
							String sp14=mca.spec14(spName);
							if("".equals(spNlp)){
								spNlp=sp14;
							}else{
								spNlp=spNlp+"；"+sp14;
							}
							spNlpValue.put(sp14, jspec.getString(spName));
						}
						
					}
					
					 jspec=null;
					 jsonstrb.put("spNlp", spNlp);
					 jsonstrb.put("spNlpValue", spNlpValue); 
					 jsonstrb.put("result", res);
					 jsonstrb.put("status", "1"); 
					
				   }else{
					    jsonstrb.put("result", "none");
						jsonstrb.put("status", "0"); 
				   }
			
			
			}else{
					String res="";
					String spNlp="";
					JSONObject spNlpValue= new JSONObject();
				    for(int m=0;m<datav.size();m++){
				    	    String data0=mca.spec14(datav.get(m));				
							datan=datavaluef+"@#&"+data0;
							jsonstra.put("data", datan);
							nstr=jsonstra.toString();
							String jsonstr0= matchPRKS23(dataClass,nstr,areaid);
							JSONObject  jsonstrb0=JSONObject.fromObject(jsonstr0);
							if("1".equals(jsonstrb0.get("status"))){									
								if("".equals(res)){
									res=jsonstrb0.getString("result");
								}else{
									res=res+","+jsonstrb0.getString("result");
								}
								if("".equals(spNlp)){
									spNlp=data0;
								}else{
									spNlp=spNlp+"；"+data0;
								}
								spNlpValue.put(data0, jsonstrb0.getString("result"));
								jsonstrb.put("result", res);
								jsonstrb.put("status", "1");	
							}else{ 
								jsonstrb.put("result", "none");
								jsonstrb.put("status", "0");
								break;
							}
				    }
				   if("1".equals(jsonstrb.getString("status"))){         //斜杠切词结果
					   jsonstrb.put("spNlp", spNlp);
					   jsonstrb.put("spNlpValue", spNlpValue);
				   }
					
			  }
		}
	return jsonstrb;
}


public String matchPRKS23(String dataClass,String str,String areaid) {
	
	String jsonarr = "";	
	JSONObject jsontemp=new JSONObject();		
	String bhlx="";                               //标化类型
	String inkey="";                              //标化后的术语		
	String valg="none";                           //返回匹配内容
	String stt="0";                               //成功失败标识
	String value="";
	 
	//获取json中的数据
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	JSONObject dc=JSONObject.fromObject(dataClass);
	JSONObject st=JSONObject.fromObject(str);
	String dataType =(String) dc.get("dataType");
	String data =(String) st.get("data");
	
	
	//对data中的数据进行分词
	String[] datas=data.split("@\\|&");		
	
		for(int j=0;j<datas.length;j++){
			String[] instr=datas[j].split("@#&");			
			if(instr.length==1){
				jsontemp.put("result", "none");		
				jsontemp.put("status", "0");
				value=jsontemp.toString();
				
				jsonarr =null;
				jsontemp=null;								
				return value;
			}
			bhlx=instr[0];
			inkey=instr[1];
			inkey=mca.firstInto(inkey);				
			
		
		String bhlxx=matchRuleCacheMap.get("DETAIL"+areaid+dataType+bhlx); 
		String pinkey=matchRuleCacheMap.get("DETAILPIN"+areaid+dataType+bhlx); 
		String memid=matchRuleCacheMap.get("MEMID"+areaid+dataType+bhlx); 
		
		//内部循环判断入口
		
		Map<String,String> map=nLMatchPRKS23(areaid,dataType,bhlxx,jsonarr,pinkey,inkey,memid);
						
		valg=map.get("valg");
		stt=map.get("stt");		
				
		
		//(诊断用)当术语是单字母，纯数字，数字+符号，纯字母组合，字母+数字组合的，如果匹配不成功，更改为输出无法标化20171122
		Pattern paSZ = Pattern.compile("[0-9]");
		Matcher m1=paSZ.matcher(inkey);	
		String nr=mca.spec12(inkey);
		int lenth=instr[1].length();
		int bo=0;		 
        if((m1.find()&&lenth==1)||"".equals(nr)){        	
        	bo=1;
        }
        
		if("0".equals(stt)&&bo==1){
			//valg="spec";
			valg="其他-@#&无法标化";			
			stt="1";
		}
		
		//诊断词中间的标点符号也可以考虑去掉20170712
		//		String ninkey=inkey;
		//		if(arr==""||"".equals(arr)){
		//			matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		//			arr = matchRuleCacheMap.get("arr");
		//		}			
		//		Pattern prn = Pattern.compile(arr);
		//		Matcher mer = prn.matcher(ninkey);
		//		if(mer.find()){
		//			String term = mer.group(1);
		//			ninkey = ninkey.replace(term, "");
		//		}
		//		if("0".equals(stt)&&ninkey.length()!=inkey.length()){
		//			
		//			Map<String,String> map1=nLMatchPRKS23(areaid,dataType,bhlxx,jsonarr,pinkey,ninkey,memid);
		//			
		//			valg=map1.get("valg");
		//			stt=map1.get("stt");	
		//		}
		
	}	
		

	jsontemp.put("result", valg);		
	jsontemp.put("status", stt);		
	value=jsontemp.toString();
	
	jsonarr =null;	
	jsontemp=null;
	
	System.out.println("==value=="+value);
	return value;
	
}


public Map<String,String> nLMatchPRKS23(String areaid,String classcode,String bhlxx,String jsonarr,String pinkey,String inkey,String memid){

	String bhType=matchRuleCacheMap.get("DATACODE"+bhlxx);			
	String keyGen=matchRuleCacheMap.get("KEY"+areaid+bhType);
	String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);
	String stand=matchRuleCacheMap.get("STAND"+areaid+bhType);
	Map<String,String> value=new HashMap<String,String>();
	
	if(bhType==null||keyGen==null||valueGen==null){
		
		System.out.println("获取DATACODE、VALUE、KEY时为空");
		value.put("valg", "none")	;
		value.put("stt", "0")	;
		return value;
	}
	
	String inkeyLI="";	
	String valg="none";                          
	String stt="0"; 
	String oldValue=jsonarr;
			
	
	
	if(stand == "1"|| "1".equals(stand)&& nlpBS=="0"){
		//inkeyLI=mca.standarde(inkey);
		inkeyLI=inkey;
	}else{
		inkeyLI=inkey;
	}
	
	if(inkey==null||inkey==""||"".equals(inkey)){
		value.put("valg", valg)	;
		value.put("stt", stt)	;
		
		return value;
	}
	
	String outJson=config(bhlxx,classcode,inkeyLI,areaid);
	JSONObject jtempin=JSONObject.fromObject(outJson);			
	
	String sttpin=(String) jtempin.get("status");	
	if(sttpin=="1"||"1".equals(sttpin)){
		jsonarr="";
		jsonarr=(String) jtempin.get(valueGen);
		
	}	
	
	
	String fallto=matchRuleCacheMap.get("FAILTO"+memid);
	String succto=matchRuleCacheMap.get("SUCCTO"+memid);
	String mark=matchRuleCacheMap.get("RESUL"+memid);
	if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){
		
		String xbhlx=matchRuleCacheMap.get("XBHLX"+fallto);
		String mempin=matchRuleCacheMap.get("MEMPIN"+fallto);
		value=nLMatchPRKS23(areaid,classcode,xbhlx,jsonarr,mempin,inkey,fallto);
		
	}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
		
		String xbhlx=matchRuleCacheMap.get("XBHLX"+succto);
		String mempin=matchRuleCacheMap.get("MEMPIN"+succto);
		value=nLMatchPRKS23(areaid,classcode,xbhlx,jsonarr,mempin,inkey,fallto);
	}else {
		if("0".equals(mark) && "1".equals(sttpin)){			
			
			valg=(String) jtempin.get(valueGen);
			stt=sttpin;
			
		}else if("1".equals(mark) && "1".equals(sttpin)){
			
			//如果无法标化和本体均匹配成功，则输出本体结果
			String wfbh="";
			if(oldValue.contains("@#&")){
				 wfbh=oldValue.substring(oldValue.lastIndexOf("@#&")+3);
			}
			
			//如果中医词表中无西医疾病名称，则两者均输出，如果有西医名称，则仅输出中医词表中的中医名称和西医名称。
			//更改为中医西医均输出20171122
			//String val=(String) jtempin.get(valueGen);
			//String[] vals=val.split("@#&");
			
			//if("".equals(oldValue)||"none".equals(oldValue)||oldValue==null||"无法标化".equals(wfbh)||!"".equals(vals[2])){
			if("".equals(oldValue)||"none".equals(oldValue)||oldValue==null||"无法标化".equals(wfbh)){
				valg=(String) jtempin.get(valueGen);
			}else{
				valg=oldValue+","+(String) jtempin.get(valueGen);
			}
			stt=sttpin;
		}else if("0".equals(sttpin)){
			if("".equals(oldValue)||"none".equals(oldValue)||oldValue==null){
				valg="none";
				stt="0";
			}else{
				valg=jsonarr;
				stt="1";
			}			
			
		}
		value.put("valg", valg)	;
		value.put("stt", stt)	;
	}	
	
	return value;
}

public String matchNlpPRKS23(String dataClass,String str,String areaid) {
	
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	//aresKey = matchRuleCacheMap.get("aresKey");
	nlpBS="1";
	
	JSONObject jsondata=JSONObject.fromObject(str);
	String datastr=(String) jsondata.get("data");
	
	
	String bhlxz=datastr.split("@#&")[0];
	String datavalue=datastr.split("@#&")[1];
	
//	long begin=System.currentTimeMillis();	
	//工大NLP
	String rs=this.nlpCtrl(nlpUrl, datavalue);
//	long end=System.currentTimeMillis();
//	long nlpTime=end-begin;
//	System.out.println("nlpTime======="+nlpTime);
	JSONObject jsout=JSONObject.fromObject(rs);

	//NLP调用结束，下面对切词后的数据进行处理 

	@SuppressWarnings("unchecked")
	List<String> terms=(List<String>) jsout.get("terms");
	@SuppressWarnings("unchecked")
	List<String> nlptype=(List<String>) jsout.get("nlptype");
	String nlptypes="";
	for(int s=0;s<nlptype.size();s++){
		if("".equals(nlptypes)){
			nlptypes=nlptype.get(s);
		}else{
			nlptypes=nlptypes+"；"+nlptype.get(s);
		}
	}
	JSONObject jsop = new JSONObject();//存储NlpValue
	
	JSONObject jsonstrb = new JSONObject(); 
	String stat="1";	
	if(terms.size()==0){
		stat="0";
	}
	for(int a=0;a<terms.size();a++){//切词后的数据进行循环匹配
	  
	  String keyv=mca.firstInto(terms.get(a));	
	  
	  
	  JSONObject jskey = new JSONObject(); 	 
	  jskey.put("data",bhlxz+"@#&"+keyv);
	  String jsonnlp= matchPRKS23(dataClass,jskey.toString(),areaid);
	  
	  JSONObject joo=JSONObject.fromObject(jsonnlp);
	  String value=(String) joo.get("result");	  
		//(诊断用)当术语是单字母，纯数字，数字+符号，纯字母组合，字母+数字组合的，如果匹配不成功，则直接舍弃，不输出标化结果20170712
		
//		if("spec".equals(value)){
//			jsop.remove(terms.get(a));
//		}else{
//			 jsop.put(terms.get(a), value);
//			 
//		}
	  //(诊断用)当术语是单字母，纯数字，数字+符号，纯字母组合，字母+数字组合的，如果匹配不成功，输出无法标化20171122
	  jsop.put(terms.get(a), value);
		//如果成功，增加SPEC字段
		if("1".equals(joo.get("status"))){
			String sre="";
			sre=(String) jsonstrb.get("SPEC");
			
			if("".equals(sre)||sre==null||"null".equals(sre)){
				jsonstrb.put("SPEC", "0");
			}else{
				jsonstrb.put("SPEC", sre+",0");
			}
		}
		
		
	  
	  if("none".equals(value)){
		  String spec=specialPRKS23(dataClass,jskey.toString(),areaid);
		  JSONObject specoo=JSONObject.fromObject(spec);		  
		  if("1".equals(specoo.get("status"))){	
			  String res=(String) jsonstrb.get("SPEC");
			  if("".equals(res)||res==null||"null".equals(res)){
					jsonstrb.put("SPEC", "1");
				}else{
					jsonstrb.put("SPEC", res+",1");
				}			
			  value=(String) specoo.get("result");		
		  
			//特殊字符处理后的剩余字符串如果为空的话，输出无法标化20171122
			  jsop.put(terms.get(a), value); 
		  }else{
			  String tszl=tsckZL(dataClass,jskey.toString(),areaid);
			  JSONObject tszloo=JSONObject.fromObject(tszl);
			  if("1".equals(tszloo.get("status"))){				 
				  value=(String) tszloo.get("TSBH");
				  jsop.put(terms.get(a), value);
				  String rezl=(String) jsonstrb.get("SPEC");
				  if("".equals(rezl)||rezl==null||"null".equals(rezl)){
						jsonstrb.put("SPEC", "0");
					}else{
						jsonstrb.put("SPEC", rezl+",0");
					}
			  }else{
				  String tsshzt=tsckSHZT(dataClass,jskey.toString(),areaid);
				  JSONObject tsshztoo=JSONObject.fromObject(tsshzt);
				 
				  if("1".equals(tsshztoo.get("status"))){				 
					  value=(String) tszloo.get("TSBH");
					  jsop.put(terms.get(a), value);
					  String rezlsh=(String) jsonstrb.get("SPEC");
					  if("".equals(rezlsh)||rezlsh==null||"null".equals(rezlsh)){
							jsonstrb.put("SPEC", "0");
						}else{
							jsonstrb.put("SPEC", rezlsh+",0");
						}
				  }else{
					  String reszl=(String) jsonstrb.get("SPEC");
					  if("".equals(reszl)||reszl==null||"null".equals(reszl)){
							jsonstrb.put("SPEC", "0");
						}else{
							jsonstrb.put("SPEC", reszl+",0");
						}
				  }
				  
				  
			  }
		  }
	  }
	  
	  if(stat=="1"&&"none".equals(value)){			  
		  stat="0"; 
	  }
	  
	  jskey=null;			  
	 }				
	
	
	
	
		  
	 jsonstrb.put("status", stat);
	 //jsonstrb.put("NLP", jsopnlp.toString());
	 jsonstrb.put("NLP", nlptypes);
	 jsonstrb.put("NlpValue", jsop);
	 jsonstrb.put("result", "none");
	 
	 String value=jsonstrb.toString();
	 
	 
	 jsop=null;
	 jsonstrb=null;
	
	 nlpBS="0";
	 logger.info(str+"匹配结果为："+value);
	 return value;
	}


public String matchNlpPRKS23b(String dataClass,String str,String areaid,String nlp) {
	
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	//aresKey = matchRuleCacheMap.get("aresKey");
	nlpBS="1";
	
	JSONObject jsondata=JSONObject.fromObject(str);
	String datastr=(String) jsondata.get("data");
	
	
	String bhlxz=datastr.split("@#&")[0];
	//String datavalue=datastr.split("@#&")[1];	

	//NLP调用结束，下面对切词后的数据进行处理 
	String nlptype=nlp;
//	String demosub = nlp.substring(2,nlp.length()-2);	
//	String demoArray[] = demosub.split("\",\"");
	String demoArray[] = nlp.split("；");
	List<String> terms = Arrays.asList(demoArray);	
	
	
	JSONObject jsop = new JSONObject();//存储NlpValue
	
	JSONObject jsonstrb = new JSONObject(); 
	String stat="1";	
	if(terms.size()==0){
		stat="0";
	}
	for(int a=0;a<terms.size();a++){//切词后的数据进行循环匹配
	  
	  String name=terms.get(a);
	  String yname="";
	  if(name.indexOf("【")!=-1){
			yname=name.substring(0,name.lastIndexOf("【"));
		}else{
			yname=name;
		}
	  	
	  String keyv=mca.firstInto(yname);	
	  
	  
	  JSONObject jskey = new JSONObject(); 	 
	  jskey.put("data",bhlxz+"@#&"+keyv);
	  String jsonnlp= matchPRKS23(dataClass,jskey.toString(),areaid);
	  
	  JSONObject joo=JSONObject.fromObject(jsonnlp);
	  String value=(String) joo.get("result");	  
	  jsop.put(yname, value);
			 
		
		//如果成功，增加SPEC字段
		if("1".equals(joo.get("status"))){
			String sre="";
			sre=(String) jsonstrb.get("SPEC");
			
			if("".equals(sre)||sre==null||"null".equals(sre)){
				jsonstrb.put("SPEC", "0");
			}else{
				jsonstrb.put("SPEC", sre+",0");
			}
		}
		
		
	  
	  if("none".equals(value)){
		  String spec=specialPRKS23(dataClass,jskey.toString(),areaid);
		  JSONObject specoo=JSONObject.fromObject(spec);		  
		  if("1".equals(specoo.get("status"))){	
			  String res=(String) jsonstrb.get("SPEC");
			  if("".equals(res)||res==null||"null".equals(res)){
					jsonstrb.put("SPEC", "1");
				}else{
					jsonstrb.put("SPEC", res+",1");
				}			
			  value=(String) specoo.get("result");		
			  
			  jsop.put(yname, value); 
			  
			 
		  }else{
			  String tszl=tsckZL(dataClass,jskey.toString(),areaid);
			  JSONObject tszloo=JSONObject.fromObject(tszl);
			  if("1".equals(tszloo.get("status"))){				 
				  value=(String) tszloo.get("TSBH");
				  jsop.put(yname, value);
				  String rezl=(String) jsonstrb.get("SPEC");
				  if("".equals(rezl)||rezl==null||"null".equals(rezl)){
						jsonstrb.put("SPEC", "0");
					}else{
						jsonstrb.put("SPEC", rezl+",0");
					}
			  }else{
				  String tsshzt=tsckSHZT(dataClass,jskey.toString(),areaid);
				  JSONObject tsshztoo=JSONObject.fromObject(tsshzt);
				 
				  if("1".equals(tsshztoo.get("status"))){				 
					  value=(String) tszloo.get("TSBH");
					  jsop.put(yname, value);
					  String rezlsh=(String) jsonstrb.get("SPEC");
					  if("".equals(rezlsh)||rezlsh==null||"null".equals(rezlsh)){
							jsonstrb.put("SPEC", "0");
						}else{
							jsonstrb.put("SPEC", rezlsh+",0");
						}
				  }else{
					  String reszl=(String) jsonstrb.get("SPEC");
					  if("".equals(reszl)||reszl==null||"null".equals(reszl)){
							jsonstrb.put("SPEC", "0");
						}else{
							jsonstrb.put("SPEC", reszl+",0");
						}
				  }
				  
				  
			  }
		  }
	  }
	  
	  if(stat=="1"&&"none".equals(value)){			  
		  stat="0"; 
	  }
	  
	  jskey=null;			  
	 }				
	
	
	
	
		  
	 jsonstrb.put("status", stat);
	 //jsonstrb.put("NLP", jsopnlp.toString());
	 jsonstrb.put("NLP", nlptype);
	 jsonstrb.put("NlpValue", jsop);
	 jsonstrb.put("result", "none");
	 
	 String value=jsonstrb.toString();
	 
	 
	 jsop=null;
	 jsonstrb=null;
	
	 nlpBS="0";
	 logger.info(str+"匹配结果为："+value);
	 return value;
	}

/*
 * 特殊类型-肿瘤处理流程
 */
public String tsckZL(String dataClass,String str,String areaid){
	
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	
	if(this.jpStrs==""||this.zlStrs==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		jpStrs = matchRuleCacheMap.get("jpStrs"); 
		zlStrs = matchRuleCacheMap.get("zlStrs"); 
	}
	
	JSONObject jsondata=JSONObject.fromObject(str);
	String datastr=(String) jsondata.get("data");	
	JSONObject json=new JSONObject();
	json.put("result", "none");
	json.put("TSBH", "none");
	json.put("status", "0");
	
	
	
	String[] instr=datastr.split("@#&");			
	if(instr.length==1){									
		return json.toString();
	}
	String bhlxz=instr[0];
	bhlxz="35";
	String datavalue=instr[1];
	datavalue=mca.firstInto(datavalue);//数据预处理
	
	String[] shzt=new String[3];
	List<String> jp=new ArrayList<String>();
	List<String> zy=new ArrayList<String>();
	
	
	int stt=-1;
	Pattern p = Pattern.compile(this.jpStrs);
	Matcher m = p.matcher(datavalue);
	while (m.find()) {	
		String term = m.group(1);
		jp.add(term);		
		datavalue=datavalue.replace(term, "").trim();
		shzt[2]=datavalue;
		Pattern p1 = Pattern.compile(this.zlStrs);
		Matcher m1 = p1.matcher(datavalue);
		while (m1.find()) {	
			String tem = m1.group(1);
			zy.add(tem);			
			datavalue=datavalue.replace(tem, "").trim();
			shzt[2]=datavalue;
			stt=1;
		}		
	}
	if(stt==1){
		for(int a=0;a<jp.size();a++){
			
			String jpval=config("36", "02", jp.get(a), areaid);
			String bhType=matchRuleCacheMap.get("DATACODE"+"36");	
			String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);
			JSONObject jsd=JSONObject.fromObject(jpval);
			String jsdvalue=(String) jsd.get(valueGen);
			for(int b=0;b<zy.size();b++){
				String zyval=config("40", "02", zy.get(b), areaid);
				String zyType=matchRuleCacheMap.get("DATACODE"+"40");	
				String zyGen=matchRuleCacheMap.get("VALUE"+areaid+zyType);
				JSONObject zyjsd=JSONObject.fromObject(zyval);
				String zyvalue=(String) zyjsd.get(zyGen);
				
				if(!"none".equals(jsdvalue)&&!"none".equals(zyvalue)){
					String zlzy=zlbw(zyvalue,jsdvalue,areaid);
					JSONObject zlzyoo=JSONObject.fromObject(zlzy);
					if(!"none".equals(zlzyoo.get("TSBH"))){
						String val2=(String) json.get("TSBH");
						if("none".equals(val2)){
							json.put("TSBH", zlzyoo.get("TSBH"));
						}else{
							json.put("TSBH", val2+","+zlzyoo.get("TSBH"));
						}					
						json.put("status", "1");
					}
				}
			}
			
		}
		
	}
	if(!("".equals(shzt[2])||shzt[2]==null)){
		shzt[2]=mca.firstInto(shzt[2]);
	}
	
	if(!("".equals(shzt[2])||shzt[2]==null)&&!"none".equals(json.get("TSBH"))){
		String spev=mca.spec11_o(shzt[2]);		
		if(!"".equals(spev)||spev!=null){
			spev=mca.spec15_o(spev);
		}
		spev=spev.trim();
		if(!"".equals(spev)||spev!=null){

			
			  JSONObject jskey = new JSONObject(); 		 
			  jskey.put("data",bhlxz+"@#&"+shzt[2]);
			  String jsonts= matchPRKS23(dataClass,jskey.toString(),areaid);
			  
			  JSONObject joo=JSONObject.fromObject(jsonts);
			  String value=(String) joo.get("result");
			
			  
			  if("none".equals(value)){
				  String spec=specialPRKS23(dataClass,jskey.toString(),areaid);
				  JSONObject specoo=JSONObject.fromObject(spec);		  
				  if("1".equals(specoo.get("status"))){
					  json.put("SPEC", specoo.get("SPEC"));
					  String splue=(String) specoo.get("result");
					  if(!"spec".equals(value)){
						  String val=(String) json.get("TSBH");
						  json.put("TSBH", splue+","+val);
					  }
					 
				  }	else{
					  json.put("status", "0");
				  }
				  
			  }else{
				  	  if(!"spec".equals(value)){
				  		  String val=(String) json.get("TSBH");
						  json.put("TSBH", value+","+val);
				  	  }
				 	 
				  
			  }
		
		}
	}
	
	return json.toString();
}

/*
 * 肿瘤部位循环匹配
 */
public String zlbw(String zyvalue,String bwvalue,String areaid){
	
	JSONObject json=new JSONObject();
	String value="";
	json.put("result", "none");
	json.put("TSBH", "none");
	json.put("status", "0");
	String hbval=config("37", "02", zyvalue+"@#&"+bwvalue, areaid);
	String bhType=matchRuleCacheMap.get("DATACODE"+"37");	
	String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);
	JSONObject jsdhbval=JSONObject.fromObject(hbval);
	String jsdva=(String) jsdhbval.get(valueGen);
	if(!"none".equals(jsdva)){
		json.put("TSBH", jsdva);
		json.put("status", "1");
		value=json.toString();
	}else{
		String jpsval=config("39", "02", bwvalue, areaid);
		JSONObject js=JSONObject.fromObject(jpsval);
		String bhTyp=matchRuleCacheMap.get("DATACODE"+"39");	
		String valueGe=matchRuleCacheMap.get("VALUE"+areaid+bhTyp);
		String jslue=(String) js.get(valueGe);
		if(!"none".equals(jslue)){
			String[] jslues=jslue.split(";");
			String val="";
			for(int i=0;i<jslues.length;i++){
				value=zlbw(zyvalue,jslues[i],areaid);
				JSONObject jsvalue=JSONObject.fromObject(value);
				String jsTSBH=(String) jsvalue.get("TSBH");
				if(!"none".equals(jsTSBH)){
					if(!"".equals(val)){
						val=val+","+jsTSBH;
					}else{
						val=jsTSBH;
					}
				}
			}
			if(!"".equals(val)){
				json.put("TSBH", val);
				json.put("status", "1");
				
			}
			
		}
		
		value=json.toString();
	}
	
	return value;
}

/*
 * 特殊类型-术后处理流程
 */
public String tsckSHZT(String dataClass,String str,String areaid){
	
	
	if(this.nlpUrl==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		nlpUrl = matchRuleCacheMap.get("nlpUrl"); 
	}
	if(this.opStrs==""){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		opStrs = matchRuleCacheMap.get("opStrs"); 
	}
	
	JSONObject jsondata=JSONObject.fromObject(str);
	String datastr=(String) jsondata.get("data");	
	JSONObject json=new JSONObject();
	json.put("result", "none");
	json.put("TSBH", "none");
	json.put("status", "0");
	
	String[] instr=datastr.split("@#&");			
	if(instr.length==1){									
		return json.toString();
	}
	String bhlxz=instr[0];
	bhlxz="35";
	String datavalue=mca.firstInto(instr[1]);//数据预处理
	String[] shzt=new String[2];
	
	
	Pattern p = Pattern.compile(this.opStrs);
	Matcher m = p.matcher(datavalue);
	while (m.find()) {	
		String term = m.group(1);	
		datavalue=datavalue.replace(term, "").trim();
		shzt[0]=term;
		shzt[1]=datavalue;
		json.put("TSBH", "特指手术后状态@#&Z98.800@#&@#&诊断");
		json.put("status", "1");
	}
	
	if(!("".equals(shzt[1])||shzt[1]==null)){
		shzt[1]=mca.firstInto(shzt[1]);
	}
	
	if(!("".equals(shzt[1])||shzt[1]==null)&&!"none".equals(json.get("TSBH"))){
		
				
		 String jp=config("36", "02", mca.spec14(shzt[1]), areaid);//解剖部位匹配
		 JSONObject jsd=JSONObject.fromObject(jp);
		 String jps=(String) jsd.get("status");
		 if("0".equals(jps)){
			 String ss=config("33", "02", mca.spec14(shzt[1]), areaid);//诊断-手术匹配
			 JSONObject ssd=JSONObject.fromObject(ss);
			 String sss=(String) ssd.get("status");
			 if("0".equals(sss)){
				 String zls=config("38", "02", mca.spec14(shzt[1]), areaid);//诊断-诊疗匹配
				 JSONObject zlsd=JSONObject.fromObject(zls);
				 String zlss=(String) zlsd.get("status");
				 
				 if("0".equals(zlss)){                                      //走诊断复杂流程匹配
					 JSONObject jskey = new JSONObject(); 		 
					  jskey.put("data",bhlxz+"@#&"+shzt[1]);
					  String jsonts= matchPRKS23(dataClass,jskey.toString(),areaid);
					  
					  JSONObject joo=JSONObject.fromObject(jsonts);
					  String value=(String) joo.get("result");
					
					  
					  if("none".equals(value)){
						  String spec=specialPRKS23(dataClass,jskey.toString(),areaid);
						  JSONObject specoo=JSONObject.fromObject(spec);		  
						  if("1".equals(specoo.get("status"))){
							  json.put("SPEC", specoo.get("SPEC"));
							  String splue=(String) specoo.get("result");
							  if(!"spec".equals(value)){
								  String val=(String) json.get("TSBH");
								  json.put("TSBH", splue+","+val);
							  }
							 
						  }else{
							  json.put("status", "0");
						  }
					  }else{
						  if(!"spec".equals(value)){
							  String val=(String) json.get("TSBH");
							  json.put("TSBH", value+","+val);
						  }
						 
					  }
				 }

			 }else{
				    String bhType=matchRuleCacheMap.get("DATACODE"+"33");	
					String valueGen=matchRuleCacheMap.get("VALUE"+areaid+bhType);						
					String ssdvalue=(String) ssd.get(valueGen);
					String val=(String) json.get("TSBH");
					json.put("TSBH", ssdvalue+","+val);
			 }
		 }
		
		 
	}
	return json.toString();
}

}

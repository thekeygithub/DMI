package com.ts.service.mts;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.entity.mts.MtsConfigDetail;
import com.ts.entity.mts.MtsConfigRecord;
import com.ts.entity.mts.MtsConfigTest;
import com.ts.service.mts.matchrule.impl.DataConfigRecord;
import com.ts.service.mts.matchrule.impl.DataMtsConfigTest;
import com.ts.util.StringUtil;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

import net.sf.json.JSONObject;

@Service("MTSConfigPrks")
public class MTSConfigPrks {
	
	@Resource(name="MtsConfig")
	private MtsConfig mc;
	@Resource(name="DataMtsConfigTest")
	private DataMtsConfigTest dmct;
	@Resource(name = "DataConfigRecord")
	private DataConfigRecord dcr;
	@Resource(name = "redisUtil")
	private IRedisUtil redisDao;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mca;
	
	private  Map<String, String> matchRuleCacheMap ;
	
	
	public void prksConfigInto(String AREA,String classcode,MtsConfigDetail mct,String memid,Map<String, String> matchRuleCacheMap){
		this.matchRuleCacheMap=matchRuleCacheMap;
		
//		long begin=System.currentTimeMillis();	
		String	 SNAME=	 mct.getSOURCEDATA();
		String value=WMatchP(AREA,classcode,"{}",SNAME,memid);
		JSONObject temp=JSONObject.fromObject(value);
		String result="";
		String status="";
		String NLP="";
		String NlpValue="";
		String SPEC="";
		String doubt="";
		String TSBH="";
		
		try {
			result=temp.getString("result");
			status=temp.getString("status");
			if(temp.has("NLP")){
				NLP=temp.getString("NLP");
			}
			
			if(temp.has("NlpValue")){
				NlpValue=temp.getString("NlpValue");
			}
			
			if(temp.has("spNlp")&&"1".equals(status)){
				NLP=temp.getString("spNlp");
			}
			
			if(temp.has("spNlpValue")&&"1".equals(status)){
				NlpValue=temp.getString("spNlpValue");
			}
			
			if(temp.has("SPEC")){
				SPEC=temp.getString("SPEC");
			}			
			
			if(temp.has("doubt")){
				doubt=temp.getString("doubt");
			}
			if(temp.has("TSBH")){
				TSBH=temp.getString("TSBH");
			}			
			if(!"".equals(TSBH)){
				result=TSBH;
			}
			
			MtsConfigTest mcten=new MtsConfigTest();
			Date d=new Date();	
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String dx=df.format(d);
			mcten.setDOUB(doubt);
			mcten.setNLP(NLP);
			mcten.setNLPVALUE(NlpValue);
			mcten.setRESULT(result);
			mcten.setSPEC(SPEC);
			mcten.setTYPE(status);
			mcten.setT_ID(mct.getT_ID());
			mcten.setOPDATE(dx);
			dmct.editConfigTest(mcten);
			
			String demoArray[] = NLP.split("；");
			List<String> list = Arrays.asList(demoArray);
			
		    if(NLP==null||"".equals(NLP)){                //人工版切词为空，则输出--无抽词
		    	MtsConfigRecord mcr=new MtsConfigRecord();	
		    	if("0".equals(status)){                   //切词为空的，nlp填空
		    		mcr.setNLP_SNAME("");
		    		mcr.setNLP_ORDER(0);
					mcr.setT_ID(mct.getT_ID());
					mcr.setNLP_STYPE("不输出");
					mcr.setNLP_VNAME("");
					mcr.setNLP_VTYPE("无抽词");
					mcr.setNLP_STATUS("0");
					dcr.addConfigRecord(mcr);
		    	}else{                                    //直接匹配成功的，填原始词
		    		mcr.setNLP_SNAME(SNAME);  
		    		mcr.setNLP_ORDER(0);
		    		mcr.setT_ID(mct.getT_ID());
		    		mcr.setSPEC_TYPE(SPEC);
		    		mcr.setNLP_SVAL(result);
		    		
		    		if(result==null||"".equals(result)){
						
						
						mcr.setNLP_STYPE("不输出");
						mcr.setNLP_VNAME("其他-");
						mcr.setNLP_VTYPE("无法标化");
						mcr.setNLP_STATUS("1");
						dcr.addConfigRecord(mcr);
						
					}else{						
						String[] valArr= result.split(",");//NLP值数组
						JSONObject json=new JSONObject();
						json.put("xyFlag", 0);                  //西医入表标识，用来判断中医中的西医是否入表
						json.put("xyF", 0);
						json.put("zyF", 0);
						json.put("ssF", 0);
						for(int j=0;j<valArr.length;j++){
							String[] val=valArr[j].split("@#&");
							String valType=val[val.length-1];						
							//如果一个词对应多个结果，里面有无法标化的就不输出
							if(valArr.length>1&&"无法标化".equals(valType)){
								continue;
							}							
							json=recordInto(mcr,val,valType,json,j);
							
						}
						
					}
		    	}
				
				
				
		    }else{                                                  //NLP切词后匹配
						
				JSONObject js=JSONObject.fromObject(NlpValue);									
				

				for(int i=0;i<list.size();i++){	
					MtsConfigRecord mcr=new MtsConfigRecord();
					String name=list.get(i);//NLP名        --NLP_SNAME   i--NLP_ORDER
					String yname="";
					if(name.indexOf("【")!=-1){
						yname=name.substring(0,name.lastIndexOf("【"));
					}else{
						yname=name;
					}					
		
					String yval=js.getString(yname);
					
					mcr.setNLP_SNAME(name);
					mcr.setNLP_ORDER(i);				
					String[] specs=SPEC.split(",");
					mcr.setSPEC_TYPE(specs[i]);
					
					mcr.setNLP_SVAL(yval);
					mcr.setT_ID(mct.getT_ID());
					
					if(yval==null||"".equals(yval)){
						
						mcr.setRES_ORDER(0);
						mcr.setNLP_STYPE("不输出");
						mcr.setNLP_VNAME("其他-");
						mcr.setNLP_VTYPE("无法标化");
						mcr.setNLP_STATUS("1");
						dcr.addConfigRecord(mcr);
						
					}else{						
						String[] valArr= yval.split(",");//NLP值数组
						JSONObject json=new JSONObject();
						json.put("xyFlag", 0);                  //西医入表标识，用来判断中医中的西医是否入表
						json.put("xyF", 0);
						json.put("zyF", 0);
						json.put("ssF", 0);
						for(int j=0;j<valArr.length;j++){
							String[] val=valArr[j].split("@#&");
							String valType=val[val.length-1];						
							//如果一个词对应多个结果，里面有无法标化的就不输出
							if(valArr.length>1&&"无法标化".equals(valType)){
								continue;
							}							
							json=recordInto(mcr,val,valType,json,j);
							
						}
						
					}
					
				}
				
		    }
		    
		    
		  //剩余字符串处理20171123
			//1）原始数据中切除NLP字段中的字符后剩下的字符串。
			//2）剩余字符串如果不是连续的，则作为多个输出。
			//3）剩余字符串如果是连续的，则按字符串中的逗号、分号和句号分开，作为多个输出。如“双膝，右手，建议复查”，按逗号分开，输出的结果为“双膝”；“右手”；“建议复查”。
			//4）输出字符串时，当字符串前后带有不是成对出现的符号时，将其去除，如“（门诊慢性病），”在去掉符号后为门诊慢性病；如果字符串在括号中，则将括号去掉，如（10）要将括号去掉，变成10。
			String xANAME=SNAME;
			MtsConfigRecord mcr=new MtsConfigRecord();	
			if(list.size()>0&&"1".equals(status)){
				for(int k=0;k<list.size();k++){      
					String sname=list.get(k);
					String yname="";
					if(sname.indexOf("【")!=-1){
						yname=sname.substring(0,sname.lastIndexOf("【"));
					}else{
						yname=sname;
					}
					if(!"".equals(yname)&&yname!=null){
						
						int i=xANAME.indexOf(yname);
						if(i!=-1){
							xANAME=xANAME.substring(0, i)+"q|q"+xANAME.substring(i+yname.length());
						}else{
							//1、将原始词和匹配词去各种空格
							xANAME=mca.trimUnicode(xANAME);			
						    yname=mca.trimUnicode(yname);								
							String szm=yname.substring(0, 1);
							
							for(int t=0;t<=xANAME.length()-yname.length();t++){
								String subXname=xANAME.substring(t,t+yname.length());				
								String subSzm=subXname.substring(0, 1);
								//2、忽略各种符号后匹配
								String xyname=yname.replaceAll("[(（）),，;；。？?]", "");				
								subXname=subXname.replaceAll("[(（）),，;；。？?]", "");
								if(subXname.equals(xyname)&&subSzm.equals(szm)){
									xANAME=xANAME.substring(0, t)+"q|q"+xANAME.substring(t+yname.length());
									break;
								}
							}

						}
						
						
					}
				}
				String[] more=xANAME.split("q|q");
				int j=more.length;
				for(int a=0;a<more.length;a++){
					String morea=more[a].trim();
					if(morea==null||"".equals(morea)){
						continue;
					}else{
						
						if(morea.contains(",")||morea.contains("，")||morea.contains(".")){
							for(int b=0;b<morea.length();b++){
						    	String bi=String.valueOf(morea.charAt(b));
						    	if(",".equals(bi)||"，".equals(bi)){
						    		String biq="";
						    		if(b!=0){
						    			biq=String.valueOf(morea.charAt(b-1));
						    		}
						    		String bih="";
						    		if(b!=morea.length()-1){
						    			bih=String.valueOf(morea.charAt(b+1));
						    		}						    		
						    		boolean qWord=biq.matches("[A-Za-z0-9]");
						    		boolean hWord=bih.matches("[A-Za-z0-9]");
						    		if(qWord==false||hWord==false){
						    			morea=morea.substring(0, b)+";"+morea.substring(b+1);
						    		}
						    	}
						    }
						}
						String[] sss=morea.split(";|；|。");
						for(int c=0;c<sss.length;c++){
							String xss=sss[c];
							xss=mca.spec14(xss);
							xss=mca.kuoh(xss);
							if(!"".equals(xss)&&xss!=null){
								
								mcr.setNLP_SNAME(xss);
								mcr.setNLP_ORDER(j);			
								mcr.setT_ID(mct.getT_ID());
								mcr.setNLP_VTYPE("无法拆分");
								dcr.addConfigRecord(mcr);
								j=j+1;
							}
							
						}
						
					}
				}
			}
			
			

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	/**
	 * 人工切词用
	 * @param AREA
	 * @param classcode
	 * @param mct
	 * @param memid
	 * @param matchRuleCacheMap
	 */
	public void prksConfigInto_b(String AREA,String classcode,MtsConfigTest mct,String memid,Map<String, String> matchRuleCacheMap){
		this.matchRuleCacheMap=matchRuleCacheMap;
		
		JSONObject jskey = new JSONObject(); 
		String	 SNAME=	 mct.getSOURCEDATA();
		jskey.put("data","05@#&"+SNAME);
		JSONObject jsclass = new JSONObject(); 
		jsclass.put("dataType",classcode);
		String snlp=mct.getNLP();
		String nlp[] = snlp.split("；");
		List<String> nlist = Arrays.asList(nlp);
		String xnlp="";                                //对人工切词重新整理，去除【症状】、】、-、空格等不对的切词
		for(int t=0;t<nlist.size();t++){
			String name=nlist.get(t);//NLP名        --NLP_SNAME   i--NLP_ORDER
			String yname="";
			if(name.indexOf("【")!=-1){
				yname=name.substring(0,name.lastIndexOf("【"));
			}else{
				yname=name;
			}
			//排除空的人工切词中的空字符              --人工切词专用
			if(yname==null||"".equals(yname)){
				continue;
			}
			//排除【】-等纯字符                                   --人工切词专用
			boolean zf=yname.matches("[【】\\- ]*");
			if(zf==true){
				continue;
			}
			
			if("".equals(xnlp)){
				xnlp=name;
			}else{
				xnlp=xnlp+"；"+name;
			}
			
		}
		String value=mc.matchNlpPRKS23b(jsclass.toString(), jskey.toString(), AREA,xnlp);	
		JSONObject temp=JSONObject.fromObject(value);
		String outJson=mc.doubt(jsclass.toString(), jskey.toString(), AREA);
		JSONObject jtempin=JSONObject.fromObject(outJson);
		String sttpin=(String) jtempin.get("status");	
		if(sttpin=="1"||"1".equals(sttpin)){
			temp.put("doubt", jtempin.get("doubt"));
		}
		
		String result="";
		String status="";
		String NLP="";
		String NlpValue="";
		String SPEC="";
		String doubt="";
		String TSBH="";
		
		try {
			result=temp.getString("result");
			status=temp.getString("status");
			if(temp.has("NLP")){
				NLP=temp.getString("NLP");
			}
			
			if(temp.has("NlpValue")){
				NlpValue=temp.getString("NlpValue");
			}
			
			if(temp.has("SPEC")){
				SPEC=temp.getString("SPEC");
			}			
			
			if(temp.has("doubt")){
				doubt=temp.getString("doubt");
			}
			if(temp.has("TSBH")){
				TSBH=temp.getString("TSBH");
			}
			
			if(!"".equals(TSBH)){
				result=TSBH;
			}
			MtsConfigTest mcten=new MtsConfigTest();
			Date d=new Date();	
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
			String dx=df.format(d);
			if((NLP==null||"".equals(NLP))&&"0".equals(status)){
				mcten.setTYPE("1");
			}else{
				mcten.setTYPE(status);
			}
			mcten.setDOUB(doubt);
			mcten.setNLP(NLP);
			mcten.setNLPVALUE(NlpValue);
			mcten.setRESULT(result);
			mcten.setSPEC(SPEC);
			
			mcten.setT_ID(mct.getT_ID());
			mcten.setOPDATE(dx);
			dmct.editConfigTest(mcten);	
			
			String demoArray[] = NLP.split("；");
			List<String> list = Arrays.asList(demoArray);
			
		    if(NLP==null||"".equals(NLP)){                //人工版切词为空，则输出--无抽词
		    	MtsConfigRecord mcr=new MtsConfigRecord();	
		    	if("0".equals(status)){                   //切词为空的，nlp填空
		    		mcr.setNLP_SNAME("");
		    	}else{                                    //直接匹配成功的，填原始词
		    		mcr.setNLP_SNAME(SNAME);               
		    	}
				
				mcr.setNLP_ORDER(0);
				mcr.setT_ID(mct.getT_ID());				
				mcr.setNLP_STYPE("不输出");
				mcr.setNLP_VNAME("");
				mcr.setNLP_VTYPE("无抽词");
				mcr.setNLP_STATUS("1");
				dcr.addConfigRecord(mcr);
				
		    }else{                                                  //NLP切词后匹配

	
				JSONObject js=JSONObject.fromObject(NlpValue);									
				

				for(int i=0;i<list.size();i++){	
					MtsConfigRecord mcr=new MtsConfigRecord();
					String name=list.get(i);//NLP名        --NLP_SNAME   i--NLP_ORDER
					String yname="";
					if(name.indexOf("【")!=-1){
						yname=name.substring(0,name.lastIndexOf("【"));
					}else{
						yname=name;
					}					
		
					String yval=js.getString(yname);
					
					mcr.setNLP_SNAME(name);
					mcr.setNLP_ORDER(i);				
					String[] specs=SPEC.split(",");
					mcr.setSPEC_TYPE(specs[i]);
					
					mcr.setNLP_SVAL(yval);
					mcr.setT_ID(mct.getT_ID());
					
				
					
					String[] valArr= yval.split(",");//NLP值数组
					JSONObject json=new JSONObject();
					json.put("xyFlag", 0);                  //西医入表标识，用来判断中医中的西医是否入表
					json.put("xyF", 0);
					json.put("zyF", 0);
					json.put("ssF", 0);
					                  

					for(int j=0;j<valArr.length;j++){
						String[] val=valArr[j].split("@#&");
						String valType=val[val.length-1];						
						//如果一个词对应多个结果，里面有无法标化的就不输出
						if(valArr.length>1&&"无法标化".equals(valType)){
							continue;
						}
						

						json=recordInto(mcr,val,valType,json,j);
						
					}
					
				}
				
		    }
		    //剩余字符串处理20171123
			//1）原始数据中切除NLP字段中的字符后剩下的字符串。
			//2）剩余字符串如果不是连续的，则作为多个输出。
			//3）剩余字符串如果是连续的，则按字符串中的逗号、分号和句号分开，作为多个输出。如“双膝，右手，建议复查”，按逗号分开，输出的结果为“双膝”；“右手”；“建议复查”。
			//4）输出字符串时，当字符串前后带有不是成对出现的符号时，将其去除，如“（门诊慢性病），”在去掉符号后为门诊慢性病；如果字符串在括号中，则将括号去掉，如（10）要将括号去掉，变成10。
			String xANAME=SNAME;
			MtsConfigRecord mcr=new MtsConfigRecord();
			for(int k=0;k<list.size();k++){      
				String sname=list.get(k);
				String yname="";
				if(sname.indexOf("【")!=-1){
					yname=sname.substring(0,sname.lastIndexOf("【"));
				}else{
					yname=sname;
				}
				if(!"".equals(yname)&&yname!=null){
					
					int i=xANAME.indexOf(yname);
					if(i!=-1){
						xANAME=xANAME.substring(0, i)+"q|q"+xANAME.substring(i+yname.length());
					}else{
						//1、将原始词和匹配词去各种空格
						xANAME=mca.trimUnicode(xANAME);			
					    yname=mca.trimUnicode(yname);								
						
						for(int t=0;t<=xANAME.length()-yname.length();t++){
							String subXname=xANAME.substring(t,t+yname.length());				
							
							//2、忽略各种符号后匹配
							String xyname=yname.replaceAll("[(（）),，;；。？?]", "");				
							subXname=subXname.replaceAll("[(（）),，;；。？?]", "");
							
							String subSzm=subXname.substring(0, 1);
							String szm=xyname.substring(0, 1);
							if(subXname.equals(xyname)&&subSzm.equals(szm)){
								xANAME=xANAME.substring(0, t)+"q|q"+xANAME.substring(t+yname.length());
								break;
							}
						}

					}
					
					
				}
			}
			String[] more=xANAME.split("q|q");
			int j=more.length;
			for(int a=0;a<more.length;a++){
				String morea=more[a].trim();
				if(morea==null||"".equals(morea)){
					continue;
				}else{
					
					if(morea.contains(",")||morea.contains("，")||morea.contains(".")){
						for(int b=0;b<morea.length();b++){
					    	String bi=String.valueOf(morea.charAt(b));
					    	if(",".equals(bi)||"，".equals(bi)){
					    		String biq="";
					    		if(b!=0){
					    			biq=String.valueOf(morea.charAt(b-1));
					    		}
					    		String bih="";
					    		if(b!=morea.length()-1){
					    			bih=String.valueOf(morea.charAt(b+1));
					    		}						    		
					    		boolean qWord=biq.matches("[A-Za-z0-9]");
					    		boolean hWord=bih.matches("[A-Za-z0-9]");
					    		if(qWord==false||hWord==false){
					    			morea=morea.substring(0, b)+";"+morea.substring(b+1);
					    		}
					    	}
					    }
					}
					String[] sss=morea.split(";|；|。");
					for(int c=0;c<sss.length;c++){
						String xss=sss[c];
						xss=mca.spec14(xss);
						xss=mca.kuoh(xss);
						if(!"".equals(xss)&&xss!=null){
							
							mcr.setNLP_SNAME(xss);
							mcr.setNLP_ORDER(j);			
							mcr.setT_ID(mct.getT_ID());
							mcr.setNLP_VTYPE("无法拆分");
							dcr.addConfigRecord(mcr);
							j=j+1;
						}
						
					}
					
				}
			}
		    

		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	/**
	 * 匹配结果拆分后入MTS_CONFIG_RECORD纵表
	 * @param mcr 
	 * @param val
	 * @param valType
	 */
	public JSONObject recordInto(MtsConfigRecord mcr,String[] val,String valType,JSONObject json,int j){
		Pattern paZM = Pattern.compile("[A-Za-z]");
	    int xyF=(int) json.get("xyF");
	    int zyF=(int) json.get("zyF");
	    int ssF=(int) json.get("ssF");
	    int xyFlag=(int) json.get("xyFlag");
		
	    try {
		if("中医".equals(valType)){
			//肉瘤病@#&BWL030@#&@#&@#&@#&中医										
			if(zyF>0){
				mcr.setRES_ORDER(j);
			}
			mcr.setNLP_STYPE("中医");
			mcr.setNLP_VNAME(val[0]);
			mcr.setNLP_VCODE1(val[1]);
			mcr.setNLP_VCODE2("");
			mcr.setNLP_VTYPE("中医");
			mcr.setNLP_STATUS("1");
			dcr.addConfigRecord(mcr);
			String zyCode=val[1];
			int hg=zyCode.indexOf("-");
			if(hg!=-1){
				String str=zyCode.substring(hg+1);				
				boolean isWord=str.matches("[a-zA-Z]+");
				if(isWord==true){
					mcr.setNLP_VCODE1(zyCode.substring(0, hg));
					mcr.setNLP_VTYPE("卫生部中医");
					dcr.addConfigRecord(mcr);
				}
				
			}else{
				mcr.setNLP_VTYPE("卫生部中医");			
				dcr.addConfigRecord(mcr);
			}
			zyF=zyF+1;
			
			
			String xy=val[2];
			if(!(xy==null||"".equals(xy))&&xyFlag==0){											
				
                if(xyF>0){
                	mcr.setRES_ORDER(j);
                }
				mcr.setNLP_VNAME(val[2]);
				mcr.setNLP_VCODE1(val[3]);
				mcr.setNLP_VCODE2(val[4]);
				mcr.setNLP_VTYPE("诊断");
				mcr.setNLP_STATUS("1");
				dcr.addConfigRecord(mcr);
				
				String code="";
				if(!"".equals(val[3])&&val[3]!=null){
					code=val[3];
				}else{
					code=val[4];
				}
				int cnt=mca.countInnerStr(code);
				if(cnt!=8){
					mcr.setNLP_VTYPE("卫生部西医");	
					dcr.addConfigRecord(mcr);
				}else{
					String xCode=code.substring(0,7);
					String inkeyL=StringUtil.full2Half(xCode).toUpperCase().trim();
					String key="410205#02#09#"+inkeyL;		
					String value=redisDao.get(RedisKeys.SYSMTS, key);
					mcr.setNLP_VNAME(value);
					mcr.setNLP_VCODE1(xCode);
					mcr.setNLP_VCODE2("");
					mcr.setNLP_VTYPE("卫生部西医");
					dcr.addConfigRecord(mcr);
				}
			}
			
			xyFlag=0;
			xyF=xyF+1;
			
		}else if("概念转换".equals(valType)){
			//混合痔@;&I84.201@;&@#&特指手术后状态@;&Z98.800@;&@#&@#&@#&@#&@#&@#&概念转换	
		
		
			mcr.setNLP_STYPE("概念转换");
			for(int d=0;d<val.length-1;d++){
				String[] valg=val[d].split("@;&");					
				if(valg.length>1){
					int op=0;	                      // 0代表手术  1代表诊断
					String beg="";
					if(valg[1]==null||"".equals(valg[1])){
						beg=valg[2].substring(0, 1);
					}else{
						beg=valg[1].substring(0, 1);
					}
					Matcher zmMt=paZM.matcher(beg);
					if(zmMt.find()){
						op=1;
					}
					if(op==1){
							
						mcr.setNLP_VNAME(valg[0]);
						mcr.setNLP_VCODE1(valg[1]);
						if(valg.length>2){
							mcr.setNLP_VCODE2(valg[2]);
						}
						mcr.setRES_ORDER(d+10);
						mcr.setNLP_VTYPE("诊断");
						mcr.setNLP_STATUS("1");
						dcr.addConfigRecord(mcr);
						
						String code="";
						if(!"".equals(valg[1])&&valg[1]!=null){
							code=valg[1];
						}else{
							code=valg[2];
						}
						int cnt=mca.countInnerStr(code);
						if(cnt!=8){
							mcr.setNLP_VTYPE("卫生部西医");	
							dcr.addConfigRecord(mcr);
						}else{
							String xCode=code.substring(0,7);
							String inkeyL=StringUtil.full2Half(xCode).toUpperCase().trim();
							String key="410205#02#09#"+inkeyL;		
							String value=redisDao.get(RedisKeys.SYSMTS, key);
							mcr.setNLP_VNAME(value);
							mcr.setNLP_VCODE1(xCode);
							mcr.setNLP_VCODE2("");
							mcr.setNLP_VTYPE("卫生部西医");
							dcr.addConfigRecord(mcr);
						}
						xyF=xyF+1;
						
					}else{														
						
						
						mcr.setNLP_VNAME(valg[0]);
						mcr.setNLP_VCODE1(valg[1]);	
						mcr.setNLP_VCODE2("");
						mcr.setNLP_VTYPE("手术");
						mcr.setRES_ORDER(d+10);
						mcr.setNLP_STATUS("1");
						dcr.addConfigRecord(mcr);
						mcr.setNLP_VTYPE("卫生部手术");
						dcr.addConfigRecord(mcr);
						
						ssF=ssF+1;
				    }
				}
				
			}						
			
			
		}else if("".equals(valType)){//不予输出的
			
			mcr.setNLP_STYPE("不输出");
			mcr.setNLP_VNAME("其他");
			mcr.setNLP_VCODE1("");
			mcr.setNLP_VCODE2("");
			mcr.setNLP_VTYPE("无法标化");
			mcr.setNLP_STATUS("1");
			dcr.addConfigRecord(mcr);
			
		}else if("诊断".equals(valType)){											
			
			if(xyF>0){
            	mcr.setRES_ORDER(j);
            }
			mcr.setNLP_STYPE("诊断");
			mcr.setNLP_VNAME(val[0]);
			mcr.setNLP_VCODE1(val[1]);
			mcr.setNLP_VCODE2(val[2]);
			mcr.setNLP_VTYPE("诊断");
			mcr.setNLP_STATUS("1");
			dcr.addConfigRecord(mcr);
			
			String code="";
			if(!"".equals(val[1])&&val[1]!=null){
				code=val[1];
			}else{
				code=val[2];
			}
			int cnt=mca.countInnerStr(code);
			if(cnt!=8){
				mcr.setNLP_VTYPE("卫生部西医");	
				dcr.addConfigRecord(mcr);
			}else{
				String xCode=code.substring(0,7);
				String inkeyL=StringUtil.full2Half(xCode).toUpperCase().trim();
				String key="410205#02#09#"+inkeyL;	
				String value=redisDao.get(RedisKeys.SYSMTS, key);
				mcr.setNLP_VNAME(value);
				mcr.setNLP_VCODE1(xCode);
				mcr.setNLP_VCODE2("");
				mcr.setNLP_VTYPE("卫生部西医");
				dcr.addConfigRecord(mcr);
			}
			xyFlag=1;
			xyF=xyF+1;
		}else if("手术".equals(valType)){											
			
			if(ssF>0){
				mcr.setRES_ORDER(j);
			}
			mcr.setNLP_STYPE("手术");
			mcr.setNLP_VNAME(val[0]);
			mcr.setNLP_VCODE1(val[1]);	
			mcr.setNLP_VCODE2("");
			mcr.setNLP_VTYPE("手术");
			mcr.setNLP_STATUS("1");
			dcr.addConfigRecord(mcr);
			mcr.setNLP_VTYPE("卫生部手术");
			dcr.addConfigRecord(mcr);
			ssF=ssF+1;
			
		}else if("无法标化".equals(valType)){											
			
			mcr.setNLP_STYPE("无法标化");
			mcr.setNLP_VNAME(val[0]);
			mcr.setNLP_VCODE1("");
			mcr.setNLP_VCODE2("");
			mcr.setNLP_VTYPE("无法标化");
			mcr.setNLP_STATUS("1");
			dcr.addConfigRecord(mcr);
			
		}else if("肿瘤部位对照".equals(valType)){											
			
			
			mcr.setNLP_STYPE("肿瘤部位对照");
			int op=0;	                      // 0代表手术  1代表诊断
			String beg=val[1].substring(0, 1);											
			Matcher zmMt=paZM.matcher(beg);
			if(zmMt.find()){
				op=1;
			}
			
			if(op==1){
				if(xyF>0){
                	mcr.setRES_ORDER(j);
                }
				mcr.setNLP_VNAME(val[0]);
				mcr.setNLP_VCODE1(val[1]);	
				mcr.setNLP_VCODE2("");
				mcr.setNLP_VTYPE("诊断");
				mcr.setNLP_STATUS("1");
				dcr.addConfigRecord(mcr);
				
				String code=val[1];         //肿瘤部位对照的诊断码肯定在第一位，所以在此code不用判断直接赋值
				
				int cnt=mca.countInnerStr(code);
				if(cnt!=8){
					mcr.setNLP_VTYPE("卫生部西医");	
					dcr.addConfigRecord(mcr);
				}else{
					String xCode=code.substring(0,7);
					String inkeyL=StringUtil.full2Half(xCode).toUpperCase().trim();
					String key="410205#02#09#"+inkeyL;		
					String value=redisDao.get(RedisKeys.SYSMTS, key);
					mcr.setNLP_VNAME(value);
					mcr.setNLP_VCODE1(xCode);
					mcr.setNLP_VCODE2("");
					mcr.setNLP_VTYPE("卫生部西医");
					dcr.addConfigRecord(mcr);
				}
				xyF=xyF+1;
			}else{
				
				if(ssF>0){
					mcr.setRES_ORDER(j);
				}
				mcr.setNLP_VNAME(val[0]);
				mcr.setNLP_VCODE1(val[1]);
				mcr.setNLP_VCODE2("");
				mcr.setNLP_VTYPE("手术");
				mcr.setNLP_STATUS("1");
				dcr.addConfigRecord(mcr);
				mcr.setNLP_VTYPE("卫生部手术");
				dcr.addConfigRecord(mcr);
				
				ssF=ssF+1;
			}
		}else if("none".equals(valType)&&val.length>1){	//专为本次准备，术后转移的标识是none										
			
			if(xyF>0){
            	mcr.setRES_ORDER(j);
            }
			mcr.setNLP_STYPE("术后转移");
			mcr.setNLP_VNAME(val[0]);
			mcr.setNLP_VCODE1(val[1]);	
			mcr.setNLP_VCODE2("");
			mcr.setNLP_VTYPE("诊断");
			mcr.setNLP_STATUS("1");
			dcr.addConfigRecord(mcr);
			
			String code=val[1];
			
			int cnt=mca.countInnerStr(code);
			if(cnt!=8){
				mcr.setNLP_VTYPE("卫生部西医");	
				dcr.addConfigRecord(mcr);
			}else{
				String xCode=code.substring(0,7);
				String inkeyL=StringUtil.full2Half(xCode).toUpperCase().trim();
				String key="410205#02#09#"+inkeyL;		
				String value=redisDao.get(RedisKeys.SYSMTS, key);
				mcr.setNLP_VNAME(value);
				mcr.setNLP_VCODE1(xCode);
				mcr.setNLP_VCODE2("");
				mcr.setNLP_VTYPE("卫生部西医");
				dcr.addConfigRecord(mcr);
			}
			xyF=xyF+1;
			
		}else if("none".equals(valType)&&val.length==1){//不予输出的
			
			mcr.setNLP_STATUS("0");
			dcr.addConfigRecord(mcr);
			
		}
	    } catch (Exception e) {			
			e.printStackTrace();
		}
	    
	   
		json.put("xyFlag", xyFlag);                  //西医入表标识，用来判断中医中的西医是否入表
		json.put("xyF", xyF);
		json.put("zyF", zyF);
		json.put("ssF", ssF);
	    return json;
	}
	
	
	/***
	 * DETAIL外部流程
	 * @param areaid  区域
	 * @param classcode  聚类
	 * @param bhlxx   新标化类型
	 * @param jsonarr  新key串
	 * @param jsonarr  拼key标识
	 * @param jsonarr  原始key
	 * @param jsonarr  流程ID
	 * @return
	 */
	public String WMatchP(String areaid,String classcode,String jsonarr,String inkey,String memid){
		
		
		String app=matchRuleCacheMap.get("WAPP"+memid);
		String fallto=matchRuleCacheMap.get("WFAILTO"+memid);
		String succto=matchRuleCacheMap.get("WSUCCTO"+memid);
		String bhlx=matchRuleCacheMap.get("WXBHLX"+memid);
		String mark=matchRuleCacheMap.get("WRESUL"+memid);
		
		JSONObject oValue=new JSONObject();
		oValue.put("status", "0");
		oValue.put("result", "none");
		String oldValue=oValue.toString();
		if(!"{}".equals(jsonarr)){
			
			oldValue=jsonarr;
		}		
		String value="";		
		JSONObject jskey = new JSONObject(); 		 
		jskey.put("data",bhlx+"@#&"+inkey);
		JSONObject jsclass = new JSONObject(); 
		jsclass.put("dataType",classcode);
		
		
		if("matchPRKS".equals(app)){
			
			String outJson=mc.matchPRKS(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				oldValue=	outJson;	
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else{
				value=oldValue;
			}
		}else if("match".equals(app)){
			
			String outJson=mc.match(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				oldValue=	outJson;	
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else{
				value=oldValue;
			}
		}else if("matchNlp".equals(app)){
			
			String outJson=mc.matchNlp(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			oldValue=outJson;		
								
					

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("matchNlpPRKS".equals(app)){
			
			String outJson=mc.matchNlpPRKS(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			oldValue=outJson;		
								
					

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
		}else if("special".equals(app)){
			
			String outJson=mc.special(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("doubt".equals(app)){
			
			String outJson=mc.doubt(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			JSONObject dbjtemp=JSONObject.fromObject(oldValue);
			if(sttpin=="1"||"1".equals(sttpin)){
				
				dbjtemp.put("doubt", jtempin.get("doubt"));
				oldValue=dbjtemp.toString();		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("inerver".equals(app)){
			
			String outJson=mc.inerver(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				JSONObject dbjtemp=JSONObject.fromObject(oldValue);	
				dbjtemp.put("result", "无法标化");
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("matchPRKS23".equals(app)){
			
			String outJson=mc.matchPRKS23(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("matchNlpPRKS23".equals(app)){
			
			String outJson=mc.matchNlpPRKS23(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			oldValue=outJson;			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("specialPRKS23".equals(app)){
			
			String outJson=mc.specialPRKS23(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("tsckZL".equals(app)){
			
			String outJson=mc.tsckZL(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("tsckSHZT".equals(app)){
			
			String outJson=mc.tsckSHZT(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}else if("specialPRKS".equals(app)){
			
			String outJson=mc.specialPRKS(jsclass.toString(), jskey.toString(), areaid);
			JSONObject jtempin=JSONObject.fromObject(outJson);
			String sttpin=(String) jtempin.get("status");
			
			if(sttpin=="1"||"1".equals(sttpin)){
				
				oldValue=outJson;		
								
			}			

			if(fallto!=null && !"".equals(fallto) && "0".equals(sttpin)){				
				
				value=WMatchP(areaid,classcode,oldValue,inkey,fallto);
				
			}else if(succto!=null && !"".equals(succto) && "1".equals(sttpin)){
				
				value=WMatchP(areaid,classcode,oldValue,inkey,succto);
			}else {
				value=oldValue;
			}	
			
		}
		
		
		
		return value;
	}

}

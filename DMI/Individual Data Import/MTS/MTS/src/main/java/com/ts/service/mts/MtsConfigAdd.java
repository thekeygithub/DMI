package com.ts.service.mts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import com.ts.util.StringUtil;

@Service("MtsConfigAdd")
public class MtsConfigAdd {
	
	
	
	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	private static Logger logger = Logger.getLogger(MtsConfigAdd.class);
	private  Map<String, String> matchRuleCacheMap ;// 匹配规则缓存容器	
	private  List<String> listend=new ArrayList<>();//限定词和非医学术语组合列表
	
	/**
	 * 规则化要匹配的内容，如去空格，无效字符，无用术语等，易保第一版
	 * @param str
	 * @return
	 */
	public String standarde(String str){
		
		if(str == null || "".equals(str)){
			return "";
		}
		String bzstr=str;		
		String[] arrs={"\"",".","。","、","，",",","'","‘","?","？","!","！","“","\\","/","|",";","；",":","："};
		
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		String termsStrs = matchRuleCacheMap.get("termsStrs"); 
		String inverStrs= matchRuleCacheMap.get("inverStrs"); 
		
		//1、转换半角
		//bzstr=StringUtil.full2Half(bzstr);
        
		
		//2、去非医学术语		
		Pattern p = Pattern.compile(termsStrs);
		Matcher m = p.matcher(bzstr);
		while (m.find()) {
			String term = m.group(1);
			bzstr = bzstr.replace(term, "");			
		}
		
		//3、去无法干预数据	
		Pattern pi = Pattern.compile(inverStrs);
		Matcher mi = pi.matcher(bzstr);
		while (mi.find()) {
			String termi = mi.group(1);
			bzstr = bzstr.replace(termi, "");			
		}
		
		//4、去（）中无中文的串。例如(C01.053)、(8)
		
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
		
        
		//5.去数字＋字符组合
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
								bzstr = bzstr.replace(ats+zf, "");	
							}
					    	
					    }							
					}
			}
		}
		
			

        //6.去空格tab等
		bzstr=bzstr.replaceAll("\\s*", "");  
		
		//7.去无用符号
		for(int j=0;j<arrs.length;j++){
			if(bzstr.contains(arrs[j])){
				bzstr=bzstr.replace(arrs[j], "");
			}
		}
		
		bzstr=bzstr.trim();
		if(bzstr==null||bzstr==""||"".equals(bzstr)){
			return "";
		}
		logger.info("标化后的术语为："+bzstr);
		return bzstr;
		
	}
	
    /**
     * 判断药品能否进入联合字段
     */
	
	public String ifZX(String str){
		
		String zy=str.split("@#&")[0];
		String fenl=str.split("@#&")[1];		
		if("Y".equals(zy)||"Z".equals(zy)){
			return "0";
		}
		if(fenl=="药品大名称"){
			return "0";
		}
		return "1";
	}
	
	
	/**
     * 多个药品名的选择
     */
	
	public String drugName(List<String> strs){
		
		Map<String,String> map=new HashMap<String,String>();
		String name="";
		
		for(int i=0;i<strs.size(); i++){
			String fenl=strs.get(i).split("@#&")[1];
			if(fenl.contains("标准产品名")){
				if(map.get("标准产品名")!=null){
					return "";
				}
				map.put("标准产品名", strs.get(i));
			}else if(fenl.contains("标准通用名")){
				if(map.get("标准通用名")!=null){
					return "";
				}
				map.put("标准通用名", strs.get(i));
				
			}else if(fenl.contains("标准商品名")){
				if(map.get("标准商品名")!=null){
					return "";
				}
				map.put("标准商品名", strs.get(i));
			}
		}
		
		if(map.get("标准产品名")!=null){
			
			name=map.get("标准产品名");
			
		}else if(map.get("标准通用名")!=null){
			
			name=map.get("标准通用名");
			
		}else if(map.get("标准商品名")!=null){
			
			name=map.get("标准商品名");
			
		}
		return name;
	}
	
	
	/**
	 * 去各种空格
	 * @param ss
	 * @return
	 */
	public String trimUnicode(String ss){
		String regEx = "[\ua000-\ufde0]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(ss);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			matcher.appendReplacement(sb, "");
		}
		 matcher.appendTail(sb);	


  	  	return sb.toString();   	  
 	  	
	}

	
	
	/**
	 *  去疾病限定词
	 * @param ss
	 * @return
	 */
	public String spec11_o(String ss){
		
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		String specialStrs = matchRuleCacheMap.get("specialStrs"); 
		
		String datavalue=ss;
		Pattern p = Pattern.compile(specialStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {	
			String term = m.group(1);
			datavalue=datavalue.replace(term, "");		
		}
		return datavalue;
	}
	
	/**
	 *  抽取疾病限定词列表
	 * @param ss
	 * @return
	 */
	public List<String> spec11(String ss){
		
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		String specialStrs = matchRuleCacheMap.get("specialStrs"); 
		List<String> list=new ArrayList<String>();
		String datavalue=ss;
		Pattern p = Pattern.compile(specialStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {	
			String term = m.group(1);
			list.add(term);					
		}
		if(list.size()!=0){
			String[] toBeStored = list.toArray(new String[list.size()]);    
			combiantion(toBeStored); 	
		}
		 
		List<String> listspec=new ArrayList<String>();
		if(listend.size()!=0){
			listspec.addAll(listend);
		}
		
		listend.clear();
		return listspec;
	}

			
	
	/**
	 * 去数字字母符号逻辑
	 * @param ss
	 * @return
	 */
	public String spec12(String ss){
		
		String datavalue=ss;	
		Pattern paZM = Pattern.compile("[A-Za-z]");  //字母
		Pattern paSZ = Pattern.compile("[0-9]");       //数字
		String FH="@+-.。、，,";
		
		//1、单字母
		Matcher zmMt=paZM.matcher(datavalue);
		if(zmMt.find()){
			for(int i=0;i<datavalue.length();i++){						
				 char at=datavalue.charAt(i);
				  String ats=String.valueOf(at); 						 
				  Matcher xzmMt=paZM.matcher(ats);
				  if(xzmMt.find()){	
					  String atq="";
					  String ath="";
					  if(i==0){                  //判断字母如果是首位，默认为前一个字符符合条件
						  atq="非" ;
					  }else{
					   atq=String.valueOf(datavalue.charAt(i-1));
					  }					  
					  Matcher xzmMtq=paZM.matcher(atq);
					  Matcher xszMtq=paSZ.matcher(atq);
					  
					  if(i==datavalue.length()-1){ //判断字母是末位，默认为后一个字符符合条件
						  ath="非" ;
					  }else{
						  ath=String.valueOf(datavalue.charAt(i+1)); 								 
					  }					 
					  Matcher xzmMth=paZM.matcher(ath);
					  Matcher xszMth=paSZ.matcher(ath);
					  
					  if(FH.contains(atq)||FH.contains(ath)||xszMtq.find()||xszMth.find()||xzmMth.find()||xzmMtq.find()){
						  continue;
					  }else{
						  String qq=datavalue.substring(0, i);
						  String hh=datavalue.substring(i+1);
						  datavalue=qq+hh;								 
					  }
				  }
			}
		}
		
		
		//2、数字和符号
		Matcher szMt2=paSZ.matcher(datavalue);
		if(szMt2.find()){
			for(int i=0;i<datavalue.length();i++){
				 char at=datavalue.charAt(i);
				  String ats=String.valueOf(at);  				 
				  Matcher xszMt=paSZ.matcher(ats);
				  if(xszMt.find()&&i!=datavalue.length()-1){
					  
					  String ath=String.valueOf(datavalue.charAt(i+1));
					  String athh="";
					  if(i+1!=datavalue.length()-1){
						  athh=String.valueOf(datavalue.charAt(i+2));		
					  }else{
						  athh="非" ;
					  }
					  Matcher xszMth=paSZ.matcher(athh);
					  Matcher xzmMth=paZM.matcher(athh);
					  if(FH.contains(ath)&&!(xszMth.find()||xzmMth.find()||FH.contains(athh))){
						  String qq=datavalue.substring(0, i);
						  String hh=datavalue.substring(i+2);
						  datavalue=qq+hh;								
					  }
				  }
			}
		}
		
		//3、数字和字母组合(数字和符号和字母组合)
		Matcher szMt3=paSZ.matcher(datavalue);
		Matcher zmMt3=paZM.matcher(datavalue);
		if(zmMt3.find()||szMt3.find()||FH.contains(datavalue)){
			for(int i=0;i<datavalue.length();i++){
				 char at=datavalue.charAt(i);
				  String ats=String.valueOf(at);  				 
				  Matcher xszMt=paSZ.matcher(ats);
				  Matcher xzmMt=paZM.matcher(ats);
				  
				  if((xszMt.find()||xzmMt.find()||FH.contains(ats))&&i!=datavalue.length()-1){
					  int q=0;
					  int h=0;							  
					  for(int j=i+1;j<datavalue.length();j++){
						  String ath=String.valueOf(datavalue.charAt(j));	
						  
						  Matcher xszMt3=paSZ.matcher(ath);
						  Matcher xzmMt3=paZM.matcher(ath);
						  if(xszMt3.find()||xzmMt3.find()||FH.contains(ath)){
							  if(j==datavalue.length()-1){
								  q=i;
								  h=j+1;  
							  }
							  continue;
						  }else{									  
							  q=i;
							  h=j;
							  break;
						  }
					  }
					  				 				 
					  String cd=datavalue.substring(q, h);							  
					  if(cd.length()>1){	
						 String qq=datavalue.substring(0, q);
						 String hh=datavalue.substring(h);
						 datavalue=qq+hh;	
					  }
				  }
			}
		}
		
		
		if("".equals(datavalue)||datavalue==null){
			return "";
		}else{
			datavalue=datavalue.trim();
		}
		return datavalue;
	}
	
	
	/**
	 * 带括号的处理逻辑
	 * @param ss
	 * @return
	 */
	public List<String> spec13(String ss){
		
		String datavalue=ss;	
		int Inte=-1;
		int Inde=-1;
		String wz="";
		List<String> list=new ArrayList<String>();
		
		//术语前后的括号内外匹配：包括小括号、中括号；		
		 if("".equals(datavalue)||datavalue==null){
			 return list;
		 }
		 String qq=datavalue.substring(0, 1);		 
		 if("(".equals(qq)||"（".equals(qq)||"[".equals(qq)||"【".equals(qq)){
			 for(int j=1;j<datavalue.length();j++){
				  String ath=String.valueOf(datavalue.charAt(j));
				  if(")".equals(ath)||"）".equals(ath)||"]".equals(ath)||"】".equals(ath)){
					  Inte=0;
					  Inde=j;
					  break;
				  }			  
			 }
			 if(Inte+1<Inde){
				  String khnr=datavalue.substring(Inte+1, Inde);				  
				  list.add(khnr);
				  wz="1";				  
				  datavalue=datavalue.substring(Inde+1);
				  Inte=-1;
				  Inde=-1;			  
			  }
		 }
		 
		 if("".equals(datavalue)||datavalue==null){
			 return list;
		 }
		 String hh=datavalue.substring(datavalue.length()-1);		 
		 if(")".equals(hh)||"）".equals(hh)||"]".equals(hh)||"】".equals(hh)){
			 for(int j=datavalue.length()-1;j>0;j--){
				  String ath=String.valueOf(datavalue.charAt(j));
				  if("(".equals(ath)||"（".equals(ath)||"[".equals(ath)||"【".equals(ath)){
					  Inte=j;
					  Inde=datavalue.length()-1;
					  break;
				  }			  
			 }
			 if(Inte+1<Inde){
				  String khnr=datavalue.substring(Inte+1,Inde);	
				  datavalue=datavalue.substring(0,Inte);
				 
				  if(wz==""||"".equals(wz)){
					  wz="0"; 
				  }else{
					  wz="1"; 
				  }
				  list.add(datavalue);
				  list.add(khnr);
				 
				  				  
			  }
		 }
		 
		 if("1".equals(wz)&&list.size()==1){
			 list.add(datavalue);
		 }		 
		 
		 list.add(wz);
		
		return list;
	}
	
	
	/**
	 *带斜杠的处理逻辑 
	 * @param ss
	 * @return
	 */
	public List<String> spec16(String ss){
		
		String datavalue=ss;		
		int Inte=-1;
		List<String> list=new ArrayList<String>();		
		
		for(int i=0;i<datavalue.length();i++){
			 char at=datavalue.charAt(i);
			  String ats=String.valueOf(at);
			  if("/".equals(ats)){
				  Inte=i;				 
			  }
			  if(Inte!=-1&&Inte!=0&&Inte!=datavalue.length()){
				  String khnr=datavalue.substring(0, Inte);
				  list.add(khnr);
				  datavalue=datavalue.substring(Inte+1);
				  Inte=-1;						
			  }
		}
			
		list.add(datavalue);
		
		return list;
	}

	
	/**
	 * 标化字符串处理逻辑
	 * @param ss
	 * @return
	 */
	public String spec14(String ss){
		
		String bzstr=ss;
		bzstr=bzstr.trim();
		if("".equals(bzstr)||bzstr==null){
			return "";
		}
	  //去左端（）中为空的串。
		String sff=bzstr.substring(0,1);		
		if("(".equals(sff)||"（".equals(sff)||"<".equals(sff)){
			int g=0;
			for(int i=1;i<bzstr.length();i++){
				char bad=bzstr.charAt(i);
				if(bad==')'||bad=='）'||bad=='>'){
					String cd=bzstr.substring(1 , i);
					cd =trimUnicode(cd);
					if(cd==""||"".equals(cd)){
						bzstr=bzstr.substring(i+1,bzstr.length());
						g=1;
						break;
					}
				}
			}
			if(g==1){
				bzstr=spec14(bzstr); 
			}
		}
		//去右端（）中为空的串。
		String sdd="";
		if(bzstr.length()>1){
			sdd=bzstr.substring(bzstr.length()-1);
		}				
		if(")".equals(sdd)||"）".equals(sdd)||">".equals(sdd)){
			int g=0;
			for(int k=bzstr.length()-2;k>0;k--){
				char bac=bzstr.charAt(k);
				if(bac=='('||bac=='（'||bac=='<'){
					String cd=bzstr.substring(k+1,bzstr.length()-1);
					cd =trimUnicode(cd);
					if(cd==""||"".equals(cd)){
						bzstr=bzstr.substring(0,k);
						g=1;
						break;
					}
				}
			}
			if(g==1){
				bzstr=spec14(bzstr); 
			}
		}
		  	if("".equals(bzstr)||bzstr==null){
				return "";
			}else{
				bzstr=bzstr.trim();
			}
	          

	  		
	  		String dd="none";
	  		String ff="none";
	  		String arr="\".。、，,'‘?？!！“\\/|;；:：·*^-";  		
	  		if(bzstr.length()>0){
	  			dd=bzstr.substring(bzstr.length()-1);
	  			ff=bzstr.substring(0,1);
	  		}	  		

	        
	  		if(arr.contains(dd)&&bzstr.length()>0){  			
	  			bzstr=bzstr.substring(0,bzstr.length()-1);
	  	  		bzstr=spec14(bzstr);  			
	  		}else if(arr.contains(ff)&&bzstr.length()>0){  			
	  			bzstr=bzstr.substring(1,bzstr.length());
	  			bzstr=spec14(bzstr);  			
	  		}

	  		
	  		if("".equals(bzstr)||bzstr==null){
	  			return "";
	  		}else{
	  			bzstr=bzstr.trim();
	  		}
	  		
	  		
	  		return bzstr;
		
	}

	
	/**
	 * 去非医学术语
	 * @param ss
	 * @return
	 */
	public String spec15_o(String ss){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		String termsStrs = matchRuleCacheMap.get("termsStrs"); 
		String bzstr=ss;
		
		List<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile(termsStrs);
		Matcher m = p.matcher(StringUtil.full2Half(bzstr).toUpperCase());
		while (m.find()) {
			String term = m.group(1);
			list.add(term);
		}
		String[] arrStr = new String[list.size()];
		
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				arrStr[i]=list.get(i);
			}
			arrStr=sortList(arrStr);
			bzstr=bzstr.replace(arrStr[0], "");
		}
		
		return bzstr;
	}
	
	/**
	 * 抽出非医学术语列表
	 * @param ss
	 * @return
	 */
	public List<String> spec15(String ss){
		matchRuleCacheMap = mcs.getMatchRuleCacheMap();
		String termsStrs = matchRuleCacheMap.get("termsStrs"); 
		String bzstr=ss;
		
		List<String> list=new ArrayList<String>();
		Pattern p = Pattern.compile(termsStrs);
		Matcher m = p.matcher(StringUtil.full2Half(bzstr).toUpperCase());
		while (m.find()) {
			String term = m.group(1);
			list.add(term);
		}
		if(list.size()!=0){
			String[] toBeStored = list.toArray(new String[list.size()]);    
			combiantion(toBeStored); 
		}
		 
		List<String> listnoterm=new ArrayList<String>();
		if(listend.size()!=0){
			listnoterm.addAll(listend);
		}
		
		listend.clear();
		return listnoterm;
	}
	
	/**
	 * 排序
	 * @param arrStr
	 * @return
	 */
	public  String[] sortList(String[] arrStr) {    
		String temp;
	    for (int i = 0; i < arrStr.length; i++) {
	        for (int j = arrStr.length - 1; j > i; j--) {
	            if (arrStr[j].length() >= arrStr[i].length()) {
	                temp = arrStr[i];
	                arrStr[i] = arrStr[j];
	                arrStr[j] = temp;
	            }
	        }
	    }
		return arrStr;  
	}

	
	/**
	 * 数据预处理
	 * @param ss
	 * @return
	 */
	public String firstInto(String ss){
		
		String ycl=trimUnicode(StringUtil.full2Half(ss).toUpperCase().trim());
		
		 //去左端（）中为空的串。
		if("".equals(ycl)||ycl==null){
			return "";
		}
		String sff=ycl.substring(0,1);		
		if("(".equals(sff)||"（".equals(sff)||"<".equals(sff)){
			int g=0;
			for(int i=1;i<ycl.length();i++){
				char bad=ycl.charAt(i);
				if(bad==')'||bad=='）'||bad=='>'){
					String cd=ycl.substring(1 , i);
					cd =trimUnicode(cd);
					if(cd==""||"".equals(cd)){
						ycl=ycl.substring(i+1,ycl.length());
						g=1;
						break;
					}
				}
			}
			if(g==1){
				ycl=firstInto(ycl); 
			}
		}
		//去右端（）中为空的串。
		String sdd="";
		if(ycl.length()>1){
			sdd=ycl.substring(ycl.length()-1);
		}				
		if(")".equals(sdd)||"）".equals(sdd)||">".equals(sdd)){
			int g=0;
			for(int k=ycl.length()-2;k>0;k--){
				char bac=ycl.charAt(k);
				if(bac=='('||bac=='（'||bac=='<'){
					String cd=ycl.substring(k+1,ycl.length()-1);
					cd =trimUnicode(cd);
					if(cd==""||"".equals(cd)){
						ycl=ycl.substring(0,k);
						g=1;
						break;
					}
				}
			}
			if(g==1){
				ycl=firstInto(ycl); 
			}
		}
	  	if("".equals(ycl)||ycl==null){
			return "";
		}else{
			ycl=ycl.trim();
		}
	  	
		  	
		String dd="none";
		String ff="none";
		String arr="\".。、，,'‘?？!！“\\/|;；:：·*^";
		if(ycl.length()>0){
			dd=ycl.substring(ycl.length()-1);
			ff=ycl.substring(0,1);
		}
		
		if(arr.contains(dd)&&ycl.length()>0){
			ycl=ycl.substring(0,ycl.length()-1);	
			ycl=firstInto(ycl);
		}else if(arr.contains(ff)&&ycl.length()>0){
			ycl=ycl.substring(1,ycl.length());
			ycl=firstInto(ycl);
		}
		
		if("".equals(ycl)||ycl==null){
			return "";
		}else{
			ycl=ycl.trim();
		}	
	 
	 return	ycl; 
	}
	
	
	public  void combiantion(String chs[]){  
        if(chs==null||chs.length==0){  
            return ;  
        }  
        List<String> list=new ArrayList<>();  
        for(int i=1;i<=chs.length;i++){  
            combine(chs,0,i,list);  
        }  
    }  
    //从字符数组中第begin个字符开始挑选number个字符加入list中  
    public  void combine(String []cs,int begin,int number,List<String> list){
    	
        if(number==0){             
            listend.add(list.toString());
            return ;  
        }  
        if(begin==cs.length){  
            return;  
        }  
        list.add(cs[begin]);  
        combine(cs,begin+1,number-1,list);  
        list.remove(cs[begin]);  
        combine(cs,begin+1,number,list);  
    } 
    
    /**
     * 去除括号逻辑
     * 输出字符串时，当字符串前后带有不是成对出现的符号时，将其去除，如“（门诊慢性病），”在去掉符号后为门诊慢性病；如果字符串在括号中，则将括号去掉，如（10）要将括号去掉，变成10。
     * @param ss
     * @return
     */
    public  String kuoh(String ss){

		
		String vv=ss;
		if(vv.length()==0){
			return "";
		}
		
		//字符串中只含有（）[]{} 空格符 tab符 换行符，则返回空
		boolean sf=vv.matches("？?、，,[(（)）\\[\\]`_~】【<>{} \\s\r\n]*");
		if(sf==true){
			return "";
		}
		String q=vv.substring(0, 1);
		String h=vv.substring(ss.length()-1);
		
		
		if(("(".equals(q)||"（".equals(q))&&vv.contains(")")==false&&vv.contains("）")==false){
			vv=kuoh(vv.substring(1));
		}else if((")".equals(h)||"）".equals(h))&&vv.contains("(")==false&&vv.contains("（")==false){
			vv=kuoh(vv.substring(0,vv.length()-1));
		}else if(("(".equals(q)||"（".equals(q))&&(")".equals(h)||"）".equals(h))){
			int qkh=-1;
			int hkh=-1;
			for(int i=1;i<vv.length()-1;i++){
				String xvv=String.valueOf(vv.charAt(i));
				if("(".equals(xvv)||"（".equals(xvv)){
					qkh=i;
				}else if(")".equals(xvv)||"）".equals(xvv)){
					hkh=i;
				}
				
				if(qkh!=-1&&hkh!=-1){
					break;
				}
			}
			
			if(qkh<hkh||qkh==hkh||(qkh==-1&&hkh!=-1)||(qkh!=-1&&hkh==-1)){
				vv=kuoh(vv.substring(1,vv.length()-1));
			}
		}else if("[".equals(q)&&vv.contains("]")==false){
			vv=kuoh(vv.substring(1));
		}else if("]".equals(h)&&vv.contains("[")==false){
			vv=kuoh(vv.substring(0,vv.length()-1));
		}else if("[".equals(q)&&"]".equals(h)){
			int qkh=-1;
			int hkh=-1;
			for(int i=1;i<vv.length()-1;i++){
				String xvv=String.valueOf(vv.charAt(i));
				if("[".equals(xvv)){
					qkh=i;
				}else if("]".equals(xvv)){
					hkh=i;
				}
				
				if(qkh!=-1&&hkh!=-1){
					break;
				}
			}
			
			if(qkh<hkh||qkh==hkh||(qkh==-1&&hkh!=-1)||(qkh!=-1&&hkh==-1)){
				vv=kuoh(vv.substring(1,vv.length()-1));
			}
		}else if(q.matches("[\ua000-\ufde0]")==true){
			vv=kuoh(vv.substring(1));
		}else if(h.matches("[\ua000-\ufde0]")==true){
			vv=kuoh(vv.substring(0,vv.length()-1));
		}else if("(".equals(h)||"（".equals(h)){
			vv=kuoh(vv.substring(0,vv.length()-1));
		}else if(")".equals(q)||"）".equals(q)){
			vv=kuoh(vv.substring(1));
		}
		
		return vv;
	
	}
    
    
    /**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public  String ToSBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
    }
    
    /**
     * 字符串中包含字母和数字的个数
     * @param str
     * @return
     */
    public  int countInnerStr( String str) {

    	int count = 0;
    	String patternStr="[A-Za-z0-9]";    	
    	 Pattern r = Pattern.compile(patternStr);

    	 Matcher m = r.matcher(str);

    	while (m.find()) {

    	count++;

    	}

    	return count;

    	}

	
}

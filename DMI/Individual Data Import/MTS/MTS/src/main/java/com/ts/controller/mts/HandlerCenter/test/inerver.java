package com.ts.controller.mts.HandlerCenter.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfigAdd;
import com.ts.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("inerver")
public class inerver extends MagazineHandler
{

	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mca;

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
   
    	String status="0";
		String inver="0";
		String datavalue=entity.getString("originalData");
		String area=entity.getString("area");
		String cluster=entity.getString("cluster");
		String exceMemid=entity.getString("exceMemid");
		int number =entity.getInt("number");    
		String key="";
		if(exceMemid==""||"".equals(exceMemid)||exceMemid==null){
			key=area+"_"+cluster;
		}else{
			key=area+"_"+cluster+"_"+exceMemid;
		}
		MatchRuleDetailT mrdt=CacheManager.getMatchRuleDetail(key);
		String inverStrs=CacheManager.getRegex("inverStrs");//获取无法标化缓存串
		String doub =entity.getString("doub");           //怀疑标识
		JSONObject NLPVALUE=new JSONObject();
		

		Pattern p = Pattern.compile(inverStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {	
			String termi = m.group(1);
			datavalue = datavalue.replace(termi, "");		
				
		}
		if(datavalue==null||datavalue==""||"".equals(datavalue)){
			
			status="1";
			inver="1";
			
		}else{
			datavalue=mca.standarde(datavalue);
			
			if(datavalue==null||datavalue==""||"".equals(datavalue)){
				
				inver="1";
				status="1";	
			}else{
				inver="0";
			}
		}
		
		entity.put("status", status);
		
		String falseto=mrdt.getFAILURE_REDIRECT_TO();
		String sucessto=mrdt.getSUCCESS_REDIRECT_TO();
		String addRs="0";
		if("1".equals(status)&&(sucessto==null||sucessto==""||"".equals(sucessto))){
			addRs="1";
		}else if("0".equals(status)&&(falseto==null||falseto==""||"".equals(falseto))){
			addRs="1";
		}
		
		if("1".equals(addRs)){
			
			if("1".equals(inver)){
	       	    NLPVALUE.put(datavalue, "无法标化");
	       	
			}else{
				 NLPVALUE.put(datavalue, "none");
			}
	        
			JSONObject rsEntity = new JSONObject();
	        rsEntity.put("T_ID", entity.get("originalCode"));
	        rsEntity.put("NLP", datavalue);        
	        rsEntity.put("NLPVALUE", "无法标化");
	        rsEntity.put("status", status);
	        rsEntity.put("doub", doub);
	        
	        AddStandardByRs(rsEntity);
	        
		}else if("1".equals(status)){
			
			JSONArray rsDatas=entity.getJSONArray("rsDatas");
			JSONObject json = new JSONObject();
			json.put("nubmer", number);
			json.put("subRsData", "无法标化");
			
			rsDatas.add(json);	
		}
		
        
        
        System.out.println(Thread.currentThread() + ",inerver - BusinessHandler" + entity.toString());
        
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",inerver - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",inerver - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }
}


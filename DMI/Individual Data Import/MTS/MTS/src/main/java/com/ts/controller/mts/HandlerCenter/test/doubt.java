package com.ts.controller.mts.HandlerCenter.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.util.StringUtil;

import net.sf.json.JSONObject;

@Service("doubt")
public class doubt extends MagazineHandler
{

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
    	
		JSONObject jsonstrb = new JSONObject(); 
		String datavalue=entity.getString("originalData");
		String val="0";		
		
		String doubtsStrs=CacheManager.getRegex("doubtsStrs");
		
		Pattern p = Pattern.compile(doubtsStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {			
			val="1";	
			break;		
		}
			  
		 jsonstrb.put("status", "1");
		 jsonstrb.put("doubt", val);	
		 
		 JSONObject rsEntity = new JSONObject();
	     rsEntity.put(datavalue,jsonstrb);      
	     AddStandardByRs(rsEntity);     
		
		 
        entity.put("status", "1");
        System.out.println(Thread.currentThread() + ",doubt - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",doubt - BeforeBusinessHandler" + beforeEntity.toString());
        return null;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",doubt - AfterTBusinessHandler " + afterEntity.toString());
        return null;
    }

}

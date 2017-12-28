package com.ts.controller.mts.HandlerCenter.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfigAdd;
import com.ts.util.StringUtil;

import net.sf.json.JSONObject;
@Service("special")
public class special extends MagazineHandler
{

	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mca;

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
   
       
		String datavalue=entity.getString("originalData");
		String datavaluef=entity.getString("standardType");	
		String dataClass=entity.getString("cluster");
		String areaid=entity.getString("area");
		String status="0";
		String inver="0";
		String specialStrs=CacheManager.getRegex("specialStrs");
	
		
		JSONObject jsonstra = new JSONObject(); 
		String value="";
		
		
		Pattern p = Pattern.compile(specialStrs);
		Matcher m = p.matcher(StringUtil.full2Half(datavalue).toUpperCase());
		while (m.find()) {	
			String term = m.group(1);
			datavalue=datavalue.replace(term, "");		
		}
		datavalue=mca.standarde(datavalue);
		String datan=datavaluef+"@#&"+datavalue;
		jsonstra.put("data", datan);
		String nstr=jsonstra.toString();
		String jsonstr="";
		JSONObject jsonstrb=JSONObject.fromObject(jsonstr);
		if(jsonstrb.get("status")=="0"||"0".equals(jsonstrb.get("status"))){
			jsonstrb.put("SPEC", "0");
		}else{
			jsonstrb.put("SPEC", "1");
		}
		 value= jsonstrb.toString();
		
        System.out.println(Thread.currentThread() + ",special - BusinessHandler" + entity.toString());
        
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",special - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",special - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }
}


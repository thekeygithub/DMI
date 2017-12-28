package com.ts.controller.mts.HandlerCenter.test;



import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfigAdd;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("nLmatch")
public class nLmatch extends MagazineHandler
{

	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mca;

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
    	
		
		JSONObject jsontemp=new JSONObject();	
		
				
		String data =entity.getString("originalData");     //源数据		
		JSONArray currOrigDatas=entity.getJSONArray("currOrigDatas");   //当前数据列表
		
		//数据清洗后，存入currOrigDatas
		data=mca.standarde(data);
		jsontemp.put("currOrigData", entity.getString("originalData"));
		jsontemp.put("currOrigDataClean", data);
		jsontemp.put("number", 1);
		currOrigDatas.add(jsontemp);
		
		
		       
        System.out.println(Thread.currentThread() + ",nLmatch - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",nLmatch - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
    	//按区域和聚类进行结果的选择或合并
    	String area =afterEntity.getString("area");           //区域
    	String cluster =afterEntity.getString("cluster");     //聚类
    	
    	if("410206".equals(area)&&"02".equals(cluster)){
    		//单个词
    		JSONObject rsEntity = new JSONObject();
            rsEntity.put("", afterEntity.get(""));
            rsEntity.put("", "");
            rsEntity.put("", "");
            rsEntity.put("", "");
            
            AddStandardByRs(rsEntity);
    	}
    	
    	      
       
        afterEntity.put("status", "1");
        System.out.println(Thread.currentThread() + ",nLmatch - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }
}


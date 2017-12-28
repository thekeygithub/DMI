package com.ts.controller.mts.HandlerCenter.test;



import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfigAdd;
import com.ts.util.StringUtil;
import com.ts.util.redis.IRedisUtil;
import com.ts.util.redis.RedisKeys;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("config")
public class config extends MagazineHandler
{

	@Resource(name = "mapConfigService")
	private MapConfigService mcs;
	@Resource(name = "MtsConfigAdd")
	private MtsConfigAdd mca;
	@Resource(name = "redisUtil")
	private IRedisUtil redisDao;

    @Override
    public JSONObject BusinessHandler(JSONObject entity)
    {
   
       
    	JSONObject json = new JSONObject();
		
		//1.获取数据	
		
		String classCode =entity.getString("cluster");                  //聚类	
	    JSONArray currOrigDatas=entity.getJSONArray("currOrigDatas");   //当前数据列表
	    int number =entity.getInt("number");                            //当前处理序号
	    JSONObject currOrigData =(JSONObject) currOrigDatas.get(number);//当前处理数据json
	    String data=currOrigData.getString("currOrigDataClean");        //清洗后数据
		String typeCode=entity.getString("standardType");               //标化类型code		
		String areaid=entity.getString("area");                         //区域
		JSONArray rsDatas=entity.getJSONArray("rsDatas");   //当前结果列表
		
		
		
		 
		//2.诊断源数据处理，忽略药品拼接		
		String mdkey=mca.trimUnicode(StringUtil.full2Half(data).toUpperCase().trim()) ;
		
		String key=areaid+"#"+classCode+"#"+typeCode+"#"+mdkey;		
		 
		
		//3.从redis中获取结果	
		 String value=redisDao.get(RedisKeys.SYSMTS, key);
		 
		 
		 json.put("nubmer", number);
		 json.put("subOriginalData", "0");
			
		if(value==null){			
			json.put("subRsData", "none");
		}else{ //4.保存成json格式	
			json.put("subRsData", value);
		}	
		
		rsDatas.add(json);	
		
        System.out.println(Thread.currentThread() + ",config - BusinessHandler" + entity.toString());
        
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",config - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
        System.out.println(Thread.currentThread() + ",config - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }
}


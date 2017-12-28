package com.ts.controller.mts.HandlerCenter.test;



import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.MagazineHandler;
import com.ts.entity.mts.MatchRuleDetailT;
import com.ts.service.mts.MapConfigService;
import com.ts.service.mts.MtsConfigAdd;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("match")
public class match extends MagazineHandler
{

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
		
		       
        System.out.println(Thread.currentThread() + ",match - BusinessHandler" + entity.toString());
        return entity;
    }

    @Override
    public JSONObject BeforeBusinessHandler(JSONObject beforeEntity)
    {
        System.out.println(Thread.currentThread() + ",match - BeforeBusinessHandler" + beforeEntity.toString());
        return beforeEntity;
    }

    @Override
    public JSONObject AfterTBusinessHandler(JSONObject afterEntity)
    {
    	
    	
    	
    	String area =afterEntity.getString("area");           //区域
    	String cluster =afterEntity.getString("cluster");     //聚类
    	JSONArray currOrigDatas=afterEntity.getJSONArray("currOrigDatas");   //源数据列表
    	JSONArray rsDatas=afterEntity.getJSONArray("rsDatas");   //结果数据列表
    	String doub =afterEntity.getString("doub");           //怀疑标识
    	String status =afterEntity.getString("status");           //状态标识
    
    	
    	
		String exceMemid=afterEntity.getString("exceMemid");
		String key="";
		if(exceMemid==""||"".equals(exceMemid)||exceMemid==null){
			key=area+"_"+cluster;
		}else{
			key=area+"_"+cluster+"_"+exceMemid;
		}
		MatchRuleDetailT mrdt=CacheManager.getMatchRuleDetail(key);
		String falseto=mrdt.getFAILURE_REDIRECT_TO();
		String sucessto=mrdt.getSUCCESS_REDIRECT_TO();
		String addRs="0";
		if("1".equals(status)&&(sucessto==null||sucessto==""||"".equals(sucessto))){
			addRs="1";
		}else if("0".equals(status)&&(falseto==null||falseto==""||"".equals(falseto))){
			addRs="1";
		}
		
		if("1".equals(addRs)){
	    	String NLP="";
			JSONObject NLPVALUE=new JSONObject();
			JSONObject js=new JSONObject();  	
			
			//按区域和聚类整理结果列表
	    	if("410206".equals(area)&&"02".equals(cluster)){     		
	    		
	    		for(int i=0;i<rsDatas.size();i++){
	    			JSONObject jo=(JSONObject) rsDatas.get(i);
	    			int num=jo.getInt("nubmer");
	    			String subRsData=jo.getString("subRsData");
	    			String result=(String) js.get(num);
	    			if("".equals(result)||result==null||result==""){
	    				js.put(num, subRsData);
	    			}else{
	    				String[] subval=subRsData.split("@#&");
						String subvalType=subval[subval.length-1];
						String[] val=result.split("@#&");
						String valType=val[val.length-1];
						
						//概念转换和诊断同时匹配上，留概念转换
						if("概念转换".equals(subvalType)&&"诊断".equals(valType)){
							js.put(num, subRsData);
						}
	    				
	    			}	
	    		}
	    		
	    	}
	    	
	    	
	    	//将整理好的结果列表按字段存储
	    	for(int j=0;j<currOrigDatas.size();j++){
				JSONObject joOrig=(JSONObject) currOrigDatas.get(j);
				String currOrigData=joOrig.getString("currOrigData");
				if("".equals(NLP)){
					NLP=currOrigData;
				}else{
					NLP=NLP+"；"+currOrigData;
				}
				if("".equals(js.get(j))){
					NLPVALUE.put(currOrigData, "none");
					status="0";
				}else{
					NLPVALUE.put(currOrigData, js.get(j));
				}
			}
	    	
	    	//入队列
			JSONObject rsEntity = new JSONObject();
	        rsEntity.put("T_ID", afterEntity.get("originalCode"));
	        rsEntity.put("NLP", NLP);
	        rsEntity.put("NLPVALUE", NLPVALUE);
	        rsEntity.put("status", status);
	        rsEntity.put("doub", doub);
	        
	        AddStandardByRs(rsEntity);
		}
    	
        System.out.println(Thread.currentThread() + ",match - AfterTBusinessHandler " + afterEntity.toString());
        return afterEntity;
    }
}


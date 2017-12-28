package com.ts.controller.mts.HandlerCenter.HandlerManager.Impl;

import com.ts.controller.mts.HandlerCenter.CacheUtil.CacheManager;
import com.ts.controller.mts.HandlerCenter.HandlerManager.IMagazineHandler;
import com.ts.controller.mts.HandlerCenter.QueueManager.QueueQuestionnaire;
import com.ts.controller.mts.HandlerCenter.QueueManager.QueueStandardByRs;
import com.ts.controller.mts.HandlerCenter.TemplateManager.Impl.ToolTempLate;
import com.ts.entity.mts.MtsToolkitT;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 工具包管理者
 * @author autumn
 *
 */
public abstract class MagazineHandler implements  IMagazineHandler
{

    @Override
    public JSONObject Toolhandler(JSONObject entity)
    {
        String applyMethod = entity.getString("flowApplyMethod");
       //此处参考处理流程中的数据对象
        JSONArray currOrigDatas = entity.getJSONArray("currOrigDatas");
        JSONArray outValue = tool(applyMethod ,currOrigDatas);
        
        if(outValue != null && !"".equals(outValue)&&!outValue.isEmpty())
        {
        	entity.put("currOrigDatas", outValue);
        }
        return BusinessHandler(entity);
    }
    
    public JSONObject ToolhandlerBefor(JSONObject entity)
    {
     
        String applyMethod = entity.getString("flowBeforApplyMethod");
        JSONArray currOrigDatas = entity.getJSONArray("currOrigDatas");
        JSONArray outValue = tool(applyMethod ,currOrigDatas);
        if(outValue != null && !"".equals(outValue)&&!outValue.isEmpty())
        {
         
        	entity.put("currOrigDatas", outValue);
        }
        return BeforeBusinessHandler(entity);
    }
    
    public JSONObject ToolhandlerAfter(JSONObject entity)
    {
    	String applyMethod = entity.getString("flowAfterApplyMethod");
        JSONArray currOrigDatas = entity.getJSONArray("currOrigDatas");
        JSONArray outValue = tool(applyMethod ,currOrigDatas);
        if(outValue != null && !"".equals(outValue)&&!outValue.isEmpty())
        {
        	entity.put("currOrigDatas", outValue);
        }
        
        return AfterTBusinessHandler(entity);
    }
    
    /**
     * 获取 执行工具包程序 
     * @param applyMethod  执行名称。
     * @return
     */
    private JSONArray tool(String applyMethod , JSONArray currOrigData)
    {
        try
        {
            if((applyMethod == null || "".equals(applyMethod)) || (currOrigData == null || "".equals(currOrigData)||currOrigData.isEmpty())) return null;
            
            MtsToolkitT tool=CacheManager.getTool(applyMethod);
            if(tool == null ||tool.getTOOLPATHS()==null||"".equals(tool.getTOOLPATHS()) )
            {
                return null;
            }
            JSONObject val = new JSONObject();
            val.put("ToolValue", currOrigData);
            
            for (String className : tool.getTOOLPATHS().split(",")) {//遍历所有的工具
            	
            	Class<ToolTempLate>  clazz =  (Class<ToolTempLate>) Class.forName(className);
                ToolTempLate exceEntity = clazz.newInstance();
                
                val =  exceEntity.tool(val);//执行方法后属性ToolValue值变成最新的
			}
            
            return val.getJSONArray("ToolValue");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    @Override
    public void AddStandardByRs(JSONObject entity)
    {
        QueueStandardByRs.addQueueStandar(entity);
    }
    
    @Override
    public void AddQuestionaire(JSONObject entity)
    {
        QueueQuestionnaire.setQueueStandar(entity);
    }
    
    
    
}

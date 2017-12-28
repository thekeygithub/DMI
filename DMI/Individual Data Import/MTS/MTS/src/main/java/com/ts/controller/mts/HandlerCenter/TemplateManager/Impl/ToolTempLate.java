package com.ts.controller.mts.HandlerCenter.TemplateManager.Impl;

import com.ts.controller.mts.HandlerCenter.TemplateManager.IToolTempLate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 工具处理魔板
 * @author autumn
 *
 */
public abstract class ToolTempLate implements IToolTempLate
{
    

    /**
     * 工具执行方法的
     */
    @Override
    public JSONObject tool(JSONObject val)
    {
        
        JSONArray value = val.getJSONArray("ToolValue");
        
        JSONArray outValue = this.toolExce(value);
        JSONObject outJson = new JSONObject();
        
        outJson.put("ToolValue", outValue);
        return outJson;
    }

    /**
     * 开发者 对 工具进行开发 
     * @param value
     * @return
     */
    public abstract  JSONArray toolExce(JSONArray value);
}

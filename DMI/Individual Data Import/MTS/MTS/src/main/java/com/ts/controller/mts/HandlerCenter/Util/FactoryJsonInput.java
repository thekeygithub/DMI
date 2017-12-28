package com.ts.controller.mts.HandlerCenter.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 输入 json 构建 
 * @author autumn
 *
 */
public final class FactoryJsonInput
{
    
   
    private FactoryJsonInput() { }
    
    /**
     * 构建 数据集合
     *    装载 待标化 数据队列  
     * @param originalData 需要标准化的词
     * @return
     */
    public static JSONObject JsonInputByOriginal(String  originalData,String originalCode)
    {
        JSONObject   value = new JSONObject();
        JSONObject  subobject = new JSONObject();
        JSONArray  jsonarray = new JSONArray();
        subobject.put("currOrigData", originalData);
        subobject.put("number", "1");
        subobject.put("status", "0");
        jsonarray.add(subobject);
        
        value.put("originalData", originalData);
        value.put("currOrigDatas", jsonarray);
        value.put("originalCode", originalCode);
        return value;
    }
    
    /**
     * 构架数据集合对象
     *   执行单独标化方法使用
     * @param areaid
     * @param dataClassCode
     * @param originalData
     * @param originalCode
     * @return
     */
    public static JSONObject JsonInputByAll(String areaid , String dataClassCode,String originalData ,String originalCode)
    {
        JSONObject value = JsonInputByOriginal(originalData,originalCode);
        
        value.put("area", areaid);
        value.put("cluster", dataClassCode);
        return value;
    }
    
    
}

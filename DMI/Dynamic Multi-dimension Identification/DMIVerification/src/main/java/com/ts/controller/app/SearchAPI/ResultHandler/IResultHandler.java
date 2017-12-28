package com.ts.controller.app.SearchAPI.ResultHandler;




import net.sf.json.JSONObject;

/**
 * 处理返回结果接口
 * @author autumn
 *
 */
public interface IResultHandler
{

    /**
     *  获得返回结果
     * @return
     */
    public String  getRSHandler(JSONObject value);
    
}

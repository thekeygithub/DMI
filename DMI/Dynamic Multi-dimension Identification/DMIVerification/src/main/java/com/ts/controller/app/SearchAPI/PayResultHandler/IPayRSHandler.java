package com.ts.controller.app.SearchAPI.PayResultHandler;




import net.sf.json.JSONObject;

/**
 * 处理返回结果接口
 * @author autumn
 *
 */
public interface IPayRSHandler
{

    /**
     *  获得返回结果
     * @return
     */
    public String  getPayRSHandler(JSONObject value);
    
}

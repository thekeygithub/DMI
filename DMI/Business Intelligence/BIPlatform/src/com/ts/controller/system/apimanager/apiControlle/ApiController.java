package com.ts.controller.system.apimanager.apiControlle;



import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.util.FormatUtil;
import com.ts.util.PageData;
import com.ts.util.SSLClient;

import net.sf.json.JSONObject;



@Controller
@RequestMapping(value= "/http")
public class ApiController extends BaseController{
	

	
	/**去API接口测试页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/httpRequester")
	@Rights(code="http/httpRequester")
	public ModelAndView HttpRequester() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/apimanager/httpRequester");
		mv.addObject("pd", pd);
		return mv;
	}

    /**
     * 模拟各种Http请求
     * @param bo
     * @return
     */
    @RequestMapping(value= "/https")
    @Rights(code="http/https")
	  public ModelAndView https() throws Exception{
			logBefore(logger, "创建HttpClient客户端");
    		ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			pd = this.getPageData();
			String url = pd.getString("url"); 
			String charset = pd.getString("charset");
			String type = pd.getString("type");
			String res = "`";    
			String val = "";
			Map<String, String> retMap = null;
			if("Post".equals(type)){
				JSONObject json = JSONObject.fromObject(pd.get("keys"));
				 retMap = this.doPost(url,json,charset);
				 val =  retMap.get("ret").toString();
				 res = retMap.get("res").toString();	
			}else if("Get".equals(type)){
				retMap = this.doGet(url, charset);
				val =  retMap.get("ret").toString();
				res = retMap.get("res").toString();
			}
		    
		    pd.put("values", FormatUtil.formatJson(val));
		    pd.put("response", res);
		    mv.setViewName("system/apimanager/httpRequester");
			mv.addObject("pd", pd);
			return mv;
		  }  
    /** 
     * 发送Post请求 
     * @param url       链接地址 
     * @param charset   字符编码， 
     * @param json    	参数， 
     * @return 
     */ 
    public Map<String, String> doPost(String url, JSONObject json, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		Map<String, String> result = null;
		Map<String,String> map = new HashMap<String,String>();
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			// 设置参数
			StringEntity se = new StringEntity(json.toString());
			se.setContentEncoding(charset);
			se.setContentType("application/json");// 发送json数据需要设置contentType
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					map.put("ret", EntityUtils.toString(resEntity, charset));
					map.put("res", response.toString());
					result = map;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result; 
	}
    
    /** 
     * 发送get请求 
     * @param url       链接地址 
     * @param charset   字符编码
     * @return 
     */  
    public Map<String, String> doGet(String url,String charset){   
        HttpClient httpClient = null;  
        HttpGet httpGet= null;  
        Map<String,String> result = null;  
        Map<String,String> map = new HashMap<String,String>(); 
        try {  
            httpClient = new SSLClient();  
            httpGet = new HttpGet(url);  
              
            HttpResponse response = httpClient.execute(httpGet);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    map.put("ret", EntityUtils.toString(resEntity,charset));
                    map.put("res", response.toString());
                	result = map;  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return result;  
    }  
}

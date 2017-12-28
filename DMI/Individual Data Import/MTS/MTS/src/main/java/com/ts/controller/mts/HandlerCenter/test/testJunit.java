package com.ts.controller.mts.HandlerCenter.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ts.controller.mts.HandlerCenter.HandlerManager.Impl.FlowHandler;
import com.ts.controller.mts.HandlerCenter.Util.FactoryJsonInput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.ts.controller.mts.HandlerCenter.HandlerManager.IFlowHandler;
//
//import net.sf.json.JSONObject;
 
public class testJunit
{

//    ApplicationContext  factory;
//    @Before
//    public void before()
//    {
//        System.out.println("-----------------");
//        factory=new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-*.xml");
//    }
//    
//    @Test
//    public void test()
//    {
//        IFlowHandler fh = (IFlowHandler)factory.getBean("flowHandler");
//        JSONObject  rsEntity = new JSONObject();
//        fh.SingletonHandler(rsEntity);
//        
////        fail("Not yet implemented");
//    }

    
//    public static void main(String[] args)
//    {
//        String str = "abcdefghij";
//        String[] s = new String[]{"b","g","i","j"};
//        //
//        for(int i = 0 ;i<s.length ; i++)
//        {
//            System.out.println(zh(s ,  i + 1 ));
//        }
//    }
//    
//    public static String  zh(String[] ss , int count)
//    {
//        String rs ="";
//        for(int i = 0 ; i < count ; i++)
//        {
//            rs = ss[i];
//            rs += zh(ss , i);
//        }
//        return rs;
//    }
    
    public static void combiantion(String chs[]){  
        if(chs==null||chs.length==0){  
            return ;  
        }  
        List<String> list=new ArrayList<>();  
        for(int i=1;i<=chs.length;i++){  
            combine(chs,0,i,list);  
        }  
    }  
    //从字符数组中第begin个字符开始挑选number个字符加入list中  
    public static void combine(String []cs,int begin,int number,List<String> list){  
        if(number==0){  
            System.out.println(list.toString());  
            return ;  
        }  
        if(begin==cs.length){  
            return;  
        }  
        list.add(cs[begin]);  
        combine(cs,begin+1,number-1,list);  
        list.remove(cs[begin]);  
        combine(cs,begin+1,number,list);  
    }  
    
    public static void main(String args[]){  
        //String chs[]={"a","b","c","d"};  
        //combiantion(chs);
        try
        {
        	FlowHandler fh = new FlowHandler();
        	fh.multipleHandler("", "");
        	fh.SingletonHandler(FactoryJsonInput.JsonInputByAll("410201", "02", "支气管和肺良性肿瘤", ""));
        	
        	
        }
        catch(Exception e )
        {
        	e.printStackTrace();
        }
    	
    	
//    	
//        Date d = new  Date();
//        Long l = 1511791821L;
//        d.setTime(l);
//        System.out.println(d.toString());
//        System.out.println(d.getTime());
//        
//        
//        
//        if(true)return;
//        
//        JSONObject jsonObject = new JSONObject();
//        JSONArray  jsonArray = new JSONArray();
//        for(int  i = 0  ; i < 10 ; i++)
//        {
//            JSONObject subJson = new JSONObject();
//            subJson.put("subName", "你好 " + i );
//            jsonArray.add(subJson);
//        }
//        System.out.println(jsonArray.toString());
//        
//        jsonObject.put("json1", "xxxxx");
//        jsonObject.put("arrays", jsonArray);
//        
//        System.out.println("----:" + jsonObject.toString());
        
    }  
}

package com.models.cloud.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AppidUtils {

    /**
     * 随机生成数字
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInteger(int min,int max){
    	 Random random = new Random();
         int s = random.nextInt(max)%(max-min+1) + min;
         return s;
    }
    
    /**
     * 获取appid
     * @return
     */
    public static String getAppid(){
    	Date date = new Date();
    	String yyMMdd = new SimpleDateFormat("yyMMdd").format(date);
    	String HHmmss = new SimpleDateFormat("HHmmss").format(date);
    	String SSS = new SimpleDateFormat("SSS").format(date);
    	return yyMMdd+getRandomInteger(10,99)+HHmmss+getRandomInteger(10,99)+SSS+getRandomInteger(100,999);
    }
    
    public static void main(String[] args) {
    	for(int i = 0;i < 100 ;i ++){
    		
    		System.out.println(AppidUtils.getAppid());
    	}
	}
    
}

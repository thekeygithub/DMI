package com.dhcc.ts.library.translate;

import java.util.Map;

import net.sf.json.JSONObject;

public class Main {
	  public static void main(String[] args) {
		  	TranslateInit api = new TranslateInit();

	        String query = "年后";
	        Map<String,String> result = api.getTransResult(query);
	        System.out.println("resultZh---"+result.get("resultZh"));
	        System.out.println("resultEn---"+result.get("resultEn"));
	        System.out.println("error---"+result.get("resultCode"));
	        
	    }
}

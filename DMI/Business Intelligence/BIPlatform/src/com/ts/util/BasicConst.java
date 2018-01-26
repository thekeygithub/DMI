package com.ts.util;

import java.util.ResourceBundle;

public class BasicConst {
	public static int DATABASE_TYPE = 0;//数据库类型，0为oracle，1为mysql
	 
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("dbconfig");
		if (bundle == null) {
			throw new IllegalArgumentException("[dbconfig.properties] is not found!");
		}
		String url = bundle.getString("url");
		if(url!=null && url.contains("mysql")){
			BasicConst.DATABASE_TYPE = 1;
		}
	}
}

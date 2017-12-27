package com.ebmi.std.common.finalkey;

import org.apache.commons.lang3.StringUtils;

import com.ebmi.std.common.util.Propertie;

/**
 * 
 * @Description: 常量
 * 
 * 重要备注： 常量配置文件中不允许出value等于数字0
 * 
 * @ClassName: Constant 
 * @author: zhengping.hu
 * @date: 2015年11月30日 下午2:04:21
 */
public class Constant {
	
	/**
	 * 外部接口连接地址
	 */
	public static String WEBSERVICE_URL="";	
	
	public static String ISTEST_FLAG="";	
	
	static{
		if(StringUtils.isBlank(WEBSERVICE_URL)){
			WEBSERVICE_URL=Propertie.APPLICATION.value("WEBSERVICE_URL");
		}
		if(StringUtils.isBlank(ISTEST_FLAG)){
			ISTEST_FLAG=Propertie.APPLICATION.value("ISTEST_FLAG");
			System.out.println("ISTEST_FLAG>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+ISTEST_FLAG);
		}
	}
}
